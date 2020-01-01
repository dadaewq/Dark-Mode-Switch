package com.modosa.switchnightui.service;

import android.app.UiModeManager;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.modosa.switchnightui.CheckUtil;
import com.modosa.switchnightui.R;

import java.util.Objects;

/**
 * @author dadaewq
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class changeUI1 extends TileService {

    private final int yes = UiModeManager.MODE_NIGHT_YES;
    private final int no = UiModeManager.MODE_NIGHT_NO;

    @Override
    public void onStartListening() {
        refreshState();
        showState();
        super.onStartListening();
    }

    @Override
    public void onClick() {
        super.onClick();

        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);

        new CheckUtil(this, uiModeManager).check();
//        if (new CheckUtil(this, uiModeManager).check()) {
        int i = yes;
        if (Objects.requireNonNull(uiModeManager).getNightMode() == yes) {
            i = no;
        }
        uiModeManager.setNightMode(i);

        if (((UiModeManager) Objects.requireNonNull(getSystemService(UI_MODE_SERVICE))).getNightMode() != i) {
            Toast.makeText(this, getString(R.string.no1), Toast.LENGTH_SHORT).show();
        }
//        } else {
//            Toast.makeText(this, getString(R.string.no1), Toast.LENGTH_SHORT).show();
//        }

        refreshState();
        showState();

    }


    private void showState() {
        Tile qsTile = getQsTile();
        if (qsTile.getState() == yes) {
            qsTile.setLabel(getString(R.string.NightUI));
        } else {
            qsTile.setLabel(getString(R.string.NonNightUI));
        }
        qsTile.updateTile();
    }

    private void refreshState() {
        Tile qsTile = getQsTile();
        if (((UiModeManager) Objects.requireNonNull(getSystemService(UI_MODE_SERVICE))).getNightMode() == yes) {
            qsTile.setState(yes);
        } else {
            qsTile.setState(no);
        }
        qsTile.updateTile();
    }
}
