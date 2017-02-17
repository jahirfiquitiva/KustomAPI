package org.kustom.api.dashboard;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

class ActivityUtils {

    static void openPkgStoreUri(@NonNull Context context, @NonNull String pkg) {
        openUri(context, String.format("market://details?id=%s", pkg));
    }

    static void openUri(@NonNull Context context, @NonNull String uriString) {
        Uri uri = Uri.parse(uriString);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(i);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
