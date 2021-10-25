package com.modosa.switchnightui.util;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.oneplus.OpSwitchDarkModeUtil;

import java.util.Objects;

/**
 * @author dadaewq
 */
public class SwitchDarkModeUtil {

    private final UiModeManager uiModeManager;
    private final Context context;


    public SwitchDarkModeUtil(Context context) {
        this.context = context;
        this.uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
    }

    public static String getServiceNightCMD(int nightmode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return "cmd uimode night " + Shell.nightModeToStr(nightmode);
        } else {
            return Shell.SERVICE_CALL_UIMODE + nightmode;
        }
    }

    public boolean isDarkMode() {
        return Objects.requireNonNull(uiModeManager).getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }

    public void switchDarkModeWithResult() {
        if (new SpUtil(context).getWorkMode() == 2) {
            setDarkModeWithResult(!WriteSettingsUtil.isNightMode(context));
        } else {
            setDarkModeWithResult(!isDarkMode());
        }
    }

    private void setDarkModeWithTip(boolean enable) {
        String msg = setDarkMode(enable).getMsg();

        if (!TextUtils.isEmpty(msg)) {
            OpUtil.showToast1(context, msg);
        }
    }

    public void setDarkModeWithTip(int nightMode) {
        boolean enable = false;
        if (nightMode == UiModeManager.MODE_NIGHT_YES) {
            enable = true;
        }
        setDarkModeWithTip(enable);
    }

    public void setDarkModeWithResult(boolean enable) {
        ResultMsg resultMsg = setDarkMode(enable);
        String msg = resultMsg.getMsg();

        if (resultMsg.isSuccess()) {
            if (!"".equals(msg)) {
                msg = msg + "\n";
            }
            if (uiModeManager != null && uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
                msg += OpUtil.getTipStr1IsStr2(context, context.getString(R.string.title_dark_mode), context.getString(R.string.tip_on));
            } else {
                msg += OpUtil.getTipStr1IsStr2(context, context.getString(R.string.title_dark_mode), context.getString(R.string.tip_off));
            }
        }
        if (!TextUtils.isEmpty(msg)) {
            OpUtil.showToast0(context, msg);
        }

    }

    public void setDarkModeWithResult(int nightMode) {
        boolean enable = false;
        if (nightMode == UiModeManager.MODE_NIGHT_YES) {
            enable = true;
        }
        setDarkModeWithResult(enable);
    }

    private ResultMsg setDarkMode(boolean enable) {

        ResultMsg result = new ResultMsg();

        SpUtil spUtil = new SpUtil(context);

        int want = UiModeManager.MODE_NIGHT_NO;
        if (enable) {
            want = UiModeManager.MODE_NIGHT_YES;
        }

        String msg = "";

        int workMode = spUtil.getWorkMode();
        if (workMode == 2) {
            ResultMsg resultMsg = switch2(want);
            msg = resultMsg.getMsg();

            if (resultMsg.isSuccess() && resultMsg.getMsg() == null) {
                result.setSuccess();
                msg = "";
            }


        } else {
            result.setSuccess();
            if (workMode == 3) {
                if (switch3(want)) {
                    msg = context.getString(R.string.no_root);
                }
            } else {
                if (switch1(want)) {
                    msg = String.format(context.getString(R.string.failworkmode), context.getString(R.string.WorkMode1));
                }
            }
        }

        result.setMsg(msg);

        if (spUtil.getFalseBoolean("oneplus_synchronize_theme")) {
            new OpSwitchDarkModeUtil(context).setDarkMode(enable);
        }

        return result;

    }

    /**
     * Switch Dark Mode  WorkMode1
     *
     * @param nightmode nightmode
     * @return WorkMode1 cannot use
     */
    private boolean switch1(final int nightmode) {
        boolean av = true;
        opCarModeForOldSdk(UiModeManager.MODE_NIGHT_YES);
        uiModeManager.setNightMode(nightmode);
        opCarModeForOldSdk(nightmode);
        if (uiModeManager.getNightMode() == nightmode) {
            av = false;
        }
        return av;
    }

    /**
     * Switch Dark Mode  WorkMode1
     *
     * @param nightmode nightmode
     * @return ResultMsg
     */
    private ResultMsg switch2(int nightmode) {

        opCarModeForOldSdk(nightmode);
        ResultMsg resultMsg = WriteSettingsUtil.putKey(context, nightmode);

        if (resultMsg.isSuccess()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                int getCallState = TelephonyManager.CALL_STATE_IDLE;
                try {
                    getCallState = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getCallState();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (TelephonyManager.CALL_STATE_IDLE == getCallState) {
                    if (Configuration.UI_MODE_TYPE_CAR != uiModeManager.getCurrentModeType()) {
                        uiModeManager.enableCarMode(UiModeManager.ENABLE_CAR_MODE_ALLOW_SLEEP);
                    }
                    uiModeManager.disableCarMode(0);
                }
            }
        } else {
            if (resultMsg.getMsg().contains("WRITE_SECURE_SETTINGS")) {
                String cmd = "adb shell pm grant " + context.getPackageName() + " android.permission.WRITE_SECURE_SETTINGS";
                OpUtil.copyCmd(context, cmd);
                resultMsg.setMsg(String.format(context.getString(R.string.tip_needpermission), cmd));
            }

        }

        return resultMsg;
    }

    /**
     * Switch Dark Mode  WorkMode1
     *
     * @param nightmode nightmode
     * @return wether without su
     */
    private boolean switch3(int nightmode) {
        opCarModeForOldSdk(UiModeManager.MODE_NIGHT_YES);
        String cmd = getServiceNightCMD(nightmode);

        String[] checkRoot = ShellUtil.execWithRoot("exit");

        if ("0".equals(checkRoot[3])) {
            ShellUtil.execWithRoot(cmd);
            opCarModeForOldSdk(nightmode);
            return false;
        } else {
            ShellUtil.exec(cmd, false);
            opCarModeForOldSdk(nightmode);
            return true;
        }

    }

    private void opCarModeForOldSdk(int nightmode) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (nightmode == UiModeManager.MODE_NIGHT_YES) {
                uiModeManager.enableCarMode(2);
            } else {
                uiModeManager.disableCarMode(0);
            }
        }
    }

    private static class Shell {
        public static final String NIGHT_MODE_STR_YES = "yes";
        public static final String NIGHT_MODE_STR_NO = "no";
        public static final String NIGHT_MODE_STR_AUTO = "auto";
        public static final String NIGHT_MODE_STR_UNKNOWN = "unknown";
        public static final String SERVICE_CALL_UIMODE = "service call uimode 4 i32 ";

        private static String nightModeToStr(int mode) {
            switch (mode) {
                case UiModeManager.MODE_NIGHT_YES:
                    return NIGHT_MODE_STR_YES;
                case UiModeManager.MODE_NIGHT_NO:
                    return NIGHT_MODE_STR_NO;
                case UiModeManager.MODE_NIGHT_AUTO:
                    return NIGHT_MODE_STR_AUTO;
                default:
                    return NIGHT_MODE_STR_UNKNOWN;
            }
        }
    }
}
