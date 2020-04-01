import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.border.MatteBorder;


import javax.swing.JTextArea;

public class GameWindow {

	private JFrame frmSchiffeversenken;
	private final JTextField textField = new JTextField();
	private final JButton btnSend = new JButton("send");
	private final JTextArea textArea = new JTextArea();
	Client client;

	
	/**
	 * Create the application.
	 */
	public GameWindow(Client pClient) {
		client = pClient;
		initialize();
	}

	public static void main(String[] args)
	{
		new GameWindow(null);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		textField.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		textField.setBackground(Color.DARK_GRAY);
		textField.setForeground(Color.WHITE);
		textField.setBounds(470, 430, 240, 20);
		textField.setColumns(10);
		frmSchiffeversenken = new JFrame();
		frmSchiffeversenken.getContentPane().setLayout(null);
		frmSchiffeversenken.getContentPane().setForeground(Color.WHITE);
		frmSchiffeversenken.setForeground(Color.WHITE);
		frmSchiffeversenken.setVisible(true);
		frmSchiffeversenken.setTitle("Schiffeversenken");
		frmSchiffeversenken.setResizable(false);
		frmSchiffeversenken.setBackground(Color.GRAY);
		frmSchiffeversenken.getContentPane().setBackground(Color.GRAY);

		frmSchiffeversenken.getContentPane().add(textField);
		btnSend.setForeground(Color.BLACK);
		btnSend.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		btnSend.setBackground(Color.LIGHT_GRAY);
		btnSend.setFont(new Font("Roboto Mono Light", Font.PLAIN, 10));
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!textField.getText().equals(""))
					client.send(textArea.getText() + textField.getText());
					client.processMessage(textArea.getText() + textField.getText());
					
					//ToDo 	client override sodass eine processedMessage in das Textfield kommt.
					
			}
		});
		btnSend.setBounds(710, 430, 60, 20);

		frmSchiffeversenken.getContentPane().add(btnSend);
		textArea.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		textArea.setBackground(Color.DARK_GRAY);
		textArea.setForeground(Color.WHITE);
		textArea.setBounds(470, 310, 300, 110);

		frmSchiffeversenken.getContentPane().add(textArea);

		GameField gameField = new GameField(client, true);
		frmSchiffeversenken.getContentPane().add(gameField);
		
		GameField viewField = new GameField(client, false);
		frmSchiffeversenken.getContentPane().add(viewField);

		frmSchiffeversenken.setBounds(100, 100, 800, 500);
		frmSchiffeversenken.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSchiffeversenken.setVisible(true);

	}
}
