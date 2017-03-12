import java.net.*;
import java.io.*;
import java.util.*;

public class CThread extends Thread
{	
	String nick;
	Boolean connected;
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	Socket clientSocket;
	String requestResponse;
	String fromPlayer, toPlayer;
	
	public static int n = 0;
	
	CThread(Socket s)
	{
		super("CThread");
		connected = false;
		nick = "";

		//constructor
		clientSocket = s;
		try 
		{
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	public boolean equals(CThread c)
	{
		return (c.nick.equals(this.nick));
	}

	synchronized void send(String msg)
	{
			out.println(msg);	
	}
	
	void listen()
	{
		try 
		{
			while (true)
			{	   			
	       	    String msg = in.readLine();
	       		System.out.println(msg);	
	        	if (msg.startsWith("Login"))
	        	{
					login(msg);
	        	}
	        	//logout
	        	else if (msg.equals("Logout"))
	        	{	
	        		if (connected)
	        		{	
	        			connected = false;
	        			int k = Server.clients.indexOf(this);
	        			Server.clients.remove(this);
						sendList();
	        			out.println("OK");
	        			out.close();
	        			in.close();
	        			clientSocket.close();
	        			return;
	        		}
	        		else
	        		{
	        			send("Not Logged in !!");
	        		}
	        		
	        	}
	        	
	        	// Game Request
	        	else if (msg.startsWith("PlayRequest "))
	        	{
	        		StringTokenizer st = new StringTokenizer(msg.substring(12,msg.length()),", ");   	
	        		
	        		String message = st.nextToken();	        		
	        		String to = st.nextToken();
					toPlayer = to;
					fromPlayer = nick;
					
					System.out.println("FROM "  + fromPlayer + "TO " + toPlayer);
					
					boolean success = false;

	        		for (int i = 0; i < Server.clients.size() ; i ++)
	        		{
	        			CThread t = (CThread)Server.clients.get(i);
	        			if (t.nick.equals(to))
	        			{
	        				requestResponse = nick+":"+t.nick;
	        				t.send("RequestRecieve #"+ nick+":"+t.nick+";" + message);
	        				success = true;
	        				break;
	        			}
	        		}
	        			 
	        		if (!success)
	        		{
	        			send("Error: Couldn't send request to player.");
	        		}       		
	        	}
	        	// Game request response
	        	else if (msg.startsWith("Accept"))
	        	{
	        		n++;
	        		
	        		String from = msg.substring(msg.indexOf("#")+1,msg.indexOf(":"));
					String to = msg.substring(msg.indexOf(":")+1,msg.length());
				
	        		System.out.println(from + "\n" + to);
	        		
	        		//boolean success = false;
					
	        		for (int i = 0; i < Server.clients.size() ; i ++)
	        		{
	        			CThread player1Thread = (CThread)Server.clients.get(i);
	        			if (player1Thread.nick.equals(to))
	        			{
	        				player1Thread.send("Play2" + from + "#" + n);
	        				//success = true;
	        				break;
	        			}
	        		}
	        		for (int i = 0; i < Server.clients.size(); i++) {
	        			CThread player2Thread = (CThread)Server.clients.get(i);
	        			if (player2Thread.nick.equals(from)) {
	        				player2Thread.send("Play1" + to + "#" + n);
	        			}
	        		}
	        	}
	        	else if (msg.startsWith("Player1Move")) {
	        	
	        	}
	        	else
	        	{
	        		send(msg);
	        	}
	   		}
		}
		catch (SocketException e)
		{
				    if (connected)
	        		{
	        			try 
	        			{        			
	        				connected = false;
	        				int k = Server.clients.indexOf(this);
	        				Server.clients.remove(this);
							sendList();
	       		 			out.close();
	       		 			in.close();
	        				clientSocket.close();
	        				return;
	       	 			}
	        			catch (Exception d)
	        			{
	        				return;
	        			}
	        		}
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void run() 
	{
		listen();
	}
	
	boolean login(String msg)
	{
	    if (connected)
	    {
	    	out.println("Allready Connected!");
	    	return true;
	    }
		boolean exists = false;
		System.out.println("Login" + msg.substring(5, msg.length()));
	    for (int i = 0;i<Server.clients.size();i++)
	    {
	    	if (Server.clients.get(i) != null)
	        {
	        	//exists = true break
				System.out.println(msg.substring(7, msg.length()));
				CThread temp = (CThread)Server.clients.get(i);
	        	if ((temp.nick).equals(msg.substring(7, msg.length())))
	        	{
					exists = true;
	        		break;
	        	}

	        }
		}

		if (exists)
		{
			out.println("NewNick");
		}
		else
		{
			connected = true;		
			nick = msg.substring(7,msg.length());
	        sendList();
		}
	    return true;
	}
	
	void sendList()
	{
		String list = "";
		System.out.println(Server.clients.size());
		if (Server.clients.size() == 0)
		{
			return;
		}

	    for (int i = 0;i<Server.clients.size();i++)
	    {
	    	CThread temp = (CThread)Server.clients.get(i); 
	    	if (Server.clients.get(i) != null)
	        {
	        		if (connected)
	        		{
	        			list =temp.nick + "," + list  ;
	        		}
	        }
		}

		list = "List " +list.substring(0,list.length() -1) +";";

	    for (int i = 0; i < Server.clients.size() ; i ++)
	    {
	    	CThread t = (CThread)Server.clients.get(i);
	    	if (t.connected)
	    	{
	    		t.send(list);
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
