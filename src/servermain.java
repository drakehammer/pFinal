import java.io.*;
/**
* servermain.java - starts the server part of the software
* @author John Hass - john@jkcool.com
* Java 1 capstone project
* Project:Capstone
* @version Revised April 22, 2003 by John Hass
**/
public class servermain {

	public static void main(String[] args) {
		//Create a serverrun object and wait for connections 
		serverrun serveit = new serverrun();
		//Create a Delete files Object and run it
		serverdelete deletefiles = new serverdelete();
			deletefiles.deletenow();
		try {
		//Start the server catch exceptions
		serveit.runserver();
		} catch (IOException e){}
	}
}