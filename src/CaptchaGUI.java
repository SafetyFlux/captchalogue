import java.awt.Container;
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
	
	private JMenuBar bar = new JMenuBar();
	private JMenuItem newMI = new JMenuItem("New");
	private JMenuItem saveMI = new JMenuItem("Save");
	private JMenuItem loadMI = new JMenuItem("Load");
	private JMenu themes = new JMenu("Themes");
	private JMenuItem symbolMI = new JMenuItem("Toggle Symbol");
	private JMenu humans = new JMenu("Humans");
	private JMenu trolls = new JMenu("Trolls");
	private JMenuItem themesHuman[] = new JMenuItem[8];
	private JMenuItem themesTroll[] = new JMenuItem[12];
	private JMenuItem alchemyMI = new JMenuItem("Toggle Alchemy");
	private JMenuItem gridMI = new JMenuItem("Toggle Grids");
	private JMenuItem aboutMI = new JMenuItem("About");
	private JMenuItem secretMI = new JMenuItem("???");
	
	public CaptchaGUI() throws FileNotFoundException{
		
		Container pane = getContentPane();
		pane.add(panel);
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(newMI);
		fileMenu.add(saveMI);
		fileMenu.add(loadMI);
		JMenu optMenu = new JMenu("Options");
		optMenu.add(themes);
		optMenu.add(alchemyMI);
		optMenu.add(gridMI);
		themes.add(symbolMI);
		themes.add(humans);
		Scanner reader = new Scanner(new File("res/ThemesHuman.txt"));
		for (int i = 0; i < themesHuman.length; i++){
			String th = reader.nextLine();
			themesHuman[i] = new JMenuItem(th);
			humans.add(themesHuman[i]);
		}
		reader.close();
		themes.add(trolls);
		reader = new Scanner(new File("res/ThemesTroll.txt"));
		for (int i = 0; i < themesTroll.length; i++){
			String th = reader.nextLine();
			themesTroll[i] = new JMenuItem(th);
			trolls.add(themesTroll[i]);
		}
		reader.close();
		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(aboutMI);
		helpMenu.add(secretMI);
		bar.add(fileMenu);
		bar.add(optMenu);
		bar.add(helpMenu);
		setJMenuBar(bar);
		
		newMI.addActionListener(new NewListener());
		saveMI.addActionListener(new SaveListener());
		loadMI.addActionListener(new LoadListener());
		symbolMI.addActionListener(new SymbolListener());
		for (int i = 0; i < themesHuman.length; i++){
			final int j = i;
			themesHuman[j].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					panel.changeTheme(themesHuman[j].getText());
				}
			});
		}
		for (int i = 0; i < themesTroll.length; i++){
			final int j = i;
			themesTroll[j].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					panel.changeTheme(themesTroll[j].getText());
				}
			});
		}
		alchemyMI.addActionListener(new AlchemyListener());
		gridMI.addActionListener(new GridListener());
		aboutMI.addActionListener(new AboutListener());
		secretMI.addActionListener(new SecretListener());
		
	}
	
	private class NewListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.resetCode();
		}
	}
	
	private class SaveListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.save();
		}
	}
	
	private class LoadListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.load();
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
	
	private class AboutListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.changeSettings("About");
		}
	}
	
	private class SecretListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.secretCode();
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		boolean run = true;
		CaptchaGUI theGUI = new CaptchaGUI();
		try {
			Image icon = ImageIO.read(new File("images/CardIcon.png"));
			theGUI.setIconImage(icon);
		} catch (IOException e) {
		}
		theGUI.setTitle("Captchalogue Card Simulator");
		if(panel.getCards())
			theGUI.setSize(881, 692);
		else
			theGUI.setSize(493, 692);
		theGUI.setResizable(false);
		theGUI.setAlwaysOnTop(false);
		
		theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theGUI.setVisible(true);
		
		while(run){
			if(panel.getCards())
				theGUI.setSize(881, 692);
			else
				theGUI.setSize(493, 692);
		}
		
	}

}
