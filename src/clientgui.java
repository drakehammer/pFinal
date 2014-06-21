import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.table.*;

/**
* clientgui.java - starts the client part of the software
* @author John Hass - john@jkcool.com
* Java 1 capstone project
* Project:Capstone
* @version Revised April 22, 2003 by John Hass
**/
public class clientgui extends JFrame{

//The server String to hold the remote server
public static String server = null;

//We will be using ObjectStreams for all of our transfers here is our in and out
public static ObjectOutputStream out = null;
public static ObjectInputStream in = null;

//Create our Socket
public static Socket client = null;

//We need a model to add things to our table
public static DefaultTableModel model = new DefaultTableModel();

/**
* This method builds the Gui Interface and accepts nothing
**/
    public void build() {

	// we have to try our throw statement incase of error
      try {

	//server connect object to hold if were on or off
	final serverconnect serveryesno = new serverconnect();

	//set serverconnect to off
	serveryesno.serverconnectset(false);

	//create the JFrame
	JFrame frame = new JFrame("JTorrent");
	ImageIcon icono = new ImageIcon("icono.png"); 
	frame.setIconImage(icono.getImage()); 

	//JKcool file Sharing is run via IP's so we need to the IP
	InetAddress addr = InetAddress.getLocalHost(); 
	String ipAddr = addr.getHostAddress();
	
	//This holds our IP sets and gets to hold our IP Address
	final ipaddressgetset ipags = new ipaddressgetset();
		ipags.setIp(ipAddr); 

	//if the program dosen't get shutdown correctly we still need to log
	//log the person off correctly or there files will get stuck in the db
	Runtime.getRuntime().addShutdownHook(new Thread() {
	public void run() { 
			try {
			
			//see if the server has an IP
			if (server != null) {

			//.deleteme calls the deleteme case on the server
			out.writeObject(".deleteme");
			
			//this sends the clients IP address so the SQL can delete them
			out.writeObject(ipags.getIp());
	
			//close the stream
			out.close();

			} //end the if statment

			//Slap the user on the hand if they exit this way usually by using ctrl+C
			System.out.println("We'll Close this time but next time close using file > exit");

			//Catch any possible exception
			} catch(Exception ex) {}
		} 
	});


	//Create our menubar object
	JMenuBar menuBar = new JMenuBar();

	//Make our JFrame use our bar	
	frame.setJMenuBar(menuBar);

	//make an Object on the menu bar
	JMenu mFile = new JMenu("File");

	//Mnemonic sets a shortcut key
	mFile.setMnemonic('f');

	//add a sub menu
	JMenuItem nw = new JMenuItem("New");
	nw.setMnemonic('n');

	//Add anothermenu to the menubar
	JMenu administer = new JMenu("Administration");

	//Add some items
	JMenuItem srv = new JMenuItem("Administer A Server");	
	JMenuItem ipad = new JMenuItem("Get my IP address");
	JMenuItem amiconnected = new JMenuItem("Am I connected");
		ipad.setMnemonic('e');
	JMenuItem rndsrv = new JMenuItem("Get a Random Server");
		rndsrv.setMnemonic('r');

	//This action Listener waits for a click on the Random server JMenuItem
	rndsrv.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			//Display a message about what you Just clicked on
			JOptionPane.showMessageDialog(null,"Some Server allow for you to see other servers Online\nthis will pole the list and get a random one","Poll Message",JOptionPane.PLAIN_MESSAGE);
			if (server.equals(null)) {
			//check for a connection
			JOptionPane.showMessageDialog(null,"You Must be connected to a server First","Error",JOptionPane.ERROR_MESSAGE);
			} else {
			try {

			//Use our server info and connect
			client = new Socket (server, 1001);

			//create our in and outputstream
			in = new ObjectInputStream(client.getInputStream());
			out = new ObjectOutputStream(client.getOutputStream());

			//poll the server for server
			out.writeObject(".poll");

			//Our server always expects 2 lines but this only requires
			//so we just shoot something that does nothing
			out.writeObject("Spacer");
			
			//out loop we'll need a few varible I make them here
			int count = 0;
			int around = 0;

			//To stop our loop we need to set go to false
			boolean go = true;
		
			//Build some vectors to hold the polling info
			Vector polledipv = new Vector();
			Vector pollednmv = new Vector();
			
			//create 2 arrays to move the vectors 2 a possible of 10 servers
			//But arrays we'll resize if it goes bigger.  They had to be set
			//to some variable to work or else get a non initlized error
			Object[] polledip = new Object[10];
			Object[] pollednm = new Object[10];

			//make our count variables (for future version use)
			int count1 = 0;
			int count2 = 0;

			//While Loop for polling
			while (go) {

			//Object input Stream uses Objects so I have to make an
			//Object varible to hold the input
			Object pollin = in.readObject();

			//Switch around for our case statement
			switch (around) {
				
				//Case 0: holds all of the trigger cases
				case 0:

					//if whatever came in through readObject then trip case 1
					if (pollin.equals(".ip")) {
						around = 1;

						//break the case statement
						break;			
					}//end the if statement

					//Same thing wait for readObject
					if (pollin.equals(".name")){
						//trip case 2 trigger
						around = 2;
						break;
					}

					//if the input is done then then use our vectors and arrays
					if (pollin.equals(".done")) {
				
						//fill our arrays with what was in our vectors
						for(int i = 0; i< polledipv.size(); i++) {
							polledip[i] = polledipv.elementAt(i);
						}
					
						for (int j = 0; j<pollednmv.size(); j++) {
							pollednm[j] = pollednmv.elementAt(j);
						}
						//we need to make sure that our while loop stops
						go = false;

						//Create a new random Object
						getrandom randomone = new getrandom();
			
						//call the getrandom method
				  		Object random = randomone.getrandomone(polledip, pollednm);
	
						//print the answer from the method
						System.out.println(random);
	
						//also print it to the screen
						JOptionPane.showMessageDialog(null,"We Found a Server for you " + random ,"New Server",JOptionPane.PLAIN_MESSAGE);
	
						//break the case statment
						break;
					}//stop our if statement
				case 1:
					//if case 1 is triggered add to our ip vector
					polledipv.addElement(pollin);
					//add one to count
					count1++;
					//get our switch variable back to 0
					around = 0;
					//break the case statement
					break;
				case 2:
					//if case 2 os triggered add 1 to our name vector
					pollednmv.addElement(pollin);
					//add one to our second count variable
					count2++;
					//flip our switch to 0
					around = 0;
					//break the case
					break;
			}//end switch
			}
			} catch (Exception e){}			
			}// end if statement
			
		}
	});

	//add the listner to see if your connected
	amiconnected.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			//call the method to see if I am connected
			String printit = serveryesno.serverconnectget();
			//print it in a nice message either way
			JOptionPane.showMessageDialog(null,printit,"Connected",JOptionPane.PLAIN_MESSAGE);

		}//end action performed
	});//end action listener			

	//add action listener to get IP address
	ipad.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {

			//make our frame and get the IP Address			
			JFrame ipframe = new JFrame("Your IPaddress");
			JLabel iplabel = new JLabel();
			iplabel.setText("Your ip address: " +ipags.getIp());
			ipframe.getContentPane().add (iplabel);
			ipframe.setSize(200, 75);
			ipframe.setLocation(300,250);
			ipframe.setVisible(true);
		}
	});

	  //set a Mnemonic for add server
	  srv.setMnemonic('a');

		  //add action listener for the server admin		
	       srv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent apae) {
				//call our serveradmin object
				serverAdmin admin = new serverAdmin();
				try {
				//startup the admin server
				admin.startUp(server);
			} catch (Exception e) {}
			}
		});

	//connect to the server action Listener
	JMenuItem connect = new JMenuItem("Connect to Server");
	    connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			try {
			//get my ipaddress
			String ipgotten = ipags.getIp();
			//pop the window for the server ip or domain
			server=JOptionPane.showInputDialog(null,"Server To Connect to","127.0.0.1");
			//create a client socket
			client = new Socket (server, 1001);
			//set connected to true
			serveryesno.serverconnectset(true);
			//build the outputstream
			out = new ObjectOutputStream(client.getOutputStream());
			//get the directory
			String currentDirectory = System.getProperty("user.dir"); 
			//set the share directory
			File dir = new File(currentDirectory + "\\share");
			//list the files
			File[] list = dir.listFiles();

			//send all your info to the server line by line
			for(int ii = 0; ii < list.length; ii++)
			{
			String outfile = list[ii].getName().toString();
			out.writeObject(".ip");
			out.writeObject(ipgotten);
			out.writeObject(".gofile");
			out.writeObject(outfile);
			out.writeObject(".filesize");
			//send the file size of the file
			out.writeObject(String.valueOf(list[ii].length()));
			out.writeObject(".type");
			//type is not implemented
			out.writeObject("Not implemented yet");
			out.writeObject(".done");
			//we have to set a Spacer to do nothing again for compatibility sake
			out.writeObject("Spacer");
			}
			} catch (Exception ae) {System.out.println(ae);}
			}
		}
	    );

	//make out exit JMenuItem and add an action Listener
	JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
				//see if server is null
				if (server != null) {
					try {
					//client socket build
					client = new Socket (server, 1001);
					//create our out socket
					out = new ObjectOutputStream(client.getOutputStream());
					//we need to delete all the files from the deleteme
					out.writeObject(".deleteme");
					out.writeObject(ipags.getIp());
					//close the stream
					out.close();
					} catch(Exception e){}
				}
				    //Pop a nice message
				    JOptionPane.showMessageDialog(null,"Thank You for using JKCool File Services","Thanks",JOptionPane.PLAIN_MESSAGE);
				    //exit the System
				    System.exit(0);
				}//end if statement
			}
		);


	//set our Mnemonics
	connect.setMnemonic('c');
	exit.setMnemonic('x');
	//add our things to the filemenu
	mFile.add(nw);
	mFile.add(connect);
	mFile.add(exit);
	administer.add(srv);
	administer.add(ipad);
	administer.add(amiconnected);
	administer.add(rndsrv);
	menuBar.add(mFile);
	menuBar.add(administer);
	//set the Layout	
	JPanel layitout = new JPanel(new FlowLayout(FlowLayout.LEFT));
	//add a nice label
	JLabel search= new JLabel("Search");
	//add a textfield 
	final JTextField searchtext = new JTextField();
	//add a table

	//make our table and make it NOT editable
	final JTable table = new JTable(model) {
		public boolean isCellEditable(int rowIndex, int vColIndex) {
 		return false; 
		} 
	};
	//set the preferred size of the textfield
	searchtext.setPreferredSize (new Dimension(200,20));
	
	//Make a search button
	JButton searchrecords = new JButton("Search");
	//Make it a default button
	frame.getRootPane().setDefaultButton(searchrecords);
	//add an action Listener for the new button
	searchrecords.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			//see if the searchtext field is empty tell them about it
			if (searchtext.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null, "You need a Search request","SEARCH",JOptionPane.PLAIN_MESSAGE);
			} else {
			try {
			//search text Object
			searchtext search = new searchtext();
			//make a client
			client = new Socket (server, 1001);
			//make our streams
			in = new ObjectInputStream(client.getInputStream());
			out = new ObjectOutputStream(client.getOutputStream());
			//clear the table
			for(int y =0; y < model.getRowCount(); y++) {
				model.removeRow(y);
			}
			//get the textbox text
			String searchtxt = searchtext.getText();
			//call the search method
			search.searchit(searchtxt, in, out, model);
			} catch (Exception e){}
			}
		}
	});
	//Add our things
	layitout.add(search);
	layitout.add(searchtext);
	layitout.add(searchrecords);



	//add column headings to the table
	model.addColumn("Filename"); 
	model.addColumn("Tag");
	model.addColumn("Ip Address");
	model.addColumn("File Size");
	//add a mouse Listener and call download if it is double clicked
	table.addMouseListener(new MouseAdapter() {
	public void mouseReleased(MouseEvent me) {
	if (me.getClickCount()!=2) return;
		int row = table.rowAtPoint(me.getPoint());
		Object ipaddresstoconnectto = table.getValueAt(row, 2); 
		Object filetoget = table.getValueAt(row, 0);
		Object filesizetoget = table.getValueAt(row, 3);
		download file = new download();
		file.getFile(ipaddresstoconnectto, filetoget, filesizetoget);
		}
	});

	//add the layouts to the Frame
	frame.getContentPane().add(layitout, BorderLayout.NORTH);
	//add scrollbars to the table
	frame.getContentPane().add(new JScrollPane(table)); 
	//set the size of the JFrame
	frame.setSize(600,400);
	//Set the close Operation
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//add a window listener if a close event happens
	frame.addWindowListener( new WindowAdapter(){
		public void windowClosing(WindowEvent e) {
		try {
		//send the deleteme stuff
		out.writeObject(".deleteme");
		out.writeObject(ipags.getIp());
		out.close();
		} catch(Exception ex) {}
		}
	});		
	//set the Location towards the middle
	frame.setLocation(100,50);
	//make sure we can see it
	frame.setVisible(true);
	//catch any acceptions
	}catch (Exception exc){}
    }
}
        