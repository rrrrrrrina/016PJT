<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<title>구매 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
	function fncGetUserList(currentPage) {
		document.getElementById("currentPage").value = currentPage;
		document.detailForm.submit();
	}
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width: 98%; margin-left: 10px;">

<form name="detailForm" action="/listPurchase.do" method="post">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"width="15" height="37"></td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left: 10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">${user.userId} 님의 구매 목록조회.</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37"><img src="/images/ct_ttl_img03.gif"	width="12" height="37"></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0"	style="margin-top: 10px;">
	<tr>
		<td colspan="11">전체${resultPage.totalCount} 건수, 현재 ${resultPage.currentPage} 페이지</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="200">상품명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">받는분성함</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">&nbsp;&nbsp;&nbsp;주문날짜 
		<a href="/listPurchase.do?searchCondition=0">(최근순▼)</a>
		<a href="/listPurchase.do?searchCondition=1">(오래된순▲)</a></td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">배송현황</td>
		<td class="ct_line02"></td>
		
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
	<c:set var='i' value='0'/>
	<c:forEach var="purchase" items="${list}">
	<c:set var="i" value="${i+1}"/>
	<tr class="ct_list_pop">
		<td align="center">
			${i}
		</td>
		<td></td>
		<td align="center">
			<a href="/getPurchase.do?tranNo=${purchase.tranNo}">${purchase.purchaseProd.prodName}</a>
		</td>
		<td></td>
		<td align="center">${purchase.receiverName}</td>
		<td></td>
		<td align="center">${purchase.orderDate}</td>
		<td></td>
		<td align="center">현재
		<c:set var="tranCode" value="구매완료"/>
		<c:if test="${purchase.tranCode.trim()=='2'}">
			<c:set var="tranCode" value="배송중"/>
		</c:if>
		<c:if test="${purchase.tranCode.trim()=='3'}">
			<c:set var="tranCode" value="배송완료"/>
		</c:if>
					${tranCode}
				상태 입니다.
			<c:if test="${purchase.tranCode.trim()=='2'}">
				<a href="/updateTranCode.do?tranNo=${purchase.tranNo}&tranCode=${purchase.tranCode}">물건도착</a>
			</c:if>
			<c:if test="${purchase.tranCode.trim()=='1'}">
				<a href="/getPurchase.do?tranNo=${purchase.tranNo}">정보수정</a>
			</c:if>
		</td>
	</tr>
		</c:forEach>
	<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 10px;">
	<tr>
		<td align="center">
		<input type="hidden" id="currentPage" name="currentPage" value=""/>
		<input type="hidden" id="searchCondition" name="searchCondition" value="${! empty search.searchCondition ? search.searchCondition : ""}"/>
			<jsp:include page="../common/pageNavigator.jsp"/>
		</td>
	</tr>
</table>

<!--  페이지 Navigator 끝 -->
</form>

</div>

</body>
</html>