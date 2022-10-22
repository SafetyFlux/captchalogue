package main;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class CaptchaGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static CaptchaPanel panel;
	static {
		try {
			panel = new CaptchaPanel();
		} catch (FileNotFoundException e){
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
	private JMenuItem themesHuman[] = new JMenuItem[8];
	private JMenuItem themesTroll[] = new JMenuItem[12];
	private JMenuItem themesAspect[] = new JMenuItem[12];
	private JMenuItem themesMisc[] = new JMenuItem[4];
	private JMenuItem alchemyMI = new JMenuItem("Toggle Alchemy");
	private JMenuItem gridMI = new JMenuItem("Toggle Grids");
	private JMenuItem operationMI = new JMenuItem("Toggle Other Operations ");
	private JMenuItem aboutMI = new JMenuItem("About");
	private JMenuItem shortcutMI = new JMenuItem("Shortcuts");
	// The font used for the toolbar
	private Font f = new Font("Courier New", Font.BOLD, 14);
	
	public CaptchaGUI() throws FileNotFoundException{
		
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
		Scanner reader = new Scanner(new File("res/ThemesHuman.txt"));
		for (int i = 0; i < themesHuman.length; i++){
			String th = reader.nextLine();
			themesHuman[i] = new JMenuItem(th);
			themesHuman[i].setFont(f);
			humans.add(themesHuman[i]);
		}
		reader.close();
		themeMenu.add(trolls);
		reader = new Scanner(new File("res/ThemesTroll.txt"));
		for (int i = 0; i < themesTroll.length; i++){
			String th = reader.nextLine();
			themesTroll[i] = new JMenuItem(th);
			themesTroll[i].setFont(f);
			trolls.add(themesTroll[i]);
		}
		reader.close();
		themeMenu.add(aspects);
		reader = new Scanner(new File("res/ThemesAspect.txt"));
		for (int i = 0; i < themesAspect.length; i++){
			String th = reader.nextLine();
			themesAspect[i] = new JMenuItem(th);
			themesAspect[i].setFont(f);
			aspects.add(themesAspect[i]);
		}
		reader.close();
		themeMenu.add(misc);
		reader = new Scanner(new File("res/ThemesMisc.txt"));
		for (int i = 0; i < themesMisc.length; i++){
			String th = reader.nextLine();
			themesMisc[i] = new JMenuItem(th);
			themesMisc[i].setFont(f);
			misc.add(themesMisc[i]);
		}
		reader.close();
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
		for (int i = 0; i < themesHuman.length; i++){
			final int j = i;
			themesHuman[j].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					panel.changeTheme(themesHuman[j].getText(), "humans");
				}
			});
		}
		for (int i = 0; i < themesTroll.length; i++){
			final int j = i;
			themesTroll[j].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					panel.changeTheme(themesTroll[j].getText(), "trolls");
				}
			});
		}
		for (int i = 0; i < themesAspect.length; i++){
			final int j = i;
			themesAspect[j].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					panel.changeTheme(themesAspect[j].getText(), "aspects");
				}
			});
		}
		for (int i = 0; i < themesMisc.length; i++){
			final int j = i;
			themesMisc[j].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					panel.changeTheme(themesMisc[j].getText(), "miscellaneous");
				}
			});
		}
		alchemyMI.addActionListener(new AlchemyListener());
		gridMI.addActionListener(new GridListener());
		operationMI.addActionListener(new OperationListener());
		aboutMI.addActionListener(new AboutListener());
		shortcutMI.addActionListener(new ShortcutListener());
		
	}
	
	// Action listeners
	private class NewListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.resetCode(true);
		}
	}
	
	private class SaveListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.savePrompt();
		}
	}
	
	private class SymbolListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.changeSettings("Toggle Symbol");
		}
	}
	
	private class AlchemyListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.changeSettings("Toggle Alchemy");
		}
	}
	
	private class GridListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.changeSettings("Toggle Grids");
		}
	}
	
	private class OperationListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.changeSettings("Toggle Other Operations");
		}
	}
	
	private class AboutListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.changeSettings("About");
		}
	}
	
	private class ShortcutListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			panel.changeSettings("Shortcuts");
		}
	}
	
	// Main component where the GUI is created
	public static void main(String[] args) throws FileNotFoundException {
		
		boolean run = true;
		CaptchaGUI theGUI = new CaptchaGUI();
		// Set window icon, title, and size
		try {
			Image icon = ImageIO.read(new File("images/icons/CardIcon.png"));
			theGUI.setIconImage(icon);
		} catch (IOException e) {
		}
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
		while(run){
			if(panel.getCards())
				theGUI.setSize(881, 692);
			else
				theGUI.setSize(493, 692);
		}
		
	}

}
