import java.io.*;
import java.net.*;

public class checkUserName {
  private static Socket adminSocket = null;
  private static ObjectOutputStream outadmin;
  private static ObjectInputStream inadmin;
  
	public String[] checkStuff(String name, String pass, String server) {
		try {
		String nameinfo = null;
		String passinfo = null;
		adminSocket = new Socket (server, 1002);
		outadmin = new ObjectOutputStream(adminSocket.getOutputStream()); 
		inadmin = new ObjectInputStream(adminSocket.getInputStream());
		outadmin.writeObject(".username");
		outadmin.writeObject(name);
		Object namein = inadmin.readObject();
		outadmin.writeObject(".password");
		outadmin.writeObject(pass);
		Object passin = inadmin.readObject();
		if (namein.equals(".bad")) {
			nameinfo = ".badusername";
		}

		if (namein.equals(".ok")) {
			nameinfo = ".userok";
		}

		if (passin.equals(".badp")) {
			passinfo = ".badpassword";
		}

		if (passin.equals(".changeit")) {
			passinfo = ".changepassword";
		}

		if (passin.equals(".ok")) {
			passinfo = ".passwordok";
		}
		String[] alldone = {nameinfo, passinfo};
		return alldone;
		} catch(Exception e) {}
		return null;
}

}