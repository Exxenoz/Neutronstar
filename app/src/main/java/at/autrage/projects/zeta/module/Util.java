package at.autrage.projects.zeta.module;


public class Util {
    public static String addLeadingZeros(int src, int length, boolean seperator) {
        String out = "";
        String str = ""+src;

        for (int i = 0, j = str.length() - 1; i < length; i++) {
            if (seperator && i != 0 && i % 3 == 0) {
                out = "," + out;
            }

            if (j >= 0) {
                out = str.charAt(j) + out;
                j--;
            }
            else {
                out = "0" + out;
            }
        }

        return out;
    }

    public static int roof(float value) {
        if (value > (int) value) {
            return (int) (value + 1);
        }
        else {
            return (int) value;
        }
    }

    public static int roof(double value) {
        if (value > (int) value) {
            return (int) (value + 1);
        }
        else {
            return (int) value;
        }
    }
}
