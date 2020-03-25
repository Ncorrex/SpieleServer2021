import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.MatteBorder;

public class GameField extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public FieldBut[][] fields = new FieldBut[10][10];
	public int ships2 = 1;
	public int ships3 = 3;
	public int ships4 = 2;
	public int ships5 = 1;
	public JButton but2h, but2v, but3h, but3v, but4h, but4v, but5h, but5v;
	String dir = "2h";

	public GameField() {
		setBounds(50, 50, 280, 250);
		setBackground(Color.DARK_GRAY);
		setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		setForeground(Color.WHITE);
		setLayout(null);

		// erstellt alle Buttons
		for (int x = 0; x < fields.length; x++) {
			for (int y = 0; y < fields[x].length; y++) {
				FieldBut temp = new FieldBut(x, y, this);
				add(temp);
				fields[x][y] = temp;
			}
		}

		// knopp für 2großes schiff horizontal
		but2h = new JButton("2→");
		but2h.setBounds(250, 0, 25, 25);
		but2h.setBackground(Color.lightGray);
		but2h.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dir = "2h";
			}
		});

		// knopp für 2großes schiff vertikal
		but2v = new JButton("2↓");
		but2v.setBounds(250, 25, 25, 25);
		but2v.setBackground(Color.lightGray);
		but2v.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dir = "2v";
			}
		});

		// ToDo alle anderen Buttons für die anderen Schiffsmöglichkeiten (siehe
		// booleans)

		add(but2h);
		add(but2v);
	}

	public void setupSet(int x, int y, boolean rem) {
		// ToDo alle Richtungen

		switch (dir) {
		case "2h":
			if (ships2 > 0)
				try {
					fields[x + 1][y].actionPerf(2);
					fields[x][y].actionPerf(2);
				} catch (Exception e) {
					fields[x][y].actionPerf(2);
				}
			break;
		case "2v":
			if (ships2 > 0)
				try {
					fields[x][y + 1].actionPerf(2);
					fields[x][y].actionPerf(2);
				} catch (Exception e) {
					fields[x][y].actionPerf(2);
				}
			break;
		}

	}

	public void minusShip(int size) { // macht die anzahl an schiffe kleiner
		switch (size) {
		case 2:
			ships2--;
			break;
		case 3:
			ships3--;
			break;
		case 4:
			ships4--;
			break;
		case 5:
			ships5--;
			break;
		}
		
		if(ships2 == 0 && ships3 == 0 && ships4 == 0 && ships5 == 0) {
			//rdy()	  //methode die alle buttons aus dem setup in den Spielmodus bringt
		}
	}

}
