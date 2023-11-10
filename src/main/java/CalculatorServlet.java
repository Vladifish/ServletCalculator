/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

/**
 *
 * @author Vlad
 */
public class CalculatorServlet extends HttpServlet {
    
    private int history_count = 0;
    private final int MAX_HISTORY = 5; 

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            double num1, num2;
            String history1 = request.getParameter("history1");
            String history2 = request.getParameter("history2");
            
            // program explodes if we use parseException
            if (history1 == null)
                num1 = Double.parseDouble(request.getParameter("firstVal"));
            else 
                num1 = Double.parseDouble(history1);

            if (history2 == null)
                num2 = Double.parseDouble(request.getParameter("secondVal")); 
            else 
                num2 = Double.parseDouble(history2);
            
           
            String opp = request.getParameter("opp"); // can be null, so handle this later
            
            double result = evaluate(num1, opp, num2); // can throw arithmetic exception, div by 0
            
            
            // the history would be a stack (kinda)
            // newest on top
            // if full, the oldest would be removed
            history_count = Math.min(history_count+1, MAX_HISTORY);
            Cookie[] calcHistory = new Cookie[history_count];
            HashMap<String, String> cookieMap = findCalcCookies(request);
            
            // should be skipped if the history is previously empty
            // moves the previous values upwards
            for (int i=history_count-1; i>0; i--) {
                String val = cookieMap.get("history" + (i-1));
                calcHistory[i] = new Cookie("history" + (i), val);
            }
            
            String truncated_res = String.format("%.2f", result);
            calcHistory[0] = new Cookie("history0", truncated_res);
            
            for (int i=0; i<history_count; i++)
                response.addCookie(calcHistory[i]);
            
            String storedCalculation = String.format("%.2f %c %.2f = %s",num1, readOperationSymbol(opp), num2, truncated_res);
            getServletContext().setAttribute("result", storedCalculation);
            
            response.sendRedirect("/ServletCalculator/Calculator.jsp");
            // this was causing all of my headaches oml
            //request.getRequestDispatcher("/Calculator.jsp").forward(request, response);
            
    }
    
    private void linkValueError() throws ParseException {
        throw new java.text.ParseException("error", MAX_HISTORY);
    }
    
    private HashMap<String,String> findCalcCookies(HttpServletRequest request) {
            HashMap<String, String> cookieMap = new HashMap<>();
        
            for (Cookie c : request.getCookies()) {
                String name = c.getName();
                if (name.contains("history")) 
                    cookieMap.put(name, c.getValue());
            }
            return cookieMap;
    }
    
    private double evaluate(double num1, String opp, double num2) {
        switch (opp) {
            case "add" :
                return num1 + num2;
            case "mul" :
                return num1 * num2;
            case "sub" :
                return num1 - num2;
            case "div" :
                if (num2 == 0)
                    throw new ArithmeticException();
                return num1 / num2;
            default : 
                // should be unreachable since the other parts are checked 
                // before the function call
                throw new NullPointerException();
        }
    }
    
    // similar to the evaluate function but simply used to provide 
    // a symbol for the result output
    private static char readOperationSymbol(String opp) {
        switch (opp) {
            case "add" :
                return '+';
            case "mul" :
                return '*';
            case "sub" :
                return '-';
            case "div" :
                return '/';
            default : 
                // should be unreachable since the other parts are checked 
                // before the function call
                throw new NullPointerException();
        }
    }
    
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
