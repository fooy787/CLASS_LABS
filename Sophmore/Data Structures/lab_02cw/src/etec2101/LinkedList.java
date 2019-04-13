package etec2101;

import java.util.Iterator;

import static java.lang.Math.abs;

public class LinkedList<E>{
    protected Node mBegin;
    protected Node mEnd;
    protected int mSize = 0;

    protected class Node{
        Node mNext;
        Node mPrev;
        E mValue;

        public Node(E value) {
            mValue = value;
            mNext = null;
            mPrev = null;
        }//End Constructor
    }//End Node Class

    public void addToEnd(E Value){
        Node newN = new Node(Value);
        if(mSize != 0){
            mEnd.mNext = newN;
            newN.mPrev = mEnd;
            mEnd = newN;
        }// End If
        else {
            mBegin = mEnd = newN;
        }//End Else
        mSize ++;
    }//End AddToEnd

    public void addToBeg(E Value){
        Node newN = new Node(Value);
        if(mSize != 0){
            mBegin.mPrev = newN;
            newN.mNext = mBegin;
            mBegin = newN;

        }// End if
        else{
            mBegin = mEnd = newN;
        }// End Else
        mSize++;
    } //End at to start
    public int length(){
        return mSize;
    }//End length

    @Override
    public String toString(){
        String s = "<";
        Node cur = mBegin;
        if(mSize !=0){
            while(cur!= null){
                s+="["+cur.mValue.toString()+"]";
                cur = cur.mNext;
            }//End for
        }//End if
        else{
            s+="empty";
        }//End Else
        s+=">";
        return s;
    }//End toString

    public E at(int Position, boolean Direction){
        int i = 0;
        if(abs(Position) > mSize) {
            throw new IndexOutOfBoundsException();
        }//End If
        if(Direction == true) {
            Node cur = mBegin;
            while (cur != null && i < Position) {
                cur = cur.mNext;
                i++;
            }//End while
            return cur.mValue;
            //End Position If
        }//End Direction if
        else{
            Node cur = mEnd;
            while (cur != null && i < Position){
                cur = cur.mPrev;
                i++;
            }//End While
            return cur.mValue;
        }//End Else
    }//End "at" with direction specified

    public E at(int Position){
        if(abs(Position) > mSize){
            throw new IndexOutOfBoundsException();
        }//End If
        Node cur = mBegin;
        int i = 0;
        while (cur != null && i < Position){
            cur = cur.mNext;
            i++;
        }//End while
        return cur.mValue;
        //End If
    }//End "at" without direction

    public void insert(int Position, E Data){
        Node newN = new Node(Data);
        int i = 0;
        Node cur = mBegin;
        if(Position==0){
            this.addToBeg(Data);
        }//End Position 0 If
        else if(Position == mSize){
            this.addToEnd(Data);
        }//End Else if
        else {
            //End Last position If
            while (i < Position) {
                cur = cur.mNext;
                i++;
            }//End While
            if (i == Position) {
                newN.mNext = cur;
                newN.mPrev = cur.mPrev;
                cur.mPrev.mNext = newN;
                cur.mPrev = newN;
                mSize++;
            }//End If
        } //End Else
    }//End insert

    public int removeAll(E type){
        Node cur = mBegin;
        int removed = 0;
        while(cur != null){
            if(cur.mValue.equals(type)){
                if(cur.equals(mBegin)){
                    mBegin = cur.mNext;
                    cur.mNext.mPrev = null;
                }//End If begin
                else if(cur.equals(mEnd)){
                    mEnd = cur.mPrev;
                    cur.mPrev.mNext = null;
                }//End if end
                else {
                    cur.mPrev.mNext = cur.mNext;
                    cur.mNext.mPrev = cur.mPrev;
                }//End Else
                removed++;
                mSize--;
            }//End If
            cur = cur.mNext;
        }//End While
        return removed;
    }//End Remove All

    public int count(E type){
        Node cur = mBegin;
        int currentAmount = 0;
        while(cur != null){
            if(cur.mValue.equals(type)){
                currentAmount++;
            }//End If
            cur = cur.mNext;
        }//End While
        return currentAmount;
    }//End count

    public class LinkedListIterator implements Iterator{
        private LinkedListIterator(LinkedList<E> list){
        }
        Node cur = mBegin;
        Node toRemove;
        Node nextVal;
        @Override
        public boolean hasNext(){
            Boolean val;
            if(cur != null){
                val = true;
            }//End if
            else{
                val = false;
            }//End else
            return val;
        }//End hasNext
        @Override
        public E next(){
            nextVal = cur;
            toRemove = nextVal;
            cur=cur.mNext;
            return nextVal.mValue;
        }//End Next

        @Override
        public void remove() throws NullPointerException{
            while(toRemove != null && !toRemove.mValue.equals(nextVal)){
                toRemove = toRemove.mNext;
            }//End while
            if (toRemove == null){ //item not found
                return;}
            else{
                if (mBegin.equals(toRemove) && mEnd.equals(toRemove)){
                    mBegin = mEnd = null;
                }//End single item
                else if(mBegin.equals(toRemove)){
                    mBegin.mNext.mPrev = null;
                    mBegin = mBegin.mNext;
                }//End Begin == Cur
                else if(mEnd.equals(toRemove)){
                    mEnd.mPrev.mNext = null;
                    mEnd = mEnd.mPrev;
                }//End End == Cur
                else{
                    toRemove.mNext.mPrev = toRemove.mPrev;
                    toRemove.mPrev.mNext = toRemove.mNext;
                }//End else
            }//End else
            mSize--;
        }//End remove
    }//End Linked List Iterator
    public LinkedListIterator iterator(){
        LinkedListIterator Returning = new LinkedListIterator(this);
        return Returning;
    }
}//End Linked List