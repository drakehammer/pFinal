
public class Screen {

	public void clearscreen() {
		for (int x = 0 ; x < 250 ; x++) {
			System.out.println();
		}
	}

	public int numberOfFinds(int num) {
		System.out.println("Número de hallazgos " + num);
		return num;
	}	
}