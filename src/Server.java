import java.io.*;
import java.util.*;
import java.net.*;

public class Server {
	private static ServerSocket serverSocket;
	private static Socket clientSocket;

	static ArrayList<ClientHandler> ch = new ArrayList<ClientHandler>();

	static int i = 0;

	public static void main(String[] args) throws IOException {
		try {
			serverSocket = new ServerSocket(1234);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				System.out.println("New client request received : " + clientSocket);

				DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
				DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

				System.out.println("Creating a new handler for this client...");

				ClientHandler mtch = new ClientHandler(ch, clientSocket, "client " + i, dis, dos);

				Thread t = new Thread(mtch);

				System.out.println("Adding this client to active client list");

				ch.add(mtch);

				t.start();

				i++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
