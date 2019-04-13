package Lab06_cw;

import java.util.logging.*;
import org.newdawn.slick.*;
public class MainProgram extends BasicGame
{
    DrawableGraph DG;
    public MainProgram(String gamename)
    {
        super(gamename);

    }
    @Override
    public void init(GameContainer gc) throws SlickException {
        DG = new DrawableGraph("maps/map04.txt");
        float scaleX = DG.toScaleX(1360);
        float scaleY = DG.toScaleY(750);
        DG.setVals(scaleX, scaleY);
    }
    @Override
    public void update(GameContainer gc, int i) throws SlickException {}
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException
    {
        g.setBackground(Color.green);
        DG.drawAll(g);
    }
    public static void main(String[] args)
    {
        try
        {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new MainProgram("Simple Slick Game"));
            appgc.setDisplayMode(1366, 760, false);
            appgc.start();
        }
        catch (SlickException ex)
        {
            Logger.getLogger(MainProgram.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}