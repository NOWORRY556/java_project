<%@ page contentType="text/html;charset=UTF-8" %>
<%! private int demoCounter=0; %>
<% demoCounter++; String studentName="ShopSphere Student"; %>
<!doctype html><html><body>
<h1>JSP Elements Demonstration</h1>
<p>Declaration counter: <%= demoCounter %></p>
<p>Scriptlet value: <%= studentName %></p>
<p>Expression Language context path: ${pageContext.request.contextPath}</p>
<%-- JSP comment: traditional syntax is retained only for the RTU syllabus demonstration. --%>
</body></html>
