<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" type="text/css" href="/jsp/common/plugin/jssor.slider/css/jssor.slider.customize.css" />
<script type="text/javascript" src="/jsp/common/plugin/jssor.slider/js/jssor.slider.mini.js"></script>
<script type="text/javascript" src="/jsp/common/plugin/jssor.slider/js/imageSliderCustomize.js"></script>
<script type="text/javascript">
	$(function(){
	    $("#jsonObj").change(function(){
	    	var jsonObj = JSON.parse($(this).val());
	    	console.log(jsonObj);
	    	var colorData = jsonObj.colorData;
	    	var detailPhotoData = jsonObj.detailPhotoData;
	    	var productData = jsonObj.productData;
	    	var sizeData = jsonObj.sizeData;
	    	var subPhotoData = jsonObj.subPhotoData;
	    	var tagData = jsonObj.tagData;
	    	
	    	$("#view_p_name").html(productData.p_name);
	    	console.log(subPhotoData);
	    	for(key in subPhotoData) {
	    		var subPhotoDataHTML = '';
	    		subPhotoDataHTML += '<div data-p="225.00" style="display: none;">';
	    		subPhotoDataHTML += '<img data-u="image" src="../jsp/common/img/logo.png" />';
	    		subPhotoDataHTML += '</div>';
	    		$("#jssor_1_container").append(subPhotoDataHTML);
	    	}
	    	var jssor_slider1 = new $JssorSlider$('slider1_container', options);
	    });
	});
</script>
<div class="layer layer-generalProductView" data-close-answer="false">
	<input id="jsonObj" type="hidden" />
	<div class="bg"></div>
	<div id="popLayer" class="pop-layer generalProductViewPop">
		<div class="pop-container">
			<div class="pop-conts">
				<!--content //-->
				<p id="view_p_name" class="pop-title mb20 pb10"></p>
				<div class="pl20 pr20 contentsArea">
					<div class="photoArea" style="width:100px;">	
						<div id="jssor_1" class="slider_image">
					        <div id="jssor_1_container" class="slider_image" data-u="slides">
					        	<div data-p="225.00" style="display: none;">
					        		<img data-u="image" src="../jsp/common/img/logo.png" />
					        	</div>
					        </div>
					        <!-- Bullet Navigator -->
					        <div data-u="navigator" class="jssorb05" data-autocenter="1">
					            <!-- bullet navigator item prototype -->
					            <div data-u="prototype"></div>
					        </div>
					        <!-- Arrow Navigator -->
					        <!-- 일단보류
					        <span data-u="arrowleft" class="jssora22l" data-autocenter="2"></span>
					        <span data-u="arrowright" class="jssora22r" data-autocenter="2"></span>
					         -->
					    </div>
					</div>
					
					<div class="mb25">
						<p class="dib mr15 fl">상품분류</p>
						<p id="productData-p_ps" class="pop-content-box dib"></p>
					</div>
					<div class="mb25">
						<p class="dib mr15 fl">가격</p>
						<p id="productData-p_price" class="pop-content-box dib"></p>
					</div>
					<div class="mb25">
						<p class="dib mr15 fl" >색상</p>
						<p class="vm mr10 colorSelected ellipse" style="background-color: #ffffff;"></p>
					</div>
					<div class="mb25">
						<p class="dib mr15 fl" >size</p>
						<p id="generalProduct_sizeMap" class="pop-content-box dib"></p>
					</div>
					<div class="mb25">
						<p class="dib mr15 fl" >메인이미지</p>
						<p class="vm mr10 colorSelected ellipse" style="background-color: #ffffff;"></p>
					</div>
					<div class="mb25">
						<p class="dib mr15 fl" >상세이미지</p>
						<p class="vm mr10 colorSelected ellipse" style="background-color: #ffffff;"></p>
					</div>
					<div class="mb25">
						<p class="dib mr15 fl" >3D 코드</p>
						<p id="productData-cat_ref" class="pop-content-box dib"></p>
					</div>
					<div class="mb25">
						<p class="dib mr15 fl" >상세설명</p>
						<p id="productData-detail_info" class="pop-content-box dib"></p>
					</div>
					<div class="mb25">
						<p class="dib mr15 fl" >태그</p>
						<p class="vm mr10 colorSelected ellipse" style="background-color: #ffffff;"></p>
					</div>
				</div>
				<div class="btn-r">
					<i class="layerClose fa fa-times fa-2x" aria-hidden="true" title="닫기"></i>
				</div>
			</div>
		</div>
	</div>
</div>