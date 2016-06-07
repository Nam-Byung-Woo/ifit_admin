<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<jsp:include page="/jsp/include/head.jsp" flush="false">
			<jsp:param value=""  name="title"/>
		</jsp:include>
		<script type="text/javascript">
			function fileDeleteOnUpdate(fileObj){
				var inputHTML = '<input type="hidden" name="' + fileObj.input + '" value="' + fileObj.name + '" />';
				$("#promotion").append(inputHTML);
			}
			$(function(){
				// 배너 이미지
				$('#pro_url').filer({
				    limit: 1,
				    maxSize: 10,
				    extensions: ['jpeg', 'jpg', 'gif', 'bmp', 'png'],
				    showThumbs: true,
				    templates: FilerTemplate.edit,
				    addMore: true
				});
				
				// 업데이트시 이미지 등록
				var pro_url = "<s:property value="promotionListDTO.pro_url"/>";
				var pro_url_name = "<s:property value="promotionListDTO.pro_url_name"/>";
				if(pro_url != ""){
					var pro_url_obj = $("#pro_url").prop("jFiler");
					var pro_url_data = {
							name: pro_url_name,
						    type: "image",
						    file: "http://"+ pro_url,
						    input: "pro_urlDeleteFileName"
					};
					pro_url_obj.append(pro_url_data);
					pro_url_obj.options.onRemove = function(dom, obj){
				    	fileDeleteOnUpdate(obj);
				    };
				}
			});
		</script>
	</head>
	<body>
		<jsp:include page="/jsp/include/gnb.jsp" flush="false" />
		<div class="admin_container">
			<jsp:include page="/jsp/include/lnb.jsp" flush="false" />
			<s:if test='isUpdateMode.equals("true")'>
				<s:set name="formNameKor" value='%{"수정"}' />
				<s:set name="formNameEng" value='%{"promotionUpdate"}' />
			</s:if>
			<s:else>
				<s:set name="formNameKor" value='%{"등록"}' />
				<s:set name="formNameEng" value='%{"promotionWrite"}' />
			</s:else>
			<div class="adminContentArea">
				<h2 class="mb20"><span>프로모션 ${formNameKor}</span></h2>
				<div class="contentBody">
					<s:form id="promotion" name="promotionEditor" data-mode="EditorForm" data-confirm-msg="프로모션을 ${formNameKor} 하시겠습니까?" cssClass="dib" method="post" namespace="/banner" action="%{formNameEng}Action" theme="simple" enctype="multipart/form-data">
						<input type="hidden" id="seq" name="seq" value="<s:property value='seq' />"  /> 
						<input type="hidden" id="queryDecode" name="queryDecode" value="<s:property value='queryDecode' />"  disabled />
						<table class="table_editor tc">
							<colgroup>
								<col width="180px"><col width="*">
							</colgroup>
							<tbody>
								<s:set name="promotionData" value="promotionListDTO" />
								<tr>
									<th scope="col">프로모션 이름</th>
									<td tabIndex="1" class="tl" id="pro_name_check">
										<input type="text" class="validate" autocomplete="off" id="pro_name" name="pro_name" value="${promotionData.pro_name}" />
									</td>
								</tr>
								<tr>
									<th scope="col">프로모션 이미지</th>
									<td class="tl" tabIndex="2" id="pro_url_check">
										<input type="file" id="pro_url" class="validate pro_url" name="pro_url" autocomplete="off" multiple="multiple" />
									</td>
								</tr>
							</tbody>
						</table>
						<div class="mt20 clear">
							<input type="button" class="listBtn simpleBtn mb10 btn2 dib fl" value="목록으로" />
							<s:if test='isUpdateMode.equals("true")'>
								<input type="submit" class="updateActionBtn simpleBtn mb10 btn2 dib fr" value="수정" />
							</s:if>
							<s:else>
								<input type="submit" class="writeActionBtn simpleBtn mb10 btn2 dib fr" value="등록" />
							</s:else>
						</div>
					</s:form>
				</div>
			</div>
		</div>
	</body>
</html>