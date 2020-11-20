/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import model.Angsuran;
import dao.AngsuranDao;
import java.util.List;
/**
 *
 * @author syamil imdad
 */
@WebServlet(name = "AngsuranCtr", urlPatterns = {"/AngsuranCtr"})
public class AngsuranCtr extends HttpServlet {

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
        
            String page = request.getParameter("page");
            PrintWriter out = response.getWriter();
            AngsuranDao dao = new AngsuranDao();
            Gson gson = new Gson();

             if (page == null) {
                List<Angsuran> list = dao.getAllAngsuran();

                String json = gson.toJson(list);
                out.println(json);
                System.out.println("berhasil get all data angsuran : "+json);
            }
             else if (page.equals("tampil")) {
                 String jsonAngsuran;
                 String type = request.getParameter("type");
                 String np = request.getParameter("np");
                 
                 if (type.equals("add")) {
                        jsonAngsuran = gson.toJson(dao.getRecord(np, 0, "add"));
                 } else {
                        jsonAngsuran = gson.toJson(dao.getRecord(np, Integer.parseInt(request.getParameter("angsurKe")), "edit"));
                 } 
                 response.setContentType("application/json");
                 out.println(jsonAngsuran);
                 System.out.println("np : "+np);
             }
             else if (page.equals("tambah")) {
                 Angsuran angsur = new Angsuran();
                 angsur.setNoPinjaman(request.getParameter("noPinjaman"));
                 angsur.setAngsurKe(Integer.parseInt(request.getParameter("angsurKe")));
                 angsur.setTglAngsur(request.getParameter("tglAngsur"));
                 angsur.setBesarAngsur(Double.valueOf(request.getParameter("besarAngsur")));
                 angsur.setSisaPinjaman(Double.valueOf(request.getParameter("sisaPinjaman")));
                 angsur.setNoKaryawan(request.getParameter("noKaryawan"));
                 System.out.println("taggl angsur : "+request.getParameter("tglAngsur"));
                 dao.simpanData(angsur, page);
                 
                 response.setContentType("text/html;charset=UTF-8");
                 out.print("Data Berhasil disimpan");
             }
             else if (page.equals("edit")) {
                 Angsuran angsur = new Angsuran();
                 angsur.setNoPinjaman(request.getParameter("noPinjaman"));
                 angsur.setAngsurKe(Integer.parseInt(request.getParameter("angsurKe")));
                 angsur.setTglAngsur(request.getParameter("tglAngsur"));
                 angsur.setNoKaryawan(request.getParameter("noKaryawan"));
                 
                 dao.simpanData(angsur, page);
                 
                 response.setContentType("text/html;charset=UTF-8");
                 out.print("Data Berhasil diupdate");
             }
             else if ("hapus".equals(page)) {
                dao.hapusData( request.getParameter("noPinjaman"), Integer.parseInt(request.getParameter("angsurKe")) );

                response.setContentType("text/html;charset=UTF-8");
                    out.print("Data Berhasil dihapus");
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
