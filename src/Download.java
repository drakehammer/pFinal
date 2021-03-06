import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

public class Download {

	public void getFile(Object ipAddy, Object fileGet, Object size) {
		JFrame dframe = new JFrame("Descargando desde " + String.valueOf(ipAddy));
		JLabel fileLabel = new JLabel(String.valueOf(fileGet));
		JProgressBar progress = new JProgressBar();
		JLabel donelabel = new JLabel();
		dframe.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));
		dframe.getContentPane().add(fileLabel);
		dframe.getContentPane().add(donelabel);
		dframe.getContentPane().add(progress);
		dframe.setSize(300, 85);
		dframe.setVisible(true);
		dframe.setResizable(false);
		donelabel.setText("Descargando");
		Thread downloadingThread = new downloadingThread(ipAddy, fileGet, size, progress, donelabel);
		downloadingThread.start();
	}
}

class downloadingThread extends Thread {
	private String ipAddy;
	private String fileGet;
	private String size;
	private JProgressBar progress;
	private int flag = 0;
	private JLabel done;
	
	public downloadingThread(Object ipa, Object fget, Object fsize, JProgressBar bar, JLabel dne){
		ipAddy = (String)ipa;
		fileGet = (String)fget;
		size = (String)fsize;
		progress = bar;
		done = dne;
	}

	public void run() {
		try {
			@SuppressWarnings("resource")
			Socket connect = new Socket (ipAddy, 1000);
			ObjectOutputStream outclient = new ObjectOutputStream(connect.getOutputStream());
			ObjectInputStream infrom = new ObjectInputStream(connect.getInputStream());
			outclient.writeObject(".download");
			outclient.writeObject(fileGet);
			progress.setMinimum(0);
			progress.setMaximum(Integer.parseInt(size.toString()));
			while (true) {
				Object get = infrom.readObject();
				switch (flag) { 
				case 0:
					if (get.equals(".startdownload")) {
						flag = 1;

					}
				case 1:
					int totalDataRead;
					int totalSizeWritten = 0;
					int PACKET_SIZE=2048;
					byte[] packet=new byte[PACKET_SIZE];
					System.out.println("Guardando archivo: " + fileGet);
					String currentDirectory = System.getProperty("user.dir");
					String uploaddirectory = currentDirectory + "\\share\\"+fileGet;
					FileOutputStream fos = new FileOutputStream(uploaddirectory);
					while((totalDataRead = infrom.read(packet,0,packet.length)) >-1) {
						fos.write(packet,0,totalDataRead);
						totalSizeWritten = totalSizeWritten + totalDataRead;
						progress.setValue(totalSizeWritten);
					}
					done.setText("Descargar Completa");
					flag = 0;
				}
			}
		} catch (Exception e) {System.out.println(e);}
	}
}



