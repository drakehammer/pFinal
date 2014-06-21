import java.util.*;

/**
* getRandom.java - This gets a random server from the server
* @author John Hass - john@jkcool.com
* Java 1 capstone project
* Project:Capstone
* @version Revised April 22, 2003 by John Hass
**/
public class getrandom extends clearscreen{

/**
* Get Random accepts ip address as an Object array and Object name as an array
**/	
	public Object getrandomone(Object[] ip, Object[] name) {
		//our super class clears the screen
		super.clearscreen();
		//get the array length of name
		int size = name.length;
		//call the get random methosd with int
		int gotrandom = getrandomone(size);
		//create our return
		Object tosend = ip[gotrandom] + " " + name[gotrandom];
		//Create a String Tokenizer
		StringTokenizer st = new StringTokenizer(tosend.toString());
		//Create String buffer
		StringBuffer sbuf = new StringBuffer(tosend.toString());
		//count the tokens there should be 2 of them
		if (st.countTokens() < 2) {
		//if not error
		System.out.println("Error Getting Random");
		} else {
		//else append a message to it
		sbuf.append(" go to it if you choose");
		}
		//Use the super class to get the size
		super.numberOfFinds(size);
		//override the super method
		int minus = numberOfFinds(size);
		//print the overriden method
		System.out.println("inclass finds " + minus);
		//print to the screen the random ip
		printtoscreen(ip[gotrandom]);
		//print the the screen the random host
		hosttoscreen(name[gotrandom]);
		//return the String Buffer
		return sbuf;
	}
/**
* This accepts and int and retruns a random value between 1 and the int
* passed
**/
	public int getrandomone(int randomfinal) {
		int gotit = (int)(Math.random()*randomfinal);
		return gotit;
	}
/**
*This Printd the ip address to the screen and accepts and object
**/
	public void printtoscreen(Object ip) {
		String ipaddy = (String)ip;
		if (ipaddy.trim().equals("")) {
			//clearscreen();
			System.out.println("I think There was a problem with the Conversion you may have to exit");
		} else {
			System.out.println("Random IP Address " + ipaddy);
		}
	}
/**
*This will print the host to the screen
**/
	public void hosttoscreen(Object hst) {
	String host = (String)hst;
		if (host.trim().equals("")) {
			System.out.println("There must have been a problem Getting the random hostname you may have to exit");
		} else {
			System.out.println("Random host " + host.toLowerCase());
		}
	}

/**
* this is an overridden Method for clear screen.
* It clears Less lines
**/
	public void clearscreen() {
		for (int i = 0 ; i <15; i++) {
			System.out.println();
		}
	}
/**
* this is the overridden method for the the number of things in the array
**/
	public int numberoffinds(int number) {
		int numby = (number - 3);
		return numby;
	}
}