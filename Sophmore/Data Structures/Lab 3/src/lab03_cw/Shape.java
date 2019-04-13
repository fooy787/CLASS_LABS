package lab03_cw;
import org.newdawn.slick.*;


public class Shape {
    /**
     * Basis for the shape class. Takes in a color and can also return a color.
     */
    protected Color mColor;
    public Shape(Color C){
        mColor = C;
    }//End Constructor

    public Color getColor(){
        return mColor;
    }//End getColor

    enum shapeType{CIRCLE, SQUARE}
    protected shapeType mType;
    protected int posX, posY;
}//End Shape