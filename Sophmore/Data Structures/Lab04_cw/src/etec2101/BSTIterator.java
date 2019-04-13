package etec2101;

import java.util.ArrayList;
import java.util.Iterator;


public class BSTIterator <E extends Comparable> implements Iterator {

    /**
     * Creates an iterator that can iterate through BSTNodes.
     * @param <E>
     */
    ArrayList<E> mValues;
    int mCur;

    protected BSTIterator(ArrayList AL, BinarySearchTree.TraversalType direction){
        if(direction == BinarySearchTree.TraversalType.in_order){
            mValues = new ArrayList<>(AL);
        }

    }//End constructor

    @Override
    public boolean hasNext(){
        /**
         * Returns if the ArrayList has a next value or not.
         * @return Boolean
         */
        return mCur < mValues.size();
    }//End has next

    @Override
    public E next(){
        /**
         * Returns the next value of the iterator.
         * @return <E>
         */
        return mValues.get(mCur++);
    }//End next
}//End BSTIterator