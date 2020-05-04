package com.modosa.switchnightui.service.tiles;

import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.annotation.RequiresApi;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.OpUtil;
import com.modosa.switchnightui.util.SwitchBatterySaverUtil;

/**
 * @author dadaewq
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class SwitchBatterySaverTile extends TileService {

    private SwitchBatterySaverUtil switchBatterySaverUtil;

    @Override
    public void onStartListening() {
        super.onStartListening();
        refreshState();
    }

    @Override
    public void onClick() {
        super.onClick();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switchForceDark();
            refreshState();
        } else {
            OpUtil.showTipNeedSdk(this, R.string.title_battery_saver, "5.0");
        }
    }

    private void switchForceDark() {
        refreshUtil();
        switchBatterySaverUtil.switchBatterySaverWithResult();
    }

    private void refreshState() {
        refreshUtil();
        Tile qsTile = getQsTile();
        try {
            if (switchBatterySaverUtil.isPowerSaveMode()) {
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
        if (switchBatterySaverUtil == null) {
            switchBatterySaverUtil = new SwitchBatterySaverUtil(this);
        }
    }
}
