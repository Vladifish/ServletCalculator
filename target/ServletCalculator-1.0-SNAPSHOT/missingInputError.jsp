<%-- 
    Document   : missingInputError
    Created on : 10 28, 23, 5:37:46 PM
    Author     : Vlad
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Cancer</h1>
        <%= request.getServletContext().getAttribute("opp")%>
    </body>
</html>
