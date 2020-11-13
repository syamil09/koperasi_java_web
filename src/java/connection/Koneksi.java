/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 *
 * @author syamil imdad
 */
public class Koneksi {
    static Connection koneksi;
    
    public static Connection getConnection() {
        if (koneksi == null) {
                MysqlDataSource data = new MysqlDataSource();
                data.setUser("root");
                data.setPassword("");
                data.setDatabaseName("PBO_koperasi");
                try {
                    koneksi = data.getConnection();
                    System.out.println("Koneksi Berhasil");
                } catch (SQLException e) {
                    System.out.println("Koneksi Gagal : "+e);
                }
        }
        
        return koneksi;
    }
    
    public static void main(String[] args) {
        getConnection();
    }

}
