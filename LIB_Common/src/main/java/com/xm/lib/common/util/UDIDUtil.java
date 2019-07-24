package com.xm.lib.common.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.util.UUID;

public class UDIDUtil {

    private static final String PREFS_FILE = "device_id.xml";
    private static final String PREFS_DEVICE_ID = "device_id";
    private static UUID uuid;

    /**
     * 获取设备ID
     *
     * @return 设备唯一标识
     */
    public static String getUDID(Context context) {
        return uuid(context);
    }

    private static String uuid(final Context cxt) {
        if (uuid == null) {
            final SharedPreferences prefs = cxt.getSharedPreferences(PREFS_FILE, 0);
            final String id = prefs.getString(PREFS_DEVICE_ID, null);

            if (id != null) {
                uuid = UUID.fromString(id);
            } else {
                @SuppressLint("HardwareIds") final String androidID = Settings.Secure.getString(cxt.getContentResolver(), Settings.Secure.ANDROID_ID);

                try {
                    //部分厂商定制出现相同的 ANDROID_ID：9774d56d682e549c
                    if (!"9774d56d682e549c".equals(androidID)) {
                        uuid = UUID.nameUUIDFromBytes(androidID.getBytes("utf8"));
                    } else {
                        if (SDKVersionUtil.INSTANCE.hasM()) {
                            uuid = UUID.randomUUID();
                        } else {
                            if (ActivityCompat.checkSelfPermission(cxt, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                uuid = UUID.randomUUID();
                            }
                            @SuppressLint("HardwareIds") final String deviceID = ((TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                            uuid = deviceID != null ? UUID.nameUUIDFromBytes(deviceID.getBytes("utf8")) : UUID.randomUUID();
                        }
                    }
                } catch (Exception e) {
                    uuid = UUID.randomUUID();
                }

                prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).apply();
            }
        }

        return uuid.toString().replace("-", "");
    }
}
