package sql;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

public class ConnectionManager {
	
	private String url;
	private String username;
	private String password;
	
	public ConnectionManager(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public Connection getConnection() throws SQLException{
		OracleDataSource ods = new OracleDataSource();
	    ods.setURL(url);
	    ods.setUser(username);
	    ods.setPassword(password);
	    return ods.getConnection();
	}
	
}