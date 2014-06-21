import java.io.*;
import java.util.*;
import java.sql.*;
/**
* serverdelete.java - Delete everything from database
* @author John Hass - john@jkcool.com
* Java 1 capstone project
* Project:Capstone
* @version Revised April 22, 2003 by John Hass
**/
public class serverdelete {

/**
*This accepts nothign and deletes everything in the database called when server starts
**/
	public void deletenow() {
		//connct to the server
		String url = System.getProperty("user.dir")+"\\db1.mdb";
		String conStr = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ="+url;
		try{
		//get the driver
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		//get the connection
		Connection con = DriverManager.getConnection(conStr);
		//Creat the SQL Statement
		Statement statement = con.createStatement();
		//build the query
		String query = ("delete from files");
		//Execute the SQL
		con.nativeSQL(query);
		//get the result if 1 success if 0 not
       	int result = statement.executeUpdate(query);
		} catch (Exception e) {}
	}
}