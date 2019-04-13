package lab03_cw;

public class Stack {
    /**
     * Stack class creates a stack. AKA first in last out.
     */
    LinkedList<Shape> LI = new LinkedList<>();
    public Stack(){
    }
    public void push(Shape toAdd){
        /**
         * Adds an item to the front of the stack, requires a shape to add.
         */
        LI.addToBegin(toAdd);
    }
    public void pop(){
        /**
         * Removes an item from the front of the stack
         */
        LinkedList.LinkedListIterator LLI = LI.iterator();
        LLI.remove();
    }
    public Shape peek(){
        /**
         * Returns the first item in the stack.
         */
        return LI.mBegin.mValue;
    }
    public int returnLength(){
        /**
         * Returns the size of the stack.
         */
        return LI.mSize;
    }
}//End Stack
