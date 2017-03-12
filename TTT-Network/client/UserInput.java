import java.io.*;

public class UserInput extends Thread
{
	public void run()
	{
		BufferedReader kin = new BufferedReader(new InputStreamReader(System.in));		
		while(true)
		{
			if (Client.logout)
			{
				return;
			}

			try
			{
				String command = kin.readLine();
				if (command.equals("Logout"))
				{
					Client.send(command);
					
					String response = Client.read();
					Client.logout = true;
					return;
				}
				else
				{
					Client.send(command);
				}
			}
			catch (Exception e)
			{
				
			}
		}
	}
}
