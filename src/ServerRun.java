import java.io.*;
import java.net.*;
import java.sql.*;

public class ServerRun {
	
	public void runserver() throws IOException{

		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(1001);
		while(true) {
			Socket client = server.accept();
			ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(client.getInputStream());
			Thread serverThread = new serverThread(out, in, client);
			serverThread.start();

		}
	}
}

class serverThread extends Thread {
	private Socket client;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String query;
	private int flag = 0;
	private String filein;
	private String typein;
	private String ip;
	private String filesize;
	private int result;
	
	public serverThread(ObjectOutputStream ot, ObjectInputStream inn, Socket client) {
		out = ot;
		in = inn;
		this.client=client;
	}

	public void run() {

		try{
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:jtorrent.db");
			Statement statement = con.createStatement();


			while(true) {
				Object get = in.readObject();
				switch (flag) {		
				case 0:
					if(get.equals(".query")) {
						flag = 1;
						break;
					}
					if (get.equals(".gofile")) {
						flag = 2;
						break;
					}
					if (get.equals(".type")) {
						flag = 3;
						break;
					}
					if (get.equals(".filesize")) {
						flag = 4;
						break;
					}
					if (get.equals(".ip")) {
						flag = 5;
						break;
					}
					if (get.equals(".done")) {
						flag = 6;
						System.out.println("Recibiendo archivo Hecho");
						break;
					}
					if (get.equals(".poll")) {
						flag = 7;
						System.out.println("Sondeo para los nuevos servidores");
						break;
					}
					if (get.equals(".deleteme")) {
						flag = 8;
						break;
					}
				case 1:
					if(get.equals("*"))
						query = ("SELECT * from files where ipaddress not like '"+client.getInetAddress().toString().replace("/", "")+"'");
					else
						query = ("SELECT * from files where filename LIKE '%" + get +"%' and ipaddress not like '"+client.getInetAddress().toString().replace("/", "")+"'");
					ResultSet rs = statement.executeQuery(query);
					int count = 0;
					while (rs.next()) {
						String flename = rs.getString("filename");
						out.writeObject(".filename");
						out.writeObject(flename);
						String type = rs.getString("type");
						out.writeObject(".type");
						out.writeObject(type);
						String ipaddress = rs.getString("ipaddress");
						String filesze = rs.getString("filesize");
						out.writeObject(".filesize");
						out.writeObject(filesze);
						out.writeObject(".ipaddress");
						out.writeObject(ipaddress);
						out.flush();
						count++; 
					}
					if (count == 0) {
						System.out.println("No hay archivos");
						out.writeObject(".nofiles");
						out.flush();
					}
					out.writeObject(".done");
					flag =0;
					break;
				case 2:
					System.out.println("Nombre archivo: " + get);
					filein = (String)get;
					flag = 0;
					break;	  
				case 3:
					System.out.println("Tipo: " + get);
					typein = (String)get;
					flag = 0;
					break;
				case 4:
					System.out.println("Tama�o archivo: " + get);
					filesize = (String)get;
					flag = 0;
					break;
				case 5:
					ip = (String)get;	
					flag = 0;
					break;
				case 6:
					try {
						query = ("Insert into files values ('"+ filein +"', '" + typein + "','" + ip + "','" + filesize + "')");
						con.nativeSQL(query);
						result = statement.executeUpdate(query);
						System.out.println(result);
						flag = 0;
						break;
					} catch (SQLException sqle) {System.out.println(sqle);}			
				case 7:
					query = ("SELECT * from Servers");
					rs = statement.executeQuery(query);
					System.out.println(query);
					while (rs.next()) {
						String pip = rs.getString("Ipaddress");
						out.writeObject(".ip");
						out.writeObject(pip);
						String pnm = rs.getString("Name");
						out.writeObject(".name");
						out.writeObject(pnm);
					}
					out.writeObject(".done");
					flag = 0;
					break;

				case 8:
					query = ("delete from files where ipaddress = '" + get + "'");
					con.nativeSQL(query);
					result = statement.executeUpdate(query);
					flag = 0;
					break;

				}

			}

		} catch(Exception e){System.out.println(e);}
	}
}