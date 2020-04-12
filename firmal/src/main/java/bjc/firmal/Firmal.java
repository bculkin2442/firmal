package bjc.firmal;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Firmal {
	public static void main(String[] args) {
		JFrame frame = FirmalPane.createFirmalPane();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		frame.pack();
		frame.setSize(640, 480);
		
		frame.setVisible(true);
	}
}
