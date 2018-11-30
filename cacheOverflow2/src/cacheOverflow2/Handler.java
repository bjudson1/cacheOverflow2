package cacheOverflow2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Handler implements Runnable{
	final static int COMMAND1 = 0;
	
	private Socket socket;
	
	public Handler(Socket socketIn) {
		socket = socketIn;
		run();
	}
	
	public void run() {
		try {
			BufferedReader clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter clientOutput = new PrintWriter(socket.getOutputStream());
			
			while(true) {
				int request = Integer.parseInt(clientInput.readLine());
			
				switch(request) {
					case COMMAND1: {
						System.out.println("Command 1");
					}
				}
			}
		}
		
		catch(IOException ex){
			System.err.println(ex);
		}
	}
}
