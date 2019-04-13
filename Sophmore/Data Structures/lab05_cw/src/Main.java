public class Main {
    public static void main(String[] args){
        HashMap<String, Integer> x = new HashMap<>(100, 0.7f, 150);

        x.set("bob", 7);
        x.set("sally", 9);
        x.set("George", 10);
        x.set("John", 2);
        x.set("dan", 1);
        x.set("Greg", 3);
        x.set("michelle", 4);
        x.set("simon", 6);
        x.set("peter", 14);
        x.set("Roberto", 5);
        x.set("sally", 20);
        x.set("George", 29);
        x.set("bob", 12);
        x.set("sally", 98);
        x.set("George", 76);

        System.out.println(x.getSize());
        System.out.println(x.get("bob"));

        x.remove("bob");
        System.out.println(x.toString());

        HashSet<String> y = new HashSet<>(100, 0.7f, 150);
        y.add("bob");
        y.add("Sue");
        y.add("joe");
        y.add("alan");
        y.add("george");
        System.out.println(y);

        HashSet<String> z = new HashSet<>(100, 0.7f, 150);
        z.add("bob");
        z.add("Sue");
        z.add("joe");
        z.add("jill");
        z.add("jack");
        System.out.println(z.toString());
        System.out.println(y.intersection(z) + " Interspection");
        System.out.println(y.relativeDifference(z) + " RelativeDifference");
        System.out.println(y.symetricDifference(z) + " SymetricDifference");
        System.out.println(y.union(z) + " Union");
    }
}