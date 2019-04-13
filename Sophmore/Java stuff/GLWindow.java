import java.awt.*;
import javax.swing.*;

public class GLWindow extends JFrame{
	public GLWindow() {
		super("Our awesome window");
		
		add(new GLPanel());
		setVisible(true);	
		setSize(400,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		GLWindow glWindow = new GLWindow();
	}
}