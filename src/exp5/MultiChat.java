package exp5;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class MultiChat {
	JTextArea textArea;

	// set vùng nhập liệu
	void setTextArea(String str) {
		textArea.append(str + '\n');
		textArea.setCaretPosition(textArea.getDocument().getLength()); // set thanh cuộn
	}

	// Khoi tao form
	public MultiChat() {
		init();
	}

	void init() {
		JFrame jf = new JFrame("My Multichat");
		jf.setBounds(500, 100, 450, 500); // Set coordinates cua cua so and size
		jf.setResizable(false); // not allowed to resize the cua so

		JPanel jp = new JPanel(); // new panel
		JLabel lable = new JLabel("My Multichat's SERVER SIDE");
		textArea = new JTextArea(23, 38); // new text area, set chieu dai va chieu rong
		textArea.setEditable(false); // vùng textarea của server chỉ để hiển thị, không dùng để nhập liệu
		JScrollPane scroll = new JScrollPane(textArea); // set panel scroll duoc cho textArea
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // show thanh cuon doc
		jp.add(lable);
		jp.add(scroll);

		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		jf.add(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close cua so khi nhan nut Close
		jf.setVisible(true); // set visible
	}
}
