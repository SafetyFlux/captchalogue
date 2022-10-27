package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONException;
import org.json.JSONObject;

import shape.BorderRect;
import shape.Rect;
import utility.Alchemize;
import utility.Conversion;
import utility.DigitValues;
import utility.ImageUtil;
import utility.Randomize;
import resources.ResourceLoader;

public class CaptchaPanel extends JPanel {

	// About page information
	private static String author = "Safety";
	private static String advisor = "DetectiveDyn";
	private static String email = "dekuwither@gmail.com";
	private static String link = "https://github.com/SafetyFlux/captchalogue";
	private static String version = "1.4.1";
	// Shortcut information
	private static String[] shortcuts = new String[] {
			"Ctrl + N  -  New", "Ctrl + Shift + N  -  New (No Prompt)", "Ctrl + S  -  Save Image", 
			"Ctrl + R  -  Randomize All Codes", "Ctrl + A  -  Toggle Alchemy", "Ctrl + G  -  Toggle Grid", 
			"Ctrl + O  -  Toggle Other Operations", "Ctrl + Y  -  Toggle Symbols", "Ctrl + T  -  About Page", 
			"Escape  -  Exit Program"
	};
	
	private static final long serialVersionUID = 1L;
	private ImageIcon captchaCard, card1, card2, symbol, symbolS;		// Images for the assets
	private ImageIcon record, mspa, gun, arrow, apple, weasel;			// Images for dialog box icons
	private Random gen = new Random();									// Random for... generating random numbers
	final JFileChooser fc = new JFileChooser();							// File chooser for loading codes
	// Integer arrays for code binaries
	private int[][] binary = new int[8][6];
	private int[][] alcBin1 = new int[8][6];
	private int[][] alcBin2 = new int[8][6];
	// Rect arrays for holes
	private BorderRect holes[] = new BorderRect[48];
	private BorderRect alcHoles1[] = new BorderRect[48];
	private BorderRect alcHoles2[] = new BorderRect[48];
	// Rect arrays for code digits
	private Rect entries[] = new Rect[8];
	private Rect alcEnt1[] = new Rect[8];
	private Rect alcEnt2[] = new Rect[8];
	// String arrays for code digits
	private String digits[] = new String[8];
	private String alcDig1[] = new String[8];
	private String alcDig2[] = new String[8];
	// All button rectangles
	private BorderRect buttonAND, buttonOR, buttonXOR, buttonNAND, buttonNOR, buttonXNOR, buttonNOT, buttonNOT1, buttonNOT2,
	fill, fill1, fill2, randomize, rand1, rand2;
	// All grid rectangle arrays
	private Rect mainGrid[] = new Rect[8];
	// Strings for codes, code updates, and the current operation
	private String code = "";
	private String alcCode1 = "";
	private String alcCode2 = "";
	private String codeUpdate = "";
	private String codeUpdate1 = "";
	private String codeUpdate2 = "";
	private String operation = "";
	
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
		// Call necessary methods
		loadSettings();
		loadRect();
		punchHole();
		changeCards();
		fixButtons();
		wardrobifier.start();
		changeTheme(theme, type);
		// Select dialog box assets
		record = new ImageIcon(ResourceLoader.loadImage("icons/Record.png"));
		mspa = new ImageIcon(ResourceLoader.loadImage("icons/MSPAFace.png"));
		gun = new ImageIcon(ResourceLoader.loadImage("icons/MSPAReader.png"));
		arrow = new ImageIcon(ResourceLoader.loadImage("icons/RightArrow.png"));
		apple = new ImageIcon(ResourceLoader.loadImage("icons/Apple.png"));
		weasel = new ImageIcon(ResourceLoader.loadImage("icons/Weasel.png"));
		// Add GUI listeners
		addMouseListener(new HoleListener());
		addKeyListener(new EntryListener());
		addKeyListener(new ShortcutListener());
		setFocusable(true);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Set up the font and font color
		g.setFont(f);
		g.setColor(Color.BLACK);
		int symbolY = (captchaCard.getIconHeight() / 2) - (symbol.getIconHeight() / 2);
		int symbolYS = (card2.getIconHeight() / 2) - (symbolS.getIconHeight() / 2);
		if (showAlcCards) {
			// Add code digits
			for (int i = 0; i < digits.length; i++) {
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
			for (int i = 0; i < digits.length; i++) {
				digits[i] = "";
				digits[i] += code.charAt(i);
				g.drawString(digits[i], 41 + (47 * i), 584);
			}
		}
		g.setFont(f);
		// Set highlights
		colAND = operation.equals("AND") ? highlight : Color.CYAN;
		colOR = operation.equals("OR") ? highlight : Color.CYAN;
		colXOR = operation.equals("XOR") ? highlight : Color.CYAN;
		colNAND = operation.equals("NAND") ? highlight : Color.CYAN;
		colNOR = operation.equals("NOR") ? highlight : Color.CYAN;
		colXNOR = operation.equals("XNOR") ? highlight : Color.CYAN;
		// Add captcha card asset
		if (showAlcCards) {
			captchaCard.paintIcon(this, g, 423, 40);
			card1.paintIcon(this, g, 100, 40);
			card2.paintIcon(this, g, 100, 335);
			if (showSymbol) {
				symbol.paintIcon(this, g, 493, 40 + symbolY);
				symbolS.paintIcon(this, g, 135, 335 + symbolYS);
			}
		}
		else{
			captchaCard.paintIcon(this, g, 39, 40);
			if(showSymbol)
				symbol.paintIcon(this, g, 109, 40 + symbolY);
		}
		// Draw operation buttons
		if (showAlcCards) {
			buttonAND.draw(g);
			buttonOR.draw(g);
			if (showOtherOps) {
				buttonXOR.draw(g);
				buttonNAND.draw(g);
				buttonNOR.draw(g);
				buttonXNOR.draw(g);
				buttonNOT1.draw(g);
				buttonNOT2.draw(g);
			}
			fill1.draw(g);
			fill2.draw(g);
			rand1.draw(g);
			rand2.draw(g);
		}
		if (showOtherOps) {
			buttonNOT.draw(g);
		}
		// Draw other buttons
		fill.draw(g);
		randomize.draw(g);
		if (showAlcCards) {
			// Draw operation strings
			if (showOtherOps) {
				g.drawString("&&", 361, 251);
				g.drawString("||", 361, 282);
				g.drawString("^^", 362, 320);
				g.drawString("~&", 362, 347);
				g.drawString("~|", 363, 378);
				g.drawString("~^", 363, 413);
				g.drawString("~~", 791, 120);
				g.drawString("~~", 291, 64);
				g.drawString("~~", 291, 359);
			}
			else {
				g.drawString("&&", 361, 315);
				g.drawString("||", 361, 346);
			}
			// Draw other strings
			g.drawString("<", 806, 584);
			g.drawString("RAND", 751, 64);
			g.setFont(fs);
			g.drawString("<", 286, 291);
			g.drawString("<", 286, 587);
			g.drawString("R", 315, 291);
			g.drawString("R", 315, 587);
		}
		else{
			// Draw operation strings
			if (showOtherOps)
				g.drawString("~~", 403, 121);
			// Draw other strings
			g.drawString("<", 419, 584);
			g.drawString("RAND", 363, 64);
			g.setFont(f);
		}
		// Draw code digit rectangles
		for (int i = 0; i < entries.length; i++) {
			entries[i].draw(g);
			if (showAlcCards) {
				alcEnt1[i].draw(g);
				alcEnt2[i].draw(g);
			}
		}
		// Draw grids
		if (showGrids) {
			for (int i = 0; i < mainGrid.length; i++)
				mainGrid[i].draw(g);
			// Arrays for grid number coordinates
			int[] gridNumX = {83, 142, 200, 258};
			int[] gridNumY = {135, 462};
			// Loop to draw grid numbers
			g.setFont(ft);
			for (int i = 0; i < 8; i++) {
				if (showAlcCards)
					g.drawString("" + (i + 1), gridNumX[i / 2] + 384, gridNumY[i % 2]);
				else
					g.drawString("" + (i + 1), gridNumX[i / 2], gridNumY[i % 2]);
			}
			g.setFont(f);
		}
		// Draw holes
		for (int i = 0; i < holes.length; i++) {
			holes[i].draw(g);
		}
		if (showAlcCards) {
			for (int i = 0; i < alcHoles1.length; i++) {
				alcHoles1[i].draw(g);
				alcHoles2[i].draw(g);
			}
		}
		repaint();
		// Update the code when a hole is toggled
		if (updateHole) {
			code = codeUpdate;
			recolor = true;
			codeUpdate = "";
			updateHole = false;
		}
		if (updateAlcHole1) {
			alcCode1 = codeUpdate1;
			recolor = true;
			codeUpdate1 = "";
			updateAlcHole1 = false;
		}
		if (updateAlcHole2) {
			alcCode2 = codeUpdate2;
			recolor = true;
			codeUpdate2 = "";
			updateAlcHole2 = false;
		}
		// Update the holes (and code) when the code is changed, filled, or randomized
		if (updateCode) {
			code = codeUpdate;
			punchHole();
			recolor = true;
			codeUpdate = "";
			updateCode = false;
		}
		// Update the 1st alchemy code when it's changed, filled, or randomized
		if (updateAlcCode1) {
			alcCode1 = codeUpdate1;
			if (showAlcCards)
				punchAlcHole1();
			recolor = true;
			codeUpdate1 = "";
			updateAlcCode1 = false;
		}
		// Update the 2nd alchemy code when it's changed, filled, or randomized
		if (updateAlcCode2) {
			alcCode2 = codeUpdate2;
			if (showAlcCards)
				punchAlcHole2();
			recolor = true;
			codeUpdate2 = "";
			updateAlcCode2 = false;
		}
		// Update the holes (and set button highlight) when an operation is performed
		if (alchemize) {
			punchHole();
			if (showAlcCards) {
				punchAlcHole1();
				punchAlcHole2();
			}
			recolor = true;
			alchemize = false;
		}
		// Repaint and save new options
		if (recolor) {
			if (showOtherOps) {
				buttonAND = new BorderRect(353, 227, 48, 32, colAND);
				buttonOR = new BorderRect(353, 259, 48, 32, colOR);
				buttonXOR = new BorderRect(353, 291, 48, 32, colXOR);
				buttonNAND = new BorderRect(353, 323, 48, 32, colNAND);
				buttonNOR = new BorderRect(353, 355, 48, 32, colNOR);
				buttonXNOR = new BorderRect(353, 387, 48, 32, colXNOR);
			}
			else {
				buttonAND = new BorderRect(353, 291, 48, 32, colAND);
				buttonOR = new BorderRect(353, 323, 48, 32, colOR);
			}
			repaint();
			try {
				saveSettings("config.json");
			} catch (Exception e) {
				e.printStackTrace();
			}
			recolor = false;
		}
	}

