
public class ConnectState {

	private static boolean connect;

	public void serverconnectset(boolean truefalse) {
		connect = truefalse;
	}

	public String serverconnectget() {
		String answerback = connect ? "Est� conectado" : "No est� conectado";
		return answerback;
	}
}