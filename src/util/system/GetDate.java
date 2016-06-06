package util.system;

import java.text.*;
import java.util.*;

public class GetDate {
	
	public GetDate(){}
	public String yymmdd(){
		
		SimpleDateFormat formatter = new SimpleDateFormat ( "yyyyMMddHHmmss", Locale.KOREA );
		Date currentTime = new Date ( );
		String dTime = formatter.format ( currentTime );
		dTime = dTime.substring(2, 4) + dTime.substring(4, 6) + dTime.substring(6, 8);		
		return dTime;		
	}
	
	public String getCurrentYear() {
		SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy", Locale.KOREA );
		Date currentTime = new Date ( );
		String dTime = formatter.format ( currentTime );
		return dTime;
		
	}
	
	public String getCurrentMonth() {
		SimpleDateFormat formatter = new SimpleDateFormat ( "MM", Locale.KOREA );
		Date currentTime = new Date ( );
		String dTime = formatter.format ( currentTime );
		return dTime;
		
	}
	
	public String yyyyMMddHHmmss(){
		
		SimpleDateFormat formatter = new SimpleDateFormat ( "yyyyMMddHHmmss", Locale.KOREA );
		Date currentTime = new Date ( );
		String dTime = formatter.format ( currentTime );
		dTime = dTime.substring(0, 14);
		return dTime;		
	}
	
	public String getDate( int iDay ) {
		Calendar temp=Calendar.getInstance ( );
		StringBuffer sbDate=new StringBuffer ( );
	
		temp.add ( Calendar.DAY_OF_MONTH, iDay );
	
		int nYear = temp.get ( Calendar.YEAR );
		int nMonth = temp.get ( Calendar.MONTH ) + 1;
		int nDay = temp.get ( Calendar.DAY_OF_MONTH );
	
		sbDate.append ( nYear );
		if ( nMonth < 10 ) 
		sbDate.append ( "0" );
		sbDate.append ( nMonth );
		if ( nDay < 10 ) 
		sbDate.append ( "0" );
		sbDate.append ( nDay );
	
		return sbDate.toString ();
	}
	public String yy_mm_dd(){
		
		SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );
		Date currentTime = new Date ( );
		String dTime = formatter.format ( currentTime );	
		return dTime;		
	}

	public String getDateDb(){
		
		SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss", Locale.KOREA );
		Date currentTime = new Date ( );
		String dTime = formatter.format ( currentTime );	
		return dTime;		
	}

	public String getDate4Stats(){
		
		SimpleDateFormat formatter = new SimpleDateFormat ( "MMddHHmmss", Locale.KOREA );
		Date currentTime = new Date ( );
		String dTime = formatter.format ( currentTime );
		return dTime;		
	}
	
	/**
     * 시간을 "년월일" 형태로 구한다.
	 *
	 * @return      예제) "20000405"
	 */
	public String GetDate2() {
		Locale.setDefault(Locale.KOREA);
		java.util.Date cal = new java.util.Date();
		SimpleDateFormat mySDF = new SimpleDateFormat("yyyyMMdd"); 
		String PostDate=mySDF.format(cal);
		return (PostDate);
	}
	
	 /**
	 * TODO 시작 기간과 종료 기간을 받아 기간의 남을 일자를 계산한다.
	 * <br>
	 * @param from
	 * @param to
	 * @return
	 * @throws java.text.ParseException
	 * 
	 * 
	 */
	public int calDaysBetween(String from, String to) throws java.text.ParseException { 
        java.text.SimpleDateFormat formatter = 
        new java.text.SimpleDateFormat ( "yyyyMMdd", java.util.Locale.KOREA); 
        
        java.util.Date d1 = formatter.parse(from); 
        java.util.Date d2 = formatter.parse(to); 
 
        long duration = d2.getTime() - d1.getTime(); 
 
        return (int)( duration/(1000 * 60 * 60 * 24) ); 
        // seconds in 1 day 
    }
	
	
	
	//입력받은 오늘 기준으로 이전 달 날짜를 전달한다. 
	public String getClasDate(int month){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, month);
		
		Date time=cal.getTime();
		SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		
		String dTime = formatter.format ( time );
		return dTime;		
	}
}
