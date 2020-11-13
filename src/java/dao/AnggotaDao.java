/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import connection.Koneksi;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Anggota;

/**
 *
 * @author syamil imdad
 */
public class AnggotaDao {
        private final Connection koneksi;
        private PreparedStatement preSmt;
        private ResultSet rs;
        // tanggal
        private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        public AnggotaDao() {
            koneksi = Koneksi.getConnection();
        }
        
        public ArrayList<Anggota> getAllAnggota(){
            ArrayList<Anggota> listAnggota = new ArrayList<>();
            try{
                String sqlAllKaryawan = "SELECT * FROM anggota ORDER BY noanggota";
                preSmt = koneksi.prepareStatement(sqlAllKaryawan);
                rs = preSmt.executeQuery();
                while(rs.next()){
                        Anggota anggota = new Anggota();
                        anggota.setNoAnggota(rs.getString("noanggota"));
                        anggota.setNama(rs.getString("nama"));
                    
                        if (rs.getString("gender") != null) {
                                anggota.setGender(rs.getString("gender").equals("L") ? "Laki-Laki" : "Perempuan");
                        }
                        else anggota.setGender("");

                        anggota.setTmpLahir(rs.getString("tmplahir") != null ? rs.getString("tmplahir") : "");
                        String tanggal = rs.getDate("tgllahir")            != null ? sdf.format(rs.getDate("tgllahir")) : "";
                        anggota.setTglLahir(tanggal);
                        anggota.setAlamat(rs.getString("alamat")       != null ? rs.getString("alamat") : "");
                        anggota.setTelepon(rs.getString("telepon")    != null ? rs.getString("telepon") : "");

                    listAnggota.add(anggota);
                }
            }
            catch(SQLException e){
                System.out.println("Kesalahan mengambil data : " + e);
            }
            return listAnggota;
        }
        
        public Anggota getRecordByNA(String na){
            Anggota ang = new Anggota();
            String sqlSearch = "select * from anggota where noanggota=?";
            try {
                preSmt = koneksi.prepareStatement(sqlSearch);
                preSmt.setString(1, na);
                rs = preSmt.executeQuery();
                if (rs.next()){
                    ang.setNoAnggota(rs.getString("noanggota"));
                    ang.setNama(rs.getString("nama"));
                    ang.setGender(rs.getString("gender"));
                    ang.setTmpLahir(rs.getString("tmplahir"));
                    ang.setTglLahir(rs.getString("tgllahir"));
                    ang.setAlamat(rs.getString("alamat"));
                    ang.setTelepon(rs.getString("telepon"));
                } else {
                    return null;
                }
            }
            catch (SQLException se){
                System.out.println("error get data by NA : " + se);
            }
            return ang;
        }
//
        public void simpanData(Anggota ang, String page){
            String sqlSimpan = null;
            if (page.equals("edit")){
                sqlSimpan = "update anggota set nama=?, gender=?, tmplahir=?, " +
                        "tgllahir=?, alamat=?, telepon=? where noanggota=?";
            }
            else if (page.equals("tambah")){
                sqlSimpan = "insert into anggota (nama, gender, tmplahir, tgllahir, alamat, telepon, noanggota) " +
                        "values (?,?,?,?,?,?,?)";
            }
            try {
                preSmt = koneksi.prepareStatement(sqlSimpan);
                preSmt.setString(1, ang.getNama());
                if (ang.getGender().equals("")) preSmt.setString(2, null);
                else preSmt.setString(2, ang.getGender());
                preSmt.setString(3, ang.getTmpLahir());
                if (ang.getTglLahir().equals("")) preSmt.setString(4, null);
                else preSmt.setString(4, ang.getTglLahir());
                preSmt.setString(5, ang.getAlamat());
                preSmt.setString(6, ang.getTelepon());
                preSmt.setString(7, ang.getNoAnggota());
                    System.out.println("nama : "+ang.getNama());
                preSmt.executeUpdate();
            }
            catch (SQLException se){
                System.out.println("error add or update : " + se);
            }
        }

        public void hapusData(String na){
            String sqlHapus = "DELETE FROM anggota WHERE noanggota=?";
            try{
                preSmt = koneksi.prepareStatement(sqlHapus);
                preSmt.setString(1, na);
                preSmt.executeUpdate();
            }
            catch(SQLException e){
                System.out.println("error delete data : " + e);
            }
        }
        
        public static void main(String[] args) {
            AnggotaDao anggotaDao = new AnggotaDao();
            System.out.println(anggotaDao.getAllAnggota());
            System.out.println("getRecordAnggota : ");
            if (anggotaDao.getRecordByNA("ooibyyfyy") != null) {
                System.out.println("ooibyyfyy ketemu");
            } else {
                System.out.println("ooibyyfyy tdk ada");
            }
            
        }
}
