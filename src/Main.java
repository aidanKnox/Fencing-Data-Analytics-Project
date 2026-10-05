import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: new fencer alert

public class Main {
    public static void main(String[] args) throws IOException {
        clearScreen();
        run();
        //fcau();
        //pools();
        //test();
        //ratingAvgPlace();
        //ratingWinrates();
    }

    public static boolean showPointChanges = true;
    public static int thisYear = Year.now().getValue();
    public static double elok = 40; // 30

    @SuppressWarnings("unused")
    private static void run() throws IOException {
        sectionStart();

        List<String> rosters = getFolder("res\\Rosters");
        List<String> ledgers = getFolder("res\\Ledgers");

        Roster fcau = new Roster("res\\Clubs\\FCAU.csv", "FCAU"); fcau.sort(); fcau.save();
        Roster friends = new Roster("res\\Clubs\\Friends of FCAU.csv"); friends.sort();
        Roster uaf = new Roster("res\\Clubs\\UAF.csv", "UAF"); uaf.sort();
        Roster override = new Roster("res\\Clubs\\Override.csv"); override.sort();
        Roster master = Roster.merge(fcau, Roster.merge(friends, Roster.merge(uaf, override)));
        //Roster master = fcau;

        for(int i = 1; i < ledgers.size()+1; i ++) {
            if(i <= 45) thisYear = 2024;
            else if(i <= 105) thisYear = 2025;
            else thisYear = Year.now().getValue();

            master = Roster.merge(master, new Roster(rosters.get(i-1)));
            new Ledger(ledgers.get(i-1), master).run();
        }

        master.dupeCheck();
        master.sort().printFancy(40);
        //Roster.merge(fcau, uaf).sort().printFancy(15, true);
        //fcau.sort().export();
        fcau.sort().printFancy(10, true);
        //uaf.sort().printFancy(10, true);

        System.out.println();
        sectionStartln();
    }

    @SuppressWarnings("unused")
    private static void fcau() throws IOException {
        sectionStart();

        List<String> ledgers = getFolder("res\\Ledgers");

        Roster master = new Roster("res\\Clubs\\FCAU.csv", "FCAU"); master.sort();

        for(int i = 1; i < ledgers.size()+1; i ++) {
            if(i <= 45) thisYear = 2024;
            else thisYear = Year.now().getValue();

            new Ledger(ledgers.get(i-1), master).run();
        }

        master.alphabetize().dupeCheck();
        master.sort().printFancy(5);
        master.getScoreTotal();

        sectionStartln();
    }

    @SuppressWarnings("unused")
    private static void pools() throws IOException {
        sectionStart();

        Pools pool = new Pools("res\\BigFCAUPools.csv", 7);
        pool.print(true);

        sectionStartln();
    }

    @SuppressWarnings("unused")
    private static void test() throws IOException {
        sectionStart();
    }

    @SuppressWarnings("unused")
    private static void ratingWinrates() throws IOException {
        Winrate test = new Winrate("res\\Rating Winrate Data.txt");
    }

    @SuppressWarnings("unused")
    private static void ratingAvgPlace() throws IOException {
        Ratings.averagePlacement("res\\AverageRatingPlacement.txt");
    }

    private static List<String> getFolder(String path) throws IOException {
        Path folder = Paths.get(path);

        List<String> output = null;
        try(Stream<Path> list = Files.list(folder)) {
            output = list.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
            //output.forEach(System.out::println);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    public static void clearScreen() {
        try {
            //for(int i = 0; i < 750; i++) System.out.println();
            if (System.getProperty("os.name").startsWith("Windows")) new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else new ProcessBuilder("clear").inheritIO().start().waitFor();
        } catch(IOException | InterruptedException ex) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    private static void sectionStart() {
        System.out.print(Ansi.purpleBright + "==========================================================================================================================================================================================================" + Ansi.reset);
    }

    private static void sectionStartln() {
        System.out.println(Ansi.purpleBright + "==========================================================================================================================================================================================================" + Ansi.reset);
    }
}
