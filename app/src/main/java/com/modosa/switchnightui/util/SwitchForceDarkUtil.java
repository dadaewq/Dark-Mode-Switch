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
    private final boolean isOneplus;


    public SwitchForceDarkUtil(Context context) {
        this.context = context;
        isOneplus = OpSwitchForceDarkUtil.isOnePlus(context);
    }

    public boolean isForceDark() {
        if (isOneplus) {
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
        if (isOneplus) {
            OpSwitchForceDarkUtil opSwitchForceDarkUtil = new OpSwitchForceDarkUtil(context);
            isSu = opSwitchForceDarkUtil.setForceDark(enable);
            //也加入普通的切换，增加覆盖
            setNormalForceDark(enable);

            isForceDark = opSwitchForceDarkUtil.isForceDark();
        } else {
            isSu = setNormalForceDark(enable);

            isForceDark = isForceDark();
        }
        if (!isSu) {
            msg = context.getString(R.string.no_root) + "\n";
        }

        if (isForceDark) {
            OpUtil.showToast0(context, msg + context.getString(R.string.tip_on_force_dark));
        } else {
            OpUtil.showToast0(context, msg + context.getString(R.string.tip_off_force_dark));
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
