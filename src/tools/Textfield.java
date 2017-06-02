package tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Textfield {

    private Image image;
    private Image glow;
    private int x;
    private int y;
    private boolean on;
    private Time time;
    private int size;
    private String content;
    private String savedContent;
    private double length;
    private  ArrayList<Double> tab;
    private int place;
    private KeyboardListener keys;
    private boolean next;

    public Textfield(Image image, Image glow, int x, int y, int size){
        this.image = image;
        this.glow = glow;
        this.x = x;
        this.size = size;
        this.y = y;
        on = false;
        time = new Time();
        content = "";
        savedContent = "";
        length = 0.0;
        place = 0;
        tab = new ArrayList<>();
        keys = new KeyboardListener(65);
        next = false;
    }

    public void draw(Graphics g){
        g.drawImage(image, x, y, null);
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.PLAIN, size));
        if (on){
            g.drawImage(glow, x, y, null);
            
            if (time.clock()){
            	g.fillRect(x + 10 + (int)(KeyboardListener.length)*2, y + 7, 2, 15);
            }
            if (keys.enter()) {
            	save();
            	on = false;
            	next = true;
            }
            g.drawString(content, x + 10, y + 19);
            content = keys.getText();
        } else {
        	g.drawString(savedContent, x + 10, y + 19);
        }
    }
    
    public void setContent(String content){
    	this.content = content;
    }
    
    public String getContent(){
    	return content;
    }
    
    public String getSavedContent(){
    	return savedContent;
    }
    
    public boolean selected(int mouseX, int mouseY, Mouse mouse){
    	return mouseX > x && mouseX < x + image.getWidth(null) && mouseY > y && mouseY < y + image.getHeight(null) && mouse.isClickedL();
    }

    public void setOn(){
        on = true;
        content = savedContent;
        keys.setField(savedContent, length, place, tab);
    }

    public void setOff(){
        on = false;
    }
    
    public void save(){
        savedContent = keys.getText();
    	length = keys.getLength();
    	place = keys.getPlace();
    	tab = keys.getTab();
    	keys.reset();
    }

    public boolean isOn(){
        return on;
    }
    
    public boolean getNext(){
    	return next;
    }
    
    public void resetNext(){
    	next = false;
    }
    
    public void reset(){
    	savedContent = "";
    	length = 0;
    	place = 0;
    	tab = new ArrayList<>();
    }
    
    public boolean finished(){
    	return next;
    }

}
