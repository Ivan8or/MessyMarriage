package online.umbcraft.messymariage.util;

public class ExpLevelConverter {

    final private static int[] LEVEL_EXP = new int[110];
    final private static int[] CUMUL_LEVEL_EXP = new int[110];

    static {
        for(int i = 1; i < LEVEL_EXP.length; i++) {
            LEVEL_EXP[i] = expForSingleLevel(i);
            CUMUL_LEVEL_EXP[i] = LEVEL_EXP[i] + CUMUL_LEVEL_EXP[i-1];
        }
    }
    private static int expForSingleLevel(int level) {
        if(level < 0)
            return 0;

        return (int)(100 + Math.pow(Math.E, 0.085*level));
    }

    public static int toLevel(int exp) {
        if(exp < 0)
            return 0;

        for(int i = 1; i < LEVEL_EXP.length; i++)
            if(CUMUL_LEVEL_EXP[i] > exp)
                return i-1;

        return CUMUL_LEVEL_EXP.length-1;
    }

    public static int toExp(int level) {
        if(level < 0)
            return 0;

        assert level < LEVEL_EXP.length : "level too high";
        return CUMUL_LEVEL_EXP[level];
    }

    public static int percentProgress(int exp) {
        if(exp < 0)
            return 0;
        int levelAt = toLevel(exp);
        int expNeeded = expForSingleLevel(levelAt + 1);

        int priorLevelExp = toExp(levelAt);
        int curLevelExp = exp - priorLevelExp;

        double ratio = (curLevelExp / (double)expNeeded) * 100;
        return (int) Math.floor(ratio);


    }

}
