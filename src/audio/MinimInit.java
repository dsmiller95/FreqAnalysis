package audio;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Empty functioned class to initiate Minim with
 * @author millerds
 */
public class MinimInit {
	public String sketchPath(String f) {
		return f;
	}
	public InputStream createInput(String f) {
		return new BufferedInputStream(null);
	}
}