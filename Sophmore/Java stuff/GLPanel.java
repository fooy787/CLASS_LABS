import java.awt.*;
import javax.swing.*;

public class GLPanel extends JPanel{
	public GLPanel(){
		
	}
	@Override
	public void paint(Graphics g) {
		System.out.println("Panel painted");
		
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, 100, 100)
	}
}