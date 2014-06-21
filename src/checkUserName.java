import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.table.*;

/**
* checkUserName.java - is buggy and probably will crash
* @author John Hass - john@jkcool.com
* Java 1 capstone project
* Project:Capstone
* @version Revised April 22, 2003 by John Hass
**/

public class checkUserName {
  //setup the varibles for the class
  private static Socket adminSocket = null;
  private static ObjectOutputStream outadmin;
  private static ObjectInputStream inadmin;
/**
* Check the Username and password return array
**/
	public String[] checkStuff(String name, String pass, String server) {
		try {
		//setup the varibles and init them
		String nameinfo = null;
		String passinfo = null;
		//make a new socket and creat the streams
		adminSocket = new Socket (server, 1002);
		outadmin = new ObjectOutputStream(adminSocket.getOutputStream()); 
		inadmin = new ObjectInputStream(adminSocket.getInputStream());
		//send out what you typed for username and password
		outadmin.writeObject(".username");
		outadmin.writeObject(name);
		//wait for an answer
		Object namein = inadmin.readObject();
		outadmin.writeObject(".password");
		outadmin.writeObject(pass);
		Object passin = inadmin.readObject();
		//check what we get back
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

//	public void checkStuff(String passchange, String server) {
//		try {
//		adminSocket = new Socket (server, 1002);
//		outadmin = new ObjectOutputStream(adminSocket.getOutputStream()); 
//		inadmin = new ObjectInputStream(adminSocket.getInputStream());
//		outadmin.writeObject(".changepassword");
//		outadmin.writeObject(passchange);
//		} catch (Exception e) {}
//	}

}