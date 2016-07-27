<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
$(document).ready(function() {
	$("#productSearchName").keydown(function (key) {
        if(key.keyCode == 13){	//엔터
        	$('#productSearchBtn').trigger('click');
        }
    });
	
	$("#productSearchBtn").click(function(){
		$("#listExist").hide();
		$("#listExist").empty();
		var data = {
			"url":"/ajaxGetData.ifit",
			"dataKind":"generalProductList",
			"queryMode":"list",
			"p_name":$("#productSearchName").val()
		};
		var jsonObj = JSON.parse(getAjaxData(data));
		if(jsonObj!=""){
			console.log(jsonObj);
			var listHtml;
			for(var i=0; i<jsonObj.length; i++){
				listHtml = "";
				listHtml += '<div data-idx="' + jsonObj[i].p_id + '" data-name="' + jsonObj[i].p_name + '">';
				listHtml += '<span class="productSelect pointer">' + jsonObj[i].p_name + '</span>';
				listHtml += '<dl>';
				listHtml += '<dt class="fl mr8">업체명</dt>';
				listHtml += '<dd>' + jsonObj[i].admin_name + '</dd>';
				listHtml += '</dl>';
				listHtml += '<dl class="mt2">';
				listHtml += '<dt class="fl mr8">등록일</dt>';
				listHtml += '<dd>' + jsonObj[i].regdate + '</dd>';
				listHtml += '</dl>';
				listHtml += '</div>';
				$("#listExist").append(listHtml);
			}
			$("#listEmpty").hide();
			$("#result>span").html("검색결과 : " + jsonObj.length);
			$("#listExist").show();
		}else{
			$("#listEmpty").show();
		}
	});
	
	$(document).on("click",".productSelect",function(e){
		var parent = $(".openPop[data-pop-id=productSearch]").parents("form").attr("id");
		
		if(parent=="promotionContent"){
			var inputHTML = '<li class="pointer ui-state-default">';
			inputHTML += '<input type="hidden" name="pro_map_seq" value="0" />';
			inputHTML += '<input type="hidden" name="p_id" value="' + $(this).parent().attr("data-idx") + '" />';
			inputHTML += '<input type="hidden" name="p_name" value="' + $(this).parent().attr("data-name") + '" />';
			inputHTML += '<span>' + $(this).parent().attr("data-name") + '</span>';
			inputHTML += '</li>';
			$("#sortable").append(inputHTML);
			$("#sortable li").last().trigger("click");
		}else if("eventBanner"){
			$("#p_id").val($(this).parent().attr("data-idx"));
			$("#p_name").val($(this).parent().attr("data-name"));
		}
		
		$('.layer-productSearch .layerClose').trigger('click');
		$("#listEmpty").show();
		$("#listExist").hide();
		$("#listExist").empty();
		$("#productSearchName").val("");
		$("#result>span").html("검색결과 : 0");
	});
});
</script>
<div class="layer layer-productSearch" data-close-answer="false" data-parent-id="">
	<div class="bg"></div>
	<div id="popLayer" class="pop-layer">
		<div class="pop-container">
			<div class="pop-conts">
				<!--content //-->
				<p id="pop-title" class="pop-title mb20 pb10">상품 검색</p>
				<div class="pl20 pr20">
					<div class="mb25">
						<p class="dib mr15 ml15 lh38 fl">상품명</p>
						<p class="dib">
							<input type="text" id="productSearchName" name="" autocomplete="off" value="" />
							<i id="productSearchBtn" class="ml10 fa fa-search" aria-hidden="true" title="검색" data-layer-id="generalProductView" data-kind="generalProduct" data-seq="<s:property value="seq"/>" > </i>
						</p>
					</div>
				</div>
				<div id="result" class="mt10">
					<span class="dib mt10">검색결과 : 0</span>
					<div id="listEmpty">
						<p class="tc">검색결과가 없습니다.<p>
					</div>
					<div id="listExist" class="hide">
					</div>
				</div>
				<div class="btn-r">
					<i class="layerClose fa fa-times fa-2x" aria-hidden="true" title="닫기"></i>
				</div>
			</div>
		</div>
	</div>
</div>