public class ServerHub extends Server {

	private List<String> players = new List<String>();
	private int active = 0;
	private List<GameServer> games = new List<GameServer>();
	boolean s1, s2, s3, s4, r1, r2, r3, r4, o1, o2, o3, o4;

	private class GameServer extends Server { // innere Klasse, damit leichter zu erstellen
		int number;
		private List<String> players = new List<String>();
		private List<String> viewers = new List<String>();
		private int nPlayers = 0;
		private int nViewers = 0;
		private boolean priv;
		String password;

		public GameServer(int pNumber, String bool, String pw) {
			super(13370 + pNumber);
			number = pNumber;
			System.out.println(number);
			priv = Boolean.getBoolean(bool);
			password = pw;
		}

		@Override
		public void processNewConnection(String pClientIP, int pClientPort) {
			send(pClientIP, pClientPort, "/INFO");
			System.out.println(pClientIP + pClientPort);
		}

		public int getNumber() {
			return number;
		}

		private boolean check(String m) {
			String[] wordsWords = m.split(":");
			if (wordsWords.length >= 2)
				if (wordsWords[1].equals(password))
					return true;
				else
					return false;
			return true;
		}

		private String removePw(String pMessage) {
			String[] wordswords = pMessage.split(":");
			return wordswords[0];
		}

		@Override
		public void processMessage(String pClientIP, int pClientPort, String pMessage) {
			System.out.println(pMessage);

			// check password every time; else kick
			if ((priv && check(pMessage)) || !priv) {

				String command = removePw(pMessage);
				processCommand(pClientIP, pClientPort, command);

				System.out.println(nPlayers);
			} else {
				send(pClientIP, pClientPort, "/WRONGPW");
				closeConnection(pClientIP, pClientPort);
			}

		}

		public void processCommand(String pClientIP, int pClientPort, String pMessage) {

			if (pMessage.startsWith("/info")) { // bekommt name vom Client
				String name = pMessage.replace("/info ", "");
				if (nPlayers < 2) {
					players.append(pClientIP + ":" + name);
					nPlayers++;
					send(pClientIP, pClientPort, "/PLAYER"); // weist den Client den Spielern zu
					if (nPlayers == 2) {
						sendToAll("/START");
					}
					send(pClientIP, pClientPort, "/WAIT4PLAYER");
				} else {
					viewers.append(pClientIP + ":" + name);
					nViewers++;
					send(pClientIP, pClientPort, "/VIEWER"); // weist den Client den Zuschauern zu
					sendToAll("/CHAT [INFO]    " + name + " schaut dem Spiel zu!");
					sendToAll("/VIEWERS " + nViewers);
				}
			} else if (pMessage.startsWith("/chat")) { // wenn es eine Chat Nachicht ist
				String name = nameVonIP(pClientIP);
				String nachicht = pMessage.replace("/chat ", "");
				sendToAll("/CHAT <" + name + ">	" + nachicht);
			}
			// ToDo: kommunikation zwischen den Spielern untereinander und zu den Viewern

			else if (Character.isDigit(pMessage.charAt(1))) {
				int x = pMessage.charAt(1);
				int y = pMessage.charAt(2);

				boolean pOne = false;

				players.toFirst();
				String[] player = players.getContent().split(":");
				if (!player[0].equals(pClientIP)) {
					pOne = false;
				}
				
				sendToAll("/HIT " + pOne + " " + x + y);
			}
		}

		@Override
		public void processClosingConnection(String pClientIP,
				int pClientPort) { /*
									 * sollte ein Spieler den GameServer verlassen, wird das Spiel und somit der
									 * Server beendet, sonst gibt es nur eine nachicht, das ein Viewer den Server
									 * verlassen hat
									 */
			boolean playerLeft = false;
			players.toFirst();
			while (players.hasAccess()) {
				String[] player = players.getContent().split(":");
				if (player[0].equals(pClientIP)) {
					players.remove();
					nPlayers--;
					playerLeft = true;
					break;
				} else {
					players.next();
				}
			}
			if (nPlayers < 2) {
				sendToAll("/CHAT [INFO]    Ein Spieler hat das Spiel verlassen. Spiel wird in 10 Sekunden beendet!");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sendToAll("/BACK");
				gameEnd(number);
				System.out.println("Closed");
				close();
			} else {
				if (!playerLeft) {
					nViewers--;
					sendToAll("/VIEWERS " + nViewers);
				}
			}
		}

	}

