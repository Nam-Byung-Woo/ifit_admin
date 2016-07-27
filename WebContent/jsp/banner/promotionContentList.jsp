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
				$("#pro_seq").change(function(){
					$(this).parents("form").submit();
				});
				
				$( "#sortable" ).sortable({
					placeholder: "ui-state-highlight"
				});
				$( "#sortable" ).disableSelection();
				
				$("#contentDeleteBtn").click(function(){
					if(confirm("[" + $(this).prev().val() + "] 컨텐츠를 삭제하시겠습니까?")){
						var inputHTML = '<input type="hidden" name="deleteContentList" value="' + $(".ui-selected").find("input[name='pro_map_seq']").val() + '" />';
						$("#promotionContent").append(inputHTML);
						$(".ui-selected").remove();
						$(this).prev().val("");
						$("#promotionContentDetailAreaInput").hide();
					}
				});
			});
			
			$(document).on("click","#sortable li",function(e){
				$("#sortable li").removeClass("ui-selected");
				$(this).addClass("ui-selected");
				$("#promotionContentDetailAreaInput").show();
				$("#promotionContentDetailAreaInput #new_p_name").val($(this).find("input[name='p_name']").val());
			});
			
			/*
			$(document).on("keyup","#new_p_name",function(e){
				$(".ui-selected").find("input[name=p_name']").val($(this).val());
				$(".ui-selected").find("span").html($(this).val());
			});
			*/
		</script>
	</head>
	<body>
		<jsp:include page="/jsp/include/gnb.jsp" flush="false" />
		<div class="admin_container">
			<jsp:include page="/jsp/include/lnb.jsp" flush="false" />
			<div class="adminContentArea">
				<h2 class="mb20"><span>프로모션컨텐츠</span></h2>
				<div class="contentBody">
					<s:if test="promotionList.size>0">
						<s:form id="promotionContent2" name="promotionContentList" method="get" namespace="/banner" action="promotionContentList" theme="simple">
							<s:select id="pro_seq" name="pro_seq" list="promotionList" listKey="pro_seq" listValue="pro_name" />
						</s:form>
					</s:if>
					<s:form id="promotionContent" name="promotionContentEditor" data-mode="EditorForm" data-confirm-msg="프로모션컨텐츠를 저장 하시겠습니까?" cssClass="dib" method="post" namespace="/banner" action="promotionContentUpdateAction" theme="simple">
						<input type="hidden" name="pro_seq" value="<s:property value="pro_seq"/>" />
						<div id="promotionContentEditArea">
							<s:if test="promotionList.size>0">
								<div class="dib fl">
									<ul id="sortable">
										<s:iterator value="dataList" status="stat">
											<li class="pointer ui-state-default"><input type="hidden" name="pro_map_seq" value="<s:property value="pro_map_seq"/>" /><input type="hidden" id="p_id" name="p_id" value="<s:property value="p_id"/>" /><input type="hidden" name="p_name" value="<s:property value="p_name"/>" /><span><s:property value="p_name"/></span></li>
										</s:iterator>
									</ul>
									<p id="promotionContentAddBtn" class="pointer openPop" data-pop-id="productSearch">
										<i class="mt5 ml5 fa fa-plus db" title="컨텐츠추가" ></i>
									</p>
								</div>
								<div id="promotionContentDetailArea" class="fl dib">
									<div class="hide" id="promotionContentDetailAreaInput">
										<span class="mr10">컨텐츠명</span>
										<input type="text" autocomplete="off" id="new_p_name" value="" readonly />
										<input type="button" id="contentDeleteBtn" value="삭제" />
									</div>
									<div id="promotionContentDetailAreaDefault">
										<h4>프로모션컨텐츠 설정 안내</h4>
										<ul>
											<li>상단 셀렉트박스로 원하는 프로모션을 선택할 수 있습니다.</li>
											<li>+버튼을 클릭하면 컨텐츠를 추가할 수 있습니다.</li>
											<li>드래그 앤 드롭으로 순서를 변경할 수 있습니다.</li>
											<li>컨텐츠를 클릭하면 상세내용이 표시됩니다.</li>
											<li>
												<span>컨텐츠를 편집한 후에 저장하기 버튼을 꼭 클릭해야<br/>변경된 내용이 반영됩니다.</span>
											</li>
										</ul>
									</div>
								</div>
							</s:if>
							<s:else>
								등록된 프로모션이 없습니다.<br/>
								먼저 프로모션을 등록해 주세요.
							</s:else>
						</div>
						<s:if test="promotionList.size>0">
							<div class="mt20 tc">
								<input type="submit" class="updateActionBtn simpleBtn mb10 btn2 clear" value="저장" />
							</div>
						</s:if>
					</s:form>
				</div>
			</div>
		</div>
		
		<jsp:include page="/jsp/banner/pop/productSearch.jsp" flush="false" />
				
	</body>
</html>