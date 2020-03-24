import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.border.MatteBorder;
import javax.swing.JList;
import javax.swing.JTextArea;

public class GameWindow {

	private JFrame frmSchiffeversenken;
	private final JTextField textField = new JTextField();
	private final JButton btnSend = new JButton("send");
	private final JTextArea textArea = new JTextArea();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameWindow window = new GameWindow();
					window.frmSchiffeversenken.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GameWindow() {
		initialize();
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
				if(!textField.getText().equals(""))
					textArea.setText(textArea.getText() + textField.getText() + "\n");
			}
		});
		btnSend.setBounds(710, 430, 60, 20);
		
		frmSchiffeversenken.getContentPane().add(btnSend);
		textArea.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		textArea.setBackground(Color.DARK_GRAY);
		textArea.setForeground(Color.WHITE);
		textArea.setBounds(470, 250, 300, 170);
		
		frmSchiffeversenken.getContentPane().add(textArea);
		
		GameField gameField = new GameField();
		frmSchiffeversenken.getContentPane().add(gameField);
		
		frmSchiffeversenken.setBounds(100, 100, 800, 500);
		frmSchiffeversenken.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}
}
