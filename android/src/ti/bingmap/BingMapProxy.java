/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2017 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.bingmap;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapElementTappedEventArgs;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapTappedEventArgs;
import com.microsoft.maps.MapView;
import com.microsoft.maps.OnMapElementTappedListener;
import com.microsoft.maps.OnMapTappedListener;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;


@Kroll.proxy(creatableInModule = TiBingmapModule.class)
public class BingMapProxy extends TiViewProxy  {
    // Standard Debugging variables
    private static final String LCAT = "BingMapProxy";
    private static final boolean DBG = TiConfig.LOGD;
    String apiKey = "";
    MapElementLayer mPinLayer;
    private MapView mMapView;

    // Constructor
    public BingMapProxy() {
        super();
    }

    @Override
    public TiUIView createView(Activity activity) {
        TiUIView view = new ExampleView(this);
        view.getLayoutParams().autoFillsHeight = true;
        view.getLayoutParams().autoFillsWidth = true;
        return view;
    }

    // Handle creation options
    @Override
    public void handleCreationDict(KrollDict options) {
        super.handleCreationDict(options);

        if (options.containsKey("apiKey")) {
            apiKey = options.getString("apiKey");
        }
    }

    // Methods
    @Kroll.method
    public void location(KrollDict kd) {
        int zoomLevel = 10;
        if (kd.containsKeyAndNotNull("zoom")) {
            zoomLevel = kd.getInt("zoom");
        }
        if (kd.containsKeyAndNotNull("lat") && kd.containsKeyAndNotNull("lon")) {
            mMapView.setScene(
                    MapScene.createFromLocationAndZoomLevel(new Geopoint(kd.getDouble("lat"), kd.getDouble("lon")), zoomLevel),
                    MapAnimationKind.NONE);
        }
    }

    @Kroll.method
    public void addPin(KrollDict kd) {

        if (kd.containsKeyAndNotNull("lat") && kd.containsKeyAndNotNull("lon")) {
            Geopoint location = new Geopoint(kd.getDouble("lat"), kd.getDouble("lon"));
            String title = kd.getString("title");

            MapIcon pushpin = new MapIcon();
            pushpin.setLocation(location);
            pushpin.setTitle(title);

            mPinLayer.getElements().add(pushpin);
        }
    }

    @Kroll.method
    public void clearPins() {
        mPinLayer.getElements().clear();
    }

    private class ExampleView extends TiUIView implements OnMapTappedListener, OnMapElementTappedListener {
        public ExampleView(TiViewProxy proxy) {
            super(proxy);
            // get the package name
            String pkgName = proxy.getActivity().getPackageName();
            // resources
            Resources res = proxy.getActivity().getResources();

            // find the layout file and get the layout
            int resId_viewHolder = res.getIdentifier("layout", "layout", pkgName);
            LayoutInflater inflater = LayoutInflater.from(proxy.getActivity());
            View viewWrapper = inflater.inflate(resId_viewHolder, null);

            int resId_view = res.getIdentifier("map_view", "id", pkgName);
            FrameLayout frameLayout = viewWrapper.findViewById(resId_view);

            mMapView = new MapView(TiApplication.getAppCurrentActivity(), MapRenderMode.VECTOR);  // or use MapRenderMode.RASTER for 2D map
            if (apiKey.equals("")) {
                Log.e(LCAT, "Please set an 'apiKey'");
            } else {
                mMapView.setCredentialsKey(apiKey);
            }
            frameLayout.addView(mMapView);
            mPinLayer = new MapElementLayer();
            mMapView.getLayers().add(mPinLayer);
            setNativeView(frameLayout);
            mMapView.addOnMapTappedListener(this);
            mPinLayer.addOnMapElementTappedListener(this);
        }

        @Override
        public void processProperties(KrollDict d) {
            super.processProperties(d);
        }

        @Override
        public boolean onMapTapped(MapTappedEventArgs mapTappedEventArgs) {
            Log.i("---", mapTappedEventArgs.location + " " + mapTappedEventArgs.position );
            return false;
        }

        @Override
        public boolean onMapElementTapped(MapElementTappedEventArgs mapElementTappedEventArgs) {
            Log.i("---", mapElementTappedEventArgs.location + " " + mapElementTappedEventArgs.mapElements.get(0).getContentDescription() );
            return false;
        }
    }
}
