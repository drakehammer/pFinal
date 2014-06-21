/**
* ipaddressgetset.java - Gets or sets the Ip Address
* @author John Hass - john@jkcool.com
* Java 1 capstone project
* Project:Capstone
* @version Revised April 22, 2003 by John Hass
**/
public class ipaddressgetset {

//Set a private varible to hold the IP Address
private static String iphold;
/**
* Sets an Ip accepts a string
**/
	public void setIp(String ipaddress) {
		iphold = ipaddress;
	}

/**
* Gets the Ip Return the String
**/
	public String getIp() {
		return iphold;
	}
}
