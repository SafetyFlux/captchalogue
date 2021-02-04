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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;


public class CaptchaPanel extends JPanel{

	private ImageIcon captchaCard, john;
	private Conversion cv = new Conversion();
	private Randomize gen = new Randomize();
	final JFileChooser fc = new JFileChooser();
	private int[][] binary = new int[8][6];
	private int[][] alcBin1 = new int[8][6];
	private int[][] alcBin2 = new int[8][6];
	private Rect holes[] = new Rect[48];
	private Rect borderHoles[] = new Rect[48];
	private Rect entries[] = new Rect[8];
	private Rect alcEnt1[] = new Rect[8];
	private Rect alcEnt2[] = new Rect[8];
	private String digits[] = new String[8];
	private String alcDig1[] = new String[8];
	private String alcDig2[] = new String[8];
	private Rect buttonAND, buttonOR, buttonXOR, buttonNOT, reset, fill, fill1, fill2,
				 optionsButton, randomize, rand1, rand2, save, load;
	private Rect borderAND, borderOR, borderXOR, borderNOT, borderReset, borderFill, borderFill1, borderFill2,
				 borderOptions, borderRandomize, borderRand1, borderRand2, borderSave, borderLoad;
	private String code = "";
	private String alcCode1 = "";
	private String alcCode2 = "";
	private String codeUpdate = "";
	private String codeUpdate1 = "";
	private String codeUpdate2 = "";
	private String author = "Safety";
	private String version = "0.4";
	private int entryNo = -1;
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
	private Font f = new Font("Courier", Font.BOLD, 26);
	private Color colAND = Color.cyan;
	private Color colOR = Color.cyan;
	private Color colXOR = Color.cyan;
	private Color highlight = new Color(50, 150, 150);

	public CaptchaPanel() throws FileNotFoundException{
		this.setBackground(Color.white);
		code = "00000000";
		alcCode1 = "00000000";
		alcCode2 = "00000000";
		loadRect();
		punchHole();
		captchaCard = new ImageIcon("images/CaptchaCardBlue (John).png");
		john = new ImageIcon("images/ConfusedJohn.png");
		addMouseListener(new HoleListener());
		addKeyListener(new EntryListener());
		setFocusable(true);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setFont(f);
		g.setColor(Color.black);
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
		captchaCard.paintIcon(this, g, 208, 40);
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
		g.drawString("&&", 39, 64);
		g.drawString("||", 88, 63);
		g.drawString("^^", 138, 68);
		g.drawString("~~", 397, 634);
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
		for (int i = 0; i < entries.length; i++) {
			entries[i].draw(g);
			alcEnt1[i].draw(g);
			alcEnt2[i].draw(g);
		}
		for (int i = 0; i < holes.length; i++)
			holes[i].draw(g);
		for (int i = 0; i < borderHoles.length; i++)
			borderHoles[i].draw(g);
		repaint();
		if(updateHole){
			code = codeUpdate;
			repaint();
			codeUpdate = "";
			updateHole = false;
		}
		if(updateCode){
			code = codeUpdate;
			punchHole();
			repaint();
			codeUpdate = "";
			updateCode = false;
		}
		if(updateAlcCode1){
			alcCode1 = codeUpdate1;
			repaint();
			codeUpdate1 = "";
			updateAlcCode1 = false;
		}
		if(updateAlcCode2){
			alcCode2 = codeUpdate2;
			repaint();
			codeUpdate2 = "";
			updateAlcCode2 = false;
		}
		if(alchemize){
			buttonAND = new Rect(32, 40, 48, 32, colAND);
			buttonOR = new Rect(80, 40, 48, 32, colOR);
			buttonXOR = new Rect(128, 40, 48, 32, colXOR);
			punchHole();
			repaint();
			alchemize = false;
		}
		if(resetCode){
			code = "00000000";
			alcCode1 = "00000000";
			alcCode2 = "00000000";
			punchHole();
			repaint();
			resetHighlight = true;
			resetCode = false;
		}
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
		if(recolor){
			repaint();
			recolor = false;
		}
	}

	private class HoleListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			int mouseX = e.getX();
			int mouseY = e.getY();

