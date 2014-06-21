import java.net.*;
import java.io.*;
import java.io.ObjectInputStream.*;
import java.io.ObjectOutputStream.*;
/**
* clientserver.java - client for the server of the client
* @author John Hass - john@jkcool.com
* Java 1 capstone project
* Project:Capstone
* @version Revised April 22, 2003 by John Hass
**/
public class clientserver {

/**
* Start the server accepts nothing
**/
  	public void startServer() {
	try {
	//Server Socket to accept connections
	ServerSocket server = new ServerSocket(1000);
    	while(true) {		
	    
          //Accept the socket 
	    Socket server1 = server.accept();
	    //ObjectOutputStream things
	    ObjectOutputStream out = new ObjectOutputStream(server1.getOutputStream());
	    ObjectInputStream in = new ObjectInputStream(server1.getInputStream());
	    //Build our download thread
    	    Thread downloadThread = new downloadThread(server1, in, out);
	    //Start the thread
	    downloadThread.start();
	}
	} catch (Exception e) {}
    }
	
}

/**
* Download Thread
**/
class downloadThread extends Thread {
    //ObjectStrams
    ObjectInputStream in;
    ObjectOutputStream out;
	//Socket the client
    Socket server;
	//start our case flag
    int flag = 0;
/**
* Our downloadThread 
* Accepts a few thing
**/
	public downloadThread(Socket sock, ObjectInputStream inn, ObjectOutputStream ot){
	    //set our things equal to each other so we can use them in the thread
	    in = inn;
	    server = sock;
	    out = ot;
	}
//Init the Thread
    public void run() {
	//Start a forever loop
	while (true) {
	    try {
	    try {
	//get our in.readObject
	    Object get = in.readObject();
		//switch the flag 
	    switch (flag) {
		//case 0 main case
		case 0:
			//see what case equals if it equals something in the 0 case trip the trigger
		    if (get.equals(".download")) {
			flag = 1;
		     }
		break;

		case 1:
		//start the download thread
		String filename = get.toString();
		//for complience send this string to trigger the download
		out.writeObject(".startdownload");
		    //we need a varible for uploading to a client of this server
                int data;
                int totalSizeTransferred = 0;
                int totalSizeRead;
                int PACKET_SIZE=2048;
                byte[] packet=new byte[PACKET_SIZE];
			//get the file
		    String currentDirectory = System.getProperty("user.dir");
		    String uploaddirectory = currentDirectory + "\\share\\"+filename;
			//open the input stream to read it into the program and out to the client
		     FileInputStream fis = new FileInputStream(uploaddirectory);
				//write byte by byte 2048 bytes at a time very compatible with all platforms
                    while((totalSizeRead = fis.read(packet,0,packet.length)) >=0){
				out.write(packet,0,totalSizeRead);
                    }
				//close the stream from the client
        	       out.close();
				//clsoe the file so it isn't locked anymore
		       fis.close();
			//set the flag to 0
		flag = 0;
			//break the case
		break;	
	    }//end the Switch Statement
	    } catch(ClassNotFoundException cnfe) {}
	    } catch(IOException e) {}
    }
}    	
}
