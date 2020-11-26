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
//            (SELECT pinjaman.lamapinjaman FROM pinjaman WHERE pinjaman.nopinjaman=angsuran.nopinjaman ) AS akhir
            try {
                String sql = "SELECT angsuran.*, pinjaman.noanggota, anggota.nama AS namaanggota, karyawan.nama AS namakaryawan, t2.akhir " +
                                    "FROM angsuran, pinjaman, anggota, karyawan, (SELECT nopinjaman,count(*) AS akhir FROM angsuran GROUP BY nopinjaman ) AS t2 " +
                                    "WHERE angsuran.nopinjaman=pinjaman.nopinjaman AND" +
                                        " pinjaman.noanggota=anggota.noanggota AND" +
                                        " angsuran.nokaryawan=karyawan.nik AND" +
                                        " angsuran.nopinjaman=t2.nopinjaman";
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
                    angsuran.setIsLast( rs.getInt("angsurke") == rs.getInt("akhir") ? true : false );
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
        
        public Angsuran getRecord(String np, int angsurke, String type) {
            System.out.println("======================");
            System.out.println("np input : "+np);
            System.out.println("angsur ke Input : "+angsurke);
            System.out.println("type : "+type);
            System.out.println("======================");
            Angsuran angsuran = new Angsuran();
            
            // jika sudah pernah angsuran
            String sql = "SELECT angsuran.*, pinjaman.noanggota, lamapinjaman, pokokpinjaman, bungapinjaman, angsurpokok, angsurbunga, anggota.nama AS namaanggota, karyawan.nama AS namakaryawan, s1.jumlahangsur "
                    + "FROM angsuran, pinjaman, anggota, karyawan, (SELECT COUNT(*) AS jumlahangsur FROM angsuran WHERE nopinjaman=?) s1 "
                    + "WHERE angsuran.nopinjaman=pinjaman.nopinjaman AND "
                                + "pinjaman.noanggota=anggota.noanggota AND "
                                + "angsuran.nokaryawan=karyawan.nik AND "
                                + "angsuran.nopinjaman=? ";
            
            if (type.equals("add")) {
                sql += " ORDER BY angsurke DESC LIMIT 1";         
            } else if (type.equals("edit")) {
                sql += " AND angsuran.angsurke=?";
            }
            
            try {
                
                preSmt = koneksi.prepareStatement(sql);
                preSmt.setString(1, np);
                preSmt.setString(2, np);
                if (type.equals("edit")) {
                    preSmt.setInt(3, angsurke);
                }
                rs = preSmt.executeQuery();
                
                if (rs.next()) {
                    //  cek jika sudah lunas
                    if (rs.getInt("angsurke") == rs.getInt("lamapinjaman")) {
                        return null;
                    }
                    
                    double besarAngsuran = 0,sisaPinjaman = 0;
                    int angsurKeDb = rs.getInt("angsurke");
                    if (type.equals("add")) {         
                        
                        besarAngsuran = rs.getDouble("angsurpokok") + rs.getDouble("angsurbunga");
                        sisaPinjaman = ( rs.getDouble("pokokpinjaman") + rs.getDouble("bungapinjaman") ) - ( (angsurKeDb+1) * besarAngsuran ) ;
                        if (besarAngsuran > rs.getDouble("sisapinjaman")) {
                            besarAngsuran = rs.getDouble("sisapinjaman");
                            sisaPinjaman = besarAngsuran;
                        }
                        
                        System.out.println("besar angsuran : "+ besarAngsuran);
                        System.out.println("sisa pinjaman now : "+sisaPinjaman);
                        System.out.println("sisa pinjaman before : "+ rs.getDouble("sisapinjaman"));
                    }
                    angsuran.setNoPinjaman(rs.getString("nopinjaman"));
                    angsuran.setAngsurKe(rs.getInt("angsurke"));
                    angsuran.setTglAngsur(sdf.format(rs.getDate("tglangsur")));
                    angsuran.setBesarAngsur( type.equals("add") ? besarAngsuran : rs.getDouble("besarangsur") );
                    angsuran.setSisaPinjaman( type.equals("add") ? sisaPinjaman : rs.getDouble("sisapinjaman") );
                    angsuran.setNoKaryawan(rs.getString("nokaryawan"));
                    angsuran.setNamaKaryawan(rs.getString("namakaryawan"));
                    angsuran.setNoAnggota(rs.getString("noanggota"));
                    angsuran.setNamaAnggota(rs.getString("namaanggota"));
                    angsuran.setJumlahAngsur( type.equals("add") ? rs.getInt("jumlahangsur")+1 : rs.getInt("jumlahangsur") );
                    
                    
                } 
                else {
                    
                    if (type.equals("add")) {
                    // jika pertama kali angsuran
                    String newSql = "SELECT pinjaman.*, (angsurpokok+angsurbunga) AS besarangsur ,( pokokpinjaman+bungapinjaman-(angsurpokok+angsurbunga) ) AS sisapinjaman, anggota.nama AS namaanggota, karyawan.nama AS namakaryawan "
                            + "FROM pinjaman, anggota, karyawan"
                            + " WHERE pinjaman.noanggota=anggota.noanggota AND pinjaman.accpetugas=karyawan.nik AND pinjaman.nopinjaman=?";
                    try {
                        preSmt = koneksi.prepareStatement(newSql);
                        preSmt.setString(1, np);
                        rs = preSmt.executeQuery();
                        if (rs.next()) {
                            angsuran.setNoPinjaman(rs.getString("nopinjaman"));
                            angsuran.setAngsurKe(1);
                            angsuran.setNoKaryawan(rs.getString("accpetugas"));
                            angsuran.setNamaKaryawan(rs.getString("namakaryawan"));
                            angsuran.setNoAnggota(rs.getString("noanggota"));
                            angsuran.setNamaAnggota(rs.getString("namaanggota"));
                            angsuran.setBesarAngsur(rs.getDouble("besarangsur"));
                            angsuran.setJumlahAngsur(1);
                            angsuran.setSisaPinjaman(rs.getDouble("sisapinjaman"));
                        }
                    } catch (SQLException e) {
                        System.out.println("gagal get record1 : "+e);
                    }
                    }
                    
                }
                System.out.println("nopinjaman : "+angsuran.getNoPinjaman());
                System.out.println("noanggota : "+angsuran.getNoAnggota());
                System.out.println("nama anggota : "+angsuran.getNamaAnggota());
                System.out.println("angsurKe : "+angsuran.getAngsurKe());
            } catch (SQLException e) {
                System.out.println("gagal get record2 : "+e);
            }
            
            return angsuran;
        }
        
        public void simpanData(Angsuran angsur, String page){
            String sqlSimpan = null;
            if (page.equals("edit")){
                sqlSimpan = "update angsuran set tglangsur=?, nokaryawan=? where nopinjaman=? and angsurke=?";
                try {
                    preSmt = koneksi.prepareStatement(sqlSimpan);
                    System.out.println("updating data .............");
                    preSmt.setString(1,angsur.getTglAngsur());
                    preSmt.setString(2,angsur.getNoKaryawan());
                    preSmt.setString(3,angsur.getNoPinjaman());
                    preSmt.setInt(4,angsur.getAngsurKe());
                    preSmt.executeUpdate();
                    
                    System.out.println("page : "+page);
                    System.out.println("nopinjaman : "+angsur.getNoPinjaman());
                                  
                }
                catch (SQLException se){
                    System.out.println("error add or update : " + se);
                }
            }
            else if (page.equals("tambah")){
                sqlSimpan = "insert into angsuran (nopinjaman, angsurke, tglangsur, besarangsur, sisapinjaman, nokaryawan) " +
                        "values (?,?,?,?,?,?)";
                System.out.println("adding data .............");
                try {
                    preSmt = koneksi.prepareStatement(sqlSimpan);
                    preSmt.setString(1,angsur.getNoPinjaman());
                    preSmt.setInt(2,angsur.getAngsurKe());
                    preSmt.setString(3,angsur.getTglAngsur());
                    preSmt.setDouble(4,angsur.getBesarAngsur());
                    preSmt.setDouble(5,angsur.getSisaPinjaman());
                    preSmt.setString(6,angsur.getNoKaryawan());
                    preSmt.executeUpdate();
                    
                    System.out.println("page : "+page);
                    System.out.println("nopinjaman : "+angsur.getNoPinjaman());
                    
                }
                catch (SQLException se){
                    System.out.println("error add or update : " + se);
                }
            }
            
        }
        
        public void hapusData(String np, int angsurke){
            String sqlHapus = "DELETE FROM angsuran WHERE nopinjaman=? AND angsurke=?";
            try{
                preSmt = koneksi.prepareStatement(sqlHapus);
                preSmt.setString(1, np);
                preSmt.setInt(2, angsurke);
                preSmt.executeUpdate();
            }
            catch(SQLException e){
                System.out.println("error delete data : " + e);
            }
        }
        
        public static void main(String[] args) {
            AngsuranDao dao = new AngsuranDao();
//            System.out.println(dao.getRecord("P-12", 0, "add").getNoPinjaman());
//            System.out.println(dao.getRecord("P-15", 0, "add"));
//            System.out.println(dao.getRecord("P-12", 0, "add").getBesarAngsur());
            System.out.println("all data angsuran : "+dao.getAllAngsuran());
        }
}
