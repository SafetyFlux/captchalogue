package main;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONObject;

import shape.Rect;
import utility.Conversion;
import utility.DigitValues;
import utility.Randomize;

public class CaptchaPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private ImageIcon captchaCard, card1, card2, symbol, symbolS;		// ImageIcons for the assets
	private ImageIcon record, mspa, gun, arrow, apple, weasel;			// ImageIcons for dialog box icons
	private Conversion cv = new Conversion();							// Class for conversion and operations
	private DigitValues dv = new DigitValues();							// Class for valid digit values
	private Randomize rand = new Randomize();							// Class for randomizing codes
	private Random gen = new Random();									// Random for... generating random numbers
	final JFileChooser fc = new JFileChooser();							// File chooser for loading codes
	// Integer arrays for code binaries
	private int[][] binary = new int[8][6];
	private int[][] alcBin1 = new int[8][6];
	private int[][] alcBin2 = new int[8][6];
	// Rect arrays for holes
	private Rect holes[] = new Rect[48];
	private Rect backHoles[] = new Rect[48];
	private Rect alcHoles1[] = new Rect[48];
	private Rect backHoles1[] = new Rect[48];
	private Rect alcHoles2[] = new Rect[48];
	private Rect backHoles2[] = new Rect[48];
	// Rect arrays for code digits
	private Rect entries[] = new Rect[8];
	private Rect alcEnt1[] = new Rect[8];
	private Rect alcEnt2[] = new Rect[8];
	// String arrays for code digits
	private String digits[] = new String[8];
	private String alcDig1[] = new String[8];
	private String alcDig2[] = new String[8];
	// All button rectangles
	private Rect buttonAND, buttonOR, buttonXOR, buttonNAND, buttonNOR, buttonXNOR, buttonNOT, buttonNOT1, buttonNOT2,
	fill, fill1, fill2, randomize, rand1, rand2;
	// All button border rectangles (black outline)
	private Rect borderAND, borderOR, borderXOR, borderNAND, borderNOR, borderXNOR, borderNOT, borderNOT1, borderNOT2,
	borderFill, borderFill1, borderFill2, borderRandomize, borderRand1, borderRand2;
	// All grid rectangle arrays
	private Rect mainGrid[] = new Rect[8];
	private Rect alcGrid1[] = new Rect[8];
	private Rect alcGrid2[] = new Rect[8];
	// Strings for codes, code updates, and the current operation
	private String code = "";
	private String alcCode1 = "";
	private String alcCode2 = "";
	private String codeUpdate = "";
	private String codeUpdate1 = "";
	private String codeUpdate2 = "";
	private String operation = "";
	// About page information
	private String author = "Safety";
	private String advisor = "DetectiveDyn";
	private String email = "dekuwither@gmail.com";
	private String link = "https://github.com/SafetyFlux/captchalogue";
	private String version = "1.4";
	// Shortcut information
	private String[] shortcuts = new String[] {
			"Ctrl + N  -  New", "Ctrl + Shift + N  -  New (No Prompt)", "Ctrl + S  -  Save Image", 
			"Ctrl + R  -  Randomize All Codes", "Ctrl + A  -  Toggle Alchemy", "Ctrl + G  -  Toggle Grids", 
			"Ctrl + O  -  Toggle Other Operations", "Ctrl + Y  -  Toggle Symbols", "Ctrl + T  -  About Page", 
			"Escape  -  Exit Program"
	};
	// Integer that tracks which code digit is being changed
	private int entryNo = -1;
	// Variables for Jade's wardrobifier
	private javax.swing.Timer wardrobifier = new javax.swing.Timer(15000, new WardrobeListener());
	private int jadeSym = 0;
	// Strings that track the current theme and type
	private String theme = "";
	private String type = "";
	// Booleans for each condition in the PaintComponent
	private boolean entryUpdate = false;
	private boolean alcEnt1Update = false;
	private boolean alcEnt2Update = false;
	private boolean updateHole = false;
	private boolean updateAlcHole1 = false;
	private boolean updateAlcHole2 = false;
	private boolean updateCode = false;
	private boolean updateAlcCode1 = false;
	private boolean updateAlcCode2 = false;
	private boolean alchemize = false;
	private boolean recolor = false;
	private boolean showAlcCards = false;
	private boolean showGrids = false;
	private boolean showSymbol = true;
	private boolean showOtherOps = false;
	// All fonts used; f is the main one
	private Font f = new Font("Courier New", Font.BOLD, 26);
	private Font fs = new Font("Courier New", Font.BOLD, 16);
	private Font ft = new Font("Courier New", Font.BOLD, 12);
	// Operation button (and highlight) colors
	private Color colAND = Color.CYAN;
	private Color colOR = Color.CYAN;
	private Color colXOR = Color.CYAN;
	private Color colNAND = Color.CYAN;
	private Color colNOR = Color.CYAN;
	private Color colXNOR = Color.CYAN;
	private Color highlight = new Color(50, 150, 150);

	public CaptchaPanel() throws FileNotFoundException, Exception {

		this.setBackground(Color.WHITE);
		// Set dialog box font and font size
		UIManager.put("OptionPane.messageFont", new Font("Courier New", Font.BOLD, 14));
		UIManager.put("OptionPane.buttonFont", new Font("Courier New", Font.BOLD, 18));
		// Load options from json file
		Scanner reader = new Scanner(new File("res/options.json"));
		JSONObject options = new JSONObject(reader.nextLine());
		if(options.has("theme"))
			theme = options.getString("theme");
		else
			theme = "Blue (John)";
		if(options.has("type"))
			type = options.getString("type");
		else
			type = "humans";
		if(options.has("jade"))
			jadeSym = options.getInt("jade");
		else
			jadeSym = 0;
		if(options.has("symbol"))
			showSymbol = options.getBoolean("symbol");
		else
			showSymbol = false;
		if(options.has("alchemy"))
			showAlcCards = options.getBoolean("alchemy");
		else
			showAlcCards = false;
		if(options.has("grids"))
			showGrids = options.getBoolean("grids");
		else
			showGrids = false;
		if(options.has("operations"))
			showOtherOps = options.getBoolean("operations");
		else
			showOtherOps = false;
		if(options.has("main_code"))
			code = options.getString("main_code");
		else
			code = "00000000";
		if(options.has("alchemy_code_1"))
			alcCode1 = options.getString("alchemy_code_1");
		else
			alcCode1 = "00000000";
		if(options.has("alchemy_code_2"))
			alcCode2 = options.getString("alchemy_code_2");
		else
			alcCode2 = "00000000";
		if(options.has("oper"))
			operation = options.getString("oper");
		else
			operation = "NONE";
		reader.close();
		// Call necessary methods
		loadRect();
		punchHole();
		changeCards();
		fixButtons();
		wardrobifier.start();
		// Select default captcha card assets
		captchaCard = new ImageIcon("images/" + type + "/CaptchaCard" + theme + ".png");
		card1 = rescaleImage(new File("images/" + type + "/CaptchaCard" + theme + ".png"), 226, 178);
		card2 = rescaleImage(new File("images/miscellaneous/CaptchaCardBlank.png"), 226, 178);
		if(theme.equals("Green (Jade)")) {
			symbol = new ImageIcon("images/" + type + "/Symbol" + theme + " " + jadeSym + ".png");
			symbolS = rescaleImage(new File("images/" + type + "/Symbol" + theme + " " + jadeSym + ".png"), 77, 90);
		}
		else {
			symbol = new ImageIcon("images/" + type + "/Symbol" + theme + ".png");
			symbolS = rescaleImage(new File("images/" + type + "/Symbol" + theme + ".png"), 77, 90);
		}
		// Select dialog box assets
		record = new ImageIcon("images/icons/Record.png");
		mspa = new ImageIcon("images/icons/MSPAFace.png");
		gun = new ImageIcon("images/icons/MSPAReader.png");
		arrow = new ImageIcon("images/icons/RightArrow.png");
		apple = new ImageIcon("images/icons/Apple.png");
		weasel = new ImageIcon("images/icons/Weasel.png");
		// Add GUI listeners
		addMouseListener(new HoleListener());
		addKeyListener(new EntryListener());
		addKeyListener(new ShortcutListener());
		setFocusable(true);

	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		// Set up the font and font color
		g.setFont(f);
		g.setColor(Color.BLACK);
		int symbolY = (captchaCard.getIconHeight() / 2) - (symbol.getIconHeight() / 2) + (symbol.getIconHeight() / 6);
		int symbolYS = (card2.getIconHeight() / 2) - (card2.getIconHeight() / 2) + (card2.getIconHeight() / 6);
		if(showAlcCards){
			// Add code digits
			for (int i = 0; i < digits.length; i++){
				digits[i] = "";
				digits[i] += code.charAt(i);
				g.drawString(digits[i], (423 + (47 * i)), 584);
			}
			g.setFont(fs);
			for (int i = 0; i < alcDig1.length; i++) {
				alcDig1[i] = "";
				alcDig1[i] += alcCode1.charAt(i);
				g.drawString(alcDig1[i], 46 + (30 * i), 292);
			}
			for (int i = 0; i < alcDig2.length; i++) {
				alcDig2[i] = "";
				alcDig2[i] += alcCode2.charAt(i);
				g.drawString(alcDig2[i], 46 + (30 * i), 587);
			}
		}
		else{
			for (int i = 0; i < digits.length; i++){
				digits[i] = "";
				digits[i] += code.charAt(i);
				g.drawString(digits[i], 41 + (47 * i), 584);
			}
		}
		g.setFont(f);
		// Set highlights
		if(operation.equals("AND"))
			colAND = highlight;
		else
			colAND = Color.CYAN;
		if(operation.equals("OR"))
			colOR = highlight;
		else
			colOR = Color.CYAN;
		if(operation.equals("XOR"))
			colXOR = highlight;
		else
			colXOR = Color.CYAN;
		if(operation.equals("NAND"))
			colNAND = highlight;
		else
			colNAND = Color.CYAN;
		if(operation.equals("NOR"))
			colNOR = highlight;
		else
			colNOR = Color.CYAN;
		if(operation.equals("XNOR"))
			colXNOR = highlight;
		else
			colXNOR = Color.CYAN;
		// Add captcha card asset
		if(showAlcCards){
			captchaCard.paintIcon(this, g, 421, 40);
			card1.paintIcon(this, g, 100, 40);
			card2.paintIcon(this, g, 100, 335);
			if(showSymbol) {
				symbol.paintIcon(this, g, 491, symbolY);
				symbolS.paintIcon(this, g, 135, 370 + symbolYS);
			}
		}
		else{
			captchaCard.paintIcon(this, g, 39, 40);
			if(showSymbol)
				symbol.paintIcon(this, g, 109, symbolY);
		}
		// Draw operation buttons
		if(showAlcCards){
			buttonAND.draw(g);
			buttonAND.setFilled(true);
			borderAND.draw(g);
			buttonOR.draw(g);
			buttonOR.setFilled(true);
			borderOR.draw(g);
			if(showOtherOps) {
				buttonXOR.draw(g);
				buttonXOR.setFilled(true);
				borderXOR.draw(g);
				buttonNAND.draw(g);
				buttonNAND.setFilled(true);
				borderNAND.draw(g);
				buttonNOR.draw(g);
				buttonNOR.setFilled(true);
				borderNOR.draw(g);
				buttonXNOR.draw(g);
				buttonXNOR.setFilled(true);
				borderXNOR.draw(g);
				buttonNOT1.draw(g);
				buttonNOT1.setFilled(true);
				borderNOT1.draw(g);
				buttonNOT2.draw(g);
				buttonNOT2.setFilled(true);
				borderNOT2.draw(g);
			}
			fill1.draw(g);
			fill1.setFilled(true);
			borderFill1.draw(g);
			fill2.draw(g);
			fill2.setFilled(true);
			borderFill2.draw(g);
			rand1.draw(g);
			rand1.setFilled(true);
			borderRand1.draw(g);
			rand2.draw(g);
			rand2.setFilled(true);
			borderRand2.draw(g);
		}
		if(showOtherOps) {
			buttonNOT.draw(g);
			buttonNOT.setFilled(true);
			borderNOT.draw(g);
		}
		// Draw other buttons
		fill.draw(g);
		fill.setFilled(true);
		borderFill.draw(g);
		randomize.draw(g);
		randomize.setFilled(true);
		borderRandomize.draw(g);
		if(showAlcCards){
			// Draw operation strings
			if(showOtherOps) {
				g.drawString("&&", 359, 251);
				g.drawString("||", 359, 282);
				g.drawString("^^", 360, 320);
				g.drawString("~&", 360, 347);
				g.drawString("~|", 361, 378);
				g.drawString("~^", 361, 413);
				g.drawString("~~", 791, 121);
				g.drawString("~~", 291, 64);
				g.drawString("~~", 291, 359);
			}
			else {
				g.drawString("&&", 359, 315);
				g.drawString("||", 359, 346);
			}
			// Draw other strings
			g.drawString("<", 807, 584);
			g.drawString("RAND", 751, 64);
			g.setFont(fs);
			g.drawString("<", 286, 292);
			g.drawString("<", 286, 587);
			g.drawString("R", 316, 292);
			g.drawString("R", 316, 587);
		}
		else{
			// Draw operation strings
			if(showOtherOps)
				g.drawString("~~", 403, 121);
			// Draw other strings
			g.drawString("<", 419, 584);
			g.drawString("RAND", 363, 64);
			g.setFont(f);
		}
		// Draw code digit rectangles
		for (int i = 0; i < entries.length; i++) {
			entries[i].draw(g);
			if(showAlcCards){
				alcEnt1[i].draw(g);
				alcEnt2[i].draw(g);
			}
		}
		// Draw grids
		if(showGrids){
			for (int i = 0; i < mainGrid.length; i++)
				mainGrid[i].draw(g);
			if(showAlcCards){
				for (int i = 0; i < alcGrid1.length; i++){
					alcGrid1[i].draw(g);
					alcGrid2[i].draw(g);
				}
				g.setFont(ft);
				g.drawString("1", 465, 135);
				g.drawString("2", 465, 462);
				g.drawString("3", 524, 135);
				g.drawString("4", 524, 462);
				g.drawString("5", 582, 135);
				g.drawString("6", 582, 462);
				g.drawString("7", 640, 135);
				g.drawString("8", 640, 462);
			}
			else{
				g.setFont(ft);
				g.drawString("1", 83, 135);
				g.drawString("2", 83, 462);
				g.drawString("3", 142, 135);
				g.drawString("4", 142, 462);
				g.drawString("5", 200, 135);
				g.drawString("6", 200, 462);
				g.drawString("7", 258, 135);
				g.drawString("8", 258, 462);
			}
			g.setFont(f);
		}
		// Draw holes
		for (int i = 0; i < holes.length; i++){
			backHoles[i].draw(g);
			//backHoles[i].setFilled(true);
			holes[i].draw(g);
			holes[i].setFilled(true);
		}
		if(showAlcCards){
			for (int i = 0; i < alcHoles1.length; i++){
				backHoles1[i].draw(g);
				alcHoles1[i].draw(g);
				alcHoles1[i].setFilled(true);
				backHoles2[i].draw(g);
				alcHoles2[i].draw(g);
				alcHoles2[i].setFilled(true);
			}
		}
		repaint();
		// Update the code when a hole is toggled
		if(updateHole){
			code = codeUpdate;
			recolor = true;
			codeUpdate = "";
			updateHole = false;
		}
		if(updateAlcHole1){
			alcCode1 = codeUpdate1;
			recolor = true;
			codeUpdate1 = "";
			updateAlcHole1 = false;
		}
		if(updateAlcHole2){
			alcCode2 = codeUpdate2;
			recolor = true;
			codeUpdate2 = "";
			updateAlcHole2 = false;
		}
		// Update the holes (and code) when the code is changed, filled, or randomized
		if(updateCode){
			code = codeUpdate;
			punchHole();
			recolor = true;
			codeUpdate = "";
			updateCode = false;
		}
		// Update the 1st alchemy code when it's changed, filled, or randomized
		if(updateAlcCode1){
			alcCode1 = codeUpdate1;
			if(showAlcCards)
				punchAlcHole1();
			recolor = true;
			codeUpdate1 = "";
			updateAlcCode1 = false;
		}
		// Update the 2nd alchemy code when it's changed, filled, or randomized
		if(updateAlcCode2){
			alcCode2 = codeUpdate2;
			if(showAlcCards)
				punchAlcHole2();
			recolor = true;
			codeUpdate2 = "";
			updateAlcCode2 = false;
		}
		// Update the holes (and set button highlight) when an operation is performed
		if(alchemize){
			punchHole();
			if(showAlcCards) {
				punchAlcHole1();
				punchAlcHole2();
			}
			recolor = true;
			alchemize = false;
		}
		// Repaint and save new options
		if(recolor){
			if(showOtherOps) {
				buttonAND = new Rect(351, 227, 48, 32, colAND);
				buttonOR = new Rect(351, 259, 48, 32, colOR);
				buttonXOR = new Rect(351, 291, 48, 32, colXOR);
				buttonNAND = new Rect(351, 323, 48, 32, colNAND);
				buttonNOR = new Rect(351, 355, 48, 32, colNOR);
				buttonXNOR = new Rect(351, 387, 48, 32, colXNOR);
			}
			else {
				buttonAND = new Rect(351, 291, 48, 32, colAND);
				buttonOR = new Rect(351, 323, 48, 32, colOR);
			}
			repaint();
			try {
				saveSettings("res/options.json");
			} catch (Exception e) {
				e.printStackTrace();
			}
			recolor = false;
		}
	}

	private class HoleListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			// Set variables for mouse position
			int mouseX = e.getX();
			int mouseY = e.getY();

			if(showAlcCards){
				// If the "&&" button is clicked, the AND operation is performed
				if(buttonAND.containsPoint(mouseX, mouseY)){
					fillBin();
					binary = cv.functionAND(alcBin1, alcBin2);
					if(operation.equals("AND"))
						operation = "NONE";
					else
						operation = "AND";
					updateHole = true;
					alchemize = true;
				}
				// If the "||" button is clicked, the OR operation is performed
				if(buttonOR.containsPoint(mouseX, mouseY)){
					fillBin();
					binary = cv.functionOR(alcBin1, alcBin2);
					if(operation.equals("OR"))
						operation = "NONE";
					else
						operation = "OR";
					updateHole = true;
					alchemize = true;
				}
				if(showOtherOps) {
					// If the "^^" button is clicked, the XOR operation is performed
					if(buttonXOR.containsPoint(mouseX, mouseY)){
						fillBin();
						binary = cv.functionXOR(alcBin1, alcBin2);
						if(operation.equals("XOR"))
							operation = "NONE";
						else
							operation = "XOR";
						updateHole = true;
						alchemize = true;
					}
					// If the "~&" button is clicked, the NAND operation is performed
					if(buttonNAND.containsPoint(mouseX, mouseY)){
						fillBin();
						binary = cv.functionNAND(alcBin1, alcBin2);
						if(operation.equals("NAND"))
							operation = "NONE";
						else
							operation = "NAND";
						updateHole = true;
						alchemize = true;
					}
					// If the "~|" button is clicked, the NOR operation is performed
					if(buttonNOR.containsPoint(mouseX, mouseY)){
						fillBin();
						binary = cv.functionNOR(alcBin1, alcBin2);
						if(operation.equals("NOR"))
							operation = "NONE";
						else
							operation = "NOR";
						updateHole = true;
						alchemize = true;
					}
					// If the "~^" button is clicked, the XNOR operation is performed
					if(buttonXNOR.containsPoint(mouseX, mouseY)){
						fillBin();
						binary = cv.functionXNOR(alcBin1, alcBin2);
						if(operation.equals("XNOR"))
							operation = "NONE";
						else
							operation = "XNOR";
						updateHole = true;
						alchemize = true;
					}
					// If the "~~" button is clicked, the NOT operation is performed (on the top code)
					if(buttonNOT1.containsPoint(mouseX, mouseY)){
						fillBin();
						alcBin1 = cv.functionNOT(alcBin1);
						resetHighlight();
						updateAlcHole1 = true;
						alchemize = true;
					}
					// If the "~~" button is clicked, the NOT operation is performed (on the bottom code)
					if(buttonNOT2.containsPoint(mouseX, mouseY)){
						fillBin();
						alcBin2 = cv.functionNOT(alcBin2);
						resetHighlight();
						updateAlcHole2 = true;
						alchemize = true;
					}
				}
				if(fill1.containsPoint(mouseX, mouseY)){
					boolean maintain = true;
					while(maintain) {
						String update = (String) JOptionPane.showInputDialog(null, "Enter Code", "Fill", JOptionPane.INFORMATION_MESSAGE,
								arrow, null, "00000000");
						if(update != null){
							codeUpdate1 = update.trim();
							// The code is set to 00000000 if the input isn't 8 digits long
							if(codeUpdate1.length() != 8) {
								codeUpdate1 = "00000000";
								errorMessage("badLength");
								maintain = true;
							}
							else {
								if(!cv.checkCode(codeUpdate1)) {
									errorMessage("badDigit");
									maintain = true;
								}
								else {
									updateAlcCode1 = true;
									resetHighlight();
									maintain = false;
								}
							}
						}
						else
							maintain = false;
					}
				}
				if(fill2.containsPoint(mouseX, mouseY)){
					boolean maintain = true;
					while(maintain) {
						String update = (String) JOptionPane.showInputDialog(null, "Enter Code", "Fill", JOptionPane.INFORMATION_MESSAGE,
								arrow, null, "00000000");
						if(update != null){
							codeUpdate2 = update.trim();
							// The code is set to 00000000 if the input isn't 8 digits long
							if(codeUpdate2.length() != 8) {
								codeUpdate2 = "00000000";
								errorMessage("badLength");
								maintain = true;
							}
							else {
								if(!cv.checkCode(codeUpdate2)) {
									errorMessage("badDigit");
									maintain = true;
								}
								else {
									updateAlcCode2 = true;
									resetHighlight();
									maintain = false;
								}
							}
						}
						else
							maintain = false;
					}
				}
				if(rand1.containsPoint(mouseX, mouseY)){
					codeUpdate1 = "";
					for (int i = 0; i < 8; i++)
						codeUpdate1 += rand.getChar();
					updateAlcCode1 = true;
					resetHighlight();
				}
				if(rand2.containsPoint(mouseX, mouseY)){
					codeUpdate2 = "";
					for (int i = 0; i < 8; i++)
						codeUpdate2 += rand.getChar();
					updateAlcCode2 = true;
					resetHighlight();
				}
			}
			// If the "~~" button is clicked, the NOT operation is performed (on the main code)
			if(showOtherOps) {
				if(buttonNOT.containsPoint(mouseX, mouseY)){
					fillBin();
					binary = cv.functionNOT(binary);
					resetHighlight();
					updateHole = true;
					alchemize = true;
				}
			}
			// If any of the "<" or "^" buttons are clicked, the corresponding field is filled in with the user input.
			if(fill.containsPoint(mouseX, mouseY)){
				boolean maintain = true;
				while(maintain) {
					String update = (String) JOptionPane.showInputDialog(null, "Enter Code", "Fill", JOptionPane.INFORMATION_MESSAGE,
							arrow, null, "00000000");
					if(update != null){
						codeUpdate = update.trim();
						// The code is set to 00000000 if the input isn't 8 digits long
						if(codeUpdate.length() != 8) {
							codeUpdate = "00000000";
							errorMessage("badLength");
							maintain = true;
						}
						else {
							if(!cv.checkCode(codeUpdate)) {
								errorMessage("badDigit");
								maintain = true;
							}
							else {
								updateCode = true;
								resetHighlight();
								maintain = false;
							}
						}
					}
					else
						maintain = false;
				}
			}
			// If any of the "RANDOMIZE" or "R" buttons are clicked, the corresponding code is randomized
			if(randomize.containsPoint(mouseX, mouseY)){
				codeUpdate = "";
				for (int i = 0; i < 8; i++)
					codeUpdate += rand.getChar();
				updateCode = true;
				resetHighlight();
			}

			// If a single digit is clicked, the location is logged and it can then be changed
			for (int i = 0; i < entries.length; i++) {
				Rect q = entries[i];
				if(q.containsPoint(mouseX, mouseY)){
					entryNo = i;
					resetHighlight();
					entryUpdate = true;
					break;
				}
			}
			if(showAlcCards){
				for (int i = 0; i < alcEnt1.length; i++) {
					Rect q = alcEnt1[i];
					if(q.containsPoint(mouseX, mouseY)){
						entryNo = i;
						resetHighlight();
						alcEnt1Update = true;
						break;
					}
				}
				for (int i = 0; i < alcEnt2.length; i++) {
					Rect q = alcEnt2[i];
					if(q.containsPoint(mouseX, mouseY)){
						entryNo = i;
						resetHighlight();
						alcEnt2Update = true;
						break;
					}
				}
			}

			// If a hole is clicked, it will toggle on or off
			for (int i = 0; i < holes.length; i++) {
				Rect r = backHoles[i];
				Rect h = holes[i];
				Rect s = alcHoles1[i];
				Rect v = backHoles1[i];
				Rect t = alcHoles2[i];
				Rect u = backHoles2[i];
				if(r.containsPoint(mouseX, mouseY)) {
					if(r.getColor() == Color.WHITE){
						r.setColor(Color.BLACK);
						h.setColor(Color.WHITE);
						binary[i / 6][i % 6] = 0;
					}
					else{
						r.setColor(Color.WHITE);
						h.setColor(Color.BLACK);
						binary[i / 6][i % 6] = 1;
					}
					resetHighlight();
					updateHole = true;
				}
				if(showAlcCards){
					if(v.containsPoint(mouseX, mouseY)) {
						// Toggle the hole when it's clicked
						if(v.getColor() == Color.WHITE){
							v.setColor(Color.BLACK);
							s.setColor(Color.WHITE);
							alcBin1[i / 6][i % 6] = 0;
						}
						else{
							v.setColor(Color.WHITE);
							s.setColor(Color.BLACK);
							alcBin1[i / 6][i % 6] = 1;
						}
						// Continue to alchemize when holes are toggled
						if(operation.equals("AND")) {
							binary = cv.functionAND(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(operation.equals("OR")) {
							binary = cv.functionOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(operation.equals("XOR")) {
							binary = cv.functionXOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(operation.equals("NAND")) {
							binary = cv.functionNAND(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(operation.equals("NOR")) {
							binary = cv.functionNOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(operation.equals("XNOR")) {
							binary = cv.functionXNOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						updateAlcHole1 = true;
					}
					if(u.containsPoint(mouseX, mouseY)) {
						// Toggle the hole when it's clicked
						if(u.getColor() == Color.WHITE){
							u.setColor(Color.BLACK);
							t.setColor(Color.WHITE);
							alcBin2[i / 6][i % 6] = 0;
						}
						else{
							u.setColor(Color.WHITE);
							t.setColor(Color.BLACK);
							alcBin2[i / 6][i % 6] = 1;
						}
						// Continue to alchemize when holes are toggled
						if(operation.equals("AND")) {
							binary = cv.functionAND(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(operation.equals("OR")) {
							binary = cv.functionOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(operation.equals("XOR")) {
							binary = cv.functionXOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(operation.equals("NAND")) {
							binary = cv.functionNAND(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(operation.equals("NOR")) {
							binary = cv.functionNOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(operation.equals("XNOR")) {
							binary = cv.functionXNOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						updateAlcHole2 = true;
					}
				}
			}

			// If a hole is clicked, the change is applied to the code
			if(updateHole){
				for (int i = 0; i < code.length(); i++) {
					int bin[] = new int[6];
					for (int j = 0; j < 6; j++)
						bin[j] = binary[i][j];
					codeUpdate += cv.binaryToDigit(bin);
				}
			}
			if(updateAlcHole1){
				for (int i = 0; i < alcCode1.length(); i++) {
					int bin[] = new int[6];
					for (int j = 0; j < 6; j++)
						bin[j] = alcBin1[i][j];
					codeUpdate1 += cv.binaryToDigit(bin);
				}
			}
			if(updateAlcHole2){
				for (int i = 0; i < alcCode2.length(); i++) {
					int bin[] = new int[6];
					for (int j = 0; j < 6; j++)
						bin[j] = alcBin2[i][j];
					codeUpdate2 += cv.binaryToDigit(bin);
				}
			}

		}
	}

	private class EntryListener extends KeyAdapter{

		// After a digit is clicked, the KeyListener looks for the next key typed
		public void keyTyped(KeyEvent e){

			char d = '0';
			// For the main code
			if(entryUpdate){
				// A few checks are in place to block action keys and allow shift to be held
				if(!e.isActionKey() && e.getKeyCode() != KeyEvent.VK_SHIFT)
					d = e.getKeyChar();
				else
					d = '0';
				// The program runs through the code again, only changing the selected digit
				if(dv.checkDigit(d)) {
					for (int i = 0; i < code.length(); i++) {
						if(i == entryNo)
							codeUpdate += d;
						else
							codeUpdate += code.charAt(i);
					}
					updateCode = true;
				}
				else
					errorMessage("badDigit");
				entryUpdate = false;
			}
			// For the alchemy codes
			else if(alcEnt1Update){
				if(!e.isActionKey() && e.getKeyCode() != KeyEvent.VK_SHIFT)
					d = e.getKeyChar();
				else
					d = '0';
				if(dv.checkDigit(d)) {
					for (int i = 0; i < alcCode1.length(); i++) {
						if(i == entryNo)
							codeUpdate1 += d;
						else
							codeUpdate1 += alcCode1.charAt(i);
					}
					updateAlcCode1 = true;
				}
				else
					errorMessage("badDigit");
				alcEnt1Update = false;
			}
			else if(alcEnt2Update){
				if(!e.isActionKey() && e.getKeyCode() != KeyEvent.VK_SHIFT)
					d = e.getKeyChar();
				else
					d = '0';
				if(dv.checkDigit(d)) {
					for (int i = 0; i < alcCode2.length(); i++) {
						if(i == entryNo)
							codeUpdate2 += d;
						else
							codeUpdate2 += alcCode2.charAt(i);
					}
					updateAlcCode2 = true;
				}
				else
					errorMessage("badDigit");
				alcEnt2Update = false;
			}
			else
				e.consume();

		}

	}

	// Keyboard Listener for all shortcuts
	private class ShortcutListener extends KeyAdapter {

		public void keyPressed(KeyEvent e) {

			if(e.isControlDown()) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_N:
					if(e.isShiftDown())
						resetCode(false);
					else
						resetCode(true);
					break;
				case KeyEvent.VK_S:
					savePrompt();
					break;
				case KeyEvent.VK_R:
					codeUpdate = "";
					for (int i = 0; i < 8; i++)
						codeUpdate += rand.getChar();
					updateCode = true;
					codeUpdate1 = "";
					if(showAlcCards) {
						for (int i = 0; i < 8; i++)
							codeUpdate1 += rand.getChar();
						updateAlcCode1 = true;
						codeUpdate2 = "";
						for (int i = 0; i < 8; i++)
							codeUpdate2 += rand.getChar();
						updateAlcCode2 = true;
					}
					resetHighlight();
					break;
				case KeyEvent.VK_A:
					changeSettings("Toggle Alchemy");
					break;
				case KeyEvent.VK_G:
					changeSettings("Toggle Grids");
					break;
				case KeyEvent.VK_O:
					changeSettings("Toggle Other Operations");
					break;
				case KeyEvent.VK_Y:
					changeSettings("Toggle Symbol");
					break;
				case KeyEvent.VK_T:
					changeSettings("About");
					break;
				default:
					e.consume();
					break;
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
				System.exit(0);
			else
				e.consume();

		}

	}

	private void punchHole(){
		// The holes are updated when the code is changed
		for (int i = 0; i < binary.length; i++) {
			int[] c = cv.digitToBinary(code.charAt(i));
			for (int j = 0; j < binary[i].length; j++)
				binary[i][j] = c[j];
		}
		for (int i = 0; i < binary.length; i++) {
			for (int j = 0; j < binary[i].length; j++) {
				int h = (6 * i) + j;
				if(binary[i][j] == 0) {
					holes[h].setColor(Color.WHITE);
					backHoles[h].setColor(Color.BLACK);
				}
				else {
					holes[h].setColor(Color.BLACK);
					backHoles[h].setColor(Color.WHITE);
				}

			}
		}
	}

	private void punchAlcHole1(){
		// The holes are updated when the code is changed
		for (int i = 0; i < alcBin1.length; i++) {
			int[] c = cv.digitToBinary(alcCode1.charAt(i));
			for (int j = 0; j < alcBin1[i].length; j++)
				alcBin1[i][j] = c[j];
		}
		for (int i = 0; i < alcBin1.length; i++) {
			for (int j = 0; j < alcBin1[i].length; j++) {
				int h = (6 * i) + j;
				if(alcBin1[i][j] == 0) {
					alcHoles1[h].setColor(Color.WHITE);
					backHoles1[h].setColor(Color.BLACK);
				}
				else {
					alcHoles1[h].setColor(Color.BLACK);
					backHoles1[h].setColor(Color.WHITE);
				}

			}
		}
	}

	private void punchAlcHole2(){
		// The holes are updated when the code is changed
		for (int i = 0; i < alcBin2.length; i++) {
			int[] c = cv.digitToBinary(alcCode2.charAt(i));
			for (int j = 0; j < alcBin2[i].length; j++)
				alcBin2[i][j] = c[j];
		}
		for (int i = 0; i < alcBin2.length; i++) {
			for (int j = 0; j < alcBin2[i].length; j++) {
				int h = (6 * i) + j;
				if(alcBin2[i][j] == 0) {
					alcHoles2[h].setColor(Color.WHITE);
					backHoles2[h].setColor(Color.BLACK);
				}
				else {
					alcHoles2[h].setColor(Color.BLACK);
					backHoles2[h].setColor(Color.WHITE);
				}

			}
		}
	}

	private void fillBin(){
		// The alchemy code binary arrays are updated when the codes are changed
		for (int i = 0; i < alcBin1.length; i++) {
			int[] c = cv.digitToBinary(alcCode1.charAt(i));
			for (int j = 0; j < alcBin1[i].length; j++)
				alcBin1[i][j] = c[j];
		}
		for (int i = 0; i < alcBin2.length; i++) {
			int[] c = cv.digitToBinary(alcCode2.charAt(i));
			for (int j = 0; j < alcBin2[i].length; j++)
				alcBin2[i][j] = c[j];
		}
	}

	private void loadRect(){
		// Load button rectangles
		buttonNOT = new Rect(394, 97, 48, 32, new Color(175, 165, 255));
		fill = new Rect(410, 560, 32, 32, Color.green);
		randomize = new Rect(346, 40, 96, 32, Color.yellow);
		// Load button borders (black outline)
		borderNOT = new Rect(394, 97, 48, 32, Color.BLACK);
		borderFill = new Rect(410, 560, 32, 32, Color.BLACK);
		borderRandomize = new Rect(346, 40, 96, 32, Color.BLACK);
		// Load entry rectangles
		for (int i = 0; i < entries.length; i++)
			entries[i] = new Rect((32 + (47 * i)), 560, 32, 32, Color.BLACK);
		// Load grids
		mainGrid[0] = new Rect(79, 123, 59, 172, Color.BLACK);
		mainGrid[1] = new Rect(79, 295, 59, 170, Color.BLACK);
		mainGrid[2] = new Rect(138, 123, 58, 172, Color.BLACK);
		mainGrid[3] = new Rect(138, 295, 58, 170, Color.BLACK);
		mainGrid[4] = new Rect(196, 123, 58, 172, Color.BLACK);
		mainGrid[5] = new Rect(196, 295, 58, 170, Color.BLACK);
		mainGrid[6] = new Rect(254, 123, 58, 172, Color.BLACK);
		mainGrid[7] = new Rect(254, 295, 58, 170, Color.BLACK);
		// Load hole rectangles and backgrounds
		for (int i = 0; i < holes.length; i++){
			int x = 0;
			int y = 0;
			
			switch(i / 12) {
			case 0:
				x = 94;
				break;
			case 1:
				x = 152;
				break;
			case 2:
				x = 210;
				break;
			case 3:
				x = 269;
				break;
			}
			
			switch(i % 12) {
			case 0:
				y = 138;
				break;
			case 1:
				y = 166;
				break;
			case 2:
				y = 193;
				break;
			case 3:
				y = 221;
				break;
			case 4:
				y = 248;
				break;
			case 5:
				y = 276;
				break;
			case 6:
				y = 303;
				break;
			case 7:
				y = 331;
				break;
			case 8:
				y = 356;
				break;
			case 9:
				y = 384;
				break;
			case 10:
				y = 411;
				break;
			case 11:
				y = 439;
				break;
			}

			holes[i] = new Rect(x, y, 29, 11, Color.WHITE);
			backHoles[i] = new Rect(x - 1, y - 1, 30, 12, Color.BLACK);
		}

	}

	// Change rectangle positions when alchemy cards are toggled
	private void changeCards(){
		if(showAlcCards){
			// Load button rectangles
			if(showOtherOps) {
				buttonAND = new Rect(351, 227, 48, 32, colAND);
				buttonOR = new Rect(351, 259, 48, 32, colOR);
			}
			else {
				buttonAND = new Rect(351, 291, 48, 32, colAND);
				buttonOR = new Rect(351, 323, 48, 32, colOR);
			}
			buttonXOR = new Rect(351, 291, 48, 32, colXOR);
			buttonNAND = new Rect(351, 323, 48, 32, colNAND);
			buttonNOR = new Rect(351, 355, 48, 32, colNOR);
			buttonXNOR = new Rect(351, 387, 48, 32, colXNOR);
			buttonNOT = new Rect(782, 97, 48, 32, new Color(175, 165, 255));
			buttonNOT1 = new Rect(282, 40, 48, 32, new Color(175, 165, 255));
			buttonNOT2 = new Rect(282, 335, 48, 32, new Color(175, 165, 255));
			fill = new Rect(798, 560, 32, 32, Color.green);
			fill1 = new Rect(280, 277, 20, 20, Color.green);
			fill2 = new Rect(280, 572, 20, 20, Color.green);
			randomize = new Rect(734, 40, 96, 32, Color.yellow);
			rand1 = new Rect(310, 277, 20, 20, Color.yellow);
			rand2 = new Rect(310, 572, 20, 20, Color.yellow);
			// Load button borders (black outline)
			if(showOtherOps) {
				borderAND = new Rect(351, 227, 48, 32, Color.BLACK);
				borderOR = new Rect(351, 259, 48, 32, Color.BLACK);
			}
			else {
				borderAND = new Rect(351, 291, 48, 32, Color.BLACK);
				borderOR = new Rect(351, 323, 48, 32, Color.BLACK);
			}
			borderXOR = new Rect(351, 291, 48, 32, Color.BLACK);
			borderNAND = new Rect(351, 323, 48, 32, Color.BLACK);
			borderNOR = new Rect(351, 355, 48, 32, Color.BLACK);
			borderXNOR = new Rect(351, 387, 48, 32, Color.BLACK);
			borderNOT = new Rect(782, 97, 48, 32, Color.BLACK);
			borderNOT1 = new Rect(282, 40, 48, 32, Color.BLACK);
			borderNOT2 = new Rect(282, 335, 48, 32, Color.BLACK);
			borderFill = new Rect(798, 560, 32, 32, Color.BLACK);
			borderFill1 = new Rect(280, 277, 20, 20, Color.BLACK);
			borderFill2 = new Rect(280, 572, 20, 20, Color.BLACK);
			borderRandomize = new Rect(734, 40, 96, 32, Color.BLACK);
			borderRand1 = new Rect(310, 277, 20, 20, Color.BLACK);
			borderRand2 = new Rect(310, 572, 20, 20, Color.BLACK);
			// Load entry rectangles
			for (int i = 0; i < entries.length; i++)
				entries[i] = new Rect((414 + (47 * i)), 560, 32, 32, Color.BLACK);
			for (int i = 0; i < entries.length; i++)
				alcEnt1[i] = new Rect(40 + (30 * i), 277, 20, 20, Color.BLACK);
			for (int i = 0; i < entries.length; i++)
				alcEnt2[i] = new Rect(40 + (30 * i), 572, 20, 20, Color.BLACK);
			// Load main grids
			for (int i = 0; i < mainGrid.length; i++)
				mainGrid[i].move(382, 0);
			// Load alchemy card 1 grids
			alcGrid1[0] = new Rect(119, 80, 25, 75, Color.BLACK);
			alcGrid1[1] = new Rect(119, 155, 25, 72, Color.BLACK);
			alcGrid1[2] = new Rect(144, 80, 25, 75, Color.BLACK);
			alcGrid1[3] = new Rect(144, 155, 25, 72, Color.BLACK);
			alcGrid1[4] = new Rect(169, 80, 25, 75, Color.BLACK);
			alcGrid1[5] = new Rect(169, 155, 25, 72, Color.BLACK);
			alcGrid1[6] = new Rect(194, 80, 25, 75, Color.BLACK);
			alcGrid1[7] = new Rect(194, 155, 25, 72, Color.BLACK);
			// Load alchemy card 2 grids
			alcGrid2[0] = new Rect(119, 375, 25, 75, Color.BLACK);
			alcGrid2[1] = new Rect(119, 450, 25, 72, Color.BLACK);
			alcGrid2[2] = new Rect(144, 375, 25, 75, Color.BLACK);
			alcGrid2[3] = new Rect(144, 450, 25, 72, Color.BLACK);
			alcGrid2[4] = new Rect(169, 375, 25, 75, Color.BLACK);
			alcGrid2[5] = new Rect(169, 450, 25, 72, Color.BLACK);
			alcGrid2[6] = new Rect(194, 375, 25, 75, Color.BLACK);
			alcGrid2[7] = new Rect(194, 450, 25, 72, Color.BLACK);
			// Load main hole rectangles and backgrounds
			for (int i = 0; i < holes.length; i++){
				holes[i].move(382, 0);
				backHoles[i].move(382, 0);
			}
			int x = 125;
			int y = 86;
			// Load alchemy card hole rectangles
			for (int i = 0; i < alcHoles1.length; i++){
				alcHoles1[i] = new Rect(x, y, 12, 4, Color.WHITE);
				backHoles1[i] = new Rect(x - 1, y - 1, 13, 5, Color.BLACK);
				alcHoles2[i] = new Rect(x, y + 295, 12, 4, Color.WHITE);
				backHoles2[i] = new Rect(x - 1, y + 294, 13, 5, Color.BLACK);
				if(i % 12 == 11){
					x += 25;
					y = 86;
				}
				else
					y += 12;
			}

		}
		else
			loadRect();
		if(showAlcCards){
			punchAlcHole1();
			punchAlcHole2();
		}
		punchHole();
		recolor = true;
	}

	// Fix the operation buttons when some are toggled
	private void fixButtons() {
		if(showAlcCards) {
			if(showOtherOps) {
				buttonAND = new Rect(351, 227, 48, 32, colAND);
				buttonOR = new Rect(351, 259, 48, 32, colOR);
				borderAND = new Rect(351, 227, 48, 32, Color.BLACK);
				borderOR = new Rect(351, 259, 48, 32, Color.BLACK);
			}
			else {
				buttonAND = new Rect(351, 291, 48, 32, colAND);
				buttonOR = new Rect(351, 323, 48, 32, colOR);
				borderAND = new Rect(351, 291, 48, 32, Color.BLACK);
				borderOR = new Rect(351, 323, 48, 32, Color.BLACK);
			}
			buttonXOR = new Rect(351, 291, 48, 32, colXOR);
			buttonNAND = new Rect(351, 323, 48, 32, colNAND);
			buttonNOR = new Rect(351, 355, 48, 32, colNOR);
			buttonXNOR = new Rect(351, 387, 48, 32, colXNOR);
			borderXOR = new Rect(351, 291, 48, 32, Color.BLACK);
			borderNAND = new Rect(351, 323, 48, 32, Color.BLACK);
			borderNOR = new Rect(351, 355, 48, 32, Color.BLACK);
			borderXNOR = new Rect(351, 387, 48, 32, Color.BLACK);
		}
	}

	// Reset all codes to 00000000
	protected void resetCode(boolean prompt){
		if(prompt) {
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset?", "Reset Codes",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, gun);
			if(confirm == JOptionPane.YES_OPTION){
				code = "00000000";
				alcCode1 = "00000000";
				alcCode2 = "00000000";
				punchHole();
				if(showAlcCards){
					punchAlcHole1();
					punchAlcHole2();
				}
				resetHighlight();
				recolor = true;
			}
		}
		else {
			code = "00000000";
			alcCode1 = "00000000";
			alcCode2 = "00000000";
			punchHole();
			if(showAlcCards){
				punchAlcHole1();
				punchAlcHole2();
			}
			resetHighlight();
			recolor = true;
		}
	}

	// Show save prompt
	protected void savePrompt() {
		// Set variables
		BufferedImage saveImg = new BufferedImage(captchaCard.getIconWidth(), captchaCard.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = saveImg.getGraphics();
		ImageIcon saveSymbol;
		boolean paintSymbol;
		
		// Select symbol for the saved image
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter imgFilter = new FileNameExtensionFilter("PNG & JPG Images", "png", "jpg");
		fc.setFileFilter(imgFilter);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setCurrentDirectory(new File("images"));
		fc.setDialogTitle("Choose Symbol");
		int sel = fc.showOpenDialog(this);
		if(sel == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			saveSymbol = new ImageIcon(file.toString());
			int newHeight = saveSymbol.getIconHeight() * (saveSymbol.getIconWidth() / 200);
			saveSymbol = rescaleImage(file, newHeight, 200);
			paintSymbol = true;
		}
		// If "Cancel" is pressed, the symbol isn't included on the card
		else {
			saveSymbol = symbol;
			paintSymbol = false;
		}
		
		// Set the symbol position, then paint the card and symbol
		int symbolY = (captchaCard.getIconHeight() / 2) - (saveSymbol.getIconHeight() / 2) + (saveSymbol.getIconHeight() / 6);
		captchaCard.paintIcon(this, g, 0, 0);
		if(paintSymbol)
			saveSymbol.paintIcon(this, g, 70, symbolY - 40);
		
		// Determine hole positions and paint the filled ones
		for (int i = 0; i < holes.length; i++){
			int x = 0;
			int y = 0;
			
			switch(i / 12) {
			case 0:
				x = 55;
				break;
			case 1:
				x = 113;
				break;
			case 2:
				x = 171;
				break;
			case 3:
				x = 230;
				break;
			}
			
			switch(i % 12) {
			case 0:
				y = 98;
				break;
			case 1:
				y = 126;
				break;
			case 2:
				y = 153;
				break;
			case 3:
				y = 181;
				break;
			case 4:
				y = 208;
				break;
			case 5:
				y = 236;
				break;
			case 6:
				y = 263;
				break;
			case 7:
				y = 291;
				break;
			case 8:
				y = 316;
				break;
			case 9:
				y = 344;
				break;
			case 10:
				y = 371;
				break;
			case 11:
				y = 399;
				break;
			}
			
			if(holes[i].getColor() == Color.BLACK) {
				g.setColor(Color.BLACK);
				g.fillRect(x, y, 29, 11);
			}
		}
		
		// Set filename
		String filename = (String) JOptionPane.showInputDialog(null, "Set Filename", "Save Card", 
				JOptionPane.INFORMATION_MESSAGE, record, null, "card");
		if(filename == null)
			filename = "card";
		
		// Show preview with option to cancel
		JLabel lbl = new JLabel(new ImageIcon(saveImg));
		String[] previewOptions = {"Save", "Cancel"};
		int option = JOptionPane.showOptionDialog(null, lbl, "Preview", 0, 
				JOptionPane.INFORMATION_MESSAGE, record, previewOptions, null);
		if(option == 0)
			saveImage(saveImg, filename);
		else
			JOptionPane.showMessageDialog(null, "Save canceled.", "Canceled", 
					JOptionPane.INFORMATION_MESSAGE, record);
		
	}
	
	// Save the main card as an image
	private void saveImage(BufferedImage img, String fn) {
		try {
			File dir = new File("saves");
			if(!dir.exists())
				dir.mkdir();
	        ImageIO.write(img, "png", new File("saves/" + fn + ".png"));
	        JOptionPane.showMessageDialog(null, "The card has been saved! Check your saves folder.", "Saved",
					  JOptionPane.INFORMATION_MESSAGE, record);
	    } catch (IOException ex) {
	        
	    }
	}

	// Show an error message
	private void errorMessage(String err) {
		if(err.equals("badLength"))
			JOptionPane.showMessageDialog(null, "Error: Code must be 8 digits long.", "Error", 
					JOptionPane.INFORMATION_MESSAGE, weasel);
		else if(err.equals("badDigit"))
			JOptionPane.showMessageDialog(null, "Error: Code contains an invalid digit.", "Error", 
					JOptionPane.INFORMATION_MESSAGE, weasel);
	}

	// Change the theme
	protected void changeTheme(String th, String ty){
		// Since image files are named after the themes, the ImageIcons can be changed with one line of code each
		captchaCard = new ImageIcon("images/" + ty + "/CaptchaCard" + th + ".png");
		card1 = rescaleImage(new File("images/" + ty + "/CaptchaCard" + th + ".png"), 226, 178);
		card2 = rescaleImage(new File("images/miscellaneous/CaptchaCardBlank.png"), 226, 178);
		if(th.equals("Green (Jade)")) {
			symbol = new ImageIcon("images/" + ty + "/Symbol" + th + " " + jadeSym + ".png");
			symbolS = rescaleImage(new File("images/" + ty + "/Symbol" + th + " " + jadeSym + ".png"), 77, 90);
		}
		else {
			symbol = new ImageIcon("images/" + ty + "/Symbol" + th + ".png");
			symbolS = rescaleImage(new File("images/" + ty + "/Symbol" + th + ".png"), 77, 90);
		}
		theme = th;
		type = ty;
		recolor = true;
	}

	// Jade's wardrobifier run method
	private class WardrobeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				jadeSym = gen.nextInt(10);
				if(theme.equals("Green (Jade)"))
					changeTheme("Green (Jade)", "humans");
			}catch(Exception ex) {}
		}
	}
	
	// Change various settings
	protected void changeSettings(String opt){
		switch(opt) {
		// For the Toggle Alchemy option
		case "Toggle Alchemy":
			showAlcCards = !showAlcCards;
			changeCards();
			break;
		// For the Toggle Grids option
		case "Toggle Grids":
			showGrids = !showGrids;
			recolor = true;
			break;
		// For the Toggle Other Operations button
		case "Toggle Other Operations":
			showOtherOps = !showOtherOps;
			fixButtons();
			recolor = true;
			break;
		// For the Toggle Symbol option
		case "Toggle Symbol":
			showSymbol = !showSymbol;
			recolor = true;
			break;
		// For the About menu
		case "About":
			JOptionPane.showMessageDialog(null, "Programmer: " + author + "\nAdvisor: " + advisor +
					"\nEmail: " + email + "\nGitHub Page: " + link + "\nVersion: " +
					version, "About", JOptionPane.INFORMATION_MESSAGE, mspa);
			break;
		// For the Shortcuts menu
		case "Shortcuts":
			String sc = "";
			for(int i = 0; i < shortcuts.length; i += 2)
				sc += String.format("%-45s%s%n", shortcuts[i], shortcuts[i + 1]);
			JOptionPane.showMessageDialog(null, sc, "Shortcuts", JOptionPane.INFORMATION_MESSAGE, apple);
			break;
		}
	}

	// Save options to json file
	protected void saveSettings(String filename) throws Exception{
		JSONObject options = new JSONObject();
		options.put("theme", theme);
		options.put("type", type);
		options.put("jade", jadeSym);
		options.put("symbol", showSymbol);
		options.put("alchemy", showAlcCards);
		options.put("grids", showGrids);
		options.put("operations", showOtherOps);
		options.put("main_code", code);
		options.put("alchemy_code_1", alcCode1);
		options.put("alchemy_code_2", alcCode2);
		options.put("oper", operation);
		Files.write(Paths.get(filename), options.toString().getBytes());
	}

	// Reset operation button colors (remove any highlights)
	private void resetHighlight(){
		operation = "NONE";
		recolor = true;
	}

	// Get the GUI width to change in the GUIWindow program
	public boolean getCards(){
		return showAlcCards;
	}
	
	// Resize an image
	public ImageIcon rescaleImage(File source, int maxHeight, int maxWidth)
	{
	    int newHeight = 0, newWidth = 0;        // Variables for the new height and width
	    int priorHeight = 0, priorWidth = 0;
	    BufferedImage image = null;
	    ImageIcon sizeImage;

	    try {
	            image = ImageIO.read(source);        // get the image
	    } catch (Exception e) {

	            e.printStackTrace();
	            System.out.println("Picture upload attempted & failed");
	    }

	    sizeImage = new ImageIcon(image);

	    if(sizeImage != null)
	    {
	        priorHeight = sizeImage.getIconHeight(); 
	        priorWidth = sizeImage.getIconWidth();
	    }

	    // Calculate the correct new height and width
	    if((float)priorHeight/(float)priorWidth > (float)maxHeight/(float)maxWidth)
	    {
	        newHeight = maxHeight;
	        newWidth = (int)(((float)priorWidth/(float)priorHeight)*(float)newHeight);
	    }
	    else 
	    {
	        newWidth = maxWidth;
	        newHeight = (int)(((float)priorHeight/(float)priorWidth)*(float)newWidth);
	    }


	    // Resize the image

	    // 1. Create a new Buffered Image and Graphic2D object
	    BufferedImage resizedImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    // 2. Use the Graphic object to draw a new image to the image in the buffer
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(image, 0, 0, newWidth, newHeight, null);
	    g2.dispose();

	    // 3. Convert the buffered image into an ImageIcon for return
	    return (new ImageIcon(resizedImg));
	}

}
