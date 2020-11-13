/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import dao.KaryawanDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Karyawan;

/**
 *
 * @author syamil imdad
 */
@WebServlet(name = "KaryawanCtr", urlPatterns = {"/KaryawanCtr"})
public class KaryawanCtr extends HttpServlet {

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
        response.setContentType("application/json");
        String page = request.getParameter("page");
        PrintWriter out = response.getWriter();
        KaryawanDao dao = new KaryawanDao();
        Gson gson = new Gson();
        System.out.println(page);
        if (page == null) {
            List<Karyawan> listKaryawan = dao.getAllKaryawan();
            
            String jsonKaryawan = gson.toJson(listKaryawan);
            out.println(jsonKaryawan);
            System.out.println("berhasil get all data : "+jsonKaryawan);
        }
        else if ("tambah".equals(page)){
            String nik = request.getParameter("nik");
            String nama = request.getParameter("nama");
            if (dao.getRecordByNIK(nik).getNik() != null) {
                response.setContentType("text/html;charset=UTF-8");
                out.print("NIK : " + nik + " - " + nama + " sudah terpakai");
            }
            else{
                Karyawan kar = new Karyawan();
                kar.setNik(request.getParameter("nik"));
                kar.setNama(request.getParameter("nama"));
                kar.setGender(request.getParameter("gender"));
                kar.setTmpLahir(request.getParameter("tmpLahir"));
                kar.setTglLahir(request.getParameter("tglLahir"));
                kar.setAlamat(request.getParameter("alamat"));
                kar.setTelepon(request.getParameter("telepon"));

                dao.simpanData(kar, page);

                response.setContentType("text/html;charset=UTF-8");
                out.print("Data Berhasil disimpan");
            }
        }
         else if("tampil".equals(page)){
            String jsonKaryawan = gson.toJson(dao.getRecordByNIK(request.getParameter("nik")));
            response.setContentType("application/json");
            out.println(jsonKaryawan);
        }
        
        else if ("edit".equals(page)) {
            Karyawan kar = new Karyawan();
                kar.setNik(request.getParameter("nik"));
                kar.setNama(request.getParameter("nama"));
                kar.setGender(request.getParameter("gender"));
                kar.setTmpLahir(request.getParameter("tmpLahir"));
                kar.setTglLahir(request.getParameter("tglLahir"));
                kar.setAlamat(request.getParameter("alamat"));
                kar.setTelepon(request.getParameter("telepon"));
                
                dao.simpanData(kar, page);
                
                response.setContentType("text/html;charset=UTF-8");
                out.print("Data Berhasil diupdate");
        }
        else if ("hapus".equals(page)) {
            dao.hapusData(request.getParameter("nik"));
            
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
