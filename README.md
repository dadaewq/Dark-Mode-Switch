## 深色模式切换

### [English](https://github.com/dadaewq/Dark-Mode-Switch/blob/master/README-en.md)

本应用旨在切换Android系统的 深色/夜间/黑暗/暗色 模式/主题/UI。（支持定时切换）

此开关就是Android9.0开发者选项中的夜间模式和Android10开始有的深色主题，本应用支持了Android4.1+。

##### Android 10及以上可用Root尝试切换强制深色，等同于开发者选项的"覆盖'强制启用 SmartDark功能'的设置"、MIUI的"全局反色"等开关。（已适配一加）

本应用提供3个等效方法来切换深色模式：

##### 方法1 可用性由系统决定

##### 方法2 需要授权

` adb shell pm grant com.modosa.switchnightui android.permission.WRITE_SECURE_SETTINGS `

##### 方法3 需要Root

应用提供磁贴、微件、Shortcut、快捷方式和设置助手应用来快捷切换，切换方法跟随主界面选择。

## 下载
[<img src="https://github.com/dadaewq/Dark-Mode-Switch/raw/master/app/src/main/ic_launcher-web.png"
     alt="Get it on CoolApk"
     height="80">](https://www.coolapk.com/apk/com.modosa.switchnightui)


#### 注意事项：

OPPO等设备开启深色模式后可能会被系统自动关闭，此时需要开启稳定模式以切换，稳定模式下点刷新可以让本APP主题跟随系统。据反馈，部分OPPO设备在锁屏后还是会自动关闭深色模式，这个暂时无解。Android10在打开省电模式时会强制系统进入深色模式，此时本应用无法关闭深色模式。

(如果设备有核心破解的话，方法1、2、3都可以直接用，无需其他多余的操作。)

##### 有关方法1：

部分系统限制了方法1，提供两种方法需要用到Root权限来解除这个限制：

(1)把本应用移动到`/system/priv-app/`下，重启设备即可。

(2)需要修改**framework-res.apk**中资源的一个bool值，打开apk修改resources.arsc中的bool，找到**config_lockDayNightMode**，修改为false后，替换原来的Apk，重启设备即可。
(这样做有一个好处就是同时你也可以用之前一些无法切换的应用了，因为这个操作相当于取消了系统对修改夜间主题的锁。)

