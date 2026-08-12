import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Roster {
    private ArrayList<String> raw;
    Fencer[] fencers;
    private double scoreTotal;
    int fencerCount;
    String path;
    String clubName;

    public Roster(String path) throws IOException {
        this.path = path;
        this.raw = new ArrayList<>();
        BufferedReader csvReader = new BufferedReader(new FileReader(this.path));

        String line = csvReader.readLine();
        while ((line = csvReader.readLine()) != null && !line.equals(",")) raw.add(line);

        this.fencerCount = raw.size();
        fencers = new Fencer[this.fencerCount];
        String[] pieces;
        for (int i = 0; i < this.fencerCount; i++) {
            pieces = raw.get(i).split(",");
            if (pieces.length > 2 && pieces[2] != null && !pieces[2].equals("") && !pieces[2].equals(" ")) fencers[i] = new Fencer(pieces[0], pieces[1], Double.parseDouble(pieces[2]));
            else fencers[i] = new Fencer(pieces[0], pieces[1]);
        }

        csvReader.close();
        // System.out.println("\n" + Ansi.underline + Ansi.blue + path + Ansi.reset);
    }

    public Roster(String path, String clubName) throws IOException {
        this.clubName = clubName;
        this.path = path;
        this.raw = new ArrayList<>();
        BufferedReader csvReader = new BufferedReader(new FileReader(this.path));

        String line = csvReader.readLine();
        while ((line = csvReader.readLine()) != null && !line.equals(",")) raw.add(line);

        this.fencerCount = raw.size();
        fencers = new Fencer[this.fencerCount];
        String[] pieces;
        for (int i = 0; i < this.fencerCount; i++) {
            pieces = raw.get(i).split(",");
            if (pieces.length > 2 && pieces[2] != null && !pieces[2].equals("") && !pieces[2].equals(" ")) fencers[i] = new Fencer(pieces[0], pieces[1], Double.parseDouble(pieces[2]));
            else fencers[i] = new Fencer(pieces[0], pieces[1]);
        }

        csvReader.close();
    }

    public Roster(ArrayList<String> raw) {
        this.raw = raw;

        this.fencerCount = raw.size();
        fencers = new Fencer[this.fencerCount];
        String[] pieces;
        for (int i = 0; i < this.fencerCount; i++) {
            pieces = raw.get(i).split(",");
            if (pieces.length > 2 && pieces[2] != null && !pieces[2].equals("") && !pieces[2].equals(" ")) fencers[i] = new Fencer(pieces[0], pieces[1], Double.parseDouble(pieces[2]));
            else fencers[i] = new Fencer(pieces[0], pieces[1]);
        }

    }

    public Roster(Fencer[] data, String clubName) {
        this.fencers = data;
        this.fencerCount = data.length;
        this.clubName = clubName;
    }

    public Roster(Fencer[] data) {
        this.fencers = data;
        this.fencerCount = data.length;
    }

    public double getScoreTotal() {
        this.scoreTotal = 0;
        for(Fencer n : this.fencers) this.scoreTotal += n.score;
        if(this.clubName != null) System.out.println(Ansi.bold + Ansi.white + this.clubName + " Score Total: " + Math.round(this.scoreTotal) + Ansi.reset);
        return this.scoreTotal;
    }
    
    public Fencer get(String firstName, String lastName) {
        for (Fencer n : this.fencers) if (n.firstName.equalsIgnoreCase(firstName) && n.lastName.equalsIgnoreCase(lastName)) return n;
        System.out.println(Ansi.yellow + "Warning: Fencer \"" + firstName + " " + lastName + "\" not found." + Ansi.reset);
        return null;
    }

    public Fencer get(String name) {
        for (Fencer n : this.fencers) if ((n.firstName + " " + n.lastName).equalsIgnoreCase(name)) return n;
        System.out.println(Ansi.yellow + "Warning: Fencer \"" + name + "\" not found." + Ansi.reset);
        return null;
    }

    public void save() throws IOException {
        FileWriter csvWriter = new FileWriter(new File(this.path), false);
        csvWriter.write("Name,Rating,Score\n");
        for (Fencer n : this.fencers) csvWriter.write(n.toString() + "\n");
        csvWriter.close();
    }

    public void saveTo(String path) throws IOException {
        FileWriter csvWriter = new FileWriter(new File(path), false);
        csvWriter.write("Name,Rating,Score\n");
        for (Fencer n : this.fencers) csvWriter.write(n.toString() + "\n");
        csvWriter.close();
    }

    public Roster sort() {
        Arrays.sort(this.fencers);
        return this;
    }

    public Roster alphabetize() {
        Arrays.sort(this.fencers, 0, fencerCount, Comparator.comparing(f -> f.firstName));
        return this;
    }

    public void dupeCheck() {
        System.out.println();
        Roster temp = new Roster(this.fencers);
        ArrayList<Integer[]> skipList = new ArrayList<>();
        boolean skip;
        String[] pieces;
        for(int i = 0; i < temp.fencerCount - 1; i ++) {
            for(int j = 0; j < temp.fencerCount - 1; j ++) {
                if(i == j) continue;

                skip = false;
                for(Integer[] n : skipList) {
                    if(i == n[1] && j == n[0]) {
                        skip = true;
                        break;
                    }
                }

                if(temp.fencers[i].firstName.equalsIgnoreCase(temp.fencers[j].firstName)) {
                    pieces = temp.fencers[i].lastName.split(" ");
                    if(temp.fencers[j].lastName.contains(pieces[pieces.length - 1].substring(0, 2))) {
                        if(!skip) System.out.println(Ansi.yellow + "Warning: Possible duplication (" + temp.fencers[i].name() + " and " + temp.fencers[j].name() + ")" + Ansi.reset);
                        skipList.add(new Integer[]{i, j});
                    }
                }
            }
        }
        if(skipList.size() == 0) System.out.println(Ansi.greenBright + "No Dupes Found" + Ansi.reset);
    }

    public static Roster merge(Roster base, Roster supplement) {
        Fencer[] noDupes = new Fencer[supplement.fencerCount];
        boolean found;
        int nullCount = 0;
        for(int i = 0; i < supplement.fencerCount; i ++) {
            found = false;
            for(int j = 0; j < base.fencerCount; j ++) {
                if(supplement.fencers[i].equals(base.fencers[j])) {
                    if(Ratings.rankToScore(supplement.fencers[i].rating, supplement.fencers[i].ratingYear) > Ratings.rankToScore(base.fencers[j].rating, base.fencers[j].ratingYear)) {
                        //Could use something to find rankups and give bonuses
                        base.fencers[j].rating = supplement.fencers[i].rating;
                        base.fencers[j].ratingYear = supplement.fencers[i].ratingYear;
                    }

                    /*if(supplement.data[i].boutsRecorded > base.data[j].boutsRecorded) {
                        double oldScore = base.data[j].score;
                        int oldBoutsRecorded = base.data[j].boutsRecorded;
                        base.data[j] = supplement.data[i];
                        System.out.println("Score Before: " + base.data[j].score);
                        base.data[j].score = (oldScore*oldBoutsRecorded + supplement.data[i].score*supplement.data[i].boutsRecorded)/(oldBoutsRecorded + supplement.data[i].boutsRecorded);
                        System.out.println("Score Before: " + base.data[j].score);
                        base.data[j].boutsRecorded += oldBoutsRecorded;
                    }*/

                    found = true;
                    nullCount ++;
                    break;
                }
            }
            if(!found) noDupes[i] = supplement.fencers[i];
        }

        Fencer[] supplementOutput = new Fencer[supplement.fencerCount - nullCount];
        int skipCount = 0;
        for(int i = 0; i < noDupes.length; i ++) {
            if(noDupes[i] != null) supplementOutput[i - skipCount] = noDupes[i];
            else skipCount ++;
        }

        Fencer[] result = Arrays.copyOf(base.fencers, base.fencerCount + supplementOutput.length);
        System.arraycopy(supplementOutput, 0, result, base.fencerCount, supplementOutput.length);

        return new Roster(result, base.clubName + " + " + supplement.clubName);
    }

    public Fencer getLargestDelta() {
        Fencer output = fencers[0];
        for(Fencer n : fencers) if(n.scoreHistory.size() > 1 && n.scoreHistory.get(n.scoreHistory.size()-1) - n.scoreHistory.get(0) > output.scoreHistory.get(output.scoreHistory.size()-1) - output.scoreHistory.get(0)) output = n;
        return output;
    }

    public void print() {
        System.out.println();
        for(Fencer n : this.fencers) n.print();
    }

    public Roster export() {
        System.out.println();
        for(Fencer n : this.fencers) n.export();
        return this;
    }

    public void printFancy(int minBouts, boolean printClubName) {
        if(printClubName) System.out.println("\n" + Ansi.bold + Ansi.white + "Club: " + this.clubName + ", Minimum Bouts: " + minBouts + Ansi.reset);
        else if(minBouts != 0) System.out.println("\n" + Ansi.bold + Ansi.white + "Minimum Bouts: " + minBouts + Ansi.reset);

        String[][] output = new String[fencerCount][];
        Fencer get;
        System.out.format(Ansi.bold + Ansi.white + "%-24s %-8s %-8s %-8s \n" + Ansi.reset, new String[] {"Name", "Rating", "Score", "Bouts"});
        for(int i = 0; i < fencerCount; i++) {
            get = this.fencers[i];
            if(!get.rating.equals("U") && !get.rating.equals("U+") && !get.rating.equals("u")) output[i] = new String[] {get.name(), get.rating + (get.ratingYear-get.ratingYear/1000*1000), Math.round(get.score)+"-"+get.aidanRating, String.valueOf(get.boutsRecorded)};
            else output[i] = new String[] {get.name(), get.rating, Math.round(get.score)+"-"+get.aidanRating, String.valueOf(get.boutsRecorded)};
            if(Integer.parseInt(output[i][3]) >= minBouts) System.out.format("%-24s %-8s %-8s %-8s \n", output[i]);
        }
        // for (String[] n : output) if(n[0].equals("Name") || Integer.parseInt(n[3]) >= minBouts) System.out.format("%-24s %-8s %-8s %-8s \n", n);
    }

    public void printFancy(boolean printClubName) {
        printFancy(0, printClubName);
    }

    public void printFancy(int minBouts) {
        printFancy(minBouts, false);
    }

}
