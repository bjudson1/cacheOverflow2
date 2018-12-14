package cacheOverflow2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {
	private ServerSocket serverSocket;
	private int clientCount;
	private ArrayList<ArrayList<UserStory>> allLogs;
	private ArrayList<Socket> clients;
	
	public Server() {
		clientCount = 0;
		allLogs = null;
		clients = new ArrayList<Socket>();
	}

	public void initialize() {		
		new Thread(() -> {
			try {
				serverSocket = new ServerSocket(8000);
				System.out.println("server running");

				while (true) {
					//accept new client
					Socket client_socket = serverSocket.accept();
					clients.add(client_socket);
					clientCount++;
					
					//start new thread to handle it
					new Thread(new Handler(client_socket, this)).start();
				}
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}).start();
	}
	
	public ArrayList<ArrayList<UserStory>> getAllLogs(){
		return allLogs;
	}
	
	public void setAllLogs(ArrayList<ArrayList<UserStory>> allLogs) {
		this.allLogs = allLogs;
	}
	
	public void updateClients(Socket orig_client){
		for(Socket client : clients) {
        	if(client == orig_client) {
        		//System.out.println(client);
        		/*try {
        			//new ObjectOutputStream(client.getOutputStream()).writeObject(allLogs);
				} catch (IOException e) {
					e.printStackTrace();
				}*/
        	}
        }
	}
}

class Handler implements Runnable{
    private Socket socket; // A connected socket
    private Server server;
    
    public Handler(Socket socket, Server server) {
      this.socket = socket;
      this.server = server;
    }

    public void run() {
      try {
    	 ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());
    	 ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());

         while (true) {
	        //if receive new state
	        String input = (String) inputFromClient.readObject();
	        System.out.println(input);
	        //send state to client
	        if(input.equals("get_state")) {	   	 
	        	//there is no state loaded
	        	if(server.getAllLogs() == null) {
	        		//write to client
			        outputToClient.writeObject("none");
			        outputToClient.flush();
			           
			        //get state from client
			        server.setAllLogs((ArrayList<ArrayList<UserStory>>) inputFromClient.readObject());
			        
			        outputToClient.writeObject("done");
			        outputToClient.flush();
	        	}
	        	
	        	//else there is a state loaded to server
	        	else {
        			//write to client
		        	outputToClient.writeObject("ready?");
		            outputToClient.flush();
		            		            
		            //get conformation
		            input = (String) inputFromClient.readObject();
		            
		            //send state
		        	outputToClient.writeObject(server.getAllLogs());
        		}
	        	
	        	System.out.println("done");
        	}
        	
	        //update state from client
        	else if(input.equals("update")){
        		//System.out.println(input);
        		//send confirmation to client
	        	outputToClient.writeObject("ready");
	            outputToClient.flush();
	            
	            //get new state
	            server.setAllLogs((ArrayList<ArrayList<UserStory>>) inputFromClient.readObject()); 

	            //update clients
	            server.updateClients(socket);
        	}
        }
      }
      catch(IOException ex) {
  		ex.printStackTrace();
      } catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
      try {
		socket.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
    }
  }
