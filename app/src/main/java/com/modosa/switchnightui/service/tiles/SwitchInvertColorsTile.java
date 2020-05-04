package com.modosa.switchnightui.service.tiles;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.SwitchDisplayUtil;

/**
 * @author dadaewq
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class SwitchInvertColorsTile extends AbstractSwitchDisplayTile {

    public SwitchInvertColorsTile() {
        key = SwitchDisplayUtil.ACCESSIBILITY_DISPLAY_INVERSION_ENABLED;
        shortcutLongLabelId = R.string.title_invert_colors;
    }
}
