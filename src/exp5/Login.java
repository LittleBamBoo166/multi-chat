package exp5;

import java.awt.*;
import javax.swing.*;

public class Login {
	JTextField textField = null;
	JPasswordField pwdField = null;
	ClientReadAndPrint.LoginListen listener = null;

	// constructor giao dien dang nhap
	public Login() {
		init();
	}

	// Khởi tạo giao diện người dùng
	void init() {
		JFrame jf = new JFrame("Login");
		jf.setBounds(500, 250, 310, 210); // set toạ độ và size của cửa sổ login
		jf.setResizable(false); // not allowed resize the giao dien
		
		// tạo j pà neo
		JPanel jp1 = new JPanel();
		JLabel headJLabel = new JLabel("Login");
		headJLabel.setFont(new Font(null, 0, 35)); // set the font type, style and size of the text
		// thêm j label headJLabel vô j pà neo jp1
		jp1.add(headJLabel);

		// tạo thêm 1 cái j pà neo nữa
		JPanel jp2 = new JPanel();
		JLabel nameJLabel = new JLabel("Username"); // label Username
		textField = new JTextField(20); // text field để điền username
		JLabel pwdJLabel = new JLabel("Password"); // label password
		pwdField = new JPasswordField(20); // password field
		JButton loginButton = new JButton("Log in"); // nút login
		// thêm tất cả vô j pà neo jp2
		jp2.add(nameJLabel);
		jp2.add(textField);
		jp2.add(pwdJLabel);
		jp2.add(pwdField);
		jp2.add(loginButton);
		
		// này là jpanel parent
		JPanel jp = new JPanel(new BorderLayout());
		jp.add(jp1, BorderLayout.NORTH); // thêm jp1 vào jp
		jp.add(jp2, BorderLayout.CENTER); // thêm jp2 vào jp (ở giữa)

		// set new loginlistener
		listener = new ClientReadAndPrint().new LoginListen(); // create a new listener class
		listener.setJTextField(textField); // call method from listener class
		listener.setJPasswordField(pwdField);
		listener.setJFrame(jf);
		pwdField.addActionListener(listener); // add action listener to trường nhập mật khẩu
		loginButton.addActionListener(listener); // button to add listener

		jf.add(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // setup for close button
		jf.setVisible(true); // set visible
	}
}
