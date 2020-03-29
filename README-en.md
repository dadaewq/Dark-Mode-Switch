## Dark Mode Switch

### [中文](https://github.com/dadaewq/Dark-Mode-Switch/blob/master/README.md)

This application is designed to switch the dark/night/ theme/UI of the Android system. (Supports timing switching)

This switch is the night mode in the Android9.0 developer options and the dark theme that began in Android10. This application supports Android4.1 +.


##### Android 10 and above can use **Root** to try to switch the forced dark, which is equivalent to the developer options "Override force-dark", MIUI's "Global Inverse Color" and other switches. (Adapted to OnePlus)

We provides 3 equivalent methods to switch the dark mode:

##### Method 1 Its availability is determined by the system

##### Method 2 requires authorization

` adb shell pm grant com.modosa.switchnightui android.permission.WRITE_SECURE_SETTINGS `

##### Method 3 requires Root

We provides tiles, widgets, static shortcuts,pinned shortcuts and setting assistant applications to quickly switch. The switching method follows the main interface selection.

## Download
[<img src="https://github.com/dadaewq/Dark-Mode-Switch/raw/master/app/src/main/ic_launcher-web.png"
     alt="Get it on CoolApk"
     height="80">](https://www.coolapk.com/apk/com.modosa.switchnightui)


#### Matters need attention:

OPPO and other devices may be automatically turned off by the system after turning on the dark mode. Only then need to turn on **stable mode** to switch. Clicking the refresh in the stable mode can make this APP theme follow the system. According to feedback, some OPPO devices will automatically turn off the dark mode after the screen is locked. This is temporarily unsolved. Android 10 will force the system to enter the dark mode when the power saving mode is turned on. At this time, the application cannot turn off the dark mode.

(If the device has a Android core patch, methods 1, 2, and 3 can be used directly without additional operations.)

##### About method 1：

Part of the system limits method 1, we provide two methods that require **Root** to remove this restriction:

(1) Move this application to `/system/priv-app/` and reboot the device.

(2) Need to modify a bool value of resources in **framework-res.apk**, open the apk to modify the bool in **resources.arsc**, find **config_lockDayNightMode**, modify it to false, replace the original Apk, and reboot the device.
(This has the advantage that at the same time you can also use some previous applications that could not be switched, because this operation is equivalent to canceling the system lock on modifying the night theme.)