	public static void main(String[] args) {
		new ServerHub();
	}

	public ServerHub() {
		super(13370);
		System.out.println("START");

		// haufen boolean die bestimmen ob ein GameServer o = offline ist, s = searching
		// ist, r = running ist
		s1 = s2 = s3 = s4 = r1 = r2 = r3 = r4 = false;
		o1 = o2 = o3 = o4 = true;
	}

	@Override
	public void processNewConnection(String pClientIP, int pClientPort) {
		send(pClientIP, pClientPort, "/INFO");
		active++;
	}

	private String removePw(String pMessage) {
		String[] wordswords = pMessage.split(":");
		return wordswords[0];
	}

	@Override
	public void processMessage(String pClientIP, int pClientPort, String pMessage) {
		System.out.println(pMessage);
		pMessage = removePw(pMessage);

		if (pMessage.startsWith("/info")) {
			String name = pMessage.replace("/info ", "");
			players.append(pClientIP + ":" + name);
			sendToAll("/CHAT [INFO]    " + name + " hat den Server betreten!");
		} else if (pMessage.startsWith("/chat")) {
			String name = nameVonIP(pClientIP);
			String nachicht = pMessage.replace("/chat ", "");
			sendToAll("/CHAT <" + name + ">	" + nachicht);
		} else if (pMessage.startsWith("/online")) {
			sendToAll("/ONLINESTART");
			players.toFirst();
			while (players.hasAccess()) {
				String[] player = players.getContent().split(":");
				String name = player[1];
				sendToAll("/ONLINE " + name);
				players.next();
			}
		} else if (pMessage.startsWith("/game1")) // Game Server 1
		{
			if (o1) {
				String[] wordsWords = pMessage.split(" ");
				try {
					GameServer game1 = new GameServer(1, wordsWords[1], wordsWords[2]);
					games.append(game1);
				} catch (Exception e) {
					GameServer game1 = new GameServer(1, wordsWords[1], "");
					games.append(game1);
				}
				send(pClientIP, pClientPort, "/GAME1");
				sendToAll("/SEARCHING1");
				o1 = false;
				s1 = true;
			} else if (s1) {
				send(pClientIP, pClientPort, "/GAME1");
				sendToAll("/RUNNING1");
				s1 = false;
				r1 = true;
			} else if (r1) {
				send(pClientIP, pClientPort, "/GAME1");
			}
		} // ToDo: die anderen beiden SErver
	}

	public void gameEnd(int closer) { // Methode wenn ein GameServer Dicht macht, damit von alleen Clients die infos
										// geupdateet werden
		sendToAll("/GAME" + closer + "CLOSE");
		games.toFirst();
		while (games.hasAccess()) {
			int num = games.getContent().getNumber();
			if (num == closer) {
				games.remove();
			} else {
				games.next();
			}
		}
		switch (closer) {
		case 1:
			s1 = false;
			r1 = false;
			o1 = true;
			break;
		}
	}

	@Override
	public void processClosingConnection(String pClientIP, int pClientPort) {
		sendToAll("/ONLINESTART"); // überarbeitet bei allen verbliebenden Clients die Online Liste
		players.toFirst();
		while (players.hasAccess()) {
			String[] player = players.getContent().split(":");
			String name = player[1];
			if (player[0].equals(pClientIP)) {
				players.remove();
			} else {
				sendToAll("/ONLINE " + name);
				players.next();
			}
		}
		active--;
		if (active == 0) {
			System.out.println("[INFO]	Server Shutdown");
			close();
		}
	}

	private String nameVonIP(String pClientIP) { // Methode um durch die IP an den jeweiligen Client Namen aus der Liste
													// zu gelangen
		players.toFirst();
		while (players.hasAccess()) {
			String[] split = players.getContent().split(":");
			if (pClientIP.contentEquals(split[0])) {
				return split[1];
			}
			players.next();
		}
		return null;
	}
}