public class Move {
    int from;
    int to;

    public Move(int from, int to) {
        this.from = from;
        this.to = to;
    }
    public String toString(){
        return "from: " + from + ", to: " + to;
    }
}