
public class Screen {

	public void clearScreen() {
		for (int x = 0 ; x < 250 ; x++) {
			System.out.println();
		}
	}

	public int numberOfFinds(int num) {
		System.out.println("N�mero de hallazgos " + num);
		return num;
	}	
}