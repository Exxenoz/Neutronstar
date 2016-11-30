package at.autrage.projects.zeta.module;

import android.util.Log;

public final class Logger {

    private Logger() {}

    public static void Error(String message, Object... values) {
        Log.e("PNE::ERR", String.format(message, values));
    }

    public static void Warning(String message, Object... values) {
        Log.w("PNE::WRN", String.format(message, values));
    }

    public static void Debug(String message, Object... values) {
        Log.d("PNE::DBG", String.format(message, values));
    }
}
