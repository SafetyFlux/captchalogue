package shape;

import java.awt.*;

public interface Shape {

   public void draw(Graphics g);
   public boolean containsPoint(int x, int y);
   public void move(int xAmount, int yAmount);
   public void setFilled(boolean b);
   
}



