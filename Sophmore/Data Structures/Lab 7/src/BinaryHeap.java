import java.util.*;

/**
 * A BinaryHeap class. Has two different constructors, one blank one that creates an empty heap of size 10. And the other that
 * gets passed in a collection, and creates a heap based off the collection.
 *
 * @author Chase Williams
 * @param <E> The type of Binary Heap
 */
public class BinaryHeap<E extends Comparable> {
    private Boolean type;//If type is true = maxHeap, else it is false and minHeap
    private Object[] internalStorage = new Object[5];
    private int mSize = 0;

    public BinaryHeap(Boolean minMaxHeap){
        internalStorage = new Object[10];
        type = minMaxHeap;
        mSize = 10;
    }//End "empty" constructor

    public BinaryHeap(Boolean minMaxHeap, Collection<E> collection){
        type = minMaxHeap;
        for (Object cur : collection) {
            if (mSize >= internalStorage.length) {
                internalStorage = resize(internalStorage);
            }//End if needing to resize
            internalStorage[mSize] = cur;
            mSize++;
        }//End iterator
    }//End non empty constructor

    @Override
    public String toString(){
        String s = "{";
        for(Object cur : internalStorage){
            s+= cur+", ";
        }
        s+="}";
        return s;
    }

    private Object[] resize(Object[] toResize){
        Object[] newArray = new Object[toResize.length + 1];
        System.arraycopy(toResize, 0, newArray, 0, toResize.length);
        return newArray;
    }//End Resize

    /**
     * Pushes new value on to the heap
     *
     * @param val the value to be pushed onto the heap
     */
    protected void push(E val){
        internalStorage = resize(internalStorage);
        if (type){
            int index = mSize;
            internalStorage[mSize] = val;
            int parent = index >> 1;
            E indexVar = (E)internalStorage[index];
            E parentVar = (E)internalStorage[parent];
            while (index >= 1 && (indexVar.compareTo(parentVar) > 0)){
                Object prevVal = internalStorage[parent];
                internalStorage[parent] = internalStorage[index];
                internalStorage[index] = prevVal;
                index = parent;
                parent = index >> 1;
            }//End while loop
        }//End if maxHeap
        else{
            int index = internalStorage.length;
            internalStorage[mSize] = val;
            int parent = index >> 1;
            E indexVar = (E)internalStorage[index];
            E parentVar = (E)internalStorage[parent];
            System.out.println(indexVar);
            while (index >= 1 && (indexVar.compareTo(parentVar) < 0)){
                Object prevVal = internalStorage[parent];
                internalStorage[parent] = internalStorage[index];
                internalStorage[index] = prevVal;
                index = parent;
                parent = index >> 1;
            }//End while loop
        }//End if minHeap
    }//End push

    /**
     * Pops a value off the heap.
     *
     * @return Returns the popped off value.
     */
    public E pop(){
        Object preVal = internalStorage[0];
        internalStorage[0] = internalStorage[mSize - 1];
        internalStorage[mSize - 1] = preVal;
        Object val = internalStorage[mSize - 1];
        internalStorage[mSize - 1] = null;
        int index = 1;
        int parentIndex = index + 1;
        int leftChildIndex = parentIndex << 1;
        int rightChildIndex = leftChildIndex + 1;
        if (type) {
            while (((E) internalStorage[index]).compareTo((E) internalStorage[leftChildIndex]) <= 0 || ((E) internalStorage[index]).compareTo(internalStorage[rightChildIndex]) <= 0) {
                int largestVal;
                if (((E) internalStorage[rightChildIndex]).compareTo((E) internalStorage[leftChildIndex]) <= 0) {
                    largestVal = leftChildIndex;
                }//End if
                else {
                    largestVal = rightChildIndex;
                }//End Else
                E oldIndex = (E) internalStorage[index];
                internalStorage[index] = internalStorage[largestVal];
                internalStorage[largestVal] = oldIndex;

                index = largestVal;
            }//End while
        }//End if max
        if (!type) {
            while (((E) internalStorage[index]).compareTo((E) internalStorage[leftChildIndex]) <= 0 || ((E) internalStorage[index]).compareTo(internalStorage[rightChildIndex]) <= 0) {
                int smallestVal;
                if (((E) internalStorage[rightChildIndex]).compareTo((E) internalStorage[leftChildIndex]) >= 0) {
                    smallestVal = leftChildIndex;
                }//End if
                else {
                    smallestVal = rightChildIndex;
                }//End Else
                E oldIndex = (E) internalStorage[index];
                internalStorage[index] = internalStorage[smallestVal];
                internalStorage[smallestVal] = oldIndex;
                index = smallestVal;
            }//End while
        }//End if max
            return (E) val;
    }//End pop

    /**
     * Gets the 'size' of the  heap
     *
     * @return the number of things in the heap
     */
    protected int size(){
        return mSize;
    }//End size
}//End BinaryHeap