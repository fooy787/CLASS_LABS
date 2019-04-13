package etec2101;

class BSTNode<E extends Comparable> {
    E mData;
    BSTNode mLeft;
    BSTNode mRight;
    BSTNode mParent;
    int startValRight, startValLeft = 1;
    int mAmount = 0;

    public BSTNode(E val){
        /**
         * Takes a value and sets the mData value to E.
         * @param <E>
         */
        mData = val;
    }

    public void add(E val){
        /**
         * Adds a new node as the left or right node based on a compare method.
         * @param <E>
         */
        if(mData.compareTo(val) > 0){
            if(mLeft == null){
                mLeft = new BSTNode(val);
                mLeft.mParent = this;
            }//End if null
            else if(mRight == null){
                mRight = new BSTNode(val);
                mRight.mParent = this;
            }//End Else
        }//End if
        else{
            mLeft.add(val);
            mLeft.mParent = this;
        }//End Else
    }//End Add

    public int count(E val){
        /**
         * Counts all the instances of E
         * @param <E>
         * @return int
         */

        if(this.mData == val){
            mAmount++;
        }
        else if(this.mLeft != null){
            count(val);
        }
        else if(this.mRight != null){
            count(val);
        }
        return mAmount;
    }
    public int getHeight(){

        /**
         * Returns the height of the tree.
         * @return int
         */

        if(mRight != null){
            startValRight++;
            mRight.getHeight();
        }//End if Right not null
        else if(mLeft != null){
            startValLeft++;
            mLeft.getHeight();
        }//End if left not null
        if (startValLeft > startValRight) {
            return startValLeft;
        }//End if Left is greater than right
        return startValRight;
    }//End getHeight
}//End BSTNode