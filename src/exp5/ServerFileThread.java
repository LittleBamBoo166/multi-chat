package exp5;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServerFileThread extends Thread {
	ServerSocket server = null;
	Socket socket = null;
	static List<Socket> list = new ArrayList<Socket>(); // store cac socket phia nguoi dung

	public void run() {
		try {
			server = new ServerSocket(8090); // server socket trên cổng 8090
			// accept các client
			while (true) {
				socket = server.accept();
				list.add(socket);
				// bat dau file transfer thread
				FileReadAndWrite fileReadAndWrite = new FileReadAndWrite(socket);
				fileReadAndWrite.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class FileReadAndWrite extends Thread {
	private Socket nowSocket = null;
	private DataInputStream input = null;
	private DataOutputStream output = null;

	public FileReadAndWrite(Socket socket) {
		this.nowSocket = socket;
	}

	public void run() {
		try {
			input = new DataInputStream(nowSocket.getInputStream()); // Input stream
			while (true) {
				// get text name and length
				String textName = input.readUTF();
				long textLength = input.readLong();
				// send text name va text length toi tat ca client
				for (Socket socket : ServerFileThread.list) {
					output = new DataOutputStream(socket.getOutputStream()); // output stream
					if (socket != nowSocket) { // send to cac client khac
						output.writeUTF(textName);
						output.flush();
						output.writeLong(textLength);
						output.flush();
					}
				}
				// send the content
				int length = -1;
				long curLength = 0;
				byte[] buff = new byte[1024];
				while ((length = input.read(buff)) > 0) {
					curLength += length;
					for (Socket socket : ServerFileThread.list) {
						output = new DataOutputStream(socket.getOutputStream()); // output stream
						if (socket != nowSocket) { // send to cac client khac
							output.write(buff, 0, length);
							output.flush();
						}
					}
					if (curLength == textLength) { // ket thuc
						break;
					}
				}
			}
		} catch (Exception e) {
			ServerFileThread.list.remove(nowSocket); // dong thread, remove cac socket
		}
	}
}