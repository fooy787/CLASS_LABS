package lab03_cw;

public class Queue {
    /**
     * Queue class creates a queue. AKA First in first out.
     */
    LinkedList<Shape> LI = new LinkedList();
    public Queue(){

    }
    public void push(Shape toPush){
        /**
         * Adds an item to the end of the queue. Requires a shape to push.
         */
        LI.addToEnd(toPush);
    }
    public void pop(){
        /**
         * removes an item from the queue.
         */
        LinkedList.LinkedListIterator LLI = LI.iterator();
        LLI.remove();
    }
    public Shape peek(){
        /**
         * Returns the first item from the queue.
         */
        return LI.mBegin.mValue;
    }
}//End Queue
