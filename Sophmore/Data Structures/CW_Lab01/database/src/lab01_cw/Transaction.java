package lab01_cw;

public class Transaction {
    protected int mID;
    protected int mAccount;
    protected float mAmount;
    protected String mNotes;
    protected static int count = 0;

    public Transaction (int ID, int Account, float Amount, String Notes){
        //Creates a new Transaction
        mID = ID;
        mAccount = Account;
        mAmount = Amount;
        mNotes = Notes;
        count++;
    }

    public Transaction (int Account, float Amount, String Notes){
        //Creates a new transaction without having the ID
        count++;
        mID = count;
        mAccount = Account;
        mAmount = Amount;
        mNotes = Notes;
    }

    public Boolean compare (Transaction Comparer, int Field) {
        int number = Field;

        if (number == 1) {
            //Compares the MIDs
            return Comparer.mID < this.mID;
        }
        else if (number == 2) {
            //Compares the Accounts
            return Comparer.mAccount < this.mAccount;
        }
        else if (number == 3) {
            //Compares the Amount
            return Comparer.mAmount < this.mAmount;
        }
        else if (number == 4) {
            //Compares the note length
            return Comparer.mNotes.length() < this.mNotes.length();
        }
        return false;
    }
    @Override
    public String toString(){
        return "["+mID+":"+mAccount+":$"+mAmount+":"+mNotes+"}";
    }
}


