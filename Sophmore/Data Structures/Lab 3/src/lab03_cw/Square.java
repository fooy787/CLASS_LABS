package lab03_cw;

import org.newdawn.slick.*;

public class Square extends Shape {
    /**
     * Square class extends off of Shape. Takes our color and throws it back to shape. Also has a length and a width.
     */
    protected float mWidth, mHeight;
    public Square(Color c, float width, float height){
        super(c);
        mWidth = width;
        mHeight = height;
    }//End constructor

    public float getWidth(){
        /**
         * Returns the width.
         */
        return mWidth;
    }//End Get width

    public float getHeight(){
        /**
         * Returns the height.
         */
        return mHeight;
    }//End getHeight
}//End Square