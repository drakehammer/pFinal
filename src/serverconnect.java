/**
* serverconnect.java - Checks to see if your connected
* @author John Hass - john@jkcool.com
* Java 1 capstone project
* Project:Capstone
* @version Revised April 22, 2003 by John Hass
**/
public class serverconnect {
//Create a private Variable to hold the boolean
private static boolean connect;
/**
* Set the ServerConnect to true or false accepts boolean
**/
	public void serverconnectset(boolean truefalse) {
		connect = truefalse;
	}

/**
* get the server info using the ternary operation accepts nothing
**/
	public String serverconnectget() {
		String answerback = connect ? "You Are Connected" : "You Are Not Connected";
 		return answerback;
	}
}