import java.io.File;
import java.io.FileInputStream;


public class Util {

	public static String getFormat(File file){
		String extension = "";
		int i = file.getName().lastIndexOf('.');
		if (i >= 0) {
			extension = file.getName().substring(i+1);
		}
		return extension;
	}

	public static float getMetaDataQualification(File f) {
		float qualification=3;
		try {
			FileInputStream file = new FileInputStream(f);
			int size = (int)f.length();
			file.skip(size - 128);
			byte[] last128 = new byte[128];
			file.read(last128);
			String id3 = new String(last128);
			String tag = id3.substring(0, 3);
			if (tag.equals("TAG")) {
				if(!id3.substring(3, 32).equals(""))
					qualification+=0.5;
				if(!id3.substring(33, 62).equals(""))
					qualification+=0.5;
				if(!id3.substring(63, 91).equals(""))
					qualification+=0.5;
				if(!id3.substring(93, 97).equals(""))
					qualification+=0.5;
			} else
				System.out.println(f.getName() + " no contiene" + " informacion ID3.");
			file.close();
		} catch (Exception e) {
			System.out.println("Error - " + e.toString());
		}
		return qualification;
	}
	
}
