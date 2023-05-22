package org.lessons.java.sql.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		
		String url = "jdbc:mysql://localhost:3306/java-nation";
		String user = "root";
		String pwd = "code";
		
		try(Scanner sc = new Scanner(System.in);
				Connection con = DriverManager.getConnection(url, user, pwd)){
			
			String sql = "SELECT r.region_id, \r\n"
					+ "	c.name AS nome_nazione,\r\n"
					+ "	r.name AS nome_regione,\r\n"
					+ "	c2.name AS nome_continente\r\n"
					+ "FROM regions r \r\n"
					+ "JOIN countries c ON r.region_id = c.region_id \r\n"
					+ "JOIN continents c2 ON r.continent_id = c2.continent_id \r\n"
					+ "WHERE c.name LIKE ?"
					+ "ORDER BY c.name ASC";
			
			System.out.print("Serach: ");
			String search = "%" + sc.nextLine() + "%";
			
			try(PreparedStatement ps = con.prepareStatement(sql)){
				
				ps.setString(1, search);
				
				try(ResultSet rs = ps.executeQuery()){
					
					while(rs.next()) {
						final int id = rs.getInt(1);
						final String nazione = rs.getString(2);
						final String regione = rs.getString(3);
						final String continente = rs.getString(4);
						
						System.out.println(id + " - " + nazione + " - " + regione + " - " + continente);
						
					}
					
				}catch(SQLException e) {
					System.err.println("Query not well formed");
				}
				
			}
			catch(SQLException e) {
				System.err.println("Error during connection to db");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
