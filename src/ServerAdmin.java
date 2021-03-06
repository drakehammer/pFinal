import javax.swing.*;
import java.io.*;
import java.net.*;

public class ServerAdmin extends JFrame {

	private static final long serialVersionUID = 1L;
	private static Socket adminSocket = null;
	private static ObjectOutputStream outadmin;

	public void startUp(String server)throws Exception{
		String username = JOptionPane.showInputDialog(null,"Username","administrator");
		JPasswordField pass = new JPasswordField();
		Object[] message = new Object[] {"Ingresar contraseņa", pass};	
		Object[] options = new String[] {"OK", "Cancelar"};
		JOptionPane op = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, options);
		JDialog dialog = op.createDialog(null, "Login");
		dialog.setVisible(true);
		String password = String.valueOf(pass.getPassword());
		String lowerCaseName = username.toLowerCase();
		String lowerCasePass = password.toLowerCase();
		adminSocket = new Socket (server, 1002);
		outadmin = new ObjectOutputStream(adminSocket.getOutputStream());
		outadmin.writeObject(".username");
		outadmin.writeObject(lowerCaseName);
		outadmin.writeObject(".password");
		outadmin.writeObject(lowerCasePass);
	}
}