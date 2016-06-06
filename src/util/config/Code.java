package util.config;

import java.util.LinkedHashMap;
import java.util.Calendar;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class Code{
    public static int deFaultcountPerPage = 10;			//	한화면에 보여줄 리스트 개수
    public static int countPerPage_App = 20;		//	한화면에 보여줄 리스트 개수(앱전용)
    public static int pagingGroupNum = 4;		//	중앙기준 앞뒤로 보여질 페이지번호 수

    private LinkedHashMap countPerPageMap = new LinkedHashMap() {{  	//	연락처 앞자리
    	put("5","5");
    	put("10","10");
    	put("15","15");
    	put("20","20");
    	put("25","25");
    	put("30","30");
    	put("35","35");
    	put("40","40");
    	put("45","45");
    	put("50","50");
    }};

    private LinkedHashMap telNumberMap = new LinkedHashMap() {{  	//	연락처 앞자리
    	put("02","02");
    	put("031","031");
    	put("032","032");
    	put("033","033");
    	put("041","041");
    	put("042","042");
    	put("043","043");
    	put("044","044");
    	put("051","051");
    	put("052","052");
    	put("053","053");
    	put("054","054");
    	put("055","055");
    	put("061","061");
    	put("062","062");
    	put("063","063");
    	put("064","064");
    	put("010","010");
    	put("011","011");
    	put("016","016");
    	put("017","017");
    	put("018","018");
    	put("019","019");
    	put("070","070");
    }};
    
    //	상품 카테고리
    private LinkedHashMap productCategoryMap = new LinkedHashMap() {{
    	put(1,"TOP");
    	put(2,"PANTS");
    	put(3,"SKIRT");
    	put(4,"ONEPIECE");
    	put(5,"OUTER");
    	put(6,"DRESS");
    	put(7,"ACCESSORIE");
    	put(8,"SHOES");
    }};
    
    //첨부 가능한 이미지 파일 확장자 모음
    private LinkedHashMap attachImageFileExtMap = new LinkedHashMap() {{
    	put("jpeg","jpeg");
    	put("jpg","jpg");
    	put("gif","gif");
    	put("bmp","bmp");
    	put("png","png");
    }};
    
    //	배너 형식
    private LinkedHashMap eventBannerTypeMap = new LinkedHashMap() {{
    	put(1,"상단배너");
    	put(2,"하단배너");
    }};
}