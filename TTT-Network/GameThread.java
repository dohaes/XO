import java.net.*;
import java.io.*;
import java.util.*;

public class GameThread extends Thread {

	private static Socket gameSocket = null;
	private static BufferedReader inData = null;
	private static PrintWriter outData = null;
	private static String outString = "";
	private static String inString = "";
	private static int inInt = -5;
	protected static boolean exitThread = false;
	
	String nick;
	Boolean connected;
	Socket gameSocket;
	PrintWriter out;
	BufferedReader in;
	String playerX, playerY;

	public GameThread(Socket socket, String player1, String player2) {
		super("GameThread");
		GameThread.gameSocket = socket;
		GameThread.playerX = player1;
		GameThread.playerY = player2;
		
		try 
		{
			out = new PrintWriter(gameSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(gameSocket.getInputStream()));
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	
	@Override
	public void run(){
		
		//Sending/Receiving Data Game Loop
		while(!exitThread) {
			try {
				//While waiting for incoming data, if we're ready to send out data, do so
				if(!in.ready() && !exitThread) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException ie) {
						System.err.println("Error Holding Thread");
						System.err.println(ie);
					}
					if(!outString.isEmpty()){
						outData.println(outString);
						outString = "";
					}
				}
				
				//When we are receiving incoming data and not exiting the game
				while(inData.ready() && !exitThread) {
					//Read incoming data
					inString = inData.readLine();
					//Get the integer out of the incoming stream
					inInt = Integer.parseInt(inString);
					//Data handling
					if(inInt != -1 && inInt != -2) {
						//Show in Chat the other player's move
						BoardGui.updateChat(timeStamp() + "SYSTEM: Other Player Moved Slot: " + 
								(inInt + 1));
						NPlayer.setInGameData(inInt); //Set the move onto the board
					//if -1, player wants to play again, -2, quit
					} else {
						NPlayer.setPlayAgain(inInt);
					}
					break; //Break out of reading data since we are done now.
				}
			} catch(IOException ioe) {
				if (!exitThread){
					System.err.println("Error with Game I/O Streams");
					System.err.println(ioe);
				}
			}
		}
		quitThread(); //At the end of the run cycle, properly exit the thread
	}
}
