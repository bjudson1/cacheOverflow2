package cacheOverflow2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	private PrintWriter output;
	private BufferedReader input;
	private Socket clientSocket;
	
	public Client(String ip,int port){ 
		connect(ip,port);
	}
	
	public void connect(String ip,int port) {
		try{
			clientSocket = new Socket(ip,port);
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			output = new PrintWriter(clientSocket.getOutputStream(),true);			
		}
		catch(IOException e) {
			System.err.println(e);
		}
	}
	
	public void disconnect() {
		try{
			input.close();
			output.close();
			clientSocket.close();
		}
		catch(IOException e) {
			System.err.println(e);
		}
	}
	
	public String sendAndRecieve(String message) {
		output.println(message);
		
		String response = "";
		try{
			response = input.readLine();
		}
		catch(IOException e) {
			System.err.println(e);
		}
		
		return response;
	}
	
}
