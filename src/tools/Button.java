package tools;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

public class Button {
    
    private Image bg;
    private Image glow;
    private int x;
    private int y;
    private int width;
    private int height;
    private String title;
    private int textX;
    private int textY;
    private int textSize;
    
    public Button(Image bg, Image glow, int x, int y, String title, int textX, int textY, int textSize){
        this.bg = bg;
        this.glow = glow;
        this.x = x;
        this.y = y;
        width = bg.getWidth(null);
        height = bg.getHeight(null);
        this.title = title;
        this.textX = textX;
        this.textY = textY;
        this.textSize = textSize;
    }
    
    public boolean isSelected(int mouseX, int mouseY){
        return ((mouseX > x) && (mouseX < x + width) && (mouseY > y) && (mouseY < y + height));
    }
    
    public void draw(Graphics g){
        g.drawImage(bg, x, y, null);
        g.setFont(new Font("Serif", Font.BOLD, textSize));
        g.drawString(title, x + textX, y + textY);
    }
    
    public void glow(Graphics g){
        g.drawImage(glow, x, y, null);
    }

}
