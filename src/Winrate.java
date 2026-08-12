import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Winrate {
    ArrayList<String> raw = new ArrayList<String>();
    int[][] data = new int[6][6];

    public Winrate(String path) throws IOException {
        //for(int i = 0; i < data.length; i ++) for(int j = 0; j < data.length; j ++) if(i == j) data[i][j] = -1;

        BufferedReader fileReader = new BufferedReader(new FileReader(path));
        String line;
        while ((line = fileReader.readLine()) != null) raw.add(line);
        fileReader.close();
        for(int i = 0; i < raw.size(); i ++) {
            raw.set(i, raw.get(i).replaceAll("\t", ","));
            if(raw.get(i).equals(",,")) {
                raw.remove(i);
                i --;
            }
        }

        String[] n;
        String ratA = "";
        String ratB = "";
        String scoreA = "";
        String scoreB = "";
        //String nameA = "";
        //String nameB = "";
        int idxA = -1;
        int idxB = -1;
        for(int i = 0; i < raw.size(); i += 2) {
            

            n = raw.get(i).split(",");
            if(n.length > 1) {
                //nameA = n[0];
                ratA = n[1];
                scoreA = n[2];
            } else continue;

            n = raw.get(i + 1).split(",");
            if(n.length > 1) {
                //nameB = n[0];
                ratB = n[1];
                scoreB = n[2];
            } else continue;


            if(!isNumeric(scoreA) || !isNumeric(scoreB)) continue;
            //System.out.println(nameA + " (" + ratA + "): " + scoreA + ", " + nameB + " (" + ratB + "): " + scoreB);

            if(Integer.parseInt(scoreB) > Integer.parseInt(scoreA)) {
                String temp = ratA;
                ratA = ratB;
                ratB = temp;
            }

            switch (ratA.charAt(0)) {
                case 'A':
                    idxA = 0;
                    break;
                case 'B':
                    idxA = 1;
                    break;
                case 'C':
                    idxA = 2;
                    break;
                case 'D':
                    idxA = 3;
                    break;
                case 'E':
                    idxA = 4;
                    break;
                case 'U':
                    idxA = 5;
                    break;
            }
            switch (ratB.charAt(0)) {
                case 'A':
                    idxB = 0;
                    break;
                case 'B':
                    idxB = 1;
                    break;
                case 'C':
                    idxB = 2;
                    break;
                case 'D':
                    idxB = 3;
                    break;
                case 'E':
                    idxB = 4;
                    break;
                case 'U':
                    idxB = 5;
                    break;
            }

            data[idxA][idxB] ++;
        }

        //System.out.println(Arrays.deepToString(data));
        System.out.println("\n    A   B   C   D   E   U");
        for(int i = 0; i < data.length; i ++) {
            switch (i) {
                case 0: System.out.print("A  "); break;
                case 1: System.out.print("B  "); break;
                case 2: System.out.print("C  "); break;
                case 3: System.out.print("D  "); break;
                case 4: System.out.print("E  "); break;
                case 5: System.out.print("U  "); break;
            }
            for(int j = 0; j < data.length; j ++) System.out.print(String.format("%03d", data[i][j]) + " ");
            System.out.println();
        }
    }

    private static boolean isNumeric(String input) { 
        try {
            Integer.parseInt(input);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}