package cn.gisdata.footprint.module.map.func.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.zx.zxutils.util.ZXDialogUtil;
import com.zx.zxutils.util.ZXPermissionUtil;
import com.zx.zxutils.util.ZXStringUtil;

public class GpsUtil {

    private static double EARTH_RADIUS = 6378.137;
    private static MapView mapView;
    private static Activity mAtivity;
    private static boolean isLocationChanged = false;
    private static final double GPS_SCALE = 30000;
    private static LocationDisplayManager locationDisplayManager;
    private static String TAG = GpsUtil.class.getName();
    private static LocationManager locationManager;

    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    public static void location(MapView mMapView, Activity activity) {
        mapView = mMapView;
        mAtivity = activity;
        isLocationChanged = true;
        if (mMapView.isLoaded()) {
            locationDisplayManager = mMapView.getLocationDisplayManager();
            if (!locationDisplayManager.isStarted()) {
                locationDisplayManager.setAllowNetworkLocation(true);
                locationDisplayManager.setLocationListener(locationListener);
                locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.OFF);
                locationDisplayManager.start();
            } else {
                locationDisplayManager.stop();
            }
        }
    }

    public static LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            if (!ZXStringUtil.isEmpty(provider)) {
                if (isLocationChanged) {
                    Location location = locationDisplayManager.getLocation();
                    if (location == null) {
                        GpsUtil.changeGPSMode(mAtivity);
                    }
                }
            }
        }

        @Override
        public void onLocationChanged(Location location) {
            if (null != location && isLocationChanged) {
                Point point = new Point(location.getLongitude(), location.getLatitude());
                mapView.zoomToScale(point, GPS_SCALE);
                isLocationChanged = false;
            }
        }
    };

    /**
     * 切换手机GPS模式
     */
    public static void changeGPSMode(final Activity mActivity) {
        ZXDialogUtil.showYesNoDialog(mActivity, "系统提示", "定位失败，请切换定位模式后再试！", (dialogInterface, i) -> {
            // 转到手机设置界面，用户设置GPS
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mActivity.startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
        });
    }

    /**
     * 判断手机GPS是否开启
     *
     * @param
     * @return
     */
    public static boolean checkGpsStatus(Activity activity) {
        if (!ZXPermissionUtil.checkPermissionsByArray(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})) {
            ZXPermissionUtil.requestPermissionsByArray(activity);
            return false;
        }
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        //通过GPS卫星定位,定位级别到街
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //通过WLAN或者移动网络确定位置
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        } else {
            openGPS(activity);
            return false;
        }
    }

    /**
     * 开启手机GPS
     */
    private static void openGPS(final Activity mActivity) {
        ZXDialogUtil.showYesNoDialog(mActivity, "提示", "GPS未打开，请打开GPS！", (dialogInterface, i) -> {
            // 转到手机设置界面，用户设置GPS
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mActivity.startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
        });
    }


    /**
     * 判断手机GPS是否开启
     *
     * @param
     * @return
     */
    public static boolean isOpen(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //通过GPS卫星定位,定位级别到街
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //通过WLAN或者移动网络确定位置
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * GPS功能已经打开-->根据GPS去获取经纬度
     */
    public static Location getLocation(Context context) {
        Location location = null;
        if (isOpen(context)) {
            String[] strArr = null;
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, mListener);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mListener);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                if (location != null) {
//                    double latitude = location.getLatitude();
//                    double longitude = location.getLongitude();
//                    strArr = new String[]{String.valueOf(latitude), String.valueOf(longitude)};
                } else {
                    Toast.makeText(context, "获取经纬度信息失败！", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            openGPS((Activity) context);
        }
        return location;

    }

    public static LocationListener mListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            if (null != location) {


            }
        }
    };

}
