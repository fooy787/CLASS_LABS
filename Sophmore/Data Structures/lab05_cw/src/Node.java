/**
 * Sets up a node that can store both the key and the value.
 * @author Chase Williams
 * @param <K> The key type.
 * @param <V> The Value type.
 */
public class Node<K, V> {
    protected K keyNumber;
    protected V valueNumber;

    /**
     * Constructor for the node, places the key and value into a holder.
     * @param key The key the user wants to store.
     * @param value The value the user wants to store.
     */
    public Node(K key, V value){
        keyNumber = key;
        valueNumber = value;
    }//End constructor

    /**
     * Returns the value.
     * @return Value
     */
    public V getValue(){
        return valueNumber;
    }//End getValue

    /**
     * Returns the Key
     * @return Key
     */
    public K getKeyNumber(){
        return keyNumber;
    }//End getKeyNumber
}//End Node