import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//TODO: make ? in spreadsheet do a sim bout

public class Ledger {
    ArrayList<String> raw;
    Roster roster;
    String path;

    public Ledger(String path, Roster roster) throws IOException {
        this.path = path;
        this.roster = roster;
        this.raw = new ArrayList<>();
        BufferedReader csvReader = new BufferedReader(new FileReader(this.path));

        String line = csvReader.readLine();
        System.out.println("\n" + Ansi.underline + Ansi.blue + path + Ansi.reset);
        while ((line = csvReader.readLine()) != null) raw.add(line);

        csvReader.close();
    }

    public void run() {
        String[] pieces;
        for(String n : raw) {
            if(n.equals("/")) continue;
            if(n.equals("")) continue;
            pieces = n.split(",");
            if(this.roster.get(pieces[0]) == null) continue;
            if(this.roster.get(pieces[1]) == null) continue;
            if(pieces[2].equals(">")) Bouts.skipBout(roster.get(pieces[0]), roster.get(pieces[1]), false);
            else if(pieces[3].equals(">")) Bouts.skipBout(roster.get(pieces[1]), roster.get(pieces[0]), false);
            else Bouts.bout(roster.get(pieces[0]), roster.get(pieces[1]), Integer.parseInt(pieces[2]), Integer.parseInt(pieces[3]), true);
        }
    }

    public void print() {
        for(String n : this.raw) System.out.println(n);
    }
}
