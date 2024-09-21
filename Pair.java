
public class Pair<A,B> {
    public A item1;
    public B item2;
    public Pair(A first, B second) {
        this.item1 = first;
        this.item2 = second;
    }
    public String toString() {
        return "(" + item1.toString() + " " + item2.toString() + ")";
    }
}
