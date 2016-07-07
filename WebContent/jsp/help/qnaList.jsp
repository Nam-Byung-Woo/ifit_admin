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
				<h2 class="mb20"><span>1:1문의 목록</span></h2>
				<div class="contentBody">
					<s:form id="qna" name="qnaList" cssClass="dib" method="get" namespace="/help" action="qnaList" theme="simple">
						<ul class="tabArea">
							<li <s:if test='tabID==0'>class="on"</s:if> data-tabID="0">전체목록</li>
							<li <s:if test='tabID==1'>class="on"</s:if> data-tabID="1">답변대기</li>
							<li <s:if test='tabID==2'>class="on"</s:if> data-tabID="2">답변완료</li>
						</ul>
						<input type="hidden" id="pageNum" name="pageNum" value="<s:property value="pageNum"/>" />
						<input type="hidden" id="sortCol" name="sortCol" value="<s:property value='sortCol' />" />	
						<input type="hidden" id="sortVal" name="sortVal" value="<s:property value='sortVal' />" />
						<input type="hidden" id="queryIncode" name="queryIncode" value="<s:property value='queryIncode' />"  disabled />	
						보기 : <s:select id="countPerPage" name="countPerPage" cssClass="" list="Code.countPerPageMap" headerKey="" headerValue="" />
						<input type="button" class="deleteBtn simpleBtn mb10 btn2 fr mr20" value="선택삭제" />
						<div class="clear"></div>
						<span>전체 : <s:property value="totalCount"/>개</span>
						<table class="table_list tc">
							<colgroup>
								<col width="20px"><col width="30px"><col width="70px"><col width="70px"><col width="70px"><col width="90px"><col width="90px">
							</colgroup>
							<thead>
								<tr>
									<th scope="col" class="checkCol">
										<input type="checkbox" class="listAllCheck db" id="listAllCheck" />
									</th>
									<th scope="col">번호</th>
									<th scope="col"><p class="listSort" data-sort-col="1">제목<i class="ml5 fa <s:if test='sortVal.equals("DESC")'>fa-caret-down</s:if><s:else>fa-caret-up</s:else> <s:if test="sortCol!=1">hide</s:if>" aria-hidden="true"></i></p></th>
									<th scope="col"><p class="listSort" data-sort-col="2">아이디<i class="ml5 fa <s:if test='sortVal.equals("DESC")'>fa-caret-down</s:if><s:else>fa-caret-up</s:else> <s:if test="sortCol!=2">hide</s:if>" aria-hidden="true"></i></p></th>
									<th scope="col"><p class="listSort" data-sort-col="3">상태<i class="ml5 fa <s:if test='sortVal.equals("DESC")'>fa-caret-down</s:if><s:else>fa-caret-up</s:else> <s:if test="sortCol!=3">hide</s:if>" aria-hidden="true"></i></p></th>
									<th scope="col"><p class="listSort" data-sort-col="4">등록일<i class="ml5 fa <s:if test='sortVal.equals("DESC")'>fa-caret-down</s:if><s:else>fa-caret-up</s:else> <s:if test="sortCol!=4">hide</s:if>" aria-hidden="true"></i></p></th>
									<th scope="col"><p class="listSort" data-sort-col="5">답변일<i class="ml5 fa <s:if test='sortVal.equals("DESC")'>fa-caret-down</s:if><s:else>fa-caret-up</s:else> <s:if test="sortCol!=5">hide</s:if>" aria-hidden="true"></i></p></th>
								</tr>
							</thead>
							<tbody>
								<s:if test = "dataList.size==0">
									<tr>
										<td colspan="7" align="center">
											등록된 1:1문의가 없습니다.
										</td>
									</tr>
								</s:if>
								<s:iterator value="dataList" status="stat">
									<tr>
										<td class="center"><input type="checkbox" name="listItemCheck" class="listItemCheck" value="<s:property value="quest_seq"/>" /></td>
										<td class="center"><s:property value="quest_seq"/></td>
										<td class="center"><p class="viewBtn pointer hoverLine" data-seq="<s:property value="quest_seq"/>"><s:property value="title"/></p></td>
										<td class="center"><s:property value="user_id"/></td>
										<td class="center"><s:if test='state == 1'>답변대기</s:if><s:else>답변완료</s:else></td>
										<td class="center"><s:property value="quest_date"/></td>
										<td class="center"><s:property value="reply_date"/></td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
						<s:if test = "dataList.size!=0">
							<div class="paging"><s:property value="pagingHTML" escapeHtml="false" /></div>
						</s:if>
						<div class="tc">
							<select id="searchCol" name ="searchCol" class="common_select middle">
								<option title="제목을 검색합니다." value="1" <s:if test="searchCol==1">selected</s:if>>제목</option>
								<option title="아이디를를검색합니다." value="2" <s:if test="searchCol==2">selected</s:if>>아이디</option>
							</select>
							<input type="text" name ="searchVal" class="ml5 searchInput" value="${searchVal}" /><i class="listSearchBtn ml10 fa fa-search" aria-hidden="true" title="검색" > </i>
						</div>
					</s:form>
				</div>
			</div>
		</div>
		
		<jsp:include page="/jsp/help/pop/qnaView.jsp" flush="false" />		
	</body>
</html>