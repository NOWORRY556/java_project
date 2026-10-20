<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="x" uri="jakarta.tags.xml" %>
<%
String xml="<products><product><name>Java Programming</name><price>799</price></product><product><name>Wireless Mouse</name><price>599</price></product></products>";
request.setAttribute("productXml",xml);
%>
<x:parse var="doc" xml="${productXml}"/>
<h1>JSTL XML Demonstration</h1>
<x:forEach select="$doc/products/product">
<p><x:out select="name"/> — ₹<x:out select="price"/></p>
</x:forEach>
