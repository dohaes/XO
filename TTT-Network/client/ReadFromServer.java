import java.util.*;
import javax.swing.*;

public class ReadFromServer extends Thread
{
	Client c;
	
	//constructor
	ReadFromServer(Client cc)
	{
		c = cc;
	}
	
	public void run()
	{	
		// Server message
		String s;
		while (true)
		{
			if (Client.logout)
			{
				return;
			}
			// Read message from server
			s = Client.read();
			// When a client logs
			if (s.startsWith("List"))
			{
				Client.mainText.setText("Connected as " + Client.nick);
				c.setTitle("Tic Tac Toe - " + c.nick + " - Connected to " + c.server);
				//connected = true;
				Client.connected = true;
				Client.list.clear();
				String nextNick = "";
				
				StringTokenizer st = new StringTokenizer(s.substring(5,s.length()),", ");
				
				String temp = null;
				while(st.hasMoreTokens())
				{
					temp = st.nextToken();
					Client.list.addElement(replace(temp,";",""));
				}
				
				System.out.print("List updated: New names: ");
				for (int i = 0; i < Client.list.size();i++)
				{
					System.out.print(Client.list.get(i) + " ");
				}
				System.out.println();
			}
			// useless stuff
			else if (s.startsWith("Recieve"))
			{
				Client.mainText.setText(Client.mainText.getText() + "\n" + s.substring(8,s.length()));
				Client.mainText.setCaretPosition(Client.mainText.getText().length());
			}
			// displays a notification to other client about the player 1's request
			else if (s.startsWith("RequestRecieve"))
			{	
				String from = s.substring(s.indexOf("#"),s.indexOf(":"));
				String to = s.substring(s.indexOf(":"),s.indexOf(";"));
				String message = s.substring(14,s.length());
				int reply = JOptionPane.showConfirmDialog(null, message, "Game Request", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
          			JOptionPane.showMessageDialog(null, "HELLO");
          			Client.send("Accept"+from+to);
        		}
			}
			// uselss stuff
			else if (s.startsWith("Accept")) {
				System.out.println("ACCEPT");
			}
			// notify player 1 that the game is started
			else if (s.startsWith("Play1")) {
  				Game game = new Game();
				System.out.print("Player 1 START");
				System.out.println("\n" + s);
				System.out.println("Game number: " + s.substring(s.indexOf("#"),s.length()));
			}
			// notify player 2
			else if (s.startsWith("Play2")) {
				  Game game = new Game();
				System.out.print("Waiting for player 1 move");
				System.out.print("\n" + s);
				System.out.println("Game number: " + s.substring(s.indexOf("#"),s.length()));
			}
			else if (s.startsWith("NewNick"))
			
			{
				Client.mainText.setText("");
				String newnick =  JOptionPane.showInputDialog(null, "Nickname already in use, choose another one:");
				Client.connected = false;
				Client.jMenuItem1.setEnabled(true);
       	 		Client.jMenuItem2.setEnabled(false);

				if (newnick != null)
				{
					Client.nick = newnick;
					Client.jMenuItem1.setEnabled(false);
       	 			Client.jMenuItem2.setEnabled(true);
       	 			Client.send("Login: "+newnick);
				}
			}
		}
	}
	static String replace(String str, String pattern, String replace) 
	{
  	  	int s = 0;
  	  	int e = 0;
  	  	StringBuffer result = new StringBuffer();
    	while ((e = str.indexOf(pattern, s)) >= 0) 
    	{
    		result.append(str.substring(s, e));
       	    result.append(replace);
       	    s = e+pattern.length();
    	}
    	result.append(str.substring(s));
    	return result.toString();
    }
}
