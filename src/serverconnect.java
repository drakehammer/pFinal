
public class serverconnect {
	
private static boolean connect;

	public void serverconnectset(boolean truefalse) {
		connect = truefalse;
	}

	public String serverconnectget() {
		String answerback = connect ? "You Are Connected" : "You Are Not Connected";
 		return answerback;
	}
}