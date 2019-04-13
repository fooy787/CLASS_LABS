package lab03_cw;

import java.util.Random;
import java.util.logging.*;
import org.newdawn.slick.*;

public class MainProgram extends BasicGame  {
    /**
     * Main Program. Sets up our loop and initializes all the variables we need.
     */
    LinkedList<Shape> Shapes = new LinkedList<>();
    Stack undo;
    Stack redo;
    Random rc;
    Shape tempShape;
    Boolean kindofShape = true;
    Boolean done = false;
    Queue shapeList;
    int mousePosX;
    int mousePosY;
    int curWidth = 50;
    int curHeight = 50;
    float sensitivity = 0.01f;

    public MainProgram(String gamename)
    {
        /**
         * Starts the loop, takes a game name variable.
         */
        super(gamename);
    }
    @Override
    public void init(GameContainer gc) throws SlickException {
        /**
         * Initializes everything
         */
    }
    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        /**
         * registers our listeners and checks to see if the game is done.
         */
        gc.getInput().addMouseListener(this);
        gc.getInput().addKeyListener(this);
        if(done == true){
            gc.exit();
        }
    }
    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy){
        /**
         * Detects our mouse movement and stores it as integers.
         */
        mousePosX = newx;
        mousePosY = newy;
    }
    @Override
    public void mouseWheelMoved(int change){
        /**
         * Allows the user to dynamically change the size of the shape depending on what way the mousewheel is moved.
         */
        //Note: I tested this on trackpad so I made it feel smooth to me.
        if(curHeight >= 50){
            curHeight = 50;
        }
        if(curWidth >= 50){
            curWidth = 50;
        }
        if(curHeight <= 5){
            curHeight = 5;
        }
        if(curWidth <= 5){
            curWidth = 5;
        }
        curHeight += change * sensitivity;
        curWidth += change * sensitivity;

    }
    @Override
    public void keyPressed(int key, char C){
        /**
         * Detects which key(s) are pressed then executes commands based on those keys.
         */
        if (key == Input.KEY_ESCAPE){
            done = true;
        }//End Escape
        if (key == Input.KEY_Z && key == Input.KEY_LCONTROL){
            redo.push(undo.peek());
            LinkedList.LinkedListIterator LLI = Shapes.iterator();
            LLI.remove();

        }//End CtrlZ
        if (key == Input.KEY_LCONTROL && key == Input.KEY_Y){
            Shapes.addToEnd(undo.peek());
            undo.pop();
        }//End CtrlY
        if (key == Input.KEY_SPACE){
            if(kindofShape == false){
                kindofShape = true;
            }//End if
            else{
                kindofShape = false;
            }//End Else
        }//End space


    }//End keyPressed
    @Override
    public void mousePressed(int button, int x, int y){
        /**
         * Detects if the first mouse button is pressed and if so, creates a new shape with a random color.
         */
        if(button == 0){
            tempShape.posX = x;
            tempShape.posY = y;
            int rc1 = rc.nextInt(255);
            int rc2 = rc.nextInt(255);
            int rc3 = rc.nextInt(255);
            tempShape.mColor= (new Color(rc1,rc2,rc3)); //Creates a new random color
            if(kindofShape == false){
                //Creates a new circle with radius half of our width.
                tempShape.mType = Shape.shapeType.CIRCLE;
                Circle circleShape = (Circle)tempShape;
                circleShape.mRad = curWidth / 2;
                Shapes.addToBegin(circleShape);
            }//End if
            else{
                //Creates a new Square passing in the current height and width.
                tempShape.mType = Shape.shapeType.SQUARE;
                Square squareShape = (Square)tempShape;
                squareShape.mHeight = curHeight;
                squareShape.mWidth = curWidth;
                Shapes.addToBegin(squareShape);
            }//End Else
            for(int i = 0; i < undo.returnLength(); i++){
                undo.pop();
            }//End popping undo
        }//End Mouse button 0
    }//End mousePressed
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException
    {
        /**
         * Gets the current shape that the user has selected. It then draws an outline of that shape around the mouse and
         * all of the shapes in their correct positions.
         */
        if(kindofShape == true) {
            g.setColor(new Color(255, 255, 255));
            g.drawRect(mousePosX - curWidth / 2, mousePosY - curHeight / 2, curWidth, curHeight);
        }//End Square check
        else{
           g.setColor(new Color(255, 255, 255));
           g.drawOval(mousePosX - curWidth / 2, mousePosY - curHeight / 2, curWidth, curHeight);
        }//End Else
        LinkedList.LinkedListIterator LLI = Shapes.iterator();
        while (LLI.hasNext()) {
            LLI.next();
        }//End while
    }//End render
    public static void main(String[] args)
    {
        /**
         * The main program, launches the application and runs everything.
         */
        try
        {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new MainProgram("Jason senpai give me an a for effort"));
            appgc.setDisplayMode(640, 480, false);
            appgc.start();
        }
        catch (SlickException ex)
        {
            Logger.getLogger(MainProgram.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//End main
}//End Main Program
