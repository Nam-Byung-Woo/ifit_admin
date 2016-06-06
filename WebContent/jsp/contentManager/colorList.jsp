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
				if($("#colorpicker").length>0){
					// 색상선택API
					$('#colorpicker').farbtastic("#new_color_val");
				}
				
				$( "#sortable" ).sortable({
					placeholder: "ui-state-highlight"
				});
				$( "#sortable" ).disableSelection();
				
				$("#colorAddBtn").click(function(){
					var inputHTML = '<li class="pointer ui-state-default">';
					inputHTML += '<input type="hidden" name="color_id" value="0" />';
					inputHTML += '<input type="hidden" name="color_val" value="#000000" />';
					inputHTML += '<span>검정(#000000)</span>';
					inputHTML += '</li>';
					$("#sortable").append(inputHTML);
					$("#sortable li").last().trigger("click");
				});
				
				$("#colorDeleteBtn").click(function(){
					if(confirm("[" + $(this).prev().val() + "] 컬러를 삭제하시겠습니까?")){
						var inputHTML = '<input type="hidden" name="deleteColorList" value="' + $(".ui-selected").find("input[name='color_id']").val() + '" />';
						$("#colorList").append(inputHTML);
						$(".ui-selected").remove();
						$(this).prev().val("");
						$("#colorDetailAreaInput").hide();
					}
				});
			});
			
			$(document).on("click","#sortable li",function(e){
				$("#sortable li").removeClass("ui-selected");
				$(this).addClass("ui-selected");
				$("#colorDetailAreaInput").show();
				$("#colorDetailAreaInput #new_color_val").val($(this).find("input[name='color_val']").val());
			});
			
			$(document).on("keyup","#new_color_val",function(e){
				$(".ui-selected").find("input[name='color_val']").val($(this).val());
				$(".ui-selected").find("span").html($(this).val());
			});
		</script>
	</head>
	<body>
		<jsp:include page="/jsp/include/gnb.jsp" flush="false" />
		<div class="admin_container">
			<jsp:include page="/jsp/include/lnb.jsp" flush="false" />
			<div class="adminContentArea">
				<h2 class="mb20"><span>컬러맵 관리</span></h2>
				<div class="contentBody">
					<s:form id="colorList" name="colorList" data-mode="EditorForm" data-confirm-msg="컬러맵을 저장 하시겠습니까?" cssClass="dib" method="post" namespace="/contentManager" action="colorListUpdateAction" theme="simple">
						<div id="colorEditArea">
							<div class="dib fl">
								<ul id="sortable">
									<s:iterator value="dataList" status="stat">
										<li class="pointer ui-state-default"><input type="hidden" name="color_id" value="<s:property value="color_id"/>" /><input type="hidden" name="color_val" value="<s:property value="color_val"/>" /><span><s:property value="color_val"/></span></li>
									</s:iterator>
								</ul>
								<p id="colorAddBtn" class="pointer">
									<i class="mt5 ml5 fa fa-plus db" title="라벨추가"></i>
								</p>
							</div>
							<div id="colorDetailArea" class="fl dib">
								<div class="hide" id="colorDetailAreaInput">
									<span class="mr10">컬러명</span>
									<div class="dib" id="colorpicker"></div>
									<input type="text" autocomplete="off" id="new_color_val" value="#000000" />
									<input type="button" id="colorDeleteBtn" value="삭제" />
								</div>
								<div id="colorDetailAreaDefault">
									<h4>컬러맵 설정 안내</h4>
									<ul>
										<li>+버튼을 클릭하면 컬러를 추가할 수 있습니다.</li>
										<li>드래그 앤 드롭으로 컬러 순서를 변경할 수 있습니다.</li>
										<li>컬러를 클릭하면 상세내용이 표시됩니다.</li>
										<li>
											<span>컬러를 삭제하면 해당컬러가 등록되어있는 상품이<br/> 영향을 받게되므로 참고 바랍니다.</span>
										</li>
										<li>
											<span>컬러를 편집한 후에 저장하기 버튼을 꼭 클릭해야 변경된<br/>내용이 반영됩니다.</span>
										</li>
									</ul>
								</div>
							</div>
						</div>
						<div class="mt20 tc">
							<input type="submit" class="updateActionBtn simpleBtn mb10 btn2 clear" value="저장" />
						</div>
					</s:form>
				</div>
			</div>
		</div>		
	</body>
</html>