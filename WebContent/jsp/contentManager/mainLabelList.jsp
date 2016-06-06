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
				$( "#sortable" ).sortable({
					placeholder: "ui-state-highlight"
				});
				$( "#sortable" ).disableSelection();
				
				$("#mainLabelAddBtn").click(function(){
					var inputHTML = '<li class="pointer ui-state-default">';
					inputHTML += '<input type="hidden" name="main_type" value="0" />';
					inputHTML += '<input type="hidden" name="label_name" value="새로운 라벨" />';
					inputHTML += '<span>새로운 라벨</span>';
					inputHTML += '</li>';
					$("#sortable").append(inputHTML);
					$("#sortable li").last().trigger("click");
				});
				
				$("#labelDeleteBtn").click(function(){
					if(confirm("[" + $(this).prev().val() + "] 라벨을 삭제하시겠습니까?")){
						var inputHTML = '<input type="hidden" name="deleteLabelList" value="' + $(".ui-selected").find("input[name='main_type']").val() + '" />';
						$("#mainLabel").append(inputHTML);
						$(".ui-selected").remove();
						$(this).prev().val("");
						$("#mainLabelDetailAreaInput").hide();
					}
				});
			});
			
			$(document).on("click","#sortable li",function(e){
				$("#sortable li").removeClass("ui-selected");
				$(this).addClass("ui-selected");
				$("#mainLabelDetailAreaInput").show();
				$("#mainLabelDetailAreaInput #new_label_name").val($(this).find("input[name='label_name']").val());
			});
			
			$(document).on("keyup","#new_label_name",function(e){
				$(".ui-selected").find("input[name='label_name']").val($(this).val());
				$(".ui-selected").find("span").html($(this).val());
			});
		</script>
	</head>
	<body>
		<jsp:include page="/jsp/include/gnb.jsp" flush="false" />
		<div class="admin_container">
			<jsp:include page="/jsp/include/lnb.jsp" flush="false" />
			<div class="adminContentArea">
				<h2 class="mb20"><span>메인라벨관리</span></h2>
				<div class="contentBody">
					<s:form id="mainLabel" name="mainLabelList" data-mode="EditorForm" data-confirm-msg="라벨을 저장 하시겠습니까?" cssClass="dib" method="post" namespace="/contentManager" action="mainLabelUpdateAction" theme="simple">
						<div id="mainLabelEditArea">
							<div class="dib fl">
								<ul id="sortable">
									<s:iterator value="dataList" status="stat">
										<li class="pointer ui-state-default"><input type="hidden" name="main_type" value="<s:property value="main_type"/>" /><input type="hidden" name="label_name" value="<s:property value="label_name"/>" /><span><s:property value="label_name"/></span></li>
									</s:iterator>
								</ul>
								<p id="mainLabelAddBtn" class="pointer">
									<i class="mt5 ml5 fa fa-plus db" title="라벨추가"></i>
								</p>
							</div>
							<div id="mainLabelDetailArea" class="fl dib">
								<div class="hide" id="mainLabelDetailAreaInput">
									<span class="mr10">라벨명</span>
									<input type="text" autocomplete="off" id="new_label_name" value="" />
									<input type="button" id="labelDeleteBtn" value="삭제" />
								</div>
								<div id="mainLabelDetailAreaDefault">
									<h4>라벨 설정 안내</h4>
									<ul>
										<li>+버튼을 클릭하면 라벨을 추가할 수 있습니다.</li>
										<li>드래그 앤 드롭으로 라벨 순서를 변경할 수 있습니다.</li>
										<li>라벨을 클릭하면 상세내용이 표시됩니다.</li>
										<li>
											<span>라벨을 삭제하면 해당라벨의 등록되어있는 컨텐츠가<br/> 자동으로 해제됩니다.</span>
										</li>
										<li>
											<span>라벨을 편집한 후에 저장하기 버튼을 꼭 클릭해야 변경된<br/>내용이 반영됩니다.</span>
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