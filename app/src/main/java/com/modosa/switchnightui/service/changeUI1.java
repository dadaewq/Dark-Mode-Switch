package com.modosa.switchnightui.service;

import android.app.UiModeManager;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

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
        super.onStartListening();
        refreshState();
    }

    @Override
    public void onClick() {
        super.onClick();

        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);

        int i = yes;
        if (Objects.requireNonNull(uiModeManager).getNightMode() == yes) {
            i = no;
        }
        uiModeManager.setNightMode(i);

        if (((UiModeManager) Objects.requireNonNull(getSystemService(UI_MODE_SERVICE))).getNightMode() != i) {
            Toast.makeText(this, getString(R.string.no1), Toast.LENGTH_SHORT).show();
        }

        refreshState();
    }

    private void refreshState() {
        Tile qsTile = getQsTile();
        try {
            if (((UiModeManager) (Objects.requireNonNull(getSystemService(UI_MODE_SERVICE)))).getNightMode() == yes) {
                qsTile.setState(Tile.STATE_ACTIVE);
            } else {
                qsTile.setState(Tile.STATE_INACTIVE);
            }
            qsTile.updateTile();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e + "", Toast.LENGTH_SHORT).show();
        }

    }
}
