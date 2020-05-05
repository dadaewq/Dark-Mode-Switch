package com.modosa.switchnightui.service.tiles;

import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.annotation.RequiresApi;

import com.modosa.switchnightui.util.OpUtil;
import com.modosa.switchnightui.util.SwitchDisplayUtil;

/**
 * @author dadaewq
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public abstract class AbstractSwitchDisplayTile extends TileService {
    String key;
    int shortcutLongLabelId;
    private SwitchDisplayUtil switchDisplayUtil;

    @Override
    public void onStartListening() {
        super.onStartListening();
        refreshState();
    }

    @Override
    public void onClick() {
        super.onClick();
        switchMethod();
        refreshState();
    }

    private void switchMethod() {
        refreshUtil();
        switchDisplayUtil.switchDisplayKeyWithResult(key, getString(shortcutLongLabelId));
    }

    private void refreshState() {
        refreshUtil();
        Tile qsTile = getQsTile();
        try {
            if (isShouldActive()) {
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
        if (switchDisplayUtil == null) {
            switchDisplayUtil = new SwitchDisplayUtil(this);
        }
    }

    private boolean isShouldActive() {
        return switchDisplayUtil.isDisplayKeyEnabled(key);
    }
}
