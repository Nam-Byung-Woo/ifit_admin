package action.basic;

import java.util.*;
import java.io.*;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import util.config.*;
import util.system.AESCrypto;
import util.system.MySqlFunction;
import util.system.StringUtil;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import dto.EachOrderDTO;
import dto.PayListDTO;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import lombok.Data;

@Data
public class ExcelManager extends ActionSupport  {
	private Map session;
	private ActionContext context;
	
	private InputStream excelStream;
	private Map<String, Object> excelChainMap = new HashMap<String, Object>();
	private String fileName;
	
	public ExcelManager() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	//	요소 초기화 및 세팅
	public void init(){
		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
	}
	
	public String excelExport(){
		init();
		this.fileName = this.excelChainMap.get("fileName").toString();
		
		try{
			// 한글파일명 깨짐 방지
			this.fileName = new String (this.fileName.getBytes("KSC5601"), "8859_1");
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		makeFile();
		return SUCCESS;		
	}
	
	private void makeFile(){
		String type = this.excelChainMap.get("type").toString();
		String head[] = null; 
		
		//워크북 생성
		XSSFWorkbook xssfWorkBook = new XSSFWorkbook();
		//워크시트 생성
		XSSFSheet xssfSheet = xssfWorkBook.createSheet();
		//시트 이름
		xssfWorkBook.setSheetName(0 , "Sheet1");
		//행생성
		XSSFRow xssfRow = null;
		//셀 생성
		XSSFCell xssfCell = null;
		
		//전체스타일
		XSSFCellStyle xssfDefaultStyle = xssfWorkBook.createCellStyle();
		xssfDefaultStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		xssfDefaultStyle.setBottomBorderColor(IndexedColors.BLACK.index);
		xssfDefaultStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		xssfDefaultStyle.setLeftBorderColor(IndexedColors.BLACK.index);
		xssfDefaultStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		xssfDefaultStyle.setRightBorderColor(IndexedColors.BLACK.index);
		xssfDefaultStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		xssfDefaultStyle.setTopBorderColor(IndexedColors.BLACK.index);
		
		//헤더스타일
		XSSFCellStyle xssfHeaderStyle = xssfWorkBook.createCellStyle();
		xssfHeaderStyle.setFillBackgroundColor(IndexedColors.WHITE.index);
		xssfHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		xssfHeaderStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		xssfHeaderStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		xssfHeaderStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		xssfHeaderStyle.setBottomBorderColor(IndexedColors.BLACK.index);
		xssfHeaderStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		xssfHeaderStyle.setLeftBorderColor(IndexedColors.BLACK.index);
		xssfHeaderStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		xssfHeaderStyle.setRightBorderColor(IndexedColors.BLACK.index);
		xssfHeaderStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		xssfHeaderStyle.setTopBorderColor(IndexedColors.BLACK.index);
		
		//금액스타일
		XSSFCellStyle xssfMoneyStyle = xssfWorkBook.createCellStyle();
		short xssfMoneyFormat = xssfWorkBook.createDataFormat().getFormat("#,##0");
		xssfMoneyStyle.setDataFormat(xssfMoneyFormat);
		xssfMoneyStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		xssfMoneyStyle.setBottomBorderColor(IndexedColors.BLACK.index);
		xssfMoneyStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		xssfMoneyStyle.setLeftBorderColor(IndexedColors.BLACK.index);
		xssfMoneyStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		xssfMoneyStyle.setRightBorderColor(IndexedColors.BLACK.index);
		xssfMoneyStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		xssfMoneyStyle.setTopBorderColor(IndexedColors.BLACK.index);
		
		/////////////////////////////////////////////////////////////////////////////////
		// 데이터 입력
		if(type.equals("orderList")){
			List <PayListDTO>data = (List<PayListDTO>)this.excelChainMap.get("data");
			head = new String[6];
			head[0] = "주문번호";
			head[1] = "상품명";
			head[2] = "주문회원";
			head[3] = "총 금액";
			head[4] = "상태";
			head[5] = "주문일";
			
			String body[] = new String[6];

			int row = 1;
			for(PayListDTO p : data){
				xssfRow = xssfSheet.createRow((short)row);
				body[0] = p.getPay_seq() + "";
				body[1] = p.getEachOrderList().size()>0 ? (p.getEachOrderList().get(0).getP_name() + (p.getEachOrderList().size()>1 ? (" 외 " + (p.getEachOrderList().size()-1) + "건") : "")) : "";
				body[2] = p.getUser_id();
				int totalPrice = 0;
				for(EachOrderDTO e : p.getEachOrderList()){
					totalPrice += (e.getPrice()*e.getAmount());
				}
				body[3] = totalPrice + "";
				body[4] = p.getEachOrderList().size()>0 ? (p.getEachOrderList().get(0).getState() == 1 ? "접수대기" : "배송처리완료") : "배송처리완료";
				body[5] = p.getOrder_date();
				
				for(int i=0; i<body.length; i++){
					xssfCell = xssfRow.createCell((short)i);
					xssfCell.setCellValue(body[i]);
					xssfCell.setCellStyle(xssfDefaultStyle);
				}
				
				xssfRow.getCell(0).setCellValue(Double.parseDouble(xssfRow.getCell(0).toString()));
				xssfRow.getCell(3).setCellValue(Double.parseDouble(xssfRow.getCell(3).toString()));
				xssfRow.getCell(3).setCellStyle(xssfMoneyStyle);
//				objRow.getCell(3).setCellType(Cell.CELL_TYPE_NUMERIC);
				
				row++;
			}
			//----------------------------------------------------------------------------------------
			//길이 설정(1000=27px)
			xssfSheet.setColumnWidth((short)0,(int)(72*37.03));
			xssfSheet.setColumnWidth((short)1,(int)(99*37.03));
			xssfSheet.setColumnWidth((short)2,(int)(147*37.03));
			xssfSheet.setColumnWidth((short)3,(int)(74*37.03));
			xssfSheet.setColumnWidth((short)4,(int)(104*37.03));
			xssfSheet.setColumnWidth((short)5,(int)(186*37.03));
			//--------------------------------------------------------------------------------------
			
		}
		/////////////////////////////////////////////////////////////////////////////////					

		
		// head 입력 //
		xssfRow = xssfSheet.createRow((short)0);
		for(int i=0; i<head.length; i++){
			xssfCell = xssfRow.createCell((short)i);
			xssfCell.setCellValue(head[i]);
			xssfCell.setCellStyle(xssfHeaderStyle);
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			xssfWorkBook.write(baos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.excelStream = new ByteArrayInputStream(baos.toByteArray());
	}
}
