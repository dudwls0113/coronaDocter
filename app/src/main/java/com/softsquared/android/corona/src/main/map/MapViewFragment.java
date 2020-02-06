package com.softsquared.android.corona.src.main.map;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.ZoomControlView;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseFragment;
import com.softsquared.android.corona.src.CustomClinicDialog;
import com.softsquared.android.corona.src.CustomHospitalDialog;
import com.softsquared.android.corona.src.CustomPatientDialog;
import com.softsquared.android.corona.src.GpsTracker;
import com.softsquared.android.corona.src.main.map.interfaces.MapViewView;
import com.softsquared.android.corona.src.main.map.models.ClinicInfo;
import com.softsquared.android.corona.src.main.map.models.HospitalInfo;
import com.softsquared.android.corona.src.main.map.models.RouteRes;
import com.softsquared.android.corona.src.main.map.models.RouteResponse;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import static com.softsquared.android.corona.src.ApplicationClass.CAN_UPDATE_CLINIC;
import static com.softsquared.android.corona.src.ApplicationClass.CAN_UPDATE_HOSPITAL;
import static com.softsquared.android.corona.src.ApplicationClass.CAN_UPDATE_ROUTE;
import static com.softsquared.android.corona.src.ApplicationClass.sSharedPreferences;


public class MapViewFragment extends BaseFragment implements OnMapReadyCallback, MapViewView {

    Context mContext;
    ZoomControlView zoomControlView;
    private MapView mapView;
    NaverMap naverMap;
    ImageView mBtn, mBtn2, mBtn3, mBtn4;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    ArrayList<PathOverlay> pathOverlays = new ArrayList<>();
    ArrayList<RouteResponse> routeResponses = new ArrayList<>();
    ArrayList<Marker> markers = new ArrayList<>();

    private boolean route1 = true;
    private boolean route2 = false;
    private boolean route3 = false;
    private boolean route4 = true;

    ArrayList<ClinicInfo> clinicInfos = new ArrayList<>();
    ArrayList<Marker> markerClinic = new ArrayList<>();
    private boolean isFirstRoute2 = true;

    ArrayList<HospitalInfo> hospitalInfos = new ArrayList<>();
    ArrayList<Marker> markerHospital = new ArrayList<>();
    private boolean isFirstRoute3 = true;
    private boolean canUpdateRoute, canUpdateClinic, canUpdateHospital;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_map, container, false);
        mContext = getContext();
        setComponentView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        markers.clear();
        routeResponses.clear();
        clinicInfos.clear();
        mapView.getMapAsync(this);

