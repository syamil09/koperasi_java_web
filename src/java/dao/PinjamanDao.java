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
import model.Pinjaman;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
/**
 *
 * @author syamil imdad
 */
public class PinjamanDao {
        private final Connection koneksi;
        private PreparedStatement preSmt;
        private ResultSet rs;
        // tanggal
        private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        
        public PinjamanDao() {
            koneksi = Koneksi.getConnection();
        }
        
        public String formatRp(Double u) {       
//                DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
//                DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
//
//                formatRp.setCurrencySymbol("Rp. ");
//                formatRp.setMonetaryDecimalSeparator(',');
//                formatRp.setGroupingSeparator('.');
//
//                kursIndonesia.setDecimalFormatSymbols(formatRp);
//                return kursIndonesia.format(u);
                String rp = String.format("Rp. %,.0f", u).replaceAll(",", ".");
                return rp;
        }
        
        public ArrayList<Pinjaman> getAllPinjaman(){
                ArrayList<Pinjaman> listPinjaman = new ArrayList<>();
                try{
                    String sql = "SELECT pinjaman.*,anggota.nama AS namaanggota, karyawan.nama AS namakaryawan FROM `pinjaman`,`anggota`,`karyawan`"
                            + " WHERE anggota.noanggota=pinjaman.noanggota AND karyawan.nik=pinjaman.accpetugas";
                    preSmt = koneksi.prepareStatement(sql);
                    rs = preSmt.executeQuery();
                    while(rs.next()){
                            Pinjaman pj = new Pinjaman();
                            pj.setNoAnggota(rs.getString("noanggota"));
                            pj.setNoPinjaman(rs.getString("nopinjaman"));
     
                            String tanggal = rs.getDate("tglpinjaman") != null ? sdf.format(rs.getDate("tglpinjaman")) : "";
                            pj.setTglPinjaman(tanggal);
                            pj.setPokokPinjaman(rs.getDouble("pokokpinjaman"));
                            pj.setPokokPinjamanRp(formatRp(rs.getDouble("pokokpinjaman")));
                            pj.setBungaPinjaman(rs.getDouble("bungapinjaman"));
                            pj.setBungaPinjamanRp(formatRp(rs.getDouble("bungapinjaman")));
                            pj.setLamaPinjaman(rs.getInt("lamapinjaman"));
                            pj.setAngsurPokok(rs.getDouble("angsurpokok"));
                            pj.setAngsurPokokRp(formatRp(rs.getDouble("angsurpokok")));
                            pj.setAngsurBunga(rs.getDouble("angsurbunga"));
                            pj.setAngsurBungaRp(formatRp(rs.getDouble("angsurbunga")));
                            pj.setAccPetugas(rs.getString("accPetugas"));
                            pj.setNamaAnggota(rs.getString("namaanggota"));
                            pj.setNamaKaryawan(rs.getString("namakaryawan"));
                            listPinjaman.add(pj);
                    }
                }
                catch(SQLException e){
                    System.out.println("Kesalahan mengambil data Pinjaman : " + e);
                }
                return listPinjaman;
        }
        
        public Pinjaman getRecordByNP(String np){
            Pinjaman pj = new Pinjaman();
            String sqlSearch = "SELECT pinjaman.*,anggota.nama AS namaanggota, karyawan.nama AS namakaryawan FROM `pinjaman`,`anggota`,`karyawan`"
                    + " WHERE anggota.noanggota=pinjaman.noanggota AND karyawan.nik=pinjaman.accpetugas AND pinjaman.nopinjaman=?";
            try {
                preSmt = koneksi.prepareStatement(sqlSearch);
                preSmt.setString(1, np);
                rs = preSmt.executeQuery();
                if (rs.next()){
                        String tanggal = rs.getDate("tglpinjaman") != null ? sdf.format(rs.getDate("tglpinjaman")) : "";
                        pj.setTglPinjaman(tanggal);
                        pj.setPokokPinjaman(rs.getDouble("pokokpinjaman"));
                        pj.setBungaPinjaman(rs.getDouble("bungapinjaman"));
                        pj.setLamaPinjaman(rs.getInt("lamapinjaman"));
                        pj.setAngsurPokok(rs.getDouble("angsurpokok"));
                        pj.setAngsurBunga(rs.getDouble("angsurbunga"));
                        pj.setAccPetugas(rs.getString("accPetugas"));
                        pj.setNamaAnggota(rs.getString("namaanggota"));
                        pj.setNamaKaryawan(rs.getString("namakaryawan"));
                        pj.setNoPinjaman(rs.getString("nopinjaman"));
                        pj.setNoAnggota(rs.getString("noanggota"));
                        pj.setAccPetugas(rs.getString("accpetugas"));
                }else {
                    return null;
                }
            }
            catch (SQLException se){
                System.out.println("error get data by NP : " + se);
            }
            return pj;
        }
//
        public void simpanData(Pinjaman pin, String page){
            String sqlSimpan = null;
            if (page.equals("edit")){
                sqlSimpan = "update pinjaman set noanggota=?, tglpinjaman=?, pokokpinjaman=?, " +
                        "bungapinjaman=?, lamapinjaman=?, angsurpokok=? , angsurbunga=?, accpetugas=? where nopinjaman=?";
            }
            else if (page.equals("tambah")){
                sqlSimpan = "insert into pinjaman (noanggota, tglpinjaman, pokokpinjaman, bungapinjaman, lamapinjaman, angsurpokok, angsurbunga, accpetugas, nopinjaman) " +
                        "values (?,?,?,?,?,?,?,?,?)";
                System.out.println("adding data .............");
            }
            try {
                preSmt = koneksi.prepareStatement(sqlSimpan);
                preSmt.setString(1,pin.getNoAnggota());
                preSmt.setString(2, pin.getTglPinjaman());
                preSmt.setDouble(3, pin.getPokokPinjaman());
                preSmt.setDouble(4, pin.getBungaPinjaman());
                preSmt.setInt(5, pin.getLamaPinjaman());
                preSmt.setDouble(6, pin.getAngsurPokok());
                preSmt.setDouble(7, pin.getAngsurBunga());
                preSmt.setString(8, pin.getAccPetugas());
                preSmt.setString(9, pin.getNoPinjaman());
                System.out.println("page : "+page);
                System.out.println("nopinjaman : "+pin.getNoPinjaman());
                preSmt.executeUpdate();
            }
            catch (SQLException se){
                System.out.println("error add or update : " + se);
            }
        }

        public void hapusData(String na){
            String sqlHapus = "DELETE FROM pinjaman WHERE nopinjaman=?";
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
            PinjamanDao pj = new PinjamanDao();
            System.out.println(pj.getAllPinjaman());
            System.out.println("getREcordByNP : ");
            System.out.println(pj.getRecordByNP("P-110"));
        }
}
