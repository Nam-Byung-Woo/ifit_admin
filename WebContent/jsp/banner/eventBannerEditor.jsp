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
				$("#eventBanner").append(inputHTML);
			}
			$(function(){
				// 배너 이미지
				$('#banner_url').filer({
				    limit: 1,
				    maxSize: 10,
				    extensions: ['jpeg', 'jpg', 'gif', 'bmp', 'png'],
				    showThumbs: true,
				    templates: FilerTemplate.edit,
				    addMore: true
				});
				
				// 업데이트시 이미지 등록
				var banner_url = "<s:property value="eventBannerDTO.banner_url"/>";
				var banner_url_name = "<s:property value="eventBannerDTO.banner_url_name"/>";
				if(banner_url != ""){
					var banner_url_obj = $("#banner_url").prop("jFiler");
					var banner_url_data = {
							name: banner_url_name,
						    type: "image",
						    file: "http://"+ banner_url,
						    input: "banner_urlDeleteFileName"
					};
					banner_url_obj.append(banner_url_data);
					banner_url_obj.options.onRemove = function(dom, obj){
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
				<s:set name="formNameEng" value='%{"eventBannerUpdate"}' />
			</s:if>
			<s:else>
				<s:set name="formNameKor" value='%{"등록"}' />
				<s:set name="formNameEng" value='%{"eventBannerWrite"}' />
			</s:else>
			<div class="adminContentArea">
				<h2 class="mb20"><span>이벤트배너 ${formNameKor}</span></h2>
				<div class="contentBody">
					<s:form id="eventBanner" name="eventBannerEditor" data-mode="EditorForm" data-confirm-msg="이벤트배너를 ${formNameKor} 하시겠습니까?" cssClass="dib" method="post" namespace="/banner" action="%{formNameEng}Action" theme="simple" enctype="multipart/form-data">
						<input type="hidden" id="seq" name="seq" value="<s:property value='seq' />"  /> 
						<input type="hidden" id="queryDecode" name="queryDecode" value="<s:property value='queryDecode' />"  disabled />
						<table class="table_editor tc">
							<colgroup>
								<col width="180px"><col width="*">
							</colgroup>
							<tbody>
								<s:set name="eventBannerData" value="eventBannerDTO" />
								<tr>
									<th scope="col">타입</th>
									<td tabIndex="1" class="tl" id="banner_type_check">
										<s:radio id="banner_type" name="banner_type" cssClass="validate" list="Code.eventBannerTypeMap" value="%{#eventBannerData.banner_type}" />
									</td>
								</tr>
								<tr>
									<th scope="col">배너이미지</th>
									<td class="tl" tabIndex="2" id="banner_url_check">
										<input type="file" id="banner_url" class="validate banner_url" name="banner_url" autocomplete="off" multiple="multiple" />
									</td>
								</tr>
								<tr>
									<th scope="col">연결상품</th>
									<td class="tl" tabIndex="2" id="p_name_check">
										<input type="text" id="p_name" class="pointer openPop" data-pop-id="productSearch" autocomplete="off" multiple="multiple" placeholder="상품 검색" value="${eventBannerData.p_name}" readonly />
										<input type="hidden" id="p_id" class="validate p_id" name="p_id" autocomplete="off" multiple="multiple" value="${eventBannerData.p_id}" />
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
		<jsp:include page="/jsp/banner/pop/productSearch.jsp" flush="false" />
	</body>
</html>