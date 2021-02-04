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
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class CaptchaPanel extends JPanel{

	private ImageIcon captchaCard, john;			// Images for the card asset and temporary "???" asset
	private Conversion cv = new Conversion();		// Class for conversion and operations
	private Randomize gen = new Randomize();		// Class for randomizing codes
	final JFileChooser fc = new JFileChooser();		// File chooser for loading codes
	private int[][] binary = new int[8][6];			// Binary for main code
	private int[][] alcBin1 = new int[8][6];		// Binary for 1st alchemy code
	private int[][] alcBin2 = new int[8][6];		// Binary for 2nd alchemy code
	private Rect holes[] = new Rect[48];			// Rect array for holes
	// Rect arrays for code digits
	private Rect entries[] = new Rect[8];
	private Rect alcEnt1[] = new Rect[8];
	private Rect alcEnt2[] = new Rect[8];
	// String arrays for code digits
	private String digits[] = new String[8];
	private String alcDig1[] = new String[8];
	private String alcDig2[] = new String[8];
	// All button rectangles
	private Rect buttonAND, buttonOR, buttonXOR, buttonNOT, reset, fill, fill1, fill2,
				 optionsButton, randomize, rand1, rand2, save, load;
	// All button border rectangles (black outline)
	private Rect borderAND, borderOR, borderXOR, borderNOT, borderReset, borderFill, borderFill1, borderFill2,
				 borderOptions, borderRandomize, borderRand1, borderRand2, borderSave, borderLoad;
	// Strings for codes and code updates
	private String code = "";
	private String alcCode1 = "";
	private String alcCode2 = "";
	private String codeUpdate = "";
	private String codeUpdate1 = "";
	private String codeUpdate2 = "";
	// About page information
	private String author = "Safety";
	private String designer = "Detective Dyn";
	private String version = "0.4";
	// Integer that tracks which code digit is being changed
	private int entryNo = -1;
	// Booleans for each condition in the PaintComponent
	private boolean entryUpdate = false;
	private boolean alcEnt1Update = false;
	private boolean alcEnt2Update = false;
	private boolean updateHole = false;
	private boolean updateCode = false;
	private boolean updateAlcCode1 = false;
	private boolean updateAlcCode2 = false;
	private boolean alchemize = false;
	private boolean resetCode = false;
	private boolean resetHighlight = false;
	private boolean recolor = false;
	// Main font for button text
	private Font f = new Font("Courier", Font.BOLD, 26);
	// Operation button (and highlight) colors
	private Color colAND = Color.cyan;
	private Color colOR = Color.cyan;
	private Color colXOR = Color.cyan;
	private Color highlight = new Color(50, 150, 150);

	public CaptchaPanel() throws FileNotFoundException{
		this.setBackground(Color.white);
		// Set all codes to 00000000 when the program is launched
		code = "00000000";
		alcCode1 = "00000000";
		alcCode2 = "00000000";
		// Load rectangles and "punch holes"
		loadRect();
		punchHole();
		// Select default captcha card asset
		captchaCard = new ImageIcon("images/CaptchaCardBlue (John).png");
		john = new ImageIcon("images/ConfusedJohn.png");
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
		// Add code digits
		for (int i = 0; i < digits.length; i++){
			digits[i] = "";
			digits[i] += code.charAt(i);
			g.drawString(digits[i], (225 + (47 * i)), 584);
		}
		for (int i = 0; i < alcDig1.length; i++) {
			alcDig1[i] = "";
			alcDig1[i] += alcCode1.charAt(i);
			g.drawString(alcDig1[i], 65, (120 + (50 * i)));
		}
		for (int i = 0; i < alcDig2.length; i++) {
			alcDig2[i] = "";
			alcDig2[i] += alcCode2.charAt(i);
			g.drawString(alcDig2[i], 129, (120 + (50 * i)));
		}
		// Add captcha card asset
		captchaCard.paintIcon(this, g, 208, 40);
		// Draw operation buttons
		buttonAND.draw(g);
		buttonAND.setFilled(true);
		borderAND.draw(g);
		buttonOR.draw(g);
		buttonOR.setFilled(true);
		borderOR.draw(g);
		buttonXOR.draw(g);
		buttonXOR.setFilled(true);
		borderXOR.draw(g);
		buttonNOT.draw(g);
		buttonNOT.setFilled(true);
		borderNOT.draw(g);
		// Draw other buttons
		reset.draw(g);
		reset.setFilled(true);
		borderReset.draw(g);
		fill.draw(g);
		fill.setFilled(true);
		borderFill.draw(g);
		fill1.draw(g);
		fill1.setFilled(true);
		borderFill1.draw(g);
		fill2.draw(g);
		fill2.setFilled(true);
		borderFill2.draw(g);
		optionsButton.draw(g);
		optionsButton.setFilled(true);
		borderOptions.draw(g);
		randomize.draw(g);
		randomize.setFilled(true);
		borderRandomize.draw(g);
		rand1.draw(g);
		rand1.setFilled(true);
		borderRand1.draw(g);
		rand2.draw(g);
		rand2.setFilled(true);
		borderRand2.draw(g);
		save.draw(g);
		save.setFilled(true);
		borderSave.draw(g);
		load.draw(g);
		load.setFilled(true);
		borderLoad.draw(g);
		// Draw operation strings
		g.drawString("&&", 39, 64);
		g.drawString("||", 88, 63);
		g.drawString("^^", 138, 68);
		g.drawString("~~", 397, 634);
		// Draw other strings
		g.drawString("RESET", 42, 634);
		g.drawString("OPTIONS", 518, 64);
		g.drawString("^", 66, 520);
		g.drawString("^", 130, 520);
		g.drawString("<", 603, 584);
		g.drawString("RANDOMIZE", 478, 634);
		g.drawString("R", 65, 566);
		g.drawString("R", 129, 566);
		g.drawString("SAVE", 171, 634);
		g.drawString("LOAD", 283, 634);
		// Draw code digit rectangles
		for (int i = 0; i < entries.length; i++) {
			entries[i].draw(g);
			alcEnt1[i].draw(g);
			alcEnt2[i].draw(g);
		}
		// Draw holes
		for (int i = 0; i < holes.length; i++)
			holes[i].draw(g);
		repaint();
		// Update the code when a hole is toggled
		if(updateHole){
			code = codeUpdate;
			repaint();
			codeUpdate = "";
			updateHole = false;
		}
		// Update the holes (and code) when the code is changed, filled, or randomized
		if(updateCode){
			code = codeUpdate;
			punchHole();
			repaint();
			codeUpdate = "";
			updateCode = false;
		}
		// Update the 1st alchemy code when it's changed, filled, or randomized
		if(updateAlcCode1){
			alcCode1 = codeUpdate1;
			repaint();
			codeUpdate1 = "";
			updateAlcCode1 = false;
		}
		// Update the 2nd alchemy code when it's changed, filled, or randomized
		if(updateAlcCode2){
			alcCode2 = codeUpdate2;
			repaint();
			codeUpdate2 = "";
			updateAlcCode2 = false;
		}
		// Update the holes (and set button highlight) when an operation is performed
		if(alchemize){
			buttonAND = new Rect(32, 40, 48, 32, colAND);
			buttonOR = new Rect(80, 40, 48, 32, colOR);
			buttonXOR = new Rect(128, 40, 48, 32, colXOR);
			punchHole();
			repaint();
			alchemize = false;
		}
		// Revert all codes back to 00000000 and update the holes
		if(resetCode){
			code = "00000000";
			alcCode1 = "00000000";
			alcCode2 = "00000000";
			punchHole();
			repaint();
			resetHighlight = true;
			resetCode = false;
		}
		// Change all operation buttons back to their original color (removing any highlight)
		if(resetHighlight){
			colAND = Color.cyan;
			colOR = Color.cyan;
			colXOR = Color.cyan;
			buttonAND = new Rect(32, 40, 48, 32, colAND);
			buttonOR = new Rect(80, 40, 48, 32, colOR);
			buttonXOR = new Rect(128, 40, 48, 32, colXOR);
			repaint();
			resetHighlight = false;
		}
		// Change the theme (just repaints)
		if(recolor){
			repaint();
			recolor = false;
		}
	}

	private class HoleListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			// Set variables for mouse position
			int mouseX = e.getX();
			int mouseY = e.getY();

			// If the "&&" button is clicked, the AND operation is performed
			if(buttonAND.containsPoint(mouseX, mouseY)){
				fillBin();
				binary = cv.functionAND(alcBin1, alcBin2);
				colAND = highlight;
				colOR = Color.cyan;
				colXOR = Color.cyan;
				updateHole = true;
				alchemize = true;
			}
			// If the "||" button is clicked, the OR operation is performed
			if(buttonOR.containsPoint(mouseX, mouseY)){
				fillBin();
				binary = cv.functionOR(alcBin1, alcBin2);
				colAND = Color.cyan;
				colOR = highlight;
				colXOR = Color.cyan;
				updateHole = true;
				alchemize = true;
			}
			// If the "^^" button is clicked, the XOR operation is performed
			if(buttonXOR.containsPoint(mouseX, mouseY)){
				fillBin();
				binary = cv.functionXOR(alcBin1, alcBin2);
				colAND = Color.cyan;
				colOR = Color.cyan;
				colXOR = highlight;
				updateHole = true;
				alchemize = true;
			}
			// If the "~~" button is clicked, the NOT operation is performed (on the main code)
			if(buttonNOT.containsPoint(mouseX, mouseY)){
				fillBin();
				binary = cv.functionNOT(binary);
				resetHighlight = true;
				updateHole = true;
				alchemize = true;
			}
			// If the "RESET" button is clicked, all codes are zeroed
			if(reset.containsPoint(mouseX, mouseY)){
				int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset?", "Confirm Reset", JOptionPane.YES_NO_OPTION);
				if(confirm == JOptionPane.YES_OPTION)
					resetCode = true;
				else
					resetCode = false;
			}
			// If any of the "<" or "^" buttons are clicked, the corresponding field is filled in with the user input.
			if(fill.containsPoint(mouseX, mouseY)){
				String update = (String) JOptionPane.showInputDialog("Enter Code", "00000000");
				codeUpdate = update.trim();
				// The code is set to 00000000 if the input isn't 8 digits long
				if(codeUpdate.length() != 8)
					codeUpdate = "00000000";
				updateCode = true;
				resetHighlight = true;
			}
			if(fill1.containsPoint(mouseX, mouseY)){
				String update = (String) JOptionPane.showInputDialog("Enter Code", "00000000");
				codeUpdate1 = update.trim();
				if(codeUpdate1.length() != 8)
					codeUpdate1 = "00000000";
				updateAlcCode1 = true;
				resetHighlight = true;
			}
			if(fill2.containsPoint(mouseX, mouseY)){
				String update = (String) JOptionPane.showInputDialog("Enter Code", "00000000");
				codeUpdate2 = update.trim();
				if(codeUpdate2.length() != 8)
					codeUpdate2 = "00000000";
				updateAlcCode2 = true;
				resetHighlight = true;
			}
			// If the "OPTIONS" button is clicked, a dropdown list of options is displayed
			if(optionsButton.containsPoint(mouseX, mouseY)){
				String[] options = {"Change Theme", "About", "???"};
				String input = (String) JOptionPane.showInputDialog(null, "Select an option:", "Settings",
						       JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				changeSettings(input);
			}
			// If any of the "RANDOMIZE" or "R" buttons are clicked, the corresponding code is randomized
			if(randomize.containsPoint(mouseX, mouseY)){
				codeUpdate = "";
				for (int i = 0; i < 8; i++)
					codeUpdate += gen.getChar();
				updateCode = true;
				resetHighlight = true;
			}
			if(rand1.containsPoint(mouseX, mouseY)){
				codeUpdate1 = "";
				for (int i = 0; i < 8; i++)
					codeUpdate1 += gen.getChar();
				updateAlcCode1 = true;
				resetHighlight = true;
			}
			if(rand2.containsPoint(mouseX, mouseY)){
				codeUpdate2 = "";
				for (int i = 0; i < 8; i++)
					codeUpdate2 += gen.getChar();
				updateAlcCode2 = true;
				resetHighlight = true;
			}
			// If the "SAVE" button is clicked, the main code is saved to the "saves" folder
			if(save.containsPoint(mouseX, mouseY)){
				// A dialog box asks for the what the file will be titled
				String filename = (String) JOptionPane.showInputDialog("Enter Filename", code);
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
			// If the "LOAD" button is clicked, a selected text file's code is loaded on to the chosen field(s)
			if(load.containsPoint(mouseX, mouseY)){
				// The file prompt only show files ending in .txt
				fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
				// The method of loading is selected
				String[] options = {"One Code (Main)", "Two Codes (Alchemy)"};
				String input = (String) JOptionPane.showInputDialog(null, "Select a Method", "Load Code",
										JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				// The directory is set automatically
				fc.setCurrentDirectory(new File("saves"));
				// Multiple file selection is enabled if the "Two Codes" method is chosen
				if(input.equals("One Code (Main)"))
					fc.setMultiSelectionEnabled(false);
				else if(input.equals("Two Codes (Alchemy)"))
					fc.setMultiSelectionEnabled(true);
				int returnVal = fc.showOpenDialog(CaptchaPanel.this);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					// Single-code method applies to the main field
					if(input.equals("One Code (Main)")){
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
					else if(input.equals("Two Codes (Alchemy)")){
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
				resetHighlight = true;
			}

			// If a single digit is clicked, the location is logged and it can then be changed
			for (int i = 0; i < entries.length; i++) {
				Rect q = entries[i];
				if(q.containsPoint(mouseX, mouseY)){
					entryNo = i;
					resetHighlight = true;
					entryUpdate = true;
					break;
				}
			}
			for (int i = 0; i < alcEnt1.length; i++) {
				Rect q = alcEnt1[i];
				if(q.containsPoint(mouseX, mouseY)){
					entryNo = i;
					resetHighlight = true;
					alcEnt1Update = true;
					break;
				}
			}
			for (int i = 0; i < alcEnt2.length; i++) {
				Rect q = alcEnt2[i];
				if(q.containsPoint(mouseX, mouseY)){
					entryNo = i;
					resetHighlight = true;
					alcEnt2Update = true;
					break;
				}
			}

			// If a hole is clicked, it will toggle on or off
			for (int i = 0; i < holes.length; i++) {
				Rect r = holes[i];
				if(r.containsPoint(mouseX, mouseY)) {
					if(r.isFilled()){
						r.setFilled(false);
						binary[i / 6][i % 6] = 0;
					}
					else{
						r.setFilled(true);
						binary[i / 6][i % 6] = 1;
					}
					resetHighlight = true;
					updateHole = true;
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
		buttonAND = new Rect(32, 40, 48, 32, colAND);
		buttonOR = new Rect(80, 40, 48, 32, colOR);
		buttonXOR = new Rect(128, 40, 48, 32, colXOR);
		buttonNOT = new Rect(388, 610, 48, 32, new Color(175, 165, 255));
		reset = new Rect(32, 610, 96, 32, Color.red);
		fill = new Rect(594, 560, 32, 32, Color.green);
		fill1 = new Rect(56, 493, 32, 32, Color.green);
		fill2 = new Rect(120, 493, 32, 32, Color.green);
		optionsButton = new Rect(510, 40, 126, 32, Color.pink);
		randomize = new Rect(470, 610, 156, 32, Color.yellow);
		rand1 = new Rect(56, 542, 32, 32, Color.yellow);
		rand2 = new Rect(120, 542, 32, 32, Color.yellow);
		save = new Rect(162, 610, 80, 32, Color.lightGray);
		load = new Rect(274, 610, 80, 32, Color.lightGray);
		// Load button borders (black outline)
		borderAND = new Rect(32, 40, 48, 32, Color.black);
		borderOR = new Rect(80, 40, 48, 32, Color.black);
		borderXOR = new Rect(128, 40, 48, 32, Color.black);
		borderNOT = new Rect(388, 610, 48, 32, Color.black);
		borderReset = new Rect(32, 610, 96, 32, Color.black);
		borderFill = new Rect(594, 560, 32, 32, Color.black);
		borderFill1 = new Rect(56, 493, 32, 32, Color.black);
		borderFill2 = new Rect(120, 493, 32, 32, Color.black);
		borderOptions = new Rect(510, 40, 126, 32, Color.black);
		borderRandomize = new Rect(470, 610, 156, 32, Color.black);
		borderRand1 = new Rect(56, 542, 32, 32, Color.black);
		borderRand2 = new Rect(120, 542, 32, 32, Color.black);
		borderSave = new Rect(162, 610, 80, 32, Color.black);
		borderLoad = new Rect(274, 610, 80, 32, Color.black);
		// Load entry rectangles
		for (int i = 0; i < entries.length; i++)
			entries[i] = new Rect((216 + (47 * i)), 560, 32, 32, Color.black);
		for (int i = 0; i < entries.length; i++)
			alcEnt1[i] = new Rect(56, (96 + (50 * i)), 32, 32, Color.black);
		for (int i = 0; i < entries.length; i++)
			alcEnt2[i] = new Rect(120, (96 + (50 * i)), 32, 32, Color.black);
		// Load hole rectangles
		holes[0] = new Rect(263, 138, 30, 12, Color.black);
		holes[1] = new Rect(263, 166, 30, 12, Color.black);
		holes[2] = new Rect(263, 193, 30, 12, Color.black);
		holes[3] = new Rect(263, 221, 30, 12, Color.black);
		holes[4] = new Rect(263, 248, 30, 12, Color.black);
		holes[5] = new Rect(263, 276, 30, 12, Color.black);
		holes[6] = new Rect(263, 303, 30, 12, Color.black);
		holes[7] = new Rect(263, 331, 30, 12, Color.black);
		holes[8] = new Rect(263, 356, 30, 12, Color.black);
		holes[9] = new Rect(263, 384, 30, 12, Color.black);
		holes[10] = new Rect(263, 411, 30, 12, Color.black);
		holes[11] = new Rect(263, 439, 30, 12, Color.black);
		holes[12] = new Rect(321, 138, 30, 12, Color.black);
		holes[13] = new Rect(321, 166, 30, 12, Color.black);
		holes[14] = new Rect(321, 193, 30, 12, Color.black);
		holes[15] = new Rect(321, 221, 30, 12, Color.black);
		holes[16] = new Rect(321, 248, 30, 12, Color.black);
		holes[17] = new Rect(321, 276, 30, 12, Color.black);
		holes[18] = new Rect(321, 303, 30, 12, Color.black);
		holes[19] = new Rect(321, 331, 30, 12, Color.black);
		holes[20] = new Rect(321, 356, 30, 12, Color.black);
		holes[21] = new Rect(321, 384, 30, 12, Color.black);
		holes[22] = new Rect(321, 411, 30, 12, Color.black);
		holes[23] = new Rect(321, 439, 30, 12, Color.black);
		holes[24] = new Rect(379, 138, 30, 12, Color.black);
		holes[25] = new Rect(379, 166, 30, 12, Color.black);
		holes[26] = new Rect(379, 193, 30, 12, Color.black);
		holes[27] = new Rect(379, 221, 30, 12, Color.black);
		holes[28] = new Rect(379, 248, 30, 12, Color.black);
		holes[29] = new Rect(379, 276, 30, 12, Color.black);
		holes[30] = new Rect(379, 303, 30, 12, Color.black);
		holes[31] = new Rect(379, 331, 30, 12, Color.black);
		holes[32] = new Rect(379, 356, 30, 12, Color.black);
		holes[33] = new Rect(379, 384, 30, 12, Color.black);
		holes[34] = new Rect(379, 411, 30, 12, Color.black);
		holes[35] = new Rect(379, 439, 30, 12, Color.black);
		holes[36] = new Rect(438, 138, 30, 12, Color.black);
		holes[37] = new Rect(438, 166, 30, 12, Color.black);
		holes[38] = new Rect(438, 193, 30, 12, Color.black);
		holes[39] = new Rect(438, 221, 30, 12, Color.black);
		holes[40] = new Rect(438, 248, 30, 12, Color.black);
		holes[41] = new Rect(438, 276, 30, 12, Color.black);
		holes[42] = new Rect(438, 303, 30, 12, Color.black);
		holes[43] = new Rect(438, 331, 30, 12, Color.black);
		holes[44] = new Rect(438, 356, 30, 12, Color.black);
		holes[45] = new Rect(438, 384, 30, 12, Color.black);
		holes[46] = new Rect(438, 411, 30, 12, Color.black);
		holes[47] = new Rect(438, 439, 30, 12, Color.black);
	}
	
	private void changeSettings(String opt){
		// For the Change Theme option
		if(opt.equals("Change Theme")){
			String[] colors = {"Blue (John)", "Orchid (Rose)", "Red (Dave)", "Green (Jade)",
							   "Cyan (Jane)", "Pink (Roxy)", "Orange (Dirk)", "Emerald (Jake)",
							   "Rust (Aradia)", "Bronze (Tavros)", "Gold (Sollux)", "Grey (Karkat)",
							   "Olive (Nepeta)", "Jade (Kanaya)", "Teal (Terezi)", "Cobalt (Vriska)",
							   "Indigo (Equius)", "Purple (Gamzee)", "Violet (Eridan)", "Fuchsia (Feferi)"};
			String input = (String) JOptionPane.showInputDialog(null, "Select a Theme", "Theme Selection",
					JOptionPane.QUESTION_MESSAGE, null, colors, colors[0]);
			// Since image files are named after the themes, the ImageIcon can be changed with one line of code
			captchaCard = new ImageIcon("images/CaptchaCard" + input + ".png");
			recolor = true;
		}
		// For the About option
		else if(opt.equals("About"))
			JOptionPane.showMessageDialog(null, "Programmer: " + author + "\nDesigner: " + designer + "\nVersion: " + 
										  version, "About", JOptionPane.INFORMATION_MESSAGE);
		// For the ??? option, which currently does nothing
		else if(opt.equals("???")){
			JOptionPane.showMessageDialog(null, "This doesn't do anything yet, but I'm working on it!",
										  "Coming soon...", JOptionPane.INFORMATION_MESSAGE, john);
		}
	}

}
