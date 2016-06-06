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
			});
		</script>
	</head>
	<body>
		<jsp:include page="/jsp/include/gnb.jsp" flush="false" />
		<div class="admin_container">
			<jsp:include page="/jsp/include/lnb.jsp" flush="false" />
			<div class="adminContentArea">
				<h2 class="mb20">상품리스트</h2>
				<div class="contentBody">
					<input type="button" id="productWriteBtn" class="writeBtn simpleBtn mb10 btn2 clear fr" value="상품등록" />
					<table class="table_list tc">
						<colgroup>
							<col width="70px"><col width="70px"><col width="70px"><col width="70px"><col width="70px">
						</colgroup>
						<thead>
							<tr>
								<th scope="col">번호</th>
								<th scope="col">이미지</th>
								<th scope="col">상품명</th>
								<th scope="col">가격</th>
								<th scope="col">등록일</th>
							</tr>
						</thead>
						<tbody>
							<!-- 
							<tr>
								<td colspan="5" align="center">
									등록된 글이 없습니다.
								</td>
							</tr>
							 -->
							<tr>
								<td class="center">5</td>
								<td class="center">
									<img src="/temp/product1.png" />
								</td>
								<td class="center">페이크 레더 점퍼</td>
								<td class="center">￦39,000</td>
								<td class="center">2016.04.06</td>
							</tr>
							<tr>
								<td class="center">4</td>
								<td class="center">
									<img src="/temp/product1.png" />
								</td>
								<td class="center">페이크 레더 점퍼</td>
								<td class="center">￦39,000</td>
								<td class="center">2016.04.06</td>
							</tr>
							<tr>
								<td class="center">3</td>
								<td class="center">
									<img src="/temp/product1.png" />
								</td>
								<td class="center">페이크 레더 점퍼</td>
								<td class="center">￦39,000</td>
								<td class="center">2016.04.06</td>
							</tr>
							<tr>
								<td class="center">2</td>
								<td class="center">
									<img src="/temp/product1.png" />
								</td>
								<td class="center">페이크 레더 점퍼</td>
								<td class="center">￦39,000</td>
								<td class="center">2016.04.06</td>
							</tr>
							<tr>
								<td class="center">1</td>
								<td class="center">
									<img src="/temp/product1.png" />
								</td>
								<td class="center">페이크 레더 점퍼</td>
								<td class="center">￦39,000</td>
								<td class="center">2016.04.06</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</body>
</html>