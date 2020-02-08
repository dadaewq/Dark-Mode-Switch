package com.modosa.switchnightui.service;

import android.app.UiModeManager;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.uitl.SpUtil;
import com.modosa.switchnightui.uitl.SwitchUtil;
import com.modosa.switchnightui.uitl.WriteSettingsUtil;

import java.util.Objects;

/**
 * @author dadaewq
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class SwitchUI extends TileService {
    private SpUtil spUtil;
    private SwitchUtil switchUtil;

    @Override
    public void onStartListening() {
        super.onStartListening();
        refreshState();
    }

    @Override
    public void onClick() {
        super.onClick();

        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        spUtil = new SpUtil(this);
        switchUtil = new SwitchUtil(this, uiModeManager);

        int want = WriteSettingsUtil.YES;
        if (Objects.requireNonNull(uiModeManager).getNightMode() == WriteSettingsUtil.YES) {
            want = WriteSettingsUtil.NO;
        }
        switchui(want);

        refreshState();
    }

    private void switchui(int want) {
        String msg = "";
        int method = spUtil.getMethod();
        if (method == 2) {
            if (WriteSettingsUtil.isNightMode(this)) {
                switchUtil.switch2(WriteSettingsUtil.NO);
            } else {
                switchUtil.switch2(WriteSettingsUtil.YES);
            }
            return;
        } else if (method == 3) {
            if (switchUtil.switch3(want)) {
                msg = getString(R.string.no_root);
            }
        } else {
            if (switchUtil.switch1(want)) {
                msg = String.format(getString(R.string.failmethod), "1");
            }
        }
        if (!"".equals(msg)) {
            switchUtil.showToast(msg);
        }

    }

    private void refreshState() {
        Tile qsTile = getQsTile();
        try {
            if (((UiModeManager) (Objects.requireNonNull(getSystemService(UI_MODE_SERVICE)))).getNightMode() == WriteSettingsUtil.YES) {
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
