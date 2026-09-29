import java.util.ArrayList;
import java.util.Arrays;

//TODO: maybe have gender bool and have alternate rating sys for female

public class Fencer implements Comparable<Fencer>{
    String firstName;
    String lastName;
    // boolean isMale = true;
    String rating;
    int ratingYear;
    double score;
    ArrayList<Double> scoreHistory = new ArrayList<Double>();
    String aidanRating;
    int boutsRecorded = 0;
    String loadMethod = "default";

    public Fencer(String name, String rating) {
        String[] pieces = name.split(" ", 2);
        if(pieces.length == 2) this.lastName = pieces[1];
        this.firstName = pieces[0];
        setRating(rating);
        this.score = Ratings.rankToScore(this.rating, this.ratingYear);
        this.aidanRating = Ratings.scoreToRank(this.score);
        this.scoreHistory.add(this.score);
    }

    public Fencer(String name, String rating, double score) {
        String[] pieces = name.split(" ", 2);
        if(pieces.length == 2) this.lastName = pieces[1];
        this.firstName = pieces[0];
        setRating(rating);
        this.score = score;
        this.loadMethod = "manual";
        this.aidanRating = Ratings.scoreToRank(this.score);
        this.scoreHistory.add(this.score);
    }

    public void setRating(String rating) {
        String[] pieces = rating.split("(?=\\d)", 2);
        this.rating = pieces[0];
        this.ratingYear = (pieces.length == 2) ? parseYear(pieces[1]) : Main.thisYear;
    }

    private int parseYear(String ratingYear) {
        if (ratingYear.length() == 2) return Integer.parseInt(ratingYear) + Main.thisYear/1000*1000;
        return Integer.parseInt(ratingYear);
    }

    public String name() {
        return this.firstName + " " + this.lastName;
    }

    public String toString() {
        if(!rating.equals("U") && !rating.equals("U+") && !rating.equals("u")) return this.firstName + " " + this.lastName + "," + this.rating + (this.ratingYear-Main.thisYear/1000*1000) + "," + Math.round(this.score);
        else return this.firstName + " " + this.lastName + "," + this.rating + "," + Math.round(this.score);
    }

    public Fencer print() {
        if(!rating.equals("U") && !rating.equals("U+") && !rating.equals("u")) System.out.println(this.firstName + " " + this.lastName + " " + this.rating + (this.ratingYear-Main.thisYear/1000*1000) + " (" + Math.round(this.score) + "-" + this.aidanRating + ")");
        else System.out.println(this.firstName + " " + this.lastName + " " + this.rating + " (" + Math.round(this.score) + "-" + this.aidanRating + ")");
        return this;
    }

    public Fencer export() {
        if(!rating.equals("U") && !rating.equals("U+") && !rating.equals("u")) System.out.println(this.firstName + " " + this.lastName + "," + this.rating + (this.ratingYear-Main.thisYear/1000*1000) + "," + Math.round(this.score));
        else System.out.println(this.firstName + " " + this.lastName + "," + this.rating + "," + Math.round(this.score));
        return this;
    }

    public void printHistory(int min, int max, int height) {
        System.out.println("\n" + Ansi.bold + Ansi.white + "Fencer: " + this.name() + " (" + Math.round(this.score) + "-" + this.aidanRating +")" + Ansi.reset);
        //TODO: write function that determines when a score crosses into a new rating
        //int width = 150;
        //int height = 40;
        //int min = 200;
        //int max = 1200;
        int range = max - min;
        double ppy = (double)range / (double)height;
        
        String[][] output = new String[this.boutsRecorded+1][];
        String[] data;
        for(int i = 0; i < this.scoreHistory.size(); i++) {
            data = new String[height+1];
            Arrays.fill(data, " ");
            data[(int)Math.round((this.scoreHistory.get(i) - min)/ppy)] = Ansi.cyan + "X" + Ansi.reset;
            output[i] = data;
        }

        //System.out.print("    +"); for(int i = 0; i < this.boutsRecorded; i++) System.out.print("-"); System.out.println();
        String[][] print = transpose(output);
        for(int i = print.length - 1; i >= 0; i --) {
            System.out.print(String.format("%1$4s", (int)(i*ppy) + min));
            System.out.print("|");
            for(String o : print[i]) {
                if(((i*ppy) + min) % 100 == 0 && o.equals(" ")) System.out.print(Ansi.black + "-" + Ansi.reset); 
                else System.out.print(o); 
            }
            System.out.println();
        }
        System.out.print("    +"); for(int i = 0; i < this.boutsRecorded+1; i++) System.out.print("-"); System.out.println();
    }

    public void printHistory() {
        printHistory(250, 1050, 32);
    }

    private static String[][] transpose(String[][] mat) {
        String[][] result = new String[mat[0].length][mat.length];
        for(int i = 0; i < mat.length; ++i) for(int j = 0; j < mat[0].length; ++j) result[j][i] = mat[i][j];
        return result;
    }

    @Override
    public int compareTo(Fencer o) {
        return Double.compare(o.score, this.score);
    }

    public boolean equals(Fencer o) {
        return this.name().equalsIgnoreCase(o.name());
    }
}
