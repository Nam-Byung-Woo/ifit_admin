package action.member;

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
import dto.UserListDTO;
import lombok.Data;

@Data
public class User extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FormValidate formValidate = new FormValidate();
	private Code code = new Code();
	private AlertMessage alertMessage = new AlertMessage();
	private IfitDAO userListDAO;
	private UserListDTO userListDTO;
	private List<UserListDTO> dataList;
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
    private String queryIncode;						//	쿼리스트링(인코딩)
    private String queryDecode;						//	쿼리스트링(디코딩)
    
	private String user_id;		
	private String user_pw;			
	private String tel1;			
	private String tel2;			
	private String tel3;			
	private String email;		
	private int seq;				//	선택된 업체 seq
	private String[] listItemCheck;		// 복수선택
	
	private String isUpdateMode = "";			// 편집모드
    
    private LinkedHashMap searchColKindMap = new LinkedHashMap() {{	// 검색 가능한 종류
    	// db상의 name과 매칭 
    	put(0,"user_id");		// 기본값
    	put(1,"user_id");	
    	put(2,"email");
    }};
    
    private LinkedHashMap sortColKindMap = new LinkedHashMap() {{	// 정렬항목 정의
    	// db상의 name과 매칭
		put(0,"orig_regdate");		// 기본값 정렬
		put(1,"user_id"); 				
		put(2,"email");	 			
		put(3,"orig_regdate");	 	// 등록일 정렬
	}};
	
	public User() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	    this.userListDTO = new UserListDTO();
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
		this.userListDAO = (IfitDAO)this.wac.getBean("userList");
		
		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
		
		this.rtnString = "";
	}
	
	public String getList() throws Exception{
		init();
		
		if(this.searchColKindMap.containsKey(this.searchCol)){
			this.searchMap.put(this.searchColKindMap.get(this.searchCol).toString(), this.searchVal);
		}
		this.paramMap.put("searchMap", this.searchMap);
		this.paramMap.put("whereMap", this.whereMap);
		this.paramMap.put("isCount",true);
		this.totalCount = (int)this.userListDAO.getList(paramMap);
		
		this.pageNum = this.pageNum == 0 || (this.pageNum*this.countPerPage>totalCount && this.pageNum*this.countPerPage-totalCount >= this.countPerPage)  ? 1  : this.pageNum;
		this.sortCol = this.sortColKindMap.containsKey(this.sortCol) ? this.sortCol : 0;
		this.sortVal = this.sortVal.equals("ASC") ? "ASC" : "DESC";
		
		this.paramMap.put("isCount",false);
		this.paramMap.put("pageNum",this.pageNum);
		this.paramMap.put("countPerPage",this.countPerPage);
		this.paramMap.put("sortCol", this.sortColKindMap.get(this.sortCol));
		this.paramMap.put("sortVal", this.sortVal);
		this.dataList = (List<UserListDTO>)this.userListDAO.getList(paramMap);
		
		this.queryIncode = StringUtil.stringToHex(this.request.getQueryString()==null ? "" : this.request.getQueryString());
		this.pagingHTML = paging.getPaging(totalCount, this.pageNum, this.countPerPage);
		
		return SUCCESS;
	}
	
	public Object getData(Map<String, Object> paramMap){
		init();
		String queryMode = paramMap.containsKey("queryMode") ? paramMap.get("queryMode").toString() : "one";
		this.whereMap.clear();
		if(queryMode.equals("one")){
			if(paramMap.containsKey("seq")){
				this.whereMap.put("seq", paramMap.get("seq"));
				this.paramMap.put("whereMap", this.whereMap);
			}else if(paramMap.containsKey("user_id")){
				this.whereMap.put("user_id", StringUtil.isNullOrSpace(paramMap.get("user_id").toString(),"").trim());
				this.paramMap.put("whereMap", this.whereMap);
			}
			return this.userListDAO.getOneRow(this.paramMap);
		}else if(queryMode.equals("list")){
			if(!paramMap.containsKey("user_id")){
				return null;
			}
			this.searchMap.put("user_id", StringUtil.isNullOrSpace(paramMap.get("user_id").toString(),"").trim());
			this.paramMap.put("whereMap", this.whereMap);
			paramMap.put("whereMap", this.whereMap);
			paramMap.put("searchMap", this.searchMap);
			paramMap.put("isCount",false);
			return (List<UserListDTO>)this.userListDAO.getList(paramMap);
		}
		return null;
	}
	
	public String getEditor() throws Exception{
		init();
		
		if(Boolean.valueOf(this.isUpdateMode).booleanValue()){
			this.paramMap.put("seq", this.seq);
			this.userListDTO = (UserListDTO) getData(this.paramMap);
		}
		
		this.queryDecode = StringUtil.hexToString(this.queryIncode);
		
		return SUCCESS;
	}
	
	public String writeAction() throws Exception {	
		init();
		
		Gson gson = new Gson();
		this.user_id = StringUtil.isNullOrSpace(this.user_id,"").trim();
		this.user_pw = StringUtil.isNullOrSpace(this.user_pw,"").trim();
		this.email = StringUtil.isNullOrSpace(this.email,"").trim();
		this.tel1 = StringUtil.isNullOrSpace(this.tel1,"").trim();
		this.tel2 = StringUtil.isNullOrSpace(this.tel2,"").trim();
		this.tel3 = StringUtil.isNullOrSpace(this.tel3,"").trim();
		paramMap.put("user_id", this.user_id);
		paramMap.put("user_pw", this.user_pw);
		paramMap.put("email", this.email);
		paramMap.put("tel1", this.tel1);
		paramMap.put("tel2", this.tel2);
		paramMap.put("tel3", this.tel3);
		
		this.validateMsgMap = formValidate.userEditorForm(paramMap);
		paramMap.clear();
		if(!(boolean)validateMsgMap.get("res")){
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}
		
		this.userListDTO.setUser_id(this.user_id);
//		this.userListDTO.setUser_pw(MySqlFunction.password(AESCrypto.encryptPassword(this.user_pw)));
		this.userListDTO.setUser_pw(this.user_pw);
		this.userListDTO.setEmail(this.email);
		this.userListDTO.setTel1(this.tel1);
		this.userListDTO.setTel2(this.tel2);
		this.userListDTO.setTel3(this.tel3);
		this.userListDTO.setRoute("a");
		
		this.userListDAO.write(this.userListDTO);
		
		this.session.put("alertMsg", this.alertMessage.getUserWriteOK());
		this.context.setSession(this.session);
		
		return SUCCESS;
	}
	
	public String updateAction() throws Exception {	
		init();

		Gson gson = new Gson();
		
		this.paramMap.put("seq", this.seq);
		UserListDTO userListData = (UserListDTO) getData(this.paramMap);
		this.paramMap.clear();
		
		this.user_id = StringUtil.isNullOrSpace(this.user_id,"").trim();
		this.user_pw = StringUtil.isNullOrSpace(this.user_pw,"").trim();
		this.email = StringUtil.isNullOrSpace(this.email,"").trim();
		this.tel1 = StringUtil.isNullOrSpace(this.tel1,"").trim();
		this.tel2 = StringUtil.isNullOrSpace(this.tel2,"").trim();
		this.tel3 = StringUtil.isNullOrSpace(this.tel3,"").trim();
		paramMap.put("seq", this.seq);
		paramMap.put("user_id", this.user_id);
		paramMap.put("user_pw", this.user_pw);
		paramMap.put("email", this.email);
		paramMap.put("tel1", this.tel1);
		paramMap.put("tel2", this.tel2);
		paramMap.put("tel3", this.tel3);
		
		this.validateMsgMap = formValidate.userEditorForm(paramMap);
		paramMap.clear();
		if(!(boolean)validateMsgMap.get("res")){
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}
		
		this.userListDTO.setUser_id(userListData.getUser_id().equals(this.user_id) ? "" : this.user_id);
//		this.userListDTO.setUser_pw(this.user_pw.equals("") ? "" : MySqlFunction.password(AESCrypto.encryptPassword(this.user_pw)));
		this.userListDTO.setUser_pw(this.user_pw.equals("") ? "" : this.user_pw);
		this.userListDTO.setEmail(this.email);
		this.userListDTO.setTel1(this.tel1);
		this.userListDTO.setTel2(this.tel2);
		this.userListDTO.setTel3(this.tel3);
		this.userListDTO.setSeq(this.seq);
		
		this.userListDAO.update(this.userListDTO);
		
		this.session.put("alertMsg", this.alertMessage.getUserEditOK());
		this.context.setSession(this.session);

		return SUCCESS;
	}
	
	public String deleteAction(){
		init();

		for(String item : listItemCheck){
			int itemSeq = Integer.parseInt(item);
			if(itemSeq!=0 && itemSeq!=1){
				this.paramMap.put("seq", itemSeq);
				this.userListDAO.delete(this.paramMap);
			}
		}
		
		this.session.put("alertMsg", this.alertMessage.getDeleteOK());
		this.context.setSession(this.session);

		return SUCCESS;	
	}

}
