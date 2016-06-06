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
				
				$("#sizeAddBtn").click(function(){
					var inputHTML = '<li class="pointer ui-state-default">';
					inputHTML += '<input type="hidden" name="size_id" value="0" />';
					inputHTML += '<input type="hidden" name="size_val" value="새로운 사이즈" />';
					inputHTML += '<span>새로운 사이즈</span>';
					inputHTML += '</li>';
					$("#sortable").append(inputHTML);
					$("#sortable li").last().trigger("click");
				});
				
				$("#sizeDeleteBtn").click(function(){
					if(confirm("[" + $(this).prev().val() + "] 사이즈를 삭제하시겠습니까?")){
						var inputHTML = '<input type="hidden" name="deleteSizeList" value="' + $(".ui-selected").find("input[name='size_id']").val() + '" />';
						$("#sizeList").append(inputHTML);
						$(".ui-selected").remove();
						$(this).prev().val("");
						$("#sizeDetailAreaInput").hide();
					}
				});
			});
			
			$(document).on("click","#sortable li",function(e){
				$("#sortable li").removeClass("ui-selected");
				$(this).addClass("ui-selected");
				$("#sizeDetailAreaInput").show();
				$("#sizeDetailAreaInput #new_size_val").val($(this).find("input[name='size_val']").val());
			});
			
			$(document).on("keyup","#new_size_val",function(e){
				$(".ui-selected").find("input[name='size_val']").val($(this).val());
				$(".ui-selected").find("span").html($(this).val());
			});
		</script>
	</head>
	<body>
		<jsp:include page="/jsp/include/gnb.jsp" flush="false" />
		<div class="admin_container">
			<jsp:include page="/jsp/include/lnb.jsp" flush="false" />
			<div class="adminContentArea">
				<h2 class="mb20"><span>사이즈맵 관리</span></h2>
				<div class="contentBody">
					<s:form id="sizeList" name="sizeList" data-mode="EditorForm" data-confirm-msg="사이즈맵을 저장 하시겠습니까?" cssClass="dib" method="post" namespace="/contentManager" action="sizeListUpdateAction" theme="simple">
						<div id="sizeEditArea">
							<div class="dib fl">
								<ul id="sortable">
									<s:iterator value="dataList" status="stat">
										<li class="pointer ui-state-default"><input type="hidden" name="size_id" value="<s:property value="size_id"/>" /><input type="hidden" name="size_val" value="<s:property value="size_val"/>" /><span><s:property value="size_val"/></span></li>
									</s:iterator>
								</ul>
								<p id="sizeAddBtn" class="pointer">
									<i class="mt5 ml5 fa fa-plus db" title="라벨추가"></i>
								</p>
							</div>
							<div id="sizeDetailArea" class="fl dib">
								<div class="hide" id="sizeDetailAreaInput">
									<span class="mr10">사이즈명</span>
									<input type="text" autocomplete="off" id="new_size_val" value="" />
									<input type="button" id="sizeDeleteBtn" value="삭제" />
								</div>
								<div id="sizeDetailAreaDefault">
									<h4>사이즈맵 설정 안내</h4>
									<ul>
										<li>+버튼을 클릭하면 사이즈를 추가할 수 있습니다.</li>
										<li>드래그 앤 드롭으로 사이즈 순서를 변경할 수 있습니다.</li>
										<li>사이즈를 클릭하면 상세내용이 표시됩니다.</li>
										<li>
											<span>사이즈를 삭제하면 해당사이즈가 등록되어있는 상품이<br/> 영향을 받게되므로 참고 바랍니다.</span>
										</li>
										<li>
											<span>사이즈를 편집한 후에 저장하기 버튼을 꼭 클릭해야 변경된<br/>내용이 반영됩니다.</span>
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