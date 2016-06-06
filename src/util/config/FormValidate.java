package util.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import dto.AdminDTO;

import util.system.StringUtil;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

import action.member.ShopMember;
import action.product.GeneralProduct;

@Data
public class FormValidate{
	
	private AlertMessage alertMessage = new AlertMessage();
	private Map<String, Object> rtnMap = new HashMap<String, Object>();
	private StringUtil stringUtil = new StringUtil();
	private Code code = new Code();
	
	public FormValidate(){
		this.rtnMap.put("res", false);		// validation결과
		this.rtnMap.put("msg", "");			// 페이지 전달 메세지
		this.rtnMap.put("elementID", "");		// form요소 id명
	}
	
	// 접근 에러
	public Map<String, Object> accessError(){
		this.rtnMap.put("res", false);
		this.rtnMap.put("msg", alertMessage.getAccessError());	
		this.rtnMap.put("elementID", "");
		return this.rtnMap;
	}
	
	// 첨부파일 에러
	public Map<String, Object> fileUploadError(){
		this.rtnMap.put("res", false);
		this.rtnMap.put("msg", alertMessage.getFileUploadError());	
		this.rtnMap.put("elementID", "");
		return this.rtnMap;
	}
	
	// 로그인 체크(DB값)
	public Map<String, Object> loginAuthError(){
		this.rtnMap.put("res", false);
		this.rtnMap.put("msg", alertMessage.getLoginError());	
		this.rtnMap.put("elementID", "admin_id");
		return this.rtnMap;
	}
	
	// 로그인 체크(validation)
	public Map<String, Object> loginForm(Map<String, Object> paramMap){
		if(!paramMap.containsKey("admin_id") || paramMap.get("admin_id").equals("")){
			// 아이디 체크
			this.rtnMap.put("msg", alertMessage.getLoginError());
			this.rtnMap.put("elementID", "admin_id");
		}else if(!paramMap.containsKey("admin_pw") || paramMap.get("admin_pw").equals("")){
			// 비밀번호 체크
			this.rtnMap.put("msg", alertMessage.getLoginError());
			this.rtnMap.put("elementID", "admin_pw");
		}else if(false){
			// 아이디/비밀번호 자리수 체크 추가 가능
		}else{
			this.rtnMap.put("res", true);
		}
		
		return this.rtnMap;
	}
	
