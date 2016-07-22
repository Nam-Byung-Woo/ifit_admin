package action.product;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import util.config.*;
import util.file.FileManager;
import util.system.StringUtil;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import dao.IfitDAO;
import dto.ColorListDTO;
import dto.ColorMapDTO;
import dto.ProductListDTO;
import dto.SubPhotoDTO;
import dto.SizeListDTO;
import dto.SizeMapDTO;
import dto.TagListDTO;
import lombok.Data;

@Data
public class GeneralProduct extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FormValidate formValidate = new FormValidate();
	private Code code = new Code();
	private AlertMessage alertMessage = new AlertMessage();
	private IfitDAO productListDAO;
	private IfitDAO sizeListDAO;
	private IfitDAO sizeMapDAO;
	private IfitDAO colorListDAO;
	private IfitDAO colorMapDAO;
	private IfitDAO subPhotoDAO;
	private IfitDAO tagListDAO;
	private ProductListDTO productListDTO;
	private SizeMapDTO sizeMapDTO;
	private ColorMapDTO colorMapDTO;
	private SubPhotoDTO subPhotoDTO;
	private TagListDTO tagListDTO;
	
	private List<ProductListDTO> dataList;
	private List<SizeListDTO> sizeList;
	private List<SizeMapDTO> sizeMap;
	private List<ColorListDTO> colorList;
	private List<ColorMapDTO> colorMap;
	private List<TagListDTO> tagList;
	private List<SubPhotoDTO> subPhotoList;
	private List<SubPhotoDTO> detailPhotoList;
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
    
    private int seq;											// view/update시 seq
	private String[] listItemCheck;		// 복수선택
	
	private String isUpdateMode = "";					// 편집모드
    
    private LinkedHashMap searchColKindMap = new LinkedHashMap() {{	// 검색 가능한 종류
    	// db상의 name과 매칭 
    	put(0,"p_name");				// 기본값
    	put(1,"p_name");				// 상품 검색	
    	put(2,"ADMIN_NAME");		// 업체명 검색
    }};
    
    private LinkedHashMap sortColKindMap = new LinkedHashMap() {{	// 정렬항목 정의
    	// db상의 name과 매칭
		put(0,"p_id");					// 기본값 정렬
		put(1,"p_name");				// 상품명 정렬
		put(2,"ADMIN_NAME");	 	// 입점 업체명 정렬
		put(3,"state");			 		// 상태 정렬
		put(4,"orig_regdate"); 		// 등록일 정렬
	}};
    
	private int admin_seq;													//	업체seq
	private String p_name;													//	상품명
	private int category;													//	상품 분류
	private String p_price;													//	가격
	private List<String> sizeArray = new ArrayList<String>();		//	상품사이즈
	private List<String> colorArray = new ArrayList<String>();		//	상품색상
	private String cat_ref;													//	3D코드
	private String detail_info;												//	상품상세설명
	private List<String> tag_list = new ArrayList<String>();			//	태그
	private int state;														//	상품상태
	
	// 첨부파일 영역
	private List<File> p_main_url = new ArrayList<File>();								//	상품 메인이미지
	private List<String> p_main_urlContentType = new ArrayList<String>(); 			//	상품 메인이미지 첨부파일 종류
	private List<String> p_main_urlFileName = new ArrayList<String>(); 				//	상품 메인이미지 첨부파일명
	private List<String> p_main_urlDeleteFileName = new ArrayList<String>();		//	상품 메인이미지 삭제 요청시 파일명
	private List<File> sub_url = new ArrayList<File>();									//	상품 서브이미지
	private List<String> sub_urlContentType = new ArrayList<String>(); 				//	상품 서브이미지 첨부파일 종류
	private List<String> sub_urlFileName = new ArrayList<String>(); 					//	상품 서브이미지 첨부파일명
	private List<String> sub_urlDeleteFileName = new ArrayList<String>();			//	상품 서브이미지 삭제 요청시 파일명
	private List<File> detail_url = new ArrayList<File>();									//	상품 상세이미지
	private List<String> detail_urlContentType = new ArrayList<String>(); 			//	상품 상세이미지 첨부파일 종류
	private List<String> detail_urlFileName = new ArrayList<String>(); 				//	상품 상세이미지 첨부파일명
	private List<String> detail_urlDeleteFileName = new ArrayList<String>();			//	상품 상세이미지 삭제 요청시 파일명
	private List<File> lookup_url = new ArrayList<File>();								//	상품 코디이미지
	private List<String> lookup_urlContentType = new ArrayList<String>(); 			//	상품 코디이미지 첨부파일 종류
	private List<String> lookup_urlFileName = new ArrayList<String>(); 				//	상품 코디이미지 첨부파일명
	private List<String> lookup_urlDeleteFileName = new ArrayList<String>();		//	상품 코디이미지 삭제 요청시 파일명
	
	public GeneralProduct() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	    
	    this.productListDTO = new ProductListDTO();
		this.sizeMapDTO = new SizeMapDTO();
		this.colorMapDTO = new ColorMapDTO();
		this.tagListDTO = new TagListDTO();
		this.subPhotoDTO = new SubPhotoDTO();
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
		this.productListDAO = (IfitDAO)this.wac.getBean("productList");
		this.sizeListDAO = (IfitDAO)this.wac.getBean("sizeList");
		this.sizeMapDAO = (IfitDAO)this.wac.getBean("sizeMap");
		this.colorListDAO = (IfitDAO)this.wac.getBean("colorList");
		this.colorMapDAO = (IfitDAO)this.wac.getBean("colorMap");
		this.subPhotoDAO = (IfitDAO)this.wac.getBean("subPhoto");
		this.tagListDAO = (IfitDAO)this.wac.getBean("tagList");
		
		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
		
		this.rtnString = "";
	}
	
	public String getList() throws Exception{
		init();
		
		if(this.searchColKindMap.containsKey(this.searchCol)){
			this.searchMap.put(this.searchColKindMap.get(this.searchCol).toString(), this.searchVal);
		}
		if(!StringUtil.stringToBool(StringUtil.isNullOrSpace((String)session.get("isAdmin"),"").trim())){
			// 업체 접근시
			this.whereMap.put("PL.admin_seq", session.get("admin_seq"));
		}
		this.paramMap.put("searchMap", this.searchMap);
		this.paramMap.put("whereMap", this.whereMap);
		this.paramMap.put("isCount",true);
		this.totalCount = (int)this.productListDAO.getList(paramMap);
		
		this.pageNum = this.pageNum == 0 || (this.pageNum*this.countPerPage>totalCount && this.pageNum*this.countPerPage-totalCount >= this.countPerPage)  ? 1  : this.pageNum;
		this.sortCol = this.sortColKindMap.containsKey(this.sortCol) ? this.sortCol : 0;
		this.sortVal = this.sortVal.equals("ASC") ? "ASC" : "DESC";
		this.paramMap.put("isCount",false);
		this.paramMap.put("pageNum",this.pageNum);
		this.paramMap.put("countPerPage",this.countPerPage);
		this.paramMap.put("sortCol", this.sortColKindMap.get(this.sortCol));
		this.paramMap.put("sortVal", this.sortVal);
		this.dataList = (List<ProductListDTO>)this.productListDAO.getList(paramMap);
		
		this.queryIncode = StringUtil.stringToHex(this.request.getQueryString()==null ? "" : this.request.getQueryString());
		this.pagingHTML = paging.getPaging(totalCount, this.pageNum, this.countPerPage);
		
		return SUCCESS;
	}
	
	public void setSizeList() throws Exception{
		init();
		
		this.paramMap.put("isCount",false);
		this.sizeList = (List<SizeListDTO>)this.sizeListDAO.getList(paramMap);
	}
	
	public void setColorList() throws Exception{
		init();
		
		this.paramMap.put("isCount",false);
		this.colorList = (List<ColorListDTO>)this.colorListDAO.getList(paramMap);
	}
	
	// 일반상품 조회
	public Object getData(Map<String, Object> paramMap){
		init();
		String queryMode = paramMap.containsKey("queryMode") ? paramMap.get("queryMode").toString() : "one";
		this.whereMap.clear();
		if(!StringUtil.stringToBool(StringUtil.isNullOrSpace((String)session.get("isAdmin"),"").trim())){
			this.whereMap.put("PL.admin_seq", session.get("admin_seq"));
		}
		if(queryMode.equals("one")){
			if(paramMap.containsKey("seq")){
				this.whereMap.put("PL.p_id", paramMap.get("seq"));
				this.paramMap.put("whereMap", this.whereMap);
			}
			ProductListDTO data = (ProductListDTO) this.productListDAO.getOneRow(this.paramMap);
			data.setCategory_name(this.code.getProductCategoryMap().get(data.getCategory()).toString());
			return data;
		}else if(queryMode.equals("list")){
			if(!paramMap.containsKey("p_name")){
				return null;
			}
			this.searchMap.put("p_name", StringUtil.isNullOrSpace(paramMap.get("p_name").toString(),"").trim());
			paramMap.put("searchMap", this.searchMap);
			paramMap.put("isCount",false);
			return (List<ProductListDTO>)this.productListDAO.getList(paramMap);
		}
		return null;
	}
	
	public Object getSizeListData(Map<String, Object> paramMap){
		init();
		Gson gson = new Gson();
		String queryMode = paramMap.containsKey("queryMode") ? paramMap.get("queryMode").toString() : "one";
		if(queryMode.equals("one")){
		}else if(queryMode.equals("list")){
			if(paramMap.containsKey("sizeArray") && paramMap.get("sizeArray") != null){
				List<Object> test = (List<Object>)gson.fromJson(paramMap.get("sizeArray").toString(), List.class);
				String customSQL = "size_id IN(0";
				for(Object code : test){
					customSQL += "," + Integer.parseInt(String.valueOf(Math.round(Double.valueOf(code.toString()).doubleValue())));
				}
				customSQL += ")";
				paramMap.put("customSQL", customSQL);
				return ((List<SizeListDTO>)this.sizeListDAO.getList(paramMap)).size();
			}
		}
		return null;
	}
	
	public Object getSizeMapData(Map<String, Object> paramMap){
		init();
		this.whereMap.clear();
		if(paramMap.containsKey("seq")){
			this.whereMap.put("p_id", paramMap.get("seq"));
			this.paramMap.put("whereMap", this.whereMap);
		}
		return (List<SizeMapDTO>)this.sizeMapDAO.getList(this.paramMap);
	}
	
	public Object getColorListData(Map<String, Object> paramMap){
		init();
		Gson gson = new Gson();
		String queryMode = paramMap.containsKey("queryMode") ? paramMap.get("queryMode").toString() : "one";
		if(queryMode.equals("one")){
		}else if(queryMode.equals("list")){
			if(paramMap.containsKey("colorArray") && paramMap.get("colorArray") != null){
				List<Object> test = (List<Object>)gson.fromJson(paramMap.get("colorArray").toString(), List.class);
				String customSQL = "color_id IN(0";
				for(Object code : test){
					customSQL += "," + Integer.parseInt(String.valueOf(Math.round(Double.valueOf(code.toString()).doubleValue())));
				}
				customSQL += ")";
				paramMap.put("customSQL", customSQL);
				return ((List<ColorListDTO>)this.colorListDAO.getList(paramMap)).size();
			}
		}
		return null;
	}
	
	public Object getColorMapData(Map<String, Object> paramMap){
		init();
		this.whereMap.clear();
		if(paramMap.containsKey("seq")){
			this.whereMap.put("p_id", paramMap.get("seq"));
			this.paramMap.put("whereMap", this.whereMap);
		}
		return (List<ColorMapDTO>)this.colorMapDAO.getList(this.paramMap);
	}
	
	public Object getTagListData(Map<String, Object> paramMap){
		init();
		this.whereMap.clear();
		if(paramMap.containsKey("seq")){
			this.whereMap.put("p_id", paramMap.get("seq"));
			this.paramMap.put("whereMap", this.whereMap);
		}
		return (List<TagListDTO>)this.tagListDAO.getList(this.paramMap);
	}
	
	public Object getSubPhotoData(Map<String, Object> paramMap){
		init();
		
		this.whereMap.clear();
		if(paramMap.containsKey("seq")){
			this.whereMap.put("p_id", paramMap.get("seq"));
			this.whereMap.put("photo_type", paramMap.get("photo_type"));
			this.paramMap.put("whereMap", this.whereMap);
		}
		return (List<SubPhotoDTO>)this.subPhotoDAO.getList(this.paramMap);
	}
	
	public Map<String, Object> getOriginalData(int seq) throws Exception{
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		this.paramMap.put("seq", seq);
		rtnMap.put("productData", (ProductListDTO) this.getData(this.paramMap));
		if(rtnMap.get("productData") == null){
			return null;
		}
		rtnMap.put("productData", (ProductListDTO) this.getData(this.paramMap));
		rtnMap.put("sizeData", (List<SizeMapDTO>) this.getSizeMapData(this.paramMap));
		rtnMap.put("colorData", (List<ColorMapDTO>) this.getColorMapData(this.paramMap));
//		rtnMap.put("colorData", Arrays.asList(((ProductListDTO)rtnMap.get("productData")).getColor_list().split(",")));
		rtnMap.put("tagData", (List<TagListDTO>) this.getTagListData(this.paramMap));
		this.paramMap.put("photo_type", 1);
		rtnMap.put("subPhotoData", (List<SubPhotoDTO>) this.getSubPhotoData(this.paramMap));
		this.paramMap.put("photo_type", 2);
		rtnMap.put("detailPhotoData", (List<SubPhotoDTO>) this.getSubPhotoData(this.paramMap));
		return rtnMap;
	}
	
	public String getEditor() throws Exception{
		init();
		setSizeList();
		setColorList();
		
		if(Boolean.valueOf(this.isUpdateMode).booleanValue()){
			Map<String, Object> data = getOriginalData(this.seq);
			this.productListDTO = (ProductListDTO) data.get("productData");
			this.sizeMap = (List<SizeMapDTO>) data.get("sizeData");
			this.colorMap = (List<ColorMapDTO>) data.get("colorData");
			this.tagList = (List<TagListDTO>) data.get("tagData");
			this.subPhotoList = (List<SubPhotoDTO>) data.get("subPhotoData");
			this.detailPhotoList = (List<SubPhotoDTO>) data.get("detailPhotoData");
		}
		
		this.queryDecode = StringUtil.hexToString(this.queryIncode);
		
		return SUCCESS;
	}
	
	public String writeAction() throws Exception {	
		init();
		
		FileManager fileManager = new FileManager("image","generalProduct");
		
		int product_seq=0;
		Gson gson = new Gson();
		
		if(!StringUtil.stringToBool(StringUtil.isNullOrSpace((String)session.get("isAdmin"),"").trim())){
			// 업체 접근시
			this.admin_seq = (int) session.get("admin_seq");
		}

		this.p_name = StringUtil.isNullOrSpace(this.p_name,"").trim();
		paramMap.put("admin_seq", this.admin_seq);
		paramMap.put("p_name", this.p_name);
		paramMap.put("category", this.category);
		paramMap.put("p_price", this.p_price);
		paramMap.put("sizeArray", this.sizeArray);
		paramMap.put("colorArray", this.colorArray);
		paramMap.put("p_main_url", this.p_main_url.size());
		paramMap.put("sub_url", this.sub_url.size());
		paramMap.put("detail_url", this.detail_url.size());
		paramMap.put("lookup_url", this.lookup_url.size());
		paramMap.put("cat_ref", this.cat_ref);
		paramMap.put("detail_info", this.detail_info);
		paramMap.put("tag_list", this.tag_list);
		paramMap.put("state", this.state);
		this.validateMsgMap = formValidate.generalProductEditorForm(paramMap);
		paramMap.clear();
		if(!(boolean)validateMsgMap.get("res")){
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}else if(!fileManager.fileValidation(getP_main_url(), getP_main_urlFileName(), this.p_main_url)){
			validateMsgMap = formValidate.fileUploadError();
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}else if(!fileManager.fileValidation(getSub_url(), getSub_urlFileName(), this.sub_url)){
			validateMsgMap = formValidate.fileUploadError();
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}else if(!fileManager.fileValidation(getDetail_url(), getDetail_urlFileName(), this.detail_url)){
			validateMsgMap = formValidate.fileUploadError();
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}else if(!fileManager.fileValidation(getLookup_url(), getLookup_urlFileName(), this.lookup_url)){
			validateMsgMap = formValidate.fileUploadError();
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}

		this.productListDTO.setAdmin_seq(this.admin_seq);
		this.productListDTO.setP_name(this.p_name);
		this.productListDTO.setCategory(this.category);
		this.productListDTO.setP_price(Integer.parseInt(StringUtil.removeStrType(this.p_price)));
		this.productListDTO.setDetail_info(this.detail_info);
		this.productListDTO.setCat_ref(this.cat_ref);
		this.productListDTO.setState(this.state);
		
		/****************** 이미지파일 Insert & upload START ******************/
		String saveFileName = "";
		
		// 메인이미지 업로드
		for (int i = 0; i < this.p_main_url.size(); i++) { 
			saveFileName = fileManager.fileUpload(getP_main_urlFileName(), getP_main_url(), i, 0);	//파일업로드
			this.productListDTO.setP_main_url(saveFileName);
		}
		// 코디이미지 업로드
		for (int i = 0; i < this.lookup_url.size(); i++) { 
			saveFileName = fileManager.fileUpload(getLookup_urlFileName(), getLookup_url(), i, 1);	//파일업로드
			this.productListDTO.setLookup_url(saveFileName);
		}		
		product_seq = this.productListDAO.write(this.productListDTO);
		
		// 파일 여러장 업로드 시작(서브이미지, 상세이미지)
		this.subPhotoDTO.setP_id(product_seq);
		
		// 서브이미지  업로드
		for (int i = 0; i < this.sub_url.size(); i++) { 
			saveFileName = fileManager.fileUpload(getSub_urlFileName(), getSub_url(), i, 2);	//파일업로드
			this.subPhotoDTO.setPhoto_url(saveFileName);
			this.subPhotoDTO.setPhoto_type(1);
			this.subPhotoDAO.write(this.subPhotoDTO);
		}
		
		// 상세이미지 업로드
		for (int i = 0; i < this.detail_url.size(); i++) { 
			saveFileName = fileManager.fileUpload(getDetail_urlFileName(), getDetail_url(), i, 3);	//파일업로드
			this.subPhotoDTO.setPhoto_url(saveFileName);
			this.subPhotoDTO.setPhoto_type(2);
			this.subPhotoDAO.write(this.subPhotoDTO);
		}
		/****************** 이미지파일 Insert & upload END ******************/
		
		/****************** 별도테이블(사이즈, 색상, 태그) Insert START ******************/
		this.sizeMapDTO.setP_id(product_seq);
		for(int i=0; i<this.sizeArray.size(); i++){
			if(!this.sizeArray.get(i).equals("")){
				this.sizeMapDTO.setSize_id(Integer.parseInt(this.sizeArray.get(i)));
				this.sizeMapDAO.write(this.sizeMapDTO);
			}
		}
		
		this.colorMapDTO.setP_id(product_seq);
		for(int i=0; i<this.colorArray.size(); i++){
			if(!this.colorArray.get(i).equals("")){
				this.colorMapDTO.setColor_id(Integer.parseInt(this.colorArray.get(i)));
				this.colorMapDAO.write(this.colorMapDTO);
			}
		}
		
		this.tagListDTO.setP_id(product_seq);
		for(int i=0; i<this.tag_list.size(); i++){
			if(!this.tag_list.get(i).equals("")){
				this.tagListDTO.setTag(this.tag_list.get(i));
				this.tagListDAO.write(this.tagListDTO);
			}
		}
		/****************** 별도테이블(사이즈, 색상, 태그) Insert END ******************/
		
		this.session.put("alertMsg", this.alertMessage.getGeneralProductWriteOK());
		this.context.setSession(this.session);
		
		return SUCCESS;
	}
	
	public String updateAction() throws Exception {	
		init();
		
		FileManager fileManager = new FileManager("image","generalProduct");
		
		Gson gson = new Gson();
		
		if(!StringUtil.stringToBool(StringUtil.isNullOrSpace((String)session.get("isAdmin"),"").trim())){
			// 업체 접근시
			this.admin_seq = (int) session.get("admin_seq");
		}
		
		this.paramMap.put("seq", this.seq);
				
		Map<String, Object> data = getOriginalData(this.seq);
		
		ProductListDTO productData = (ProductListDTO) data.get("productData");
		List<SizeMapDTO> sizeData = (List<SizeMapDTO>) data.get("sizeData");
		List<ColorMapDTO> colorData = (List<ColorMapDTO>) data.get("colorData");
		List<TagListDTO> tagData = (List<TagListDTO>) data.get("tagData");
		List<SubPhotoDTO> subPhotoData = (List<SubPhotoDTO>) data.get("subPhotoData");
		List<SubPhotoDTO> detailPhotoData = (List<SubPhotoDTO>) data.get("subPhotoData");
		
		if(productData == null){
			validateMsgMap = formValidate.accessError();
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}
		this.paramMap.clear();

		this.p_name = StringUtil.isNullOrSpace(this.p_name,"").trim();
		paramMap.put("admin_seq", this.admin_seq);
		paramMap.put("p_name", this.p_name);
		paramMap.put("category", this.category);
		paramMap.put("p_price", this.p_price);
		paramMap.put("sizeArray", this.sizeArray);
		paramMap.put("colorArray", this.colorArray);
		paramMap.put("p_main_url", 1 - (p_main_urlDeleteFileName.contains(productData.getP_main_url_name()) ? 1 : 0) + this.p_main_url.size() );
		paramMap.put("sub_url", 1);
		paramMap.put("detail_url", 1);
		paramMap.put("lookup_url", 1 - (lookup_urlDeleteFileName.contains(productData.getLookup_url_name()) ? 1 : 0) + this.lookup_url.size() );
		paramMap.put("cat_ref", this.cat_ref);
		paramMap.put("detail_info", this.detail_info);
		paramMap.put("tag_list", this.tag_list);
		paramMap.put("state", this.state);
		this.validateMsgMap = formValidate.generalProductEditorForm(paramMap);
		paramMap.clear();
		if(!(boolean)validateMsgMap.get("res")){
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}else if(!fileManager.fileValidation(getP_main_url(), getP_main_urlFileName(), this.p_main_url)){
			validateMsgMap = formValidate.fileUploadError();
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}else if(!fileManager.fileValidation(getSub_url(), getSub_urlFileName(), this.sub_url)){
			validateMsgMap = formValidate.fileUploadError();
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}else if(!fileManager.fileValidation(getDetail_url(), getDetail_urlFileName(), this.detail_url)){
			validateMsgMap = formValidate.fileUploadError();
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}else if(!fileManager.fileValidation(getLookup_url(), getLookup_urlFileName(), this.lookup_url)){
			validateMsgMap = formValidate.fileUploadError();
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}

		/****************** 기존 데이터 삭제 START ******************/
		List<String> allDeleteFileName =  new ArrayList<String>();
		allDeleteFileName.addAll(this.p_main_urlDeleteFileName);
		allDeleteFileName.addAll(this.sub_urlDeleteFileName);
		allDeleteFileName.addAll(this.detail_urlDeleteFileName);
		allDeleteFileName.addAll(this.lookup_urlDeleteFileName);
		
		if(this.sub_urlDeleteFileName.size() > 0){
			this.paramMap.put("p_id", this.seq);
			this.paramMap.put("photo_url", this.sub_urlDeleteFileName);
			this.subPhotoDAO.delete(this.paramMap);
		}
		this.paramMap.clear();
		
		if(this.detail_urlDeleteFileName.size() > 0){
			this.paramMap.put("p_id", this.seq);
			this.paramMap.put("photo_url", this.detail_urlDeleteFileName);
			this.subPhotoDAO.delete(this.paramMap);
		}
		this.paramMap.clear();
		
		for(String str : allDeleteFileName){
			fileManager.fileDelete(str);
		}
		
		this.paramMap.put("p_id", this.seq);
		this.sizeMapDAO.delete(this.paramMap);
		this.colorMapDAO.delete(this.paramMap);
		this.tagListDAO.delete(this.paramMap);
		this.paramMap.clear();
		/****************** 기존 데이터 삭제 END ******************/
		
		this.productListDTO.setP_id(this.seq);
		this.productListDTO.setAdmin_seq(this.admin_seq);
		this.productListDTO.setP_name(this.p_name);
		this.productListDTO.setCategory(this.category);
		this.productListDTO.setP_price(Integer.parseInt(StringUtil.removeStrType(this.p_price)));
		this.productListDTO.setDetail_info(this.detail_info);
		this.productListDTO.setCat_ref(this.cat_ref);
		this.productListDTO.setState(this.state);
		
		/****************** 이미지파일 update & upload START ******************/
		
		String saveFileName = "";
		
		// 메인이미지 업로드
		for (int i = 0; i < this.p_main_url.size(); i++) { 
			saveFileName = fileManager.fileUpload(getP_main_urlFileName(), getP_main_url(), i, 0);	//파일업로드
			this.productListDTO.setP_main_url(saveFileName);
		}
		// 코디이미지 업로드
		for (int i = 0; i < this.lookup_url.size(); i++) { 
			saveFileName = fileManager.fileUpload(getLookup_urlFileName(), getLookup_url(), i, 1);	//파일업로드
			this.productListDTO.setLookup_url(saveFileName);
		}		
		
		this.productListDTO.setP_main_url((this.productListDTO.getP_main_url() == null) ? productData.getP_main_url() : this.productListDTO.getP_main_url());
		this.productListDTO.setLookup_url((this.productListDTO.getLookup_url() == null) ? productData.getLookup_url() : this.productListDTO.getLookup_url());
		
		this.productListDAO.update(this.productListDTO);
		
		// 파일 여러장 업로드 시작(서브이미지, 상세이미지)
		this.subPhotoDTO.setP_id(this.seq);
		
		// 서브이미지  업로드
		for (int i = 0; i < this.sub_url.size(); i++) { 
			saveFileName = fileManager.fileUpload(getSub_urlFileName(), getSub_url(), i, 2);	//파일업로드
			this.subPhotoDTO.setPhoto_url(saveFileName);
			this.subPhotoDTO.setPhoto_type(1);
			this.subPhotoDAO.write(this.subPhotoDTO);
		}
		
		// 상세이미지 업로드
		for (int i = 0; i < this.detail_url.size(); i++) { 
			saveFileName = fileManager.fileUpload(getDetail_urlFileName(), getDetail_url(), i, 3);	//파일업로드
			this.subPhotoDTO.setPhoto_url(saveFileName);
			this.subPhotoDTO.setPhoto_type(2);
			this.subPhotoDAO.write(this.subPhotoDTO);
		}
		/****************** 이미지파일 Insert & upload END ******************/
		
		/****************** 별도테이블(사이즈, 색상, 태그) Insert START ******************/
		this.sizeMapDTO.setP_id(this.seq);
		for(int i=0; i<this.sizeArray.size(); i++){
			if(!this.sizeArray.get(i).equals("")){
				this.sizeMapDTO.setSize_id(Integer.parseInt(this.sizeArray.get(i)));
				this.sizeMapDAO.write(this.sizeMapDTO);
			}
		}
		
		this.colorMapDTO.setP_id(this.seq);
		for(int i=0; i<this.colorArray.size(); i++){
			if(!this.colorArray.get(i).equals("")){
				this.colorMapDTO.setColor_id(Integer.parseInt(this.colorArray.get(i)));
				this.colorMapDAO.write(this.colorMapDTO);
			}
		}
		
		this.tagListDTO.setP_id(this.seq);
		for(int i=0; i<this.tag_list.size(); i++){
			if(!this.tag_list.get(i).equals("")){
				this.tagListDTO.setTag(this.tag_list.get(i));
				this.tagListDAO.write(this.tagListDTO);
			}
		}
		/****************** 별도테이블(사이즈, 색상, 태그) Insert END ******************/

		this.session.put("alertMsg", this.alertMessage.getGeneralProductUpdateOK());
		this.context.setSession(this.session);
		
		return SUCCESS;
	}
	
	public String deleteAction() throws Exception{
		init();

		FileManager fileManager = new FileManager("image", "generalProduct");
		
		Gson gson = new Gson();
		
		for(String item : listItemCheck){
			int itemSeq = Integer.parseInt(item);
			
			this.paramMap.put("p_id", itemSeq);
			
			Map<String, Object> data = getOriginalData(itemSeq);
			this.productListDTO = (ProductListDTO) data.get("productData");
			this.subPhotoList = (List<SubPhotoDTO>) data.get("subPhotoData");
			this.detailPhotoList = (List<SubPhotoDTO>) data.get("detailPhotoData");
			
			if(this.productListDTO == null){
				validateMsgMap = formValidate.accessError();
				this.rtnString = gson.toJson(validateMsgMap);
				return "validation";
			}
			
			this.productListDAO.delete(this.paramMap);
			
			/****************** 기존 데이터 삭제 START ******************/		
			fileManager.fileDelete(this.productListDTO.getP_main_url_name());
			fileManager.fileDelete(this.productListDTO.getLookup_url_name());
			for(SubPhotoDTO subPhoto : this.subPhotoList){
				fileManager.fileDelete(subPhoto.getPhoto_url_name());
			}
			for(SubPhotoDTO detailPhoto : this.detailPhotoList){
				fileManager.fileDelete(detailPhoto.getPhoto_url_name());
			}
			/****************** 기존 데이터 삭제 END ******************/
		
		}
		
		this.session.put("alertMsg", this.alertMessage.getDeleteOK());
		this.context.setSession(this.session);
		
		return SUCCESS;	
	}

}
