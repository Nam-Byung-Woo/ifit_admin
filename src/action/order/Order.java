package action.order;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import util.config.*;
import util.system.AESCrypto;
import util.system.MySqlFunction;
import util.system.StringUtil;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import dao.IfitDAO;
import dto.EachOrderDTO;
import dto.PayListDTO;
import dto.SizeListDTO;
import lombok.Data;

@Data
public class Order extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FormValidate formValidate = new FormValidate();
	private Code code = new Code();
	private AlertMessage alertMessage = new AlertMessage();
	private IfitDAO payListDAO;
	private IfitDAO eachOrderDAO;
	private PayListDTO payListDTO;
	private EachOrderDTO eachOrderDTO;
	private List<PayListDTO> dataList;
	private List<EachOrderDTO> eachOrderList;
	private Paging paging = new Paging();
	private Map<String, Object> paramMap = new HashMap<String, Object>();
	private Map<String, Object> searchMap = new HashMap<String, Object>();
	private Map<String, Object> whereMap = new HashMap<String, Object>();
	private Map<String, Object> validateMsgMap = new HashMap<String, Object>();
	private Map<String, Object> excelChainMap = new HashMap<String, Object>();
	private String rtnString;
	
	private int pageNum;				//	페이지번호
	private String pagingHTML = "";
	
	private int searchCol = 0;			// 	검색분류
    private String searchVal = "";		// 	검색어
    private int sortCol = 0;											// 	정렬 컬럼
    private String sortVal = "";										// 	정렬 내용
    private int countPerPage = Code.deFaultcountPerPage;		//	한페이지에 보일 리스트 수
    private int totalCount = 0;
	
    private HttpServletRequest request = ServletActionContext.getRequest();
    private String queryIncode = "";						//	쿼리스트링(인코딩)
    private String queryDecode = "";						//	쿼리스트링(디코딩)
    
	private int seq;
	private int tabID;
	private int admin_seq;
	private String admin_name;
	private String date_start;
	private String date_end;
	private String[] listItemCheck;		// 복수선택
	
	private String isUpdateMode = "";					// 편집모드
	private String isExcelDownloadMode = "";	
	
	private LinkedHashMap tabIDMap = new LinkedHashMap() {{
    	put(0,0);					// 기본값
    	put(1,1);					
    	put(2,2);	
    	put(3,3);
    }};
	
    private LinkedHashMap searchColKindMap = new LinkedHashMap() {{	// 검색 가능한 종류
    	// db상의 name과 매칭 
    	put(0,"PAY.user_id");					// 기본값
    	put(1,"PAY.user_id");					// 제목 검색	
    }};
    
    private LinkedHashMap sortColKindMap = new LinkedHashMap() {{	// 정렬항목 정의
    	// db상의 name과 매칭
		put(0,"PAY.pay_seq");		// 기본값 정렬
		put(1,"PAY.pay_seq");		// 제목 정렬
	}};
	
	public Order() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	    this.eachOrderDTO = new EachOrderDTO();
	}
	
	public void setServletRequest(HttpServletRequest request) { 
		this.request = request; 
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	//	요소 초기화 및 세팅
	public void init(){
		this.payListDAO = (IfitDAO)this.wac.getBean("payList");
		this.eachOrderDAO = (IfitDAO)this.wac.getBean("eachOrder");

		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
		
		this.rtnString = "";
	}
	
	public String getList() throws Exception{
		init();
		
		if(this.searchColKindMap.containsKey(this.searchCol)){
			this.searchMap.put(this.searchColKindMap.get(this.searchCol).toString(), this.searchVal);
		}
		this.tabID = this.tabIDMap.containsKey(this.tabID) ? this.tabID : 0;
		
		if(!StringUtil.stringToBool(StringUtil.isNullOrSpace((String)session.get("isAdmin"),"").trim())){
			// 업체 접근시
			this.whereMap.put("EO.admin_seq", session.get("admin_seq"));
		}else if(this.admin_seq != 0){
			// 업체 선택시
			this.whereMap.put("EO.admin_seq", this.admin_seq);
		}
		
		if(!(StringUtil.isNullOrSpace(this.date_start,"").trim()).equals("")){
			this.paramMap.put("date_start",this.date_start);
		}
		
		if(!(StringUtil.isNullOrSpace(this.date_end,"").trim()).equals("")){
			this.paramMap.put("date_end",this.date_end);
		}
		
		this.paramMap.put("searchMap", this.searchMap);
		this.paramMap.put("whereMap", this.whereMap);
		paramMap.put("isCount",true);
		paramMap.put("tabID",this.tabID);
		this.totalCount = (int)this.payListDAO.getList(paramMap);
		
		this.pageNum = this.pageNum == 0 || (this.pageNum*this.countPerPage>totalCount && this.pageNum*this.countPerPage-totalCount >= this.countPerPage)  ? 1  : this.pageNum;
		this.sortCol = this.sortColKindMap.containsKey(this.sortCol) ? this.sortCol : 0;
		this.sortVal = this.sortVal.equals("ASC") ? "ASC" : "DESC";
		paramMap.put("isCount",false);
		paramMap.put("pageNum",Boolean.valueOf(this.isExcelDownloadMode).booleanValue() ? 0 : this.pageNum);
		paramMap.put("countPerPage",this.countPerPage);
		paramMap.put("tabID",this.tabID);
		this.paramMap.put("sortCol", this.sortColKindMap.get(this.sortCol));
		this.paramMap.put("sortVal", this.sortVal);
		
		this.dataList = (List<PayListDTO>)this.payListDAO.getList(paramMap);
		
		getEachOrderData();
		
		this.queryIncode = StringUtil.stringToHex(this.request.getQueryString()==null ? "" : this.request.getQueryString());
		this.pagingHTML = paging.getPaging(totalCount, this.pageNum, this.countPerPage);
		
		if(Boolean.valueOf(this.isExcelDownloadMode).booleanValue()){
			this.excelChainMap.put("type", "orderList");
			this.excelChainMap.put("fileName", "판매현황" + (this.admin_seq != 0 ? "_" + this.admin_name : "") + ".xlsx");
			this.excelChainMap.put("data", this.dataList);
			return "EXCEL";
		}else{
			return SUCCESS;
		}
	}
	
	public void getData(Map<String, Object> paramMap){
		init();
		
		this.whereMap.clear();
		if(paramMap.containsKey("seq")){
			this.whereMap.put("faq_seq", paramMap.get("seq"));
			this.paramMap.put("whereMap", this.whereMap);
		}
	}
	
	public void getEachOrderData(){
		Gson gson = new Gson();
		for(PayListDTO temp : this.dataList){
			this.whereMap.put("pay_seq", temp.getPay_seq());
			this.paramMap.put("whereMap", this.whereMap);
			this.paramMap.put("sortCol", "EO.order_seq");
			this.paramMap.put("sortVal", "DESC");
			this.paramMap.remove("searchMap");
			temp.setEachOrderList((List<EachOrderDTO>)this.eachOrderDAO.getList(paramMap));
			temp.setEachOrderListToJson(gson.toJson(temp.getEachOrderList()));
		}
	}
	
	public String getEditor() throws Exception{
		init();
		
		if(Boolean.valueOf(this.isUpdateMode).booleanValue()){
			this.paramMap.put("seq", this.seq);
//			this.eachOrderDTO = (EachOrderDTO) getData(this.paramMap);
		}
		
		this.queryDecode = StringUtil.hexToString(this.queryIncode);
		
		return SUCCESS;
	}
	
	public String writeAction() throws Exception {	
//		init();
//		
//		Gson gson = new Gson();
//		
//		this.title = StringUtil.isNullOrSpace(this.title,"").trim();
//		this.content = StringUtil.isNullOrSpace(this.content,"").trim();
//		paramMap.put("title", this.title);
//		paramMap.put("content", this.content);
//		
//		this.validateMsgMap = formValidate.faqEditorForm(paramMap);
//		paramMap.clear();
//		if(!(boolean)validateMsgMap.get("res")){
//			this.rtnString = gson.toJson(validateMsgMap);
//			return "validation";
//		}
//		
//		this.faqDTO.setTitle(this.title);
//		this.faqDTO.setContent(this.content);
//		
//		this.faqDAO.write(this.faqDTO);
//		
//		this.session.put("alertMsg", this.alertMessage.getFaqWriteOK());
//		this.context.setSession(this.session);
//		
		return SUCCESS;
	}
	
	public void deliveryOK(Map<String, Object> paramMap){
		init();
		
		this.eachOrderDTO.setState(2);
		this.eachOrderDTO.setDelivery_number(paramMap.get("delivery_number").toString());
		this.eachOrderDTO.setPay_seq(Integer.parseInt(paramMap.get("pay_seq").toString()));
		this.eachOrderDTO.setAdmin_seq(Integer.parseInt(session.get("admin_seq").toString()));
		
		this.eachOrderDAO.update(this.eachOrderDTO);
	}
	
	public String updateAction() throws Exception {	
		init();
		
		Gson gson = new Gson();
		
//		this.title = StringUtil.isNullOrSpace(this.title,"").trim();
//		this.content = StringUtil.isNullOrSpace(this.content,"").trim();
//		paramMap.put("title", this.title);
//		paramMap.put("content", this.content);
//		
//		this.validateMsgMap = formValidate.faqEditorForm(paramMap);
//		paramMap.clear();
//		if(!(boolean)validateMsgMap.get("res")){
//			this.rtnString = gson.toJson(validateMsgMap);
//			return "validation";
//		}
//		
//		this.faqDTO.setTitle(this.title);
//		this.faqDTO.setContent(this.content);
//		this.faqDTO.setFaq_seq(this.seq);
//		
//		this.faqDAO.update(this.faqDTO);
//		
//		this.session.put("alertMsg", this.alertMessage.getFaqUpdateOK());
//		this.context.setSession(this.session);
//		
		return SUCCESS;
	}
	
	public String deleteAction(){
		init();
//
//		this.paramMap.put("faq_seq", this.seq);
//		
//		this.faqDAO.delete(this.paramMap);
//		
//		this.session.put("alertMsg", this.alertMessage.getDeleteOK());
//		this.context.setSession(this.session);
		
		return SUCCESS;	
	}

}
