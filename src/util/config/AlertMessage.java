package util.config;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class AlertMessage{
	public AlertMessage(){
	}
	//*******************기본알림*******************/
	private String InputError = "입력값이 올바르지 않습니다.";				//	입력값오류
	private String fileUploadError = "파일 첨부가 실패 하였습니다.";
	private String accessError = "잘못된 접근입니다.";
	private String deleteOK = "삭제 되었습니다.";
	
	//*******************로그인관련*******************/
	private String LoginError = "입력하신 내용과 일치하는 정보를 찾을 수 없습니다.\\n아이디와 비밀번호를 확인해주시기 바랍니다.";
//    private String okMSG = "처리 되었습니다.";
//    private String failMSG = "실패하였습니다.";
//    private String expireMSG = "만료된 페이지 입니다. 다시 등록하여 주십시요.";
//    private String retryMSG = "잠시후 다시 시도 하여 주십시요.";
//    private String timeoutMSG = "수정 제한시간이 초과 되었습니다.\\n관리자에게 요청하여 주십시요.";
//    private String findPasswordSuccess = "가입시 입력하신 메일 주소로 변경된 임시번호가 발송되었습니다.\\n\\n대단히 감사합니다.";
//    private String findPasswordFail = "임시 비밀번호 발송이 실패되었습니다.\\n\\n시스템 관리자 (admin@kipex.or.kr 또는 02-570-5224)에게 문의 하시기 바랍니다.";
//    private String joinErrorMSG = "이미 회원가입 되었거나 잘못된 요청 입니다.";
//    private String falseUrlMSG = "잘못된 접근입니다.";
	
	//*******************입점회원관련*******************/
	private String ShopWriteOK = "입점회원이 등록되었습니다.";
	private String ShopEditOK = "정보가 수정되었습니다.";
	private String ShopIdError = "올바른 아이디를 입력해 주세요.\n아이디는 5~19자의 영문 대 소문자, 숫자만 사용 가능합니다.";
	private String ShopPwError = "올바른 비밀번호를 입력해 주세요.\n비밀번호는 6~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.";
	private String ShopNameError = "올바른 입점 업체명을 입력해 주세요.";
	private String ShopDuplicateError = "이미 등록되어있는 아이디 입니다.";
	private String ShopTelError = "올바른 연락처를 입력해 주세요.";
	
	//*******************일반회원관련*******************/
	private String UserWriteOK = "일반회원이 등록되었습니다.";
	private String UserEditOK = "정보가 수정되었습니다.";
	private String UserIdError = "올바른 아이디를 입력해 주세요.\n아이디는 5~19자의 영문 대 소문자, 숫자만 사용 가능합니다.";
	private String UserPwError = "올바른 비밀번호를 입력해 주세요.\n비밀번호는 6~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.";
	private String UserEmailError = "올바른 이메일을 입력해 주세요.";
	private String UserDuplicateError = "이미 등록되어있는 아이디 입니다.";
	private String UserTelError = "올바른 연락처를 입력해 주세요.";
	
	//*******************FAQ관련*******************/
	private String FaqWriteOK = "자주하는질문이 등록되었습니다.";
	private String FaqUpdateOK = "자주하는질문이 수정되었습니다.";
	private String FaqTitleError = "올바른 제목을 입력해 주세요.";
	private String FaqContentError = "내용을 입력해 주세요.";
	
	//*******************QNA관련*******************/
	private String QnaReplyWriteOK = "답변이 등록되었습니다.";
	private String QnaReplyUpdateOK = "답변이 수정되었습니다.";
	private String QnaReplyError = "내용을 입력해 주세요.";
	
	//*******************일반상품관련*******************/
	private String GeneralProductWriteOK = "상품이 등록되었습니다.";
	private String GeneralProductUpdateOK = "상품이 수정되었습니다.";
	private String GeneralProductShopError = "올바른 업체를 검색해 주세요.";
	private String GeneralProductNameError = "올바른 상품명을 입력해 주세요.";
	private String GeneralProductCategoryError = "올바른 상품분류를 선택해 주세요.";
	private String GeneralProductPriceError = "올바른 가격을 입력해 주세요.";
	private String GeneralProductColorError = "올바른 색상을 선택해 주세요.";
	private String GeneralProductSizeError = "올바른 사이즈를 선택해 주세요.";
	private String GeneralProductMainImageError = "메인이미지를 등록해 주세요.";
	private String GeneralProductSubImageError = "서브이미지를 등록해 주세요.";
	private String GeneralProductDetailImageError = "상세이미지를 등록해 주세요.";
	private String GeneralProductCodiImageError = "코디이미지를 등록해 주세요.";
	private String GeneralProductTagError = "올바른 태그를 입력해 주세요.";
	private String GeneralProductStateError = "올바른 상태를 입력해 주세요.";
	
	//*******************이벤트 배너 관련*******************/
	private String EventBannerWriteOK = "배너가 등록되었습니다.";
	private String EventBannerUpdateOK = "배너가 수정되었습니다.";
	private String EventBannerTypeError = "올바른 타입을 선택해 주세요.";
	private String EventBannerImageError = "배너이미지를 등록해 주세요.";
	private String EventBannerProductError = "올바른 상품을 선택해 주세요.";
	
	//*******************프로모션 관련*******************/
	private String PromotionWriteOK = "프로모션이 등록되었습니다.";
	private String PromotionUpdateOK = "프로모션이 수정되었습니다.";
	private String PromotionNameError = "프로모션 이름을 입력해 주세요.";
	private String PromotionImageError = "프로모션 이미지를 등록해 주세요.";
	
	//*******************프로모션컨텐츠 관련*******************/
	private String PromotionContentWriteOK = "프로모션컨텐츠가 등록되었습니다.";
	private String PromotionContentUpdateOK = "프로모션컨텐츠가 저장되었습니다.";
//	private String MainLabelError = "올바른 태그를 입력해 주세요.";
	
	//*******************태그 관련*******************/
	private String AdminTagWriteOK = "태그가 등록되었습니다.";
	private String AdminTagUpdateOK = "태그가 저장되었습니다.";
	private String AdminTagTagError = "올바른 태그를 입력해 주세요.";
	
	//*******************라벨 관련*******************/
	private String MainLabelWriteOK = "라벨이 등록되었습니다.";
	private String MainLabelUpdateOK = "라벨이 저장되었습니다.";
//	private String MainLabelError = "올바른 태그를 입력해 주세요.";
	
	//*******************라벨컨텐츠 관련*******************/
	private String LabelProductWriteOK = "라벨컨텐츠가 등록되었습니다.";
	private String LabelProductUpdateOK = "라벨컨텐츠가 저장되었습니다.";
//	private String MainLabelError = "올바른 태그를 입력해 주세요.";
	
	//*******************사이즈맵 관련*******************/
	private String SizeListWriteOK = "사이즈맵이 등록되었습니다.";
	private String SizeListUpdateOK = "사이즈맵이 저장되었습니다.";
//	private String MainLabelError = "올바른 태그를 입력해 주세요.";
	
	//*******************색상맵 관련*******************/
	private String ColorListWriteOK = "색상맵이 등록되었습니다.";
	private String ColorListUpdateOK = "색상맵이 저장되었습니다.";
//	private String MainLabelError = "올바른 태그를 입력해 주세요.";
}