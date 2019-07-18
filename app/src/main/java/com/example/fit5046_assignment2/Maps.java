package com.example.fit5046_assignment2;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Address;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Maps extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener
{
    View map;
    private GoogleMap mMap;
    private MapView mapView;
    Geocoder geocoder = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        map = inflater.inflate(R.layout.fragment_map, container, false);
        return map;
    }

    public void onMapReady(GoogleMap googleMap)
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user2", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id", 0);
        mMap = googleMap;
        FindAddress findAddress = new FindAddress();
        findAddress.execute(userId);

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    public boolean onMarkerClick(final Marker marker) {

        Integer clickCount = (Integer) marker.getTag();

        if (clickCount != null)
        {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
        }

        return false;
    }

    public List<Double> setLocation(String address)
    {
        geocoder = new Geocoder(getActivity());
        try
        {
            List<Address> addressList = geocoder.getFromLocationName(address, 1);
            double lat = addressList.get(0).getLatitude();
            double lng = addressList.get(0).getLongitude();
            List<Double> b = new ArrayList<>();
            b.add(lat);
            b.add(lng);
            return b;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private class FindAddress extends AsyncTask<Integer, Void, String>
    {
        @Override
        protected String doInBackground(Integer... params)
        {
            return RestMethod.findAddress(params[0]);
        }

        @Override
        protected void onPostExecute(String input)
        {
            List<Double> c = setLocation(input);
            LatLng home = new LatLng(c.get(0), c.get(1));
            mMap.addMarker(new MarkerOptions().position(home).title("Home"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(home));

            FindPark findPark = new FindPark();     //if put right API, it will work
            findPark.execute(home);
        }
    }

    private class FindPark extends AsyncTask<LatLng, Void, JSONArray>
    {
        @Override
        protected JSONArray doInBackground(LatLng... params)
        {
            return MapAPI.findParks(params[0]);
        }

        @Override
        protected void onPostExecute(JSONArray input)
        {
            for (int i = 0; i < input.length(); i++)
            {
                try
                {
                    JSONObject result = input.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                    LatLng latLng = new LatLng(Double.parseDouble(result.getString("lat")), Double.parseDouble(result.getString("lng")));

                    mMap.addMarker(new MarkerOptions().position(latLng).title("Park")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
