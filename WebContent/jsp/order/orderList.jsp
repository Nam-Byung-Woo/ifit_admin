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
				$(".openPop").click(function(){
					var layerObj = $(".layer-orderView");
					layerObj.find("#jsonObj").val($(this).attr("data-eachOrder"));
					layerObj.find("#jsonObj").trigger("change");
					layerObj.find("#pay_seq").html($(this).parents("tr").find(".pay_seq").val());
					layerObj.find("#order_date").html($(this).parents("tr").find(".order_date").val());
					var inputHTML = '';
					if($(this).parents("tr").find(".delivery_number").val()==""){
						inputHTML += '<input type="text" />';
						inputHTML += '<input type="button" id="deliveryOK" class="simpleBtn ml10 btn2 dib" value="배송처리" />';
					}else{
						inputHTML += $(this).parents("tr").find(".delivery_number").val();
					}
					layerObj.find("#delivery_number").html(inputHTML);
					layerObj.find("#state").html($(this).parents("tr").find(".state").val());
				});
				
				$("#admin_seq").change(function(){
					$(this).parents("form").submit();
				});
				
				$("#date_start").change(function(){
					$(this).parents("form").submit();
				});
				
				$("#date_end").change(function(){
					$(this).parents("form").submit();
				});
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
					<s:form id="order" name="orderList" cssClass="dib" method="get" namespace="/order" action="orderList" theme="simple">
						<ul class="tabArea">
							<li <s:if test='tabID==0'>class="on"</s:if> data-tabID="0">전체목록</li>
							<li <s:if test='tabID==1'>class="on"</s:if> data-tabID="1">접수대기</li>
							<li <s:if test='tabID==2'>class="on"</s:if> data-tabID="2">배송처리완료</li>
							<li <s:if test='tabID==3'>class="on"</s:if> data-tabID="3">요약차트</li>
						</ul>
						<s:if test='tabID!=3'>
							<input type="hidden" id="pageNum" name="pageNum" value="<s:property value="pageNum"/>" />
							<input type="hidden" id="sortCol" name="sortCol" value="<s:property value='sortCol' />" />	
							<input type="hidden" id="sortVal" name="sortVal" value="<s:property value='sortVal' />" />
							<input type="hidden" id="queryIncode" name="queryIncode" value="<s:property value='queryIncode' />"  disabled />	
							보기 : <s:select id="countPerPage" name="countPerPage" cssClass="" list="Code.countPerPageMap" headerKey="" headerValue="" />
							<input type="hidden" class="validate" id="admin_seq" name="admin_seq" value="${admin_seq}" />
							<s:if test = "#session.isAdmin">
								<div class="fr">업체 선택 : <input type="text" class="pointer openPop vm" data-pop-id="shopSearch" autocomplete="off" placeholder="업체 검색" id="admin_name" name="admin_name" value="<s:if test='admin_seq != 0'><s:property value="admin_name"/></s:if>" readonly /></div>
							</s:if>
							<div class="clear mb10"></div>
							<div class="fr">
								기간 선택 : <input type="text" class="datepicker vm" id="date_start" name="date_start" value="" readonly /> ~ <input type="text" class="datepicker vm" id="date_end" name="date_end" value="" readonly />
							</div>
							<div class="clear mb10"></div>
							<span>전체 : <s:property value="totalCount"/>개</span>
							<table class="table_list tc">
								<colgroup>
									<col width="70px"><col width="70px"><col width="70px"><col width="70px"><col width="70px"><col width="70px">
								</colgroup>
								<thead>
									<tr>
										<th scope="col">주문번호</th>
										<th scope="col"><p class="listSort" data-sort-col="1">상품명<i class="ml5 fa <s:if test='sortVal.equals("DESC")'>fa-caret-down</s:if><s:else>fa-caret-up</s:else> <s:if test="sortCol!=1">hide</s:if>" aria-hidden="true"></i></p></th>
										<th scope="col">주문회원</th>
										<th scope="col">총 금액</th>
										<th scope="col">상태</th>
										<th scope="col">주문일</th>
									</tr>
								</thead>
								<tbody>
									<s:if test = "dataList.size==0">
										<tr>
											<td colspan="6" align="center">
												판매내역이 없습니다.
											</td>
										</tr>
									</s:if>
									<s:iterator value="dataList" status="stat">
										<tr>
											<td class="center"><s:property value="pay_seq"/></td>
											<td class="center">
												<p class="openPop pointer hoverLine" data-pop-id="orderView" data-eachOrder="<s:property value="eachOrderListToJson"/>">
													<s:property value="eachOrderList.get(0).p_name"/><s:if test='eachOrderList.size>1'> 외 <s:property value="eachOrderList.size-1"/>건</s:if>
												</p>
											</td>
											<td class="center"><s:property value="user_id"/></td>
											<td class="center">
												<s:set name="totalPrice" value='0' />
												<s:iterator value="eachOrderList" status="stat2">
													<s:set name="totalPrice" value='%{#totalPrice+(price*amount)}' />
												</s:iterator>
												${totalPrice}
											</td>
											<td class="center"><s:if test='eachOrderList.get(0).state == 1'>접수대기</s:if><s:else>배송처리완료</s:else></td>
											<td class="center"><s:property value="@util.system.StringUtil@simpleDate(order_date)"/></td>
											<input type="hidden" class="pay_seq" value="<s:property value="pay_seq"/>" />
											<input type="hidden" class="state" value="<s:if test='eachOrderList.get(0).state == 1'>접수대기</s:if><s:else>배송처리완료</s:else>" />
											<input type="hidden" class="order_date" value="<s:property value="order_date"/>" />
											<input type="hidden" class="delivery_number" value="<s:property value="eachOrderList.get(0).delivery_number"/>" />
										</tr>
									</s:iterator>
								</tbody>
							</table>
							<s:if test = "dataList.size!=0">
								<div class="paging"><s:property value="pagingHTML" escapeHtml="false" /></div>
							</s:if>
							<div class="tc">
								<select id="searchCol" name ="searchCol" class="common_select middle">
									<option title="회원을 검색합니다." value="1" <s:if test="searchCol==1">selected</s:if>>주문회원</option>
								</select>
								<input type="text" name ="searchVal" class="ml5 searchInput" value="${searchVal}" /><i class="listSearchBtn ml10 fa fa-search" aria-hidden="true" title="검색" > </i>
							</div>
						</s:if>
					</s:form>
				</div>
			</div>
		</div>
		<jsp:include page="/jsp/order/pop/shopSearch.jsp" flush="false" />
		<jsp:include page="/jsp/order/pop/orderView.jsp" flush="false" />		
	</body>
</html>