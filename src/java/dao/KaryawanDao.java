/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import connection.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import model.Karyawan;

/**
 *
 * @author syamil imdad
 */
public class KaryawanDao {
        private final Connection koneksi;
        private PreparedStatement preSmt;
        private ResultSet rs;
        // tanggal
        private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        public KaryawanDao(){
            koneksi = Koneksi.getConnection();
        }

        public ArrayList<Karyawan> getAllKaryawan(){
            ArrayList<Karyawan> listKaryawan = new ArrayList<>();
            try{
                String sqlAllKaryawan = "SELECT * FROM karyawan ORDER BY nik";
                preSmt = koneksi.prepareStatement(sqlAllKaryawan);
                rs = preSmt.executeQuery();
                while(rs.next()){
                    Karyawan karyawan = new Karyawan();
                    karyawan.setNik(rs.getString("nik"));
                    karyawan.setNama(rs.getString("nama"));
                    
                    if (rs.getString("gender") != null) {
                        if (rs.getString("gender").equals("L"))
                            karyawan.setGender("Laki-Laki");
                        else 
                            karyawan.setGender("Perempuan");
                    }
                    else karyawan.setGender("");
                    
                    if (rs.getString("tmplahir") != null) {
                        karyawan.setTmpLahir(rs.getString("tmplahir"));
                    }
                    else karyawan.setTmpLahir("");
                    
                    if(rs.getDate("tgllahir") != null){
                        String tanggal = sdf.format(rs.getDate("tgllahir"));
                        karyawan.setTglLahir(tanggal);
                    }
                    else karyawan.setTglLahir("");
                    
                    if (rs.getString("alamat") != null){
                        karyawan.setAlamat(rs.getString("alamat"));
                    }
                    else karyawan.setAlamat("");
                    
                    if (rs.getString("telepon") != null){
                        karyawan.setTelepon(rs.getString("telepon"));
                    }
                    else karyawan.setTelepon("");
                    
                    listKaryawan.add(karyawan);
                }
            }
            catch(SQLException e){
                System.out.println("Kesalahan mengambil data : " + e);
            }
            return listKaryawan;
        }

        public Karyawan getRecordByNIK(String nik){
            Karyawan kar = new Karyawan();
            String sqlSearch = "select * from karyawan where nik=?";
            try {
                preSmt = koneksi.prepareStatement(sqlSearch);
                preSmt.setString(1, nik);
                rs = preSmt.executeQuery();
                if (rs.next()){
                    kar.setNik(rs.getString("nik"));
                    kar.setNama(rs.getString("nama"));
                    kar.setGender(rs.getString("gender"));
                    kar.setTmpLahir(rs.getString("tmplahir"));
                    kar.setTglLahir(rs.getString("tgllahir"));
                    kar.setAlamat(rs.getString("alamat"));
                    kar.setTelepon(rs.getString("telepon"));
                }
            }
            catch (SQLException se){
                System.out.println("kesalahan pada : " + se);
            }
            return kar;
        }
//
        public void simpanData(Karyawan kar, String page){
            String sqlSimpan = null;
            if (page.equals("edit")){
                sqlSimpan = "update karyawan set nama=?, gender=?, tmplahir=?, " +
                        "tgllahir=?, alamat=?, telepon=? where nik=?";
            }
            else if (page.equals("tambah")){
                sqlSimpan = "insert into karyawan (nama, gender, tmplahir, tgllahir, alamat, telepon, nik) " +
                        "values (?,?,?,?,?,?,?)";
            }
            try {
                preSmt = koneksi.prepareStatement(sqlSimpan);
                preSmt.setString(1, kar.getNama());
                if (kar.getGender().equals("")) preSmt.setString(2, null);
                else preSmt.setString(2, kar.getGender());
                preSmt.setString(3, kar.getTmpLahir());
                if (kar.getTglLahir().equals("")) preSmt.setString(4, null);
                else preSmt.setString(4, kar.getTglLahir());
                preSmt.setString(5, kar.getAlamat());
                preSmt.setString(6, kar.getTelepon());
                preSmt.setString(7, kar.getNik());
                    System.out.println("nama : "+kar.getNama());
                preSmt.executeUpdate();
            }
            catch (SQLException se){
                System.out.println("ada kesalahan : " + se);
            }
        }

        public void hapusData(String nik){
            String sqlHapus = "DELETE FROM karyawan WHERE nik=?";
            try{
                preSmt = koneksi.prepareStatement(sqlHapus);
                preSmt.setString(1, nik);
                preSmt.executeUpdate();
            }
            catch(SQLException e){
                System.out.println("kesalahan hapus data: " + e);
            }
        }


        public static void main(String[] args) {
            KaryawanDao kardao = new KaryawanDao();
            System.out.println(kardao);
            Karyawan kar = new Karyawan();
            kar.setNik("0099923932");
            kar.setNama("Dudung");
            kar.setTglLahir("2001-02-12");
            kar.setAlamat("Batu ampar, Jakarta");
            kar.setTelepon("08128392292");
            kar.setTmpLahir("Madura");
            kar.setGender("L");
            
            kardao.simpanData(kar,"edit");
//              kardao.hapusData("admin");
        }
}
