import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Ratings {
    public static LinkedHashMap<String, Integer> ratings = new LinkedHashMap<>();
    static {
        ratings.put("O", 1100);
        ratings.put("A", 1000);
        ratings.put("B", 900);
        ratings.put("C", 800);
        ratings.put("D", 700);
        ratings.put("E", 600);
        ratings.put("F", 590);
        ratings.put("U+", 500);
        ratings.put("U", 400);
        ratings.put("u", 300);
    }

    // Could make it so that rating is at top of roster file; may need to restructure functions todo that
    public static double rankToScore(String rating, int ratingYear) { // TODO: Probably need a hookup to change year so we arent grading bouts of past as worse
        if (Main.thisYear - ratingYear < 4) return ratings.get(rating) - ((Main.thisYear - ratingYear) * 25) + 50;
        else return ratings.get(rating) - 100 + 50;
    }

    public static String scoreToRank(double score) {
        for(Map.Entry<String, Integer> n : ratings.entrySet()) {
            if(score >= n.getValue()) {
                return n.getKey();
            }
        }
        return "u";
    }

    public static double winChance(Fencer given, Fencer opponent) {
        return 1.0 / (1.0 + Math.pow(10, (opponent.score - given.score) / 400.0));
    }

    public static void averagePlacement(String path) throws IOException {
        System.out.println("[U, E, D, C, B, A]");
        ArrayList<String> raw = new ArrayList<String>();
        double[] totals = new double[6];
        int[] counts = new int[6];

        BufferedReader fileReader = new BufferedReader(new FileReader(path));
        String line;
        while ((line = fileReader.readLine()) != null) raw.add(line);
        fileReader.close();

        for (int i = 0; i < raw.size(); i++) raw.set(i, raw.get(i).replaceAll("\t", ","));

        for (int i = 0; i < raw.size(); i++) {
            String letter = raw.get(i).split(",")[1];

            switch (letter.toUpperCase().charAt(0)) {
                case 'U':
                    totals[0] += i + 1.0;
                    counts[0]++;
                    break;
                case 'E':
                    totals[1] += i + 1.0;
                    counts[1]++;
                    break;
                case 'D':
                    totals[2] += i + 1.0;
                    counts[2]++;
                    break;
                case 'C':
                    totals[3] += i + 1.0;
                    counts[3]++;
                    break;
                case 'B':
                    totals[4] += i + 1.0;
                    counts[4]++;
                    break;
                case 'A':
                case 'O':
                    totals[5] += i + 1.0;
                    counts[5]++;
                    break;
            }
        }

        for (int i = 0; i < totals.length; i++) if (counts[i] != 0) totals[i] /= counts[i];
        System.out.println(java.util.Arrays.toString(totals));
    }
}
