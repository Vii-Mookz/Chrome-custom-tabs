package ibs.ctdm.chromecustomtabpur.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.widget.Toast;

import ibs.ctdm.chromecustomtabpur.R;
import ibs.ctdm.chromecustomtabpur.customtab.CustomTabActivityHelper;
import ibs.ctdm.chromecustomtabpur.customtab.WebviewFallback;

public class MainActivity extends AppCompatActivity {

    private CustomTabActivityHelper mCustomTabActivityHelper;

    private String URL = "http://service.eternity.co.th/purchase_test/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setupCustomTabHelper();
        openCustomTab();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCustomTabActivityHelper.bindCustomTabsService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCustomTabActivityHelper.unbindCustomTabsService(this);
    }
    private void setupCustomTabHelper() {
        mCustomTabActivityHelper = new CustomTabActivityHelper();
        mCustomTabActivityHelper.setConnectionCallback(mConnectionCallback);
        mCustomTabActivityHelper.mayLaunchUrl(Uri.parse(URL), null, null);
    }

    private void openCustomTab() {

        //ตัวแปรนี้จะให้ในการกำหนดค่าต่างๆ ที่ข้างล่างนี้
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        //กำหนดสีของ Action bar
        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        //กำหนดชื่อบน Action bar
        intentBuilder.setShowTitle(true);
//        intentBuilder.enableUrlBarHiding();

        //กำหนดรูปให้ปุ่ม back, default จะเป็นปุ่มกากบาท
        intentBuilder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back)); //ควรไปทำใน background thread


        //สร้างปุ่มบน Option Menu item แบบมีรูป
        intentBuilder.setActionButton(BitmapFactory.decodeResource(getResources(), R.drawable.ic_share), "Share", createPendingShareIntent()); //ปุ่ม share ที่มี icon

        //สร้างปุ่มบน Option Menu item แบบไม่มีรูป
        PendingIntent menuItemPendingIntent = createPendingShareIntent();
        intentBuilder.addMenuItem("Share", menuItemPendingIntent);

        //กำหนดให้มี Animation เมื่อ Custom tab เข้ามาและออกไป ถ้าไม่มีจะเหมือน Activity ที่เด้งเข้ามาเลย
        setAnimation(intentBuilder);

        //Launch Custome tab ให้ทำงาน
        CustomTabActivityHelper.openCustomTab(
                this, intentBuilder.build(), Uri.parse(URL), new WebviewFallback());
    }

    private void setAnimation(CustomTabsIntent.Builder intentBuilder) {
        intentBuilder.setStartAnimations(this, android.R.anim.slide_out_right, android.R.anim.slide_in_left);
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    // You can use this callback to make UI changes
    private CustomTabActivityHelper.ConnectionCallback mConnectionCallback = new CustomTabActivityHelper.ConnectionCallback() {
        @Override
        public void onCustomTabsConnected() {
            Toast.makeText(MainActivity.this, "Connected to service", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCustomTabsDisconnected() {
            Toast.makeText(MainActivity.this, "Disconnected from service", Toast.LENGTH_SHORT).show();
        }
    };

    //method กำหนดว่าถ้าคลิกปุ่มแล้วให้ไปทำอะไร
    private PendingIntent createPendingShareIntent() {
        Intent actionIntent = new Intent(Intent.ACTION_SEND);
        actionIntent.setType("text/plain");
        actionIntent.putExtra(Intent.EXTRA_TEXT, "This is sharing some text");
        return PendingIntent.getActivity(getApplicationContext(), 0, actionIntent, 0);
    }

}
