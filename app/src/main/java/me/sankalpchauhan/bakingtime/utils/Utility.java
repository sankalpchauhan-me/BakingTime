package me.sankalpchauhan.bakingtime.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.DisplayMetrics;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import me.sankalpchauhan.bakingtime.service.model.Ingredient;
import timber.log.Timber;

/**
 * A utility class for common methods
 */
public class Utility {

    public static boolean isOnline() {
        /**
         * ATTRIBUTION: isOnline() CODE
         * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
         **/
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static int calculateNoOfColumns(Context context) {
        /**
         * Dynamically Calculates the number of columns that can fit in the display view
         *
         * ATTRIBUTION calculateNoOfColumns(Context context)
         * https://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns/38472370#38472370
         */
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        return (int) (dpWidth / scalingFactor);
    }

    /**
     * Returns a user agent string based on the given application name and the library version.
     *
     * @param context         A valid context of the calling application.
     * @param applicationName String that will be prefix'ed to the generated user agent.
     * @return A user agent string generated using the applicationName and the library version.
     */
    public static String getUserAgent(Context context, String applicationName) {
        String versionName;
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "?";
        }
        return applicationName + "/" + versionName + " (Linux;Android " + Build.VERSION.RELEASE
                + ") " + ExoPlayerLibraryInfo.VERSION_SLASHY;
    }

    public static String stringFormatter(List<Ingredient> list) {
        StringBuilder finalString = new StringBuilder();
        for (int i = 0; i <= list.size() - 1; i++) {
            Ingredient ingredient = list.get(i);
            finalString.append(i + 1).append(") ").append(ingredient.getQuantity()).append(" ").append(ingredient.getMeasure()).append(" ").append(ingredient.getIngredient()).append("\n");
        }
        Timber.d("CHECK STRING FORMAT \n" + finalString.toString());
        return finalString.toString();

    }

}
