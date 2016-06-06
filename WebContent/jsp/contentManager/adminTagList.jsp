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
			<div class="adminContentArea">
				<h2 class="mb20"><span>태그 목록</span></h2>
				<div class="contentBody">
					<s:form id="adminTag" name="adminTagList" data-mode="EditorForm" data-confirm-msg="태그를 저장 하시겠습니까?" cssClass="dib" method="post" namespace="/contentManager" action="adminTagUpdateAction" theme="simple">
						<input type="hidden" id="pageNum" name="pageNum" value="<s:property value="pageNum"/>" />
						<input type="hidden" id="sortCol" name="sortCol" value="<s:property value='sortCol' />" />	
						<input type="hidden" id="sortVal" name="sortVal" value="<s:property value='sortVal' />" />
						<input type="hidden" id="queryIncode" name="queryIncode" value="<s:property value='queryIncode' />"  disabled />	
						<table class="table_editor tc">
							<colgroup>
								<col width="180px"><col width="*">
							</colgroup>
							<tbody tabIndex="1" id="tag_check">
								<s:iterator value="dataList" status="stat">
									<tr>
										<th scope="col">태그<s:property value="admin_tag_seq"/></th>
										<td class="tl"><input type="text" class="validate" autocomplete="off" name="tag" value="<s:property value="tag"/>" /></td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
						<div class="tc mt20">
							<input type="submit" class="updateActionBtn simpleBtn mb10 btn2 clear" value="저장" />
						</div>
					</s:form>
				</div>
			</div>
		</div>		
	</body>
</html>