import java.awt.Container;
import java.io.FileNotFoundException;

import javax.swing.JFrame;

public class GUIWindow {

	public static void main(String[] args) throws FileNotFoundException {
		JFrame theGUI = new JFrame();
		theGUI.setTitle("Captchalogue Translation");
		theGUI.setSize(822, 852);
		theGUI.setResizable(false);
		theGUI.setAlwaysOnTop(false);
		theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		CaptchaPanel panel = new CaptchaPanel();
		Container pane = theGUI.getContentPane();
		pane.add(panel);
		theGUI.setVisible(true);
	}

}
