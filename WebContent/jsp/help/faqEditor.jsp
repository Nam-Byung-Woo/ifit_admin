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
				<s:set name="formNameEng" value='%{"faqUpdate"}' />
			</s:if>
			<s:else>
				<s:set name="formNameKor" value='%{"등록"}' />
				<s:set name="formNameEng" value='%{"faqWrite"}' />
			</s:else>
			<div class="adminContentArea">
				<h2 class="mb20"><span>자주하는질문 ${formNameKor}</span></h2>
				<div class="contentBody">
					<s:form id="faq" name="faqEditor" data-mode="EditorForm" data-confirm-msg="자주하는질문을 ${formNameKor} 하시겠습니까?" cssClass="dib" method="post" namespace="/help" action="%{formNameEng}Action" theme="simple" enctype="multipart/form-data">
						<input type="hidden" id="seq" name="seq" value="<s:property value='seq' />"  /> 
						<input type="hidden" id="queryDecode" name="queryDecode" value="<s:property value='queryDecode' />"  disabled />
						<table class="table_editor tc">
							<colgroup>
								<col width="180px"><col width="*">
							</colgroup>
							<tbody>
								<s:set name="faqData" value="faqDTO" />
								<tr>
									<th scope="col">제목</th>
									<td tabIndex="1" id="title_check"><input type="text" class="validate" autocomplete="off" id="title" name="title" value="${faqData.title}" /></td>
								</tr>
								<tr>
									<th scope="col">내용</th>
									<td tabIndex="2" id="content_check"><input type="text" class="validate" autocomplete="off" id="content" name="content" value="${faqData.content}" /></td>
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