//        SharedPreferences spf = sSharedPreferences;
//        canUpdateRoute = spf.getBoolean(CAN_UPDATE_ROUTE, true);
//        canUpdateClinic = spf.getBoolean(CAN_UPDATE_CLINIC, true);
//        canUpdateHospital = spf.getBoolean(CAN_UPDATE_HOSPITAL, true);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        } else {
            showCustomToast("알 수 없는 오류가 발생했습니다. 재접속을 부탁드립니다;");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void setComponentView(View v) {
        zoomControlView = v.findViewById(R.id.zoom);

        mBtn = v.findViewById(R.id.fragment_map_btn); // 감염자 경로
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (route1) {
                    for (int i = 0; i < markers.size(); i++) {
                        Log.d("map", "??개");
                        markers.get(i).setMap(null);
                    }
                    for (int i = 0; i < pathOverlays.size(); i++) {
                        pathOverlays.get(i).setMap(null);
                    }
//                    routeResponses.clear();
//                    markers.clear();
//                    pathOverlays.clear();
                    mBtn.setImageResource(R.drawable.ic_route1_off);
                    route1 = false;
                    mBtn4.setImageResource(R.drawable.ic_route4_off);
                    route4 = false;
                } else {
                    for (int i = 0; i < markers.size(); i++) {
                        markers.get(i).setMap(naverMap);
                    }
                    for (int i = 0; i < pathOverlays.size(); i++) {
                        pathOverlays.get(i).setMap(naverMap);
                    }
                    mBtn.setImageResource(R.drawable.ic_route1);
//                    getRoute();
                    route1 = true;
                    mBtn4.setImageResource(R.drawable.ic_route4);
                    route4 = true;
                }
            }
        });
        mBtn2 = v.findViewById(R.id.fragment_map_btn2); // 선별 진료소
        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (route2) {
                    mBtn2.setImageResource(R.drawable.ic_route2);
                    route2 = false;
                    for (int i = 0; i < markerClinic.size(); i++) {
                        markerClinic.get(i).setMap(null);
                    }
                } else {
                    mBtn2.setImageResource(R.drawable.ic_route2_on);
                    route2 = true;
                    showCustomToast("선별진료소란 일반환자와 선별하여 진료하고 유행 병원 외부에 설치된 음압진료소에서 별도로 진료를 시행하는 공간입니다.");
                    if (isFirstRoute2) {
                        getClinic();
                        isFirstRoute2 = false;
                    } else {
                        for (int i = 0; i < markerClinic.size(); i++) {
                            markerClinic.get(i).setMap(naverMap);
                        }
                    }
                }
            }
        });
        mBtn3 = v.findViewById(R.id.fragment_map_btn3); // 격리병원
        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (route3) {
                    mBtn3.setImageResource(R.drawable.ic_route3);
                    route3 = false;
                    for (int i = 0; i < markerHospital.size(); i++) {
                        markerHospital.get(i).setMap(null);
                    }
                } else {
                    mBtn3.setImageResource(R.drawable.ic_route3_on);
                    route3 = true;
                    if (isFirstRoute3) {
                        getHospital();
                        isFirstRoute3 = false;
                    } else {
                        for (int i = 0; i < markerHospital.size(); i++) {
                            markerHospital.get(i).setMap(naverMap);
                        }
                    }
                }
            }
        });
        mBtn4 = v.findViewById(R.id.fragment_map_btn4);
        mBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (route4) {
                    mBtn4.setImageResource(R.drawable.ic_route4_off);
                    route4 = false;
                    for (int i = 0; i < pathOverlays.size(); i++) {
                        pathOverlays.get(i).setMap(null);
                    }
                } else {
                    mBtn4.setImageResource(R.drawable.ic_route4);
                    route4 = true;
                    for (int i = 0; i < pathOverlays.size(); i++) {
                        pathOverlays.get(i).setMap(naverMap);
                    }
                }
            }
        });
        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            return;
        } else {
            CameraPosition cameraPosition = new CameraPosition(new LatLng(37.5535582, 126.9670034), 8);
            naverMap.setCameraPosition(cameraPosition);
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }


    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap2) {
        //NaverMap 객체

        naverMap = naverMap2;
        Log.d("map", "onMapReady");
        naverMap.setNightModeEnabled(true);

        naverMap.addOnOptionChangeListener(new NaverMap.OnOptionChangeListener() {
            @Override
            public void onOptionChange() {
                Log.d("map", "onOptionChange");
            }
        });


        naverMap.setOnMapLongClickListener(new NaverMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                showCustomToast(pointF + "");
            }
        });

        naverMap.setLocationSource(locationSource);

        GpsTracker gpsTracker = new GpsTracker(mContext);

        System.out.println("gps: " + gpsTracker.getLatitude() + ", " + gpsTracker.getLongitude());

        CameraPosition cameraPosition = new CameraPosition(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 8);
        naverMap.setCameraPosition(cameraPosition);


        zoomControlView.setMap(naverMap);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        uiSettings.setZoomControlEnabled(false);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        naverMap.setLightness(0.3f);

