<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" type="text/css" href="/jsp/common/plugin/jssor.slider/css/jssor.slider.customize.css" />
<script type="text/javascript" src="/jsp/common/plugin/jssor.slider/js/jssor.slider.mini.js"></script>
<script type="text/javascript" src="/jsp/common/plugin/jssor.slider/js/imageSliderCustomize.js"></script>
<script type="text/javascript">
	$(function(){
	    $("#jsonObj").change(function(){
	    	var addHTML = "";
	    	var target = $(".orderViewPop");
	    	var jsonObj = JSON.parse($(this).val());
	    	
	    	var tbody = target.find(".table_list tbody");
	    	target.find("#eachOrderCount").html("전체: "+ jsonObj.length +"개");
	    	$.each(jsonObj, function(key, value){
	    		addHTML += '<tr>';
		    	addHTML += '<td class="center">' + value.p_name + '</td>';
		    	addHTML += '<td class="center">' + value.order_seq + '</td>';
		    	addHTML += '<td class="center">' + value.amount + '</td>';
		    	addHTML += '<td class="center">' + value.price + '</td>';
		    	addHTML += '</tr>';
    		});
	    	
	    	tbody.append(addHTML)
	    });
	    
	    $(document).on("click","#deliveryOK",function(e){
	    	if($(this).prev().val() == ""){
	    		alert("배송번호를 입력해 주세요.");
	    		$(this).prev().focus();
	    		return false;
	    	}
	    	if(confirm("배송처리 하시겠습니까?")){
	    		var data = {"actionName":"deliveryOK"};
	    		data.pay_seq = $("#pay_seq").html();
	    		data.delivery_number = $(this).prev().val();
	    		data.url = "/ajaxAction.ifit";
	    		getAjaxData(data);
	    	}
	    	alert("적용되었습니다.");
	    	$("#delivery_number").html($(this).prev().val());
			$("#state").html("배송처리완료");
	    });
	    
	    $(document).on("click",".layerClose",function(e){
	    	$(".orderViewPop .table_list tbody").html("");
	    	$(".orderViewPop .eachOrderCount").html("");
	    	$("#pay_seq").html("");
	    	$("#order_date").html("");
	    	$("#delivery_number").html("");
	    	$("#state").html("");
		});
	});
</script>
<style>
.slider_image {
    width: 343px;
    height: 437px;
}
</style>
<div class="layer layer-orderView" data-close-answer="false">
	<div class="bg"></div>
	<div id="popLayer" class="pop-layer orderViewPop">
		<div class="pop-container">
			<div class="pop-conts">
				<s:form id="order" name="orderEditor" data-mode="EditorForm" data-confirm-msg="" cssClass="dib" method="post" namespace="/help" action="" theme="simple" enctype="multipart/form-data">
					<input id="jsonObj" type="hidden" />
					<input type="hidden" id="seq" name="seq" value=""  /> 
					<!--content //-->
					<p id="title" class="pop-title mb20 pb10">주문상세</p>
					<div class="pl20 pr20 contentsArea">
						<table class="table_editor mb30">
							<colgroup>
								<col width="180px"><col width="*">
							</colgroup>
							<tbody>
								<tr>
									<th scope="col">주문번호</th>
									<td id="pay_seq"></td>
								</tr>
								<tr>
									<th scope="col">주문일자</th>
									<td id="order_date"></td>
								</tr>
								<tr>
									<th scope="col">배송번호</th>
									<td id="delivery_number"></td>
								</tr>
								<tr>
									<th scope="col">상태</th>
									<td id="state"></td>
								</tr>
							</tbody>
						</table>
					
						<span id="eachOrderCount"></span>
						<table class="table_list tc">
							<colgroup>
								<col width="70px"><col width="70px"><col width="70px"><col width="70px">
							</colgroup>
							<thead>
								<tr>
									<th scope="col">주문상품정보</th>
									<th scope="col">상품별 주문번호</th>
									<th scope="col">수량</th>
									<th scope="col">가격</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</s:form>
				<div class="btn-r">
					<i class="layerClose fa fa-times fa-2x" aria-hidden="true" title="닫기"></i>
				</div>
			</div>
		</div>
	</div>
</div>