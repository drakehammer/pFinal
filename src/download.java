import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.JProgressBar.*;

/**
* clientmain.java - starts the download
* @author John Hass - john@jkcool.com
* Java 1 capstone project
* Project:Capstone
* @version Revised April 22, 2003 by John Hass
**/
public class download {
	
/**
* Accepts ipaddress the file to get and the file size
**/
public void getFile(Object ipAddy, Object fileGet, Object size) {
	//build a download frame
	JFrame dframe = new JFrame("Download from " + String.valueOf(ipAddy));
	//print the JLabel of the file size
	JLabel fileLabel = new JLabel(String.valueOf(fileGet));
	//make a new progress bar
	JProgressBar progress = new JProgressBar();
	//make a new label that holds downloading or connecting status
	JLabel donelabel = new JLabel();
	//setup the layout
	dframe.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));
	//add the filelabel
	dframe.getContentPane().add(fileLabel);
	//add the empty done label
	dframe.getContentPane().add(donelabel);
	//add the progress bar
      dframe.getContentPane().add(progress);
	//set the size of the frame
	dframe.setSize(300, 75);
	//make it visible
	dframe.setVisible(true);
	//Change the label of the donelabel
	donelabel.setText("Downloading");
	//build the downloading thread
	Thread downloadingThread = new downloadingThread(ipAddy, fileGet, size, progress, donelabel);
	//start the thread
	downloadingThread.start();
	}
}

//this Thread extends the client
class downloadingThread extends Thread {
	//ip address
    String ipAddy;
	//get the file name
    String fileGet;
	//set the size
    String size;
	//build the progress bar
    JProgressBar progress;
	//set the flag
    int flag = 0;
	//make the Label
    JLabel done;
	public downloadingThread(Object ipa, Object fget, Object fsize, JProgressBar bar, JLabel dne){
		//we have to set all the varibles to Strings and set them equal to the
		//what we made up top
		ipAddy = (String)ipa;
		fileGet = (String)fget;
		size = (String)fsize;
		progress = bar;
		done = dne;
	}
//INIT the Thread
public void run() {
  try {
	//set up our connection
  Socket connect = new Socket (ipAddy, 1000);
  ObjectOutputStream outclient = new ObjectOutputStream(connect.getOutputStream());
  ObjectInputStream infrom = new ObjectInputStream(connect.getInputStream());
  //write the objects of stuff to get to the server
  outclient.writeObject(".download");
  outclient.writeObject(fileGet);
  //set our progress bars minimum
  progress.setMinimum(0);
  //setup the progress bars maximum
  progress.setMaximum(Integer.parseInt(size.toString()));
  //start the while loop
while (true) {
	//setup the get varible to read from the server
	Object get = infrom.readObject();
	//switch the flag so our in from server can have some sense made of them
	switch (flag) {
	//case 0 is our main case this is when things get triggered 
	case 0:
		//see if we got a start download;
		if (get.equals(".startdownload")) {
			//set flag to one but DO NOT BREAK
			flag = 1;
	
		}
	 case 1:
	//our in is the same as our out BUT we have to read from the stream byte by byte
	int data;
	int totalDataRead;
	int totalSizeWritten = 0;
	int totalSizeRead;
	int PACKET_SIZE=2048;
	byte[] packet=new byte[PACKET_SIZE];
	System.out.println("File to Save: " + fileGet);
    String currentDirectory = System.getProperty("user.dir");
    String uploaddirectory = currentDirectory + "\\share\\"+fileGet;
	FileOutputStream fos = new FileOutputStream(uploaddirectory);
      while((totalDataRead = infrom.read(packet,0,packet.length)) >-1) {
            fos.write(packet,0,totalDataRead);
	    totalSizeWritten = totalSizeWritten + totalDataRead;
	    progress.setValue(totalSizeWritten);
      }
	//then of course make sure the users know that it is done by changing the label
	done.setText("Download Complete");
	//set flag to 0
	flag = 0;
	}
    }
  } catch (Exception e) {System.out.println(e);}
  }
}
	


	 