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
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.ZoomControlView;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseFragment;
import com.softsquared.android.corona.src.CustomPatientDialog;
import com.softsquared.android.corona.src.GpsTracker;
import com.softsquared.android.corona.src.main.map.interfaces.MapViewView;
import com.softsquared.android.corona.src.main.map.models.RouteRes;
import com.softsquared.android.corona.src.main.map.models.RouteResponse;

import java.util.ArrayList;
import java.util.concurrent.Executor;


public class MapViewFragment extends BaseFragment implements OnMapReadyCallback, MapViewView {

    Context mContext;
    ZoomControlView zoomControlView;
    private MapView mapView;
    NaverMap naverMap;
    Button mBtn, mBtn2, mBtn3;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    ArrayList<PathOverlay> pathOverlays = new ArrayList<>();
    ArrayList<RouteResponse> routeResponses = new ArrayList<>();
    ArrayList<Marker> markers = new ArrayList<>();

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
        zoomControlView = v.findViewById(R.id.zoom);

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
                path.setWidth(10);
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

        CameraPosition cameraPosition = new CameraPosition(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 9);
        naverMap.setCameraPosition(cameraPosition);

        naverMap.setLocationSource(locationSource);

        zoomControlView.setMap(naverMap);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        uiSettings.setZoomControlEnabled(false);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

//        Marker marker = new Marker();
//        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
//        marker.setMap(naverMap);



        getRoute();
    }

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
            for(int j=0; j<routeResponses.get(i).getRouteRes().size(); j++){
                System.out.println(routeResponses.get(i).getRouteRes().get(j).getLatitude()+", "+routeResponses.get(i).getRouteRes().get(j).getLongtitude());
                latLngs.add(new LatLng(routeResponses.get(i).getRouteRes().get(j).getLatitude(), routeResponses.get(i).getRouteRes().get(j).getLongtitude()));
                Marker marker = new Marker();
                marker.setPosition(new LatLng(routeResponses.get(i).getRouteRes().get(j).getLatitude(), routeResponses.get(i).getRouteRes().get(j).getLongtitude()));
                marker.setIcon(OverlayImage.fromResource(R.drawable.corona_marker));
                marker.setAnchor(new PointF((float)0.5,(float)0.5));
                marker.setWidth(90);
                marker.setHeight(90);
                marker.setMap(naverMap);
                final RouteRes routeRes = routeResponses.get(i).getRouteRes().get(j);
                marker.setOnClickListener(new Overlay.OnClickListener() {
                    @Override
                    public boolean onClick(@NonNull Overlay overlay) {
                        CustomPatientDialog customPatientDialog = new CustomPatientDialog(mContext,routeRes);
                        customPatientDialog.show();
                        return false;
                    }
                });
                markers.add(marker);
            }
            PathOverlay path = new PathOverlay();
            path.setCoords(latLngs);
            path.setWidth(10);
            path.setColor(Color.parseColor("#ff4700"));
            path.setOutlineWidth(5);
            path.setOutlineColor(Color.parseColor("#99ff8353"));
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
