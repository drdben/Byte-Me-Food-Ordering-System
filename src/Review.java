public class Review {
    private int score;
    private String comment;

    public Review(int sc, String com){
        this.score = sc;
        this.comment = com;
    }

    public int getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }
}
