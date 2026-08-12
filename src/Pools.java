//TODO: could have a way to lookup fencers and use the score instead of ratings
//TODO: add 50 point randoming

import java.io.IOException;

public class Pools {
    Roster roster;
    int poolSize;
    Fencer[][] pools;

    public Pools(String path, int poolSize) throws IOException {
        this.roster = new Roster(path); roster.alphabetize().dupeCheck();
        for(Fencer n : this.roster.fencers) n.score += Math.random()/100;
        roster.sort();
        this.poolSize = poolSize;
        this.pools = new Fencer[(int) Math.ceil((double)roster.fencerCount/this.poolSize)][this.poolSize];
        
        int dir = 1;
        int loop = 0;
        int pool = 0;
        for(int i = 0; i < roster.fencerCount; i ++) {
            pools[pool][loop] = roster.fencers[i];
            if(dir == 1 && pool == (int) Math.ceil((double)roster.fencerCount/this.poolSize) - 1) {
                dir = -1;
                loop ++;
            } else if(dir == -1 && pool == 0) {
                dir = 1;
                loop ++;
            } else {
                pool += dir;
            }
        }

        for (int i = 0; i < this.pools.length; i++) {
            this.pools[i] = trim(this.pools[i]);
        }
    }

    private Fencer[] trim(Fencer[] input) {
        int count = 0;
        for(Fencer n : input) if(n != null) count ++;
        
        Fencer[] output = new Fencer[count];
        int index = 0;
        for(Fencer n : input) if(n != null) output[index++] = n;

        return output;
    }

    public void print(boolean printRatings) {
        int idx = 0;
        for(Fencer[] n : pools) {
            System.out.println(Ansi.bold + Ansi.underline + Ansi.white + "\nPool " + (char)('A' + idx) + " (" + n.length + ")" + Ansi.reset); idx ++;
            Roster temp = new Roster(n);
            poolPrint(temp, printRatings);
        }
    }

    private void poolPrint(Roster roster, boolean printRatings) {
        String[][] output = new String[roster.fencerCount][];
        Fencer get;
        for(int i = 0; i < roster.fencerCount; i++) {
            get = roster.fencers[i];
            if(!get.rating.equals("U") && !get.rating.equals("U+") && !get.rating.equals("u")) output[i] = new String[] {get.name(), get.rating + (get.ratingYear-get.ratingYear/1000*1000), Math.round(get.score)+"-"+get.aidanRating, String.valueOf(get.boutsRecorded)};
            else output[i] = new String[] {get.name(), get.rating, Math.round(get.score)+"-"+get.aidanRating, String.valueOf(get.boutsRecorded)};
        }
        if(printRatings) for (String[] n : output) System.out.format("%-24s %-8s \n", n);
        else {
            for (String[] n : output) System.out.format("%s\n", n[0]);
            System.out.println();
            //for (String[] n : output) System.out.format("%s\n", n[1]);
        }
    }
}