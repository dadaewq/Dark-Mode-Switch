package com.modosa.switchnightui.service.tiles;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.SwitchDisplayUtil;


/**
 * @author dadaewq
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class SwitchGrayScaleTile extends AbstractSwitchDisplayTile {

    public SwitchGrayScaleTile() {
        key = SwitchDisplayUtil.GRAYSCALE;
        shortcutLongLabelId = R.string.title_gray_scale;
    }
}
