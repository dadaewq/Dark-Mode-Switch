package com.modosa.switchnightui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.LocationUtil;
import com.modosa.switchnightui.util.MyTimePickerDialog;
import com.modosa.switchnightui.util.OpUtil;
import com.modosa.switchnightui.util.SpUtil;
import com.modosa.switchnightui.util.TimingSwitchUtil;

import java.util.Calendar;


/**
 * @author dadaewq
 */
@SuppressWarnings("ConstantConditions")
public class TimingSwitchFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
    private final boolean isSupportForceDark = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    private Context context;
    private SpUtil spUtil;
    private TimingSwitchUtil timingSwitchUtil;
    private MyTimePickerDialog timePickerDialog;
    private AlertDialog alertDialog;
    private SwitchPreferenceCompat enableTimingSwitch;
    private Preference timeon;
    private Preference timeoff;
    private SwitchPreferenceCompat enableTimingSwitch2;
    private Preference timeon2;
    private Preference timeoff2;
    private LocationUtil locationUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_timingswitch, rootKey);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == 665) {
            updateSunRiseAndSunSet();
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void init() {
        spUtil = new SpUtil(context);
        timingSwitchUtil = new TimingSwitchUtil(context);
        locationUtil = new LocationUtil(context);
        initView();
//        updateSunRiseAndSunSet();
    }

    private void updateSunRiseAndSunSet() {

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e("TAG", "onLocationChanged: ");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.e("TAG", "onStatusChanged: ");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.e("TAG", "onProviderEnabled: ");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.e("TAG", "onProviderDisabled: ");
            }
        };

        if (!locationUtil.isLocationEnabled()) {
            //no Network and GPS providers is enabled
            OpUtil.showToast0(context, R.string.tip_need_access_Location_info);
            locationUtil.openGpsSettings();
            return;
        }

        Location getLocation = locationUtil.getLocation(locationListener);
        Log.e("getLocation", "" + getLocation);

        if (getLocation == null) {
            OpUtil.showToast0(context, R.string.tip_fail_getLocation);
            return;
        }
        Calendar calendar = Calendar.getInstance();
        com.luckycatlabs.sunrisesunset.dto.Location location = new com.luckycatlabs.sunrisesunset.dto.Location(getLocation.getLatitude(), getLocation.getLongitude());

        SunriseSunsetCalculator sunriseSunsetCalculator = new SunriseSunsetCalculator(location, calendar.getTimeZone());
        String Sunrise = sunriseSunsetCalculator.getOfficialSunriseForDate(calendar);
        String Sunset = sunriseSunsetCalculator.getOfficialSunsetForDate(calendar);

        Log.e("Sunrise", ": " + Sunrise);
        Log.e("Sunset-", ": " + Sunset);

        TimingSwitchUtil timingSwitchUtil = new TimingSwitchUtil(context);
        SpUtil spUtil = new SpUtil(context);

        updateView(timeon, Sunset);
        updateView(timeoff, Sunrise);
        if (spUtil.getFalseBoolean(TimingSwitchUtil.ENABLE_TIMING_SWITCH)) {
            timingSwitchUtil.setAllSwitchAlarm();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            updateView(timeon2, Sunset);
            updateView(timeoff2, Sunrise);
            if (spUtil.getFalseBoolean(TimingSwitchUtil.ENABLE_TIMING_SWITCH2)) {
                timingSwitchUtil.setAllSwitchAlarm2();
            }
        }

        OpUtil.showToast0(context, R.string.tip_success_getLocation);
    }

    private void updateView(Preference preference, String time) {
        spUtil.putString(preference.getKey(), time);
        preference.setSummary(time);
    }

    private void initView() {
        timeon = findPreference(TimingSwitchUtil.SP_KEY_TIMEON);
        timeoff = findPreference(TimingSwitchUtil.SP_KEY_TIMEOFF);
        timeon.setSummary(spUtil.getString(TimingSwitchUtil.SP_KEY_TIMEON));
        timeoff.setSummary(spUtil.getString(TimingSwitchUtil.SP_KEY_TIMEOFF));
        timeon.setOnPreferenceClickListener(this);
        timeoff.setOnPreferenceClickListener(this);

        enableTimingSwitch = findPreference(TimingSwitchUtil.ENABLE_TIMING_SWITCH);
        assert enableTimingSwitch != null;
        enableTimingSwitch.setOnPreferenceClickListener(v -> {
            if (enableTimingSwitch.isChecked()) {
                timingSwitchUtil.setAllSwitchAlarm();
            }
            TimingSwitchFragment.this.setTimeViewState(enableTimingSwitch.isChecked());
            return true;
        });

        if (isSupportForceDark) {
            findPreference("TimingSwitch2").setVisible(true);
            timeon2 = findPreference(TimingSwitchUtil.SP_KEY_TIMEON2);
            timeoff2 = findPreference(TimingSwitchUtil.SP_KEY_TIMEOFF2);
            timeon2.setSummary(spUtil.getString(TimingSwitchUtil.SP_KEY_TIMEON2));
            timeoff2.setSummary(spUtil.getString(TimingSwitchUtil.SP_KEY_TIMEOFF2));
            timeon2.setOnPreferenceClickListener(this);
            timeoff2.setOnPreferenceClickListener(this);

            enableTimingSwitch2 = findPreference(TimingSwitchUtil.ENABLE_TIMING_SWITCH2);
            assert enableTimingSwitch2 != null;
            enableTimingSwitch2.setOnPreferenceClickListener(v -> {
                if (enableTimingSwitch2.isChecked()) {
                    timingSwitchUtil.setAllSwitchAlarm2();
                }
                TimingSwitchFragment.this.setTimeViewState2(enableTimingSwitch2.isChecked());
                return true;
            });
        }
        refresh();

    }


    private void refresh() {
        boolean isEnable = spUtil.getFalseBoolean(TimingSwitchUtil.ENABLE_TIMING_SWITCH);
        enableTimingSwitch.setChecked(isEnable);
        setTimeViewState(isEnable);
        if (isSupportForceDark) {
            boolean isEnable2 = spUtil.getFalseBoolean(TimingSwitchUtil.ENABLE_TIMING_SWITCH2);
            enableTimingSwitch2.setChecked(isEnable2);
            setTimeViewState2(isEnable);
        }
    }

    private void setTimeViewState(boolean isEnable) {
        timeon.setEnabled(isEnable);
        timeoff.setEnabled(isEnable);
    }

    private void setTimeViewState2(boolean isEnable) {
        timeon2.setEnabled(isEnable);
        timeoff2.setEnabled(isEnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @SuppressLint("SetTextI18n")
    private void showTimePickerDialog(final Preference preference, final TimingSwitchFragment.CallBack callback) {

        String key = preference.getKey();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour, minute;
        if (spUtil.getString(key) == null) {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        } else {
            String[] time = spUtil.getString(key).split(":");
            hour = Integer.parseInt(time[0]);
            minute = Integer.parseInt(time[1]);
        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {

            final EditText timeEditText = new EditText(context);
            timeEditText.setText(hour + ":" + minute);
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(TimingSwitchUtil.SP_KEY_TIMEOFF.equals(key) ? R.string.title_timeup_off : R.string.title_timeup_on)
                    .setView(timeEditText)
                    .setNeutralButton(android.R.string.cancel, null)

                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        String timeText = timeEditText.getText().toString().trim();
                        if (!"".equals(timeText)) {
                            String[] times = timeText.replace("：", ":").split(":");
                            String h, m;

                            int hourOfDay = 22, minute12 = 0;
                            try {
                                hourOfDay = Integer.parseInt(times[0].trim());
                                minute12 = Integer.parseInt(times[1].trim());
                            } catch (Exception e) {
                                e.printStackTrace();
                                OpUtil.showToast1(context, "" + e);
                            }

                            if (hourOfDay >= 10) {
                                h = hourOfDay + "";
                            } else {
                                h = "0" + hourOfDay;
                            }
                            if (minute12 >= 10) {
                                m = minute12 + "";
                            } else {
                                m = "0" + minute12;
                            }
                            preference.setSummary(h + ":" + m);
                            spUtil.putString(key, h + ":" + m);
                            callback.set();
                        }
                    });

            alertDialog = builder.create();
            OpUtil.showAlertDialog(context, alertDialog);


        } else {
            timePickerDialog = new MyTimePickerDialog(context, (view, hourOfDay, minute1) -> {
                String h;
                String m;

                if (hourOfDay >= 10) {
                    h = hourOfDay + "";
                } else {
                    h = "0" + hourOfDay;
                }
                if (minute1 >= 10) {
                    m = minute1 + "";
                } else {
                    m = "0" + minute1;
                }
                preference.setSummary(h + ":" + m);
                spUtil.putString(key, h + ":" + m);
                callback.set();

            }, hour, minute, true);

            timePickerDialog.show();
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timePickerDialog != null) {
            timePickerDialog.dismiss();
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        int nightModeOrForceDark = -1;

        switch (preference.getKey()) {
            case TimingSwitchUtil.SP_KEY_TIMEON:
                nightModeOrForceDark = UiModeManager.MODE_NIGHT_YES;
                break;
            case TimingSwitchUtil.SP_KEY_TIMEOFF:
                nightModeOrForceDark = UiModeManager.MODE_NIGHT_NO;
                break;
            case TimingSwitchUtil.SP_KEY_TIMEON2:
                nightModeOrForceDark = 22;
                break;
            case TimingSwitchUtil.SP_KEY_TIMEOFF2:
                nightModeOrForceDark = 11;
                break;
            default:
        }

        Log.e("nightModeOrForceDark", "" + nightModeOrForceDark);
        if (nightModeOrForceDark != -1) {
            int finalNightModeOrForceDark = nightModeOrForceDark;
            showTimePickerDialog(preference, () -> timingSwitchUtil.setSingleSwitchAlarm(finalNightModeOrForceDark));

        }

        return false;
    }

    interface CallBack {
        /**
         * 设置好时间后开始设置定时任务
         */
        void set();
    }

}


