
public class ClientMain {

	public static void main(String[] args) {
		ClientGUI gui = new ClientGUI();
		gui.build();
		ClientServer cserv = new ClientServer();
		cserv.startServer();
	}

}
