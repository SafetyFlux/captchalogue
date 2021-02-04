import java.awt.Container;
import java.io.FileNotFoundException;

import javax.swing.JFrame;

public class GUIWindow {

	public static void main(String[] args) throws FileNotFoundException {
		boolean run = true;
		JFrame theGUI = new JFrame();
		theGUI.setTitle("Captchalogue Card Simulator");
		theGUI.setSize(658, 687);
		theGUI.setResizable(false);
		theGUI.setAlwaysOnTop(false);
		theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		CaptchaPanel panel = new CaptchaPanel();
		Container pane = theGUI.getContentPane();
		pane.add(panel);
		theGUI.setVisible(true);
		
		while(run){
			if(panel.getCards())
				theGUI.setSize(823, 687);
			else
				theGUI.setSize(658, 687);
		}
	}

}
