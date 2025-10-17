package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class koneksi {
	
	private static Connection conn;
	
	public static Connection getConnection() {
		
		try {
			if (conn==null) {
				String url = "jdbc:mysql://localhost:3306/db_cafe";
				String user = "root";
				String pass = "";
				
				 Class.forName("com.mysql.cj.jdbc.Driver");
				
				conn = DriverManager.getConnection(url, user, pass);
				System.out.println("Koneksi Berhasil!");
			}
		} catch (Exception e) {
			System.out.println("Koneksi Gagal!");
			e.printStackTrace();
		}
		
		return conn;
	}
}
