import java.sql.*;

public class serverdelete {

	public void deletenow() {
		String url = System.getProperty("user.dir")+"\\db1.mdb";
		String conStr = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ="+url;
		try{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection(conStr);
		Statement statement = con.createStatement();
		String query = ("delete from files");
		con.nativeSQL(query);
       	statement.executeUpdate(query);
		} catch (Exception e) {}
	}
}