	// 입점회원 등록 체크(validation)
	public Map<String, Object> shopMemberEditorForm(Map<String, Object> paramMap){
		ShopMember shop = new ShopMember();
		int seq = !paramMap.containsKey("seq") ? 0 : Integer.parseInt(paramMap.get("seq").toString());
		paramMap.remove("seq");
		boolean idCheckNull = (shop.getData(paramMap)==null);
		if(seq!=0){
			paramMap.put("seq",seq);
		}
		if(!paramMap.containsKey("id") || paramMap.get("id").equals("") || !stringUtil.id_validation(paramMap.get("id").toString())){
			// 업체아이디 체크
			this.rtnMap.put("msg", alertMessage.getShopIdError());
			this.rtnMap.put("elementID", "id_check");
		}else if(seq==0 && shop.getData(paramMap)!=null){
			// 업체아이디 중복 체크(등록)
			this.rtnMap.put("msg", alertMessage.getShopDuplicateError());
			this.rtnMap.put("elementID", "id_check");
		}else if(seq!=0 && !paramMap.get("id").equals(((AdminDTO)shop.getData(paramMap)).getId()) && !idCheckNull){
			// 업체아이디 중복 체크(수정)
			this.rtnMap.put("msg", alertMessage.getShopDuplicateError());
			this.rtnMap.put("elementID", "id_check");
		}else if(seq==0 && (!paramMap.containsKey("pw") || paramMap.get("pw").equals("") || !stringUtil.pw_validation(paramMap.get("pw").toString())) ){
			// 업체비밀번호 입력 체크(등록)
			this.rtnMap.put("msg", alertMessage.getShopPwError());
			this.rtnMap.put("elementID", "pw_check");
		}else if(seq!=0 && !paramMap.get("pw").equals("") && !stringUtil.pw_validation(paramMap.get("pw").toString())){
			// 업체비밀번호 입력 체크(수정)
			this.rtnMap.put("msg", alertMessage.getShopPwError());
			this.rtnMap.put("elementID", "pw_check");
		}else if(!paramMap.containsKey("name") || paramMap.get("name").equals("")){
			// 업체명 체크
			this.rtnMap.put("msg", alertMessage.getShopNameError());
			this.rtnMap.put("elementID", "name_check");
		}else if(!paramMap.containsKey("tel1") || paramMap.get("tel1").equals("")){
			// 업체번호 앞자리 체크
			this.rtnMap.put("msg", alertMessage.getShopTelError());
			this.rtnMap.put("elementID", "tel_check");
		}else if(!paramMap.containsKey("tel2") || paramMap.get("tel2").equals("")){
			// 업체번호 중간자리 체크
			this.rtnMap.put("msg", alertMessage.getShopTelError());
			this.rtnMap.put("elementID", "tel_check");
		}else if(!paramMap.containsKey("tel3") || paramMap.get("tel3").equals("")){
			// 업체번호 뒷자리 체크
			this.rtnMap.put("msg", alertMessage.getShopTelError());
			this.rtnMap.put("elementID", "tel_check");
		}else if(!stringUtil.mobileNumber_validation(paramMap.get("tel1").toString(), paramMap.get("tel2").toString(), paramMap.get("tel3").toString(), code.getTelNumberMap())){
			// 업체번호 전체 체크
			this.rtnMap.put("msg", alertMessage.getShopTelError());
			this.rtnMap.put("elementID", "tel_check");
		}else if(false){
			// 추가 가능
		}else{
			this.rtnMap.put("res", true);
		}
		
		return this.rtnMap;
	}
	
	// FAQ 등록/수정 체크(validation)
	public Map<String, Object> faqEditorForm(Map<String, Object> paramMap){
		if(!paramMap.containsKey("title") || paramMap.get("title").equals("")){
			// FAQ 제목 체크
			this.rtnMap.put("msg", alertMessage.getFaqTitleError());
			this.rtnMap.put("elementID", "title_check");
		}else if(!paramMap.containsKey("content") || paramMap.get("content").equals("")){
			// FAQ 내용 체크
			this.rtnMap.put("msg", alertMessage.getFaqContentError());
			this.rtnMap.put("elementID", "content_check");
		}else if(false){
			// 추가 가능
		}else{
			this.rtnMap.put("res", true);
		}
		
		return this.rtnMap;
	}
	
