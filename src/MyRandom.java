import java.util.*;

public class MyRandom extends Screen{

	public Object getrandomone(Object[] ip, Object[] name) {
		super.clearScreen();
		int size = name.length;
		int gotrandom = getrandomone(size);
		Object tosend = ip[gotrandom] + " " + name[gotrandom];
		StringTokenizer st = new StringTokenizer(tosend.toString());
		StringBuffer sbuf = new StringBuffer(tosend.toString());
		if (st.countTokens() < 2) {
			System.out.println("Error al obtener aleatorio");
		} else {
			sbuf.append(" Ir a ella si lo desea");
		}
		super.numberOfFinds(size);
		int minus = numberOfFinds(size);
		System.out.println("Encontrado " + minus);
		printtoscreen(ip[gotrandom]);
		hosttoscreen(name[gotrandom]);
		return sbuf;
	}

	public int getrandomone(int randomfinal) {
		int gotit = (int)(Math.random()*randomfinal);
		return gotit;
	}
	public void printtoscreen(Object ip) {
		String ipaddy = (String)ip;
		if (ipaddy.trim().equals("")) {
			System.out.println("Ocurrio un problema con la conversión, debe salir");
		} else {
			System.out.println("Dirección IP aleatoria " + ipaddy);
		}
	}
	public void hosttoscreen(Object hst) {
		String host = (String)hst;
		if (host.trim().equals("")) {
			System.out.println("Problema obteniendo servidor aleatorio, debe salir");
		} else {
			System.out.println("Host aleatorio " + host.toLowerCase());
		}
	}

	public void clearscreen() {
		for (int i = 0 ; i <15; i++) {
			System.out.println();
		}
	}

	public int numberoffinds(int number) {
		int numby = (number - 3);
		return numby;
	}
}