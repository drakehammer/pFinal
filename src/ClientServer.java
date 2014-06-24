import java.net.*;
import java.io.*;

public class ClientServer {

	public void startServer() {
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(1000);
			while(true) {
				Socket server1 = server.accept();
				ObjectOutputStream out = new ObjectOutputStream(server1.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(server1.getInputStream());
				Thread downloadThread = new downloadThread(server1, in, out);
				downloadThread.start();
			}
		} catch (Exception e) {}
	}

}

class downloadThread extends Thread {
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private int flag = 0;
	
	public downloadThread(Socket sock, ObjectInputStream inn, ObjectOutputStream ot){
		in = inn;
		out = ot;
	}
	public void run() {
		while (true) {
			try {
				try {
					Object get = in.readObject(); 
					switch (flag) {
					case 0:
						if (get.equals(".download")) {
							flag = 1;
						}
						break;

					case 1:
						String filename = get.toString();
						out.writeObject(".startdownload");
						int totalSizeRead;
						int PACKET_SIZE=2048;
						byte[] packet=new byte[PACKET_SIZE];
						String currentDirectory = System.getProperty("user.dir");
						String uploaddirectory = currentDirectory + "\\share\\"+filename;
						FileInputStream fis = new FileInputStream(uploaddirectory);
						while((totalSizeRead = fis.read(packet,0,packet.length)) >=0){
							out.write(packet,0,totalSizeRead);
						}
						out.close();
						fis.close();
						flag = 0;
						break;	
					}
				} catch(ClassNotFoundException cnfe) {}
			} catch(IOException e) {}
		}
	}    	
}
