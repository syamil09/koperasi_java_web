/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.howtodoinjava.hashing.password.demo.bcrypt.BCrypt;
import java.util.ArrayList;
import model.User;
import connection.Koneksi;
import java.sql.SQLException;
/**
 *
 * @author syamil imdad
 */
public class UserDao {
        private final Connection koneksi;
        private PreparedStatement preSmt;
        private ResultSet rs;
        
        public UserDao() {
            koneksi = Koneksi.getConnection();
        }
        
        public ArrayList<User> getAlluser() {
            ArrayList<User> listUser = new ArrayList<>();
            try {
                String sql = "SELECT * FROM user";
                preSmt = koneksi.prepareStatement(sql);
                rs = preSmt.executeQuery();
                
                while (rs.next()) {
                    User usr = new User();
                    usr.setUserId(rs.getString("userid"));
                    usr.setPassword(rs.getString("password"));
                    usr.setLevel(rs.getString("level"));
                    usr.setNik(rs.getString("nik"));
                    usr.setAktif(rs.getString("aktif"));
                    listUser.add(usr);
                    
                    System.out.println("userid : "+rs.getString("userid"));
                }
                
            } catch (SQLException e ) {
                System.out.println("gagal get data user : "+e);
            }
            
            return listUser;
        }
        
        public User getRecordById(String userid) {
            User usr = new User();
            String sql = "SELECT u.*, k.nama FROM user u, karyawan k WHERE u.nik=k.nik AND u.userid=?";
            
            try {
                preSmt = koneksi.prepareStatement(sql);
                preSmt.setString(1, userid);
                rs = preSmt.executeQuery();
                
                if (rs.next()) {
                        usr.setUserId(userid);
                        usr.setAktif(rs.getString("aktif"));
                        usr.setLevel(rs.getString("level"));
                        usr.setNik(rs.getString("nik"));
                        usr.setNama(rs.getString("nama"));
                }
                
            } catch(SQLException e) {
                System.out.println("error get data user by id : "+e);
            }
            
            return usr;
        }
        
        public String getPasswordHash() {
            String  originalPassword = "password";
            String generatedSecuredPasswordHash = BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
            System.out.println(generatedSecuredPasswordHash);
            
            boolean matched = BCrypt.checkpw(originalPassword, generatedSecuredPasswordHash);
            System.out.println(matched);
            return generatedSecuredPasswordHash;
        }
        
        public String login(String userid, String password) {
            User usr = new User();
            try {
                String sql = "select u.*, k.nama from user u, karyawan k where u.nik=k.nik and userid=? ";
                preSmt = koneksi.prepareStatement(sql);
                preSmt.setString(1, userid);
                rs = preSmt.executeQuery();
                rs.last();
                System.out.println("password sama : "+BCrypt.checkpw(password, rs.getString("password")));
                if (rs.getRow() == 1) {
                    
                    if (BCrypt.checkpw(password, rs.getString("password"))) {
                        if (rs.getString("aktif").equals("T")) {
                            return "blokir";
                        }
                        System.out.println("berhasil login");
                        return "berhasil";
                    } else {
                        System.out.println("password salah");
                    }
                }
            } catch (SQLException e) {
                System.out.println("gagal login : "+e);
            }
            return "gagal";
        }
        
        public static void main(String[] args) {
            UserDao u = new UserDao();
//            System.out.println(u.getAlluser());
            System.out.println(u.login("admin", "password"));
        }
}
