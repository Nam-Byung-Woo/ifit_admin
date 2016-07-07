<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" type="text/css" href="/jsp/common/plugin/jssor.slider/css/jssor.slider.customize.css" />
<script type="text/javascript" src="/jsp/common/plugin/jssor.slider/js/jssor.slider.mini.js"></script>
<script type="text/javascript" src="/jsp/common/plugin/jssor.slider/js/imageSliderCustomize.js"></script>
<script type="text/javascript">
	$(function(){
	    $("#jsonObj").change(function(){
	    	var target = $(".qnaViewPop");
	    	var jsonObj = JSON.parse($(this).val());

	    	target.find("#seq").val(jsonObj.quest_seq);
	    	target.find("#title").html(jsonObj.title);
	    	target.find("#user_id").html(jsonObj.user_id);
	    	target.find("#content").html(jsonObj.content);
	    	target.find("#quest_date").html(jsonObj.quest_date);
	    	if(jsonObj.state==1){
	    		target.find("#state").html("답변대기");
	    		target.find("input[type=submit]").addClass("writeActionBtn");
	    		target.find("input[type=submit]").val("등록");
	    		target.find("form").attr("data-confirm-msg","답변을 등록 하시겠습니까?");
	    		target.find("form").attr("action","qnaReplyWriteAction.ifit");
	    	}else{
	    		target.find("#state").html("답변완료");
	    		target.find("input[type=submit]").addClass("updateActionBtn");
	    		target.find("input[type=submit]").val("수정");
	    		target.find("form").attr("data-confirm-msg","답변을 수정 하시겠습니까?");
	    		target.find("form").attr("action","qnaReplyUpdateAction.ifit");
	    		target.find("#reply").val(jsonObj.reply);
	    	}
	    });
	    $(document).on("click",".layerClose",function(e){
	    	$(".qnaViewPop #reply").val("");
		});
	});
</script>
<style>
.slider_image {
    width: 343px;
    height: 437px;
}
</style>
<div class="layer layer-qnaView" data-close-answer="false">
	<div class="bg"></div>
	<div id="popLayer" class="pop-layer qnaViewPop">
		<div class="pop-container">
			<div class="pop-conts">
				<s:form id="qna" name="qnaEditor" data-mode="EditorForm" data-confirm-msg="" cssClass="dib" method="post" namespace="/help" action="" theme="simple" enctype="multipart/form-data">
					<input id="jsonObj" type="hidden" />
					<input type="hidden" id="seq" name="seq" value=""  /> 
					<!--content //-->
					<p id="title" class="pop-title mb20 pb10"></p>
					<div class="pl20 pr20 contentsArea">
						<table class="table_editor">
							<colgroup>
								<col width="180px"><col width="*">
							</colgroup>
							<tbody>
								<tr>
									<th scope="col">작성회원</th>
									<td id="user_id"></td>
								</tr>
								<tr>
									<th scope="col">내용</th>
									<td id="content"></td>
								</tr>
								<tr>
									<th scope="col">문의 등록일</th>
									<td id="quest_date"></td>
								</tr>
								<tr>
									<th scope="col">상태</th>
									<td id="state"></td>
								</tr>
								<tr>
									<th scope="col">답변내용</th>
									<td tabIndex="1" id="reply_check"><textarea id="reply" name="reply" class="validate"></textarea></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="mt20 tc clear">
						<input type="submit" class="simpleBtn mb10 btn2 dib" value="" />
					</div>
				</s:form>
				<div class="btn-r">
					<i class="layerClose fa fa-times fa-2x" aria-hidden="true" title="닫기"></i>
				</div>
			</div>
		</div>
	</div>
</div>