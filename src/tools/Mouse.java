package tools;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {

    public static boolean left = false;
    private boolean right = false;

    public void mouseClicked(MouseEvent arg0) {}

    public void mouseEntered(MouseEvent arg0) {}

    public void mouseExited(MouseEvent arg0) {}

    public void mousePressed(MouseEvent arg0) {
        if(arg0.getButton() == MouseEvent.BUTTON1) left = true;
        else if(arg0.getButton() == MouseEvent.BUTTON3) right = true;
    }

    public void mouseReleased(MouseEvent arg0){}
    
    public boolean isClickedL(){
        return left;
    }
    
    public boolean isClickedR(){
        return right;
    }
    
    public void reset(){
        left = false;
        right = false;
    }

}