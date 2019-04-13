import java.util.ArrayList;

public class mainProgram {
    public static void main(String[] args){
        ArrayList tester = new ArrayList(5);
        for(int i = 0; i <= 5; i++){
            tester.add(i);
        }
        BinaryHeap<Integer> BH = new BinaryHeap(true, tester);
        System.out.println(BH);
        BH.push(3);
        System.out.println(BH);
        BH.push(2);
        System.out.println(BH);
        BH.push(0);
        System.out.println(BH);
        WordCounter SC = new WordCounter("Documents/Document.txt");
        System.out.println(SC.BruteForceMethod(5));
    }
}
