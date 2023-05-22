package org.lessons.java.sql.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) {
		
		String url = "jdbc:mysql://localhost:3306/java-nation";
		String user = "root";
		String pwd = "code";
		
		try(Connection con = DriverManager.getConnection(url, user, pwd)){
			
			String sql = "SELECT r.region_id, \r\n"
					+ "	c.name AS nome_nazione,\r\n"
					+ "	r.name AS nome_regione,\r\n"
					+ "	c2.name AS nome_continente\r\n"
					+ "FROM regions r \r\n"
					+ "JOIN countries c ON r.region_id = c.region_id \r\n"
					+ "JOIN continents c2 ON r.continent_id = c2.continent_id \r\n"
					+ "ORDER BY c.name ASC";
			
			try(PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()){
				
					while(rs.next()) {
						final int id = rs.getInt(1);
						final String nazione = rs.getString(2);
						final String regione = rs.getString(3);
						final String continente = rs.getString(4);
						
						System.out.println(id + " - " + nazione + " - " + regione + " - " + continente);
						
					}
					
				}
			catch(SQLException e) {
				System.out.println();
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
