import javax.swing.JFrame;

public class MyApp extends JFrame {
	
	private static final long serialVersionUID = 1L;

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