	// 일반상품 등록/수정 체크(validation)
	public Map<String, Object> generalProductEditorForm(Map<String, Object> paramMap){
		// 파일체크는 단순 객체 크기만 비교하고 client는 script plugin으로, server는 fileuploader에서 한다.
		Gson gson = new Gson();
		ShopMember shop = new ShopMember();
		GeneralProduct generalProduct = new GeneralProduct();
		paramMap.put("seq", paramMap.get("admin_seq"));
		paramMap.put("queryMode", "list");
		int colorCnt = generalProduct.getColorListData(paramMap) == null ? 0 : (int)generalProduct.getColorListData(paramMap);
		int sizeCnt = generalProduct.getSizeListData(paramMap) == null ? 0 : (int)generalProduct.getSizeListData(paramMap);
		paramMap.put("queryMode", "one");

		if(!paramMap.containsKey("admin_seq") || paramMap.get("admin_seq").equals("") || shop.getData(paramMap)==null){
			// 일반상품 업체 체크
			this.rtnMap.put("msg", alertMessage.getGeneralProductShopError());
			this.rtnMap.put("elementID", "admin_seq_check");
		}else if(!paramMap.containsKey("p_name") || paramMap.get("p_name").equals("")){
			// 일반상품명 체크
			this.rtnMap.put("msg", alertMessage.getGeneralProductNameError());
			this.rtnMap.put("elementID", "p_name_check");
		}else if(!paramMap.containsKey("category") || paramMap.get("category").equals("") || !code.getProductCategoryMap().containsKey(Integer.parseInt(paramMap.get("category").toString()))){
			// 일반상품 분류 체크
			this.rtnMap.put("msg", alertMessage.getGeneralProductCategoryError());
			this.rtnMap.put("elementID", "category_check");
		}else if(!paramMap.containsKey("p_price") || paramMap.get("p_price").equals("") || Integer.parseInt(stringUtil.removeStrType(paramMap.get("p_price").toString())) > 1000000000 ){
			// 일반상품 가격 체크
			this.rtnMap.put("msg", alertMessage.getGeneralProductPriceError());
			this.rtnMap.put("elementID", "p_price_check");
		}else if(!paramMap.containsKey("colorArray") || paramMap.get("colorArray") == null || ((List<String>)gson.fromJson(paramMap.get("colorArray").toString(), List.class)).size()<=0 || colorCnt != ((List<String>)gson.fromJson(paramMap.get("colorArray").toString(), List.class)).size()){
			// 일반상품 색상 체크
			this.rtnMap.put("msg", alertMessage.getGeneralProductColorError());
			this.rtnMap.put("elementID", "colorArray_check");
		}else if(!paramMap.containsKey("sizeArray") || paramMap.get("sizeArray") == null || ((List<String>)gson.fromJson(paramMap.get("sizeArray").toString(), List.class)).size()<=0 || sizeCnt != ((List<String>)gson.fromJson(paramMap.get("sizeArray").toString(), List.class)).size()){
			// 일반상품 사이즈 체크
			this.rtnMap.put("msg", alertMessage.getGeneralProductSizeError());
			this.rtnMap.put("elementID", "sizeArray_check");
		}else if(!paramMap.containsKey("p_main_url") || Integer.parseInt(paramMap.get("p_main_url").toString()) <= 0 ){
			// 일반상품 메인 이미지 체크
			this.rtnMap.put("msg", alertMessage.getGeneralProductMainImageError());
			this.rtnMap.put("elementID", "p_main_url_check");
		}else if(!paramMap.containsKey("sub_url") || Integer.parseInt(paramMap.get("sub_url").toString()) <= 0 ){
			// 일반상품 서브 이미지 체크
			this.rtnMap.put("msg", alertMessage.getGeneralProductSubImageError());
			this.rtnMap.put("elementID", "sub_url_check");
		}else if(!paramMap.containsKey("detail_url") || Integer.parseInt(paramMap.get("detail_url").toString()) <= 0 ){
			// 일반상품 상세 이미지 체크
			this.rtnMap.put("msg", alertMessage.getGeneralProductDetailImageError());
			this.rtnMap.put("elementID", "detail_url_check");
		}else if(!paramMap.containsKey("lookup_url") || Integer.parseInt(paramMap.get("lookup_url").toString()) <= 0 ){
			// 일반상품 코디 이미지 체크
			this.rtnMap.put("msg", alertMessage.getGeneralProductCodiImageError());
			this.rtnMap.put("elementID", "lookup_url_check");
		}else if(!paramMap.containsKey("tag_list") || paramMap.get("tag_list") == null || ((List<String>)paramMap.get("color_list")).size()>10){
			// 일반상품 태그 체크
			this.rtnMap.put("msg", alertMessage.getGeneralProductTagError());
			this.rtnMap.put("elementID", "tag_list_check");
		}else if(false){
			// 추가 가능
		}else{
			this.rtnMap.put("res", true);
		}		
		return this.rtnMap;
	}
	
