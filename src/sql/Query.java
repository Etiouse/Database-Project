package sql;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import tools.Button;
import tools.Mouse;

public class Query {

	private String name;
	private String description;
	private String sql;
	private Image img;
	private Image glow;
	private Image click;
	private Button button;
	private boolean clicked;
	private int x;
	private int y;

	private String[] descriptions = {
			"Print the brand group names with the highest number of Belgian indicia publishers", 
			"Print the ids and names of publishers of Danish book series",
			"Print the names of all Swiss series that have been published in magazines",
			"Starting from 1990, print the number of issues published each year",
			"Print the number of series for each indicia publisher whose name resembles ‘DC comics’",
			"Print the titles of the 10 most reprinted stories",
			"Print the artists that have scripted, drawn, and colored at least one of the stories they were involved in",
			"Print all non-reprinted stories involving Batman as a non-featured character",
			"Print the series names that have the highest number of issues which contain a story whose type (e.g., cartoon) is not the one occurring most frequently in the database",
			"Print the names of publishers who have series with all series types",
			"Print the 10 most-reprinted characters from Alan Moore's stories",
			"Print the writers of nature-related stories that have also done the pencilwork in all their nature-related stories.",
			"For each of the top-10 publishers in terms of published series, print the 3 most popular languages of their series",
			"Print the languages that have more than 10000 original stories published in magazines, along with the number of those stories",
			"Print all story types that have not been published as a part of Italian magazine series",
			"Print the writers of cartoon stories who have worked as writers for more than one indicia publisher",
			"Print the 10 brand groups with the highest number of indicia publishers",
			"Print the average series length (in terms of years) per indicia publisher",
			"Print the top 10 indicia publishers that have published the most single-issue series",
			"Print the 10 indicia publishers with the highest number of script writers in a single story.",
			"Print all Marvel heroes that appear in Marvel-DC story crossovers",
			"Print the top 5 series with most issues",
			"Given an issue, print its most reprinted story. Use partition for each issue"
	};

	private String[] sqls = {
			"select bg.name from Brand_group bg where bg.pid in (select pid from (select ip.pid, count(ip.iid) as countmax from indicia_publisher ip where ip.cid = (select c.cid from country c where c.name='Belgium') group by ip.pid) where countmax= (select max(bip) from (select ip.pid, count(ip.iid) as bip from indicia_publisher ip where ip.cid = (select c.cid from country c where c.name='Belgium') group by ip.pid)))", 
			"2",
			"3",
			"4",
			"5",
			"6",
			"7",
			"8",
			"9",
			"10",
			"11",
			"12",
			"13",
			"14",
			"15",
			"16",
			"17",
			"18",
			"19",
			"20",
			"21",
			"22",
			"23"
	};

	public Query(int queryNumber){
		if (queryNumber >= 0 && queryNumber < 23){
			name = "Query " + (queryNumber + 1);
			description = descriptions[queryNumber];
			sql = sqls[queryNumber];
		} else {
			name = "Query ?";
			description = "none";
			sql = "";
		}
		x = 105 + (int)(Math.floor(queryNumber / 6.0))*150;
		y = 140 + (queryNumber % 6)*46;
		clicked = false;
		img = new ImageIcon(getClass().getResource("/images/buttons/query.png")).getImage();
		glow = new ImageIcon(getClass().getResource("/images/buttons/queryGlow.png")).getImage();
		click = new ImageIcon(getClass().getResource("/images/buttons/querySelected.png")).getImage();
		button = new Button(img, glow, x, y, name, 5, 15, 14);
	}

	public void draw(Graphics g, Mouse mouse, int mouseX, int mouseY) {
		
		g.setColor(Color.WHITE);
		button.draw(g);
		if (button.isSelected(mouseX, mouseY)) {
			button.glow(g);
			if (mouse.isClickedL()) {
				clicked = true;
			}
		}
		
		if (clicked) {
			g.drawImage(click, x, y, null);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Serif", Font.BOLD, 14));
			g.drawString("Description of the query:", 105, 440);
			g.setFont(new Font("Serif", Font.PLAIN, 14));
			g.drawString("" + description, 105, 460);
			g.setFont(new Font("Serif", Font.BOLD, 14));
		}
	}
	
	public boolean selected(){
		return clicked;
	}
	
	public void done(){
		clicked = false;
	}
	
	public String getSql(){
		return sql;
	}

}
