<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel='stylesheet' href='styles.css'>
        <title>JSP Page</title>
    </head>
    <body>
        <%@page import="java.util.*"%>
        <h1>
            La Calculatora
        </h1>
        <form action="/ServletCalculator/CalculatorServlet" method="post">
            <% 
                Cookie[] cookies = request.getCookies(); 
                HashMap<String, String> cookieMap = new HashMap<String, String>();
                if (cookies != null) {
                    for (Cookie c : cookies) {
                        String name = c.getName();
                        if (name.contains("history")) 
                            cookieMap.put(name, c.getValue());
                }
            }
            %>
            <span>
            <label for="firstVal">Input 1st Number:</label> <input type="string" name="firstVal">
            <select name='history1'>
            <option selected='selected'>History</option> <!-- comment -->
                <%
                     // should stop if the succeding cookie is not found
                     for (int i=0; i<5; i++) {
                        String cookieValue = null;
                        if (cookieMap.containsKey("history"+i)) {
                            cookieValue = cookieMap.get("history"+i);
                        }
                        else {
                            break;
                        }

                %>
                    <option value=<%= cookieValue %>> 
                        <%=cookieValue%>
                    </option>
                <%}%>
            </select>
            </span>
            <span>
            <label for="firstVal">Input 2nd Number:</label> <input type="string" name="firstVal">
            <select name='history2'>
                <option selected='selected'>History</option> <!-- comment -->
                <%
                 // should stop if the succeding cookie is not found
                 for (int i=0; i<5; i++) {
                    String cookieValue = null;
                    if (cookieMap.containsKey("history"+i)) {
                        cookieValue = cookieMap.get("history"+i);
                    }
                    else {
                        break;
                    }
                        
                %>
                <option value=<%= cookieValue %>> 
                    <%=cookieValue%>
                </option>
                <%}%>
            </select>
            </span>
            <label>Operator</label> 
            <span>
            <input type='radio' name='opp' value='add'>Add
            <input type='radio' name='opp' value='sub'>Subtract
            <input type='radio' name='opp' value='mul'>Multiply
            <input type='radio' name='opp' value='div'>Divide
            </span>
            <input type='submit' value='=' id='submit'>
            
        </form>
        
    </body>
</html>
