package com.modosa.switchnightui.service.tiles;

import android.content.res.Configuration;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.annotation.RequiresApi;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.OpUtil;
import com.modosa.switchnightui.util.SpUtil;
import com.modosa.switchnightui.util.SwitchBatterySaverUtil;
import com.modosa.switchnightui.util.SwitchDarkModeUtil;
import com.modosa.switchnightui.util.WriteSettingsUtil;

/**
 * @author dadaewq
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class SwitchDarkModeTile extends TileService {

    private SwitchDarkModeUtil switchDarkModeUtil;
    private SwitchBatterySaverUtil switchBatterySaverUtil;
    private boolean enable;
    private boolean isClick = false;

    @Override
    public void onStartListening() {
        super.onStartListening();
        refreshState();
    }

    @Override
    public void onClick() {
        super.onClick();
        isClick = true;
        switchDarkMode();
        refreshState();
    }

    private void switchDarkMode() {
        refreshUtil();
        if (new SpUtil(this).getWorkMode() == 2) {
            enable = !WriteSettingsUtil.isNightMode(this);
        } else {
            enable = !switchDarkModeUtil.isDarkMode();
        }
        switchDarkModeUtil.setDarkModeWithResult(enable);
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
            if (switchBatterySaverUtil.isPowerSaveMode()) {
                qsTile.setLabel(getString(R.string.title_dark_mode) + "*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    qsTile.setSubtitle(getString(R.string.title_battery_saver));
                }
            } else {
                qsTile.setLabel(getString(R.string.title_dark_mode));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    qsTile.setSubtitle(null);
                }
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
        if (switchBatterySaverUtil == null) {
            switchBatterySaverUtil = new SwitchBatterySaverUtil(this);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (isClick && new SpUtil(this).isStableMode()) {
            new SwitchDarkModeUtil(this).setDarkModeWithResult(enable);
        }
    }
}
