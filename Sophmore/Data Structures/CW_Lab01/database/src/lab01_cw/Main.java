package lab01_cw;

//As I continued working, the Database got a bunch of red errors under neath them so hopefully you can at least understand my logic.
public class Main {
    public static void main(String[] args){
        //Opens the database
        Database data = new Database("dbase.txt");
        //Adds a Transaction given everything
        data.addTransaction(13, 2394, 3485.65f, "With Notes");
        //Adds a new transaction without Notes
        data.addTransaction(14, 4567, 3948.73f);
        //Adds a new Transaction without both notes and ID
        data.addTransaction(2456, 2983.65f);
        //Adds a new Transaction without an ID
        data.addTransaction(3424, 34285f, "Without ID");
        //Sorts by ID
        data.sortTransaction(1);
        //Sorts by Account #
        data.sortTransaction(2);
        //Sorts by Amount
        data.sortTransaction(3);
        //Sorts by note length
        data.sortTransaction(4);
        //Removes a Transaction from the Database
        data.removeTransaction(5);
        //Saves the database
        data.saveDBase();



    }
}
