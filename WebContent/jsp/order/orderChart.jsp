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
				$("#date_start").datepicker();
				$("#date_end").datepicker();
				$("#date_start").datepicker( "option", "dateFormat", "yy.mm.dd");
				$("#date_end").datepicker( "option", "dateFormat", "yy.mm.dd");
				$("#date_start").datepicker( "setDate", "<s:property value="date_start"/>");
				$("#date_end").datepicker( "setDate", "<s:property value="date_end"/>");
								
				$("#admin_seq").change(function(){
					$(this).parents("form").submit();
				});
				
				$("#date_start").change(function(){
					$(this).parents("form").submit();
				});
				
				$("#date_end").change(function(){
					$(this).parents("form").submit();
				});
				
				$(".selectRefreshBtn").click(function(){
					if($(this).attr("id")=="shopRefresh"){
						$("#admin_seq").val("");
						$("#admin_name").val("");
					}else if($(this).attr("id")=="dateRefresh"){
						$("#date_start").val("");
						$("#date_end").val("");
					}
					$(this).parents("form").submit();
				});
				
				var queryData={};
				queryData.admin_seq = $("#admin_seq").val();
				queryData.date_start = $("#date_start").val();
				queryData.date_end = $("#date_end").val();
				
				chartInit({"type":"orderChartCategory", "queryData":queryData});
			});
		</script>
	</head>
	<body>
		<jsp:include page="/jsp/include/gnb.jsp" flush="false" />
		<div class="admin_container">
			<jsp:include page="/jsp/include/lnb.jsp" flush="false" />
			<div class="adminContentArea">
				<h2 class="mb20"><span>판매현황</span></h2>
				<div class="contentBody">
					<s:form id="order" name="orderChart" cssClass="dib" method="get" namespace="/order" action="orderList" theme="simple">
						<ul class="tabArea">
							<li <s:if test='tabID==0'>class="on"</s:if> data-tabID="0">전체목록</li>
							<li <s:if test='tabID==1'>class="on"</s:if> data-tabID="1">접수대기</li>
							<li <s:if test='tabID==2'>class="on"</s:if> data-tabID="2">배송처리완료</li>
							<li <s:if test='tabID==3'>class="on"</s:if> data-tabID="3">요약차트</li>
						</ul>
						<input type="hidden" id="tabID" name="tabID" value="<s:property value='tabID' />" />
						<input type="hidden" class="validate" id="admin_seq" name="admin_seq" value="${admin_seq}" />
						<s:if test = "#session.isAdmin">
							<span id="shopRefresh" class="selectRefreshBtn fr ml5">초기화<i class="fa fa-refresh" aria-hidden="true" title="초기화" ></i></span>	
							<div class="fr">업체 선택 : <input type="text" class="pointer openPop vm" data-pop-id="shopSearch" autocomplete="off" placeholder="업체 검색" id="admin_name" name="admin_name" value="<s:if test='admin_seq != 0'><s:property value="admin_name"/></s:if>" readonly /></div>
						</s:if>
						<div class="clear mb10"></div>
						<span id="dateRefresh" class="selectRefreshBtn fr ml5">초기화<i class="fa fa-refresh" aria-hidden="true" title="초기화" ></i></span>
						<div class="fr">
							기간 선택 : <input type="text" class="datepicker vm" id="date_start" name="date_start" value="" readonly /> ~ <input type="text" class="datepicker vm" id="date_end" name="date_end" value="" readonly />
						</div>
						<div class="clear mb10"></div>
						<div class="chartArea">
							<div class="chartTitle">
								<span class="ml10"><i class="chartDisplayBtn mr5 fa fa-chevron-up" aria-hidden="true" title="화면" ></i>상품분류별 판매분석</span>
							</div>
							<div class="chartContent">
								<div id="orderChartCategory"></div>
							</div>
						</div>
						
					</s:form>
				</div>
			</div>
		</div>
		<jsp:include page="/jsp/order/pop/shopSearch.jsp" flush="false" />
	</body>
</html>