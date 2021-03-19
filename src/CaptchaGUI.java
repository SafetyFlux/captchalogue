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
	
	private JMenuBar bar = new JMenuBar();
	private JMenuItem newMI = new JMenuItem("New");
	private JMenu save = new JMenu("Save");
	private JMenuItem saveMI0 = new JMenuItem("Main Code");
	private JMenuItem saveMI1 = new JMenuItem("Alchemy Code (Top)");
	private JMenuItem saveMI2 = new JMenuItem("Alchemy Code (Bottom)");
	private JMenuItem loadMI = new JMenuItem("Load");
	private JMenu themes = new JMenu("Themes");
	private JMenuItem symbolMI = new JMenuItem("Toggle Symbol");
	private JMenu humans = new JMenu("Humans");
	private JMenu trolls = new JMenu("Trolls");
	private JMenu aspects = new JMenu("Aspects");
	private JMenu sways = new JMenu("Lunar Sways");
	private JMenuItem themesHuman[] = new JMenuItem[8];
	private JMenuItem themesTroll[] = new JMenuItem[12];
	private JMenuItem themesAspect[] = new JMenuItem[12];
	private JMenuItem themesSway[] = new JMenuItem[2];
	private JMenuItem alchemyMI = new JMenuItem("Toggle Alchemy");
	private JMenuItem gridMI = new JMenuItem("Toggle Grids");
	private JMenuItem operationMI = new JMenuItem("Toggle Other Operations");
	private JMenuItem aboutMI = new JMenuItem("About");
	private JMenuItem shortcutMI = new JMenuItem("Shortcuts");
	private JMenuItem secretMI = new JMenuItem("???");
	private Font f = new Font("Courier", Font.BOLD, 14);
	
	public CaptchaGUI() throws FileNotFoundException{
		
		Container pane = getContentPane();
		pane.add(panel);
		JMenu fileMenu = new JMenu("File");
		fileMenu.setFont(f);
		newMI.setFont(f);
		save.setFont(f);
		loadMI.setFont(f);
		saveMI0.setFont(f);
		saveMI1.setFont(f);
		saveMI2.setFont(f);
		fileMenu.add(newMI);
		fileMenu.add(save);
		fileMenu.add(loadMI);
		save.add(saveMI0);
		save.add(saveMI1);
		save.add(saveMI2);
		JMenu optMenu = new JMenu("Options");
		optMenu.setFont(f);
		themes.setFont(f);
		alchemyMI.setFont(f);
		gridMI.setFont(f);
		operationMI.setFont(f);
		symbolMI.setFont(f);
		humans.setFont(f);
		trolls.setFont(f);
		aspects.setFont(f);
		sways.setFont(f);
		optMenu.add(themes);
		optMenu.add(alchemyMI);
		optMenu.add(gridMI);
		optMenu.add(operationMI);
		themes.add(symbolMI);
		themes.add(humans);
		Scanner reader = new Scanner(new File("res/ThemesHuman.txt"));
		for (int i = 0; i < themesHuman.length; i++){
			String th = reader.nextLine();
			themesHuman[i] = new JMenuItem(th);
			themesHuman[i].setFont(f);
			humans.add(themesHuman[i]);
		}
		reader.close();
		themes.add(trolls);
		reader = new Scanner(new File("res/ThemesTroll.txt"));
		for (int i = 0; i < themesTroll.length; i++){
			String th = reader.nextLine();
			themesTroll[i] = new JMenuItem(th);
			themesTroll[i].setFont(f);
			trolls.add(themesTroll[i]);
		}
		reader.close();
		themes.add(aspects);
		reader = new Scanner(new File("res/ThemesAspect.txt"));
		for (int i = 0; i < themesAspect.length; i++){
			String th = reader.nextLine();
			themesAspect[i] = new JMenuItem(th);
			themesAspect[i].setFont(f);
			aspects.add(themesAspect[i]);
		}
		reader.close();
		themes.add(sways);
		reader = new Scanner(new File("res/ThemesSway.txt"));
		for (int i = 0; i < themesSway.length; i++){
			String th = reader.nextLine();
			themesSway[i] = new JMenuItem(th);
			themesSway[i].setFont(f);
			sways.add(themesSway[i]);
		}
		reader.close();
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setFont(f);
		aboutMI.setFont(f);
		shortcutMI.setFont(f);
		secretMI.setFont(f);
		helpMenu.add(aboutMI);
		helpMenu.add(shortcutMI);
		helpMenu.add(secretMI);
		bar.add(fileMenu);
		bar.add(optMenu);
		bar.add(helpMenu);
		setJMenuBar(bar);
		
		newMI.addActionListener(new NewListener());
		saveMI0.addActionListener(new SaveListener0());
		saveMI1.addActionListener(new SaveListener1());
		saveMI2.addActionListener(new SaveListener2());
		loadMI.addActionListener(new LoadListener());
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
		for (int i = 0; i < themesSway.length; i++){
			final int j = i;
			themesSway[j].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					panel.changeTheme(themesSway[j].getText(), "sways");
				}
			});
		}
		alchemyMI.addActionListener(new AlchemyListener());
		gridMI.addActionListener(new GridListener());
		operationMI.addActionListener(new OperationListener());
		aboutMI.addActionListener(new AboutListener());
		shortcutMI.addActionListener(new ShortcutListener());
		secretMI.addActionListener(new SecretListener());
		
	}
	
	private class NewListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.resetCode(true);
		}
	}
	
	private class SaveListener0 implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.save(0);
		}
	}
	
	private class SaveListener1 implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.save(1);
		}
	}
	
	private class SaveListener2 implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.save(2);
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
	
	private class SecretListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			panel.secretCode();
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		boolean run = true;
		CaptchaGUI theGUI = new CaptchaGUI();
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
