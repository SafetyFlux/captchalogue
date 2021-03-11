import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONObject;

public class CaptchaPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private ImageIcon captchaCard, card1, card2, symbol;				// ImageIcons for the assets
	private ImageIcon errIcon, pumpkin, record, mspa, gun,				// ImageIcons for dialog box icons
	arrow, apple, weasel;
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
	private Rect alcHoles2[] = new Rect[48];
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
	// Strings for codes and code updates
	private String code = "";
	private String alcCode1 = "";
	private String alcCode2 = "";
	private String codeUpdate = "";
	private String codeUpdate1 = "";
	private String codeUpdate2 = "";
	// About page information
	private String author = "Safety";
	private String advisor = "DetectiveDyn";
	private String email = "dekuwither@gmail.com";
	private String link = "https://github.com/SafetyFlux/captchalogue";
	private String version = "1.2.1";
	// Integer that tracks which code digit is being changed
	private int entryNo = -1;
	// Strings that tracks the current theme
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
	private boolean highlightAND = false;
	private boolean highlightOR = false;
	private boolean highlightXOR = false;
	private boolean highlightNAND = false;
	private boolean highlightNOR = false;
	private boolean highlightXNOR = false;
	// All fonts used; f is the main one
	private Font f = new Font("Courier", Font.BOLD, 26);
	private Font fs = new Font("Courier", Font.BOLD, 16);
	private Font ft = new Font("Courier", Font.BOLD, 12);
	// Operation button (and highlight) colors
	private Color colAND = Color.cyan;
	private Color colOR = Color.cyan;
	private Color colXOR = Color.cyan;
	private Color colNAND = Color.cyan;
	private Color colNOR = Color.cyan;
	private Color colXNOR = Color.cyan;
	private Color highlight = new Color(50, 150, 150);

	public CaptchaPanel() throws FileNotFoundException, Exception {

		this.setBackground(Color.white);
		// Load options from json file
		Scanner reader = new Scanner(new File("res/options.json"));
		JSONObject options = new JSONObject(reader.nextLine());
		theme = options.getString("theme");
		type = options.getString("type");
		showSymbol = options.getBoolean("symbol");
		showAlcCards = options.getBoolean("alchemy");
		showGrids = options.getBoolean("grids");
		showOtherOps = options.getBoolean("operations");
		code = options.getString("main_code");
		alcCode1 = options.getString("alchemy_code_1");
		alcCode2 = options.getString("alchemy_code_2");
		highlightAND = options.getBoolean("function_and");
		highlightOR = options.getBoolean("function_or");
		highlightXOR = options.getBoolean("function_xor");
		highlightNAND = options.getBoolean("function_nand");
		highlightNOR = options.getBoolean("function_nor");
		highlightXNOR = options.getBoolean("function_xnor");
		reader.close();
		// Call necessary methods
		loadRect();
		punchHole();
		changeCards();
		fixButtons();
		// Select default captcha card assets
		captchaCard = new ImageIcon("images/" + type + "/CaptchaCard" + theme + ".png");
		card1 = new ImageIcon("images/" + type + "/SmallCard" + theme + ".png");
		card2 = new ImageIcon("images/" + type + "/SmallCard" + theme + ".png");
		symbol = new ImageIcon("images/" + type + "/Symbol" + theme + ".png");
		// Select dialog box assets
		pumpkin = new ImageIcon("images/icons/WhatPumpkin.png");
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
		g.setColor(Color.black);
		int symbolY = (captchaCard.getIconHeight() / 2) - (symbol.getIconHeight() / 2) + (symbol.getIconHeight() / 6);
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
		if(highlightAND)
			colAND = highlight;
		else
			colAND = Color.cyan;
		if(highlightOR)
			colOR = highlight;
		else
			colOR = Color.cyan;
		if(highlightXOR)
			colXOR = highlight;
		else
			colXOR = Color.cyan;
		if(highlightNAND)
			colNAND = highlight;
		else
			colNAND = Color.cyan;
		if(highlightNOR)
			colNOR = highlight;
		else
			colNOR = Color.cyan;
		if(highlightXNOR)
			colXNOR = highlight;
		else
			colXNOR = Color.cyan;
		// Add captcha card asset
		if(showAlcCards){
			captchaCard.paintIcon(this, g, 421, 40);
			card1.paintIcon(this, g, 100, 40);
			card2.paintIcon(this, g, 100, 335);
			if(showSymbol)
				symbol.paintIcon(this, g, 491, symbolY);
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
				alcHoles1[i].draw(g);
				alcHoles2[i].draw(g);
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
			punchAlcHole1();
			punchAlcHole2();
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
					highlightAND = !highlightAND;
					highlightOR = false;
					highlightXOR = false;
					highlightNAND = false;
					highlightNOR = false;
					highlightXNOR = false;
					updateHole = true;
					alchemize = true;
				}
				// If the "||" button is clicked, the OR operation is performed
				if(buttonOR.containsPoint(mouseX, mouseY)){
					fillBin();
					binary = cv.functionOR(alcBin1, alcBin2);
					highlightAND = false;
					highlightOR = !highlightOR;
					highlightXOR = false;
					highlightNAND = false;
					highlightNOR = false;
					highlightXNOR = false;
					updateHole = true;
					alchemize = true;
				}
				if(showOtherOps) {
					// If the "^^" button is clicked, the XOR operation is performed
					if(buttonXOR.containsPoint(mouseX, mouseY)){
						fillBin();
						binary = cv.functionXOR(alcBin1, alcBin2);
						highlightAND = false;
						highlightOR = false;
						highlightXOR = !highlightXOR;
						highlightNAND = false;
						highlightNOR = false;
						highlightXNOR = false;
						updateHole = true;
						alchemize = true;
					}
					// If the "~&" button is clicked, the NAND operation is performed
					if(buttonNAND.containsPoint(mouseX, mouseY)){
						fillBin();
						binary = cv.functionNAND(alcBin1, alcBin2);
						highlightAND = false;
						highlightOR = false;
						highlightXOR = false;
						highlightNAND = !highlightNAND;
						highlightNOR = false;
						highlightXNOR = false;
						updateHole = true;
						alchemize = true;
					}
					// If the "~|" button is clicked, the NOR operation is performed
					if(buttonNOR.containsPoint(mouseX, mouseY)){
						fillBin();
						binary = cv.functionNOR(alcBin1, alcBin2);
						highlightAND = false;
						highlightOR = false;
						highlightXOR = false;
						highlightNAND = false;
						highlightNOR = !highlightNOR;
						highlightXNOR = false;
						updateHole = true;
						alchemize = true;
					}
					// If the "~^" button is clicked, the XNOR operation is performed
					if(buttonXNOR.containsPoint(mouseX, mouseY)){
						fillBin();
						binary = cv.functionXNOR(alcBin1, alcBin2);
						highlightAND = false;
						highlightOR = false;
						highlightXOR = false;
						highlightNAND = false;
						highlightNOR = false;
						highlightXNOR = !highlightXNOR;
						updateHole = true;
						alchemize = true;
					}
					// If the "~~" button is clicked, the NOT operation is performed (on the main code)
					if(buttonNOT.containsPoint(mouseX, mouseY)){
						fillBin();
						binary = cv.functionNOT(binary);
						resetHighlight();
						updateHole = true;
						alchemize = true;
					}
					// If the "~~" button is clicked, the NOT operation is performed (on the main code)
					if(buttonNOT1.containsPoint(mouseX, mouseY)){
						fillBin();
						alcBin1 = cv.functionNOT(alcBin1);
						resetHighlight();
						updateAlcHole1 = true;
						alchemize = true;
					}
					// If the "~~" button is clicked, the NOT operation is performed (on the main code)
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
				Rect t = alcHoles2[i];
				if(r.containsPoint(mouseX, mouseY)) {
					if(r.getColor() == Color.white){
						r.setColor(Color.black);
						h.setColor(Color.white);
						binary[i / 6][i % 6] = 0;
					}
					else{
						r.setColor(Color.white);
						h.setColor(Color.black);
						binary[i / 6][i % 6] = 1;
					}
					resetHighlight();
					updateHole = true;
				}
				if(showAlcCards){
					if(s.containsPoint(mouseX, mouseY)) {
						// Toggle the hole when it's clicked
						if(s.isFilled()){
							s.setFilled(false);
							alcBin1[i / 6][i % 6] = 0;
						}
						else{
							s.setFilled(true);
							alcBin1[i / 6][i % 6] = 1;
						}
						// Continue to alchemize when holes are toggled
						if(highlightAND) {
							binary = cv.functionAND(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(highlightOR) {
							binary = cv.functionOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(highlightXOR) {
							binary = cv.functionXOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(highlightNAND) {
							binary = cv.functionNAND(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(highlightNOR) {
							binary = cv.functionNOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(highlightXNOR) {
							binary = cv.functionXNOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						updateAlcHole1 = true;
					}
					if(t.containsPoint(mouseX, mouseY)) {
						// Toggle the hole when it's clicked
						if(t.isFilled()){
							t.setFilled(false);
							alcBin2[i / 6][i % 6] = 0;
						}
						else{
							t.setFilled(true);
							alcBin2[i / 6][i % 6] = 1;
						}
						// Continue to alchemize when holes are toggled
						if(highlightAND) {
							binary = cv.functionAND(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(highlightOR) {
							binary = cv.functionOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(highlightXOR) {
							binary = cv.functionXOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(highlightNAND) {
							binary = cv.functionNAND(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(highlightNOR) {
							binary = cv.functionNOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if(highlightXNOR) {
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

	private class ShortcutListener extends KeyAdapter {

		public void keyPressed(KeyEvent e) {

			if(e.isControlDown()) {
				if(e.getKeyCode() == KeyEvent.VK_N)
					resetCode();
				else if(e.getKeyCode() == KeyEvent.VK_S)
					save();
				else if(e.getKeyCode() == KeyEvent.VK_L)
					load();
				else if(e.getKeyCode() == KeyEvent.VK_A)
					changeSettings("Toggle Alchemy");
				else if(e.getKeyCode() == KeyEvent.VK_G)
					changeSettings("Toggle Grids");
				else if(e.getKeyCode() == KeyEvent.VK_O)
					changeSettings("Toggle Other Operations");
				else if(e.getKeyCode() == KeyEvent.VK_Y)
					changeSettings("Toggle Symbol");
				else if(e.getKeyCode() == KeyEvent.VK_T)
					changeSettings("About");
				else if(e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_SLASH)
					secretCode();
				else
					e.consume();
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
				//if(binary[i][j] == 0)
				//holes[h].setFilled(false);
				//else
				//holes[h].setFilled(true);
				if(binary[i][j] == 0) {
					holes[h].setColor(Color.white);
					backHoles[h].setColor(Color.black);
				}
				else {
					holes[h].setColor(Color.black);
					backHoles[h].setColor(Color.white);
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
				if(alcBin1[i][j] == 0)
					alcHoles1[h].setFilled(false);
				else
					alcHoles1[h].setFilled(true);
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
				if(alcBin2[i][j] == 0)
					alcHoles2[h].setFilled(false);
				else
					alcHoles2[h].setFilled(true);
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
		borderNOT = new Rect(394, 97, 48, 32, Color.black);
		borderFill = new Rect(410, 560, 32, 32, Color.black);
		borderRandomize = new Rect(346, 40, 96, 32, Color.black);
		// Load entry rectangles
		for (int i = 0; i < entries.length; i++)
			entries[i] = new Rect((32 + (47 * i)), 560, 32, 32, Color.black);
		// Load grids
		mainGrid[0] = new Rect(79, 123, 59, 172, Color.black);
		mainGrid[1] = new Rect(79, 295, 59, 170, Color.black);
		mainGrid[2] = new Rect(138, 123, 58, 172, Color.black);
		mainGrid[3] = new Rect(138, 295, 58, 170, Color.black);
		mainGrid[4] = new Rect(196, 123, 58, 172, Color.black);
		mainGrid[5] = new Rect(196, 295, 58, 170, Color.black);
		mainGrid[6] = new Rect(254, 123, 58, 172, Color.black);
		mainGrid[7] = new Rect(254, 295, 58, 170, Color.black);
		// Load hole rectangles and backgrounds
		for (int i = 0; i < holes.length; i++){
			int x = 0;
			int y = 0;
			if(i / 12 == 0)
				x = 94;
			else if(i / 12 == 1)
				x = 152;
			else if(i / 12 == 2)
				x = 210;
			else if(i / 12 == 3)
				x = 269;

			if(i % 12 == 0)
				y = 138;
			else if(i % 12 == 1)
				y = 166;
			else if(i % 12 == 2)
				y = 193;
			else if(i % 12 == 3)
				y = 221;
			else if(i % 12 == 4)
				y = 248;
			else if(i % 12 == 5)
				y = 276;
			else if(i % 12 == 6)
				y = 303;
			else if(i % 12 == 7)
				y = 331;
			else if(i % 12 == 8)
				y = 356;
			else if(i % 12 == 9)
				y = 384;
			else if(i % 12 == 10)
				y = 411;
			else if(i % 12 == 11)
				y = 439;

			holes[i] = new Rect(x, y, 29, 11, Color.white);
			backHoles[i] = new Rect(x - 1, y - 1, 30, 12, Color.black);
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
				borderAND = new Rect(351, 227, 48, 32, Color.black);
				borderOR = new Rect(351, 259, 48, 32, Color.black);
			}
			else {
				borderAND = new Rect(351, 291, 48, 32, Color.black);
				borderOR = new Rect(351, 323, 48, 32, Color.black);
			}
			borderXOR = new Rect(351, 291, 48, 32, Color.black);
			borderNAND = new Rect(351, 323, 48, 32, Color.black);
			borderNOR = new Rect(351, 355, 48, 32, Color.black);
			borderXNOR = new Rect(351, 387, 48, 32, Color.black);
			borderNOT = new Rect(782, 97, 48, 32, Color.black);
			borderNOT1 = new Rect(282, 40, 48, 32, Color.black);
			borderNOT2 = new Rect(282, 335, 48, 32, Color.black);
			borderFill = new Rect(798, 560, 32, 32, Color.black);
			borderFill1 = new Rect(280, 277, 20, 20, Color.black);
			borderFill2 = new Rect(280, 572, 20, 20, Color.black);
			borderRandomize = new Rect(734, 40, 96, 32, Color.black);
			borderRand1 = new Rect(310, 277, 20, 20, Color.black);
			borderRand2 = new Rect(310, 572, 20, 20, Color.black);
			// Load entry rectangles
			for (int i = 0; i < entries.length; i++)
				entries[i] = new Rect((414 + (47 * i)), 560, 32, 32, Color.black);
			for (int i = 0; i < entries.length; i++)
				alcEnt1[i] = new Rect(40 + (30 * i), 277, 20, 20, Color.black);
			for (int i = 0; i < entries.length; i++)
				alcEnt2[i] = new Rect(40 + (30 * i), 572, 20, 20, Color.black);
			// Load main grids
			for (int i = 0; i < mainGrid.length; i++)
				mainGrid[i].move(382, 0);
			// Load alchemy card 1 grids
			alcGrid1[0] = new Rect(119, 80, 25, 75, Color.black);
			alcGrid1[1] = new Rect(119, 155, 25, 72, Color.black);
			alcGrid1[2] = new Rect(144, 80, 25, 75, Color.black);
			alcGrid1[3] = new Rect(144, 155, 25, 72, Color.black);
			alcGrid1[4] = new Rect(169, 80, 25, 75, Color.black);
			alcGrid1[5] = new Rect(169, 155, 25, 72, Color.black);
			alcGrid1[6] = new Rect(194, 80, 25, 75, Color.black);
			alcGrid1[7] = new Rect(194, 155, 25, 72, Color.black);
			// Load alchemy card 2 grids
			alcGrid2[0] = new Rect(119, 375, 25, 75, Color.black);
			alcGrid2[1] = new Rect(119, 450, 25, 72, Color.black);
			alcGrid2[2] = new Rect(144, 375, 25, 75, Color.black);
			alcGrid2[3] = new Rect(144, 450, 25, 72, Color.black);
			alcGrid2[4] = new Rect(169, 375, 25, 75, Color.black);
			alcGrid2[5] = new Rect(169, 450, 25, 72, Color.black);
			alcGrid2[6] = new Rect(194, 375, 25, 75, Color.black);
			alcGrid2[7] = new Rect(194, 450, 25, 72, Color.black);
			// Load main hole rectangles and backgrounds
			for (int i = 0; i < holes.length; i++){
				holes[i].move(382, 0);
				backHoles[i].move(382, 0);
			}
			int x = 125;
			int y = 86;
			// Load alchemy card hole rectangles
			for (int i = 0; i < alcHoles1.length; i++){
				alcHoles1[i] = new Rect(x, y, 13, 5, Color.black);
				alcHoles2[i] = new Rect(x, y + 295, 13, 5, Color.black);
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
				borderAND = new Rect(351, 227, 48, 32, Color.black);
				borderOR = new Rect(351, 259, 48, 32, Color.black);
			}
			else {
				buttonAND = new Rect(351, 291, 48, 32, colAND);
				buttonOR = new Rect(351, 323, 48, 32, colOR);
				borderAND = new Rect(351, 291, 48, 32, Color.black);
				borderOR = new Rect(351, 323, 48, 32, Color.black);
			}
			buttonXOR = new Rect(351, 291, 48, 32, colXOR);
			buttonNAND = new Rect(351, 323, 48, 32, colNAND);
			buttonNOR = new Rect(351, 355, 48, 32, colNOR);
			buttonXNOR = new Rect(351, 387, 48, 32, colXNOR);
			borderXOR = new Rect(351, 291, 48, 32, Color.black);
			borderNAND = new Rect(351, 323, 48, 32, Color.black);
			borderNOR = new Rect(351, 355, 48, 32, Color.black);
			borderXNOR = new Rect(351, 387, 48, 32, Color.black);
		}
	}

	// Reset all codes to 00000000
	protected void resetCode(){
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

	// Save the main code
	protected void save(){
		// A dialog box asks for the what the file will be titled
		String filename = (String) JOptionPane.showInputDialog(null, "Enter Filename", "Save", JOptionPane.INFORMATION_MESSAGE,
				record, null, code);
		if(filename != null){
			File directory = new File("saves");
			try {
				// If the "saves" folder doesn't exist, the program creates it automatically
				if(!directory.exists())
					directory.mkdir();
				// The code is saved as a .txt file
				PrintWriter writer = new PrintWriter(new File("saves/" + filename + ".txt"));
				writer.print(code);
				writer.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}

	// Load one or two codes
	protected void load(){
		// The file prompt only show files ending in .txt
		fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
		// The directory is set automatically
		fc.setCurrentDirectory(new File("saves"));
		// Multiple file selection is enabled if the "Two Codes" method is chosen
		if(!showAlcCards){
			fc.setMultiSelectionEnabled(false);
			fc.setDialogTitle("Choose One Code");
		}
		else{
			fc.setMultiSelectionEnabled(true);
			fc.setDialogTitle("Choose Two Codes");
		}
		// Single-code method applies to the main field
		if(!showAlcCards){
			int returnVal = fc.showOpenDialog(CaptchaPanel.this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					Scanner reader = new Scanner(fc.getSelectedFile());
					codeUpdate = reader.nextLine();
					reader.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				updateCode = true;
			}
		}

		// Double-code method applies to the alchemy fields
		else{
			boolean maintain = true;
			while(maintain) {
				int returnVal = fc.showOpenDialog(CaptchaPanel.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File[] files = fc.getSelectedFiles();
					try {
						// If one file is selected, the single code is applied to both alchemy fields
						if(files.length != 2)
							errorMessage("badLoad");
						// If more than two files are selected, all but the first two are ignored
						else {
							Scanner reader = new Scanner(files[0]);
							codeUpdate1 = reader.nextLine();
							reader.close();
							reader = new Scanner(files[1]);
							codeUpdate2 = reader.nextLine();
							reader.close();
							// Both codes are updated
							updateAlcCode1 = true;
							updateAlcCode2 = true;
							maintain = false;
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
				else
					maintain = false;
			}
		}

		resetHighlight();
	}

	// Show an error message
	private void errorMessage(String err) {
		if(err.equals("badLength"))
			JOptionPane.showMessageDialog(null, "Error: Code must be 8 digits long.", "Error", JOptionPane.INFORMATION_MESSAGE, weasel);
		else if(err.equals("badDigit"))
			JOptionPane.showMessageDialog(null, "Error: Code contains an invalid digit.", "Error", JOptionPane.INFORMATION_MESSAGE, weasel);
		else if(err.equals("badLoad"))
			JOptionPane.showMessageDialog(null, "Error: Please select 2 files.", "Error", JOptionPane.INFORMATION_MESSAGE, weasel);
	}

	// Change the theme
	protected void changeTheme(String th, String ty){
		// Since image files are named after the themes, the ImageIcons can be changed with one line of code each
		captchaCard = new ImageIcon("images/" + ty + "/CaptchaCard" + th + ".png");
		card1 = new ImageIcon("images/" + ty + "/SmallCard" + th + ".png");
		card2 = new ImageIcon("images/" + ty + "/SmallCard" + th + ".png");
		symbol = new ImageIcon("images/" + ty + "/Symbol" + th + ".png");
		theme = th;
		type = ty;
		recolor = true;
	}

	protected void changeSettings(String opt){
		// For the Toggle Alchemy Cards option
		if(opt.equals("Toggle Alchemy")){
			showAlcCards = !showAlcCards;
			changeCards();
		}
		// For the Toggle Grids option
		else if(opt.equals("Toggle Grids")){
			showGrids = !showGrids;
			recolor = true;
		}
		// For the Toggle Other Operations button
		else if(opt.equals("Toggle Other Operations")) {
			showOtherOps = !showOtherOps;
			fixButtons();
			recolor = true;
		}
		// For the Toggle Symbol option
		else if(opt.equals("Toggle Symbol")){
			showSymbol = !showSymbol;
			recolor = true;
		}
		// For the About menu
		else if(opt.equals("About"))
			JOptionPane.showMessageDialog(null, "Programmer: " + author + "\nAdvisor: " + advisor +
					"\nEmail: " + email + "\nGitHub Page: " + link + "\nVersion: " +
					version, "About", JOptionPane.INFORMATION_MESSAGE, mspa);
		// For the Shortcuts menu
		else if(opt.equals("Shortcuts"))
			JOptionPane.showMessageDialog(null, "Ctrl + N  -  New" + "\nCtrl + S  -  Save" + "\nCtrl + L  -  Load" + 
					"\nCtrl + A  -  Toggle Alchemy" + "\nCtrl + G  -  Toggle Grids" + 
					"\nCtrl + O  -  Toggle Other Operations" + "\nCtrl + Y  -  Toggle Symbol" +
					"\nCtrl + T  -  About Page" + "\nCtrl + Shift + /  -  Trigger ??? Effect" +
					"\nEscape  -  Exit Program", "Shortcuts", JOptionPane.INFORMATION_MESSAGE, apple);
	}

	// Save options to json file
	protected void saveSettings(String filename) throws Exception{
		JSONObject options = new JSONObject();
		options.put("theme", theme);
		options.put("type", type);
		options.put("symbol", showSymbol);
		options.put("alchemy", showAlcCards);
		options.put("grids", showGrids);
		options.put("operations", showOtherOps);
		options.put("main_code", code);
		options.put("alchemy_code_1", alcCode1);
		options.put("alchemy_code_2", alcCode2);
		options.put("function_and", highlightAND);
		options.put("function_or", highlightOR);
		options.put("function_xor", highlightXOR);
		options.put("function_nand", highlightNAND);
		options.put("function_nor", highlightNOR);
		options.put("function_xnor", highlightXNOR);
		Files.write(Paths.get(filename), options.toString().getBytes());
	}

	// Easter egg handling (??? button)
	protected void secretCode(){
		// Developer code for Safety - ...
		if(code.equals("Safety56"))
			JOptionPane.showMessageDialog(null, "Something is supposed to happen, but it hasn't been coded yet.",
					"Coming soon...", JOptionPane.INFORMATION_MESSAGE, pumpkin);
		// Developer code for DetectiveDyn - ...
		else if(code.equals("rehersba"))
			JOptionPane.showMessageDialog(null, "Something is supposed to happen, but it hasn't been coded yet.",
					"Coming soon...", JOptionPane.INFORMATION_MESSAGE, pumpkin);
		// Code to Caliborn's juju - changes to a special theme
		else if(code.equals("uROBuROS"))
			changeTheme("Special (Caliborn)", "special");
		// Code to Calliope's juju - changes to a special theme
		else if(code.equals("UrobUros"))
			changeTheme("Special (Calliope)", "special");
		// Code to John's jetpack - ...
		else if(code.equals("PCHOOOOO"))
			JOptionPane.showMessageDialog(null, "Something is supposed to happen, but it hasn't been coded yet.",
					"Coming soon...", JOptionPane.INFORMATION_MESSAGE, pumpkin);
		// Code to the fluorite octet (Vriska's dice) - ...
		else if(code.equals("82THE8TH"))
			JOptionPane.showMessageDialog(null, "Something is supposed to happen, but it hasn't been coded yet.",
					"Coming soon...", JOptionPane.INFORMATION_MESSAGE, pumpkin);
		// What pumpkin? You are certain there are no pumpkins in this code
		else if(code.equals("pumpkin?"))
			JOptionPane.showMessageDialog(null, "Something is supposed to happen, but it hasn't been coded yet.",
					"Coming soon...", JOptionPane.INFORMATION_MESSAGE, pumpkin);
		// When the code doesn't match any of these, an error message is loaded with a random icon
		else {
			int err = gen.nextInt(4);
			if(err == 0)
				errIcon = new ImageIcon("images/icons/ConfusedJohn.png");
			else if(err == 1)
				errIcon = new ImageIcon("images/icons/Facepalm.png");
			else if(err == 2)
				errIcon = new ImageIcon("images/icons/HeadBonk.png");
			else if(err == 3)
				errIcon = new ImageIcon("images/icons/LilCal.png");
			JOptionPane.showMessageDialog(null, "Nothing happened. Well, nothing aside from this text box.",
					"Try Again", JOptionPane.INFORMATION_MESSAGE, errIcon);
		}
	}

	// Reset operation button colors (remove any highlights)
	private void resetHighlight(){
		highlightAND = false;
		highlightOR = false;
		highlightXOR = false;
		highlightNAND = false;
		highlightNOR = false;
		highlightXNOR = false;
		recolor = true;
	}

	// Get the GUI width to change in the GUIWindow program
	public boolean getCards(){
		return showAlcCards;
	}

}
