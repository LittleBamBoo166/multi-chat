package exp5;

import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class Client {
	// main function, create a new login window
	public static void main(String[] args) {
		// gọi cửa sổ login bên file Login.java
		new Login();
	}
}

/**
 * read và write message của client, đăng nhập
 * đăng nhập và gửi mess được viết riêng để share dữ liệu
 */
class ClientReadAndPrint extends Thread {
	static Socket mySocket = null; // static, nếu không sẽ bị empty khi create a new thread
	static JTextField textInput;
	static JTextArea textShow;
	static JFrame chatViewJFrame;
	static BufferedReader in = null;
	static PrintWriter out = null;
	static String userName;

	// used to nhận messages từ server
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(mySocket.getInputStream())); // input stream
			while (true) {
				String str = in.readLine(); // get input từ server (tin nhắn của other client khác)
				textShow.append(str + '\n'); // add message vào text area
				textShow.setCaretPosition(textShow.getDocument().getLength()); // set thanh cuộn at the bottom
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Login listener
	class LoginListen implements ActionListener {
		JTextField textField;
		JPasswordField pwdField;
		JFrame loginJFrame; // login window itself

		// giao diện chat của client
		ChatView chatView = null;

		public void setJTextField(JTextField textField) {
			this.textField = textField;
		}

		public void setJPasswordField(JPasswordField pwdField) {
			this.pwdField = pwdField;
		}

		public void setJFrame(JFrame jFrame) {
			this.loginJFrame = jFrame;
		}

		public void actionPerformed(ActionEvent event) {
			userName = textField.getText();
			String userPwd = String.valueOf(pwdField.getPassword()); // method getPassword() để lấy giá trị password
			if (userName.length() >= 1 && userPwd.equals("123")) { // Nếu password = 123 và username >= 1
				chatView = new ChatView(userName); // tạo cửa sổ chat mới, set username của cửa sổ chat (set tĩnh)
				// khởi tạo connection tới server
				try {
					InetAddress addr = InetAddress.getByName(null); // lấy địa chỉ host
					mySocket = new Socket(addr, 8081); // client socket, port 8081
					loginJFrame.setVisible(false); // hide cửa sổ login
					out = new PrintWriter(mySocket.getOutputStream()); // output stream
					out.println("User: " + userName + " enter the chat room!"); // in ra trong server và các client khác
					out.flush(); // clear dữ liệu bộ đệm
				} catch (IOException e) {
					e.printStackTrace();
				}
				// tạo luồng read và print mới
				ClientReadAndPrint readAndPrint = new ClientReadAndPrint();
				readAndPrint.start();
				// tạo file thread mới
				ClientFileThread fileThread = new ClientFileThread(userName, chatViewJFrame, out);
				fileThread.start();
			} else {
				JOptionPane.showMessageDialog(loginJFrame,
						"The account number or password is incorrect, please re-enter", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	// giao diện chat
	class ChatViewListen implements ActionListener {
		public void setJTextField(JTextField text) {
			textInput = text; // có thể dùng ở chỗ khác
		}

		public void setJTextArea(JTextArea textArea) {
			textShow = textArea;
		}

		public void setChatViewJf(JFrame jFrame) {
			chatViewJFrame = jFrame;
			// đóng giao diện chat
			chatViewJFrame.addWindowListener(new WindowAdapter() {
				// khi client nhấn nút đóng ở cửa sổ chat, người đó sẽ rời chat room
				public void windowClosing(WindowEvent e) {
					out.println("User " + userName + " leave the chat room!");
					out.flush(); // xoá bộ nhớ đệm
					System.exit(0);
				}
			});
		}

		// monitor execution function
		public void actionPerformed(ActionEvent event) {
			try {
				String str = textInput.getText();
				// if textbox content rỗng, người dùng không nhập tin nhắn mà đã nhấn gửi
				if ("".equals(str)) {
					textInput.grabFocus(); // set focus
					// hiện message dialog
					JOptionPane.showMessageDialog(chatViewJFrame, "Input is empty, please re-enter!", "Warning",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				out.println(userName + " say: " + str); // output to server
				out.flush(); // clear bộ nhớ đệm

				textInput.setText(""); // làm rỗng textbox nhắn tin
				textInput.grabFocus(); // set focus
				// textInput.requestFocus(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