//        Marker marker = new Marker();
//        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
//        marker.setMap(naverMap);


        getRoute();
    }

    void getRoute() {
        showProgressDialog(getActivity());
        final MapViewService mapViewService = new MapViewService(this);
        mapViewService.getRoute();
    }

    void getClinic() {

        showProgressDialog(getActivity());
        final MapViewService mapViewService = new MapViewService(this);
        mapViewService.getClinic();

    }

    void getHospital() {
        showProgressDialog(getActivity());
        final MapViewService mapViewService = new MapViewService(this);
        mapViewService.getHospital();
    }

    @Override
    public void validateGetRouteSuccess(ArrayList<RouteResponse> arrayList) {
        routeResponses.addAll(arrayList);
        for(int i=0; i<markers.size(); i++){
            markers.get(i).setMap(null);
            Log.d("map", "널");
        }
        OverlayImage marker1 = OverlayImage.fromResource(R.drawable.corona_marker);
        OverlayImage marker2 = OverlayImage.fromResource(R.drawable.corona_marker2);
        OverlayImage marker3 = OverlayImage.fromResource(R.drawable.corona_marker3);
        OverlayImage marker4 = OverlayImage.fromResource(R.drawable.corona_marker4);
        OverlayImage marker5 = OverlayImage.fromResource(R.drawable.corona_marker5);

        OverlayImage newMarker1 = OverlayImage.fromResource(R.drawable.new_corona_marker1);
        OverlayImage newMarker2 = OverlayImage.fromResource(R.drawable.new_corona_marker2);
        OverlayImage newMarker3 = OverlayImage.fromResource(R.drawable.new_corona_marker3);
        OverlayImage newMarker4 = OverlayImage.fromResource(R.drawable.new_corona_marker4);
        OverlayImage newMarker5 = OverlayImage.fromResource(R.drawable.new_corona_marker5);

        for (int i = 0; i < routeResponses.size(); i++) {
            ArrayList<LatLng> latLngs = new ArrayList<>();
            for (int j = 0; j < routeResponses.get(i).getRouteRes().size(); j++) {
                latLngs.add(new LatLng(routeResponses.get(i).getRouteRes().get(j).getLatitude(), routeResponses.get(i).getRouteRes().get(j).getLongtitude()));
                Marker marker = new Marker();
                marker.setPosition(new LatLng(routeResponses.get(i).getRouteRes().get(j).getLatitude(), routeResponses.get(i).getRouteRes().get(j).getLongtitude()));
                if (i % 5 == 0) {
                    marker.setIcon(marker1);
                } else if (i % 5 == 1) {
                    marker.setIcon(marker2);
                } else if (i % 5 == 2) {
                    marker.setIcon(marker3);
                } else if (i % 5 == 3) {
                    marker.setIcon(marker4);
                } else if (i % 5 == 4) {
                    marker.setIcon(marker5);
                }
                marker.setHeight(90);
                if(routeResponses.get(i).getRouteRes().get(j).getNewRoute() == 1){
                    if (i % 5 == 0) {
                        marker.setIcon(newMarker1);
                    } else if (i % 5 == 1) {
                        marker.setIcon(newMarker2);
                    } else if (i % 5 == 2) {
                        marker.setIcon(newMarker3);
                    } else if (i % 5 == 3) {
                        marker.setIcon(newMarker4);
                    } else if (i % 5 == 4) {
                        marker.setIcon(newMarker5);
                    }
                    marker.setHeight(120);
                }
                marker.setAnchor(new PointF((float) 0.5, (float) 0.5));
                marker.setWidth(90);
                marker.setMap(naverMap);
                final RouteRes routeRes = routeResponses.get(i).getRouteRes().get(j);
                marker.setOnClickListener(new Overlay.OnClickListener() {
                    @Override
                    public boolean onClick(@NonNull Overlay overlay) {
                        CustomPatientDialog customPatientDialog = new CustomPatientDialog(mContext, routeRes);
                        customPatientDialog.show();
                        return false;
                    }
                });
                markers.add(marker);
            }
            if (latLngs.size() >= 2) {
                PathOverlay path = new PathOverlay();
                path.setCoords(latLngs);
                if (i % 5 == 0) {
                    path.setWidth(10);
                    path.setColor(Color.parseColor("#ff8353"));
                    path.setOutlineWidth(1);
                    path.setOutlineColor(Color.parseColor("#ff8353"));
                } else if (i % 5 == 1) {
                    path.setWidth(10);
                    path.setColor(Color.parseColor("#b31b3c"));
                    path.setOutlineWidth(1);
                    path.setOutlineColor(Color.parseColor("#b31b3c"));
                } else if (i % 5 == 2) {
                    path.setWidth(10);
                    path.setColor(Color.parseColor("#091ab3"));
                    path.setOutlineWidth(1);
                    path.setOutlineColor(Color.parseColor("#091ab3"));
                } else if (i % 5 == 3) {
                    path.setWidth(10);
                    path.setColor(Color.parseColor("#801ab3"));
                    path.setOutlineWidth(1);
                    path.setOutlineColor(Color.parseColor("#801ab3"));
                } else if (i % 5 == 4) {
                    path.setWidth(10);
                    path.setColor(Color.parseColor("#b0e400"));
                    path.setOutlineWidth(1);
                    path.setOutlineColor(Color.parseColor("#b0e400"));
                }
                path.setMap(naverMap);
                pathOverlays.add(path);
            }
        }
        hideProgressDialog();

//        markers.clear();
//        pathOverlays.clear();
//        final SharedPreferences.Editor editor = sSharedPreferences.edit();
//        editor.putBoolean(CAN_UPDATE_ROUTE, false);
//        editor.apply();
    }

    @Override
    public void validateGetRouteFail(String message) {
        hideProgressDialog();
        showCustomToast(message == null || message.isEmpty() ? getString(R.string.network_error) : message);
    }

    @Override
    public void validateGetClinicSuccess(ArrayList<ClinicInfo> clinicInfos) {
        hideProgressDialog();
        this.clinicInfos.addAll(clinicInfos);
        for (int i = 0; i < clinicInfos.size(); i++) {
            Marker marker = new Marker();
            marker.setPosition(new LatLng(clinicInfos.get(i).getLatitude(), clinicInfos.get(i).getLongitude()));
            marker.setIcon(OverlayImage.fromResource(R.drawable.ic_clinic2));
            marker.setAnchor(new PointF((float) 0.5, (float) 0.5));
            marker.setWidth(90);
            marker.setHeight(90);
            marker.setMap(naverMap);
            final ClinicInfo clinicInfo = clinicInfos.get(i);
            marker.setOnClickListener(new Overlay.OnClickListener() {
                @Override
                public boolean onClick(@NonNull Overlay overlay) {
                    //다이얼 로그 이동
                    CustomClinicDialog customClinicDialog = new CustomClinicDialog(mContext, clinicInfo);
                    customClinicDialog.show();
                    return false;
                }
            });
            markerClinic.add(marker);
        }
        final SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putBoolean(CAN_UPDATE_CLINIC, false);
        editor.apply();
//        markerClinic.
    }

    @Override
    public void validateGetHospitalSuccess(ArrayList<HospitalInfo> arrayList) {
        hideProgressDialog();
        hospitalInfos.addAll(arrayList);
        for (int i = 0; i < hospitalInfos.size(); i++) {
            Marker marker = new Marker();
            marker.setPosition(new LatLng(hospitalInfos.get(i).getLatitude(), hospitalInfos.get(i).getLongitude()));
            marker.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital));
            marker.setAnchor(new PointF((float) 0.5, (float) 0.5));
            marker.setWidth(90);
            marker.setHeight(90);
            marker.setMap(naverMap);
            final HospitalInfo hospitalInfo = hospitalInfos.get(i);
            marker.setOnClickListener(new Overlay.OnClickListener() {
                @Override
                public boolean onClick(@NonNull Overlay overlay) {
                    //다이얼로그 이동
                    CustomHospitalDialog customHospitalDialog = new CustomHospitalDialog(mContext, hospitalInfo);
                    customHospitalDialog.show();
                    return false;
                }
            });
            markerHospital.add(marker);
        }
        final SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putBoolean(CAN_UPDATE_HOSPITAL, false);
        editor.apply();
    }
}