			if(buttonAND.containsPoint(mouseX, mouseY)){
				fillBin();
				binary = cv.functionAND(alcBin1, alcBin2);
				colAND = highlight;
				colOR = Color.cyan;
				colXOR = Color.cyan;
				updateHole = true;
				alchemize = true;
			}
			if(buttonOR.containsPoint(mouseX, mouseY)){
				fillBin();
				binary = cv.functionOR(alcBin1, alcBin2);
				colAND = Color.cyan;
				colOR = highlight;
				colXOR = Color.cyan;
				updateHole = true;
				alchemize = true;
			}
			if(buttonXOR.containsPoint(mouseX, mouseY)){
				fillBin();
				binary = cv.functionXOR(alcBin1, alcBin2);
				colAND = Color.cyan;
				colOR = Color.cyan;
				colXOR = highlight;
				updateHole = true;
				alchemize = true;
			}
			if(buttonNOT.containsPoint(mouseX, mouseY)){
				fillBin();
				binary = cv.functionNOT(binary);
				resetHighlight = true;
				updateHole = true;
				alchemize = true;
			}
			if(reset.containsPoint(mouseX, mouseY)){
				int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset?", "Confirm Reset", JOptionPane.YES_NO_OPTION);
				if(confirm == JOptionPane.YES_OPTION)
					resetCode = true;
				else
					resetCode = false;
			}
			if(fill.containsPoint(mouseX, mouseY)){
				String update = (String) JOptionPane.showInputDialog("Enter Code", "00000000");
				codeUpdate = update.trim();
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
			if(optionsButton.containsPoint(mouseX, mouseY)){
				String[] options = {"Change Theme", "About", "???"};
				String input = (String) JOptionPane.showInputDialog(null, "Select an option:", "Settings",
						       JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				changeSettings(input);
			}
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
			if(save.containsPoint(mouseX, mouseY)){
				String filename = (String) JOptionPane.showInputDialog("Enter Filename", code);
				File directory = new File("saves");
				try {
					if(!directory.exists())
						directory.mkdir();
					PrintWriter writer = new PrintWriter(new File("saves/" + filename + ".txt"));
					writer.print(code);
					writer.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			if(load.containsPoint(mouseX, mouseY)){
				fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
				String[] options = {"One Code (Main)", "Two Codes (Alchemy)"};
				String input = (String) JOptionPane.showInputDialog(null, "Select a Method", "Load Code", JOptionPane.QUESTION_MESSAGE,
						null, options, options[0]);
				fc.setCurrentDirectory(new File("saves"));
				if(input.equals("One Code (Main)"))
					fc.setMultiSelectionEnabled(false);
				else if(input.equals("Two Codes (Alchemy)"))
					fc.setMultiSelectionEnabled(true);
				int returnVal = fc.showOpenDialog(CaptchaPanel.this);
				if(returnVal == JFileChooser.APPROVE_OPTION){
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
					else if(input.equals("Two Codes (Alchemy)")){
						File[] files = fc.getSelectedFiles();
						try {
							if(files.length == 1){
								Scanner reader = new Scanner(files[0]);
								codeUpdate1 = reader.nextLine();
								codeUpdate2 = codeUpdate1;
								reader.close();
							}
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
						updateAlcCode1 = true;
						updateAlcCode2 = true;
					}
				}
				resetHighlight = true;
			}

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

		public void keyTyped(KeyEvent e){
			char d = '0';
			if(entryUpdate){
				if(!e.isActionKey() && e.getKeyCode() != KeyEvent.VK_SHIFT)
					d = e.getKeyChar();
				else
					d = '0';
				for (int i = 0; i < code.length(); i++) {
					if(i == entryNo)
						codeUpdate += d;
					else
						codeUpdate += code.charAt(i);
				}
				entryUpdate = false;
				updateCode = true;
			}
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
		// Load other rectangles
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
		// Load hole border rectangles
		borderHoles[0] = new Rect(263, 138, 30, 12, Color.black);
		borderHoles[1] = new Rect(263, 166, 30, 12, Color.black);
		borderHoles[2] = new Rect(263, 193, 30, 12, Color.black);
		borderHoles[3] = new Rect(263, 221, 30, 12, Color.black);
		borderHoles[4] = new Rect(263, 248, 30, 12, Color.black);
		borderHoles[5] = new Rect(263, 276, 30, 12, Color.black);
		borderHoles[6] = new Rect(263, 303, 30, 12, Color.black);
		borderHoles[7] = new Rect(263, 331, 30, 12, Color.black);
		borderHoles[8] = new Rect(263, 356, 30, 12, Color.black);
		borderHoles[9] = new Rect(263, 384, 30, 12, Color.black);
		borderHoles[10] = new Rect(263, 411, 30, 12, Color.black);
		borderHoles[11] = new Rect(263, 439, 30, 12, Color.black);
		borderHoles[12] = new Rect(321, 138, 30, 12, Color.black);
		borderHoles[13] = new Rect(321, 166, 30, 12, Color.black);
		borderHoles[14] = new Rect(321, 193, 30, 12, Color.black);
		borderHoles[15] = new Rect(321, 221, 30, 12, Color.black);
		borderHoles[16] = new Rect(321, 248, 30, 12, Color.black);
		borderHoles[17] = new Rect(321, 276, 30, 12, Color.black);
		borderHoles[18] = new Rect(321, 303, 30, 12, Color.black);
		borderHoles[19] = new Rect(321, 331, 30, 12, Color.black);
		borderHoles[20] = new Rect(321, 356, 30, 12, Color.black);
		borderHoles[21] = new Rect(321, 384, 30, 12, Color.black);
		borderHoles[22] = new Rect(321, 411, 30, 12, Color.black);
		borderHoles[23] = new Rect(321, 439, 30, 12, Color.black);
		borderHoles[24] = new Rect(379, 138, 30, 12, Color.black);
		borderHoles[25] = new Rect(379, 166, 30, 12, Color.black);
		borderHoles[26] = new Rect(379, 193, 30, 12, Color.black);
		borderHoles[27] = new Rect(379, 221, 30, 12, Color.black);
		borderHoles[28] = new Rect(379, 248, 30, 12, Color.black);
		borderHoles[29] = new Rect(379, 276, 30, 12, Color.black);
		borderHoles[30] = new Rect(379, 303, 30, 12, Color.black);
		borderHoles[31] = new Rect(379, 331, 30, 12, Color.black);
		borderHoles[32] = new Rect(379, 356, 30, 12, Color.black);
		borderHoles[33] = new Rect(379, 384, 30, 12, Color.black);
		borderHoles[34] = new Rect(379, 411, 30, 12, Color.black);
		borderHoles[35] = new Rect(379, 439, 30, 12, Color.black);
		borderHoles[36] = new Rect(438, 138, 30, 12, Color.black);
		borderHoles[37] = new Rect(438, 166, 30, 12, Color.black);
		borderHoles[38] = new Rect(438, 193, 30, 12, Color.black);
		borderHoles[39] = new Rect(438, 221, 30, 12, Color.black);
		borderHoles[40] = new Rect(438, 248, 30, 12, Color.black);
		borderHoles[41] = new Rect(438, 276, 30, 12, Color.black);
		borderHoles[42] = new Rect(438, 303, 30, 12, Color.black);
		borderHoles[43] = new Rect(438, 331, 30, 12, Color.black);
		borderHoles[44] = new Rect(438, 356, 30, 12, Color.black);
		borderHoles[45] = new Rect(438, 384, 30, 12, Color.black);
		borderHoles[46] = new Rect(438, 411, 30, 12, Color.black);
		borderHoles[47] = new Rect(438, 439, 30, 12, Color.black);
	}
	
	private void changeSettings(String opt){
		if(opt.equals("Change Theme")){
			String[] colors = {"Blue (John)", "Orchid (Rose)", "Red (Dave)", "Green (Jade)",
							   "Cyan (Jane)", "Pink (Roxy)", "Orange (Dirk)", "Emerald (Jake)",
							   "Rust (Aradia)", "Bronze (Tavros)", "Gold (Sollux)", "Grey (Karkat)",
							   "Olive (Nepeta)", "Jade (Kanaya)", "Teal (Terezi)", "Cobalt (Vriska)",
							   "Indigo (Equius)", "Purple (Gamzee)", "Violet (Eridan)", "Fuchsia (Feferi)"};
			String input = (String) JOptionPane.showInputDialog(null, "Select a Theme", "Theme Selection",
					JOptionPane.QUESTION_MESSAGE, null, colors, colors[0]);
			captchaCard = new ImageIcon("images/CaptchaCard" + input + ".png");
			recolor = true;
		}
		else if(opt.equals("About"))
			JOptionPane.showMessageDialog(null, "Author: " + author + "\nVersion: " + version, "About",
										  JOptionPane.INFORMATION_MESSAGE);
		else if(opt.equals("???")){
			JOptionPane.showMessageDialog(null, "This doesn't do anything yet, but I'm working on it!",
										  "Coming soon...", JOptionPane.INFORMATION_MESSAGE, john);
		}
	}

}
