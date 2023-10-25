package bjc.firmal;

/**
 * Exception thrown when we got a directory when should've gotten a non directory.
 * @author Ben Culkin
 *
 */
public class DirectoryExpected extends RuntimeException {
	private static final long serialVersionUID = 6614868098505684922L;

	/**
	 * Create a new exception.
	 * @param pth The path to the directory.
	 */
	public DirectoryExpected(String pth) {
		super(String.format("Non-directory '%s' passed where a directory was expected", pth));
	}
}