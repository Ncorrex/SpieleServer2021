import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

//imports von swing basieren auf awt

public class Start {
	//test
	private JFrame frame, waitFrame; //fraems sind die Anwendungsfenster
	private final JLabel lblWilkommenBeiSchiffe = new JLabel("Wilkommen bei Schiffe versenken ONLINE"); //label sind Textbereiche
	private final JTextField textField = new JTextField(); //TextField sind felder in denen man text eingibt
	private final JLabel lblIp = new JLabel("IP:");	
	private final JTextField textField_1 = new JTextField();
	private final JLabel lblName = new JLabel("NAME:");
	private final JButton btnJoin = new JButton("JOIN"); //buttons
	
	public class Lobby {

		public class Player extends Client{		//innere klasse für leichteren zugriff auf Methoden und Attribute
			private String name, IP, pass;	
			public Player(String pIP, int pPort, String pName, String pPass) {
				super(pIP, pPort);
				name = pName;
				IP = pIP;
				pass = pPass;
			}

			@Override
			public void processMessage(String pMessage) {
				System.out.println(pMessage);
				
				
				if(pMessage.startsWith("/CHAT"))	///CHAT für eine chatnachicht
				{
					String out = pMessage.replace("/CHAT ", "");	//überarbeitet den Parameter
					textPane_chat.setText(textPane_chat.getText().concat(out + "\n"));	//überarbeitet das chatfeld
				} 
				
				else if(pMessage.startsWith("/INFO")) {		// /INFO für Login als Namensabfrage
					send("/info " + name);
					send("/online");
				} 
				
				else if(pMessage.startsWith("/ONLINESTART"))	// /ONLINESTART für leeren des Online Feldes damit alle namen neu eingegeben werden können, ohne sich zu doppeln
				{
					textPane_online.setText("");	//setzt den Text des Online Feldes leer
				} 
				
				else if(pMessage.startsWith("/ONLINE")) {	// für Liste der Online Clients
					String on = pMessage.replace("/ONLINE", "");
					textPane_online.setText(textPane_online.getText() + on + "\n");	//	erweiter die Online liste um den mit /ONLINE <user> command des Servers
				} 
				
				else if(pMessage.startsWith("/GAME1CLOSE")) {	//wenn der Gameserver 1 runterfährt
					buttonLobby1.setText("Empty Lobby");	//überarbeitet den Text des dazugehörigen buttons
					buttonLobby1.setForeground(Color.white);	//s.o. aber mit Farbe
				}
				
				else if(pMessage.startsWith("/GAME")) {	//Serverbefehl sich mit dem zu verbindene GameServer zu verbinden dargestellt durch /GAME<SpielNummer>
					int num = Integer.valueOf(pMessage.replace("/GAME", ""));
					startGame(IP, num, name, pass);	//startet ein SpielFenster
				}
				
				else if(pMessage.startsWith("/SEARCHING")) {	//sobald ein GameServer einen Spieler sucht wird das überarbeitet
					int num = Integer.valueOf(pMessage.replace("/SEARCHING", ""));	//sucht sich aus der pMessage die Nummer des suchenden Servers
					switch (num) {	//switch anstatt if else
					case 1:	//wenn GameServer 1
						buttonLobby1.setText("Warte auf Spieler...");	//überarbeite den jewiligen button
						buttonLobby1.setForeground(Color.magenta);		//so.o aber mit Farbe
						break;											//abbrechen, damit nicht noch ein anderer case genutzt wird, da der richtige gefunden wurde
					}
				}
				
				else if(pMessage.startsWith("/WAIT4PLAYER")) {			//ServerBefehl wenn man selbst auf einen Spieler wartet
					waitFrame = new JFrame();							//erstelle ein neues Fenster
					waitFrame.setBounds(100, 100, 300, 200);			//größe und Position
					waitFrame.setBackground(Color.GRAY);				//hintergrundfarbe
					waitFrame.getContentPane().setBackground(Color.GRAY);//hintergrundfabre von Panel (worauf die Buttons, label, etc. drauf sind)
					waitFrame.getContentPane().setLayout(null);			//kein Layout, da layouts kacke sind
					waitFrame.setForeground(Color.WHITE);				//setz Schriftfarbe
					waitFrame.getContentPane().setForeground(Color.WHITE);
					
					JLabel wait = new JLabel("Warte auf Spieler...");	//erstelle ein neues Label
					wait.setFont(new Font("Roboto Mono Medium", Font.BOLD, 16));	//setze die Schriftart- und Größe
					wait.setForeground(Color.WHITE);
					wait.setBounds(0, 20, 300, 30);
					wait.setHorizontalAlignment(SwingConstants.CENTER);	//befehlt ist selbsterklärend wenn man Englisch nicht 4- steht
					wait.setVerticalAlignment(SwingConstants.CENTER);					
					waitFrame.getContentPane().add(wait);		//.add fügt das Object dem Fenster oder dem Pane zu, sodass es tatsächlich darauf existiert
					
					JButton exit = new JButton("EXIT");		//neuer Button
					exit.setBackground(Color.DARK_GRAY);
					exit.setForeground(Color.WHITE);
					exit.setBounds(100, 70, 100, 30);
					waitFrame.getContentPane().add(exit);
					exit.addActionListener(new ActionListener() {		//darin wird die Methode erschaffen die Ausgeführt wird, wenn der Button gedrückt wird
						public void actionPerformed(ActionEvent e) {
							gamePlayer.close();
							gamePlayer = null;
							waitFrame.dispose();
							frame.setVisible(true);
						}
					});
							
					waitFrame.setVisible(true);		//setze das Frame sichtbar, damit es auch angezeigt wird
					
				} 
			}
			
