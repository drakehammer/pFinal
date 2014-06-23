import java.sql.*;

public class ServerInit {

	public void deletenow() {

		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:jtorrent.db");
			Statement statement = c.createStatement();
			String query = ("DROP TABLE IF EXISTS FILES");
			statement.executeUpdate(query);
			query = "CREATE TABLE IF NOT EXISTS FILES (" + 
					" filename varchar(50)," +
					" type varchar(50)," +
					" ipaddress varchar(50)," +
					" filesize varchar(50))"; 
			statement.executeUpdate(query);
			statement.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
	}
}