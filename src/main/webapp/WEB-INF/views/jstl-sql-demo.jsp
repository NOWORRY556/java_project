<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sql" uri="jakarta.tags.sql" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
Object dataSource=null;
try { dataSource=new javax.naming.InitialContext().lookup("java:comp/env/jdbc/ShopSphereDB"); } catch(Exception ignored) {}
request.setAttribute("demoDataSource",dataSource);
%>
<h1>JSTL SQL Demonstration</h1>
<c:choose>
<c:when test="${not empty demoDataSource}">
<sql:query var="rows" dataSource="${demoDataSource}" sql="SELECT product_id,name,price FROM products ORDER BY product_id LIMIT 5"/>
<c:forEach var="row" items="${rows.rows}"><p>${row.product_id} — ${row.name} — ₹${row.price}</p></c:forEach>
</c:when>
<c:otherwise><p>Configure the ShopSphereDB JNDI datasource to run this syllabus demo.</p></c:otherwise>
</c:choose>
