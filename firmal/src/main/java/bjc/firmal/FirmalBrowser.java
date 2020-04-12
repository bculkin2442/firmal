package bjc.firmal;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import bjc.esodata.SingleTape;
import bjc.esodata.Tape;
import bjc.utils.gui.SimpleDialogs;
import bjc.utils.gui.SimpleKeyedButton;
import bjc.utils.gui.layout.VLayout;

public class FirmalBrowser extends JPanel {
	public class DirectoryExpected extends RuntimeException {
		private static final long serialVersionUID = 6614868098505684922L;

		public DirectoryExpected(String pth) {
			super(String.format("Non-directory '%s' passed where a directory was expected", pth));
		}
	}

	private static final long serialVersionUID = 9078988253392361649L;

	private JEditorPane contentPane;

	private String dirName;
	private String dirPath;

	private Tape<File> loadedFiles;

	private JFrame root;

	public FirmalBrowser(JFrame root) {
		super();
		
		this.loadedFiles = new SingleTape<>();
		this.root = root;

		setLayout(new BorderLayout());

		contentPane = new JEditorPane();
		contentPane.setEditable(false);
		contentPane.setContentType("text/html;charset=UTF-8");
		
		JScrollPane scrollPane = new JScrollPane(contentPane);

		JPanel buttonPanel = new JPanel(new VLayout(2));

		JButton refreshButton = new JButton("Refresh");

		JPanel navButtonPanel = new JPanel();
		navButtonPanel.setLayout(new GridLayout(2, 2));

		SimpleKeyedButton firstButton = new SimpleKeyedButton("<< First");
		firstButton.setGlobalDefaultKeystroke("firstFile", "control shift P", (aev) -> {
			moveFirst();
		});
		firstButton.setMnemonic(KeyEvent.VK_F);

		SimpleKeyedButton prevButton = new SimpleKeyedButton("< Previous");
		prevButton.setGlobalDefaultKeystroke("prevFile", "control P", (ev) -> {
			movePrevious();
		});

		SimpleKeyedButton lastButton = new SimpleKeyedButton("Last >>");
		lastButton.setGlobalDefaultKeystroke("lastFile", "control shift N", (ev) -> {
			moveLast();
		});

		SimpleKeyedButton nextButton = new SimpleKeyedButton("Next >");
		nextButton.setGlobalDefaultKeystroke("nextFile", "control N", (ev) -> {
			moveNext();
		});

		navButtonPanel.add(firstButton);
		navButtonPanel.add(lastButton);
		navButtonPanel.add(prevButton);
		navButtonPanel.add(nextButton);

		buttonPanel.add(refreshButton);
		buttonPanel.add(navButtonPanel);

		add(BorderLayout.PAGE_END, buttonPanel);
		add(BorderLayout.CENTER, scrollPane);
	}

	public void moveNext() {
		boolean res = loadedFiles.right();
		if (!res) {
			SimpleDialogs.showMessage(this, "Already There", "No next file. Already at the last one.");
			return;
		}

		loadFile(loadedFiles.item());
	}

	public void moveLast() {
		if (loadedFiles.atEnd()) {
			SimpleDialogs.showMessage(this, "Already There", "Already at the last file");
			return;
		}

		loadedFiles.last();

		loadFile(loadedFiles.item());
	}

	public void movePrevious() {
		boolean res = loadedFiles.left();
		if (!res) {
			SimpleDialogs.showMessage(this, "Already There", "No previous file. Already at the first one.");
			return;
		}

		loadFile(loadedFiles.item());
	}

	public void moveFirst() {
		if (loadedFiles.position() == 0) {
			SimpleDialogs.showMessage(this, "Already There", "Already at the first file");
			return;
		}

		loadedFiles.first();

		loadFile(loadedFiles.item());
	}

	public void openDirectory(File dir) {
		if (!dir.isDirectory())
			throw new DirectoryExpected(dir.getAbsolutePath());

		dirPath = dir.getAbsolutePath();
		dirName = dir.getName();

		loadedFiles = new SingleTape<>();

		// NOTE: Currently; ignores sub-directories. These should probably be handled in
		// some way (flag/dialog box?)
		for (File fle : dir.listFiles()) {
			if (fle.isDirectory())
				continue; // See above

			// We don't handle non-html file types for now.
			if (fle.getName().endsWith(".htm") || fle.getName().endsWith(".html")) {
				loadedFiles.append(fle);
			} else {
				System.err.printf("WARN: Ignoring non-HTML file '%s' in directory '%s' (%s)", fle.getName(), dirName,
						dirPath);
			}
		}

		loadedFiles.first();

		File curFile = loadedFiles.item();
		loadFile(curFile);
	}

	private void loadFile(File curFile) {
		try {
			contentPane.setPage(curFile.toURI().toURL());

			String msg = String.format("Firmal Browser (Browsing: %s) - %s - %d of %d", dirPath, curFile.getName(),
					loadedFiles.position(), loadedFiles.size());
			root.setTitle(msg);
		} catch (IOException ioex) {
			String msg = String.format("Couldn't load file '%s' from directory '%s' because %s", curFile, dirName,
					ioex.getMessage());

			SimpleDialogs.showError(this, "Error loading File", msg);

			ioex.printStackTrace();
		}
	}
}
