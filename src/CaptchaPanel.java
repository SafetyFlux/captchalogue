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
	private ImageIcon errIcon, pumpkin, record, mspa;					// ImageIcons for dialog box icons
	private Conversion cv = new Conversion();							// Class for conversion and operations
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
	private Rect buttonAND, buttonOR, buttonXOR, buttonNOT, fill, fill1, fill2,
				 randomize, rand1, rand2;
	// All button border rectangles (black outline)
	private Rect borderAND, borderOR, borderXOR, borderNOT, borderFill, borderFill1, borderFill2,
				 borderRandomize, borderRand1, borderRand2;
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
	private String version = "1.0";
	// Integer that tracks which code digit is being changed
	private int entryNo = -1;
	// String that tracks the current theme
	private String theme = "";
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
	private boolean highlightAND = false;
	private boolean highlightOR = false;
	private boolean highlightXOR = false;
	// All fonts used; f is the main one
	private Font f = new Font("Courier", Font.BOLD, 26);
	private Font fs = new Font("Courier", Font.BOLD, 16);
	private Font ft = new Font("Courier", Font.BOLD, 12);
	// Operation button (and highlight) colors
	private Color colAND = Color.cyan;
	private Color colOR = Color.cyan;
	private Color colXOR = Color.cyan;
	private Color highlight = new Color(50, 150, 150);

	public CaptchaPanel() throws FileNotFoundException, Exception {
		
		this.setBackground(Color.white);
		// Load options from json file
		Scanner reader = new Scanner(new File("res/options.json"));
		JSONObject options = new JSONObject(reader.nextLine());
		theme = options.getString("theme");
		showSymbol = options.getBoolean("symbol");
		showAlcCards = options.getBoolean("alchemy");
		showGrids = options.getBoolean("grids");
		code = options.getString("main_code");
		alcCode1 = options.getString("alchemy_code_1");
		alcCode2 = options.getString("alchemy_code_2");
		highlightAND = options.getBoolean("function_and");
		highlightOR = options.getBoolean("function_or");
		highlightXOR = options.getBoolean("function_xor");
		reader.close();
		// Call necessary methods
		loadRect();
		punchHole();
		changeCards();
		// Select default captcha card assets
		captchaCard = new ImageIcon("images/CaptchaCard" + theme + ".png");
		card1 = new ImageIcon("images/SmallCard" + theme + ".png");
		card2 = new ImageIcon("images/SmallCard" + theme + ".png");
		symbol = new ImageIcon("images/Symbol" + theme + ".png");
		// Select dialog box assets
		pumpkin = new ImageIcon("images/WhatPumpkin.png");
		record = new ImageIcon("images/Record.png");
		mspa = new ImageIcon("images/MSPAFace.png");
		// Add GUI listeners
		addMouseListener(new HoleListener());
		addKeyListener(new EntryListener());
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
			buttonXOR.draw(g);
			buttonXOR.setFilled(true);
			borderXOR.draw(g);
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
		buttonNOT.draw(g);
		buttonNOT.setFilled(true);
		borderNOT.draw(g);
		// Draw other buttons
		fill.draw(g);
		fill.setFilled(true);
		borderFill.draw(g);
		randomize.draw(g);
		randomize.setFilled(true);
		borderRandomize.draw(g);
		if(showAlcCards){
			// Draw operation strings
			g.drawString("&&", 359, 299);
			g.drawString("||", 359, 330);
			g.drawString("^^", 360, 368);
			g.drawString("~~", 791, 64);
			// Draw other strings
			g.drawString("<", 807, 584);
			g.drawString("R", 807, 121);
			g.setFont(fs);
			g.drawString("<", 286, 292);
			g.drawString("<", 286, 587);
			g.drawString("R", 316, 292);
			g.drawString("R", 316, 587);
		}
		else{
			// Draw operation strings
			g.drawString("~~", 403, 64);
			// Draw other strings
			g.drawString("<", 419, 584);
			g.drawString("R", 419, 121);
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
			backHoles[i].setFilled(true);
			holes[i].draw(g);
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
			recolor = true;
			alchemize = false;
		}
		// Repaint and save new options
		if(recolor){
			buttonAND = new Rect(351, 275, 48, 32, colAND);
			buttonOR = new Rect(351, 307, 48, 32, colOR);
			buttonXOR = new Rect(351, 339, 48, 32, colXOR);
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
					highlightAND = true;
					highlightOR = false;
					highlightXOR = false;
					updateHole = true;
					alchemize = true;
				}
				// If the "||" button is clicked, the OR operation is performed
				if(buttonOR.containsPoint(mouseX, mouseY)){
					fillBin();
					binary = cv.functionOR(alcBin1, alcBin2);
					highlightAND = false;
					highlightOR = true;
					highlightXOR = false;
					updateHole = true;
					alchemize = true;
				}
				// If the "^^" button is clicked, the XOR operation is performed
				if(buttonXOR.containsPoint(mouseX, mouseY)){
					fillBin();
					binary = cv.functionXOR(alcBin1, alcBin2);
					highlightAND = false;
					highlightOR = false;
					highlightXOR = true;
					updateHole = true;
					alchemize = true;
				}
				if(fill1.containsPoint(mouseX, mouseY)){
					String update = (String) JOptionPane.showInputDialog("Enter Code", "00000000");
					if(update != null){
						codeUpdate1 = update.trim();
						if(codeUpdate1.length() != 8)
							codeUpdate1 = "00000000";
						updateAlcCode1 = true;
						resetHighlight();
					}
				}
				if(fill2.containsPoint(mouseX, mouseY)){
					String update = (String) JOptionPane.showInputDialog("Enter Code", "00000000");
					if(update != null){
						codeUpdate2 = update.trim();
						if(codeUpdate2.length() != 8)
							codeUpdate2 = "00000000";
						updateAlcCode2 = true;
						resetHighlight();
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
			if(buttonNOT.containsPoint(mouseX, mouseY)){
				fillBin();
				binary = cv.functionNOT(binary);
				resetHighlight();
				updateHole = true;
				alchemize = true;
			}
			// If any of the "<" or "^" buttons are clicked, the corresponding field is filled in with the user input.
			if(fill.containsPoint(mouseX, mouseY)){
				String update = (String) JOptionPane.showInputDialog("Enter Code", "00000000");
				if(update != null){
					codeUpdate = update.trim();
					// The code is set to 00000000 if the input isn't 8 digits long
					if(codeUpdate.length() != 8)
						codeUpdate = "00000000";
					updateCode = true;
					resetHighlight();
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
				Rect r = holes[i];
				Rect s = alcHoles1[i];
				Rect t = alcHoles2[i];
				if(r.containsPoint(mouseX, mouseY)) {
					if(r.isFilled()){
						r.setFilled(false);
						binary[i / 6][i % 6] = 0;
					}
					else{
						r.setFilled(true);
						binary[i / 6][i % 6] = 1;
					}
					resetHighlight();
					updateHole = true;
				}
				if(showAlcCards){
					if(s.containsPoint(mouseX, mouseY)) {
						if(s.isFilled()){
							s.setFilled(false);
							alcBin1[i / 6][i % 6] = 0;
						}
						else{
							s.setFilled(true);
							alcBin1[i / 6][i % 6] = 1;
						}
						resetHighlight();
						updateAlcHole1 = true;
					}
					if(t.containsPoint(mouseX, mouseY)) {
						if(t.isFilled()){
							t.setFilled(false);
							alcBin2[i / 6][i % 6] = 0;
						}
						else{
							t.setFilled(true);
							alcBin2[i / 6][i % 6] = 1;
						}
						resetHighlight();
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
				for (int i = 0; i < code.length(); i++) {
					if(i == entryNo)
						codeUpdate += d;
					else
						codeUpdate += code.charAt(i);
				}
				entryUpdate = false;
				updateCode = true;
			}
			// For the alchemy codes
			else if(alcEnt1Update){
				if(!e.isActionKey() && e.getKeyCode() != KeyEvent.VK_SHIFT)
					d = e.getKeyChar();
				else
					d = '0';
				for (int i = 0; i < alcCode1.length(); i++) {
					if(i == entryNo)
						codeUpdate1 += d;
					else
						codeUpdate1 += alcCode1.charAt(i);
				}
				alcEnt1Update = false;
				updateAlcCode1 = true;
			}
			else if(alcEnt2Update){
				if(!e.isActionKey() && e.getKeyCode() != KeyEvent.VK_SHIFT)
					d = e.getKeyChar();
				else
					d = '0';
				for (int i = 0; i < alcCode2.length(); i++) {
					if(i == entryNo)
						codeUpdate2 += d;
					else
						codeUpdate2 += alcCode2.charAt(i);
				}
				alcEnt2Update = false;
				updateAlcCode2 = true;
			}
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
				if(binary[i][j] == 0)
					holes[h].setFilled(false);
				else
					holes[h].setFilled(true);
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
		buttonNOT = new Rect(394, 40, 48, 32, new Color(175, 165, 255));
		fill = new Rect(410, 560, 32, 32, Color.green);
		randomize = new Rect(410, 97, 32, 32, Color.yellow);
		// Load button borders (black outline)
		borderNOT = new Rect(394, 40, 48, 32, Color.black);
		borderFill = new Rect(410, 560, 32, 32, Color.black);
		borderRandomize = new Rect(410, 97, 32, 32, Color.black);
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
			
			holes[i] = new Rect(x, y, 30, 12, Color.black);
			backHoles[i] = new Rect(x, y, 30, 12, Color.white);
		}
		
	}
	
	// Change rectangle positions when alchemy cards are toggled
	private void changeCards(){
		if(showAlcCards){
			// Load button rectangles
			buttonAND = new Rect(351, 275, 48, 32, colAND);
			buttonOR = new Rect(351, 307, 48, 32, colOR);
			buttonXOR = new Rect(351, 339, 48, 32, colXOR);
			buttonNOT = new Rect(782, 40, 48, 32, new Color(175, 165, 255));
			fill = new Rect(798, 560, 32, 32, Color.green);
			fill1 = new Rect(280, 277, 20, 20, Color.green);
			fill2 = new Rect(280, 572, 20, 20, Color.green);
			randomize = new Rect(798, 97, 32, 32, Color.yellow);
			rand1 = new Rect(310, 277, 20, 20, Color.yellow);
			rand2 = new Rect(310, 572, 20, 20, Color.yellow);
			// Load button borders (black outline)
			borderAND = new Rect(351, 275, 48, 32, Color.black);
			borderOR = new Rect(351, 307, 48, 32, Color.black);
			borderXOR = new Rect(351, 339, 48, 32, Color.black);
			borderNOT = new Rect(782, 40, 48, 32, Color.black);
			borderFill = new Rect(798, 560, 32, 32, Color.black);
			borderFill1 = new Rect(280, 277, 20, 20, Color.black);
			borderFill2 = new Rect(280, 572, 20, 20, Color.black);
			borderRandomize = new Rect(798, 97, 32, 32, Color.black);
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
	
	// Reset all codes to 00000000
	protected void resetCode(){
		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset?", "Confirm Reset",
													JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, record);
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
		String filename = (String) JOptionPane.showInputDialog("Enter Filename", code);
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
		int returnVal = fc.showOpenDialog(CaptchaPanel.this);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			// Single-code method applies to the main field
			if(!showAlcCards){
				try {
					Scanner reader = new Scanner(fc.getSelectedFile());
					codeUpdate = reader.nextLine();
					reader.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				updateCode = true;
			}
			// Double-code method applies to the alchemy fields
			else{
				File[] files = fc.getSelectedFiles();
				try {
					// If one file is selected, the single code is applied to both alchemy fields
					if(files.length == 1){
						Scanner reader = new Scanner(files[0]);
						codeUpdate1 = reader.nextLine();
						codeUpdate2 = codeUpdate1;
						reader.close();
					}
					// If more than two files are selected, all but the first two are ignored
					else{
						Scanner reader = new Scanner(files[0]);
						codeUpdate1 = reader.nextLine();
						reader.close();
						reader = new Scanner(files[1]);
						codeUpdate2 = reader.nextLine();
						reader.close();
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				// Both codes are updated
				updateAlcCode1 = true;
				updateAlcCode2 = true;
			}
		}
		
		resetHighlight();
	}
	
	// Change the theme
	protected void changeTheme(String th){
		// Since image files are named after the themes, the ImageIcons can be changed with one line of code each
		captchaCard = new ImageIcon("images/CaptchaCard" + th + ".png");
		card1 = new ImageIcon("images/SmallCard" + th + ".png");
		card2 = new ImageIcon("images/SmallCard" + th + ".png");
		symbol = new ImageIcon("images/Symbol" + th + ".png");
		theme = th;
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
		// For the Toggle Symbol option
		else if(opt.equals("Toggle Symbol")){
			showSymbol = !showSymbol;
			recolor = true;
		}
		// For the About option
		else if(opt.equals("About"))
			JOptionPane.showMessageDialog(null, "Programmer: " + author + "\nAdvisor: " + advisor +
										  "\nEmail: " + email + "\nGitHub Page: " + link + "\nVersion: " +
										  version, "About", JOptionPane.INFORMATION_MESSAGE, mspa);
	}
	
	// Save options to json file
	protected void saveSettings(String filename) throws Exception{
		JSONObject options = new JSONObject();
		options.put("theme", theme);
		options.put("symbol", showSymbol);
		options.put("alchemy", showAlcCards);
		options.put("grids", showGrids);
		options.put("main_code", code);
		options.put("alchemy_code_1", alcCode1);
		options.put("alchemy_code_2", alcCode2);
		options.put("function_and", highlightAND);
		options.put("function_or", highlightOR);
		options.put("function_xor", highlightXOR);
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
			changeTheme("Special (Caliborn)");
		// Code to Calliope's juju - changes to a special theme
		else if(code.equals("UrobUros"))
			changeTheme("Special (Calliope)");
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
				errIcon = new ImageIcon("images/ConfusedJohn.png");
			else if(err == 1)
				errIcon = new ImageIcon("images/Facepalm.png");
			else if(err == 2)
				errIcon = new ImageIcon("images/HeadBonk.png");
			else if(err == 3)
				errIcon = new ImageIcon("images/LilCal.png");
			JOptionPane.showMessageDialog(null, "Nothing happened. Well, nothing aside from this text box.",
					"Try Again", JOptionPane.INFORMATION_MESSAGE, errIcon);
		}
	}
	
	// Reset operation button colors (remove any highlights)
	private void resetHighlight(){
		highlightAND = false;
		highlightOR = false;
		highlightXOR = false;
		recolor = true;
	}
	
	// Get the GUI width to change in the GUIWindow program
	public boolean getCards(){
		return showAlcCards;
	}

}
