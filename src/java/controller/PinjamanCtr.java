/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import dao.PinjamanDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Pinjaman;

/**
 *
 * @author syamil imdad
 */
@WebServlet(name = "PinjamanCtr", urlPatterns = {"/PinjamanCtr"})
public class PinjamanCtr extends HttpServlet {

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
        PinjamanDao dao = new PinjamanDao();
        Gson gson = new Gson();
        
         if (page == null) {
            List<Pinjaman> list = dao.getAllPinjaman();
            
            String json = gson.toJson(list);
            out.println(json);
            System.out.println("berhasil get all data : "+json);
        }
         else if ("tambah".equals(page)){
            String np = request.getParameter("noPinjaman");
            String nama = request.getParameter("nama");
            if (dao.getRecordByNP(np) != null) {
                response.setContentType("text/html;charset=UTF-8");
                out.print("NO. Pinjaman : " + np + " sudah terpakai");
            }
            else{
                System.out.println("No pinjam ctr: "+np);
                System.out.println("angsur pokok : "+request.getParameter("angsurPokok"));
                Pinjaman kar = new Pinjaman();
                kar.setNoPinjaman(np);
                kar.setNoAnggota(request.getParameter("noAnggota"));
                kar.setPokokPinjaman(Double.valueOf(request.getParameter("pokokPinjaman")));
                kar.setTglPinjaman(request.getParameter("tglPinjaman"));
                kar.setBungaPinjaman(Double.valueOf(request.getParameter("bungaPinjaman")));
                kar.setLamaPinjaman(Integer.parseInt(request.getParameter("lamaPinjaman")));
                kar.setAngsurPokok(Double.valueOf(request.getParameter("angsurPokok")));
                kar.setAngsurBunga(Double.valueOf(request.getParameter("angsurBunga")));
                kar.setAccPetugas(request.getParameter("nikKaryawan"));
                
                dao.simpanData(kar, page);
                
                response.setContentType("text/html;charset=UTF-8");
                out.print("Data Berhasil disimpan");
            }
        }
         else if("tampil".equals(page)){
            String jsonKaryawan = gson.toJson(dao.getRecordByNP(request.getParameter("noPinjaman")));
            response.setContentType("application/json");
            out.println(jsonKaryawan);
        }
        
        else if ("edit".equals(page)) {
            Pinjaman kar = new Pinjaman();
                kar.setNoPinjaman(request.getParameter("noPinjaman"));
                kar.setNoAnggota(request.getParameter("noAnggota"));
                kar.setPokokPinjaman(Integer.parseInt(request.getParameter("pokokPinjaman")));
                kar.setTglPinjaman(request.getParameter("tglPinjaman"));
                kar.setBungaPinjaman(Integer.parseInt(request.getParameter("bungaPinjaman")));
                kar.setLamaPinjaman(Integer.parseInt(request.getParameter("lamaPinjaman")));
                kar.setAngsurPokok(Integer.parseInt(request.getParameter("angsurPokok")));
                kar.setAngsurBunga(Integer.parseInt(request.getParameter("angsurBunga")));
                kar.setAccPetugas(request.getParameter("nikKaryawan"));
                
                dao.simpanData(kar, page);
                
                response.setContentType("text/html;charset=UTF-8");
                out.print("Data Berhasil diupdate");
        }
        else if ("hapus".equals(page)) {
            dao.hapusData(request.getParameter("noPinjaman"));
            
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
