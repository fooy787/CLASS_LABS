import java.util.Iterator;
import static java.lang.Math.abs;

/**
 * A simple Hashmap class that requires the user to specify a what type of key and what type of value they will be using.
 * @author  Chase Williams
 * @param <K> The given Key type
 * @param <V> The given Value type
 */
public class HashMap<K, V> {

    protected int initial_capacity;
    protected int resize_capacity;
    protected float load_factor;
    protected int curSize = 0;
    protected enum IteratorType{KEYS, VALUES}
    protected Node[] hashArray;

    /**
     * Sets up a basic HashMap using the values that the user passes in.
     * @param Capacity The initial capacity of the HashMap
     * @param Load The load (between 0.0 - 1). Once it reaches this value the hashmap will resize.
     * @param ResizeBy How much to resize by.
     */
    public HashMap(int Capacity, float Load, int ResizeBy){
        this.initial_capacity = Capacity;
        this.resize_capacity = ResizeBy;
        this.load_factor = Load;
        this.hashArray = new Node[initial_capacity];
    }//End constructor

    /**
     * Sets a key to a given value, if the key already exists, changes the value.
     * @param key What key the user wants to set or change.
     * @param value The value that the key has.
     */
    public void set(K key, V value){
        int index = abs(key.hashCode() % initial_capacity);
        if (hashArray[index] == null){
            hashArray[index] = new Node<>(key, value);
            curSize++;
        }//End if null

        else if(hashArray[index].keyNumber == key){
            hashArray[index].valueNumber = value;
        }//End if it already exists and just needs changed.

        else if (hashArray[index] != null && hashArray[index].keyNumber != key){
            while(hashArray[index] != null){
                if(index == initial_capacity - 1){
                    index = 0;
                }//End if it reaches end of array
                index++;
            }//End while not null
            hashArray[index] = new Node<>(key, value);
            curSize++;
        }//End if we need to increment the index

        if(curSize / hashArray.length >= load_factor ){
            initial_capacity += resize_capacity;
            Node[] tmpArray = new Node[initial_capacity];
            for(int i = 0; i < hashArray.length; i++){
                if(hashArray != null){
                    int tmpIndex = abs(hashArray[i].getKeyNumber().hashCode() % initial_capacity);
                    tmpArray[tmpIndex] = hashArray[i];
                }//End if not null
            }//End finding the Nodes
            hashArray = tmpArray;
        }//End resize and rehash

    }//End void

    /**
     * How many slots are currently being used.
     * @return An integer based on how many slots are being used.
     */
    public int getSize(){
        return curSize;
    }//Return getSize

    /**
     * Gets the value of that specific key if it exists.
     * @param key The key that the user wants to get the value of.
     * @return The value of the key or null;
     */
    public V get(K key){
        int index = key.hashCode() % initial_capacity;
        if(hashArray[index] == null){
            return null;
        }//Return if null
        return (V)hashArray[index].getValue();
    }//End get

    /**
     * Sets up a basic iterator of the HashMap
     * @param <K> The key type.
     * @param <V> The value type.
     */
    public class HashMapIterator<K, V> implements Iterator {
        int curIndex = 0;
        Object returnable;
        HashMap.IteratorType IT;

        /**
         * Constructor for the HashMap Iterator.
         * @param t the traversal type, either KEYS or VALUES.
         * @return An iterator that will iterate through type t
         */
        public HashMapIterator HashMapIterator(IteratorType t){
            IT = t;
            HashMapIterator HMI = new HashMapIterator();
            return HMI;
        }//End Constructor

        /**
         * Returns whether or not the user has reached the end of the HashMap.
         * @return True or False depending on if the iterator is at the end of the HashMap
         */
        @Override
        public boolean hasNext() {
            for (;curIndex  < hashArray.length; curIndex++) {
                return true;
            }//End for loop
            return false;
        }//End has next

        /**
         * Gets the next taken slot in the HashMap. Skips over nulls.
         * @return Returns the next either key or value in the HashMap.
         */
        @Override
        public Object next() {

            if(hashArray[curIndex] != null) {
                if (IT == IteratorType.KEYS) {
                    returnable = hashArray[curIndex].keyNumber;
                }//End if Keys
                if (IT == IteratorType.VALUES){
                    returnable = hashArray[curIndex].valueNumber;
                }
            }//End if not null
            else if(hashArray[curIndex] == null){
                curIndex++;
            }//End if null
            return returnable;
        }//End Next

    }//End HashMapIterator

    /**
     * Removes a given set from the HashMap.
     * @param key The key to remove.
     */
    public void remove(K key){
        int index = key.hashCode() % initial_capacity;
        hashArray[index].keyNumber = null;
        hashArray[index].valueNumber = null;
        hashArray[index] = null;
        curSize--;
    }//End Remove

    /**
     * Overrides the string method to print the HashMap in a usable String form.
     * @return Returns a string of the hashmap.
     */
    @Override
    public String toString(){
        String s = "{";
        int cur = 0;
        curLoop:
        for(int i = 0; i < hashArray.length; i ++){
            if( hashArray[i] != null){
                cur++;
                s += hashArray[i].getKeyNumber() + ":" + hashArray[i].getValue();
                if(cur == this.getSize()){
                    break curLoop;
                }//End Break
                s+= ", ";
            }//End if not null
        }//End for loop
        s += "}";
        return s;
    }//End toString
}//End Hashmap