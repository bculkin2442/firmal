package bjc.firmal;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import bjc.utils.gui.SimpleFileChooser;
import bjc.utils.gui.layout.AutosizeLayout;

/**
 * Pane for Firmal.
 * @author Ben Culkin
 *
 */
public class FirmalFrame {
	/**
	 * Create a new Firmal Pane.
	 * @return The firmal pane.
	 */
	public static JFrame createFirmalPane() {
		JFrame mainframe = new JFrame("Firmal Browser");
		mainframe.setLayout(new AutosizeLayout());

		FirmalBrowserPanel browser = new FirmalBrowserPanel(mainframe);
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem openItem = new JMenuItem("Open...");
		openItem.addActionListener((ev) -> {
			File dir = SimpleFileChooser.pickDirectory(mainframe, "Pick Directory to Browse...");
			
			// Didn't pick a directory
			if (dir == null) return;
			
			browser.openDirectory(dir);
		});
		openItem.setMnemonic(KeyEvent.VK_O);
		openItem.setAccelerator(KeyStroke.getKeyStroke("control O"));
		
		fileMenu.add(openItem);
		
		menuBar.add(fileMenu);
		
		mainframe.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mev) {
				if (mev.getButton() == 4) browser.moveNext();
				if (mev.getButton() == 5) browser.movePrevious();
			}
		});
		mainframe.setJMenuBar(menuBar);
		mainframe.add(browser);
		
		return mainframe;
	}
}
