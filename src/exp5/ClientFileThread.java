package exp5;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class ClientFileThread extends Thread {
	private Socket socket = null;
	private JFrame chatViewJFrame = null;
	static String userName = null;
	static PrintWriter out = null; // sending các message từ Server.java
	static DataInputStream fileIn = null;
	static DataOutputStream fileOut = null;
	static DataInputStream fileReader = null;
	static DataOutputStream fileWriter = null;

	public ClientFileThread(String userName, JFrame chatViewJFrame, PrintWriter out) {
		ClientFileThread.userName = userName;
		this.chatViewJFrame = chatViewJFrame;
		ClientFileThread.out = out;
	}

	// Client receives files
	public void run() {
		try {
			InetAddress addr = InetAddress.getByName(null); // get host address
			socket = new Socket(addr, 8090); // create new socket for current client
			fileIn = new DataInputStream(socket.getInputStream()); // input stream
			fileOut = new DataOutputStream(socket.getOutputStream()); // output stream
			// nhận message
			while (true) {
				String textName = fileIn.readUTF();
				long totleLength = fileIn.readLong();
				int result = JOptionPane.showConfirmDialog(chatViewJFrame, "Whether to accept?", "Option",
						JOptionPane.YES_NO_OPTION);
				int length = -1;
				byte[] buff = new byte[1024];
				long curLength = 0;
				// result = 0 => OK, ngc lại
				if (result == 0) {
					File userFile = new File("D:\\Code Java\\multichat-system\\file\\" + userName);
					if (!userFile.exists()) { // Create a new folder for the current user
						userFile.mkdir();
					}
					File file = new File("D:\\Code Java\\multichat-system\\file\\" + userName + "\\" + textName);
					fileWriter = new DataOutputStream(new FileOutputStream(file));
					while ((length = fileIn.read(buff)) > 0) { // write file to local
						fileWriter.write(buff, 0, length);
						fileWriter.flush();
						curLength += length;
						if (curLength == totleLength) { // buộc dừng
							break;
						}
					}
					out.println("User " + userName + " received the file!");
					out.flush();
					// Tip file storage address
					JOptionPane.showMessageDialog(chatViewJFrame, "Rile storage address: \n" +
							"D:\\Code Java\\multichat-system\\file\\" +
							userName + "\\" + textName, "Option", JOptionPane.INFORMATION_MESSAGE);
				} else { // not accepted
					while ((length = fileIn.read(buff)) > 0) {
						curLength += length;
						if (curLength == totleLength) { // buộc dừng
							break;
						}
					}
				}
				fileWriter.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Client sends file
	static void outFileToServer(String path) {
		try {
			File file = new File(path);
			fileReader = new DataInputStream(new FileInputStream(file));
			fileOut.writeUTF(file.getName()); // send file name
			fileOut.flush();
			fileOut.writeLong(file.length()); // send file length
			fileOut.flush();
			int length = -1;
			byte[] buff = new byte[1024];
			while ((length = fileReader.read(buff)) > 0) { // send nội dung

				fileOut.write(buff, 0, length);
				fileOut.flush();
			}

			out.println("User " + userName + " file sent successfully");
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
