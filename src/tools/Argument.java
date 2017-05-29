package tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.sql.Date;

import javax.swing.ImageIcon;

public class Argument {
	
	private String name;
	private String type;
	private int nullable;
	private int x;
	private int y;
	private Textfield t;
	
	public Argument(String name, String type, int nullable, int x, int y){
		this.name = name;
		this.type = type;
		this.nullable = nullable;
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
	
	String returnType(){
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
	
	public int isNullable(){
		return nullable;
	}
	
	public boolean matchType(String c){
		if (type.equals("CHAR") || type.equals("VARCHAR") || type.equals("VARCHAR2")) return c.length() < 255;
		if (type.equals("LONG") || c.equals("")) return true;
		if (type.equals("NUMBER")) return isInteger(c);
		if (type.equals("DATE")) return isDate(c);
		return false;
	}
	
	public boolean isDate(String string) {
		if (string.length() < 8) return false;
		int start = 0;
		for (int i = 0; i < string.length() - 8; i++) {
			if (string.charAt(i) == ' ') start++;
			else break;
		}
		for (int i = start; i < start + 8; i++) {
			if (i % 3 == 2) {
				if (string.charAt(i) != '.') return false;
			} else {
				if (!Character.isDigit(string.charAt(i))) return false;
			}
		}
		for (int i = start + 8; i < string.length(); i++) {
			if (string.charAt(i) != ' ') return false;
		}
		return true;
	}
	
	public boolean isInteger(String string) {
	    try {
	        Integer.parseInt(string);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	public String getGoodType(){
		switch (type) {
			case "NUMBER": return "a number";
			case "VARCHAR": return "a string (smaller than 254 characters)";
			case "LONG": return "a string";
			case "CHAR": return "a string (smaller than 254 characters)";
			case "DATE": return "a date (xx.yy.zz)";
			default: return type;
		}
	}

}
