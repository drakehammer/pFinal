import java.io.*;

public class servermain {

	public static void main(String[] args) { 
		serverrun serveit = new serverrun();
		serverdelete deletefiles = new serverdelete();
			deletefiles.deletenow();
		try {
		serveit.runserver();
		} catch (IOException e){}
	}
}