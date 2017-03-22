<%@ page contentType="text/html; charset=EUC-KR" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>

<title>열어본 상품 보기</title>

</head>
<body>
	당신이 열어본 상품을 알고 있다
<br>
<br>

<c:set var='i' value='0'/>
	<c:forEach var="history" items="${history}">
		<a href="/product/getProduct?prodNo=${history}&menu=search"	target="rightFrame">${history}</a><br/>
	</c:forEach>
</body>
</html>