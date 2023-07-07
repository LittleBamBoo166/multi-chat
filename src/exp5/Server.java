package exp5;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	static ServerSocket server = null;
	static Socket socket = null;
	static List<Socket> list = new ArrayList<Socket>(); // luu tru socket cua client

	public static void main(String[] args) {
		MultiChat multiChat = new MultiChat(); // new giao dien chat MultiChat.java
		try {
			// thread bat dau truyen du lieu tren server den cac client
			ServerFileThread serverFileThread = new ServerFileThread();
			serverFileThread.start();
			server = new ServerSocket(8081); // server side socket
			// cho client ket noi
			while (true) {
				socket = server.accept(); // waiting for the connection
				list.add(socket); // add client da dc accept vao list
				// mo thread truyen du lieu tren client
				ServerReadAndPrint readAndPrint = new ServerReadAndPrint(socket, multiChat);
				readAndPrint.start();
			}
		} catch (IOException e1) {
			e1.printStackTrace(); // print loi xay ra (loi vao ra)
		}
	}
}

/**
 * server-side class luong read-write
 * server doc noi dung tu phia client bang class nay va gui den tat ca cac
 * client con lai
 */
class ServerReadAndPrint extends Thread {
	Socket nowSocket = null;
	MultiChat multiChat = null;
	BufferedReader in = null;
	PrintWriter out = null;

	public ServerReadAndPrint(Socket s, MultiChat multiChat) {
		this.multiChat = multiChat; // get giao dien tro chuyen chung
		this.nowSocket = s; // get current client
	}

	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(nowSocket.getInputStream())); // input stream
			// get the message from client va gui den tat ca cac client con lai
			while (true) {
				String str = in.readLine(); // get nội dung client nhập trong textbox tin nhắn
				// send to all client con lai
				for (Socket socket : Server.list) {
					out = new PrintWriter(socket.getOutputStream()); // create new print writer cho all client
					if (socket == nowSocket) { // current socket
						out.println("(YOU) " + str);
					} else { // send to other clients
						out.println(str);
					}
					out.flush(); // don cache
				}
				// set noi dung cho vùng show tin nhắn textarea
				multiChat.setTextArea(str);
			}
		} catch (Exception e) {
			Server.list.remove(nowSocket); // dong thread, remove socket hien tai
		}
	}
}
