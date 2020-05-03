package com.modosa.switchnightui.service.tiles;

import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.annotation.RequiresApi;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.OpUtil;
import com.modosa.switchnightui.util.SwitchForceDarkUtil;


/**
 * @author dadaewq
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class SwitchForceDarkTile extends TileService {

    private SwitchForceDarkUtil switchForceDarkUtil;


    @Override
    public void onStartListening() {
        super.onStartListening();
        refreshState();
    }

    @Override
    public void onClick() {
        super.onClick();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            switchForceDark();
            refreshState();
        } else {
            OpUtil.showToast1(this, String.format(
                    getString(R.string.tip_switch_what1_need_android_what2),
                    getString(R.string.title_force_dark),
                    "10"
            ));
        }
    }

    private void switchForceDark() {
        refreshUtil();
        switchForceDarkUtil.switchForceDark();
    }

    private void refreshState() {
        refreshUtil();
        Tile qsTile = getQsTile();
        try {
            if (switchForceDarkUtil.isForceDark()) {
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
        if (switchForceDarkUtil == null) {
            switchForceDarkUtil = new SwitchForceDarkUtil(this);
        }
    }
}
