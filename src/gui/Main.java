package gui;

import java.sql.*;

import sql.ConnectionManager;

public class Main {

	private static final String url = "jdbc:oracle:thin:@diassrv2.epfl.ch:1521:orcldias";
	private static final String username = "DB2017_G09"; 
	private static final String password = "DB2017_G09";
	
	private static ConnectionManager cm;

	public static void main(String[] args) {
		
		cm = new ConnectionManager(url, username, password);
		Connection connection;
		
		try {
			connection = cm.getConnection();
			@SuppressWarnings("unused")
			Frame frame = new Frame(800, 800, connection);
		} catch (SQLException e) {
			System.err.println("Connection failed");
		}
	}
}