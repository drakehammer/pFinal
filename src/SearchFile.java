import java.io.*;
import java.net.InetAddress;

import javax.swing.table.*;

public class SearchFile{

	public void searchit(String searchtxt, ObjectInputStream in, ObjectOutputStream out, DefaultTableModel modl){
		Thread inqueryThread = new inqueryThread(in, out, modl);
		inqueryThread.start();
		try {
			out.writeObject(".query");
			out.writeObject(searchtxt);
		} catch (Exception e) {}
	}
}

class inqueryThread extends Thread {
	ObjectInputStream inny;
	ObjectOutputStream out;
	int flag = 0;
	DefaultTableModel model;
	String first;
	String second;
	String third;
	String forth;
	public inqueryThread(ObjectInputStream inn, ObjectOutputStream outt, DefaultTableModel mod) {
		out = outt;
		inny = inn;
		model = mod;
	}
	public void run() {
		while(true) {
			try {
				Object get = inny.readObject();
				switch (flag) {
				case 0:
					if (get.equals(".filename")) {
						flag = 1;
						break;
					}
					if(get.equals(".type")) {
						flag = 2;
						break;
					}
					if(get.equals(".ipaddress")) {
						flag = 3;
						break;
					}
					if (get.equals(".filesize")) {
						flag = 4;
						break;
					}
					if (get.equals(".nofiles")) {
						model.insertRow(0, new Object[]{"Búsqueda no produjo resultados"});
						flag = 0;
						break;
					}
				case 1:
					first = (String)get;
					flag = 0;
					break;
				case 2:
					second = (String)get;
					flag = 0;
					break;
				case 3:
					third = (String)get;
					if(!InetAddress.getLocalHost().toString().split("/")[1].equals(third.split("/")[1])){
						model.insertRow(0, new Object[]{first, second, third, forth});
						flag = 0;
					}
					break;
				case 4:
					forth = (String)get;
					flag = 0;
					break;
				}
			} catch (Exception e) {System.out.println (e);}
		}
	}
}


