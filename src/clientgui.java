import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.table.*;

public class clientgui extends JFrame{

public static String server = null;

public static ObjectOutputStream out = null;
public static ObjectInputStream in = null;

public static Socket client = null;

public static DefaultTableModel model = new DefaultTableModel();

    public void build() {

      try {

	final serverconnect serveryesno = new serverconnect();

	serveryesno.serverconnectset(false);

	JFrame frame = new JFrame("JTorrent");
	ImageIcon icono = new ImageIcon("icono.png"); 
	frame.setIconImage(icono.getImage()); 

	InetAddress addr = InetAddress.getLocalHost(); 
	String ipAddr = addr.getHostAddress();
	
	final ipaddressgetset ipags = new ipaddressgetset();
		ipags.setIp(ipAddr); 

	Runtime.getRuntime().addShutdownHook(new Thread() {
	public void run() { 
			try {
			
			if (server != null) {

			out.writeObject(".deleteme");
			
			out.writeObject(ipags.getIp());
	
			out.close();

			} 

			System.out.println("We'll Close this time but next time close using file > exit");

			} catch(Exception ex) {}
		} 
	});


	JMenuBar menuBar = new JMenuBar();
	
	frame.setJMenuBar(menuBar);

	JMenu mFile = new JMenu("Archivo");

	mFile.setMnemonic('f');

	JMenuItem nw = new JMenuItem("Nuevo");
	nw.setMnemonic('n');

	JMenu administer = new JMenu("Administrador");

	JMenuItem srv = new JMenuItem("Administrar Servidor");	
	JMenuItem ipad = new JMenuItem("Obtener mi direccion IP");
	JMenuItem amiconnected = new JMenuItem("estoy conectado?");
		ipad.setMnemonic('e');
	JMenuItem rndsrv = new JMenuItem("conectarme un servidor aleatorio");
		rndsrv.setMnemonic('r');

	rndsrv.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			JOptionPane.showMessageDialog(null,"Salgún servidor permite que usted vea otros servidores en linea, se conecta aleatoreamente","Poll Message",JOptionPane.PLAIN_MESSAGE);
			if (server.equals(null)) {
			JOptionPane.showMessageDialog(null,"Es necesario estar conectado al primer servidor ","Error",JOptionPane.ERROR_MESSAGE);
			} else {
			try {

			client = new Socket (server, 1001);

			in = new ObjectInputStream(client.getInputStream());
			out = new ObjectOutputStream(client.getOutputStream());

			out.writeObject(".poll");

			out.writeObject("Spacer");
			
			int count = 0;
			int around = 0;

			boolean go = true;
		
			Vector polledipv = new Vector();
			Vector pollednmv = new Vector();
			
			Object[] polledip = new Object[10];
			Object[] pollednm = new Object[10];

			int count1 = 0;
			int count2 = 0;

			while (go) {

			Object pollin = in.readObject();

			switch (around) {
				
				case 0:

					if (pollin.equals(".ip")) {
						around = 1;

						break;			
					}

					if (pollin.equals(".name")){
						around = 2;
						break;
					}

					if (pollin.equals(".done")) {
				
						for(int i = 0; i< polledipv.size(); i++) {
							polledip[i] = polledipv.elementAt(i);
						}
					
						for (int j = 0; j<pollednmv.size(); j++) {
							pollednm[j] = pollednmv.elementAt(j);
						}
						go = false;

						getrandom randomone = new getrandom();
			
				  		Object random = randomone.getrandomone(polledip, pollednm);
	
						System.out.println(random);
	
						JOptionPane.showMessageDialog(null,"encontramoes el servidor siguente para usted: " + random ,"New Server",JOptionPane.PLAIN_MESSAGE);
	
						break;
					}
				case 1:
					polledipv.addElement(pollin);
					count1++;
					around = 0;
					break;
				case 2:
					pollednmv.addElement(pollin);
					count2++;
					around = 0;
					break;
			}
			}
			} catch (Exception e){}			
			}
			
		}
	});

	amiconnected.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			String printit = serveryesno.serverconnectget();
			JOptionPane.showMessageDialog(null,printit,"conectardo",JOptionPane.PLAIN_MESSAGE);

		}
	});			

	ipad.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
	
			JFrame ipframe = new JFrame("Su Direccion IP");
			JLabel iplabel = new JLabel();
			iplabel.setText("su direccion IP es: " +ipags.getIp());
			ipframe.getContentPane().add (iplabel);
			ipframe.setSize(200, 75);
			ipframe.setLocation(300,250);
			ipframe.setVisible(true);
		}
	});

	  srv.setMnemonic('a');

	       srv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent apae) {
				serverAdmin admin = new serverAdmin();
				try {
				admin.startUp(server);
			} catch (Exception e) {}
			}
		});

	JMenuItem connect = new JMenuItem("Conectar a servidor");
	    connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			try {
			String ipgotten = ipags.getIp();
			server=JOptionPane.showInputDialog(null,"Conectar al servidor","127.0.0.1");
			client = new Socket (server, 1001);
			serveryesno.serverconnectset(true);
			out = new ObjectOutputStream(client.getOutputStream());
			String currentDirectory = System.getProperty("user.dir");
			File dir = new File(currentDirectory + "\\share");
			File[] list = dir.listFiles();

			for(int ii = 0; ii < list.length; ii++)
			{
			String outfile = list[ii].getName().toString();
			out.writeObject(".ip");
			out.writeObject(ipgotten);
			out.writeObject(".gofile");
			out.writeObject(outfile);
			out.writeObject(".filesize");
			out.writeObject(String.valueOf(list[ii].length()));
			out.writeObject(".type");
			out.writeObject("Not implemented yet");
			out.writeObject(".done");
			out.writeObject("Spacer");
			}
			} catch (Exception ae) {System.out.println(ae);}
			}
		}
	    );
	JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
				if (server != null) {
					try {
					client = new Socket (server, 1001);
					out = new ObjectOutputStream(client.getOutputStream());
					out.writeObject(".deleteme");
					out.writeObject(ipags.getIp());
					out.close();
					} catch(Exception e){}
				}
				    JOptionPane.showMessageDialog(null,"Gracias por usar Servicios de jTorrent","Thanks",JOptionPane.PLAIN_MESSAGE);
				    System.exit(0);
				}
			}
		);


	connect.setMnemonic('c');
	exit.setMnemonic('x');
	mFile.add(nw);
	mFile.add(connect);
	mFile.add(exit);
	administer.add(srv);
	administer.add(ipad);
	administer.add(amiconnected);
	administer.add(rndsrv);
	menuBar.add(mFile);
	menuBar.add(administer);
	JPanel layitout = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JLabel search= new JLabel("búsqueda");
	final JTextField searchtext = new JTextField();
	final JTable table = new JTable(model) {
		public boolean isCellEditable(int rowIndex, int vColIndex) {
 		return false; 
		} 
	};
	searchtext.setPreferredSize (new Dimension(200,20));
	
	JButton searchrecords = new JButton("búsqueda");
	frame.getRootPane().setDefaultButton(searchrecords);
	searchrecords.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			if (searchtext.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null, "Se necesita una solicitud de búsqueda","SEARCH",JOptionPane.PLAIN_MESSAGE);
			} else {
			try {
			searchtext search = new searchtext();
			client = new Socket (server, 1001);
			in = new ObjectInputStream(client.getInputStream());
			out = new ObjectOutputStream(client.getOutputStream());
			for(int y =0; y < model.getRowCount(); y++) {
				model.removeRow(y);
			}
			String searchtxt = searchtext.getText();
			search.searchit(searchtxt, in, out, model);
			} catch (Exception e){}
			}
		}
	});
	layitout.add(search);
	layitout.add(searchtext);
	layitout.add(searchrecords);


	model.addColumn("Nombre de archivo"); 
	model.addColumn("Etiqueta");
	model.addColumn("Direccion IP");
	model.addColumn("Tamaño del archivo");
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

	frame.getContentPane().add(layitout, BorderLayout.NORTH);
	frame.getContentPane().add(new JScrollPane(table));
	frame.setSize(600,400);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.addWindowListener( new WindowAdapter(){
		public void windowClosing(WindowEvent e) {
		try {
		out.writeObject(".deleteme");
		out.writeObject(ipags.getIp());
		out.close();
		} catch(Exception ex) {}
		}
	});
	frame.setLocation(100,50);
	frame.setVisible(true);
	}catch (Exception exc){}
    }
}
        