			public void send(String pMessage) {
				super.send(pMessage + ":" + pass);		//jaguut nh
			}
		}
		
		private class checkWin extends JFrame{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;		//kp ist halt auch da und notwendig
			boolean priv = false;			//boolean, ob der Server privat werden soll oder nicht
			
			//erstellt die Dinge
			JLabel info = new JLabel("Soll es ein privates Spiel sein?");
			JRadioButton jn = new JRadioButton("Public");
			JButton start = new JButton("Start");
			JTextField pw = new JTextField();
			int num;
			
			public checkWin(int pNum) {	//constructor
				num = pNum;
				setResizable(false);
				setLayout(null);
				setBackground(Color.GRAY);
				getContentPane().setBackground(Color.GRAY);
				getContentPane().setLayout(null);
				setForeground(Color.WHITE);
				getContentPane().setForeground(Color.WHITE);
				setBounds(100, 100, 300, 250);
				
				info.setBounds(0,0, 300,50);
				info.setAlignmentX(SwingConstants.CENTER);
				info.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				info.setForeground(Color.WHITE);
				info.setBackground(Color.DARK_GRAY);
				
				start.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				start.setForeground(Color.WHITE);
				start.setBackground(Color.DARK_GRAY);
				start.setBounds((getWidth() / 2) - 75, 150, 150, 50);
				start.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						send();
						dispose();	//dispose schließt das aktuelle Fenster
					}
				});
				
				pw.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				pw.setForeground(Color.WHITE);
				pw.setBackground(Color.DARK_GRAY);
				pw.setBounds(75, 150, 100, 50);
				pw.setEnabled(false);
				
