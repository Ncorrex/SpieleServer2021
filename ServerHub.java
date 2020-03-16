
public class ServerHub extends Server {

	private List<String> players = new List<String>();
	private int active = 0;
	private List<GameServer> games = new List<GameServer>();
	boolean s1, s2, s3, s4, r1, r2, r3, r4, o1, o2, o3, o4;

	private class GameServer extends Server {
		int number;
		private List<String> players = new List<String>();
		private List<String> viewers = new List<String>();
		private int nPlayers = 0;
		private int nViewers = 0;

		public GameServer(int pNumber) {
			super(13370 + pNumber);
			number = pNumber;
			System.out.println(number);
		}

		@Override
		public void processNewConnection(String pClientIP, int pClientPort) {
			send(pClientIP, pClientPort, "/INFO");
			System.out.println(pClientIP + pClientPort);
		}

		public int getNumber()
		{
			return number;
		}
		@Override
		public void processMessage(String pClientIP, int pClientPort, String pMessage) {
			System.out.println(pMessage);
			if (pMessage.startsWith("/info")) {
				String name = pMessage.replace("/info ", "");
				if (nPlayers < 2) {
					players.append(pClientIP + ":" + name);
					nPlayers++;
					send(pClientIP, pClientPort, "/PLAYER");
					send(pClientIP, pClientPort, "/WAIT4PLAYER");
				} else {
					viewers.append(pClientIP + ":" + name);
					nViewers++;
					send(pClientIP, pClientPort, "/VIEWER");
					sendToAll("/CHAT [INFO]    " + name + " schaut dem Spiel zu!");
					sendToAll("/VIEWERS " + nViewers);
				}
			} else if (pMessage.startsWith("/chat")) {
				String name = nameVonIP(pClientIP);
				String nachicht = pMessage.replace("/chat ", "");
				sendToAll("/CHAT <" + name + ">	" + nachicht);
			}
			System.out.println(nPlayers);
		}

		@Override
		public void processClosingConnection(String pClientIP, int pClientPort) {
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
				if(!playerLeft)
				{
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
		s1 = s2 = s3 = s4 = r1 = r2 = r3 = r4 = false;
		o1 = o2 = o3 = o4 = true;
	}

	@Override
	public void processNewConnection(String pClientIP, int pClientPort) {
		send(pClientIP, pClientPort, "/INFO");
		active++;
	}

	@Override
	public void processMessage(String pClientIP, int pClientPort, String pMessage) {
		System.out.println(pMessage);
		if (pMessage.startsWith("/info")) {
			String name = pMessage.replace("/info ", "");
			players.append(pClientIP + ":" + name);
			sendToAll("/CHAT [INFO]    " + name + " hat den Server betreten!");
		} else if (pMessage.startsWith("/chat")) {
			String name = nameVonIP(pClientIP);
			String nachicht = pMessage.replace("/chat ", "");
			sendToAll("/CHAT <" + name + ">	" + nachicht);
		} else if (pMessage.startsWith("/online")) {
			send(pClientIP, pClientPort, "/ONLINESTART");
			players.toFirst();
			while (players.hasAccess()) {
				String[] player = players.getContent().split(":");
				String name = player[1];
				send(pClientIP, pClientPort, "/ONLINE " + name);
				players.next();
			}
		} else if (pMessage.startsWith("/game1")) // Game Server 1
		{
			if(o1)
			{
				GameServer game1 = new GameServer(1);
				games.append(game1);
				send(pClientIP, pClientPort, "/GAME1");
				sendToAll("/SEARCHING1");
				o1 = false;
				s1 = true;
			} else if(s1)
			{
				send(pClientIP, pClientPort, "/GAME1");
				sendToAll("/RUNNING1");
				s1 = false;
				r1 = true;
			} else if(r1)
			{
				send(pClientIP, pClientPort, "/GAME1");
			}
		} 
	}
	
	public void gameEnd(int closer) {
		sendToAll("/GAME" + closer + "CLOSE");
		games.toFirst();
		while(games.hasAccess())
		{
			int num = games.getContent().getNumber();
			if(num == closer)
			{
				games.remove();
			}
			else
			{
				games.next();
			}
		}
		switch (closer){
		case 1:
			s1 = false;
			r1 = false;
			o1 = true;
		}
	}

	@Override
	public void processClosingConnection(String pClientIP, int pClientPort) {
		sendToAll("/ONLINESTART");
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
			close();
		}
	}

	private String nameVonIP(String pClientIP) {
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
