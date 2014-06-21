/**
* clearscreen.java - Clears the screen holds a super class
* @author John Hass - john@jkcool.com
* Java 1 capstone project
* Project:Capstone
* @version Revised April 22, 2003 by John Hass
**/

public class clearscreen {
	
/**
* Accepts nothing but clears the console screen	
**/
	public void clearscreen() {
		//print a blank line 250 times
		for (int x = 0 ; x < 250 ; x++) {
		System.out.println();
		}
	}
/**
* print the number of finds
**/

	public int numberOfFinds(int num) {
		System.out.println("Number Of finds " + num);
		return num;
	}	
}