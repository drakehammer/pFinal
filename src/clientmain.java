
public class clientmain {

    public static void main(String[] args) {

      clientgui gui = new clientgui();

	gui.build();

	clientserver cserv = new clientserver();

	cserv.startServer();

    }

}
	