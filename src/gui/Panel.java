package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.MouseInfo;
import java.awt.ScrollPane;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import sql.Query;
import tools.Argument;
import tools.Button;
import tools.DeleteCondition;
import tools.Mouse;
import tools.Textfield;

public class Panel extends JPanel {

	private static final long serialVersionUID = 1L;
	private int mouseX;
	private int mouseY;
	private Mouse mouse;
	private Connection c;
	private Statement s;
	private Button[] onglets;
	private int panel;
	private int lastTFS;
	private Button add;
	private Button addMode;
	private Button deleteMode;
	private Button search;
	private String error;
	private String done;
	private int mode;
	private int time;
	private boolean timesup;

	private ArrayList<String> tables;
	private ArrayList<Button> tablesButtons;
	private int tableSelected;
	private ArrayList<Argument> args;
	private ArrayList<DeleteCondition> deconds;
	private Query[] queries;
	private Textfield tf;

	private JPanel contentPanel;
	private JScrollPane scrollPane;
	private int index = 0;

	private Button find;
	private JScrollPane scroll;

	public Panel(Connection connection) throws SQLException {
		mouseX = 0;
		mouseY = 0;
		panel = 0;
		tableSelected = 1;
		lastTFS = -1;
		tables = new ArrayList<>();
		tablesButtons = new ArrayList<>();
		args = new ArrayList<>();
		deconds = new ArrayList<>();
		mouse = new Mouse();

		add = new Button(getImg("/images/buttons/add.png"), getImg("/images/buttons/addSelected.png"), 550, 620, "", 20, -5, 14);
		addMode = new Button(getImg("/images/buttons/addMode.png"), getImg("/images/buttons/modeSelected.png"), 360, 640, "", 20, 25, 18);
		deleteMode = new Button(getImg("/images/buttons/deleteMode.png"), getImg("/images/buttons/modeSelected.png"), 360, 640, "", 20, 25, 18);
		search = new Button(getImg("/images/buttons/query.png"), getImg("/images/buttons/queryGlow.png"), 600, 150, "Search", 40, 25, 18);
		find = new Button(getImg("/images/buttons/query.png"), getImg("/images/buttons/queryGlow.png"), 335, 490, "Find", 54, 25, 18);
		queries = new Query[23];
		generateQueries();
		scroll = new JScrollPane();
		contentPanel = new JPanel();
		contentPanel.setLayout(null);
		this.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(42, 245, 715, 530);
		this.add(scrollPane);
		tf = new Textfield(getImg("/images/textfields/longtextfield.png"), getImg("/images/textfields/longtextfieldGlow.png"), 100, 157, 14);

		c = connection;
		s = c.createStatement();
		declareButtons();
		error = "";
		done = "";
		mode = 0;
		time = 0;
		timesup = true;

		refreshDatabase();
		loadTableArgs(tables.get(tableSelected));
	}

