import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.table.*;

public class ClientGUI extends JFrame{

	private static final long serialVersionUID = 1L;
	private String server = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	private Socket client = null;
	private DefaultTableModel model = new DefaultTableModel();
	private ConnectState serveryesno = new ConnectState();
	private MyIP ipags = new MyIP();

	public void build() {
		try {
			serveryesno.serverconnectset(false);
			JFrame frame = new JFrame("JTorrent");
			ImageIcon icono = new ImageIcon("icono.png"); 
			frame.setIconImage(icono.getImage());
			InetAddress addr = InetAddress.getLocalHost(); 
			String ipAddr = addr.getHostAddress();
			ipags.setIp(ipAddr);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() { 
					try {
						if (server != null) {
							out.writeObject(".deleteme");
							out.writeObject(ipags.getIp());
							out.close();
						}
						System.out.println("Cerraremos esta vez pero la próxima vez usa Archivo> Salir");
					} catch(Exception ex) {}
				} 
			});
			JMenuBar menuBar = new JMenuBar();
			frame.setJMenuBar(menuBar);
			JMenu mFile = new JMenu("Archivo");
			mFile.setMnemonic('f');
			JMenuItem nw = new JMenuItem("Nuevo");
			nw.setMnemonic('n');
			JMenu administer = new JMenu("Administración");
			JMenuItem ipad = new JMenuItem("Obtener mi direccion IP");
			JMenuItem amiconnected = new JMenuItem("Estoy conectado?");
			ipad.setMnemonic('e');
			amiconnected.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					String printit = serveryesno.serverconnectget();
					JOptionPane.showMessageDialog(null,printit,"Conectado",JOptionPane.PLAIN_MESSAGE);
				}
			});
			ipad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFrame ipframe = new JFrame("Su Direccion IP");
					JLabel iplabel = new JLabel();
					iplabel.setText("Su direccion IP es: " +ipags.getIp());
					ipframe.getContentPane().add (iplabel);
					ipframe.setSize(200, 75);
					ipframe.setLocation(300,250);
					ipframe.setVisible(true);
				}
			});
			JMenuItem connect = new JMenuItem("Conectar a servidor");
			connect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						server=JOptionPane.showInputDialog(null,"Conectar al servidor","127.0.0.1");
						client = new Socket (server, 1001);
						serveryesno.serverconnectset(true);
						out = new ObjectOutputStream(client.getOutputStream());
						String currentDirectory = System.getProperty("user.dir");
						File dir = new File(currentDirectory + "\\share");
						File[] list = dir.listFiles();
						for(int ii = 0; ii < list.length; ii++){
							if(Util.getFormat(list[ii]).equals("mp3")){
								String outfile = list[ii].getName().toString();
								out.writeObject(".ip");
								out.writeObject(InetAddress.getLocalHost().toString());
								out.writeObject(".gofile");
								out.writeObject(outfile);
								out.writeObject(".filesize");
								out.writeObject(String.valueOf(list[ii].length()));
								out.writeObject(".type");
								out.writeObject(String.valueOf(Util.getMetaDataQualification(list[ii])));
								out.writeObject(".done");
								out.writeObject("Spacer");
							}
						}
					} catch (Exception ae) {System.out.println(ae);}
				}
			});
			JMenuItem exit = new JMenuItem("Salir");
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
					JOptionPane.showMessageDialog(null,"Gracias por usar Servicios de jTorrent","Gracias",JOptionPane.PLAIN_MESSAGE);
					System.exit(0);
				}
			});
			connect.setMnemonic('c');
			exit.setMnemonic('x');
			mFile.add(nw);
			mFile.add(connect);
			mFile.add(exit);
			administer.add(ipad);
			administer.add(amiconnected);
			menuBar.add(mFile);
			menuBar.add(administer);
			JPanel layitout = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel search= new JLabel("Buscar");
			final JTextField searchtext = new JTextField();
			final JTable table = new JTable(model) {
			private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int rowIndex, int vColIndex) {
					return false; 
				} 
			};
			searchtext.setPreferredSize (new Dimension(200,20));
			JButton searchrecords = new JButton("Buscar");
			frame.getRootPane().setDefaultButton(searchrecords);
			searchrecords.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					if (searchtext.getText().trim().equals("")) {
						JOptionPane.showMessageDialog(null, "Se necesita una solicitud de búsqueda","Buscar",JOptionPane.PLAIN_MESSAGE);
					} else {
						try {
							SearchFile search = new SearchFile();
							client = new Socket (server, 1001);
							in = new ObjectInputStream(client.getInputStream());
							out = new ObjectOutputStream(client.getOutputStream());
							while(model.getRowCount()>0){
								model.removeRow(0);
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
			model.addColumn("Calificación");
			model.addColumn("Direccion IP");
			model.addColumn("Tamaño del archivo");
			table.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (me.getClickCount()!=2) return;
					int row = table.rowAtPoint(me.getPoint());
					String ipaddresstoconnectto = (String)table.getValueAt(row, 2); 
					Object filetoget = table.getValueAt(row, 0);
					Object filesizetoget = table.getValueAt(row, 3);
					Download file = new Download();
					file.getFile(ipaddresstoconnectto.split("/")[1], filetoget, filesizetoget);
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
