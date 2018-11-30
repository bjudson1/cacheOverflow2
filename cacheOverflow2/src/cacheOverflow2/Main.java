package cacheOverflow2;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//UI ui = new UI();
		UI.run(args);
		
		Server server = new Server();
		server.initialize();
	}

}
