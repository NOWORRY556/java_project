<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="shop" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/fragments/header.jsp" %>
<main>
<h2>Fragments, Tag Files and JSTL</h2>
<shop:productCard name="Java Programming" price="799"/>
<c:set var="available" value="true"/>
<c:if test="${available}"><p>JSTL c:if is active.</p></c:if>
<c:forEach var="item" items="${['JDBC','Servlet','JSP']}"><span>${item} </span></c:forEach>
</main>
<%@ include file="/fragments/footer.jsp" %>
