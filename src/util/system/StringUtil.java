package util.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StringUtil {
	
	/**
	 * TODO property정보를 가져온다
	 * 
	 * @param fileName : 대상 property 파일 이름 <br>
	 * @param key : 가져올 속성(key)값 <br>
	 * @return String <br>
	 */
	public static String getPropertiesValue(String fileName,String Key){
		String propertiesValue = "";
		try{
			InputStream is = StringUtil.class.getResourceAsStream("/"+fileName);
			Properties props = new Properties();
			props.load(is);
			propertiesValue = props.get(Key).toString();
		}catch(Exception e){}
		
		return propertiesValue;
	}
	
	/**
	 * TODO NVL 함수처럼 null인 것을 다른 문자로 치환한다.
	 * 
	 * @param str1	: 비교할 문자 <br>
	 * @param str2	: 치환할 문자 <br>
	 * @return String <br>
	 */
	public static String isNullOrSpace(String str1, String str2) {
		return ((str1 == null || str1.length() <= 0 || str1.equals("null")) ? str2
				: str1);
	}
	
	/**
	 * TODO string to boolean 변환
	 * 
	 * @param str : 변환할 문자 <br>
	 * @return boolean <br>
	 */
	public static boolean stringToBool(String str){
		return Boolean.valueOf(str).booleanValue();
	}	
	
	/**
	 * TODO string to boolean 변환
	 * 
	 * @param l : 변환할 long데이터 <br>
	 * @return int <br>
	 */
	public static int longToInt(long l) {
	    int i = (int)l;
	    if ((long)i != l) {
	        throw new IllegalArgumentException(l + " cannot be cast to int without changing its value.");
	    }
	    return i;
	}
	
	/**
	 * TODO 객체에 gson형식으로 다른 객체 추가
	 * 
	 * @param str1 : 원본gson문자열 <br>
	 * @param str2 : 추가할gson문자열 값 <br>
	 * @param str3 : 추가할gson문자열 키 <br>
	 * @return String <br>
	 */
	public static String addObjectGson(String str1, String str2, String str3){
		return str1.substring(0, str1.length()-1) + ",\"" + str3 + "\":" + str2 + "}";
	}	
	
	/**
	 * TODO 아이디 validation 체크.
	 * 
	 * @param str : 체크할 아이디 <br>
	 * @return boolean <br>
	 */
	public boolean id_validation(String str){
		// 5~19자의 영문 대 소문자, 숫자 사용 가능
		String strRegex = "^[A-Za-z0-9]{5,19}$";
		return str.matches(strRegex);
	}
	
	/**
	 * TODO 패스워드 validation 체크.
	 * 
	 * @param str : 체크할 패스워드 <br>
	 * @return boolean <br>
	 */
	public boolean pw_validation(String str){
		// 6~16자의 영문 대 소문자, 숫자, 특수문자 사용 가능
		String strRegex = "^[A-Za-z0-9~!@#$%<>^&*()-=+_\']{6,16}$";
		return str.matches(strRegex);
	}	
	
	/**
	 * TODO 휴대폰번호 validation 체크.
	 * 
	 * @param num1 : 휴대폰번호 앞자리 <br>
	 * @param num2 : 휴대폰번호 중간자리 <br>
	 * @param num3 : 휴대폰번호 뒷자리 <br>
	 * @param num1Map : 휴대폰번호 앞자리가 명시되어있는 맵
	 * @return boolean <br>
	 */
	public boolean mobileNumber_validation(String num1, String num2, String num3, LinkedHashMap num1Map){
		String strRegex = "[0-9]{3,4}";
		int falseCnt = 0;
		falseCnt = num1Map.containsKey(num1) ? falseCnt : falseCnt+1;
		falseCnt = num2.matches(strRegex) ? falseCnt : falseCnt+1;
		strRegex = "[0-9]{4}";
		falseCnt = num3.matches(strRegex) ? falseCnt : falseCnt+1;
		return falseCnt > 0  ? false : true;
	}
	
	/**
	 * TODO 16진수 색상 체크.
	 * 
	 * @param strList : 색상값 <br>
	 * @return boolean <br>
	 */
	public boolean colorHex_validation(List<String> strList){
		for(String code : strList){
			String strRegex = "^#?([a-f0-9]{6}|[a-f0-9]{3})$";
			if(!code.matches(strRegex)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * TODO String -> Hex 변환.
	 * 
	 * @param str : 변환할 String <br>
	 * @return String <br>
	 */
	public static String stringToHex(String str) {
		char[] chars = str.toCharArray();
		StringBuffer strBuffer = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			strBuffer.append(Integer.toHexString((int) chars[i]));
		}
		return strBuffer.toString();
	}
	
	/**
	 * TODO Hex -> String 변환.
	 * 
	 * @param hexText : 변환할 hexText <br>
	 * @return String <br>
	 */
	public static String hexToString(String hexText) {

		String decodedText = "";
		String chunk = null;

		if (hexText != null && hexText.length() > 0) {
			int numBytes = hexText.length() / 2;

			byte[] rawToByte = new byte[numBytes];
			int offset = 0;
			int bCounter = 0;
			for (int i = 0; i < numBytes; i++) {
				chunk = hexText.substring(offset, offset + 2);
				offset += 2;
				rawToByte[i] = (byte) (Integer.parseInt(chunk, 16) & 0x000000FF);
			}
			decodedText = new String(rawToByte);
		}
		return decodedText;
	}
	
	/**
	 * TODO int형 문자열 숫자 제외한 모든 문자 제거.
	 * 
	 * @param str : 변환할 str <br>
	 * @return String <br>
	 */
	public static String removeStrType(String str) {
		String strRegex = "[^0-9]";
		return str.replaceAll(strRegex, "");
	}
	
	/**
	 * TODO 10진수 정수 체크
	 * 
	 * @param str : 체크할 str <br>
	 * @return boolean <br>
	 */
	public static boolean isStringInt(String str) {
		try{
			Integer.parseInt(str);
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * TODO list -> string으로 변환 (java8에 추가됨 String.join())
	 * 
	 * @param list : 변환할 리스트 <br>
	 * @return String <br>
	 */
	public static String arrayToString(List list) {
		String temp = list.toString();
		temp = temp.replaceAll("\\s", "");
		temp = temp.substring(1, temp.length()-1);
		return temp;
	}
	
//	/**
//	 * TODO 이름 validation 체크.
//	 * 
//	 * @param str : 체크할 이름 <br>
//	 * @return boolean <br>
//	 */
//	public static boolean name_validation(String str){
//		String strRegex = "[가-힣]{2,10}";
//		return str.matches(strRegex);
//	}	
//
//	/**
//	 * TODO 이메일 validation 체크.
//	 * 
//	 * @param str : 체크할 이메일 <br>
//	 * @return boolean <br>
//	 */
//	public boolean email_validation(String str){
//		String strRegex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
//		return str.matches(strRegex);
//	}
//	
//	/**
//	 * TODO 생년월일 validation 체크.
//	 * 
//	 * @param str : 체크할 생년월일 <br>
//	 * @return boolean <br>
//	 */
//	public static boolean birth_validation(String str){
//		String strRegex = "^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])-([123]0|[012][1-9]|31)$";
//		return str.matches(strRegex);
//	}
//	
//	/**
//	 * TODO 주소 validation 체크.
//	 * 
//	 * @param str1 : 우편번호 <br>
//	 * @param str2 : 상세주소 <br>
//	 * @return boolean <br>
//	 */
//	public static boolean address_validation(String str1, String str2){
//		String strRegex = "^([0-9]{3})-([0-9]{3})$";
//		return str1.matches(strRegex) && str2.length() < 100;
//	}
//	
//	
//	/**
//	 * TODO String -> int 변환.
//	 * 
//	 * @param str : 변환할 String <br>
//	 * @param intStr : 변환불가일 경우 return할 int <br>
//	 * @return String <br>
//	 */
//	public static int parseInt(String str, int intStr) throws IOException {
//		str = isNullOrSpace(str, "");
//		str = str.trim();
//
//		if (str.length() <= 0)
//			return intStr;
//
//		int charLength = str.length();
//
//		for (int i = 0; i < charLength; i++) {
//			if (Character.getType(str.charAt(i)) != 9) {
//				return intStr;
//			}
//		}
//		return Integer.parseInt(str);
//	}
//	
//	
//	/**
//	 * TODO 문자열 치환
//	 * 
//	 * @param str
//	 * @param pattern
//	 * @param replace
//	 * @return <br>
//	 */
//	public static String strReplace(String str, String pattern, String replace) {
//		int s = 0; // 찾기 시작할 위치
//		int e = 0; // StringBuffer에 append 할 위치
//
//		if (str == null || str.equals(""))
//			return "";
//
//		StringBuffer buffer = new StringBuffer(str);
//		buffer.delete(0, buffer.length());
//
//		while ((e = str.indexOf(pattern, s)) >= 0) {
//			buffer.append(str.substring(s, e));
//			buffer.append(replace);
//			s = e + pattern.length();
//		}
//		buffer.append(str.substring(s));
//		return buffer.toString();
//	}
//	
//	public static String arrayToString(String[][] arrayStr, int col, String joinStr) {		//2차원 배열 전용
//		String returnVal = "";
//		if (arrayStr == null)
//			return "0";
//
//		for (int i = 0; i < arrayStr.length; i++) {
//			if (arrayStr[i][col] == null)
//				arrayStr[i][col] = "0";
//			if (i == 0) {
//				returnVal += arrayStr[i][col];
//			} else {
//				returnVal += joinStr + arrayStr[i][col];
//			}
//		}
//		return returnVal;
//	}
//	
//	//	xml형태 찾기
//	public static String xmlDeclareRemove(String xmlStr){
//	    String rtnStr = "";
//	    String xmlRegex = "^\\s*<\\?xml.*\\?>";
//	    Matcher matcher = Pattern.compile(xmlRegex).matcher(xmlStr);
//	    if(matcher.find()){
//	    	rtnStr = xmlStr.replace(matcher.group(), "");
//	    }
//	    
//	    return rtnStr;
//	}
//	
//	/**
//	 * TODO 도메인 체크.
//	 * 
//	 * @param str : 체크할 이름 <br>
//	 * @return boolean <br>
//	 */
//	public static String domain_check(String urlStr){
//		String rtnStr = "";
//
//		String domain="";
//		
//		Pattern urlPattern = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");
//		
//		Matcher matcher = urlPattern.matcher(urlStr);
//	    
//		if(matcher.matches()){
//			domain = matcher.group(2);
//	    }
//		
//		if(domain.indexOf("app") > -1){
//			rtnStr = "APP";
//		}else{
//			rtnStr = "WEB";
//		}
//		
//	    
//	    return rtnStr;
//	}	
//	
//	/**
//	 * TODO action url을 완성한다
//	 * 
//	 * @param path : 확장자 제외한 대상 action path <br>
//	 * @return String <br>
//	 */
//	public static String getCompleteUrl(String path){
//		return path + "." + getPropertiesValue("struts.properties","struts.action.extension");
//	}
//	
//	// 일치하는 문자열(문자)의 수를 구한다.(문자열, 비교값)
//	static public int countEqualStr(String str, String compare){
//		int cnt = 0;
//		int tmpIdx = 0;
//		String tmpStr = str;
//		while(tmpStr.length() > 0){
//			tmpIdx = tmpStr.indexOf(compare);
//			if(tmpIdx > 0){
//				cnt++;
//				tmpStr = tmpStr.substring(tmpIdx);
//			}else{
//				break;
//			}	
//		}
//		return cnt;
//	}
//	
//	// 경영공시관련 사용함수(inputCheck + null치환 + 천단위 콤마)
//	static public String isManage(String str, String blank, boolean comma) {
//		str = inputCheck(str);
//		str = isNullOrSpace(str, blank);
//		if(comma){
//			str = thousandComma(str);
//		}
//		return str;
//	}
//
//	// 천단위 콤마 표시
//	static public String thousandComma(String str)  {
//		String rtnVal = "";
//		char charVal = 0; //숫자여부체크
//		for(int i=1; i<=str.length(); i++){
//			charVal = str.charAt(i-1);
//			if(charVal < 48 || charVal >57){
//				rtnVal = str;
//				break;
//			}
//			if(i > 1 && (i%3)==1){
//				rtnVal = str.charAt(str.length() - i) + "," + rtnVal;
//			}else{ 
//				rtnVal = str.charAt(str.length() - i) + rtnVal;    
//		    }
//		}
//		return rtnVal;
//    }
//
//	/**
//	 * TODO NVL 함수처럼 null인 것을 다른 문자로 치환한다.
//	 * 
//	 * @param str1
//	 * @param str2
//	 * @return int <br>
//	 */
//	public static int isNullOrSpace(String str1, int str2) {
//		return ((str1 == null || str1.length() <= 0) ? str2 : Integer
//				.parseInt(str1));
//	}
//
//	/**
//	 * TODO 한글(2바이트)코드로 변환
//	 * 
//	 * @param Unicodestr
//	 *            한글(2바이트)코드로 변경한 String 변수
//	 * @return Code 한글(2바이트)코드로 변환한 String 변수
//	 */
//	public static String Uni2Ksc(String Unicodestr) {
//		String Code = null;
//
//		try {
//			if (Unicodestr == null)
//				Code = null;
//			else
//				Code = new String(Unicodestr.getBytes("8859_1"), "KSC5601");
//		} catch (UnsupportedEncodingException e) {
//		}
//
//		return Code;
//	}
//
//	/**
//	 * TODO 영문(1바이트)코드로 변환
//	 * 
//	 * @param Unicodestr
//	 *            영문(1바이트)코드로 변환할 String 변수
//	 * @return Code 영문(1바이트)코드로 변환된 String 변수
//	 */
//	public static String Ksc2Uni(String Unicodestr) {
//		String Code = null;
//
//		try {
//			if (Unicodestr == null)
//				Code = null;
//			else
//				Code = new String(Unicodestr.getBytes("KSC5601"), "8859_1");
//		} catch (UnsupportedEncodingException e) {
//		}
//
//		return Code;
//	}
//
//	/**
//	 * @param data
//	 * @return boolean
//	 */
//	public static boolean isEmpty(String data) {
//		return data == null || data.trim().length() == 0;
//	}
//
//	/**
//	 * TODO textarea에 저장된 데이터를 출력할 때 enter를 친 부분에 <br>
//	 * 태그를 붙혀준다.
//	 * 
//	 * @param src
//	 *            원본 String 변수
//	 * @return <br>
//	 *         태그를 붙힌 변수를 돌려준다.
//	 */
//	public static String nl2br(String s) {
//		if (isEmpty(s))
//			return "";
//		StringBuffer stringbuffer = new StringBuffer();
//		char ac[] = s.toCharArray();
//		int i = ac.length;
//		for (int j = 0; j < i; j++)
//			if (ac[j] == '\n')
//				stringbuffer.append("<br/>");
//			else
//				stringbuffer.append(ac[j]);
//
//		return stringbuffer.toString();
//	}
//
//	public static String strRepeat(String s, int num) {
//
//		s = StringUtil.isNullOrSpace(s, "");
//		String returnString = "";
//		for (int i = 0; i < num; i++) {
//			returnString += s;
//		}
//		if (num == 0)
//			returnString = "";
//		return returnString;
//	}
//
//	public static String nl2br2(String s) {
//		if (isEmpty(s))
//			return "";
//		StringBuffer stringbuffer = new StringBuffer();
//		char ac[] = s.toCharArray();
//		int i = ac.length;
//		for (int j = 0; j < i; j++)
//			if (ac[j] == '\n')
//				stringbuffer.append("<br/>");
//			else if (ac[j] == ' ')
//				stringbuffer.append("&nbsp;");
//			else
//				stringbuffer.append(ac[j]);
//
//		return stringbuffer.toString();
//	}
//
//	/**
//	 * html tag off
//	 * 
//	 * @param s
//	 * @return
//	 */
//	public static String tagOff(String s) {
//		if (isEmpty(s))
//			return "";
//		StringBuffer stringbuffer = new StringBuffer();
//		char ac[] = s.toCharArray();
//		int i = ac.length;
//		for (int j = 0; j < i; j++)
//			if (ac[j] == '&')
//				stringbuffer.append("&amp;");
//			else if (ac[j] == '<')
//				stringbuffer.append("&lt;");
//			else if (ac[j] == '>')
//				stringbuffer.append("&gt;");
//			else if (ac[j] == '"')
//				stringbuffer.append("&quot;");
//			else if (ac[j] == '\'')
//				stringbuffer.append("&#039;");
//			else
//				stringbuffer.append(ac[j]);
//
//		return stringbuffer.toString();
//	}
//
//	public static String sqlChange(String s) {
//		s = StringUtil.tagOff(s);
//		s = StringUtil.strReplace(s, ";", "");
//		s = StringUtil.strReplace(s, "'", "''");
//		s = StringUtil.strReplace(s, "--", "");
//		return s;
//	}
//
//	public static String sqlChange2(String s) {
//		// s = StringUtil.tagOff(s);
//		// s = StringUtil.strReplace(s, "'", "''");
//		s = StringUtil.strReplace(s, ";", "");
//		s = StringUtil.strReplace(s, "--", "");
//		return s;
//	}
//	
//	public static String ampChange(String s){//////////////////// 웹접근성 관련 태그 교체 함수
//
//		s = StringUtil.strReplace(s, "&", "&amp;");		// &->&amp;
//
//		return s;
//	}
//	
//	public static String webChange(String s){//////////////////// 웹접근성 관련 태그 교체 함수
////		s = StringUtil.strReplace(s, "<br>", "<br/>");
////		s = StringUtil.strReplace(s, "<BR>", "<BR/>");		// 개행 태그 닫아주기
////		s = StringUtil.strReplace(s, "&", "&amp;");		// &->&amp;
////		
////		s = StringUtil.strReplace(s, "<?xml", "<!xml");
////		s = StringUtil.strReplace(s, "<?XML", "<!XML");		// 오피스 관련 xml 무시
////		
////		s = webImage(s);
////		s = webBr(s);
////		//s = StringUtil.strReplace(s, "<BR>", "<BR/>");
//		return s;
//	}
//	
////	public static String webImage(String s){///////////////////// 웹접근성 관련 이미지 태그 교체 함수
////		int condition = 0;			// 맞는 조건
////		StringBuffer stringbuffer = new StringBuffer();
////		if(s.indexOf("<img") != -1 || s.indexOf("<IMG") != -1){
////			char ac[] = s.toCharArray();
////			int i = ac.length;
////			for (int j = 0; j < i; j++){
////				if (ac[j] == '<' && ac[j+1] == 'I' && ac[j+2] == 'M' && ac[j+3] == 'G'){
////					stringbuffer.append("<");
////					condition = 1;
////				}else if (ac[j] == '<' && ac[j+1] == 'i' && ac[j+2] == 'm' && ac[j+3] == 'g'){
////					stringbuffer.append("<");
////					condition = 1;
////				}else if (condition == 1 && ac[j] == '>'){
////					if(ac[j-1] != '/'){
////						stringbuffer.append("/>");
////					}else{
////						stringbuffer.append(">");
////					}
////					condition = 0;
////				}else{
////					stringbuffer.append(ac[j]);
////				}
////			}
////		}else{
////			char ac[] = s.toCharArray();
////			int i = ac.length;
////			for (int j = 0; j < i; j++){
////				stringbuffer.append(ac[j]);
////			}
////		}
////		return stringbuffer.toString();
////	}
//	
////	public static String webBr(String s){///////////////////// 웹접근성 관련 style포함 br 태그 교체 함수
////		int condition = 0;			// 맞는 조건
////		StringBuffer stringbuffer = new StringBuffer();
////		if(s.indexOf("<br") != -1 || s.indexOf("<BR") != -1){
////			char ac[] = s.toCharArray();
////			int i = ac.length;
////			for (int j = 0; j < i; j++){
////				if (ac[j] == '<' && ac[j+1] == 'B' && ac[j+2] == 'R'){
////					stringbuffer.append("<");
////					condition = 1;
////				}else if (ac[j] == '<' && ac[j+1] == 'b' && ac[j+2] == 'r'){
////					stringbuffer.append("<");
////					condition = 1;
////				}else if (condition == 1 && ac[j] == '>'){
////					if(ac[j-1] != '/'){
////						stringbuffer.append("/>");
////					}else{
////						stringbuffer.append(">");
////					}
////					condition = 0;
////				}else{
////					stringbuffer.append(ac[j]);
////				}
////			}
////		}else{
////			char ac[] = s.toCharArray();
////			int i = ac.length;
////			for (int j = 0; j < i; j++){
////				stringbuffer.append(ac[j]);
////			}
////		}
////		return stringbuffer.toString();
////	}
//
//	public static boolean NumberChk(String str) {
//		char c;
//
//		if (str.equals(""))
//			return false;
//
//		for (int i = 0; i < str.length(); i++) {
//			c = str.charAt(i);
//			if (c < 48 || c > 59) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	/**
//	 * TODO 이메일 형식의 아이디를 받아서 진짜 아이디와 도메인을 분리하여 String[]로 돌려준다.
//	 * 
//	 * @param src
//	 *            이메일 형식의 아이디 예) ksang@useweb.co.kr
//	 * @return String[] 예) users[0] : ksang<br>
//	 *         users[1] : useweb.co.kr
//	 */
//	public static String[] getIDDomain(String src) {
//		String[] users = new String[2];
//		if (src.indexOf("@") == -1) {
//			users[0] = src;
//			users[1] = null;
//		} else {
//			users[0] = src.substring(0, src.indexOf("@"));
//			users[1] = src.substring(src.indexOf("@") + 1, src.length());
//		}
//		return users;
//	}
//
//	/**
//	 * TODO 나이계산
//	 * 
//	 * @param birthday
//	 *            생일에 해당하는 문자열<br>
//	 *            예) "19740927"
//	 * @return 나이에 해당하는 int형 값 예) 18
//	 */
//	public static int getAge(String birthday) {
//		int year, birth, old;
//
//		Date now = new Date();
//		SimpleDateFormat nowYF = new SimpleDateFormat("yyyy");
//		String nowYear = nowYF.format(now);
//
//		String birthYear = birthday.substring(0, 4);
//
//		year = Integer.parseInt(nowYear);
//		birth = Integer.parseInt(birthYear);
//		old = year - birth + 1;
//
//		return old;
//	}
//
//	public static String getYyyyMMddHHmm() {
//
//		Date now = new Date();
//		SimpleDateFormat nowYF = new SimpleDateFormat("yyyyMMddHHmm");
//		String nowString = nowYF.format(now);
//
//		return nowString;
//	}
//
//	public static String getTimeStamp() {
//
//		Date now = new Date();
//		SimpleDateFormat nowYF = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		String nowString = nowYF.format(now);
//
//		return nowString;
//	}
//
//	/**
//	 * TODO '문자를 "문자로 변환,<br>
//	 * \문자를 문자로 변환,<br>
//	 * 문자를 \\문자로 변환한다.<br>
//	 * 
//	 * @param chr
//	 *            변경하고자 하는 원본 문자열
//	 * @return 변경후 문자열
//	 */
//	static public String chkStr(String chr) {
//		while (chr.indexOf("'") != -1) {
//			String tmp = chr;
//			chr = tmp.substring(0, tmp.indexOf("'"));
//			chr += "\"";
//			chr += tmp.substring(tmp.indexOf("'") + 1, tmp.length());
//		}
//
//		while (chr.indexOf("\\") != -1) {
//			String tmp2 = chr;
//			chr = tmp2.substring(0, tmp2.indexOf("\\"));
//			chr += "";
//			chr += tmp2.substring(tmp2.indexOf("\\") + 1, tmp2.length());
//		}
//
//		while (chr.indexOf("") != -1) {
//			String tmp2 = chr;
//			chr = tmp2.substring(0, tmp2.indexOf(""));
//			chr += "\\\\";
//			chr += tmp2.substring(tmp2.indexOf("") + 1, tmp2.length());
//		}
//
//		return chr;
//	}
//
//	/**
//	 * TODO 영문과 숫자로 된 올바른 아이디인가를 체크하여 boolean값으로 돌려준다.
//	 * 
//	 * @param UserID
//	 *            체크하고자 하는 아이디
//	 * @return boolean 영문과 숫자로 구성된 아이디가 아니면 false<br>
//	 *         영문과 숫자로 구성되어 있다면 true
//	 */
//	static public boolean checkID(String UserID) {
//		boolean returnValue = true;
//
//		if (UserID.length() <= 0)
//			return false;
//
//		UserID = UserID.toLowerCase();
//		int charLength = UserID.length();
//
//		for (int i = 0; i < charLength; i++) {
//			if (Character.getType(UserID.charAt(i)) != 2
//					&& Character.getType(UserID.charAt(i)) != 9)
//				returnValue = false;
//
//			if (!returnValue)
//				break;
//		}
//
//		return returnValue;
//	}
//
//	/**
//	 * TODO 다름 장비에서 화면에 출력되는 값을 가져오기
//	 * 
//	 * @param cgi
//	 *            페이지 URL
//	 * @return String 화면에 출력되는 값
//	 */
//	public String GetHttp(String cgi) {
//		String ret = "";
//		try {
//			URL cgiURL = new URL(cgi);
//			BufferedReader cin = new BufferedReader(new InputStreamReader(
//					cgiURL.openStream()));
//			String inputLine;
//			while ((inputLine = cin.readLine()) != null) {
//				ret += inputLine + "\n";
//			}
//		} catch (MalformedURLException e) {
//			ret = "";
//		} catch (IOException e) {
//			ret = "";
//		}
//		return (ret);
//	}
//
//	/**
//	 * @param cgi
//	 * @param param
//	 * @return <br>
//	 */
//	public String GetHttpPost(String cgi, String param) {
//		String ret = "";
//		URL url = null;
//		URLConnection urlc = null;
//		OutputStream urlo = null;
//		InputStream urli = null;
//
//		try {
//			url = new URL(cgi);
//			urlc = url.openConnection();
//			urlc.setDoOutput(true);
//			urlo = urlc.getOutputStream();
//
//			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(
//					urlo));
//			bout.write(param);
//			bout.close();
//
//			urli = urlc.getInputStream();
//			BufferedReader bin = new BufferedReader(new InputStreamReader(urli));
//			String line;
//			while ((line = bin.readLine()) != null)
//				ret = ret + line;
//			bin.close();
//		} catch (Exception e) {
//			ret = "";
//		}
//		
//		return ret;
//	}
//
//
//	/**
//	 * TODO 암호화할 입력값을 입력받아 자체의 특정한 규칙에 따라 Encoding한다.
//	 * 
//	 * @param strIn
//	 *            암호화할 문자열
//	 * @return String 암호화된 문자열
//	 */
//	public static String USEEncode(String strIn) {
//		strIn = strIn + "kg";
//		String retStr = "";
//		for (int i = 0; i < strIn.length(); i++) {
//			retStr += (char) ((int) strIn.charAt(i) + (i % 5) + 1);
//		}
//		return retStr;
//	}
//
//	/**
//	 * TODO 암호된 입력값을 입력받아 자체의 특정한 규칙에 따라 Decoding한다.
//	 * 
//	 * @param strIn
//	 *            암호화된 문자열
//	 * @return String 일반 문자열
//	 */
//	public static String USEDecode(String strIn) {
//		String retStr = "";
//		for (int i = 0; i < (strIn.length()); i++) {
//			retStr += (char) ((int) (strIn.charAt(i)) - (i % 5) - 1);
//		}
//		if (retStr.length() < 2
//				|| !retStr.substring(retStr.length() - 2).equals("kg")) {
//			retStr = "";
//		} else {
//			retStr = retStr.substring(0, retStr.length() - 2);
//
//		}
//		return retStr;
//	}
//
//	/**
//	 * TODO 문자열을 짤라서 출력함. 문자열 , 숫자 를 입력하면 숫자 만큼의 문자열 출력후 <br>
//	 * 나머지 문자열 ... 으로 표시
//	 * 
//	 * @param s
//	 * @param i
//	 * @return <br>
//	 * 
//	 */
//	public static String cutString(String s, int size, String strAdd) {
//		if (s == null)
//			return "";
//		if (s.length() == 0)
//			return "";
//
//		StringBuffer value = new StringBuffer();
//
//		int cnt = 0;
//
//		for (int i = 0; i < s.length(); i++) {
//			if (cnt + 1 <= size)
//				value.append(s.charAt(i));
//
//			if (new Character(s.charAt(i)).hashCode() > 4400)
//				cnt = cnt + 2;
//			else
//				cnt = cnt + 1;
//		}
//
//		if (cnt > size)
//			value.append(strAdd);
//
//		return value.toString();
//	}
//
//	/**
//	 * TODO 왼쪽에 문자열 붙이기 (부족한 만큼) EX) String str = "111"; lpad(str , 5, '2'); ==>
//	 * 11122
//	 * 
//	 * @param str
//	 * @param len
//	 * @param c
//	 * @return <br>
//	 * 
//	 */
//	public static String lpad(String str, int len, char c) {
//		StringBuffer sbuf = new StringBuffer();
//
//		if (str == null) {
//			return null;
//		}
//		for (int i = str.length(); i < len; i++) {
//			sbuf.append(c);
//		}
//		sbuf.append(str);
//		return sbuf.toString();
//	}
//
//	/**
//	 * TODO 오른쪽에 문자열 붙이기 (부족한 만큼) 위와 동일
//	 * 
//	 * @param str
//	 * @param len
//	 * @param c
//	 * @return <br>
//	 * 
//	 */
//	public static String Rpad(String str, int len, char c) {
//		StringBuffer sbuf = new StringBuffer();
//
//		if (str == null) {
//			return null;
//		}
//		sbuf.append(str);
//		for (int i = str.length(); i < len; i++) {
//			sbuf.append(c);
//			sbuf.append(c);
//			sbuf.append(c);
//		}
//		return sbuf.toString();
//	}
//
//	/**
//	 * TODO 동일한 문자열이 있는지 체크
//	 * 
//	 * @param temp
//	 * @param temp2
//	 * @return boolean <br>
//	 */
//	public static boolean match(String temp, String temp2) {
//		try {
//
//			int k = 0;
//			for (int i = 0; i < temp.length(); i++) {
//				if (temp.charAt(i) == temp2.charAt(0)) {
//					k = 0;
//					for (int j = 0; j < temp2.length(); j++) {
//						if (temp.charAt(i + j) == temp2.charAt(j)) {
//							k++;
//						}
//						if (k == temp2.length()) {
//							return true;
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//		}
//		return false;
//
//	}
//
//	/**
//	 * TODO len 길이만큼의 문자를 왼쪽부터 채운다.<br>
//	 * getFilledString("123","0",5) --> "00123"
//	 * 
//	 * @param src
//	 * @param value
//	 * @param len
//	 * @return <br>
//	 */
//	public static String getFilledString(String src, String value, int len) {
//
//		StringBuffer result = new StringBuffer();
//
//		if (len <= src.length())
//			return src;
//
//		for (int i = 0; i < len - src.length(); i++) {
//			result.append(value);
//		}
//		result.append(src);
//
//		return result.toString();
//
//	}
//
//	/**
//	 * TODO beLeft : true : 왼쪽부터 채운다. false : 오른쪽부터 채운다.
//	 * 
//	 * @param src
//	 * @param value
//	 * @param len
//	 * @param beLeft
//	 * @return <br>
//	 * 
//	 */
//	public static String getFilledString(String src, String value, int len,
//			boolean beLeft) {
//
//		if (beLeft)
//			return getFilledString(src, value, len);
//
//		StringBuffer result = new StringBuffer();
//
//		if (len <= src.length())
//			return src;
//
//		result.append(src);
//		for (int i = 0; i < len - src.length(); i++) {
//			result.append(value);
//		}
//
//		return result.toString();
//
//	}
//
//	/**
//	 * 사용자가 요청한 페이지의 URI값을 이용 파일 이름( 확장자 포함 )만 추출하는 메쏘드
//	 * 
//	 * @param uri
//	 *            URI 정보
//	 * @return URI에서 파일명( 확장자 포함 )만 리턴
//	 */
//	public static String getRequestFileName(String uri) {
//
//		int firstCutPos = 0;
//
//		firstCutPos = uri.lastIndexOf('/') + 1;
//
//		return uri.substring(firstCutPos);
//	}
//
//	/**
//	 * Decode a string from <code>x-www-form-urlencoded</code> format.
//	 * 
//	 * @param s
//	 *            an encoded <code>String</code> to be translated.
//	 * @return the original <code>String</code>.
//	 * @see java.net.URLEncoder#encode(java.lang.String)
//	 */
//	public static String URLDecode(String s)
//			throws UnsupportedEncodingException {
//		return java.net.URLDecoder.decode(s, "euc-kr");
//		// return java.net.URLEncoder.encode(s);
//
//	}
//
//	/**
//	 * Translates a string into <code>x-www-form-urlencoded</code> format.
//	 * 
//	 * @param s
//	 *            <code>String</code> to be translated.
//	 * @return the translated <code>String</code>.
//	 * @see java.net.URLEncoder#encode(java.lang.String)
//	 */
//	public static String URLEncode(String s)
//			throws UnsupportedEncodingException {
//		return java.net.URLEncoder.encode(s, "euc-kr");
//		// return java.net.URLEncoder.encode(s);
//	}
//
//	/**
//	 * 사용자가 요청한 페이지의 URI값을 이용 파일 이름( 확장자 포함 )만 추출하는 메쏘드
//	 * 
//	 * @param uri
//	 *            URI 정보
//	 * @return URI에서 파일명( 확장자 포함 )만 리턴
//	 */
//	public static String format(long a, long b, int minFract) {
//		NumberFormat nf = NumberFormat.getNumberInstance();
//		nf.setMinimumFractionDigits(minFract);
//		return nf.format((double) a / (double) b);
//	}
//
//	/**
//	 * 숫자를 체크한다.
//	 */
//	public static boolean isNumeric(String str) throws IOException {
//		boolean returnValue = true;
//		str = isNullOrSpace(str.trim(), "");
//
//		if (str.length() <= 0)
//			return false;
//
//		int charLength = str.length();
//
//		for (int i = 0; i < charLength; i++) {
//			if (Character.getType(str.charAt(i)) != 9)
//				returnValue = false;
//
//			if (!returnValue)
//				break;
//		}
//
//		return returnValue;
//	}
//
//
//	/**
//	 * 쿠키 생성한다.
//	 */
//	public static void setCookie(HttpServletResponse response, String name,
//			String value) {
//		value = java.net.URLEncoder.encode(value);
//		Cookie cookie = new Cookie(name, value);
//		// cookie.setDomain("WWW.INTERPARK.COM");
//		// cookie.setDomain("192.168.0.1");
//		// cookie.setMaxAge(60*60*24*15);
//		cookie.setPath("/");
//		response.addCookie(cookie);
//	}
//
//	/**
//	 * 쿠키를 반환한다.
//	 */
//	public static String getCookie(HttpServletRequest request, String cookieName)
//			throws Exception {
//		Cookie[] cookies = request.getCookies();
//		if (cookies == null)
//			return "";
//		String value = "";
//		for (int i = 0; i < cookies.length; i++) {
//			if (cookieName.equals(cookies[i].getName())) {
//				value = java.net.URLDecoder.decode(cookies[i].getValue(),
//						"euc-kr");
//				// value = cookies[i].getValue();
//				break;
//			}
//		}
//		return value;
//	}
//

//
//	public static boolean findArrayToString(String arrayStr, int findStr) {
//
//		if (arrayStr == null)
//			return false;
//
//		String[] ArrayStr = arrayStr.split(",");
//
//		for (int i = 0; i < ArrayStr.length; i++) {
//			if (ArrayStr[i] == null)
//				continue;
//			if (ArrayStr[i].equals(String.valueOf(findStr))) {
//				return true;
//			}
//		}
//
//		return false;
//	}
//
//	public static boolean findArrayToString(String arrayStr, String findStr) {
//
//		if (arrayStr == null)
//			return false;
//
//		String[] ArrayStr = arrayStr.split(",");
//
//		for (int i = 0; i < ArrayStr.length; i++) {
//			if (ArrayStr[i] == null)
//				continue;
//			if (ArrayStr[i].equalsIgnoreCase(findStr)) {
//				return true;
//			}
//		}
//
//		return false;
//	}
//
//	public static boolean findArrayToString(String arrayStr, String findStr,
//			String spliter) {
//		if (arrayStr == null)
//			return false;
//		StringTokenizer tokens = new StringTokenizer(arrayStr, spliter);
//		for (int i = 1; tokens.hasMoreElements(); i++) {
//			if (tokens.nextToken().equalsIgnoreCase(findStr)) {
//				// System.out.println(tokens.nextToken());
//				return true;
//			}
//		}
//		return false;
//	}
//
//	// urlEncode 된 문자비교
//	public static boolean findArrayToString(String[] arrayStr, String findStr) {
//		if (arrayStr == null)
//			return false;
//		String[] ArrayStr = arrayStr;
//		for (int i = 0; i < ArrayStr.length; i++) {
//			if (ArrayStr[i] == null)
//				continue;
//			if (ArrayStr[i].equalsIgnoreCase(findStr)) {
//				return true;
//			}
//		}
//
//		return false;
//	}
//
//	public static int findIndexArrayToString(String arrayStr, String findStr) {
//
//		if (arrayStr == null)
//			return -1;
//
//		String[] ArrayStr = arrayStr.split(",");
//
//		for (int i = 0; i < ArrayStr.length; i++) {
//			if (ArrayStr[i] == null)
//				continue;
//			if (ArrayStr[i].equalsIgnoreCase(findStr)) {
//				return i;
//			}
//		}
//
//		return -1;
//	}
//
//	public static String substr(String str, int firstIndex, int lastIndex) {
//		if (str == null || str.equals(""))
//			return "";
//		if (str.length() < 1)
//			return "";
//		if (firstIndex >= str.length())
//			return str;
//		if (lastIndex > str.length())
//			return str;
//		if (lastIndex != 0) {
//			str = str.substring(firstIndex, lastIndex);
//		} else {
//			str = str.substring(firstIndex);
//		}
//		return str;
//	}
//
//	public static String seleted(String str1, String str2) {
//		if (str1 == null || str2 == null)
//			return "";
//		if (str1.equals(str2))
//			return "selected";
//		else
//			return "";
//	}
//
//	public static String checked(String str1, String str2) {
//		if (str1 == null || str2 == null)
//			return "";
//		if (str1.equals(str2))
//			return "checked";
//		else
//			return "";
//	}
//
//	public static String fileSize(int size) {
//		String rtnVal = "";
//
//		if (size < 1024) {
//			rtnVal = size + " Byte";
//		} else if (size < 1024 * 1024) {
//			rtnVal = Math.round((size / 1024.0) * 10) / 10.0 + " KB";
//		} else {
//			rtnVal = Math.round((size / 1024.0 / 1024.0) * 10) / 10.0 + " MB";
//		}
//		return rtnVal;
//	}
//
//	public static String fillMsg(String str1, String src, String str2) {
//		str1 = StringUtil.isNullOrSpace(str1, "");
//		src = StringUtil.isNullOrSpace(src, "");
//		str2 = StringUtil.isNullOrSpace(str2, "");
//
//		if (src.equals(""))
//			return "";
//
//		return str1 + src + str2;
//	}
//
//	public static String XSS(String str) {
//		str = StringUtil.isNullOrSpace(str, "");
//
//		// str = str.replaceAll("(?i)&", "&amp;");
//		str = str.replaceAll("(?i)<xmp", "<x-xmp");
//		str = str.replaceAll("(?i)javascript", "x-javascript");
//		str = str.replaceAll("(?i)SCRIPT", "x-SCRIPT");
//		// str = str.replaceAll("(?i)iframe", "x-iframe");
//		str = str.replaceAll("(?i)document", "x-document");
//		str = str.replaceAll("(?i)vbscript", "x-vbscript");
//		str = str.replaceAll("(?i)applet", "x-applet");
//		str = str.replaceAll("(?i)embed", "x-embed");
//		str = str.replaceAll("(?i)object", "x-object");
//		str = str.replaceAll("(?i)frame", "x-frame");
//		str = str.replaceAll("(?i)frameset", "x-frameset");
//		str = str.replaceAll("(?i)layer", "x-layer");
//		str = str.replaceAll("(?i)bgsound", "x-bgsound");
//		str = str.replaceAll("(?i)alert", "x-alert");
//		str = str.replaceAll("(?i)import", "x-import");
//		str = str.replaceAll("(?i)style", "x-style");
//		str = str.replaceAll("(?i)link", "x-link");
//		// str = str.replaceAll(str, "&lt;p&gt;", "<P>");
//		// str = str.replaceAll(str, "&lt;br&gt;", "<BR>");
//		// str = str.replaceAll(str, "&lt;img", "<IMG");
//		str = str.replaceAll("(?i)onblur", "x-onblur");
//		str = str.replaceAll("(?i)onchange", "x-onchange");
//		str = str.replaceAll("(?i)onclick", "x-onclick");
//		str = str.replaceAll("(?i)ondblclick", "x-ondblclick");
//		str = str.replaceAll("(?i)onerror", "x-onerror");
//		str = str.replaceAll("(?i)onfocus", "x-onfocus");
//		str = str.replaceAll("(?i)onload", "x-onload");
//		str = str.replaceAll("(?i)onmouse", "x-onmouse");
//		str = str.replaceAll("(?i)onscroll", "x-onscroll");
//		str = str.replaceAll("(?i)onsubmit", "x-onsubmit");
//		str = str.replaceAll("(?i)onunload", "x-onunload");
//
//		return str;
//	}
//
//	public static String adminXSS(String str) {
//		str = StringUtil.isNullOrSpace(str, "");
//		// s.replaceAll("(?i)abc", "ZZZ");
//
//		// str = str.replaceAll("(?i)&", "&amp;");
//		str = str.replaceAll("(?i)<xmp", "<x-xmp");
//		// str = str.replaceAll("(?i)javascript", "x-javascript");
//		// str = str.replaceAll("(?i)SCRIPT", "x-SCRIPT");
//		// str = str.replaceAll("(?i)script", "x-script");
//		// str = str.replaceAll("(?i)iframe", "x-iframe");
//		// str = str.replaceAll("(?i)document", "x-document");
//		str = str.replaceAll("(?i)vbscript", "x-vbscript");
//		str = str.replaceAll("(?i)applet", "x-applet");
//		// str = str.replaceAll("(?i)embed", "x-embed");
//		// str = str.replaceAll("(?i)object", "x-object");
//		str = str.replaceAll("(?i)frame", "x-frame");
//		str = str.replaceAll("(?i)frameset", "x-frameset");
//		str = str.replaceAll("(?i)layer", "x-layer");
//		str = str.replaceAll("(?i)bgsound", "x-bgsound");
//		// str = str.replaceAll("(?i)alert", "x-alert");
//		str = str.replaceAll("(?i)import", "x-import");
//		// str = str.replaceAll("(?i)style", "x-style");
//		// str = str.replaceAll("(?i)link", "x-link");
//		// str = replace(str, "&lt;p&gt;", "<P>");
//		// str = replace(str, "&lt;br&gt;", "<BR>");
//		// str = replace(str, "&lt;img", "<IMG");
//		str = str.replaceAll("(?i)onblur", "x-onblur");
//		str = str.replaceAll("(?i)onchange", "x-onchange");
//		// str = str.replaceAll("(?i)onclick", "x-onclick");
//		str = str.replaceAll("(?i)ondblclick", "x-ondblclick");
//		str = str.replaceAll("(?i)onerror", "x-onerror");
//		str = str.replaceAll("(?i)onfocus", "x-onfocus");
//		str = str.replaceAll("(?i)onload", "x-onload");
//		str = str.replaceAll("(?i)onmouse", "x-onmouse");
//		str = str.replaceAll("(?i)onscroll", "x-onscroll");
//		str = str.replaceAll("(?i)onsubmit", "x-onsubmit");
//		str = str.replaceAll("(?i)onunload", "x-onunload");
//
//		return str;
//	}
//
//	public static String inputCheck(String str) {
//		str = StringUtil.isNullOrSpace(str, "");
//		// s.replaceAll("(?i)abc", "ZZZ");
//		
//		str = str.replaceAll("(?i)&", "&amp;");
//		str = str.replaceAll("(?i)'", "&#39;");
//		str = str.replaceAll("(?i)\"", "&#34;");
//		str = str.replaceAll("(?i)<", "&lt;");
//		str = str.replaceAll("(?i)>", "&gt;");
//		// str = str.replaceAll("(?i)|", "&#124;");
//		return str;
//	}
//
//	public static String inputCheck2(String str) {
//		str = StringUtil.isNullOrSpace(str, "0");
//		// s.replaceAll("(?i)abc", "ZZZ");
//		str = str.replaceAll("(?i)&", "&amp;");
//		str = str.replaceAll("(?i)'", "&#39;");
//		str = str.replaceAll("(?i)\"", "&#34;");
//		str = str.replaceAll("(?i)<", "&lt;");
//		str = str.replaceAll("(?i)>", "&gt;");
//		// str = str.replaceAll("(?i)|", "&#124;");
//		return str;
//	}
//
//	public static String uninputCheck(String str) {
//		str = StringUtil.isNullOrSpace(str, "");
//		// s.replaceAll("(?i)abc", "ZZZ");
//		str = str.replaceAll("(?i)&amp;", "&");
//		str = str.replaceAll("(?i)&#39;", "'");
//		str = str.replaceAll("(?i)&#34;", "\"");
//		str = str.replaceAll("(?i)&lt;", "<");
//		str = str.replaceAll("(?i)&gt;", ">");
//		// str = str.replaceAll("(?i)&#124;","|");
//		return str;
//	}
//
//	public static String searchString(String[] strArr, String findColumn) {
//		String str = "";
//		findColumn = StringUtil.sqlChange2(findColumn);
//		if (strArr != null) {
//			if (strArr.length > 0) {
//				for (int i = 0; i < strArr.length; i++) {
//					strArr[i] = StringUtil.sqlChange2(strArr[i]);
//					str += " AND ','||" + findColumn + " like '%," + strArr[i]
//							+ "%' ";
//				}
//			}
//		}
//		return str;
//	}
//
//	public static String searchStr(String str1, String findColumn) {
//		String str = "";
//		findColumn = StringUtil.sqlChange2(findColumn);
//		if (str1 != null) {
//			if (str1.length() > 0) {
//				str1 = StringUtil.sqlChange2(str1);
//				str = " AND " + findColumn + " like '%" + str1 + "%' ";
//			}
//		}
//		return str;
//	}
//
//	public static String equalStr(String str1, String str2, String str3,
//			String str4) {
//		String rtnStr = "";
//		if (str1 != null && str2 != null) {
//			if (str1.equals(str2)) {
//				rtnStr = str3;
//			} else {
//				rtnStr = str4;
//			}
//		} else {
//			rtnStr = "";
//		}
//		return rtnStr;
//	}
//
//	public static String escape(String string) {
//		StringBuffer sb = new StringBuffer();
//		String ncStr = "*+-./0123456789@ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz";
//		char c;
//
//		for (int i = 0; i < string.length(); i++) {
//			c = string.charAt(i);
//			if (c > 0x7f) {
//				sb.append("%u");
//				sb.append(Integer.toHexString((int) c).toUpperCase());
//			} else if (ncStr.indexOf((int) c) == -1) {
//				sb.append('%');
//				if (c <= 0xf)
//					sb.append('0');
//				sb.append(Integer.toHexString((int) c).toUpperCase());
//			} else
//				sb.append(c);
//		}
//
//		return sb.toString();
//	}
//
//	private static interface Patterns {
//		// javascript tags and everything in between
//		public static final Pattern SCRIPTS = Pattern.compile(
//				"<(no)?script[^>]*>.*?</(no)?script>", Pattern.DOTALL);
//
//		public static final Pattern STYLE = Pattern.compile(
//				"<style[^>]*>.*</style>", Pattern.DOTALL);
//		// HTML/XML tags
//
//		public static final Pattern TAGS = Pattern
//				.compile("<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>");
//
//		public static final Pattern nTAGS = Pattern
//				.compile("<\\w+\\s+[^<]*\\s*>");
//		// entity references
//		public static final Pattern ENTITY_REFS = Pattern.compile("&[^;]+;");
//		// repeated whitespace
//		public static final Pattern WHITESPACE = Pattern.compile("\\s\\s+");
//	}
//
//	/**
//	 * Clean the HTML input.
//	 */
//	public static String HTMLclean(String s) {
//		if (s == null) {
//			return "";
//		}
//
//		Matcher m;
//
//		m = Patterns.SCRIPTS.matcher(s);
//		s = m.replaceAll("");
//		m = Patterns.STYLE.matcher(s);
//		s = m.replaceAll("");
//		m = Patterns.TAGS.matcher(s);
//		s = m.replaceAll("");
//		m = Patterns.ENTITY_REFS.matcher(s);
//		s = m.replaceAll("");
//		m = Patterns.WHITESPACE.matcher(s);
//		s = m.replaceAll(" ");
//
//		return s;
//	}
//
//	public static String ViewHTMLclean(String s) {
//		if (s == null) {
//			return "";
//		}
//
//		Matcher m;
//
//		m = Patterns.SCRIPTS.matcher(s);
//		s = m.replaceAll("");
//		m = Patterns.STYLE.matcher(s);
//		s = m.replaceAll("");
//		m = Patterns.TAGS.matcher(s);
//		s = m.replaceAll("");
//		return s;
//	}
//
//	/**
//	 * 랜덤 숫자를 반환한다.
//	 */
//	public static String randomNumber() {
//		String randomString = "";
//		Random random = new Random();
//		Hashtable ht = new Hashtable();
//		int i = 0;
//		while (true) {
//			int value = random.nextInt(15);
//
//			if (!ht.containsKey(new Integer(value)) && i <= 5) {
//				ht.put(new Integer(value), new Integer(value));
//				i++;
//			} else if (i == 6) {
//				break;
//			}
//		}
//
//		Enumeration enums = ht.keys();
//		while (enums.hasMoreElements()) {
//			randomString += ht.get(enums.nextElement());
//		}
//		return randomString;
//	}
//
//	public static String replace(String sStrString, String sStrOld,
//			String sStrNew) {
//		if (sStrString == null)
//			return null;
//		for (int iIndex = 0; (iIndex = sStrString.indexOf(sStrOld, iIndex)) >= 0; iIndex += sStrNew
//				.length())
//			sStrString = sStrString.substring(0, iIndex) + sStrNew
//					+ sStrString.substring(iIndex + sStrOld.length());
//
//		return sStrString;
//	}
//
//	public static Object[][] getTable(int year, int month) {
//		Calendar cal = Calendar.getInstance();
//		int date = cal.get(Calendar.DATE);
//
//		cal.set(year, month - 1, 1);
//
//		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//		int firstDay = cal.get(Calendar.DAY_OF_WEEK);
//		Object temp[][] = new Object[6][7];
//		int daycount = 1;
//		for (int i = 0; i < 6; i++) {
//			for (int j = 0; j < 7; j++) {
//				if (firstDay - 1 > 0 || daycount > lastDay) {
//					temp[i][j] = "";
//					firstDay--;
//					continue;
//				} else {
//					temp[i][j] = String.valueOf(daycount);
//				}
//				daycount++;
//			}
//		}
//		return temp;
//	}
//
//	static public String findMenuName(String boardNo) {
//		Object menuNameArr[][] = { { "17", "신의료 소식" }, { "6", "언론보도/홍보" },
//				{ "4", "의료기술평가 동향" }, { "24", "의료기술평가관련기구소개" },
//				{ "3", "관련법령" }, { "19", "관련법령(영문)" }, { "9", "평가진행현황" },
//				{ "11", "nHTA FAQ" }, { "20", "nHTA FAQ(영문)" },
//				{ "1", "신의료기술평가보고서" }, { "5", "알기쉬운 신의료기술평가보고서" },
//				{ "28", "보고서 카테고리 관리" }, { "27", "기타 출판물" },
//				{ "7", "학회 및 세미나" }, { "14", "e-뉴스레터 신의료공감" },
//				{ "15", "의혜안(醫慧眼)" }, { "23", "이 달의 연구원" },
//				{ "13", "e-뉴스레터 신청" }, { "10", "nHTA NOTICE" },
//				{ "8", "PUBLICATION" }, { "29", "상담신청 리스트" },
//				{ "30", "전화,대면 상담신청 캘린더" }, { "31", "분자병리검사 DB" },
//				{ "32", "카테고리 관리" }, { "33", "질병별 보고서" }, { "34", "진료과별 보고서" } };
//		String returnVal = "";
//		if (boardNo == null)
//			boardNo = "";
//		for (int i = 0; i < menuNameArr.length; i++) {
//			if (menuNameArr[i][0].equals(boardNo)) {
//				returnVal = menuNameArr[i][1].toString();
//				break;
//			}
//		}
//
//		return returnVal;
//	}
//
//	public static String removeGetQueryString(String queryString,
//			String removeParamName) {
//		if (isEmpty(queryString) || queryString.length() == 1) {
//			return "";
//		} else if (queryString.indexOf("&") == 0) { // & 가 처음에 있다면
//			queryString = queryString.substring(1);
//		}
//
//		String queryStringArr[] = queryString.split("&");
//		String reQueryString = "";
//		for (int k = 0; k < queryStringArr.length; k++) {
//			reQueryString += (queryStringArr[k].indexOf(removeParamName + "=") == 0) ? "" : "&" + queryStringArr[k];
//		}
//
//		return reQueryString;
//	}
//
//
//
//	public static String strip_tags(String str) {
//		return str.replaceAll(
//				"<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
//	}
}