package action.banner;

import java.io.File;
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
import util.file.FileManager;
import util.system.AESCrypto;
import util.system.MySqlFunction;
import util.system.StringUtil;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import dao.IfitDAO;
import dto.PromotionListDTO;
import lombok.Data;

@Data
public class Promotion extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FormValidate formValidate = new FormValidate();
	private Code code = new Code();
	private AlertMessage alertMessage = new AlertMessage();
	private IfitDAO promotionListDAO;
	private PromotionListDTO promotionListDTO;
	private List<PromotionListDTO> dataList;
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
    
    private String pro_name;
	private int pro_use;		
	private int seq;
	
	// 첨부파일 영역
	private List<File> pro_url = new ArrayList<File>();								//	프로모션 이미지
	private List<String> pro_urlContentType = new ArrayList<String>(); 			//	프로모션 이미지 첨부파일 종류
	private List<String> pro_urlFileName = new ArrayList<String>(); 				//	프로모션 이미지 첨부파일명
	private List<String> pro_urlDeleteFileName = new ArrayList<String>();		//	프로모션 이미지 삭제 요청시 파일명
	
	private String isUpdateMode = "";					// 편집모드
    
    private LinkedHashMap searchColKindMap = new LinkedHashMap() {{	// 검색 가능한 종류
    	// db상의 name과 매칭 
    	put(0,"pro_name");					// 기본값
    	put(1,"pro_name");						
    }};
    
    private LinkedHashMap sortColKindMap = new LinkedHashMap() {{	// 정렬항목 정의
    	// db상의 name과 매칭
		put(0,"pro_seq");		// 기본값 정렬
		put(1,"pro_name");
	}};
	
	public Promotion() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	    this.promotionListDTO = new PromotionListDTO();
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
		this.promotionListDAO = (IfitDAO)this.wac.getBean("promotionList");

		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
		
		this.rtnString = "";
	}
	
	// faq 목록
	public String getList() throws Exception{
		init();
		
		if(this.searchColKindMap.containsKey(this.searchCol)){
			this.searchMap.put(this.searchColKindMap.get(this.searchCol).toString(), this.searchVal);
		}
		
		this.paramMap.put("searchMap", this.searchMap);
		this.paramMap.put("whereMap", this.whereMap);
		paramMap.put("isCount",true);
		int totalCount = (int)this.promotionListDAO.getList(paramMap);
		
		this.pageNum = this.pageNum == 0 || (this.pageNum*this.countPerPage>totalCount && this.pageNum*this.countPerPage-totalCount >= this.countPerPage)  ? 1  : this.pageNum;
		this.sortCol = this.sortColKindMap.containsKey(this.sortCol) ? this.sortCol : 0;
		this.sortVal = this.sortVal.equals("ASC") ? "ASC" : "DESC";
		paramMap.put("isCount",false);
		paramMap.put("pageNum",this.pageNum);
		paramMap.put("countPerPage",this.countPerPage);
		this.paramMap.put("sortCol", this.sortColKindMap.get(this.sortCol));
		this.paramMap.put("sortVal", this.sortVal);
		this.dataList = (List<PromotionListDTO>)this.promotionListDAO.getList(paramMap);
		
		this.queryIncode = StringUtil.stringToHex(this.request.getQueryString()==null ? "" : this.request.getQueryString());
		this.pagingHTML = paging.getPaging(totalCount, this.pageNum, this.countPerPage);
		
		return SUCCESS;
	}
	
	// faq 조회
	public Object getData(Map<String, Object> paramMap){
		init();
		
		this.whereMap.clear();
		if(paramMap.containsKey("seq")){
			this.whereMap.put("pro_seq", paramMap.get("seq"));
			this.paramMap.put("whereMap", this.whereMap);
		}

		return (PromotionListDTO) this.promotionListDAO.getOneRow(this.paramMap);
	}
	
	public String getEditor() throws Exception{
		init();
		
		if(Boolean.valueOf(this.isUpdateMode).booleanValue()){
			this.paramMap.put("seq", this.seq);
			this.promotionListDTO = (PromotionListDTO) getData(this.paramMap);
		}
		
		this.queryDecode = StringUtil.hexToString(this.queryIncode);
		
		return SUCCESS;
	}
	
	//	faq 등록
	public String writeAction() throws Exception {	
		init();
		
		FileManager fileManager = new FileManager("image", "promotion");
		
		Gson gson = new Gson();
		
		this.pro_name = StringUtil.isNullOrSpace(this.pro_name,"").trim();
		paramMap.put("pro_name", this.pro_name);
		paramMap.put("pro_url", this.pro_url.size());
		
		this.validateMsgMap = formValidate.promotionEditorForm(paramMap);
		paramMap.clear();
		if(!(boolean)validateMsgMap.get("res")){
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}else if(!fileManager.fileValidation(getPro_url(), getPro_urlFileName(), this.pro_url)){
			validateMsgMap = formValidate.fileUploadError();
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}
		
		this.promotionListDTO.setPro_name(this.pro_name);
		this.promotionListDTO.setPro_use(1);
		
		/****************** 이미지파일 Insert & upload START ******************/
		String saveFileName = "";
		
		// 메인이미지 업로드
		for (int i = 0; i < this.pro_url.size(); i++) { 
			saveFileName = fileManager.fileUpload(getPro_urlFileName(), getPro_url(), i, 0);	//파일업로드
			this.promotionListDTO.setPro_url(saveFileName);
		}	
		/****************** 이미지파일 Insert & upload END ******************/
		
		this.promotionListDAO.write(this.promotionListDTO);
		
		this.session.put("alertMsg", this.alertMessage.getPromotionWriteOK());
		this.context.setSession(this.session);
		
		return SUCCESS;
	}
	
	public String updateAction() throws Exception {	
		init();
		
		FileManager fileManager = new FileManager("image", "promotion");
		
		Gson gson = new Gson();
		
		this.paramMap.put("seq", this.seq);
		
		PromotionListDTO promotionData = (PromotionListDTO) getData(this.paramMap);
		
		this.pro_name = StringUtil.isNullOrSpace(this.pro_name,"").trim();
		paramMap.put("pro_name", this.pro_name);
		paramMap.put("pro_use", 1);
		paramMap.put("pro_url", 1 - (pro_urlDeleteFileName.contains(promotionData.getPro_url_name()) ? 1 : 0) + this.pro_url.size() );
		
		this.validateMsgMap = formValidate.promotionEditorForm(paramMap);
		paramMap.clear();
		if(!(boolean)validateMsgMap.get("res")){
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}else if(!fileManager.fileValidation(getPro_url(), getPro_urlFileName(), this.pro_url)){
			validateMsgMap = formValidate.fileUploadError();
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}
		
		/****************** 기존 데이터 삭제 START ******************/
		for(String str : this.pro_urlDeleteFileName){
			fileManager.fileDelete(str);
		}
		/****************** 기존 데이터 삭제 END ******************/
		
		this.promotionListDTO.setPro_name(this.pro_name);
		this.promotionListDTO.setPro_seq(this.seq);
		this.promotionListDTO.setPro_use(1);
		
		/****************** 이미지파일 update & upload START ******************/
		String saveFileName = "";
		
		for (int i = 0; i < this.pro_url.size(); i++) { 
			saveFileName = fileManager.fileUpload(getPro_urlFileName(), getPro_url(), i, 0);	//파일업로드
			this.promotionListDTO.setPro_url(saveFileName);
		}
		
		this.promotionListDTO.setPro_url((this.promotionListDTO.getPro_url() == null) ? promotionData.getPro_url() : this.promotionListDTO.getPro_url());
		/****************** 이미지파일 Insert & upload END ******************/
		
		this.promotionListDAO.update(this.promotionListDTO);
		
		this.session.put("alertMsg", this.alertMessage.getPromotionUpdateOK());
		this.context.setSession(this.session);
		
		return SUCCESS;
	}
	
	public String deleteAction() throws Exception{
		init();
		
		FileManager fileManager = new FileManager("image", "promotion");

		this.paramMap.put("pro_seq", this.seq);
		this.paramMap.put("seq", this.seq);
		
		this.promotionListDTO = (PromotionListDTO) getData(this.paramMap);
		
		this.promotionListDAO.delete(this.paramMap);
		
		/****************** 기존 데이터 삭제 START ******************/		
		fileManager.fileDelete(this.promotionListDTO.getPro_url_name());
		/****************** 기존 데이터 삭제 END ******************/
		
		this.session.put("alertMsg", this.alertMessage.getDeleteOK());
		this.context.setSession(this.session);
		
		return SUCCESS;	
	}

}
