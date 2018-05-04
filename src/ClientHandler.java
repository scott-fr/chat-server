import java.io.*;
import java.util.*;
import java.net.*;

public class ClientHandler extends Thread{
	Scanner scn = new Scanner(System.in);
	private String name;
	final DataInputStream dis;
	final DataOutputStream dos;
	private ArrayList<ClientHandler> clients;
	Socket s;
	boolean isLoggedIn;

	public ClientHandler(ArrayList<ClientHandler> clients, Socket s, String name, DataInputStream dis, DataOutputStream dos) {
		this.dos = dos;
		this.dis = dis;
		this.name = name;
		this.clients = clients;
		this.s = s;
		this.isLoggedIn = true;
	}
	
	public String getClientName(){
		return this.name;
	}

	@Override
	public void run() {
		String received;
		while (!this.s.isClosed()) {
			try {
				received = dis.readUTF();

				System.out.println(received);

				if (received.equals("logout")) {
					this.isLoggedIn = false;
					this.s.close();
					break;
				}

				for(int i = 0; i < clients.size(); i++){
					ClientHandler r = clients.get(i);
					if (r.isLoggedIn && r != this) {
						r.dos.writeUTF(this.name + " : " + received);
						break;
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			this.dis.close();
			this.dos.close();
			clients.remove(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