	@Override
	public void paintComponent(Graphics g) {
		mouseX = MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x;
		mouseY = MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y;

		if (panel == 0) {
			g.drawImage(getImg("/images/backgrounds/bg1.png"), 0, 0, null);
			// Display name of selected table
			g.setColor(Color.WHITE);
			g.setFont(new Font("Serif", Font.BOLD, 20));
			search.draw(g);

			if (search.isSelected(mouseX, mouseY)) {
				search.glow(g);
				if (mouse.isClickedL()) {
					String c = tf.getContent();
					searchQuery(g, c, "");
					scrollPane.getViewport().add(contentPanel);
					scrollPane.validate();
				}
			}

			
			tf.draw(g);
			if (tf.selected(mouseX, mouseY, mouse)) {
				tf.setOn();
			} else {
				if (mouse.isClickedL()) {
					tf.setOff();
					tf.save();
				}
			}

		} else if (panel == 1) {
			g.drawImage(getImg("/images/backgrounds/bg1.png"), 0, 0, null);

			int selected = -1;
			for (int i = 0; i < queries.length; i++) {
				queries[i].draw(g, mouse, mouseX, mouseY);
				if (queries[i].selected()) {
					selected = i;
					for (int j = 0; j < queries.length; j++) {
						if (i != j)
							queries[j].done();
					}
				}
			}

			g.setColor(Color.WHITE);
			find.draw(g);
			if (find.isSelected(mouseX, mouseY)) {
				find.glow(g);
				if (mouse.isClickedL() && selected > -1) {
					predefQuery(g, queries[selected].getSql());
				}
			}

		} else {
			// Insert + Remove panel
			g.drawImage(getImg("/images/backgrounds/bg2.png"), 0, 0, null);

			// Display name of selected table
			g.setColor(Color.BLACK);
			g.setFont(new Font("Serif", Font.BOLD, 20));
			String nameOfTable = tables.get(tableSelected);
			g.drawString(nameOfTable, 470 - nameOfTable.length() * 5, 140);

			// Insert mode
			if (mode == 0) {
				// Handle the textfields
				boolean oneClicked = false;
				for (int i = 0; i < args.size(); i++) {
					Argument arg = args.get(i);
					arg.draw(g);
					if (arg.getNext()) {
						arg.resetNext();
						int next = (i + 1) % args.size();
						lastTFS = next;
						args.get(next).setTextOn();
					}
					if (arg.textSelected(mouseX, mouseY, mouse)) {
						oneClicked = true;
						if (lastTFS >= 0)
							args.get(lastTFS).save();
						lastTFS = i;
						for (int j = 0; j < args.size(); j++) {
							if (i != j)
								args.get(j).setTextOff();
						}
						arg.setTextOn();
					}
				}
				// Deselect everything if no textfields clicked
				if (!oneClicked && mouse.isClickedL()) {
					for (int i = 0; i < args.size(); i++) {
						args.get(i).setTextOff();
						if (lastTFS >= 0) {
							args.get(lastTFS).save();
							lastTFS = -1;
						}
					}
				}

				// Display the add button
				add.draw(g);
				if (add.isSelected(mouseX, mouseY)) {
					add.glow(g);
					if (mouse.isClickedL()) {
						Argument[] content = new Argument[args.size()];
						for (int i = 0; i < args.size(); i++) {
							content[i] = args.get(i);
						}
						insertQuery(g, tables.get(tableSelected), content);
					}
				}

				// Delete mode
			} else {
				// Display the conditions
				for (int i = 0; i < deconds.size(); i++) {
					deconds.get(i).draw(g, mouseX, mouseY, mouse);
				}

				// Display the delete button
				add.draw(g);
				if (add.isSelected(mouseX, mouseY)) {
					add.glow(g);
					if (mouse.isClickedL()) {
						deleteQuery(g, tables.get(tableSelected), deconds);
					}
				}

				// Handle the deleteConditions
				if (deconds.get(deconds.size() - 1).finished() && deconds.size() < 3) {
					String[] argsList = new String[args.size()];
					for (int i = 0; i < args.size(); i++) {
						argsList[i] = args.get(i).getName();
					}
					deconds.add(new DeleteCondition(argsList, 250, 200 + deconds.size() * 140));
				}
			}

			// Handle the exposure time of the info message
			if (!done.equals("") && timesup) {
				timesup = false;
				time = (int) Calendar.getInstance().getTime().getTime();
			}
			if (!timesup) {
				int currentTime = (int) Calendar.getInstance().getTime().getTime();
				if (currentTime - time > 2000) {
					timesup = true;
					done = "";
				}
			}

			// Chose the mode
			if (mode == 0) {
				addMode.draw(g);
				if (addMode.isSelected(mouseX, mouseY)) {
					addMode.glow(g);
					if (mouse.isClickedL()) {
						mode = 1;
						error = "";
						done = "";
						String[] argsList = new String[args.size()];
						for (int i = 0; i < args.size(); i++) {
							argsList[i] = args.get(i).getName();
						}
						deconds.add(new DeleteCondition(argsList, 250, 200));
					}
				}
			} else {
				deleteMode.draw(g);
				if (deleteMode.isSelected(mouseX, mouseY)) {
					deleteMode.glow(g);
					if (mouse.isClickedL()) {
						mode = 0;
						error = "";
						done = "";
						deconds = new ArrayList<>();
					}
				}
			}

			// Display an eventual error
			g.setColor(Color.RED);
			g.setFont(new Font("Serif", Font.BOLD, 18));
			g.drawString(error, 245, 185);

			// Display an eventual done message
			g.setColor(Color.GREEN);
			g.setFont(new Font("Serif", Font.BOLD, 18));
			g.drawString(done, 245, 185);
			g.setColor(Color.BLACK);
		}

		// Display all the table

		if (panel == 2) {
			g.setFont(new Font("Serif", Font.BOLD, 12));
			g.setColor(Color.WHITE);
			for (int i = 0; i < tablesButtons.size(); i++) {
				Button onglet = tablesButtons.get(i);
				if (onglet.isSelected(mouseX, mouseY)) {
					onglet.glow(g);
					if (mouse.isClickedL()) {
						tableSelected = i;
						error = "";
						done = "";
						args = new ArrayList<>();
						lastTFS = -1;
						loadTableArgs(tables.get(tableSelected));
						deconds = new ArrayList<>();
						String[] argsList = new String[args.size()];
						for (int j = 0; j < args.size(); j++) {
							argsList[j] = args.get(j).getName();
						}
						deconds.add(new DeleteCondition(argsList, 250, 200));
					}
				}
				onglet.draw(g);
			}
		}

		// Select the panel to draw
		g.setFont(new Font("Serif", Font.PLAIN, 20));
		g.setColor(Color.WHITE);
		for (int i = 0; i < onglets.length; i++) {
			Button onglet = onglets[i];
			onglet.draw(g);
			if (onglet.isSelected(mouseX, mouseY)) {
				onglet.glow(g);
				if (mouse.isClickedL()) {
					panel = i;
					this.remove(scrollPane);
					this.remove(scroll);
				}
			}
		}

		mouse.reset();

	}

