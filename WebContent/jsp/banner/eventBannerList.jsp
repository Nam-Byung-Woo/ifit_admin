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
				<h2 class="mb20"><span>이벤트배너 목록</span></h2>
				<div class="contentBody">
					<s:form id="eventBanner" name="eventBannerList" cssClass="dib" method="get" namespace="/banner" action="eventBannerList" theme="simple">
						<input type="hidden" id="pageNum" name="pageNum" value="<s:property value="pageNum"/>" />
						<input type="hidden" id="sortCol" name="sortCol" value="<s:property value='sortCol' />" />	
						<input type="hidden" id="sortVal" name="sortVal" value="<s:property value='sortVal' />" />
						<input type="hidden" id="queryIncode" name="queryIncode" value="<s:property value='queryIncode' />"  disabled />	
						보기 : <s:select id="countPerPage" name="countPerPage" cssClass="" list="Code.countPerPageMap" headerKey="" headerValue="" />
						<input type="button" class="writeBtn simpleBtn mb10 btn2 clear fr" value="등록" />
						<input type="button" class="deleteBtn simpleBtn mb10 btn2 fr mr20" value="선택삭제" />
						<div class="clear"></div>
						<span>전체 : <s:property value="totalCount"/>개</span>
						<table class="table_list tc">
							<colgroup>
								<col width="20px"><col width="50px"><col width="70px"><col width="70px"><col width="70px">
							</colgroup>
							<thead>
								<tr>
									<th scope="col" class="checkCol">
										<input type="checkbox" class="listAllCheck db" id="listAllCheck" />
									</th>
									<th scope="col">번호</th>
									<th scope="col">이미지</th>
									<th scope="col"><p class="listSort" data-sort-col="1">타입<i class="ml5 fa <s:if test='sortVal.equals("DESC")'>fa-caret-down</s:if><s:else>fa-caret-up</s:else> <s:if test="sortCol!=1">hide</s:if>" aria-hidden="true"></i></p></th>
									<th scope="col">추가작업</th>
								</tr>
							</thead>
							<tbody>
								<s:if test = "dataList.size==0">
									<tr>
										<td colspan="5" align="center">
											등록된 이벤트배너가 없습니다.
										</td>
									</tr>
								</s:if>
								<s:iterator value="dataList" status="stat">
									<tr>
										<td class="center"><input type="checkbox" name="listItemCheck" class="listItemCheck" value="<s:property value="banner_seq"/>" /></td>
										<td class="center"><s:property value="banner_seq"/></td>
										<!--  <td class="center"><p class="viewBtn pointer" data-seq="<s:property value="banner_seq"/>"><img src="http://<s:property value="banner_url"/>" id="banner_img_preview" /></p></td>-->
										<td class="center"><img src="http://<s:property value="banner_url"/>" id="banner_img_preview" /></td>
										<td class="center"><s:property value="code.getEventBannerTypeMap().get(banner_type)"/></td>
										<td class="center">
											<i class="editBtn mr10 fa fa-pencil-square-o" aria-hidden="true" title="편집" data-seq="<s:property value="banner_seq"/>" > </i>
										</td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
						<s:if test = "dataList.size!=0">
							<div class="paging"><s:property value="pagingHTML" escapeHtml="false" /></div>
						</s:if>
					</s:form>
				</div>
			</div>
		</div>
	</body>
</html>