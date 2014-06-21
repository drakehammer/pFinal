/**
* clientmain.java - starts the client part of the software
* @author John Hass - john@jkcool.com
* Java 1 capstone project
* Project:Capstone
* @version Revised April 22, 2003 by John Hass
**/
public class clientmain {

    //Start the program
    public static void main(String[] args) {

	//Create the Object
      clientgui gui = new clientgui();

	//Run the Build script
	gui.build();

	//Create the Object for the client side server
	clientserver cserv = new clientserver();

	//start the server
	cserv.startServer();

    }//end the clientmain class

}//end the main statement
	