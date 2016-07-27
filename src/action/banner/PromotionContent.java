package action.banner;

import java.util.ArrayList;
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
import dto.PromotionListDTO;
import dto.PromotionMapDTO;
import lombok.Data;

@Data
public class PromotionContent extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FormValidate formValidate = new FormValidate();
	private Code code = new Code();
	private AlertMessage alertMessage = new AlertMessage();
	private IfitDAO promotionMapDAO;
	private IfitDAO promotionListDAO;
	private PromotionMapDTO promotionMapDTO;
	private List<PromotionMapDTO> dataList;
	private List<PromotionListDTO> promotionList;
	private Paging paging = new Paging();
	private Map<String, Object> paramMap = new HashMap<String, Object>();
	private Map<String, Object> searchMap = new HashMap<String, Object>();
	private Map<String, Object> whereMap = new HashMap<String, Object>();
	private Map<String, Object> validateMsgMap = new HashMap<String, Object>();
	private String rtnString;
	
	private int pageNum;				//	페이지번호
	private String pagingHTML = "";
	
	private int searchCol = 0;			// 	검색분류
    private String searchVal = "";		// 	검색어
    private int sortCol = 0;											// 	정렬 컬럼
    private String sortVal = "";										// 	정렬 내용
    private int countPerPage = Code.deFaultcountPerPage;		//	한페이지에 보일 리스트 수
	
    private HttpServletRequest request = ServletActionContext.getRequest();
    private String queryIncode = "";						//	쿼리스트링(인코딩)
    private String queryDecode = "";						//	쿼리스트링(디코딩)
    
	private List<String> p_id = new ArrayList<String>();
	private List<String> deleteContentList = new ArrayList<String>();
	private int map_order;
	private int pro_seq;
	private List<String> pro_map_seq = new ArrayList<String>();
	private int seq;
	
	private String isUpdateMode = "";					// 편집모드
    
    private LinkedHashMap searchColKindMap = new LinkedHashMap() {{	// 검색 가능한 종류
    	// db상의 name과 매칭 
    	put(0,"p_id");			// 기본값
    }};
    
    private LinkedHashMap sortColKindMap = new LinkedHashMap() {{	// 정렬항목 정의
    	// db상의 name과 매칭
		put(0,"map_order");		// 기본값 정렬
	}};
	
	public PromotionContent() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	    this.promotionMapDTO = new PromotionMapDTO();
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
		this.promotionMapDAO = (IfitDAO)this.wac.getBean("promotionMap");
		this.promotionListDAO = (IfitDAO)this.wac.getBean("promotionList");

		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
		
		this.rtnString = "";
	}
	
	public String getList() throws Exception{
		init();
		setPromotionList();
		
		this.pro_seq = (this.pro_seq == 0 && this.promotionList.size()>0) ? this.promotionList.get(this.pro_seq).getPro_seq() : this.pro_seq;  
		
		this.searchMap.put("pro_seq", this.pro_seq);
		this.paramMap.put("searchMap", this.searchMap);
		this.paramMap.put("whereMap", this.whereMap);
		paramMap.put("isCount",true);
		int totalCount = (int)this.promotionMapDAO.getList(paramMap);
		
		this.pageNum = this.pageNum == 0 || (this.pageNum*this.countPerPage>totalCount && this.pageNum*this.countPerPage-totalCount >= this.countPerPage)  ? 1  : this.pageNum;
		this.sortCol = this.sortColKindMap.containsKey(this.sortCol) ? this.sortCol : 0;
		this.sortVal = this.sortVal.equals("DESC") ? "DESC" : "ASC";
		paramMap.put("isCount",false);
		paramMap.put("pageNum",this.pageNum);
		paramMap.put("countPerPage",this.countPerPage);
		this.paramMap.put("sortCol", this.sortColKindMap.get(this.sortCol));
		this.paramMap.put("sortVal", this.sortVal);
		this.dataList = (List<PromotionMapDTO>)this.promotionMapDAO.getList(paramMap);
		
		this.queryIncode = StringUtil.stringToHex(this.request.getQueryString()==null ? "" : this.request.getQueryString());
		this.pagingHTML = paging.getPaging(totalCount, this.pageNum, this.countPerPage);
		
		return SUCCESS;
	}
	
	public void setPromotionList() throws Exception{
		init();
		
		this.paramMap.put("sortCol", "pro_seq");
		this.paramMap.put("sortVal", "ASC");
		this.paramMap.put("isCount",false);
		this.promotionList = (List<PromotionListDTO>)this.promotionListDAO.getList(paramMap);
		this.paramMap.clear();
	}
	
	public Object getData(Map<String, Object> paramMap){
		init();
		
		this.whereMap.clear();
		if(paramMap.containsKey("seq")){
			this.whereMap.put("pro_map_seq", paramMap.get("seq"));
			this.paramMap.put("whereMap", this.whereMap);
		}

		return (PromotionMapDTO) this.promotionMapDAO.getOneRow(this.paramMap);
	}
	
	public String getEditor() throws Exception{
		init();
		
		if(Boolean.valueOf(this.isUpdateMode).booleanValue()){
			this.paramMap.put("seq", this.seq);
			this.promotionMapDTO = (PromotionMapDTO) getData(this.paramMap);
		}
		
		this.queryDecode = StringUtil.hexToString(this.queryIncode);
		
		return SUCCESS;
	}
	
	//	faq 등록
	public String writeAction() throws Exception {	
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
//		
//		this.faqDAO.write(this.faqDTO);
//		
//		this.session.put("alertMsg", this.alertMessage.getFaqWriteOK());
//		this.context.setSession(this.session);
		
		return SUCCESS;
	}
	
	public String updateAction() throws Exception {	
		init();
		
		Gson gson = new Gson();
		
//		this.label_name = StringUtil.isNullOrSpace(this.label_name,"").trim();
//		paramMap.put("label_name", this.label_name);
//		paramMap.put("label_order", this.label_order);
		
//		this.validateMsgMap = formValidate.mainLabelEditorForm(paramMap);
//		paramMap.clear();
//		if(!(boolean)validateMsgMap.get("res")){
//			this.rtnString = gson.toJson(validateMsgMap);
//			return "validation";
//		}
		
		for(int i=0; i<this.pro_map_seq.size(); i++){
			this.promotionMapDTO.setP_id(Integer.parseInt(this.p_id.get(i)));
			this.promotionMapDTO.setPro_seq(this.pro_seq);
			this.promotionMapDTO.setMap_order(i+1);
			this.promotionMapDTO.setPro_map_seq(Integer.parseInt(this.pro_map_seq.get(i)));
			int result = this.promotionMapDAO.update(this.promotionMapDTO);
			if(result==0){
				this.promotionMapDAO.write(this.promotionMapDTO);
			}
		}
		
		for(String seq : deleteContentList){
			this.paramMap.put("pro_map_seq", Integer.parseInt(seq));
			this.promotionMapDAO.delete(this.paramMap);
			this.paramMap.clear();
		}
		
		this.session.put("alertMsg", this.alertMessage.getPromotionContentUpdateOK());
		this.context.setSession(this.session);
		
		return SUCCESS;
	}
	
	public String deleteAction(){
		init();

//		this.paramMap.put("faq_seq", this.seq);
//		
//		this.faqDAO.delete(this.paramMap);
//		
//		this.session.put("alertMsg", this.alertMessage.getDeleteOK());
//		this.context.setSession(this.session);
		
		return SUCCESS;	
	}

}
