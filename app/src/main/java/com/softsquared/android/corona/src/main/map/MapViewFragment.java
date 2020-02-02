package com.softsquared.android.corona.src.main.map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseFragment;
import com.softsquared.android.corona.src.CustomPatientDialog;
import com.softsquared.android.corona.src.GpsTracker;
import com.softsquared.android.corona.src.main.map.interfaces.MapViewView;
import com.softsquared.android.corona.src.main.map.models.RouteResponse;

import java.util.ArrayList;
import java.util.concurrent.Executor;


public class MapViewFragment extends BaseFragment implements OnMapReadyCallback, MapViewView {

    Context mContext;
    private MapView mapView;
    NaverMap naverMap;
    Button mBtn, mBtn2, mBtn3;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    ArrayList<PathOverlay> pathOverlays = new ArrayList<>();
    ArrayList<RouteResponse> routeResponses = new ArrayList<>();


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
        mapView.getMapAsync(this);

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
        mapView.onSaveInstanceState(outState);
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
        mBtn = v.findViewById(R.id.fragment_map_btn); // 감염자 경로
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(37.5666102, 126.9783881))
                        .animate(CameraAnimation.Easing);
                naverMap.moveCamera(cameraUpdate);
            }
        });
        mBtn2 = v.findViewById(R.id.fragment_map_btn2); // 선별 진료소
        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<pathOverlays.size(); i++){
                    pathOverlays.get(i).setMap(null);
                }
                PathOverlay path = new PathOverlay();
                ArrayList<LatLng> latLngs = new ArrayList<>();
                latLngs.add(new LatLng(37.4601904, 126.4319409));
                latLngs.add(new LatLng(37.56607, 126.98268));
                latLngs.add(new LatLng(37.56445, 126.97707));
                latLngs.add(new LatLng(37.55855, 126.97822));
                path.setCoords(latLngs);
                path.setWidth(30);
                path.setMap(naverMap);
                pathOverlays.add(path);
                CircleOverlay circle = new CircleOverlay();
                circle.setCenter(new LatLng(37.4601904, 126.4319409));
                circle.setRadius(3000);
                circle.setColor(Color.GREEN);
                circle.setMap(naverMap);
                circle.setOnClickListener(patientClick);

                CircleOverlay circle2 = new CircleOverlay();
                circle2.setCenter(new LatLng(37.56607, 126.98268));
                circle2.setRadius(3000);
                circle2.setColor(Color.GREEN);
                circle2.setMap(naverMap);
                circle2.setOnClickListener(patientClick);

                CircleOverlay circle3 = new CircleOverlay();
                circle3.setCenter(new LatLng(37.56445, 126.97707));
                circle3.setRadius(3000);
                circle3.setColor(Color.GREEN);
                circle3.setMap(naverMap);
                circle3.setOnClickListener(patientClick);

                CircleOverlay circle4 = new CircleOverlay();
                circle4.setCenter(new LatLng(337.55855, 126.97822));
                circle4.setRadius(3000);
                circle4.setColor(Color.GREEN);
                circle4.setMap(naverMap);
                circle4.setOnClickListener(patientClick);
            }
        });
        mBtn3 = v.findViewById(R.id.fragment_map_btn3); // 격리병원
        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<pathOverlays.size(); i++){
                    pathOverlays.get(i).setMap(null);
                }
                PathOverlay path = new PathOverlay();
                ArrayList<LatLng> latLngs = new ArrayList<>();
                latLngs.add(new LatLng(35.9658449, 126.6760305));
                latLngs.add(new LatLng(37.56607, 126.98268));
                latLngs.add(new LatLng(37.56445, 126.97707));
                latLngs.add(new LatLng(37.55855, 126.97822));
                path.setCoords(latLngs);
                path.setWidth(30);
                path.setMap(naverMap);
                pathOverlays.add(path);
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
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }


    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap2) {
        //NaverMap 객체 반환
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

        GpsTracker gpsTracker = new GpsTracker(mContext);

        System.out.println(gpsTracker.getLatitude()+", " +  gpsTracker.getLongitude());

        CameraPosition cameraPosition = new CameraPosition(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 8);
        naverMap.setCameraPosition(cameraPosition);

        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

//        Marker marker = new Marker();
//        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
//        marker.setMap(naverMap);



        getRoute();
    }

    private Overlay.OnClickListener patientClick = new Overlay.OnClickListener() {
        @Override
        public boolean onClick(@NonNull Overlay overlay) {
            CustomPatientDialog customPatientDialog = new CustomPatientDialog(mContext);
            customPatientDialog.show();
            return false;
        }
    };

    void getRoute(){
        showProgressDialog(getActivity());
        final MapViewService mapViewService = new MapViewService(this);
        mapViewService.getRoute();
    }

    @Override
    public void validateGetRouteSuccess(ArrayList<RouteResponse> arrayList) {
        hideProgressDialog();
        routeResponses.addAll(arrayList);
        for(int i=0; i<routeResponses.size(); i++){
            ArrayList<LatLng> latLngs = new ArrayList<>();
            for(int j=0; j<routeResponses.get(i).getArrayList().size(); i++){
                latLngs.add(new LatLng(routeResponses.get(i).getArrayList().get(j).getLatitude(), routeResponses.get(i).getArrayList().get(j).getLongtitude()));
            }
            PathOverlay path = new PathOverlay();
            path.setCoords(latLngs);
            path.setWidth(30);
            path.setMap(naverMap);
            pathOverlays.add(path);
        }
    }

    @Override
    public void validateGetRouteFail(String message) {
        hideProgressDialog();
        showCustomToast(message == null || message.isEmpty() ? getString(R.string.network_error) : message);
    }
}
