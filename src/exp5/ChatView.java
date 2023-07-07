package exp5;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ChatView {
	String userName; // user đặt khi đăng nhập
	JTextField text;
	JTextArea textArea;
	ClientReadAndPrint.ChatViewListen listener;

	// constructor
	public ChatView(String userName) {
		this.userName = userName;
		init();
	}

	// init function
	void init() {
		JFrame jf = new JFrame("Client");
		jf.setBounds(500, 200, 400, 330); // set toạ độ và size của frame
		jf.setResizable(false); // khum cho phép zoom

		JPanel jp = new JPanel();
		JLabel lable = new JLabel("User " + userName);
		textArea = new JTextArea("Login successful, welcome to the multiplayer chat room\n", 12, 35);
		textArea.setEditable(false); // khum cho phép sửa
		JScrollPane scroll = new JScrollPane(textArea); // thiết lập scroll pane cho textarea
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // always show cuộn dọc
		jp.add(lable);
		jp.add(scroll);

		text = new JTextField(20);
		JButton button = new JButton("Send");
		JButton openFileBtn = new JButton("Send files");
		jp.add(text);
		jp.add(button);
		jp.add(openFileBtn);

		// setup the openfile action listener
		openFileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showFileOpenDialog(jf);
			}
		});

		// setup action listener cho nút gửi
		listener = new ClientReadAndPrint().new ChatViewListen();
		listener.setJTextField(text); // Call the method of the PoliceListen class
		listener.setJTextArea(textArea);
		listener.setChatViewJf(jf);
		text.addActionListener(listener); // Add a listener to the text box
		button.addActionListener(listener); // button đc add listener

		jf.add(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set the role of the close icon in the upper right corner
		jf.setVisible(true); // set visible
	}

	// "Open file" call function
	void showFileOpenDialog(JFrame parent) {
		// Create a default file chooser
		JFileChooser fileChooser = new JFileChooser();
		// Set default displayed folders
		fileChooser.setCurrentDirectory(new File("D:/Code Java/multichat-system/file"));
		// Add available file filters (tham số đầu là description, thứ 2 là phần mở
		// rộng)
		// fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("(txt)",
		// "txt"));
		// set bộ lọc file (tham số đầu là mô tả, tiếp theo là phần mở rộng)
		fileChooser.setFileFilter(new FileNameExtensionFilter("(txt)", "txt"));
		// open cửa sổ chọn file (luồng bị block until select box đc đóng)
		int result = fileChooser.showOpenDialog(parent); // set vị trí hộp chọn file
		// click OK
		if (result == JFileChooser.APPROVE_OPTION) {
			// get đường dẫn
			File file = fileChooser.getSelectedFile();
			String path = file.getAbsolutePath();
			ClientFileThread.outFileToServer(path);
		}
	}
}