	private class HoleListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			// Set variables for mouse position
			int mouseX = e.getX();
			int mouseY = e.getY();

			if (showAlcCards) {
				// If the "&&" button is clicked, the AND operation is performed
				if (buttonAND.containsPoint(mouseX, mouseY)) {
					fillBin();
					binary = Alchemize.functionAND(alcBin1, alcBin2);
					operation = operation.equals("AND") ? "NONE" : "AND";
					updateHole = true;
					alchemize = true;
				}
				// If the "||" button is clicked, the OR operation is performed
				if (buttonOR.containsPoint(mouseX, mouseY)) {
					fillBin();
					binary = Alchemize.functionOR(alcBin1, alcBin2);
					operation = operation.equals("OR") ? "NONE" : "OR";
					updateHole = true;
					alchemize = true;
				}
				if (showOtherOps) {
					// If the "^^" button is clicked, the XOR operation is performed
					if (buttonXOR.containsPoint(mouseX, mouseY)) {
						fillBin();
						binary = Alchemize.functionXOR(alcBin1, alcBin2);
						operation = operation.equals("XOR") ? "NONE" : "XOR";
						updateHole = true;
						alchemize = true;
					}
					// If the "~&" button is clicked, the NAND operation is performed
					if (buttonNAND.containsPoint(mouseX, mouseY)) {
						fillBin();
						binary = Alchemize.functionNAND(alcBin1, alcBin2);
						operation = operation.equals("NAND") ? "NONE" : "NAND";
						updateHole = true;
						alchemize = true;
					}
					// If the "~|" button is clicked, the NOR operation is performed
					if (buttonNOR.containsPoint(mouseX, mouseY)) {
						fillBin();
						binary = Alchemize.functionNOR(alcBin1, alcBin2);
						operation = operation.equals("NOR") ? "NONE" : "NOR";
						updateHole = true;
						alchemize = true;
					}
					// If the "~^" button is clicked, the XNOR operation is performed
					if (buttonXNOR.containsPoint(mouseX, mouseY)) {
						fillBin();
						binary = Alchemize.functionXNOR(alcBin1, alcBin2);
						operation = operation.equals("XNOR") ? "NONE" : "XNOR";
						updateHole = true;
						alchemize = true;
					}
					// If the "~~" button is clicked, the NOT operation is performed (on the top code)
					if (buttonNOT1.containsPoint(mouseX, mouseY)) {
						fillBin();
						alcBin1 = Alchemize.functionNOT(alcBin1);
						resetHighlight();
						updateAlcHole1 = true;
						alchemize = true;
					}
					// If the "~~" button is clicked, the NOT operation is performed (on the bottom code)
					if (buttonNOT2.containsPoint(mouseX, mouseY)) {
						fillBin();
						alcBin2 = Alchemize.functionNOT(alcBin2);
						resetHighlight();
						updateAlcHole2 = true;
						alchemize = true;
					}
				}
				if (fill1.containsPoint(mouseX, mouseY)) {
					boolean maintain = true;
					while(maintain) {
						String update = (String) JOptionPane.showInputDialog(null, "Enter Code", "Fill", 
								JOptionPane.INFORMATION_MESSAGE, (Icon) arrow, null, "00000000");
						if (update != null) {
							codeUpdate1 = update.trim();
							// The code is set to 00000000 if the input isn't 8 digits long
							if (codeUpdate1.length() != 8) {
								codeUpdate1 = "00000000";
								errorMessage("badLength");
								maintain = true;
							}
							else {
								if (!Conversion.checkCode(codeUpdate1)) {
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
				if (fill2.containsPoint(mouseX, mouseY)) {
					boolean maintain = true;
					while(maintain) {
						String update = (String) JOptionPane.showInputDialog(null, "Enter Code", "Fill", 
								JOptionPane.INFORMATION_MESSAGE, (Icon) arrow, null, "00000000");
						if (update != null) {
							codeUpdate2 = update.trim();
							// The code is set to 00000000 if the input isn't 8 digits long
							if (codeUpdate2.length() != 8) {
								codeUpdate2 = "00000000";
								errorMessage("badLength");
								maintain = true;
							}
							else {
								if (!Conversion.checkCode(codeUpdate2)) {
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
				if (rand1.containsPoint(mouseX, mouseY)) {
					codeUpdate1 = "";
					for (int i = 0; i < 8; i++)
						codeUpdate1 += Randomize.getChar();
					updateAlcCode1 = true;
					resetHighlight();
				}
				if (rand2.containsPoint(mouseX, mouseY)) {
					codeUpdate2 = "";
					for (int i = 0; i < 8; i++)
						codeUpdate2 += Randomize.getChar();
					updateAlcCode2 = true;
					resetHighlight();
				}
			}
			// If the "~~" button is clicked, the NOT operation is performed (on the main code)
			if (showOtherOps) {
				if (buttonNOT.containsPoint(mouseX, mouseY)) {
					fillBin();
					binary = Alchemize.functionNOT(binary);
					resetHighlight();
					updateHole = true;
					alchemize = true;
				}
			}
			// If any of the "<" or "^" buttons are clicked, the corresponding field is filled in with the user input.
			if (fill.containsPoint(mouseX, mouseY)) {
				boolean maintain = true;
				while(maintain) {
					String update = (String) JOptionPane.showInputDialog(null, "Enter Code", "Fill", 
							JOptionPane.INFORMATION_MESSAGE, (Icon) arrow, null, "00000000");
					if (update != null) {
						codeUpdate = update.trim();
						// The code is set to 00000000 if the input isn't 8 digits long
						if (codeUpdate.length() != 8) {
							codeUpdate = "00000000";
							errorMessage("badLength");
							maintain = true;
						}
						else {
							if (!Conversion.checkCode(codeUpdate)) {
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
			if (randomize.containsPoint(mouseX, mouseY)) {
				codeUpdate = "";
				for (int i = 0; i < 8; i++)
					codeUpdate += Randomize.getChar();
				updateCode = true;
				resetHighlight();
			}

			// If a single digit is clicked, the location is logged and it can then be changed
			for (int i = 0; i < entries.length; i++) {
				Rect q = entries[i];
				if (q.containsPoint(mouseX, mouseY)) {
					entryNo = i;
					resetHighlight();
					entryUpdate = true;
					break;
				}
			}
			if (showAlcCards) {
				for (int i = 0; i < alcEnt1.length; i++) {
					Rect q = alcEnt1[i];
					if (q.containsPoint(mouseX, mouseY)) {
						entryNo = i;
						resetHighlight();
						alcEnt1Update = true;
						break;
					}
				}
				for (int i = 0; i < alcEnt2.length; i++) {
					Rect q = alcEnt2[i];
					if (q.containsPoint(mouseX, mouseY)) {
						entryNo = i;
						resetHighlight();
						alcEnt2Update = true;
						break;
					}
				}
			}

			// If a hole is clicked, it will toggle on or off
			for (int i = 0; i < holes.length; i++) {
				BorderRect h = holes[i];
				BorderRect s = alcHoles1[i];
				BorderRect t = alcHoles2[i];
				if (h.containsPoint(mouseX, mouseY)) {
					if (h.getColor() == Color.BLACK) {
						h.setBorderColor(Color.BLACK);
						h.setColor(Color.WHITE);
						binary[i / 6][i % 6] = 0;
					}
					else {
						h.setBorderColor(Color.WHITE);
						h.setColor(Color.BLACK);
						binary[i / 6][i % 6] = 1;
					}
					resetHighlight();
					updateHole = true;
				}
				if (showAlcCards) {
					if (s.containsPoint(mouseX, mouseY)) {
						// Toggle the hole when it's clicked
						if (s.getColor() == Color.BLACK) {
							s.setBorderColor(Color.BLACK);
							s.setColor(Color.WHITE);
							alcBin1[i / 6][i % 6] = 0;
						}
						else {
							s.setBorderColor(Color.WHITE);
							s.setColor(Color.BLACK);
							alcBin1[i / 6][i % 6] = 1;
						}
						// Continue to alchemize when holes are toggled
						if (operation.equals("AND")) {
							binary = Alchemize.functionAND(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if (operation.equals("OR")) {
							binary = Alchemize.functionOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if (operation.equals("XOR")) {
							binary = Alchemize.functionXOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if (operation.equals("NAND")) {
							binary = Alchemize.functionNAND(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if (operation.equals("NOR")) {
							binary = Alchemize.functionNOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if (operation.equals("XNOR")) {
							binary = Alchemize.functionXNOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						updateAlcHole1 = true;
					}
					if (t.containsPoint(mouseX, mouseY)) {
						// Toggle the hole when it's clicked
						if (t.getColor() == Color.BLACK) {
							t.setBorderColor(Color.BLACK);
							t.setColor(Color.WHITE);
							alcBin2[i / 6][i % 6] = 0;
						}
						else {
							t.setBorderColor(Color.WHITE);
							t.setColor(Color.BLACK);
							alcBin2[i / 6][i % 6] = 1;
						}
						// Continue to alchemize when holes are toggled
						if (operation.equals("AND")) {
							binary = Alchemize.functionAND(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if (operation.equals("OR")) {
							binary = Alchemize.functionOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if (operation.equals("XOR")) {
							binary = Alchemize.functionXOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if (operation.equals("NAND")) {
							binary = Alchemize.functionNAND(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if (operation.equals("NOR")) {
							binary = Alchemize.functionNOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						if (operation.equals("XNOR")) {
							binary = Alchemize.functionXNOR(alcBin1, alcBin2);
							updateHole = true;
							alchemize = true;
						}
						updateAlcHole2 = true;
					}
				}
			}

			// If a hole is clicked, the change is applied to the code
			if (updateHole) {
				for (int i = 0; i < code.length(); i++) {
					int bin[] = new int[6];
					for (int j = 0; j < 6; j++)
						bin[j] = binary[i][j];
					codeUpdate += Conversion.binaryToDigit(bin);
				}
			}
			if (updateAlcHole1) {
				for (int i = 0; i < alcCode1.length(); i++) {
					int bin[] = new int[6];
					for (int j = 0; j < 6; j++)
						bin[j] = alcBin1[i][j];
					codeUpdate1 += Conversion.binaryToDigit(bin);
				}
			}
			if (updateAlcHole2) {
				for (int i = 0; i < alcCode2.length(); i++) {
					int bin[] = new int[6];
					for (int j = 0; j < 6; j++)
						bin[j] = alcBin2[i][j];
					codeUpdate2 += Conversion.binaryToDigit(bin);
				}
			}

		}
	}

	private class EntryListener extends KeyAdapter{

		// After a digit is clicked, the KeyListener looks for the next key typed
		public void keyTyped(KeyEvent e) {

			char d = '0';
			// For the main code
			if (entryUpdate) {
				// A few checks are in place to block action keys and allow shift to be held
				if (!e.isActionKey() && e.getKeyCode() != KeyEvent.VK_SHIFT)
					d = e.getKeyChar();
				else
					d = '0';
				// The program runs through the code again, only changing the selected digit
				if (DigitValues.checkDigit(d)) {
					for (int i = 0; i < code.length(); i++) {
						if (i == entryNo)
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
			else if (alcEnt1Update) {
				if (!e.isActionKey() && e.getKeyCode() != KeyEvent.VK_SHIFT)
					d = e.getKeyChar();
				else
					d = '0';
				if (DigitValues.checkDigit(d)) {
					for (int i = 0; i < alcCode1.length(); i++) {
						if (i == entryNo)
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
			else if (alcEnt2Update) {
				if (!e.isActionKey() && e.getKeyCode() != KeyEvent.VK_SHIFT)
					d = e.getKeyChar();
				else
					d = '0';
				if (DigitValues.checkDigit(d)) {
					for (int i = 0; i < alcCode2.length(); i++) {
						if (i == entryNo)
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

			if (e.isControlDown()) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_N:
					if (e.isShiftDown())
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
						codeUpdate += Randomize.getChar();
					updateCode = true;
					codeUpdate1 = "";
					if (showAlcCards) {
						for (int i = 0; i < 8; i++)
							codeUpdate1 += Randomize.getChar();
						updateAlcCode1 = true;
						codeUpdate2 = "";
						for (int i = 0; i < 8; i++)
							codeUpdate2 += Randomize.getChar();
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
			else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				System.exit(0);
			else
				e.consume();

		}

	}

	private void punchHole() {
		// The holes are updated when the code is changed
		for (int i = 0; i < binary.length; i++) {
			int[] c = Conversion.digitToBinary(code.charAt(i));
			for (int j = 0; j < binary[i].length; j++)
				binary[i][j] = c[j];
		}
		for (int i = 0; i < binary.length; i++) {
			for (int j = 0; j < binary[i].length; j++) {
				int h = (6 * i) + j;
				if (binary[i][j] == 0) {
					holes[h].setColor(Color.WHITE);
					holes[h].setBorderColor(Color.BLACK);
				}
				else {
					holes[h].setColor(Color.BLACK);
					holes[h].setBorderColor(Color.WHITE);
				}

			}
		}
	}

	private void punchAlcHole1() {
		// The holes are updated when the code is changed
		for (int i = 0; i < alcBin1.length; i++) {
			int[] c = Conversion.digitToBinary(alcCode1.charAt(i));
			for (int j = 0; j < alcBin1[i].length; j++)
				alcBin1[i][j] = c[j];
		}
		for (int i = 0; i < alcBin1.length; i++) {
			for (int j = 0; j < alcBin1[i].length; j++) {
				int h = (6 * i) + j;
				if (alcBin1[i][j] == 0) {
					alcHoles1[h].setColor(Color.WHITE);
					alcHoles1[h].setBorderColor(Color.BLACK);
				}
				else {
					alcHoles1[h].setColor(Color.BLACK);
					alcHoles1[h].setBorderColor(Color.WHITE);
				}

			}
		}
	}

	private void punchAlcHole2() {
		// The holes are updated when the code is changed
		for (int i = 0; i < alcBin2.length; i++) {
			int[] c = Conversion.digitToBinary(alcCode2.charAt(i));
			for (int j = 0; j < alcBin2[i].length; j++)
				alcBin2[i][j] = c[j];
		}
		for (int i = 0; i < alcBin2.length; i++) {
			for (int j = 0; j < alcBin2[i].length; j++) {
				int h = (6 * i) + j;
				if (alcBin2[i][j] == 0) {
					alcHoles2[h].setColor(Color.WHITE);
					alcHoles2[h].setBorderColor(Color.BLACK);
				}
				else {
					alcHoles2[h].setColor(Color.BLACK);
					alcHoles2[h].setBorderColor(Color.WHITE);
				}

			}
		}
	}

	private void fillBin() {
		// The alchemy code binary arrays are updated when the codes are changed
		for (int i = 0; i < alcBin1.length; i++) {
			int[] c = Conversion.digitToBinary(alcCode1.charAt(i));
			for (int j = 0; j < alcBin1[i].length; j++)
				alcBin1[i][j] = c[j];
		}
		for (int i = 0; i < alcBin2.length; i++) {
			int[] c = Conversion.digitToBinary(alcCode2.charAt(i));
			for (int j = 0; j < alcBin2[i].length; j++)
				alcBin2[i][j] = c[j];
		}
	}

	private void loadRect() {
		// Load button rectangles
		buttonNOT = new BorderRect(394, 96, 48, 32, new Color(175, 165, 255));
		fill = new BorderRect(410, 560, 32, 32, Color.GREEN);
		randomize = new BorderRect(346, 40, 96, 32, Color.YELLOW);
		// Load entry rectangles
		for (int i = 0; i < entries.length; i++)
			entries[i] = new Rect((32 + (47 * i)), 560, 32, 32, Color.BLACK);
		// Load grids
		int[] gridX = { 80, 138, 196, 254 };
		int[] gridY = { 123, 294 };
		for (int i = 0; i < 8; i++)
			mainGrid[i] = new Rect(gridX[i / 2], gridY[i % 2], 58, 171, Color.BLACK);
		
		// Load hole rectangles and backgrounds
		for (int i = 0; i < holes.length; i++) {
			int x = 0;
			int y = 0;
			
			switch(i / 12) {
			case 0:
				x = 93;
				break;
			case 1:
				x = 152;
				break;
			case 2:
				x = 209;
				break;
			case 3:
				x = 269;
				break;
			}
			
			switch(i % 12) {
			case 0:
				y = 136;
				break;
			case 1:
				y = 164;
				break;
			case 2:
				y = 191;
				break;
			case 3:
				y = 219;
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

			holes[i] = new BorderRect(x, y, 30, 12, Color.WHITE);
		}

	}

	// Change rectangle positions when alchemy cards are toggled
	private void changeCards() {
		if (showAlcCards) {
			// Load button rectangles
			if (showOtherOps) {
				buttonAND = new BorderRect(353, 227, 48, 32, colAND);
				buttonOR = new BorderRect(353, 259, 48, 32, colOR);
			}
			else {
				buttonAND = new BorderRect(353, 291, 48, 32, colAND);
				buttonOR = new BorderRect(353, 323, 48, 32, colOR);
			}
			buttonXOR = new BorderRect(353, 291, 48, 32, colXOR);
			buttonNAND = new BorderRect(353, 323, 48, 32, colNAND);
			buttonNOR = new BorderRect(353, 355, 48, 32, colNOR);
			buttonXNOR = new BorderRect(353, 387, 48, 32, colXNOR);
			buttonNOT = new BorderRect(782, 96, 48, 32, new Color(175, 165, 255));
			buttonNOT1 = new BorderRect(282, 40, 48, 32, new Color(175, 165, 255));
			buttonNOT2 = new BorderRect(282, 335, 48, 32, new Color(175, 165, 255));
			fill = new BorderRect(798, 560, 32, 32, Color.green);
			fill1 = new BorderRect(281, 276, 20, 20, Color.green);
			fill2 = new BorderRect(281, 572, 20, 20, Color.green);
			randomize = new BorderRect(734, 40, 96, 32, Color.yellow);
			rand1 = new BorderRect(310, 276, 20, 20, Color.yellow);
			rand2 = new BorderRect(310, 572, 20, 20, Color.yellow);
			// Load entry rectangles
			for (int i = 0; i < entries.length; i++)
				entries[i] = new Rect((414 + (47 * i)), 560, 32, 32, Color.BLACK);
			for (int i = 0; i < entries.length; i++)
				alcEnt1[i] = new Rect(40 + (30 * i), 277, 20, 20, Color.BLACK);
			for (int i = 0; i < entries.length; i++)
				alcEnt2[i] = new Rect(40 + (30 * i), 572, 20, 20, Color.BLACK);
			// Load main grids
			for (int i = 0; i < mainGrid.length; i++)
				mainGrid[i].move(384, 0);
			// Load main hole rectangles and backgrounds
			for (int i = 0; i < holes.length; i++) {
				holes[i].move(384, 0);
			}
			int x = 128;
			int y = 86;
			// Load alchemy card hole rectangles
			for (int i = 0; i < alcHoles1.length; i++) {
				alcHoles1[i] = new BorderRect(x, y, 13, 5, Color.WHITE);
				alcHoles2[i] = new BorderRect(x, y + 294, 13, 5, Color.WHITE);
				if (i % 12 == 11) {
					x += 24;
					y = 86;
				}
				else
					y += 12;
			}

		}
		else
			loadRect();
		if (showAlcCards) {
			punchAlcHole1();
			punchAlcHole2();
		}
		punchHole();
		recolor = true;
	}

	// Fix the operation buttons when some are toggled
	private void fixButtons() {
		if (showAlcCards) {
			if (showOtherOps) {
				buttonAND = new BorderRect(353, 227, 48, 32, colAND);
				buttonOR = new BorderRect(353, 259, 48, 32, colOR);
			}
			else {
				buttonAND = new BorderRect(353, 291, 48, 32, colAND);
				buttonOR = new BorderRect(353, 323, 48, 32, colOR);
			}
			buttonXOR = new BorderRect(353, 291, 48, 32, colXOR);
			buttonNAND = new BorderRect(353, 323, 48, 32, colNAND);
			buttonNOR = new BorderRect(353, 355, 48, 32, colNOR);
			buttonXNOR = new BorderRect(353, 387, 48, 32, colXNOR);
		}
	}

	// Reset all codes to 00000000
	protected void resetCode(boolean prompt) {
		if (prompt) {
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset?", "Reset Codes",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, (Icon) gun);
			if (confirm == JOptionPane.YES_OPTION) {
				code = "00000000";
				alcCode1 = "00000000";
				alcCode2 = "00000000";
				punchHole();
				if (showAlcCards) {
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
			if (showAlcCards) {
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
		BufferedImage saveImg = new BufferedImage(captchaCard.getIconWidth(), captchaCard.getIconHeight(), 
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = saveImg.getGraphics();
		ImageIcon saveSymbol;
		boolean paintSymbol;
		
		// Select symbol for the saved image
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter imgFilter = new FileNameExtensionFilter("PNG & JPG Images", "png", "jpg");
		fc.setFileFilter(imgFilter);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setDialogTitle("Choose Symbol");
		int sel = fc.showOpenDialog(this);
		if (sel == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			saveSymbol = new ImageIcon(file.toString());
			int newHeight = saveSymbol.getIconHeight() * (saveSymbol.getIconWidth() / 200);
			saveSymbol = ImageUtil.rescaleImage(file, newHeight, 200);
			paintSymbol = true;
		}
		// If "Cancel" is pressed, the symbol isn't included on the card
		else {
			saveSymbol = symbol;
			paintSymbol = false;
		}
		
		// Set the symbol position, then paint the card and symbol
		int symbolY = (captchaCard.getIconHeight() / 2) - (saveSymbol.getIconHeight() / 2);
		captchaCard.paintIcon(this, g, 0, 0);
		if (paintSymbol)
			saveSymbol.paintIcon(this, g, 70, symbolY);
		
		// Determine hole positions and paint the filled ones
		for (int i = 0; i < holes.length; i++) {
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
			
			if (holes[i].getColor() == Color.BLACK) {
				g.setColor(Color.BLACK);
				g.fillRect(x, y, 29, 11);
			}
		}
		
		// Set filename
		String filename = (String) JOptionPane.showInputDialog(null, "Set Filename", "Save Card", 
				JOptionPane.INFORMATION_MESSAGE, (Icon) record, null, "card");
		if (filename == null)
			filename = "card";
		
		// Show preview with option to cancel
		JLabel lbl = new JLabel(new ImageIcon(saveImg));
		String[] previewOptions = {"Save", "Cancel"};
		int option = JOptionPane.showOptionDialog(null, lbl, "Preview", 0, 
				JOptionPane.INFORMATION_MESSAGE, (Icon) record, previewOptions, null);
		if (option == 0)
			saveImage(saveImg, filename);
		else
			JOptionPane.showMessageDialog(null, "Save canceled.", "Canceled", 
					JOptionPane.INFORMATION_MESSAGE, (Icon) record);
		
	}
	
	// Save the main card as an image
	private void saveImage(BufferedImage img, String fn) {
		try {
			File dir = new File("saves");
			if (!dir.exists())
				dir.mkdir();
	        ImageIO.write(img, "png", new File("saves/" + fn + ".png"));
	        JOptionPane.showMessageDialog(null, "The card has been saved! Check your saves folder.", "Saved",
					  JOptionPane.INFORMATION_MESSAGE, (Icon) record);
	    } catch (IOException ex) {
	        
	    }
	}

	// Show an error message
	private void errorMessage(String err) {
		if (err.equals("badLength"))
			JOptionPane.showMessageDialog(null, "Error: Code must be 8 digits long.", "Error", 
					JOptionPane.INFORMATION_MESSAGE, (Icon) weasel);
		else if (err.equals("badDigit"))
			JOptionPane.showMessageDialog(null, "Error: Code contains an invalid digit.", "Error", 
					JOptionPane.INFORMATION_MESSAGE, (Icon) weasel);
	}

	// Change the theme
	protected void changeTheme(String th, String ty) {
		// Since image files are named after the themes, the Images can be changed with one line of code each
		captchaCard = new ImageIcon(ResourceLoader.loadImage(ty + "/CaptchaCard" + th + ".png"));
		card1 = ImageUtil.rescaleImage(ResourceLoader.loadImage(ty + "/CaptchaCard" + th + ".png"), 226, 178);
		card2 = ImageUtil.rescaleImage(ResourceLoader.loadImage("miscellaneous/CaptchaCardBlank.png"), 226, 178);
		
		String tempTh = th;
		if (th.equals("Green (Jade)"))
			tempTh = th + " " + jadeSym;
		symbol = new ImageIcon(ResourceLoader.loadImage(ty + "/Symbol" + tempTh + ".png"));

		// Set variables for smaller symbol size
		double symHeight = (double) symbol.getIconHeight();
		double symWidth = (double) symbol.getIconWidth();
		double hToWRatio = symHeight / symWidth;
		int newHeight = (int) Math.round(80.0 * hToWRatio);
		symbolS = ImageUtil.rescaleImage(ResourceLoader.loadImage(ty + "/Symbol" + tempTh + ".png"), newHeight, 80);
		
		theme = th;
		type = ty;
		recolor = true;
	}

	// Jade's wardrobifier run method
	private class WardrobeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (theme.equals("Green (Jade)")) {
				try {
					jadeSym = gen.nextInt(10);
					changeTheme("Green (Jade)", "humans");
				} catch (Exception ex) {}
			}
		}
	}
	
	// Load settings from json file
	protected void loadSettings() throws JSONException {
		// Set up scanner and JSONObject
		File config = new File("config.json");
		JSONObject options = new JSONObject();
		String jsonStr = "";
		Scanner reader;
		
		try {
			// Create new file if it doesn't exist
			if (!config.exists()) {
				config.createNewFile();
			}
			// Populate JSONObject with options if it doesn't exist
			else {
				reader = new Scanner(config);
				while(reader.hasNextLine())
					jsonStr += reader.nextLine();
				reader.close();
				options = new JSONObject(jsonStr);
			}
		} catch (IOException | JSONException e) { }
		
		// Set variables to options
		theme = options.has("theme") ? options.getString("theme") : "Blue (John)";
		type = options.has("type") ? options.getString("type") : "humans";
		jadeSym = options.has("jade") ? options.getInt("jade") : 0;
		showSymbol = options.has("symbol") ? options.getBoolean("symbol") : false;
		showAlcCards = options.has("alchemy") ? options.getBoolean("alchemy") : false;
		showGrids = options.has("grids") ? options.getBoolean("grids") : false;
		showOtherOps = options.has("operations") ? options.getBoolean("operations") : false;
		code = options.has("main_code") ? options.getString("main_code") : "00000000";
		alcCode1 = options.has("alchemy_code_1") ? options.getString("alchemy_code_1") : "00000000";
		alcCode2 = options.has("alchemy_code_2") ? options.getString("alchemy_code_2") : "00000000";
		operation = options.has("oper") ? options.getString("oper") : "NONE";
	}
	
	// Change various settings
	protected void changeSettings(String opt) {
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
					version, "About", JOptionPane.INFORMATION_MESSAGE, (Icon) mspa);
			break;
		// For the Shortcuts menu
		case "Shortcuts":
			String sc = "";
			for(int i = 0; i < shortcuts.length; i += 2)
				sc += String.format("%-45s%s%n", shortcuts[i], shortcuts[i + 1]);
			JOptionPane.showMessageDialog(null, sc, "Shortcuts", JOptionPane.INFORMATION_MESSAGE, (Icon) apple);
			break;
		}
	}

	// Save options to json file
	protected void saveSettings(String filename) throws Exception {
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
		Files.write(Paths.get(filename), options.toString(4).getBytes());
	}

	// Reset operation button colors (remove any highlights)
	private void resetHighlight() {
		operation = "NONE";
		recolor = true;
	}

	// Get the GUI width to change in the GUIWindow program
	public boolean getCards() {
		return showAlcCards;
	}

}
