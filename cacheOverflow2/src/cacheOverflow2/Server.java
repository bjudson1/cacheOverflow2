package cacheOverflow2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Platform;

public class Server {
	private ServerSocket serverSocket;
	private int clientCount;
	
	public Server() {
		clientCount = 0;
	}
	
	public void initialize() {
		new Thread( () -> {
			try {
				serverSocket = new ServerSocket(8000);
				
				while(true) {
					Socket socket = serverSocket.accept();
					clientCount++;
					
					Platform.runLater( () -> {
						System.out.println(clientCount);
					});
					
					new Thread(new Handler(socket)).start();
				}
			}
			catch(IOException ex){
				System.err.println(ex);
			}
		}).start();
	}
}
