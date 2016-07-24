package action.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import util.config.*;
import util.system.AESCrypto;
import util.system.MySqlFunction;
import util.system.StringUtil;

import action.help.Faq;
import action.help.Qna;
import action.member.ShopMember;
import action.order.Order;
import action.product.GeneralProduct;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import dao.IfitDAO;
import dto.ChartDataDTO;

import lombok.Data;

@Data
public class ChartData extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private IfitDAO chartDataDAO;
	private List<ChartDataDTO> dataList;
	private Map<String, Object> queryMap = new HashMap<String, Object>();
	private Map<String, Object> whereMap = new HashMap<String, Object>();
	private StringUtil stringUtil = new StringUtil();
	private boolean isAdmin;
	
	private String sql;
	private String rtnString;
	
	public ChartData() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	//	요소 초기화 및 세팅
	public void init(){
		this.chartDataDAO = (IfitDAO)this.wac.getBean("chartData");
		
		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
		this.isAdmin = StringUtil.stringToBool(StringUtil.isNullOrSpace((String)session.get("isAdmin"),"").trim());
		this.sql = "";
	}
	
	public Object getData(Map<String, Object> paramMap){
		init();
		
		String chartType = paramMap.containsKey("chartType") ? (String)paramMap.get("chartType") : "";
		
		if(chartType.equals("orderChartCategory")){
			// 상품분류별 판매분석
			return orderChartCategory((Map<String, Object>)paramMap.get("queryData"));
		}else{
			return null;
		}
	}
	
	public Object orderChartCategory(Map<String, Object> queryData){
		this.sql += "	SELECT PC.name as 'key', ifnull(sum(EO.amount), 0) as 'val' FROM each_order EO	\n";
		this.sql += "	join pay_list PAY on EO.pay_seq = PAY.pay_seq	\n";
		
		if(!queryData.get("date_start").toString().equals("")){
        	sql += "	and DATE_FORMAT(PAY.order_date,  '%Y.%m.%d') >= :date_start	\n";
        	this.whereMap.put("date_start", queryData.get("date_start").toString());
        }
		
		if(!queryData.get("date_end").toString().equals("")){
        	sql += "	and DATE_FORMAT(PAY.order_date,  '%Y.%m.%d') <= :date_end	\n";
        	this.whereMap.put("date_end", queryData.get("date_end").toString());
        }
		
		this.sql += "	join product_list PL on EO.p_id = PL.p_id 		\n";
		
		if(!StringUtil.stringToBool(StringUtil.isNullOrSpace((String)session.get("isAdmin"),"").trim())){
			// 업체 접근시
			this.whereMap.put("admin_seq", session.get("admin_seq"));
			this.sql += " and PL.admin_seq = :admin_seq	\n";
		}else if(Integer.parseInt(queryData.get("admin_seq").toString()) != 0){
			// 업체 선택시
			this.whereMap.put("admin_seq", Integer.parseInt(queryData.get("admin_seq").toString()));
			this.sql += " and PL.admin_seq = :admin_seq	\n";
		}
		
		this.sql += "	right outer join product_category PC on PL.category = PC.category_seq	\n";
		this.sql += "	where :one = :one		\n";
		
		this.queryMap.put("whereMap", this.whereMap);
		this.queryMap.put("groupBy", "PC.category_seq");
		this.queryMap.put("sql", this.sql);
		
		return this.chartDataDAO.getList(this.queryMap);
	}
	
}