	private Image getImg(String text) {
		return new ImageIcon(getClass().getResource(text)).getImage();
	}

	private void declareButtons() {
		onglets = new Button[3];
		String image = "/images/buttons/onglet.png";
		String glow = "/images/buttons/ongletSelected.png";
		onglets[0] = new Button(getImg(image), getImg(glow), 50, 50, "         Search", 26, 29, 18);
		onglets[1] = new Button(getImg(image), getImg(glow), 300, 50, "Predefined Queries", 26, 29, 18);
		onglets[2] = new Button(getImg(image), getImg(glow), 550, 50, "     Insert/Delete", 26, 29, 18);
	}

	private void loadTableArgs(String tableName) {
		try {
			ResultSet argsQ = s.executeQuery("SELECT * FROM " + tableName);
			ResultSetMetaData meta = argsQ.getMetaData();
			int columnCount = meta.getColumnCount();

			for (int i = 1; i <= columnCount; i++) {
				int x = 248 + ((i - 1) % 3) * 170;
				int y = 200 + ((int) Math.floor((i - 1) / 3)) * 70;
				String name = meta.getColumnName(i);
				String type = meta.getColumnTypeName(i);
				int nullable = meta.isNullable(i);
				if (name.length() > 17)
					name = name.substring(0, 17) + "...";
				args.add(new Argument(name, type, nullable, x, y));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void refreshDatabase() {
		args = new ArrayList<>();
		try {
			ResultSet tablesSet = s.executeQuery("SELECT table_name FROM user_tables");
			int posY = 150;
			while (tablesSet.next()) {
				String name = tablesSet.getString(1);
				tables.add(name);
				tablesButtons.add(new Button(getImg("/images/buttons/table.png"),
						getImg("/images/buttons/tableSelected.png"), 50, posY, name, 7, 16, 12));
				posY += 28;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void insertQuery(Graphics g, String tableName, Argument[] content) {
		String query = "INSERT INTO " + tableName + " (";
		boolean correct = true;
		for (int i = 0; i < content.length; i++) {
			query += content[i].getName();
			if (i < content.length - 1)
				query += ", ";
			if (!content[i].matchType(content[i].getContent())) {
				correct = false;
				error = content[i].getName() + " should be " + content[i].getGoodType() + "";
				done = "";
				break;
			}
			if (content[i].isNullable() == 0 && content[i].getContent().equals("")) {
				correct = false;
				error = content[i].getName() + " has to be filled";
				done = "";
				break;
			}
		}

		if (correct) {
			query += ") VALUES (";
			for (int i = 0; i < content.length; i++) {
				String cont = content[i].getContent();
				if (cont.equals(""))
					query += "null";
				else
					query += "'" + cont + "'";
				if (i < content.length - 1)
					query += ", ";
			}
			query += ")";
			try {
				s.executeQuery(query);
				error = "";
				for (int i = 0; i < content.length; i++) {
					content[i].resetText();
				}
				done = "Insertion done";
				error = "";
			} catch (SQLException e) {
				error = "Wrong format detected";
				done = "";
			}
		}
	}

	private void deleteQuery(Graphics g, String tableName, ArrayList<DeleteCondition> content) {
		String query = "DELETE FROM " + tableName + " WHERE ";
		boolean correct = true;
		for (int i = 0; i < content.size(); i++) {
			DeleteCondition dc = content.get(i);
			if (i > 0 && !dc.getText().equals(""))
				query += " AND ";
			query += dc.getText();
			Argument a = null;
			for (int j = 0; j < args.size(); j++) {
				if (args.get(j).getName().equals(content.get(i).getArg()))
					a = args.get(j);
			}
			if (a != null && !a.matchType(a.getContent())) {
				correct = false;
				error = a.getName() + " should be " + a.getGoodType() + "";
				done = "";
				break;
			}
		}
		if (correct) {
			try {
				if (!query.substring(query.length() - 6, query.length()).equals("WHERE ")) {
					s.executeQuery(query);
					error = "";
				} else
					error = "You didn't put any information";
				deconds = new ArrayList<>();
				String[] argsList = new String[args.size()];
				for (int i = 0; i < args.size(); i++) {
					argsList[i] = args.get(i).getName();
				}
				deconds.add(new DeleteCondition(argsList, 250, 200));
				done = "Deletion done";
				error = "";
			} catch (SQLException e) {
				error = "Wrong format detected";
				done = "";
			}
		}
	}

	private void predefQuery(Graphics g, String query) {
		try {
			ResultSet rs = s.executeQuery(query);
			JTable jTab = new JTable(buildTabelModel(rs));
			if (jTab.getRowCount() > 0) {

				this.remove(scroll);
				Dimension size = jTab.getPreferredSize();
				scroll = new JScrollPane(jTab);

				scroll.setBounds(100, 550, 600, Math.min(198, size.height + 22));

				this.setLayout(null);
				this.add(scroll);
			}
		} catch (SQLException e) {
			System.out.println("predef error");
		}
	}

	// Table name can be null (""), we have then to look in every table
	private void searchQuery(Graphics g, String search, String tableName) {
		System.out.println("Search for table : " + tableName);
		if (tableName.equals("")) {
			// Go through every table
			for (int i = 0; i < tables.size(); ++i) {
				String tabName = tables.get(i);
				searchQuery(g, search, tabName);
			}
			System.out.println("DONE");
		} else {
			// Select all from the table where any argument is like the searched
			// term
			String query = "SELECT * FROM " + tableName + " WHERE ";
			args = new ArrayList<>();
			loadTableArgs(tableName);
			for (int i = 0; i < args.size(); i++) {
				query += args.get(i).getName() + " LIKE \'%" + search + "%\' OR ";
			}
			query = query.substring(0, query.length() - 4);
			System.out.println(query);
			try {
				if (!query.substring(query.length() - 6, query.length()).equals("WHERE ")) {
					ResultSet rs = s.executeQuery(query);
					// read info; and pretty print it

					JTable jTab = new JTable(buildTabelModel(rs));
					if (jTab.getRowCount() > 0) {
						jTab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						JScrollPane scroll = new JScrollPane(jTab);
						this.setLayout(null);
						JLabel lab = new JLabel("FOUND in table "+ tableName);
						lab.setBounds(100, 100+ 100*index, 100, 5);
						scroll.setBounds(100, 110 + 250*index, 250, 250);
						index++;
						contentPanel.add(lab);
						contentPanel.add(scroll);
						error = "";
					}
					// done = "You search query find something !";
				} else
					error = "You didn't put any information";
			} catch (SQLException e) {
				error = "Wrong format detected";
				done = "";
			}
		}
	}

	public static DefaultTableModel buildTabelModel(ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();

		// column's name
		Vector<String> colNames = new Vector<String>();
		int colCount = metaData.getColumnCount();
		for (int col = 1; col <= colCount; ++col) {
			colNames.add(metaData.getColumnName(col));
		}

		// Data from the table
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		while (rs.next()) {
			Vector<Object> vect = new Vector<Object>();
			for (int col = 1; col <= colCount; col++) {
				vect.add(rs.getObject(col));
			}
			data.add(vect);
		}
		return new DefaultTableModel(data, colNames);
	}

	private void generateQueries() {
		for (int i = 0; i < queries.length; i++) {
			queries[i] = new Query(i);
		}
	}
}