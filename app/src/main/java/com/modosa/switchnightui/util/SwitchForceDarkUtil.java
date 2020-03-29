package com.modosa.switchnightui.util;

import android.content.Context;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.oneplus.OpSwitchForceDarkUtil;

/**
 * @author dadaewq
 */
public class SwitchForceDarkUtil {
    private static final String CMD_GET_FORCE_DARK = "getprop debug.hwui.force_dark";
    private static final String CMD_SET_FORCE_DARK = "setprop debug.hwui.force_dark ";
    private final Context context;


    public SwitchForceDarkUtil(Context context) {
        this.context = context;
    }

    public boolean isForceDark() {
        if (OpSwitchForceDarkUtil.isOnePlus(context)) {
            return OpSwitchForceDarkUtil.isOnePlusForceDark(context);
        } else {
            String[] result = ShellUtil.exec(CMD_GET_FORCE_DARK, false);
            return Boolean.parseBoolean(result[0]);
        }

    }

    public void setForceDark(int forceDark) {
        if (forceDark == 22) {
            setForceDark(true);
        } else if (forceDark == 11) {
            setForceDark(false);
        }
    }

    /**
     * Set Force Dark
     *
     * @param enable is enable force dark
     */
    private void setForceDark(boolean enable) {

        String msg = "";
        boolean isSu, isForceDark;
        if (OpSwitchForceDarkUtil.isOnePlus(context)) {
            OpSwitchForceDarkUtil opSwitchForceDarkUtil = new OpSwitchForceDarkUtil(context);
            isSu = opSwitchForceDarkUtil.setForceDark(enable);
            if (!isSu) {
                OpUtil.showToast0(context, R.string.no_root);
            }
            isForceDark = opSwitchForceDarkUtil.isForceDark();
        } else {
            isSu = setNormalForceDark(enable);
            if (!isSu) {
                msg = context.getString(R.string.no_root) + "\n";
            }
            isForceDark = isForceDark();
        }

        if (isForceDark) {
            OpUtil.showToast0(context, msg + context.getString(R.string.ForceDarkOn));
        } else {
            OpUtil.showToast0(context, msg + context.getString(R.string.ForceDarkOff));
        }

    }

    public void switchForceDark() {
        setForceDark(!isForceDark());
    }

    private boolean setNormalForceDark(boolean enable) {
        String[] checkRoot = ShellUtil.execWithRoot("exit");

        if ("0".equals(checkRoot[3])) {
            ShellUtil.execWithRoot(CMD_SET_FORCE_DARK + enable);
            return true;
        } else {
            ShellUtil.exec(CMD_SET_FORCE_DARK + enable, false);
            return false;
        }

    }


}
