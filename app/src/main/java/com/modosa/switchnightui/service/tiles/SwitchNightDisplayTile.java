package com.modosa.switchnightui.service.tiles;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.OpUtil;
import com.modosa.switchnightui.util.SwitchDisplayUtil;

/**
 * @author dadaewq
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class SwitchNightDisplayTile extends AbstractSwitchDisplayTile {

    public SwitchNightDisplayTile() {
        key = SwitchDisplayUtil.NIGHT_DISPLAY_ACTIVATED;
        shortcutLongLabelId = R.string.title_night_display;
    }

    @Override
    public void onClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            super.onClick();
        } else {
            OpUtil.showTipNeedSdk(this, R.string.title_night_display, "7.1");
        }
    }
}
