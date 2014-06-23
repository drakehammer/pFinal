import java.io.*;

public class ServerMain {

	public static void main(String[] args) { 

		ServerInit deletefiles = new ServerInit();
		deletefiles.deletenow();
		ServerRun serveit = new ServerRun();
		try {
			serveit.runserver();
		} catch (IOException e){}
	}
}