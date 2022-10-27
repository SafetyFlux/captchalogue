package shape;

import java.awt.Color;
import java.awt.Graphics;

public class BorderRect implements Shape {

	private int cornerX, cornerY, width, height, borderThickness;
	private Color color, borderColor;
	private boolean filled;

	public BorderRect(int x, int y, int w, int h, Color c, int t, Color b) {
		cornerX = x;
		cornerY = y;
		width = w;
		height = h;
		color = c;
		borderThickness = t;
		borderColor = b;
		filled = true;
	}

	// Constructor with regular Rect fields
	public BorderRect(int x, int y, int w, int h, Color c) {
		cornerX = x;
		cornerY = y;
		width = w;
		height = h;
		color = c;
		// Automatically set border thickness to 1 and border color to black
		borderThickness = 1;
		borderColor = Color.BLACK;
		filled = true;
	}

	public void draw(Graphics g) {
		// Set old color variable
		Color oldColor = g.getColor();
		// Draw border
		g.setColor(borderColor);
		g.fillRect(cornerX, cornerY, width, height);
		// Draw rectangle
		g.setColor(color);
		g.fillRect(cornerX + borderThickness, cornerY + borderThickness, 
				width - (borderThickness * 2), height - (borderThickness * 2));
		// Reset color
		g.setColor(oldColor);
	}

	public boolean containsPoint(int x, int y) {
		return x >= cornerX && x  <= cornerX + width &&
				y >= cornerY && y <= cornerY + height; 
	}

	public void move(int xAmount, int yAmount) {
		cornerX += xAmount;
		cornerY += yAmount;
	}

	public void setFilled(boolean b) {
		filled = b;
	}

	public boolean getFilled() {
		return filled;
	}

	public void setColor(Color c) {
		color = c;
	}

	public Color getColor() {
		return color;
	}

	public void setBorderColor(Color b) {
		borderColor = b;
	}

	public Color getBorderColor() {
		return borderColor;
	}

}
