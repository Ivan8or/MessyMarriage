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
        assert level >= 0 : "negative level can't have exp";
        return (int)(100 + Math.pow(Math.E, 0.085*level));
    }

    public static int toLevel(int exp) {
        assert exp >= 0 : "negative exp can't have level";
        for(int i = 1; i < LEVEL_EXP.length; i++) {
            if(CUMUL_LEVEL_EXP[i] > exp)
                return i-1;
        }
        return CUMUL_LEVEL_EXP.length-1;
    }

    public static int toExp(int level) {
        assert level >= 0 : "negative level can't have exp";
        assert level < LEVEL_EXP.length : "level too high";
        return CUMUL_LEVEL_EXP[level];
    }

}
