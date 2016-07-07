package action.help;

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
import dto.QuestionPersonDTO;
import lombok.Data;

@Data
public class Qna extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FormValidate formValidate = new FormValidate();
	private Code code = new Code();
	private AlertMessage alertMessage = new AlertMessage();
	private IfitDAO questionPersonDAO;
	private QuestionPersonDTO questionPersonDTO;
	private List<QuestionPersonDTO> dataList;
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
    private int totalCount = 0;
	
    private HttpServletRequest request = ServletActionContext.getRequest();
    private String queryIncode = "";						//	쿼리스트링(인코딩)
    private String queryDecode = "";						//	쿼리스트링(디코딩)
    
	private String reply;
	private int seq;
	private int tabID;
	private String[] listItemCheck;		// 복수선택
	
	private LinkedHashMap tabIDMap = new LinkedHashMap() {{
    	put(0,1);					// 기본값
    	put(1,1);					
    	put(2,2);			
    }};
	
    private LinkedHashMap searchColKindMap = new LinkedHashMap() {{	// 검색 가능한 종류
    	// db상의 name과 매칭 
    	put(0,"title");					// 기본값
    	put(1,"title");					
    	put(2,"user_id");			
    }};
    
    private LinkedHashMap sortColKindMap = new LinkedHashMap() {{	// 정렬항목 정의
    	// db상의 name과 매칭
		put(0,"quest_seq");		// 기본값 정렬
		put(1,"title");
		put(2,"user_id");			
		put(3,"state");	
		put(4,"quest_date");
		put(5,"reply_date");
	}};
	
	public Qna() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	    this.questionPersonDTO = new QuestionPersonDTO();
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
		this.questionPersonDAO = (IfitDAO)this.wac.getBean("questionPerson");

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
		
		this.paramMap.put("searchMap", this.searchMap);
		this.paramMap.put("whereMap", this.whereMap);
		paramMap.put("isCount",true);
		paramMap.put("tabID",this.tabID);
		this.totalCount = (int)this.questionPersonDAO.getList(paramMap);
		
		this.pageNum = this.pageNum == 0 || (this.pageNum*this.countPerPage>totalCount && this.pageNum*this.countPerPage-totalCount >= this.countPerPage)  ? 1  : this.pageNum;
		this.sortCol = this.sortColKindMap.containsKey(this.sortCol) ? this.sortCol : 0;
		this.sortVal = this.sortVal.equals("ASC") ? "ASC" : "DESC";
		paramMap.put("isCount",false);
		paramMap.put("pageNum",this.pageNum);
		paramMap.put("countPerPage",this.countPerPage);
		paramMap.put("tabID",this.tabID);
		this.paramMap.put("sortCol", this.sortColKindMap.get(this.sortCol));
		this.paramMap.put("sortVal", this.sortVal);
		this.dataList = (List<QuestionPersonDTO>)this.questionPersonDAO.getList(paramMap);
		
		this.queryIncode = StringUtil.stringToHex(this.request.getQueryString()==null ? "" : this.request.getQueryString());
		this.pagingHTML = paging.getPaging(totalCount, this.pageNum, this.countPerPage);
		
		return SUCCESS;
	}
	
	public Object getData(Map<String, Object> paramMap){
		init();
		
		this.whereMap.clear();
		if(paramMap.containsKey("seq")){
			this.whereMap.put("quest_seq", paramMap.get("seq"));
			this.paramMap.put("whereMap", this.whereMap);
		}

		return (QuestionPersonDTO) this.questionPersonDAO.getOneRow(this.paramMap);
	}
	
	public String getEditor() throws Exception{
		init();
		
//		if(Boolean.valueOf(this.isUpdateMode).booleanValue()){
//			this.paramMap.put("seq", this.seq);
//			this.questionPersonDTO = (QuestionPersonDTO) getData(this.paramMap);
//		}
//		
//		this.queryDecode = StringUtil.hexToString(this.queryIncode);
		
		return SUCCESS;
	}
	
	public String writeAction() throws Exception {	
		init();
		
		Gson gson = new Gson();
		
		this.reply = StringUtil.isNullOrSpace(this.reply,"").trim();
		paramMap.put("reply", this.reply);
		
		this.validateMsgMap = formValidate.qnaEditorForm(paramMap);
		paramMap.clear();
		if(!(boolean)validateMsgMap.get("res")){
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}
		
		this.questionPersonDTO.setReply(this.reply);
		this.questionPersonDTO.setState(2);
		this.questionPersonDTO.setQuest_seq(this.seq);
		
		this.questionPersonDAO.update(this.questionPersonDTO);
		
		this.session.put("alertMsg", this.alertMessage.getQnaReplyWriteOK());
		this.context.setSession(this.session);
		
		return SUCCESS;
	}
	
	public String updateAction() throws Exception {	
		init();
		
		Gson gson = new Gson();
		
		this.reply = StringUtil.isNullOrSpace(this.reply,"").trim();
		paramMap.put("reply", this.reply);
		
		this.validateMsgMap = formValidate.qnaEditorForm(paramMap);
		paramMap.clear();
		if(!(boolean)validateMsgMap.get("res")){
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}
		
		this.questionPersonDTO.setReply(this.reply);
		this.questionPersonDTO.setState(2);
		this.questionPersonDTO.setQuest_seq(this.seq);
		
		this.questionPersonDAO.update(this.questionPersonDTO);
		
		this.session.put("alertMsg", this.alertMessage.getQnaReplyUpdateOK());
		this.context.setSession(this.session);
		
		return SUCCESS;
	}
	
	public String deleteAction(){
		init();

		for(String item : listItemCheck){
			int itemSeq = Integer.parseInt(item);
			this.paramMap.put("quest_seq", itemSeq);
			this.questionPersonDAO.delete(this.paramMap);
		}
		
		this.session.put("alertMsg", this.alertMessage.getDeleteOK());
		this.context.setSession(this.session);
		
		return SUCCESS;	
	}

}
