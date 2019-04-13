import java.util.ArrayList;

/**
 * A basic hashset class that only requires the key.
 * @author Chase Williams
 * @param <E> The key or value type you want to store.
 */
public class HashSet<E> {
    protected int passCapacity;
    protected float passLoad;
    protected int resizeBy;
    HashMap<E, Integer> HM;

    /**
     * Initializes the HashSet to use these values
     * @param cap the current capacity of the Hashset.
     * @param load a decimal between 0 and 1 indicating at what percent we want to resize.
     * @param resize How many more slots the user wants to add when they resize.
     */
    public HashSet(int cap, float load, int resize){
        passCapacity = cap;
        passLoad = load;
        resizeBy = resize;
        HM =  new HashMap<>(passCapacity, passLoad, resizeBy);
    }//End Constructor

    /**
     * Add a value to the hashset.
     * @param val The actual value to add.
     */
    public void add(E val){
        HM.set(val, 1);
    }//End add

    /**
     * Converts the HashMap into a readable String.
     * @return Returns a string of HashMap
     */
    @Override
    public String toString(){
        String s = "{";
        int cur = 0;
        curLoop:
        for(int i = 0; i < HM.hashArray.length; i++){
            if(HM.hashArray[i] != null){
                cur++;
                s += HM.hashArray[i].getKeyNumber();
                if(cur == HM.getSize()){
                    break curLoop;
                }
                s += ", ";
            }//End if not null
        }//End for loop
        s+= "}";
        return s;
    }//End toString

    /**
     * A union of two Hashsets.
     * @param RHS The Hashset you want to compare to.
     * @return Contains all the values in both Hashsets and what they have in common. Only appearing once.
     */
    public String union(HashSet RHS){
        String s = "";
        ArrayList tm = new ArrayList();
        for (int i = 0; i < RHS.HM.hashArray.length; i++) {
            if (RHS.HM.hashArray[i] != null) {
                tm.add(RHS.HM.hashArray[i].getKeyNumber());
            }//End checking for null
        }//End adding in the first one
        for (int i = 0; i < this.HM.hashArray.length; i++) {
            if (this.HM.hashArray[i] != null && !tm.contains(this.HM.hashArray[i].getKeyNumber())) {
                tm.add(this.HM.hashArray[i].getKeyNumber());
            }//End checking for null
        }//End adding in the first one
        s+= tm.toString();
        return s;
    }//End union

    /**
     * An intersection of two hashsets. Only returns what the two hashsets have in common.
     * @param RHS The hashset to compare to.
     * @return Returns a string of the similarities between the hashsets.
     */
    public String intersection(HashSet RHS){
        String s ="{";
        int cur = 0;
        curBreak:
        for(int i = 0; i < this.HM.hashArray.length; i++){
            for(int q = 0; q < RHS.HM.hashArray.length; q++){
                if(this.HM.hashArray[i] != null && RHS.HM.hashArray[q] != null) {
                    if (this.HM.hashArray[i].keyNumber == RHS.HM.hashArray[q].keyNumber) {
                        cur++;
                        s+= HM.hashArray[i].getKeyNumber();
                        if(cur == this.HM.getSize() - 2){
                            break curBreak;
                        }
                        s += ", ";
                    }//End if they're the same
                }//End if not null
            }//End if it exists in RHS
        }//End if it's in LHS
        s+="}";
        return s;
    }//End intersection

    /**
     * Returns the relative difference of what the two hashsets, or the values that only appear on the right side.
     * @param RHS The hashset to compare to.
     * @return A string that shows the relative difference of the hashsets
     */
    public String relativeDifference(HashSet RHS) {
        String s = "";
        ArrayList tm = new ArrayList();
        for (int i = 0; i < RHS.HM.hashArray.length; i++) {
            if (RHS.HM.hashArray[i] != null) {
                tm.add(RHS.HM.hashArray[i].getKeyNumber());
            }//End checking for null
        }//End adding in the first one
        for (int i = 0; i < this.HM.hashArray.length; i++) {
            if (this.HM.hashArray[i] != null && tm.contains(this.HM.hashArray[i].getKeyNumber())) {
                tm.remove(this.HM.hashArray[i].getKeyNumber());
            }//End checking if it already exists
        }//End adding this array
        s += tm.toString();
        return s;
    }//End relative difference

    /**
     * Returns the symetric difference of the two hashsets. Or the values that only appear in each hashset and not in each others.
     * @param RHS The hashset to compare to.
     * @return A string of the symetric difference.
     */
    public String symetricDifference(HashSet RHS){
        String s = "";
        ArrayList tm = new ArrayList();
        for (int i = 0; i < RHS.HM.hashArray.length; i++) {
            if (RHS.HM.hashArray[i] != null) {
                tm.add(RHS.HM.hashArray[i].getKeyNumber());
                }//End checking for null
            }//End adding in the first one
        for (int i = 0; i < this.HM.hashArray.length; i++) {
            if (this.HM.hashArray[i] != null && !tm.contains(this.HM.hashArray[i].getKeyNumber())) {
                tm.add(this.HM.hashArray[i].getKeyNumber());
                }//End adding in this array.
            else if (this.HM.hashArray[i] != null && tm.contains(this.HM.hashArray[i].getKeyNumber())) {
                tm.remove(this.HM.hashArray[i].getKeyNumber());
            }//End checking if it already contains that number.
        }//End checking this array
            s += tm.toString();
        return s;
    }//End relative difference
}//End HashSet