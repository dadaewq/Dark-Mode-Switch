package com.modosa.switchnightui.service;

import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.annotation.RequiresApi;

import com.modosa.switchnightui.util.OpUtil;
import com.modosa.switchnightui.util.SwitchDarkModeUtil;

/**
 * @author dadaewq
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class SwitchUi extends TileService {

    private SwitchDarkModeUtil switchDarkModeUtil;

    @Override
    public void onStartListening() {
        super.onStartListening();
        refreshState();
    }

    @Override
    public void onClick() {
        super.onClick();

        switchDarkMode();

        refreshState();
    }

    private void switchDarkMode() {
        refreshUtil();
        switchDarkModeUtil.switchDarkModeWithResult();
    }

    private void refreshState() {
        refreshUtil();
        Tile qsTile = getQsTile();
        try {
            if (switchDarkModeUtil.isDarkMode()) {
                qsTile.setState(Tile.STATE_ACTIVE);
            } else {
                qsTile.setState(Tile.STATE_INACTIVE);
            }
            qsTile.updateTile();
        } catch (Exception e) {
            e.printStackTrace();
            OpUtil.showToast0(this, e + "");
        }

    }

    private void refreshUtil() {
        if (switchDarkModeUtil == null) {
            switchDarkModeUtil = new SwitchDarkModeUtil(this);
        }
    }
}
