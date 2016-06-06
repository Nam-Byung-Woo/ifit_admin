<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<jsp:include page="/jsp/include/head.jsp" flush="false">
			<jsp:param value=""  name="title"/>
		</jsp:include>
		<script type="text/javascript">
			$(function(){
			});
		</script>
	</head>
	<body>
		<jsp:include page="/jsp/include/gnb.jsp" flush="false" />
		<div class="admin_container">
			<jsp:include page="/jsp/include/lnb.jsp" flush="false" />
			<s:if test='isUpdateMode.equals("true")'>
				<s:set name="formNameKor" value='%{"수정"}' />
				<s:set name="formNameEng" value='%{"shopMemberUpdate"}' />
			</s:if>
			<s:else>
				<s:set name="formNameKor" value='%{"등록"}' />
				<s:set name="formNameEng" value='%{"shopMemberWrite"}' />
			</s:else>
			<div class="adminContentArea">
				<h2 class="mb20"><span>입점회원 ${formNameKor}</span></h2>
				<div class="contentBody">
					<s:form id="shopMember" name="shopMemberEditor" data-mode="EditorForm" data-confirm-msg="입점회원을 ${formNameKor} 하시겠습니까?" cssClass="dib" method="post" namespace="/member" action="%{formNameEng}Action" theme="simple" enctype="multipart/form-data">
						<input type="hidden" id="seq" name="seq" value="<s:property value='seq' />"  /> 
						<input type="hidden" id="queryDecode" name="queryDecode" value="<s:property value='queryDecode' />"  disabled />
						<table class="table_editor tc">
							<colgroup>
								<col width="180px"><col width="*">
							</colgroup>
							<tbody>
								<s:set name="shopMemberData" value="adminDTO" />
								<tr>
									<th scope="col">아이디</th>
									<td class="tl" tabIndex="1" id="id_check">
										<input type="text" class="validate" autocomplete="off" id="id" name="id" value="${shopMemberData.id}" />
										<s:if test='isUpdateMode.equals("true")'>
											*변경시 주의 요망
										</s:if>
									</td>
								</tr>
								<tr>
									<th scope="col">비밀번호</th>
									<td class="tl" tabIndex="2" id="pw_check">
										<input type="password" class="validate" autocomplete="off" id="pw" name="pw" value="" />
										<s:if test='isUpdateMode.equals("true")'>
											*입력하지 않으면 유지됩니다.
										</s:if>
									</td>
								</tr>
								<tr>
									<th scope="col">입점업체명</th>
									<td class="tl" tabIndex="3" id="name_check"><input type="text" class="validate" autocomplete="off" id="name" name="name" value="${shopMemberData.name}" /></td>
								</tr>
								<tr>
									<th scope="col">연락처</th>
									<td class="tl" tabIndex="4" id="tel_check">
										<s:select id="tel1" name="tel1" cssClass="validate" list="Code.telNumberMap" headerKey="" headerValue="선택" value="%{#shopMemberData.tel1}" />-
										<input type="text" class="validate" autocomplete="off" id="tel2" name="tel2" value="${shopMemberData.tel2}" />-
										<input type="text" class="validate" autocomplete="off" id="tel3" name="tel3" value="${shopMemberData.tel3}" />
									</td>
								</tr>
							</tbody>
						</table>
						<div class="mt20 clear">
							<input type="button" class="listBtn simpleBtn mb10 btn2 dib fl" value="목록으로" />
							<s:if test='isUpdateMode.equals("true")'>
								<input type="submit" class="updateActionBtn simpleBtn mb10 btn2 dib fr" value="수정" />
							</s:if>
							<s:else>
								<input type="submit" class="writeActionBtn simpleBtn mb10 btn2 dib fr" value="등록" />
							</s:else>
						</div>
					</s:form>
				</div>
			</div>
		</div>
	</body>
</html>