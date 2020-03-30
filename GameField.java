import java.awt.Color;
import java.awt.Font;
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
	public JButton[][] detonator = new JButton[fields.length][fields[0].length];
	public int ships2 = 1;
	public int ships3 = 3;
	public int ships4 = 2;
	public int ships5 = 1;
	public JButton but2h, but2v, but3h, but3v, but4h, but4v, but5h, but5v;
	String dir = "2h";
	Client client;
	boolean setup;

	public GameField(Client pClient, boolean pSetup) {
		setup = pSetup;
		client = pClient;
		setBackground(Color.DARK_GRAY);
		setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		setForeground(Color.WHITE);
		setLayout(null);

		if (setup) {
			setBounds(50, 50, 280, 250);
			initSetup();
		} else {
			setBounds(500, 50, 250, 250);
			initView();
		}

	}

	public void setupExpansion(int x, int y) {
		// ToDo alle Richtungen

		switch (dir) {
		case "2h":
			if (ships2 > 0)
				try {
					fields[x + 1][y].actionPerf(2);
					fields[x][y].actionPerf(2);
				} catch (Exception e) {}
			break;
		case "2v":
			if (ships2 > 0)
				try {
					fields[x][y + 1].actionPerf(2);
					fields[x][y].actionPerf(2);
				} catch (Exception e) {}
			break;
		}

	}

	private void initView() {
		for (int x = 0; x < fields.length; x++) {
			for (int y = 0; y < fields[x].length; y++) {
				JButton temp = new JButton();
				final int alX = x;
				final int alY = y;
				final Client cl = client;
				temp.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String message = "/" + alX + " " + alY;
						cl.send(message);
					}
				});
				temp.setBounds(x * 25, y * 25, 25, 25);
				add(temp);
				detonator[x][y] = temp;
			}
		}
	}

	private void initSetup() {
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
		but2h.setFont(new Font("Roboto Mono Medium", Font.BOLD, 10));
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

		if (ships2 == 0 && ships3 == 0 && ships4 == 0 && ships5 == 0) {
			ready();
		}
	}

	public void ready() {
		for (int x = 0; x < fields.length; x++) {
			for (int y = 0; y < fields[x].length; y++) {
				fields[x][y].ready();
			}
		}
		client.send("/ready");
	}

}
