import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.table.*;
/**
* serverAdmin.java - Controls the Server Most Dosen't work still experimental
* @author John Hass - john@jkcool.com
* Java 1 capstone project
* Project:Capstone
* @version Revised April 22, 2003 by John Hass
**/
public class serverAdmin extends JFrame {
	//build the private variables
  private static Socket adminSocket = null;
  private static ObjectOutputStream outadmin;
  private static ObjectInputStream inadmin;
/**
* StartUp accepts a server string and it will throw any excepions
**/
	public void startUp(String server)throws Exception{
	//Make an ok variable
	String ok;
	//make an input dialog with username and password mask the password
	String username = JOptionPane.showInputDialog(null,"Username","administrator");
	JPasswordField pass = new JPasswordField();
	Object[] message = new Object[] {"Enter Password", pass};	
	Object[] options = new String[] {"OK", "Cancel"};
	JOptionPane op = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, options);
	JDialog dialog = op.createDialog(null, "Login");
	dialog.show();
	//get the password
	String password = String.valueOf(pass.getPassword());
	//make all lowercase so it matches server Values
	String lowerCaseName = username.toLowerCase();
	String lowerCasePass = password.toLowerCase(); 
	  //Create a new Socket and make the Streams
	  adminSocket = new Socket (server, 1002);
	  outadmin = new ObjectOutputStream(adminSocket.getOutputStream()); 
	  inadmin = new ObjectInputStream(adminSocket.getInputStream());
	  outadmin.writeObject(".username");
	  outadmin.writeObject(lowerCaseName);
	  outadmin.writeObject(".password");
	  outadmin.writeObject(lowerCasePass);
	  //Write the Info
	  //Nothing more has been Created for this option
	  Object infromdatabase = inadmin.readObject();
	}
}