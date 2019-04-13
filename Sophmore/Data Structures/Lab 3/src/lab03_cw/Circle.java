package lab03_cw;

import org.newdawn.slick.*;

public class Circle extends Shape {
    /**
     * Circle class extends off of shape. It will take our color and throw it up to shape. It also has a radius.
     */
    protected float mRad;
    public Circle(Color c, float r){
        super(c);
        mRad = r;
    }//End Circle
    public float getRadius(){
        return mRad;
    }//End Get Radius
}//End Circle