<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<jsp:include page="/jsp/include/head.jsp" flush="false" />
		<script type="text/javascript">
			$(function(){
			});
		</script>
	</head>
	<body class="indexBG" style="background: black;">
		<form id="loginForm" name="login" class="mt200 mb100" method="post" action="<s:url namespace="/" action="loginAction" />" autocomplete="off">
			<div class="loginDiv" >
				<div class="logo mainLogo mb10"></div>
				<div class="mb10">
					<div>
						<input type="text" id="admin_id" name="admin_id" placeholder="아이디" value="${admin_id}" autofocus />
					</div>
					<div class="mt15">
						<input type="password" id="admin_pw" name="admin_pw" placeholder="비밀번호" value="${admin_pw}" />
					</div>
				</div>
				<input type="submit" id="loginBtn" class="loginBtn btn1 mt10 center" value="로그인" />
			</div>
		</form>
	</body>
</html>