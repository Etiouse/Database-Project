package tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class DeleteCondition {

	private String arg;
	private String operation;
	private String value;
	private int x;
	private int y;
	private int levelY;

	private String[] args;
	private String[] operations = {"<", "<=", "=", ">=", ">"};

	private ArrayList<Button> argsButtons;
	private ArrayList<Button> operationsButtons;

	private Textfield valueList;

	public DeleteCondition(String[] args, int x, int y) {
		arg = "";
		operation = "";
		value = "";
		this.x = x;
		this.y = y;
		levelY = y;
		this.args = args; 

		argsButtons = new ArrayList<>();
		operationsButtons = new ArrayList<>();

		for (int i = 0; i < args.length; i++) {
			String a = args[i];
			if (a.length() > 7) a = a.substring(0, 7) + "...";
			argsButtons.add(new Button(new ImageIcon(getClass().getResource("/images/buttons/arg.png")).getImage(), 
					new ImageIcon(getClass().getResource("/images/buttons/argSelected.png")).getImage(), 
					x + 95*(i%5), levelY, a, 10, 19, 12));
			if ((i+1) % 5 == 0) levelY += 35;
		}

		for (int i = 0; i < operations.length; i++) {
			operationsButtons.add(new Button(new ImageIcon(getClass().getResource("/images/buttons/op.png")).getImage(), 
					new ImageIcon(getClass().getResource("/images/buttons/opSelected.png")).getImage(), 
					x + 120 + 50*i, y + 40, operations[i], 8 - (i%2)*6, 23, 24));
		}

		valueList = new Textfield(new ImageIcon(getClass().getResource("/images/textfields/textfield.png")).getImage(), 
				new ImageIcon(getClass().getResource("/images/textfields/textfieldGlow.png")).getImage(),
				x + 160, y + 80, 14);
	}

	public String getText(){
		if (!arg.equals("") && !operation.equals("") && value.equals("")) return arg + " IS NULL";
		else if (!arg.equals("") && operation.equals("")) return "";
		else if (operation.equals("=") && value.equals("null")) return arg + " IS NULL";
		else if (arg.equals("") && operation.equals("") && value.equals("")) return "";
		else return arg + " " + operation + " '" + value + "'";
	}
	
	public boolean finished(){
		return valueList.finished();
	}
	
	public String getArg(){
		return arg;
	}

	public void draw(Graphics g, int mouseX, int mouseY, Mouse mouse){
		
		if (finished()) {
			g.drawImage(new ImageIcon(getClass().getResource("/images/backgrounds/deleteCond.png")).getImage(),
					x - 10, y - 10, null);
		}
		
		if (!arg.equals("")) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Serif", Font.BOLD, 20));
			g.drawString(arg, x + 240 - arg.length()*7, y + 20);
			if (!operation.equals("")) {
				g.setColor(Color.WHITE);
				g.setFont(new Font("Serif", Font.BOLD, 24));
				g.drawString(operation, x + 230, y + 62);

				valueList.draw(g);
				if (valueList.selected(mouseX, mouseY, mouse)){
					valueList.setOn();
				} else {
					if (mouse.isClickedL()) valueList.setOff();
				}
				value = valueList.getSavedContent();

			} else {
				g.setColor(Color.BLACK);
				for (int i = 0; i < operationsButtons.size(); i++) {
					operationsButtons.get(i).draw(g);
					if (operationsButtons.get(i).isSelected(mouseX, mouseY)) {
						operationsButtons.get(i).glow(g);
						if (mouse.isClickedL()) {
							operation = operations[i];
							valueList.setOn();
						}
					}
				}
			}
		} else {
			for (int i = 0; i < argsButtons.size(); i++) {
				argsButtons.get(i).draw(g);
				if (argsButtons.get(i).isSelected(mouseX, mouseY)) {
					argsButtons.get(i).glow(g);
					if (mouse.isClickedL()) {
						arg = args[i];
					}
				}
			}
		}
	}

}
