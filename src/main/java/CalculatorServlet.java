/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            
            num1 = Double.parseDouble(request.getParameter("firstVal")); // TODO: handle errors late
            num2 = Double.parseDouble(request.getParameter("secondVal")); // throws numberformat exception on empty string
            String opp = request.getParameter("opp"); // can be null, so handle this later
            
            double result = evaluate(num1, opp, num2); // can throw arithmetic exception, div by 0
            // double result = 1;
            // every successful computation should land here, no more exceptions
            
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
            calcHistory[0] = new Cookie("history0", result+"");
            
            for (int i=0; i<history_count; i++)
                response.addCookie(calcHistory[i]);
            
            getServletContext().setAttribute("result", result);
            
            request.getRequestDispatcher("/Calculator.jsp").forward(request, response);
            
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
        processRequest(request, response);
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