	// 이벤트배너 등록/수정 체크(validation)
	public Map<String, Object> eventBannerEditorForm(Map<String, Object> paramMap){
		if(!paramMap.containsKey("banner_type") || paramMap.get("banner_type").equals("") || !code.getEventBannerTypeMap().containsKey(Integer.parseInt(paramMap.get("banner_type").toString()))){
			// 배너 타입 체크
			this.rtnMap.put("msg", alertMessage.getEventBannerTypeError());
			this.rtnMap.put("elementID", "banner_type_check");
		}else if(!paramMap.containsKey("banner_url") || Integer.parseInt(paramMap.get("banner_url").toString()) <= 0 ){
			// 배너 이미지 체크
			this.rtnMap.put("msg", alertMessage.getEventBannerImageError());
			this.rtnMap.put("elementID", "banner_url_check");
		}else if(false){
			// 추가 가능
		}else{
			this.rtnMap.put("res", true);
		}
		
		return this.rtnMap;
	}
	
	// 라벨 등록/수정 체크(validation)
	public Map<String, Object> mainLabelEditorForm(Map<String, Object> paramMap){
//		if(!paramMap.containsKey("tag") || paramMap.get("tag") == null || ((List<String>)paramMap.get("tag")).size()<=0 || ((List<String>)paramMap.get("tag")).size()>3){
//			// tag 체크
//			this.rtnMap.put("msg", alertMessage.getAdminTagTagError());
//			this.rtnMap.put("elementID", "tag_check");
//		}else if(false){
		if(false){
			// 추가 가능
		}else{
			this.rtnMap.put("res", true);
		}
		
		return this.rtnMap;
	}
	
	// 컨텐츠라벨 등록/수정 체크(validation)
	public Map<String, Object> labelProductEditorForm(Map<String, Object> paramMap){
//		if(!paramMap.containsKey("tag") || paramMap.get("tag") == null || ((List<String>)paramMap.get("tag")).size()<=0 || ((List<String>)paramMap.get("tag")).size()>3){
//			// tag 체크
//			this.rtnMap.put("msg", alertMessage.getAdminTagTagError());
//			this.rtnMap.put("elementID", "tag_check");
//		}else if(false){
		if(false){
			// 추가 가능
		}else{
			this.rtnMap.put("res", true);
		}
		
		return this.rtnMap;
	}
	
	// Admin Tag 등록/수정 체크(validation)
	public Map<String, Object> adminTagEditorForm(Map<String, Object> paramMap){
//		if(!paramMap.containsKey("tag") || paramMap.get("tag") == null || ((List<String>)paramMap.get("tag")).size()<=0 || ((List<String>)paramMap.get("tag")).size()>3){
//			// tag 체크
//			this.rtnMap.put("msg", alertMessage.getAdminTagTagError());
//			this.rtnMap.put("elementID", "tag_check");
//		}else if(false){
		if(false){
			// 추가 가능
		}else{
			this.rtnMap.put("res", true);
		}
		
		return this.rtnMap;
	}
	
	// 컬러맵 등록/수정 체크(validation)
	public Map<String, Object> colorListEditorForm(Map<String, Object> paramMap){
//		if(!paramMap.containsKey("tag") || paramMap.get("tag") == null || ((List<String>)paramMap.get("tag")).size()<=0 || ((List<String>)paramMap.get("tag")).size()>3){
//			// tag 체크
//			this.rtnMap.put("msg", alertMessage.getAdminTagTagError());
//			this.rtnMap.put("elementID", "tag_check");
//		}else if(false){
		if(false){
			// 추가 가능
		}else{
			this.rtnMap.put("res", true);
		}
		
		return this.rtnMap;
	}
	
	// 사이즈맵 등록/수정 체크(validation)
	public Map<String, Object> sizeListEditorForm(Map<String, Object> paramMap){
//		if(!paramMap.containsKey("tag") || paramMap.get("tag") == null || ((List<String>)paramMap.get("tag")).size()<=0 || ((List<String>)paramMap.get("tag")).size()>3){
//			// tag 체크
//			this.rtnMap.put("msg", alertMessage.getAdminTagTagError());
//			this.rtnMap.put("elementID", "tag_check");
//		}else if(false){
		if(false){
			// 추가 가능
		}else{
			this.rtnMap.put("res", true);
		}
		
		return this.rtnMap;
	}
	
}