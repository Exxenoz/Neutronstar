package at.autrage.projects.zeta.module;

import android.util.Log;

public final class Logger {

    private Logger() {}

    public static void E(String message, Object... values) {
        Log.e("PNE::E", String.format(message, values));
    }

    public static void W(String message, Object... values) {
        Log.w("PNE::W", String.format(message, values));
    }

    public static void D(String message, Object... values) {
        Log.d("PNE::D", String.format(message, values));
    }
}
