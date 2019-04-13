package lab01_cw;

import java.io.*;

import lab01_cw.*;

public class Database {
    protected Transaction mTransactions[];
    public int mSize = Transaction.count;
    protected String mFileName = "dbase.txt";
    protected int mMax = 20;

    static int mSizeIncrement = 20;

    public Database(String FileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(mFileName))) {
            //Starts BufferedReader
            while (br.ready()) {
                String line = br.readLine();
                //reads a line
                line = line.trim();
                //trims
                line = line.substring(1, line.length() - 1);
                String elem[] = line.split(":");
                int first = Integer.parseInt(elem[0]);
                elem[2] = elem[2].substring(1, elem[2].length());
                int second = Integer.parseInt(elem[1]);
                float third = Float.parseFloat(elem[2]);
                //Will get a transaction ready to be added
                try {
                    Transaction New = new Transaction(first, second, third, elem[3]);
                    mTransactions[first] = New;
                    mSize += 1;
                    //Tries to make a transaction
                }
                catch (ArrayIndexOutOfBoundsException g){
                    String notes = " ";
                    Transaction New = new Transaction(first,second,third, notes);
                    mTransactions[first] = New;
                    mSize += 1;
                    //In case there are no notes
                }


            }
        }
        catch(java.io.IOException e){
            File File = new File (mFileName);
        }
    }

    public void sSizeIncrement() {
        //increases the array if need be copying over any of the past transactions.
        int tempSize = mSize + mSizeIncrement;
        Transaction[] tempArray = new Transaction[tempSize];
        if (mSize >= mMax) {
            System.arraycopy(mTransactions, 0, tempArray,0);
            mMax += mSizeIncrement;

        }
    }


    public void addTransaction(int Account, float Amount) {
        //picks up if you don't put any an ID or notes
        String tempString = new String(" ");
        int tempID = mSize + 1;
        try {
            mTransactions[mSize] = new Transaction(tempID, Account, Amount, tempString);
        }
        catch (ArrayIndexOutOfBoundsException f){
            this.sSizeIncrement();
        }

    }

    public void addTransaction(int ID, int Account, float Amount, String Notes) {
        //If you put in everything this one gets activated
        try {
            mTransactions[mSize] = new Transaction(ID, Account, Amount, Notes);
        }
        catch (ArrayIndexOutOfBoundsException f){
            this.sSizeIncrement();
        }
    }

    public void addTransaction(int ID, int Account, float Amount) {
        //Picks up if you dont include any notes
        String tempString = new String(" ");
        try {
            mTransactions[mSize] = new Transaction(ID, Account, Amount, tempString);
        }
        catch (ArrayIndexOutOfBoundsException f){
            this.sSizeIncrement();
        }
    }

    public void addTransaction(int Account, float Amount, String Notes) {
        //Picks up if you dont include an ID
        try {
            mTransactions[mSize] = new Transaction(Account, Amount, Notes);
        }
        catch (ArrayIndexOutOfBoundsException f){
            this.sSizeIncrement();
        }

    }

    public void sortTransaction(int Sort) {
        //Sorts the transactions by a given integer
        Boolean sorted = false;
        if(Sort == 1){
            while(!sorted){
                sorted = true;
                for(int i = 0; i < mSize; i++){
                    //Call compare and pass in mTransactions[i] and SortBy
                    //if False create tempTransaction and set it = to mTransactions[i]
                    if(mTransactions[i].compare(mTransactions[i+1], Sort) == false){
                        Transaction tempTransaction = mTransactions[i];
                        mTransactions[i] = mTransactions[i+1];
                        mTransactions[i+1] = tempTransaction;
                    }
                    sorted = false;
                }
            }
        }
        if(Sort == 2){
            while(!sorted){
                sorted = true;
                for(int i = 0; i < mSize; i++){
                    //Call compare and pass in mTransactions[i] and SortBy
                    //if False create tempTransaction and set it = to mTransactions[i]
                    if(mTransactions[i].compare(mTransactions[i+1], Sort) == false){
                        Transaction tempTransaction = mTransactions[i];
                        mTransactions[i] = mTransactions[i+1];
                        mTransactions[i+1] = tempTransaction;
                    }
                    sorted = false;
                }
            }
        } if(Sort == 3){
            while(!sorted){
                sorted = true;
                for(int i = 0; i < mSize; i++){
                    //Call compare and pass in mTransactions[i] and SortBy
                    //if False create tempTransaction and set it = to mTransactions[i]
                    if(mTransactions[i].compare(mTransactions[i+1], Sort) == false){
                        Transaction tempTransaction = mTransactions[i];
                        mTransactions[i] = mTransactions[i+1];
                        mTransactions[i+1] = tempTransaction;
                    }
                    sorted = false;
                }
            }
        } if(Sort == 4){
            while(!sorted){
                sorted = true;
                for(int i = 0; i < mSize; i++){
                    //Call compare and pass in mTransactions[i] and SortBy
                    //if False create tempTransaction and set it = to mTransactions[i]
                    if(mTransactions[i].compare(mTransactions[i+1], Sort) == false){
                        Transaction tempTransaction = mTransactions[i];
                        mTransactions[i] = mTransactions[i+1];
                        mTransactions[i+1] = tempTransaction;
                    }
                    sorted = false;
                }
            }
        }
    }


    public void removeTransaction(int mID) {
        //removes a transaction
        try {

            mTransactions[mID] = null; //Sets the value to null
            int i = mID;
            while (i < mSize) {
                //moves the transactions down
                mTransactions[i] = mTransactions[i + 1];
                //Object temp = mTransactions[i + 1];
                //mTransactions[i + 1] = null;
                //this.addTransaction(i, temp[1], temp[2], temp[3]);
                i += 1;
            }
            mSize -= 1;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Could not remove Transaction because it doesn't exist");}}


    public void saveDBase() {
//        java.io.File f = new java.io.File(mFileName);
//        if(f.exists()) {
//            PrintWriter toWrite = new PrintWriter("dbase.txt");
//            for (int i = 0; i < mTransactions.length; i++)
//                toWrite.printLn(toString(mTransactions));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(mFileName))) {
            for (int i = 0; i < mSize; i++) {
                bw.write(toString(mTransactions[i]) + "\n");
                //makes a loop that writes each transaction to the file with a new line in between each one then closes it
                bw.close();
            }
        } catch (java.io.IOException e) {
            File newFile = new File(mFileName);
            //Catches if no file exists and creates one
        }
    }
}