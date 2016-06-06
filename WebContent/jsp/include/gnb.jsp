<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="adminGnbArea">
	<div class="logo ml20 pointer fl"></div>
	<input type="button" id="logoutBtn" class="fr mt45 mr5" value="로그아웃"  />
	<p class="fr mt60 mr20"><span class="fs20"><s:property value="#session.admin_name"/></span> 님 환영합니다</p>
	<div class="clear"></div>
	<hr/>
</div>