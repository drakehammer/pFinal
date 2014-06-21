import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.table.*;

/**
* searchtext.java - Calls the search stuff for the server
* @author John Hass - john@jkcool.com
* Java 1 capstone project
* Project:Capstone
* @version Revised April 22, 2003 by John Hass
**/
public class searchtext{
/**
* Searchit take a search string an object in and out and the table model
**/
	public void searchit(String searchtxt, ObjectInputStream in, ObjectOutputStream out, DefaultTableModel modl){
	//build the query thread
	Thread inqueryThread = new inqueryThread(in, out, modl);
	//actually start it
	inqueryThread.start();
	try {
	//run the search so the Thread has something to read
	out.writeObject(".query");
	out.writeObject(searchtxt);
	} catch (Exception e) {}
	}
}

class inqueryThread extends Thread {
//The thread needs varibles so the whole thread can use them
ObjectInputStream inny;
ObjectOutputStream out;
int flag = 0;
DefaultTableModel model;
String first;
String second;
String third;
String forth;
	public inqueryThread(ObjectInputStream inn, ObjectOutputStream outt, DefaultTableModel mod) {
	//set the varibles we made to the varibles we passed
	out = outt;
	inny = inn;
	model = mod;
	}
//INIT the therad
public void run() {
	while(true) {
		try {
		//set the input reader to an Object variable
		Object get = inny.readObject();
			 switch (flag) {
				//main case triggers all
				case 0:
					//check the inputs and if a known input is triggered it's called
					if (get.equals(".filename")) {
						flag = 1;
						break;
					}
					if(get.equals(".type")) {
						flag = 2;
						break;
					}
					if(get.equals(".ipaddress")) {
						flag = 3;
						break;
					}
					if (get.equals(".filesize")) {
						flag = 4;
						break;
					}
					//if .nofiles reicved the search returned nothign and we need to say that
					if (get.equals(".nofiles")) {
						model.insertRow(0, new Object[]{"Search Yieled no results"});
						flag = 0;
						break;
					}
				case 1:
					//first sets the first model column
					first = (String)get;
					flag = 0;
					break;
				case 2:
					//second sets the second model column
					second = (String)get;
					flag = 0;
					break;
				case 3:
					//third sets the third column
					third = (String)get;
					model.insertRow(0, new Object[]{first, second, third, forth});
					flag = 0;
					break;
				case 4:
					//forth the forth
					forth = (String)get;
					flag = 0;
					break;
			}
	} catch (Exception e) {System.out.println (e);}
	}
}
}

	
