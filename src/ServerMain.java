import java.io.*;

public class ServerMain {

	public static void main(String[] args) {
		ServerInit si = new ServerInit();
		si.deletenow();
		ServerRun server = new ServerRun();
		try {
			server.runserver();
		} catch (IOException e){}
	}
}