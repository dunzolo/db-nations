package org.lessons.java.sql.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
	public static void main(String[] args) {
		
		String url = "jdbc:mysql://localhost:3306/java-nation";
		String user = "root";
		String pwd = "code";
		
		try(Scanner sc = new Scanner(System.in);
				Connection con = DriverManager.getConnection(url, user, pwd)){
			
			String sql = "SELECT c.country_id, \r\n"
					+ "	c.name AS nome_nazione,\r\n"
					+ "	r.name AS nome_regione,\r\n"
					+ "	c2.name AS nome_continente\r\n"
					+ "FROM regions r \r\n"
					+ "JOIN countries c ON r.region_id = c.region_id \r\n"
					+ "JOIN continents c2 ON r.continent_id = c2.continent_id \r\n"
					+ "WHERE c.name LIKE ?"
					+ "ORDER BY c.name ASC";
			
			System.out.print("Search: ");
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
				}
			}
			catch(SQLException e) {
				System.err.println("Query not well formed");
			}
			
			String sql1 = "SELECT DISTINCT c.name,\r\n"
					+ "	l.`language`,\r\n"
					+ "	cs.`year`,\r\n"
					+ "	cs.population,\r\n"
					+ "	cs.gdp \r\n"
					+ "FROM countries c \r\n"
					+ "JOIN country_languages cl ON c.country_id = cl.country_id \r\n"
					+ "JOIN languages l ON cl.language_id = l.language_id \r\n"
					+ "JOIN country_stats cs ON c.country_id = cs.country_id \r\n"
					+ "WHERE c.country_id = ? "
					+ "ORDER BY cs.`year` DESC";
			
			System.out.print("Chose a country id: ");
			int searchId = sc.nextInt();
			
			try(PreparedStatement ps = con.prepareStatement(sql1)){
				
				ps.setInt(1, searchId);
				
				try(ResultSet rs = ps.executeQuery()){
					
					Set<String> setNazione = new HashSet<>();
					Set<String> setLingue = new HashSet<>();
					List<Integer> listAnno = new ArrayList<>();
					List<Integer> listPopolazione = new ArrayList<>();
					List<Long> listGPD = new ArrayList<>();
					
					while(rs.next()) {
						final String nazione = rs.getString(1);
						setNazione.add(nazione);
						final String language = rs.getString(2);
						setLingue.add(language);
						final int anno = rs.getInt(3);
						listAnno.add(anno);
						final int popolazione = rs.getInt(4);
						listPopolazione.add(popolazione);
						final long gdp = rs.getLong(5);
						listGPD.add(gdp);
						
					}
					
					System.out.println("Details for country: " + setNazione);
					System.out.print("Languages: " + setLingue);
					System.out.println("\nMost recent stats");
					System.out.println("Year: " + listAnno.get(0));
					System.out.println("Population: " + listPopolazione.get(0));
					System.out.println("GDP: " + listGPD.get(0));
				}
			}
			catch(SQLException e) {
				System.err.println("Query not well formed");
			}
			
		}catch(SQLException e) {
			System.err.println("Error during connection to db");
		}
	}
}
