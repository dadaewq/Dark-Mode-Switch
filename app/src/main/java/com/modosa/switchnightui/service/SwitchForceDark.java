package com.modosa.switchnightui.service;

import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.uitl.SwitchUtil;

/**
 * @author dadaewq
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class SwitchForceDark extends TileService {

    private SwitchUtil switchUtil;

    @Override
    public void onStartListening() {
        super.onStartListening();

        switchUtil = new SwitchUtil(this, null);
        refreshState();
    }

    @Override
    public void onClick() {
        super.onClick();

        switchForceDark(switchUtil);
        refreshState();
    }

    private void switchForceDark(SwitchUtil switchUtil) {
        boolean isSu = switchUtil.switchForceDark();
        String msg = isSu ? "" : (getString(R.string.no_root) + "\n");
        boolean isForceDark = switchUtil.isForceDark();

        if (isForceDark) {
            switchUtil.showToast(msg + getString(R.string.ForceDarkOn));
        } else {
            switchUtil.showToast(msg + getString(R.string.ForceDarkOff));
        }
    }

    private void refreshState() {
        Tile qsTile = getQsTile();
        try {
            if (switchUtil.isForceDark()) {
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
