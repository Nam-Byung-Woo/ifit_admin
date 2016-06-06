package action.basic;

import java.util.HashMap;
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
import action.member.ShopMember;
import action.product.GeneralProduct;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import dto.ProductListDTO;

import lombok.Data;

@Data
public class Ajax extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FormValidate formValidate = new FormValidate();
	private Map<String, Object> validateMsgMap = new HashMap<String, Object>();
	private StringUtil stringUtil = new StringUtil();
	private boolean isAdmin;
	
	private String data;
	private String rtnString;
	
	public Ajax() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	//	요소 초기화 및 세팅
	public void init(){
		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
		this.isAdmin = StringUtil.stringToBool(StringUtil.isNullOrSpace((String)session.get("isAdmin"),"").trim());
	}
	
	public String ajaxFormValidate(){
		init();
		JSONObject jsonObject = (JSONObject) JSONValue.parse(data);
		String formID = jsonObject.get("formID").toString();
		if(formID.equals("shopMemberEditorForm")){
			validateMsgMap = this.isAdmin ? formValidate.shopMemberEditorForm(jsonObject) : null;
		}else if(formID.equals("faqEditorForm")){
			validateMsgMap = this.isAdmin ? formValidate.faqEditorForm(jsonObject) : null;
		}else if(formID.equals("generalProductEditorForm")){
			validateMsgMap = formValidate.generalProductEditorForm(jsonObject);
		}else if(formID.equals("eventBannerEditorForm")){
			validateMsgMap = formValidate.eventBannerEditorForm(jsonObject);
		}else if(formID.equals("mainLabelEditorForm")){
			validateMsgMap = formValidate.mainLabelEditorForm(jsonObject);
		}else if(formID.equals("labelProductEditorForm")){
			validateMsgMap = formValidate.labelProductEditorForm(jsonObject);
		}else if(formID.equals("adminTagEditorForm")){
			validateMsgMap = formValidate.adminTagEditorForm(jsonObject);
		}else if(formID.equals("colorListEditorForm")){
			validateMsgMap = formValidate.colorListEditorForm(jsonObject);
		}else if(formID.equals("sizeListEditorForm")){
			validateMsgMap = formValidate.sizeListEditorForm(jsonObject);
		}
		
		Gson gson = new Gson();
		this.rtnString = gson.toJson(validateMsgMap);
		
		return SUCCESS;		
	}
	
	public String ajaxGetData() throws Exception{
		init();
		Gson gson = new Gson();
		JSONObject jsonObject = (JSONObject) JSONValue.parse(data);
		String dataKind = jsonObject.get("dataKind").toString();
		System.out.println(jsonObject);
		if(this.isAdmin && dataKind.equals("shop")){
			jsonObject.put("queryMode","one");
			ShopMember shopMember= new ShopMember();
			this.rtnString = gson.toJson(shopMember.getData(jsonObject));
		}else if(this.isAdmin && dataKind.equals("shopList")){
			jsonObject.put("queryMode","list");
			ShopMember shopMember= new ShopMember();
			this.rtnString = gson.toJson(shopMember.getData(jsonObject));
		}else if(this.isAdmin && dataKind.equals("faq")){
			Faq faq = new Faq();
			this.rtnString = gson.toJson(faq.getData(jsonObject));
		}else if(dataKind.equals("generalProduct")){
			GeneralProduct generalProduct = new GeneralProduct();
			Map<String, Object> data = generalProduct.getOriginalData(Integer.parseInt(jsonObject.get("seq").toString()));
			this.rtnString = gson.toJson(data);
		}else if(dataKind.equals("generalProductList")){
			GeneralProduct generalProduct = new GeneralProduct();
			this.rtnString = gson.toJson(generalProduct.getData(jsonObject));
		}
		
		System.out.println("AJAX!!!!!!!!!" + this.rtnString);
		return SUCCESS;		
	}
}
