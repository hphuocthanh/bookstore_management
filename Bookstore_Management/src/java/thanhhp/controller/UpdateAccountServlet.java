/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thanhhp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import thanhhp.registeration.RegistrationCreateError;
import thanhhp.registeration.RegistrationDAO;

/**
 *
 * @author PC
 */
public class UpdateAccountServlet extends HttpServlet {
    private final String ERROR_PAGE = "errors.html";
    private final String SEARCH_PAGE = "SearchLastNameServlet";
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String username =  request.getParameter("txtUsername");
        String password = request.getParameter("txtPassword");
        String role = request.getParameter("chkAdmin");
        boolean isRole = false;
        if (role != null) {
            isRole = true;
        }
        String searchValue = request.getParameter("lastSearchValue");
        String url = ERROR_PAGE;
        RegistrationCreateError errors = new RegistrationCreateError();
        boolean foundErr = false;
        
        try {
            if (password.trim().length() < 6 || password.trim().length() > 20) {
                foundErr = true;
                errors.setPasswordLengthErr("Password is required input from 6 to 20 chars");
            }
            
            RegistrationDAO dao = new RegistrationDAO();
            if (dao.checkLogin(username, password) == 1 && !isRole) {
                foundErr = true;
                errors.setAdminRemoveAdmin("You cannot remove yourself as an admin!");
            }
            
            if (foundErr) {
                request.setAttribute("UPDATE_ERROR", errors);
                url = SEARCH_PAGE;
            } else {
                // call DAO
                 boolean result = dao.updateAccount(username,password,isRole);

                if (result) {
                     url = SEARCH_PAGE;
                } //end if delete action is successful
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (NamingException ex) {
            ex.printStackTrace();
        } finally {
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
            out.close();
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
