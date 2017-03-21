<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    
<html>
<head>
<title>상품 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
	// 검색 / page 두가지 경우 모두 Form 전송을 위해 JavaScrpt 이용  
	function fncGetUserList(currentPage) {
		document.getElementById("currentPage").value = currentPage;
	   	document.detailForm.submit();		
	}
	
	
</script>

</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width:98%; margin-left:10px;">

<form name="detailForm" action="/listProduct.do?menu=${menu}" method="post">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
						
	<tr>
		<td width="15" height="37">
			<img src="/images/ct_ttl_img01.gif" width="15" height="37"/>
		</td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
				<td width="93%" class="ct_ttl01">${!empty menu && menu=="search" ? "상품목록조회" : "상품관리"}</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37">
			<img src="/images/ct_ttl_img03.gif" width="12" height="37"/>
		</td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="right">가격검색
			<input 	type="text" id="startPrice" name="startPrice" value="${!empty search.startPrice? search.startPrice : ""}"
							class="ct_input_g" style="height:20px;width:60px;" />~
			<input 	type="text" id="endPrice" name="endPrice" value="${!empty search.endPrice? search.endPrice : ""}"
							class="ct_input_g" style="height:20px;width:60px;" />
		</td>
		<td align="right" width="70">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="17" height="23">
						<img src="/images/ct_btnbg01.gif" width="17" height="23">
					</td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
						<a href="javascript:fncGetUserList('${resultPage.currentPage}');">검색</a>
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif" width="14" height="23">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="right">
			<select name="searchCondition" class="ct_input_g" style="width:80px">
				<option value="1" ${search.searchCondition=="1"? "selected" : ""}>상품명</option>
				<option value="2" ${search.searchCondition=="2"? "selected" : ""}>상품가격</option>
			</select>
			<input 	type="text" name="searchKeyword"  value="${!empty search.searchKeyword? search.searchKeyword : ""}" 
							class="ct_input_g" style="width:200px; height:19px" >
		</td>
		<td align="right" width="70">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="17" height="23">
						<img src="/images/ct_btnbg01.gif" width="17" height="23">
					</td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
						<a href="javascript:fncGetUserList('${resultPage.currentPage}');">검색</a>
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif" width="14" height="23">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">

	<tr>
		<td colspan="11" >전체 ${resultPage.totalCount} 건수, 현재 ${resultPage.currentPage} 페이지</td>
	</tr>
	<tr>
		
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">상품명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">가격
		<a href="/listProduct.do?orderByPrice=1&menu=${menu}">(▲)</a>
		<a href="/listProduct.do?orderByPrice=2&menu=${menu}">(▼)</a>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="300">상품정보</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">등록날짜</td>	
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">현재상태</td>	
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="100">찜한사람 수</td>
		<td class="ct_line02"></td>
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>

	<c:set var='i' value='0'/>
	<c:forEach var="product" items="${list}">
		<c:set var="i" value="${i+1}"/>
		<tr class="ct_list_pop">
			<td align="center">${i}</td>
		<td></td>
			<td align="center">
				<a href="/getProduct.do?prodNo=${product.prodNo}&menu=${menu}&proTranCode=${product.proTranCode}">${product.prodName}</a>
			</td>
		<td></td>
		<td align="center">${product.price}</td>
		<td></td>
		<td align="center">${product.prodDetail}</td>
		<td></td>
		<td align="center">${product.regDate}</td>
		<td></td>
			<c:set var="tranCode" value="판매중"/>
			<c:if test="${role=='admin'}">
				<c:if test="${product.proTranCode.trim()=='1'}">
					<c:set var="tranCode" value="구매완료"/>
				</c:if>
				<c:if test="${product.proTranCode.trim()=='2'}">
					<c:set var="tranCode" value="배송중"/>
				</c:if>
				<c:if test="${product.proTranCode.trim()=='3'}">
					<c:set var="tranCode" value="배송완료"/>
				</c:if>
			</c:if>
			<c:if test="${role=='user'}">
				<c:if test="${product.proTranCode.trim()!='0'}">
					<c:set var="tranCode" value="재고없음"/>
				</c:if>
			</c:if>
			<td align="center">${tranCode}
			<c:if test="${!empty menu && menu=='manage' && product.proTranCode.trim()=='1'}">
				<a href="/updateTranCodeByProd.do?prodNo=${product.prodNo}&proTranCode=${product.proTranCode}">배송하기</a>
			</c:if>
			</td>
			<td></td>
			<td align="center">${product.countLiked}</td>
			<td></td>
		</tr>
	</c:forEach>
	
	<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>
	
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr><td align="center">
		   <input type="hidden" id="currentPage" name="currentPage" value=""/>
		   <input type="hidden" id="orderByPrice" name="orderByPrice" value="${!empty search.orderByPrice? search.orderByPrice:''}"/>
		  
				
				
				<jsp:include page="../common/pageNavigator.jsp"/>	
    	</td>
	</tr>
</table>
<!--  페이지 Navigator 끝 -->

</form>

</div>
</body>
</html>
