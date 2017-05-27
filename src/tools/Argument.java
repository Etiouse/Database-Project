package tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Argument {
	
	private String name;
	private int type;
	private int x;
	private int y;
	private Textfield t;
	
	public Argument(String name, int type, int x, int y){
		this.name = name;
		this.type = type;
		this.x = x;
		this.y = y;
		t = new Textfield(new ImageIcon(getClass().getResource("/images/textfields/textfield.png")).getImage(), 
				x - 10, y + 10, 14);
	}
	
	public void addContent(String content){
		t.setContent(content);
	}
	
	public String getContent(){
		return t.getSavedContent();
	}
	
	public String getName(){
		return name;
	}
	
	int returnType(){
		return type;
	}
	
	public void draw(Graphics g){
		g.setFont(new Font("Serif", Font.BOLD, 13));
		g.setColor(Color.WHITE);
		g.drawString(name, x, y + 5);
		t.draw(g);
	}
	
	public boolean textSelected(int mouseX, int mouseY, Mouse mouse){
		return t.selected(mouseX, mouseY, mouse);
	}
	
	public void setTextOn(){
		t.setOn();
	}
	
	public void setTextOff(){
		t.setOff();
	}
	
	public void save(){
		t.save();
	}
	
	public boolean getNext(){
		return t.getNext();
	}
	
	public void resetNext(){
    	t.resetNext();
    }
	
	public void resetText(){
		t.reset();
	}

}
