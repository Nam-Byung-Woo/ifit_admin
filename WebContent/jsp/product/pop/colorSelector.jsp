<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
$(document).ready(function() {
	if($("#colorpicker").length>0){
		// 색상선택API
		$('#colorpicker').farbtastic("#hexColor");
	}
	
	$(".selectBtn").click(function(){
		var selectedColor = $("#hexColor").val();
		
		var pattern = /^#?([a-f0-9]{6}|[a-f0-9]{3})$/;
		if(!pattern.test(selectedColor)){
			alert("올바른 색상을 선택해 주세요.");
			return false;
		}
		
		var colorHTML = "";
		colorHTML += '<p class="colorReady vm mr10">';
		colorHTML += '<i class="openPop ml10 fa fa-plus" data-pop-id="colorSelector" title="색상추가"></i>';
		colorHTML += '</p>';
		
		$(".colorReady").last().css("background-color", selectedColor);
		$(".colorReady").last().addClass("colorSelected");
		$(".colorReady").last().addClass("ellipse");
		$(".colorReady").last().removeClass("colorReady");
		$(".colorSelected").last().append('<input type="text" id="color_list" name="color_list" class="validate arrayData hide" value="' + selectedColor + '" readonly />');
		if($(".colorSelected").length < 10){
			$(".colorSelected").last().after(colorHTML);
		}
		
		$('.layer-colorSelector .layerClose').trigger('click');
	});
	
	$(document).on("click",".colorSelected",function(e){
		if(!confirm("해당 색상을 삭제 하시겠습니까?")){
			return false;
		}
		
		$(this).remove();
		
		if($(".colorSelected").length == 10-1){
			var colorHTML = "";
			colorHTML += '<p class="colorReady vm mr10">';
			colorHTML += '<i class="openPop ml10 fa fa-plus" data-pop-id="colorSelector" title="색상추가"></i>';
			colorHTML += '</p>';
			$(".colorSelected").last().after(colorHTML);
		}
		
	});
});
</script>
<div class="layer layer-colorSelector" data-close-answer="false" >
	<div class="bg"></div>
	<div id="popLayer" class="pop-layer">
		<div class="pop-container">
			<div class="pop-conts">
				<!--content //-->
				<p id="pop-title" class="pop-title mb20 pb10">색상 선택</p>
				<div class="pl20 pr20">
					<div class="mb25">
						<p class="dib">
							<div class="dib" id="colorpicker"></div>
							<input type="text" id="hexColor" class="tc ml30" value="#000000" />
						</p>
					</div>
				</div>
				<div class="tc mt100">
					<input type="button" class="selectBtn simpleBtn" value="선택" />
				</div>
				<div class="btn-r">
					<i class="layerClose fa fa-times fa-2x" aria-hidden="true" title="닫기"></i>
				</div>
			</div>
		</div>
	</div>
</div>