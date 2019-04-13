package etec2101;

import java.util.ArrayList;

public class BinarySearchTree <E extends Comparable> {
    protected BSTNode mRoot;
    protected int mSize;
    public enum TraversalType{in_order, pre_order, post_order}
    ArrayList<E> tmpArrayList;

    /**
     * Creates a BinArySearchTree
     *
     */

    public void add(E val){
        /**
         * Takes a value, sets that value to the root if there is no root. If there is, passes it off to the add method to add as a leaf. Increments size.
         * @param <E>
         */
        if (mRoot == null){
            mRoot = new BSTNode(val);
        }//End If
        else{
            mRoot.add(val);
        }//End Else
        mSize++;
    }//End add

    public int getHeight(){
        /**
         * Returns height of the tree.
         * @return int
         */
        return mRoot.getHeight();
    }
    public int count(E val){
        /**
         * Returns the amount of times E is in the tree.
         * @param <E>
         * @return int
         */
        return mRoot.count(val);
    }

    public ArrayList<E>toArray(BSTNode tmpNode){
        /**
         * Takes a BSTNode of where to start. It then recursively goes through the tree and adds all the leaves to an ArrayList. Returns the ArrayList.
         * @param BSTNode
         * @return ArrayList
         */
        if (tmpNode.mLeft !=null){
            toArray(tmpNode.mLeft);
        }
        tmpArrayList.add((E)tmpNode.mData);
        if (tmpNode.mRight != null){
            toArray(tmpNode.mRight);
        }
        return tmpArrayList;
    }

    public void rebalance(){
        /**
         * Reblances the tree in an attempt to make all sides equal.
         */

        ArrayList tmpArray = this.toArray(mRoot);
        int length = tmpArray.size();
        Object tmpNode = tmpArray.get(length / 2);
        this.add((E)tmpNode);
        for(int i = 0; i < length / 2; i++){
            this.add((E)tmpArray.get(i));
        }
        for(int i = length + 1; i < length; i++){
            this.add((E)tmpArray.get(i));
        }
    }
    public BSTIterator iterator(TraversalType direction ){
        /**
         * Requires the traversal type in order to specify what way the iterator is sorting.
         * @param TraversalType
         * @return BSTIterator
         */

        return new BSTIterator(this.toArray(mRoot), direction);
    }

    @Override
    public String toString() {
        /**
         * Returns a string of the whole tree.
         * @return String
         */
        String tmpStr = "";
        BSTNode tmpNode = mRoot;
        for (int i = 0; i < mRoot.getHeight(); i++) {
            if (tmpNode.mLeft != null) {
                tmpStr += "/t";
                tmpNode = tmpNode.mLeft;
            }
            tmpStr += tmpNode.mData;
            if (tmpNode.mRight != null) {
                tmpStr += "/t";
                tmpNode = tmpNode.mRight;
            }
        }
        return (tmpStr);
    }


}//End Binary Search Tree