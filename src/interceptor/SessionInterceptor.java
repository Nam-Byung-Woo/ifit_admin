package interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import util.config.ActionConfig;
import util.system.StringUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SessionInterceptor extends AbstractInterceptor {
	ActionConfig actionConfig = new ActionConfig();
	/*
		
		Code code = new Code();
		
		/************session체크 모든 intercept [16.04.10 by.Nam]**********
		
		접근하는 모든페이지에 대해 체크한다.
		error종류는 struts.xml -> global-results 명시되어 있는 4가지
		ActionConfig에서 체크한 후 return 받아온다.
		1. login	: 로그인이 필요할 경우
		 (1-1) loginPop : 팝업용 로그인
		2. auth	: 접근 권한이 없을 경우
		3. exception	: 기타 모든 오류
		
		***********************************************************************/

        @Override
        public String intercept(ActionInvocation invocation) throws Exception {
        	System.out.println("인터셉터시작" + "(sessionIntercepteor.java)");
        	ActionContext context = ActionContext.getContext();
        	ServletContext servletContext = ServletActionContext.getServletContext();
        	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        	HttpServletRequest request = ServletActionContext.getRequest();
        	Map<String, Object> session = (Map<String, Object>) context.getSession();
        	
        	String nextAction = request.getRequestURI().substring(1);	//	이동할 페이지 액션(다음페이지)
        	String[] nextActionArr = nextAction.split("/");
        	
        	boolean isAdminSession = StringUtil.stringToBool(StringUtil.isNullOrSpace((String)session.get("isAdmin"),"").trim());		//	관리자세션
        	boolean isShopSession = StringUtil.stringToBool(StringUtil.isNullOrSpace((String)session.get("isShop"),"").trim());			//	업체세션
        	
        	boolean needSessionAction = StringUtil.stringToBool(actionConfig.getActionAttr(context.getName(), "needSession"));		// Action의 needSession 값 추출
        	boolean needAdminAction = StringUtil.stringToBool(actionConfig.getActionAttr(context.getName(), "needAdmin"));		// Action의 needAdmin 값 추출
        	
        	String result = "";
        	
        	if(!actionConfig.ActionNameChk(context.getName())){		// 1. Action이 명시되어 있는지 체크
        		result = "exception";
        	}else if(needSessionAction){
        		// session이 필요한 액션 체크
        		if(!isAdminSession && !isShopSession){
        			// session 둘다 없을 때
        			result = "login";
                   	if(nextActionArr.length==1){
                		session.put("nextActionName", nextActionArr[0]);
                	}else{
                		session.put("nextActionNamespace", nextActionArr[0]);
                		session.put("nextActionName", nextActionArr[1]);
                	}
        		}else{
        			// session이 있긴 있다
        			if(needAdminAction && !isAdminSession){
        				// admin액션인데 admin이 아니면 -> shop의 불법 접근
        				result = "logout";
        			}
        		}
        	}
        	
        	System.out.println("context::::" + context.getName() + "(sessionIntercepteor.java)");
        	System.out.println("isAdminSession:"+isAdminSession);
        	System.out.println("isShopSession:"+isShopSession);
        	System.out.println("result::::" + result + "(sessionIntercepteor.java)");
        	
        	if(result.equals("")){
        		return invocation.invoke();		// 정상적인 진행 
        	}else{
        		return result;
        	}
        }
        
        @Override
        public void init() {
         // TODO Auto-generated method stub
         super.init();
        }
        
        @Override
        public void destroy() {
         // TODO Auto-generated method stub
         super.destroy();
        }
}

