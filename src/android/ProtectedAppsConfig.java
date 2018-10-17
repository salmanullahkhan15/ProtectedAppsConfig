package cordova.plugin.protectedappsconfig;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.content.ComponentName;
import android.provider.Settings;
import android.os.Build;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
/**
 * This class echoes a string called from JavaScript.
 */
public class ProtectedAppsConfig extends CordovaPlugin {
    private Context context;

    public void StartPowerSaverIntent(CallbackContext callbackContext) {

        PluginResult.Status status = PluginResult.Status.OK;

        for (MyModel device : DevicesArray()) {
            Intent intent = new Intent();
            intent.setClassName(device.getPackageName(), device.getPackageName() + "." + device.getClassName());
            if (isCallable(intent)) {
                callbackContext.sendPluginResult(new PluginResult(status, device.getPackageName() + "/." + device.getClassName()));
            }
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        context = this.cordova.getActivity().getApplicationContext();
        PluginResult.Status status = PluginResult.Status.OK;
        Uri packageUri = Uri.parse("package:" + this.cordova.getActivity().getPackageName());
        String result = "";

        action = args.getString(0);
        boolean check = args.getBoolean(1);
        Intent intent = null;

        if (check) {
            openSettings(action);
        } else {
            StartPowerSaverIntent(callbackContext);
        }

        return true;
    }


    private void openSettings(String activityName) {
        try {
            String cmd = "am start -n " + activityName;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                cmd += " --user " + getUserSerial();
            }
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ignored) {
        }
    }

    private String getUserSerial() {
        //noinspection ResourceType
        Object userManager = context.getSystemService("user");
        if (null == userManager) return "";

        try {
            Method myUserHandleMethod = android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
            Object myUserHandle = myUserHandleMethod.invoke(android.os.Process.class, (Object[]) null);
            Method getSerialNumberForUser = userManager.getClass().getMethod("getSerialNumberForUser", myUserHandle.getClass());
            Long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);
            if (userSerial != null) {
                return String.valueOf(userSerial);
            } else {
                return "";
            }
        } catch (Exception e) {
            Log.e("sds", "getUserSerial exception:" + e.getMessage());
        }
        return "";
    }


    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    class MyModel {
        private String packageName;
        private String className;

        public MyModel(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }

    private List<MyModel> DevicesArray() {

        List<MyModel> listOfDevices = new ArrayList<>();

        MyModel letvsafe = new MyModel("com.letv.android.letvsafe", "AutobootManageActivity");
        listOfDevices.add(letvsafe);
        MyModel huawei = new MyModel("com.huawei.systemmanager", "optimize.process.ProtectActivity");
        listOfDevices.add(huawei);
        MyModel huawei1 = new MyModel("com.huawei.systemmanager", "appcontrol.activity.StartupAppControlActivity");
        listOfDevices.add(huawei1);
        MyModel coloros = new MyModel("com.coloros.safecenter", "permission.startup.StartupAppListActivity");
        listOfDevices.add(coloros);
        MyModel oppo = new MyModel("com.coloros.safecenter", "startupapp.StartupAppListActivity");
        listOfDevices.add(oppo);
        MyModel safe = new MyModel("com.oppo.safe", "permission.startup.StartupAppListActivity");
        listOfDevices.add(safe);
        MyModel secure = new MyModel("com.iqoo.secure", "ui.phoneoptimize.AddWhiteListActivity");
        listOfDevices.add(secure);
        MyModel iqoo = new MyModel("com.iqoo.secure", "ui.phoneoptimize.BgStartUpManager");
        listOfDevices.add(iqoo);
        MyModel vivo = new MyModel("com.vivo.permissionmanager", "activity.BgStartUpManagerActivity");
        listOfDevices.add(vivo);
        MyModel htc = new MyModel("com.htc.pitroad", "landingpage.activity.LandingPageActivity");
        listOfDevices.add(htc);
        MyModel asus = new MyModel("com.asus.mobilemanager", "MainActivity");
        listOfDevices.add(asus);
        return listOfDevices;
    }


}
