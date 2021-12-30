package net.nosocial.traits.core;

/**
 * @author ikh
 * @since 2/7/15
 */
public class OSUtils {
    private static String OS = null;

    public static String getOsName()
    {
        if (OS == null) {
            OS = System.getProperty("os.name");
        }
        return OS;
    }

    public static boolean isWindows()
    {
        return getOsName().startsWith("Windows");
    }
}
