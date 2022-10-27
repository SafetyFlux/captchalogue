package main;

import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import resources.ResourceLoader;

public class CaptchaGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static CaptchaPanel panel;
	static {
		try {
			panel = new CaptchaPanel();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// All the JMenu components
	private JMenuBar bar = new JMenuBar();
	private JMenuItem newMI = new JMenuItem("Reset Card");
	private JMenuItem saveMI = new JMenuItem("Save Card as Image ");
	private JMenuItem symbolMI = new JMenuItem("Toggle Symbols");
	private JMenu humans = new JMenu("Humans");
	private JMenu trolls = new JMenu("Trolls");
	private JMenu aspects = new JMenu("Aspects");
	private JMenu misc = new JMenu("Miscellaneous  ");
	private JMenuItem alchemyMI = new JMenuItem("Toggle Alchemy");
	private JMenuItem gridMI = new JMenuItem("Toggle Grids");
	private JMenuItem operationMI = new JMenuItem("Toggle Other Operations ");
	private JMenuItem aboutMI = new JMenuItem("About");
	private JMenuItem shortcutMI = new JMenuItem("Shortcuts");
	// The font used for the toolbar
	private Font f = new Font("Courier New", Font.BOLD, 14);
	// Theme arrays
	private static String[] thListHuman =
		{
			"Blue (John)",
			"Orchid (Rose)",
			"Red (Dave)",
			"Green (Jade)",
			"Cyan (Jane)",
			"Pink (Roxy)",
			"Orange (Dirk)",
			"Emerald (Jake)"
		};
	private static String[] thListTroll =
		{
			"Rust (Aradia)",
			"Bronze (Tavros)",
			"Gold (Sollux)",
			"Grey (Karkat)",
			"Olive (Nepeta)",
			"Jade (Kanaya)",
			"Teal (Terezi)",
			"Cobalt (Vriska)",
			"Indigo (Equius)",
			"Purple (Gamzee)",
			"Violet (Eridan)",
			"Fuchsia (Feferi)"
		};
	private static String[] thListAspect =
		{
			"Space",
			"Time",
			"Breath",
			"Blood",
			"Mind",
			"Heart",
			"Light",
			"Void",
			"Life",
			"Doom",
			"Hope",
			"Rage"
		};
	private static String[] thListMisc =
		{
			"Prospit",
			"Derse",
			"Cherub (Caliborn)",
			"Cherub (Calliope)"
		};
	
	// Get theme lists
	public static String[] getThemesHuman() {
		return thListHuman;
	}
	public static String[] getThemesTroll() {
		return thListTroll;
	}
	public static String[] getThemesAspect() {
		return thListAspect;
	}
	public static String[] getThemesMisc() {
		return thListMisc;
	}
	
	public CaptchaGUI() throws FileNotFoundException {
		
		// Set up the JMenu toolbar
		Container pane = getContentPane();
		pane.add(panel);
		JMenu fileMenu = new JMenu("File");
		fileMenu.setFont(f);
		newMI.setFont(f);
		saveMI.setFont(f);
		fileMenu.add(newMI);
		fileMenu.add(saveMI);
		JMenu themeMenu = new JMenu("Themes");
		themeMenu.setFont(f);
		themeMenu.add(humans);
		for (int i = 0; i < thListHuman.length; i++) {
			String th = thListHuman[i];
			JMenuItem item = new JMenuItem(th);
			item.setFont(f);
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					panel.changeTheme(item.getText(), "humans");
				}
			});
			humans.add(item);
		}
		themeMenu.add(trolls);
		for (int i = 0; i < thListTroll.length; i++) {
			String th = thListTroll[i];
			JMenuItem item = new JMenuItem(th);
			item.setFont(f);
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					panel.changeTheme(item.getText(), "trolls");
				}
			});
			trolls.add(item);
		}
		themeMenu.add(aspects);
		for (int i = 0; i < thListAspect.length; i++) {
			String th = thListAspect[i];
			JMenuItem item = new JMenuItem(th);
			item.setFont(f);
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					panel.changeTheme(item.getText(), "aspects");
				}
			});
			aspects.add(item);
		}
		themeMenu.add(misc);
		for (int i = 0; i < thListMisc.length; i++) {
			String th = thListMisc[i];
			JMenuItem item = new JMenuItem(th);
			item.setFont(f);
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					panel.changeTheme(item.getText(), "miscellaneous");
				}
			});
			misc.add(item);
		}
		JMenu optMenu = new JMenu("Options");
		optMenu.setFont(f);
		alchemyMI.setFont(f);
		gridMI.setFont(f);
		operationMI.setFont(f);
		symbolMI.setFont(f);
		humans.setFont(f);
		trolls.setFont(f);
		aspects.setFont(f);
		misc.setFont(f);
		optMenu.add(alchemyMI);
		optMenu.add(gridMI);
		optMenu.add(operationMI);
		optMenu.add(symbolMI);
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setFont(f);
		aboutMI.setFont(f);
		shortcutMI.setFont(f);
		helpMenu.add(aboutMI);
		helpMenu.add(shortcutMI);
		bar.add(fileMenu);
		bar.add(themeMenu);
		bar.add(optMenu);
		bar.add(helpMenu);
		setJMenuBar(bar);
		
		// Add action listeners for each toolbar item
		newMI.addActionListener(new NewListener());
		saveMI.addActionListener(new SaveListener());
		symbolMI.addActionListener(new SymbolListener());
		alchemyMI.addActionListener(new AlchemyListener());
		gridMI.addActionListener(new GridListener());
		operationMI.addActionListener(new OperationListener());
		aboutMI.addActionListener(new AboutListener());
		shortcutMI.addActionListener(new ShortcutListener());
		
	}
	
	// Action listeners
	private class NewListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			panel.resetCode(true);
		}
	}
	
	private class SaveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			panel.savePrompt();
		}
	}
	
	private class SymbolListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			panel.changeSettings("Toggle Symbol");
		}
	}
	
	private class AlchemyListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			panel.changeSettings("Toggle Alchemy");
		}
	}
	
	private class GridListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			panel.changeSettings("Toggle Grids");
		}
	}
	
	private class OperationListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			panel.changeSettings("Toggle Other Operations");
		}
	}
	
	private class AboutListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			panel.changeSettings("About");
		}
	}
	
	private class ShortcutListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			panel.changeSettings("Shortcuts");
		}
	}
	
	// Main component where the GUI is created
	public static void main(String[] args) throws FileNotFoundException {
		
		boolean run = true;
		CaptchaGUI theGUI = new CaptchaGUI();
		Image icon = ResourceLoader.loadImage("icons/CardIcon.png");
		theGUI.setIconImage(icon);
		theGUI.setTitle("Captchalogue Card Simulator");
		if(panel.getCards())
			theGUI.setSize(881, 692);
		else
			theGUI.setSize(493, 692);
		// Set every other window attribute
		theGUI.setResizable(false);
		theGUI.setAlwaysOnTop(false);
		theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theGUI.setVisible(true);
		
		// Change window size when alchemy is toggled
		while (run) {
			if (panel.getCards())
				theGUI.setSize(881, 692);
			else
				theGUI.setSize(493, 692);
		}
		
	}

}
