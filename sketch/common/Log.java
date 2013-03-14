package sketch.common;

import static sketch.common.Config.*;

public class Log {

    public static void debug (String msg) {
        debug(msg, 1);
    }

    public static void debug (String msg, int lvl) {
        if (ENABLE_DEBUGGING &&
            lvl >= DBG_LEVEL) {
            System.out.println(msg);
        }
    }

}
