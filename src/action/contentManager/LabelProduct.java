package action.contentManager;

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
import dto.MainLabelListDTO;
import dto.MainProductListDTO;
import lombok.Data;

@Data
public class LabelProduct extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FormValidate formValidate = new FormValidate();
	private Code code = new Code();
	private AlertMessage alertMessage = new AlertMessage();
	private IfitDAO mainProductListDAO;
	private IfitDAO labelListDAO;
	private MainProductListDTO mainProductListDTO;
	private List<MainProductListDTO> dataList;
	private List<MainLabelListDTO> labelList;
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
	private int product_order;
	private int main_type;
	private List<String> m_p_seq = new ArrayList<String>();
	private int seq;
	
	private String isUpdateMode = "";					// 편집모드
    
    private LinkedHashMap searchColKindMap = new LinkedHashMap() {{	// 검색 가능한 종류
    	// db상의 name과 매칭 
    	put(0,"p_id");			// 기본값
    }};
    
    private LinkedHashMap sortColKindMap = new LinkedHashMap() {{	// 정렬항목 정의
    	// db상의 name과 매칭
		put(0,"product_order");		// 기본값 정렬
	}};
	
	public LabelProduct() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	    this.mainProductListDTO = new MainProductListDTO();
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
		this.mainProductListDAO = (IfitDAO)this.wac.getBean("mainProductList");
		this.labelListDAO = (IfitDAO)this.wac.getBean("mainLabelList");

		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
		
		this.rtnString = "";
	}
	
	public String getList() throws Exception{
		init();
		setLabelList();
		
		this.main_type = (this.main_type == 0 && this.labelList.size()>0) ? this.labelList.get(this.main_type).getMain_type() : this.main_type;  
		
		this.searchMap.put("main_type", this.main_type);
		this.paramMap.put("searchMap", this.searchMap);
		this.paramMap.put("whereMap", this.whereMap);
		paramMap.put("isCount",true);
		int totalCount = (int)this.mainProductListDAO.getList(paramMap);
		
		this.pageNum = this.pageNum == 0 || (this.pageNum*this.countPerPage>totalCount && this.pageNum*this.countPerPage-totalCount >= this.countPerPage)  ? 1  : this.pageNum;
		this.sortCol = this.sortColKindMap.containsKey(this.sortCol) ? this.sortCol : 0;
		this.sortVal = this.sortVal.equals("DESC") ? "DESC" : "ASC";
		paramMap.put("isCount",false);
		paramMap.put("pageNum",this.pageNum);
		paramMap.put("countPerPage",this.countPerPage);
		this.paramMap.put("sortCol", this.sortColKindMap.get(this.sortCol));
		this.paramMap.put("sortVal", this.sortVal);
		this.dataList = (List<MainProductListDTO>)this.mainProductListDAO.getList(paramMap);
		
		this.queryIncode = StringUtil.stringToHex(this.request.getQueryString()==null ? "" : this.request.getQueryString());
		this.pagingHTML = paging.getPaging(totalCount, this.pageNum, this.countPerPage);
		
		return SUCCESS;
	}
	
	public void setLabelList() throws Exception{
		init();
		
		this.paramMap.put("sortCol", "label_order");
		this.paramMap.put("sortVal", "ASC");
		this.paramMap.put("isCount",false);
		this.labelList = (List<MainLabelListDTO>)this.labelListDAO.getList(paramMap);
		this.paramMap.clear();
	}
	
	public Object getData(Map<String, Object> paramMap){
		init();
		
		this.whereMap.clear();
		if(paramMap.containsKey("seq")){
			this.whereMap.put("m_p_seq", paramMap.get("seq"));
			this.paramMap.put("whereMap", this.whereMap);
		}

		return (MainProductListDTO) this.mainProductListDAO.getOneRow(this.paramMap);
	}
	
	public String getEditor() throws Exception{
		init();
		
		if(Boolean.valueOf(this.isUpdateMode).booleanValue()){
			this.paramMap.put("seq", this.seq);
			this.mainProductListDTO = (MainProductListDTO) getData(this.paramMap);
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
		
		for(int i=0; i<this.m_p_seq.size(); i++){
			this.mainProductListDTO.setP_id(Integer.parseInt(this.p_id.get(i)));
			this.mainProductListDTO.setMain_type(this.main_type);
			this.mainProductListDTO.setProduct_order(i+1);
			this.mainProductListDTO.setM_p_seq(Integer.parseInt(this.m_p_seq.get(i)));
			int result = this.mainProductListDAO.update(this.mainProductListDTO);
			if(result==0){
				this.mainProductListDAO.write(this.mainProductListDTO);
			}
		}
		
		for(String seq : deleteContentList){
			this.paramMap.put("m_p_seq", Integer.parseInt(seq));
			this.mainProductListDAO.delete(this.paramMap);
			this.paramMap.clear();
		}
		
		this.session.put("alertMsg", this.alertMessage.getLabelProductUpdateOK());
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
