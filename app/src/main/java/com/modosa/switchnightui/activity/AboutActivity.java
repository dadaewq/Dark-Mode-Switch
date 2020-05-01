package com.modosa.switchnightui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.drakeet.about.AbsAboutActivity;
import com.drakeet.about.Card;
import com.drakeet.about.Category;
import com.drakeet.about.Contributor;
import com.drakeet.about.License;
import com.modosa.switchnightui.BuildConfig;
import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.OpUtil;

import java.util.List;

/**
 * @author dadaewq
 */
public class AboutActivity extends AbsAboutActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.alertdialog_background);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.rate) {
            OpUtil.launchCustomTabsUrl(this, "https://www.coolapk.com/apk/com.modosa.switchnightui");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreateHeader(@NonNull ImageView icon, @NonNull TextView slogan, @NonNull TextView version) {
        icon.setImageResource(R.mipmap.ic_launcher);
        slogan.setText(getString(R.string.app_name));
        version.setText("v" + BuildConfig.VERSION_NAME + "（" + BuildConfig.VERSION_CODE + "）");
    }

    @Override
    protected void onItemsCreated(@NonNull List<Object> items) {
        items.add(new Category(getString(R.string.title_Introduction)));
        items.add(new Card(getString(R.string.desc_Introduction)));

        items.add(new Category(getString(R.string.title_developer)));
        items.add(new Contributor(R.drawable.avatar_dadaewq_circle_middle, "dadaewq", getString(R.string.desc_developer), "http://www.coolapk.com/u/460110"));

        items.add(new Category(getString(R.string.title_certification)));
        items.add(new Contributor(R.mipmap.ic_green_apps, getString(R.string.title_green_apps), "https://green-android.org", "https://green-android.org"));


        items.add(new Category(getString(R.string.title_other_works)));
        items.add(new Contributor(R.mipmap.ic_install_lion, getString(R.string.name_install_lion), getString(R.string.desc_install_lion), "https://www.coolapk.com/apk/com.modosa.apkinstaller"));


        items.add(new Category(getString(R.string.title_licenses)));
        items.add(new License("Dark-Mode-Switch", "dadaewq", getString(R.string.License_MulanPSL_2), "https://github.com/dadaewq/Dark-Mode-Switch"));
        items.add(new License("MultiType", "drakeet", License.APACHE_2, "https://github.com/drakeet/MultiType"));
        items.add(new License("about-page", "drakeet", License.APACHE_2, "https://github.com/drakeet/about-page"));
        items.add(new License("AndroidX", "Google", License.APACHE_2, "https://source.google.com"));
        items.add(new License("material-components-android", "Google", License.APACHE_2, "https://github.com/material-components/material-components-android"));
        items.add(new License("AndroidUtilCode", "Blankj", License.APACHE_2, "https://github.com/Blankj/AndroidUtilCode"));
        items.add(new License("sunrisesunsetlib-java", "mikereedell", License.APACHE_2, "https://github.com/mikereedell/sunrisesunsetlib-java"));
    }
}
