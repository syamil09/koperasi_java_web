/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author syamil imdad
 */
public class Angsuran {
    private String noPinjaman;
    private int angsurKe;
    private String tglAngsur;
    private double besarAngsur;
    private double sisaPinjaman;
    private String noKaryawan;
    private String namaKaryawan;
    private String noAnggota;
    private String namaAnggota;
    private int jumlahAngsur;
    private boolean isLast;

    public boolean isIsLast() {
        return isLast;
    }

    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }
    
    public int getJumlahAngsur() {
        return jumlahAngsur;
    }

    public void setJumlahAngsur(int jumlahAngsur) {
        this.jumlahAngsur = jumlahAngsur;
    }
    
    public String getNamaKaryawan() {
        return namaKaryawan;
    }

    public void setNamaKaryawan(String namaKaryawan) {
        this.namaKaryawan = namaKaryawan;
    }

    public String getNoAnggota() {
        return noAnggota;
    }

    public void setNoAnggota(String noAnggota) {
        this.noAnggota = noAnggota;
    }

    public String getNamaAnggota() {
        return namaAnggota;
    }

    public void setNamaAnggota(String namaAnggota) {
        this.namaAnggota = namaAnggota;
    }

    public String getNoPinjaman() {
        return noPinjaman;
    }

    public void setNoPinjaman(String noPinjaman) {
        this.noPinjaman = noPinjaman;
    }

    public int getAngsurKe() {
        return angsurKe;
    }

    public void setAngsurKe(int angsurKe) {
        this.angsurKe = angsurKe;
    }

    public String getTglAngsur() {
        return tglAngsur;
    }

    public void setTglAngsur(String tglAngsur) {
        this.tglAngsur = tglAngsur;
    }

    public double getBesarAngsur() {
        return besarAngsur;
    }

    public void setBesarAngsur(double besarAngsur) {
        this.besarAngsur = besarAngsur;
    }

    public double getSisaPinjaman() {
        return sisaPinjaman;
    }

    public void setSisaPinjaman(double sisaPinjaman) {
        this.sisaPinjaman = sisaPinjaman;
    }

    public String getNoKaryawan() {
        return noKaryawan;
    }

    public void setNoKaryawan(String noKaryawan) {
        this.noKaryawan = noKaryawan;
    }
    
}
