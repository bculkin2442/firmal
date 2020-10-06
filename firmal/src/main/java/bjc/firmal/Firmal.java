package bjc.firmal;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Main class for Firmal.
 * 
 * Firmal is a image/file browser.
 * 
 * @author Ben Culkin
 *
 */
public class Firmal {
	/**
	 * General main method.
	 * @param args Currently unused CLI args.
	 */
	public static void main(String[] args) {
		JFrame frame = FirmalPane.createFirmalPane();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		frame.pack();
		frame.setSize(640, 480);
		
		frame.setVisible(true);
	}
}
