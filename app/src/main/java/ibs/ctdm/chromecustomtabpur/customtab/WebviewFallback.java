package ibs.ctdm.chromecustomtabpur.customtab;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import ibs.ctdm.chromecustomtabpur.activity.WebviewActivity;


/**
 * A Fallback that opens a Webview when Custom Tabs is not available
 */
public class WebviewFallback implements CustomTabActivityHelper.CustomTabFallback {
    @Override
    public void openUri(Activity activity, Uri uri) {
        Intent intent = new Intent(activity, WebviewActivity.class);
        intent.putExtra("KEY_URL", uri.toString());
        activity.startActivity(intent);
    }
}