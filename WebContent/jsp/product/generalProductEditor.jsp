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
				$("#generalProduct").append(inputHTML);
			}
			$(function(){
				// 상품 메인 이미지
				$('#p_main_url').filer({
				    limit: 1,
				    maxSize: 10,
				    extensions: ['jpeg', 'jpg', 'gif', 'bmp', 'png'],
				    showThumbs: true,
				    templates: FilerTemplate.edit,
				    addMore: true
				});

				// 상품 서브 이미지
				$('#sub_url').filer({
				    limit: 5,
				    maxSize: 10,
				    extensions: ['jpeg', 'jpg', 'gif', 'bmp', 'png'],
				    showThumbs: true,
				    templates: FilerTemplate.edit,
				    addMore: true
				});
				
				// 상품 상세 이미지
				$('#detail_url').filer({
				    limit: 5,
				    maxSize: 10,
				    extensions: ['jpeg', 'jpg', 'gif', 'bmp', 'png'],
				    showThumbs: true,
				    templates: FilerTemplate.edit,
				    addMore: true
				});
				
				// 상품 코디 이미지
				$('#lookup_url').filer({
				    limit: 1,
				    maxSize: 10,
				    extensions: ['jpeg', 'jpg', 'gif', 'bmp', 'png'],
				    showThumbs: true,
				    templates: FilerTemplate.edit,
				    addMore: true
				});
				
				// 업데이트시 이미지 등록
				var p_main_url = "<s:property value="productListDTO.p_main_url"/>";
				var p_main_url_name = "<s:property value="productListDTO.p_main_url_name"/>";
				if(p_main_url != ""){
					var p_main_url_obj = $("#p_main_url").prop("jFiler");
					var p_main_url_data = {
							name: p_main_url_name,
						    type: "image",
						    file: "http://"+p_main_url,
						    input: "p_main_urlDeleteFileName"
					};
					p_main_url_obj.append(p_main_url_data);
					p_main_url_obj.options.onRemove = function(dom, obj){
				    	fileDeleteOnUpdate(obj);
				    };
				}
				
				<s:iterator value="subPhotoList" status="stat">
					var sub_url = "<s:property value="photo_url"/>";
					var sub_url_name = "<s:property value="photo_url_name"/>";
					var sub_url_obj = $("#sub_url").prop("jFiler");
					var sub_url_data = {
							name: sub_url_name,
						    type: "image",
						    file: "http://"+sub_url,
						    input: "sub_urlDeleteFileName"
					};
					sub_url_obj.append(sub_url_data);
					sub_url_obj.options.onRemove = function(dom, obj){
				    	fileDeleteOnUpdate(obj);
				    };
				</s:iterator>
				
				<s:iterator value="detailPhotoList" status="stat">
					var detail_url = "<s:property value="photo_url"/>";
					var detail_url_name = "<s:property value="photo_url_name"/>";
					var detail_url_obj = $("#detail_url").prop("jFiler");
					var detail_url_data = {
							name: detail_url_name,
						    type: "image",
						    file: "http://"+detail_url,
						    input: "detail_urlDeleteFileName"
					};
					detail_url_obj.append(detail_url_data);
					detail_url_obj.options.onRemove = function(dom, obj){
				    	fileDeleteOnUpdate(obj);
				    };
				</s:iterator>
				
				var lookup_url = "<s:property value="productListDTO.lookup_url"/>";
				var lookup_url_name = "<s:property value="productListDTO.lookup_url_name"/>";
				if(lookup_url != ""){
					var lookup_url_obj = $("#lookup_url").prop("jFiler");
					var lookup_url_data = {
							name: lookup_url_name,
						    type: "image",
						    file: "http://"+lookup_url,
						    input: "lookup_urlDeleteFileName"
					};
					lookup_url_obj.append(lookup_url_data);
					lookup_url_obj.options.onRemove = function(dom, obj){
				    	fileDeleteOnUpdate(obj);
				    };
				}
				
				// 태그 입력폼 추가
				$(document).on("click",".addTag",function(e){
					$(this).hide();
					
					var tagHTML = '';
					
					tagHTML += '<p class="mb10">';
					tagHTML += '<input type="text" name="tag_list" class="validate arrayData" autocomplete="off" />';
					tagHTML += '<i class="removeTag ml10 fa fa-times" title="태그삭제"></i>';
					tagHTML += '<i class="addTag mt10 ml5 fa fa-plus db" title="태그추가"></i>';
					tagHTML += '</p>';
					
					$(this).parent().after(tagHTML);
					$(this).parents("td").children("p").first().children(".removeTag").show();
					
					if($(this).parents("td").children("p").length >= 10){
						$(this).parents("td").children("p").last().children(".addTag").hide();
					}
				});
				
				// 태그 입력폼 삭제
				$(document).on("click",".removeTag",function(e){
					if(!confirm("해당 태그를 삭제 하시겠습니까?")){
						return false;
					}
					$(this).parent().remove();
					
					$(".addTag").last().show();		
					
					if($(".removeTag").length == 1){
						$(".removeTag").hide();
					}
				});
			});
		</script>
	</head>
	<body>
		<jsp:include page="/jsp/include/gnb.jsp" flush="false" />
		<div class="admin_container">
			<jsp:include page="/jsp/include/lnb.jsp" flush="false" />
			<s:if test='isUpdateMode.equals("true")'>
				<s:set name="formNameKor" value='%{"수정"}' />
				<s:set name="formNameEng" value='%{"generalProductUpdate"}' />
			</s:if>
			<s:else>
				<s:set name="formNameKor" value='%{"등록"}' />
				<s:set name="formNameEng" value='%{"generalProductWrite"}' />
			</s:else>
			<div class="adminContentArea">
				<h2 class="mb20"><span>일반상품 ${formNameKor}</span></h2>
				<div class="contentBody">
					<s:form id="generalProduct" name="generalProductEditor" data-mode="EditorForm" data-confirm-msg="상품을 ${formNameKor} 하시겠습니까?" cssClass="dib" method="post" namespace="/product" action="%{formNameEng}Action" theme="simple" enctype="multipart/form-data">
						<input type="hidden" id="seq" name="seq" value="<s:property value='seq' />"  /> 
						<input type="hidden" id="queryDecode" name="queryDecode" value="<s:property value='queryDecode' />"  disabled />
						<table class="table_editor tc">
							<colgroup>
								<col width="180px"><col width="*">
							</colgroup>
							<tbody>
								<s:set name="productData" value="productListDTO" />
								<s:set name="sizeData" value="sizeMap" />
								<s:set name="colorData" value="colorMap" />
								<s:set name="tagData" value="tagList" />
								<s:if test = "#session.isAdmin">
									<tr>
										<th scope="col">입점업체명</th>
										<td tabIndex="1" id="admin_seq_check" >
											<input type="hidden" class="validate" id="admin_seq" name="admin_seq" value="${productData.admin_seq}" />
											<input type="text" class="pointer openPop" data-pop-id="shopSearch" autocomplete="off" placeholder="업체 검색" id="admin_name" value="${productData.admin_name}" readonly />
										</td>
									</tr>
								</s:if>
								<s:else>
									<input type="hidden" class="validate" id="admin_seq" name="admin_seq" value="<s:property value="#session.admin_seq" />" />
								</s:else>
								<tr>
									<th scope="col">상품명</th>
									<td tabIndex="2" id="p_name_check"><input type="text" class="validate" autocomplete="off" id="p_name" name="p_name" value="${productData.p_name}" /></td>
								</tr>
								<tr>
									<th scope="col">상품분류</th>
									<td class="tl" tabIndex="3" id="category_check">
										<s:select id="category" name="category" cssClass="validate" list="Code.productCategoryMap" headerKey="" headerValue="선택" value="%{#productData.category}" />
									</td>
								</tr>
								<tr>
									<th scope="col">가격</th>
									<td class="tl" tabIndex="4" id="p_price_check">
										<input type="text" class="number validate" autocomplete="off" id="p_price" name="p_price" value="${productData.p_price}" />원
										<span class="ml5">(ex: 10000, 20000 등 숫자로 입력)</span>
									</td>
								</tr>
								<tr>
									<th scope="col">색상</th>
									<td class="tl" tabIndex="5" id="colorArray_check">
										<s:if test='isUpdateMode.equals("true")'>
											<s:select id="colorArray" name="colorArray" cssClass="colorPreview validate" list="colorList" listKey="color_id" listValue="color_name+'('+color_val+')'" listTitle="color_val" multiple="true" value="%{#colorData.{color_id}}" />
										</s:if>
										<s:else>
											<s:select id="colorArray" name="colorArray" cssClass="colorPreview validate" list="colorList" listKey="color_id" listValue="color_name+'('+color_val+')'" listTitle="color_val" multiple="true" value="" />
										</s:else>
										<span class="ml5">(Ctrl 혹은 Shift 키를 누른상태로 클릭하면 복수선택 가능)</span>
									</td>
								</tr>
								<tr>
									<th scope="col">사이즈</th>
									<td class="tl" tabIndex="6" id="sizeArray_check">
										<s:if test='isUpdateMode.equals("true")'>
											<s:select id="sizeArray" name="sizeArray" cssClass="validate" data-test="test" list="sizeList" listKey="size_id" listValue="size_val" multiple="true" value="%{#sizeData.{size_id}}" />
										</s:if>
										<s:else>
											<s:select id="sizeArray" name="sizeArray" cssClass="validate" data-test="test" list="sizeList" listKey="size_id" listValue="size_val" multiple="true" value="" />
										</s:else>
										<span class="ml5">(Ctrl 혹은 Shift 키를 누른상태로 클릭하면 복수선택 가능)</span>
									</td>
								</tr>
								<tr>
									<th scope="col">메인이미지</th>
									<td class="tl imageArea" tabIndex="7" id="p_main_url_check">
										<span>메인, 서브이미지 가로: 세로 비율 = 1 : 1.275<br/>ex) 104</span>
										<input type="file" id="p_main_url" class="validate p_main_url" name="p_main_url" autocomplete="off" multiple="multiple" />
									</td>
								</tr>
								<tr>
									<th scope="col">서브이미지(슬라이드)</th>
									<td class="tl imageArea" tabIndex="8" id="sub_url_check">
										<span>메인, 서브이미지 가로: 세로 비율 = 1 : 1.275<br/>ex) 104</span>
										<input type="file" id="sub_url" class="validate sub_url" name="sub_url" autocomplete="off" multiple="multiple" />
									</td>
								</tr>
								<tr>
									<th scope="col">상세이미지</th>
									<td class="tl imageArea" tabIndex="9" id="detail_url_check">
										<span>메인, 서브이미지 가로: 세로 비율 = 1 : 1.275<br/>ex) 104</span>
										<input type="file" id="detail_url" class="validate detail_url" name="detail_url" autocomplete="off" multiple="multiple" />
									</td>
								</tr>
								<tr>
									<th scope="col">코디이미지</th>
									<td class="tl imageArea" tabIndex="10" id="lookup_url_check">
										<span>메인, 서브이미지 가로: 세로 비율 = 1 : 1.275<br/>ex) 104</span>
										<input type="file" id="lookup_url" class="validate lookup_url" name="lookup_url" autocomplete="off" multiple="multiple" />
									</td>
								</tr>
								<tr>
									<th scope="col">3D 코드</th>
									<td class="tl"><input type="text" class="validate" autocomplete="off" id="cat_ref" name="cat_ref" value="${productData.cat_ref}" /></td>
								</tr>
								<tr>
									<th scope="col">상세설명</th>
									<td class="tl">
										<textarea id="detail_info" name="detail_info" class="validate">${productData.detail_info}</textarea>
									</td>
								</tr>
								<tr>
									<th scope="col">태그</th>
									<td class="tl" tabIndex="10" id="tag_list_check">
										<s:iterator value="tagData" status="stat">
											<p class="mb10">
												<input type="text" name="tag_list" class="validate arrayData" autocomplete="off" value="${tag}" />
												<i class="removeTag ml7 fa fa-times <s:if test = "tagList.size()==1">hide</s:if>" title="태그삭제"></i>
												<i class="addTag mt10 ml5 fa fa-plus db <s:if test = "tagList.size()-1!=#stat.index">hide</s:if>" title="태그추가"></i>
											</p>
										</s:iterator>
										<s:if test = 'tagList==null || tagList.size()==0'>
											<p class="mb10">
												<input type="text" name="tag_list" class="validate arrayData" autocomplete="off" />
												<i class="removeTag ml7 fa fa-times hide" title="태그삭제"></i>
												<i class="addTag mt10 ml5 fa fa-plus db" title="태그추가"></i>
											</p>
										</s:if>
										<span class="ml5">(최대 10개 입력)</span>
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
		<jsp:include page="/jsp/product/pop/shopSearch.jsp" flush="false" />
	</body>
</html>