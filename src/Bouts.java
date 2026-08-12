public class Bouts {
    private static double boutExp = 15.0; //10.0
    private static double elop = 0.5 / 2.0;
    private static double elow = 0.5 / 2.0;

    public static Fencer testBout(Fencer left, Fencer right, boolean forScore) {
        if (left.score > right.score) {
            if(forScore) scoreCalc(left, right);
            return left;
        } else if (left.score < right.score) {
            if(forScore) scoreCalc(right, left);
            return right;
        } else {
            if (Math.random() >= 0.5) {
                if(forScore) scoreCalc(left, right);
                return left;
            } else {
                if(forScore) scoreCalc(right, left);
                return right;
            }
        }
    }

    public static Fencer simBout(Fencer left, Fencer right, boolean forScore) {
        double winChance = Ratings.winChance(left, right);
        double roll = Math.random();
        if(roll < winChance) {
            if(forScore) scoreCalc(left, right);
            return left;
        } else {
            if(forScore) scoreCalc(right, left);
            return right;
        }
    }

    public static Fencer bout(Fencer left, Fencer right, int touchLeft, int touchRight, boolean forScore) {
        if(touchLeft > touchRight) {
            if(forScore) scoreCalc(left, right, touchLeft, touchRight);
            return left;
        } else if(touchLeft < touchRight) {
            if(forScore) scoreCalc(left, right, touchLeft, touchRight);
            return right;
        }
        return null;
    }

    public static Fencer skipBout(Fencer winner, Fencer loser, boolean forScore) {
        if(forScore) scoreCalc(winner, loser);
        return winner;
    }

    public static void scoreCalc(Fencer left, Fencer right, int touchLeft, int touchRight) {
        double change = Main.elok*((0.5 + elop*(touchLeft - touchRight)/15.0 + elow*(Math.signum(touchLeft - touchRight))) - Ratings.winChance(left, right));
        if(Main.showPointChanges) System.out.println(left.name() + " (" + Math.round(left.score) + "): " + Math.round(change) + ", " + right.name() + " (" + Math.round(right.score) + "): " + -Math.round(change));
        left.score += change; right.score -= change;
        left.score += boutExp/(Math.exp(left.score/250.0)); right.score += boutExp/(Math.exp(right.score/250.0));
        left.aidanRating = Ratings.scoreToRank(left.score); right.aidanRating = Ratings.scoreToRank(right.score);
        left.boutsRecorded ++; right.boutsRecorded ++;
        left.scoreHistory.add(left.score); right.scoreHistory.add(right.score);

    }

    public static void scoreCalc(Fencer winner, Fencer loser) {
        scoreCalc(winner, loser, 5, 0);
    }
}
