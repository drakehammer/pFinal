import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;
import java.applet.*;

/**
* serverrun.java - The Base of the Server classes
* @author John Hass - john@jkcool.com
* Java 1 capstone project
* Project:Capstone
* @version Revised April 22, 2003 by John Hass
**/
public class serverrun {

/**
*runserver() accepts nothing and throws any acceptions
**/ 
        public void runserver() throws IOException{

	    //accept clients
	   ServerSocket server = new ServerSocket(1001);
	  	//forever loop
		while(true) {
		//accept the client
		Socket client = server.accept();
		//create our objects
		ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(client.getInputStream());
		//bulid the thread
		Thread serverThread = new serverThread(out, in);
		//start the thread
		serverThread.start();
			
		}
	}
}

//Thread started
class serverThread extends Thread {
//create the thread variables
    ObjectInputStream in;
    ObjectOutputStream out;
    String query;
    int flag = 0;
    String filein;
    String typein;
    String ip;
    String filesize;
    int result;
	//make the variables equal to one another
	public serverThread(ObjectOutputStream ot, ObjectInputStream inn) {
	   out = ot;
	   in = inn;
	}
//INIT the server
public void run() {
//Get a DB connection
String url = System.getProperty("user.dir")+"\\db1.mdb";
String conStr = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ="+url;
try{
//The statement statement
Statement statement;
//drivers
Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
//get the connection
Connection con = DriverManager.getConnection("jdbc:odbc:bd");
//create the statement
statement = con.createStatement();

			
 while(true) {
	//read from the socket
	Object get = in.readObject();
		switch (flag) {
			//The 0 case is our main case all triggers tripped from here		
		    case 0:
			//a search term
			if(get.equals(".query")) {
			    flag = 1;
			    break;
			}
			//get the file name
			if (get.equals(".gofile")) {
				flag = 2;
				break;
			}
			//get the type for input
			if (get.equals(".type")) {
				flag = 3;
				break;
			}
			//get the filesize trip
			if (get.equals(".filesize")) {
				flag = 4;
				break;
			}
			//ip trip wire
			if (get.equals(".ip")) {
				flag = 5;
				break;
			} 
			//file done trip
			if (get.equals(".done")) {
				flag = 6;
				System.out.println("Done Recieveing file");
				break;
			}
			//server pole trip
			if (get.equals(".poll")) {
				flag = 7;
				System.out.println("Polling for new Servers");
				break;
			}
			//delete me trip
			if (get.equals(".deleteme")) {
				flag = 8;
				break;
			}
		   case 1:
			//Query for a search using wildcards and like statement
			query = ("SELECT * from files where filename LIKE '%" + get +"%'");
			//execute the query
                  statement.executeQuery(query);
			//get the result set
                  ResultSet rs = statement.getResultSet();
			//varible to count results
			int count = 0;
			//go until you can't go anymore
			while (rs.next()) {
			//get the filename
			String flename = rs.getString("filename");
			//send it
			out.writeObject(".filename");
			out.writeObject(flename);
			String type = rs.getString("type");
			//send the type
			out.writeObject(".type");
			out.writeObject(type);
			String ipaddress = rs.getString("ipaddress");
			String filesze = rs.getString("filesize");
			//send IP address and file size
			out.writeObject(".filesize");
			out.writeObject(filesze);
			out.writeObject(".ipaddress");
			out.writeObject(ipaddress);
            	out.flush();
		      count++; 
			}
			if (count == 0) {
				//if no count then no files tell that
				System.out.println("No files");
				out.writeObject(".nofiles");
				out.flush();
			}
				//got to know were done or everything crashes
				out.writeObject(".done");
			//flag to 0 and break the case
			flag =0;
			break;
		//cases 2 3 4 and 5 read from the stream and set the varibles for case 6
		case 2:
		  System.out.println("Filename: " + get);
		  filein = (String)get;
		  flag = 0;
		  break;	  
		case 3:
		   System.out.println("Type: " + get);
		   typein = (String)get;
		   flag = 0;
		   break;
		case 4:
		   System.out.println("file size: " + get);
		   filesize = (String)get;
		   flag = 0;
		   break;
		case 5:
		   //set the ip address
		   ip = (String)get;
			//delete all the files with matching db ip
		   query = ("delete from files where ipaddress ='" + ip + "'");
		   con.nativeSQL(query);
       	   result = statement.executeUpdate(query);	
		   flag = 0;
		   break;
		case 6:
		try {
		//add all the things to the db
		query = ("Insert into files values ('"+ filein +"', '" + typein + "','" + ip + "','" + filesize + "')");
		con.nativeSQL(query);
		//do the query
       	result = statement.executeUpdate(query);
		flag = 0;
		break;
		} catch (SQLException sqle) {System.out.println(sqle);}			
		case 7:
		//get the servers
		query = ("SELECT * from Servers");
            statement.executeQuery(query);
            rs = statement.getResultSet();
		System.out.println(query);
		while (rs.next()) {
			//get the ips and send them in client nice fashion
		String pip = rs.getString("Ipaddress");
		out.writeObject(".ip");
		out.writeObject(pip);
		//get the serverdomains and set them to client nice fashion
		String pnm = rs.getString("Name");
		out.writeObject(".name");
		out.writeObject(pnm);
		}
		//we have to tell the client we are done so they can break
		out.writeObject(".done");
		flag = 0;
		//break case
		break;
		
		case 8:
		//delete me statement deletes all the files and folders
		query = ("delete from files where ipaddress = '" + get + "'");
		//execute query
		con.nativeSQL(query);
       	result = statement.executeUpdate(query);
		flag = 0;
		//break it
		break;

		}
		
 	}

} catch(Exception e){System.out.println(e);}
	}
}