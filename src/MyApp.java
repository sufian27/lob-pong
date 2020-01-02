import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MyApp extends JFrame {
	public MyApp() {
		setSize(750, 750);
		setTitle("Lob Pong");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new GameCanvas());
	}
	public static void main(String[] args) {
		new MyApp().setVisible(true);
	}

}
