package action.basic;

import java.util.HashMap;
import java.util.Map;

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

import dao.IfitDAO;
import dto.AdminDTO;
import lombok.Data;

@Data
public class Index extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FormValidate formValidate = new FormValidate();
	private IfitDAO ifitDAO;
	private AdminDTO adminDTO;
	private Map<String, Object> paramMap = new HashMap<String, Object>();
	private Map<String, Object> whereMap = new HashMap<String, Object>();
	private Map<String, Object> validateMsgMap = new HashMap<String, Object>();
	private String rtnString;
	
	private String admin_id;		//	관리자 아이디
	private String admin_pw;		//	관리자 비밀번호
	private String nextActionNamespace;
	private String nextActionName;
	
	public Index() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	//	요소 초기화 및 세팅
	public void init(){
		this.ifitDAO = (IfitDAO)this.wac.getBean("admin");
		this.adminDTO = new AdminDTO();
		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
	}
	
	//	로그인 체크
	public String loginAction() throws Exception {	
		init();
		Gson gson = new Gson();
		this.admin_id = StringUtil.isNullOrSpace(this.admin_id,"").trim();
		this.admin_pw = StringUtil.isNullOrSpace(this.admin_pw,"").trim();
		paramMap.put("admin_id", this.admin_id);
		paramMap.put("admin_pw", this.admin_pw);
		validateMsgMap = formValidate.loginForm(paramMap);
		paramMap.clear();
		if(!(boolean)validateMsgMap.get("res")){
			this.rtnString = gson.toJson(validateMsgMap);
			this.rtnString = "test";
			return "validation";
		}
		String passwordEncode = MySqlFunction.password(AESCrypto.encryptPassword(this.admin_pw));
		whereMap.put("id", this.admin_id);
		paramMap.put("whereMap", whereMap);
		this.adminDTO = (AdminDTO)this.ifitDAO.getOneRow(paramMap);
		paramMap.clear();
		if(this.adminDTO == null){
			//아이디 불일치
			validateMsgMap = formValidate.loginAuthError();
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}else if(!this.adminDTO.getPw().equals(passwordEncode)){
			//비밀번호 불일치
			validateMsgMap = formValidate.loginAuthError();
			this.rtnString = gson.toJson(validateMsgMap);
			return "validation";
		}

		this.nextActionNamespace = (session.get("nextActionNamespace")==null) ? "/" : session.get("nextActionNamespace").toString();
		
		if (session.get("nextActionName")!=null) {
			this.nextActionName = session.get("nextActionName").toString();
		}else{
			this.nextActionName = "index";
		}
		
		session.remove("nextActionNamespace");
		session.remove("nextActionName");
		
		// admin, shop 구분
		if(this.adminDTO.getIsAdmin()){
			this.session.put("isAdmin", "true");
			this.session.put("admin_name", "iFit 관리자");
		}else{
			this.session.put("isShop", "true");
			this.session.put("admin_name", this.adminDTO.getName() + " 관리자");
		}
		this.session.put("admin_seq", this.adminDTO.getSeq());
		this.context.setSession(this.session);
		
		return SUCCESS;
	}
	
	public String logoutAction(){
		init();
		this.session.clear();
		this.context.setSession(this.session);
		return SUCCESS;		
	}
}
