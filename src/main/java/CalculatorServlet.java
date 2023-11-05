/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
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
            
            double num1 = Double.parseDouble(request.getParameter("firstVal")); // TODO: handle errors late
            double num2 = Double.parseDouble(request.getParameter("secondVal"));
            String opp = request.getParameter("opp"); // can be null, so handle this later
            getServletContext().setAttribute("opp", "hi");
            double result = evaluate(num1, opp, num2); // can throw arithmetic exception, div by 0
            
            // every successful computation should land here, no more exceptionss
            
            // the history would be a stack (kinda)
            // newest on top
            // if full, the oldest would be removed
            history_count = Math.min(history_count+1, MAX_HISTORY);
            Cookie[] calcHistory = new Cookie[history_count];
        
            // should be skipped if the history is previously empty
            if (history_count > 1) {
                // will store previous cookies, with one blank space
                findCalcCookies(request, calcHistory); 
                // slow operation, so I just perform it once
                // I could probably use an actual queue to make the operation faster lol
                for (int i=history_count-1, j=i-1; i>0; i--, j--) {
                    calcHistory[i] = new Cookie("history"+i, calcHistory[j].getValue());
                }
            }
            calcHistory[0] = new Cookie("history0", result+"");
            
            for (int i=0; i<history_count; i++)
                response.addCookie(calcHistory[i]);
            
            request.getRequestDispatcher("/Calculator.jsp").forward(request, response);
            
    }
    
    private void findCalcCookies(HttpServletRequest request, Cookie[] calcHistory) {
        Cookie[] cookies = request.getCookies();
        int i=0;
        for (Cookie c : cookies) {
            if(c.getValue().equals("history" + i))
                calcHistory[i++] = c;
            
        }
    }
    
    protected Cookie[] updateHistory(double recent_result) {
        Cookie[] calcHistory = new Cookie[history_count];
        
        // should be skipped if the history is previously empty
        for (int i=history_count-1; i>0; i--) {
            
        }
        calcHistory[0] = new Cookie("history0", recent_result+""); 
        return calcHistory;
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
