package gui;


import java.awt.Insets;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JFrame;

import tools.KeyboardListener;
import tools.Mouse;

public class Frame extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private Panel panel;
    private boolean on;
    private Mouse mouse;
    private KeyboardListener keys;

    public Frame(int frameX, int frameY, Connection connection) throws SQLException { 
        
        on = true;
        mouse = new Mouse();
        keys = new KeyboardListener(140);
        panel = new Panel(connection);

        this.setFocusable(true);
        this.setSize(frameX + 6, frameY + 35);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle("Comics Database");

        this.setContentPane(panel);
        this.addMouseListener(mouse);
        this.addKeyListener(keys);
        this.setResizable(false);
        this.setVisible(true); 
        
        run();
        
        this.dispose();
    }
    
    public void run(){
        while (on){
            panel.repaint();
        }
    }
    
    public void end(){
        on = false;
    }
}



