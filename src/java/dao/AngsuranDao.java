/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author syamil imdad
 */
import connection.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import model.Angsuran;

public class AngsuranDao {
    
        private final Connection koneksi;
        private PreparedStatement preSmt;
        private ResultSet rs;
        // tanggal
        private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        
        public AngsuranDao() {
            koneksi = Koneksi.getConnection();
        }
        
        public ArrayList<Angsuran> getAllAngsuran() {
            ArrayList<Angsuran> listAngsuran = new ArrayList<>();
            
            try {
                String sql = "SELECT angsuran.*, pinjaman.noanggota, anggota.nama AS namaanggota, karyawan.nama AS namakaryawan "
                                  +"FROM angsuran, pinjaman, anggota, karyawan "
                                  +"WHERE angsuran.nopinjaman=pinjaman.nopinjaman AND "
                                            + "pinjaman.noanggota=anggota.noanggota AND "
                                            + "angsuran.nokaryawan=karyawan.nik";
                preSmt = koneksi.prepareStatement(sql);
                rs = preSmt.executeQuery();
                while(rs.next()){
                    Angsuran angsuran = new Angsuran();
                    
                    angsuran.setNoPinjaman(rs.getString("nopinjaman"));
                    angsuran.setAngsurKe(rs.getInt("angsurke"));
                    String tanggal = rs.getDate("tglangsur") != null ? sdf.format(rs.getDate("tglangsur")) : "";
                    angsuran.setTglAngsur(tanggal);
                    angsuran.setBesarAngsur(rs.getDouble("besarangsur"));
                    angsuran.setSisaPinjaman(rs.getDouble("sisapinjaman"));
                    angsuran.setNoKaryawan(rs.getString("nokaryawan"));
                    angsuran.setNamaKaryawan(rs.getString("namakaryawan"));
                    angsuran.setNoAnggota(rs.getString("noanggota"));
                    angsuran.setNamaAnggota(rs.getString("namaanggota"));
                    
                    listAngsuran.add(angsuran);
                }
                
            } catch (SQLException e) {
                System.out.println("gagal get data angsuran : "+e);
            }
            
            return listAngsuran;
        }
//        SELECT angsuran.*, pinjaman.noanggota, anggota.nama AS namaanggota, karyawan.nama AS namakaryawan, s1.jumlahangsur 
//FROM angsuran, pinjaman, anggota, karyawan, (SELECT COUNT(*) AS jumlahangsur FROM angsuran WHERE nopinjaman="P-12") s1
//WHERE angsuran.nopinjaman=pinjaman.nopinjaman AND pinjaman.noanggota=anggota.noanggota AND angsuran.nokaryawan=karyawan.nik
}
