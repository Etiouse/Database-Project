package tools;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class KeyboardListener implements KeyListener{

    public static String texte;
    public static ArrayList<Double> tab;
    public static boolean enter;
    private static boolean maj;
    private static boolean alt;
    public static double length;
    public static int place;
    public static int maxLength;
    public static boolean right;
    public static boolean left;
    public static boolean up;
    public static boolean down;
    Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();

    public KeyboardListener(int maxLength){
        KeyboardListener.maxLength = maxLength;
        tab = new ArrayList<>();
        texte = "";
        enter = false;
        maj = false;
        alt = false;
        length = 0;
        place = 0;
        right = false;
        left = false;
        up = false;
        down = false;
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getExtendedKeyCode();
        
        if (code == 16){
            maj = true;
        } else if (code == 18){
            alt = true;
        }
        if (code == 37){
            if (place > 0){
                length = length - tab.get(place-1);
                place--;
            }
        } else if (code == 39){
            if (place < tab.size()){
                length = length + tab.get(place);
                place++;
            }
        } else if (maj){   
            if (length < maxLength){
            	if (code == 73){
                    add(2.5);
                    addText((char)(code));
                } else if (code == 74){
                    add(3);
                    addText((char)(code));
                } else if (code == 66 || code == 69 || code == 70 || code == 76
                		|| code == 80 || code == 83 || code == 90){
                    add(4);
                    addText((char)(code));
                } else if (code == 65 || code == 67 || code == 72 || code == 75
                		|| code == 82 || code == 84 || code == 85 || code == 86
                		|| code == 89){
                    add(4.5);
                    addText((char)(code));
                } else if (code == 68 || code == 71 || code == 78 || code == 79
                		|| code == 81 || code == 88){
                    add(5);
                    addText((char)(code));
                } else if (code == 77){
                    add(6);
                    addText((char)(code));
                } else if (code == 87){
                    add(6.5);
                    addText((char)(code));
                } else if (code == 44){
                    add(2);
                    addText(";");
                } else if (code == 45){
                    add(3.5);
                    addText("_");
                } else if (code == 46){
                    add(1.5);
                    addText(":");
                } else if (code == 48){
                    add(4);
                    addText("=");
                } else if (code == 49){
                    add(4);
                    addText("+");
                } else if (code == 50){
                    add(2.5);
                    addText('"');
                } else if (code == 51){
                    add(3.5);
                    addText("*");
                } else if (code == 52){
                    add(3.5);
                    addText("§");
                } else if (code == 53){
                    add(6);
                    addText("%");
                } else if (code == 54){
                    add(5.5);
                    addText("&");
                } else if (code == 55){
                    add(2);
                    addText("/");
                } else if (code == 56){
                    add(2.5);
                    addText("(");
                } else if (code == 57){
                    add(2.5);
                    addText(")");
                } else if (code == 16777449){
                    add(3);
                    addText("¶");
                } else if (code == 16777448){
                	add(5);
                    addText("¼");
                } else if (code == 16777440){
                    add(3.5);
                    addText("¤");
                } else if (code == 153){
                    add(4);
                    addText(">");
                } else if (code == 222){
                    add(3);
                    addText("?");
                } else if (code == 135){
                    add(2.5);
                    addText("!");
                } else if (code == 515){
                    add(3.5);
                    addText("£");
                }
            }
        } else if (alt){
            if (length < maxLength){
                if (code == 49){
                    add(1.5);
                    addText("¦");
                } else if (code == 50){
                    add(6.5);
                    addText('@');
                } else if (code == 51){
                    add(3.5);
                    addText("#");
                } else if (code == 52){
                    add(3);
                    addText("°");
                } else if (code == 53){
                    add(3.5);
                    addText("§");
                } else if (code == 54){
                    add(4);
                    addText("¬");
                } else if (code == 55){
                    add(1.5);
                    addText("|");
                } else if (code == 56){
                    add(3.5);
                    addText("¢");
                } else if (code == 69){
                    add(3.5);
                    addText("€");
                } else if (code == 153){
                    add(2);
                    addText("\\");
                } else if (code == 130){
                    add(4);
                    addText("~");
                } else if (code == 135){
                    add(2.5);
                    addText("]");
                } else if (code == 515){
                    add(3.5);
                    addText("}");
                } else if (code == 16777448){
                    add(2.5);
                    addText("[");
                } else if (code == 16777440){
                    add(3.5);
                    addText("{");
                }
            }
        } else {
            if (code == KeyEvent.VK_BACK_SPACE){
                if (place > 0){
                    length = length - tab.get(place-1);
                    tab.remove(place-1);
                    texte = texte.substring(0, place-1) + texte.substring(place, texte.length());
                    place = place - 1;
                }
            } else if (code == KeyEvent.VK_ENTER){
                enter = true;
            } else if (length < maxLength){
            	if (code == 44) {
            		add(2);
            		addText(",");
            	} else if (code == 45) {
            		add(2.5);
            		addText("-");
            	} else if (code == 46) {
            		add(2);
            		addText(".");
            	} else if (code == 16777448){
                    add(3);
                    addText("è");
                } else if (code == 16777449){
                    add(3);
                    addText("é");
                } else if (code == 16777440){
                    add(3);     
                    addText("à");
                } else if (code == 153){  
                    add(4);
                    addText("<");
                } else if (code == 222){
                    add(1.5);
                    addText("'");
                } else if (code == 130){
                    add(3);
                    addText("^");
                } else if (code == 135){
                    add(2.5);
                    addText("¨");
                } else if (code == 515){
                    add(3.5);
                    addText("$");  
                } else if (code == 73 || code == 74 || code == 76) {
            		add(1.5);
            		addText((char)(code + 32));
            	} else if (code == 84) {
            		add(2);
                    addText((char)(code + 32));
            	} else if (code == KeyEvent.VK_SPACE) {
            		add(2);
            		addText(" ");
            	} else if (code == 70 || code == 82 || code == 86) {
            		add(2.5);
                    addText((char)(code + 32));
            	} else if (code == 65 || code == 69 || code == 83 || code == 90) {
            		add(3);
                    addText((char)(code + 32));
            	} else if (code > 47 && code < 59){
            		add(3.5);
            		addText("" + (code - 48));
            	} else if (code == 66 || code == 67 || code == 68 || code == 71 
            			|| code == 72 || code == 75 || code == 78 || code == 79 
            			|| code == 80 || code == 81 || code == 85 || code == 88
            			|| code == 89){
            		add(3.5);
            		addText((char)(code + 32));
            	} else if (code == 77 || code == 87) {
            		add(5.5);
                    addText((char)(code + 32));
            	} 
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getExtendedKeyCode();
        if (code == 16){
            maj = false;
        } else if (code == 18){
            alt = false;
        }
        if (code == KeyEvent.VK_RIGHT){
            right = false;
        } 
        if (code == KeyEvent.VK_LEFT){
            left = false;
        } 
        if (code == KeyEvent.VK_UP){
            up = false;
        } 
        if (code == KeyEvent.VK_DOWN){
            down = false;
        }
    }

    public void add(double nb){
        tab.add(place, nb);
        length = length + nb;
        place++;
    }

    public void addText(char newText){
        texte = texte.substring(0, place-1) + newText + texte.substring(place-1, texte.length());
    }

    public void addText(String newText){
        texte = texte.substring(0, place-1) + newText + texte.substring(place-1, texte.length());
    }

    public void keyTyped(KeyEvent e){}  

    public void onKeyDown (KeyEvent e) {}

    public void reset(){
        tab = new ArrayList<>();
        texte = "";
        enter = false;
        maj = false;
        alt = false;
        length = 0;
        place = 0;
    }
    
    public void setField(String text, double l, int p, ArrayList<Double> t){
    	tab = new ArrayList<>(t);
        texte = text;
        enter = false;
        maj = false;
        alt = false;
        length = l;
        place = p;
    }
    
    public String getText(){
    	return texte;
    }
    
    public double getLength(){
    	return length;
    }
    
    public int getPlace(){
    	return place;
    }
    
    public ArrayList<Double> getTab(){
    	return tab;
    }
    
    public boolean enter(){
    	return enter;
    }

}