				jn.setBounds(75, 100, 50, 50);
				jn.setText("Public");
				jn.setForeground(Color.WHITE);
				jn.setBackground(Color.DARK_GRAY);
				jn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {	//jedes mal wenn der Button gedrückt wird, wird der boolean verändert
						priv = !priv;
						if(priv) {
							jn.setText("Privat");
							pw.setEnabled(true);
						}
						else {
							jn.setText("Public");
							pw.setEnabled(false);
						}
					}
				});
				
				add(info);
				add(start);
				add(jn);
				add(pw);
				
				setVisible(true);
				
			}
			
			public void send() {
				String sPass = "";
				if(pw.isEnabled()) {
					sPass = pw.getText();
				}
				player.pass = sPass;
				player.send("/game" + num +" " + priv + " " + sPass);		//sendet an den Server, dass er einen neuen Gameserver erstellen soll und die dazu nötigen Infos gleich mit
			}
			
		}
		
		//alle dinge sind soweit versucht worden zu benennen, dass sie beim vergleich des GUIs mit dem Code erkennbar werden
		
		private Player player, gamePlayer;
		private JFrame frame;
		private final JButton buttonLobby1 = new JButton("Empty Lobby");
		private final JButton buttonLobby2 = new JButton("Empty Lobby");
		private final JButton buttonLobby3 = new JButton("Empty Lobby");
		private final JButton buttonLobby4 = new JButton("Empty Lobby");
		private final JTextPane textPane_chat = new JTextPane();
		private final TextField textField_chatInput = new TextField();
		private final Button button_send = new Button("Send");
		private final JLabel lblNewLabel = new JLabel("Chat");
		private final JTextPane textPane_online = new JTextPane();
		private final JLabel lblNewLabel_1 = new JLabel("Online:");
		private JScrollPane scroller;
		
		/**
		 * Create the application.
		 */
		public Lobby(String pIP, String pName) {
			initialize();
			frame.setVisible(true);
			startHub(pIP, pName);
		}

		/**
		 * Initialize the contents of the frame.
		 */
		private void initialize() {
			frame = new JFrame();
			frame.setBackground(Color.GRAY);
			frame.getContentPane().setBackground(Color.GRAY);
			frame.getContentPane().setLayout(null);
			frame.setForeground(Color.WHITE);
			frame.getContentPane().setForeground(Color.WHITE);
			buttonLobby1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			buttonLobby1.setForeground(Color.WHITE);
			buttonLobby1.setBackground(Color.DARK_GRAY);
			buttonLobby1.setBounds(30, 50, 200, 50);
			buttonLobby1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new checkWin(1);
				}
			});
			
			frame.getContentPane().add(buttonLobby1);
			buttonLobby2.setBackground(Color.DARK_GRAY);
			buttonLobby2.setForeground(Color.WHITE);
			buttonLobby2.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			buttonLobby2.setBounds(30, 150, 200, 50);
			buttonLobby2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			
			frame.getContentPane().add(buttonLobby2);
			buttonLobby3.setForeground(Color.WHITE);
			buttonLobby3.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			buttonLobby3.setBackground(Color.DARK_GRAY);
			buttonLobby3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			buttonLobby3.setBounds(30, 250, 200, 50);
			
			frame.getContentPane().add(buttonLobby3);
			buttonLobby4.setBackground(Color.DARK_GRAY);
			buttonLobby4.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			buttonLobby4.setForeground(Color.WHITE);
			buttonLobby4.setBounds(30, 350, 200, 50);
			
			frame.getContentPane().add(buttonLobby4);
			textPane_chat.setForeground(Color.WHITE);
			textPane_chat.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			textPane_chat.setBackground(Color.GRAY);
			textPane_chat.setEditable(false);
			scroller = new JScrollPane(textPane_chat);
			scroller.setBounds(250, 50, 300, 350);
			scroller.setAutoscrolls(true);
			
			frame.getContentPane().add(scroller);
			textField_chatInput.setForeground(Color.BLACK);
			textField_chatInput.setBackground(Color.WHITE);
			textField_chatInput.setBounds(250, 420, 200, 20);
			
			frame.getContentPane().add(textField_chatInput);
			button_send.setBackground(Color.DARK_GRAY);
			button_send.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						player.send("/chat "+textField_chatInput.getText());
					} catch (Exception e1) {}
					textField_chatInput.setText("");
				}
			});
			button_send.setBounds(474, 420, 76, 22);
			
			frame.getContentPane().add(button_send);
			lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 20));
			lblNewLabel.setForeground(Color.WHITE);
			lblNewLabel.setBounds(250, 20, 100, 30);
			
			frame.getContentPane().add(lblNewLabel);
			textPane_online.setText(" ");
			textPane_online.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			textPane_online.setForeground(Color.WHITE);
			textPane_online.setBackground(Color.GRAY);
			textPane_online.setBounds(570, 50, 200, 350);
			textPane_online.setEditable(false);
			
			frame.getContentPane().add(textPane_online);
			lblNewLabel_1.setForeground(Color.GREEN);
			lblNewLabel_1.setBounds(570, 20, 100, 30);
			
			frame.getContentPane().add(lblNewLabel_1);
			frame.setFont(new Font("Roboto Mono Medium", Font.PLAIN, 12));
			frame.setResizable(false);
			frame.setBounds(100, 100, 800, 500);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		

		public void startHub(String pIP, String pName)
		{
			player = new Player(pIP, 13370, pName, "");
		}
		
		public void startGame(String pIP, int pNumber, String pName, String password) {
			gamePlayer = new Player(pIP, 13370+pNumber, pName, password);
			frame.setVisible(false);
			System.out.println("Changed Server to Gameserver " + pNumber);
		}
	}


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Start window = new Start();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Start() {
		initialize();
	}

	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		textField.setBounds(85, 75, 300, 30);
		textField.setColumns(10);
		frame = new JFrame();
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setForeground(Color.WHITE);
		frame.getContentPane().setBackground(Color.GRAY);
		lblWilkommenBeiSchiffe.setForeground(Color.BLACK);
		lblWilkommenBeiSchiffe.setFont(new Font("Roboto Mono Medium", Font.BOLD, 18));
		lblWilkommenBeiSchiffe.setBounds(10, 0, 430, 55);
		frame.getContentPane().add(lblWilkommenBeiSchiffe);
		lblWilkommenBeiSchiffe.setBackground(Color.GRAY);
		
		frame.getContentPane().add(textField);
		lblIp.setFont(new Font("Roboto Mono Medium", Font.BOLD, 15));
		lblIp.setBounds(20, 75, 75, 30);
		
		frame.getContentPane().add(lblIp);
		textField_1.setColumns(10);
		textField_1.setBounds(85, 140, 300, 30);
		
		frame.getContentPane().add(textField_1);
		lblName.setFont(new Font("Roboto Mono Medium", Font.BOLD, 15));
		lblName.setBounds(20, 140, 75, 30);
		
		frame.getContentPane().add(lblName);
		btnJoin.setFont(new Font("Roboto Mono Light", Font.BOLD, 12));
		btnJoin.setForeground(Color.WHITE);
		btnJoin.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		btnJoin.setBackground(Color.DARK_GRAY);
		btnJoin.setBounds(150, 200, 150, 30);
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					new Lobby(textField.getText(), textField_1.getText());
					System.out.println("CONNECTED");
				} catch (Exception e1) {System.out.println("FAILED TO CONNECT");	return;}
				frame.setVisible(false);
			}
		});
		
		frame.getContentPane().add(btnJoin);
		frame.setBackground(Color.GRAY);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
