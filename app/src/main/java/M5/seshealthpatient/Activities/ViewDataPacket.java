package M5.seshealthpatient.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import M5.seshealthpatient.Models.DataPacket;
import M5.seshealthpatient.Models.LocationDefaults;
import M5.seshealthpatient.Models.PatientUser;
import M5.seshealthpatient.R;



public class ViewDataPacket extends BaseActivity implements OnMapReadyCallback {

    private DatabaseReference mUserDb;
    private DataPacket mDataPacket;
    private String mPatientId;
    private PatientUser mPatient;
    private TextView mSentFromTV;
    private TextView mQueryTV;
    private TextView mHeartRateTV;
    private TextView mLocationTV;
    private Toolbar toolbar;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private GeoDataClient mGeoDataClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_packet);


        bindViewComponents();
        mDataPacket = (DataPacket)getIntent().getSerializableExtra("DATA_PACKET");
        mPatientId = (String)getIntent().getSerializableExtra("PATIENT_ID");
        toolbar.setTitle("Data Packet - " + mDataPacket.getTitle());
        if (mDataPacket.getQuery() != null)
            mQueryTV.setText(mDataPacket.getQuery());
        if (mDataPacket.getHeartRate() != null)
            mHeartRateTV.setText(mDataPacket.getHeartRate());

        if (mDataPacket.hasLocation()){
            mLocationTV.setVisibility(View.GONE);
            setUpGoogleMaps(savedInstanceState);
        }
        else {
            mLocationTV.setText("Patient has not set their location");
            mMapView.setVisibility(View.GONE);
        }

        mUserDb = FirebaseDatabase.getInstance().getReference("Users/" + mPatientId);
        mUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPatient = dataSnapshot.getValue(PatientUser.class);
                mSentFromTV.setText(mPatient.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void bindViewComponents() {
        mSentFromTV = findViewById(R.id.sentFromTV);
        mQueryTV = findViewById(R.id.queryTV);
        mHeartRateTV = findViewById(R.id.heartRateTV);
        mLocationTV = findViewById(R.id.locationTV);
        mMapView = findViewById(R.id.mapView);
        toolbar = findViewById(R.id.toolbar);
    }

    public void setUpGoogleMaps(Bundle savedInstanceState) {
        mGeoDataClient = Places.getGeoDataClient(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        mMapView.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
        LatLng latLng =  new LatLng(mDataPacket.getLatitude(), mDataPacket.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Patient's Location");
        mGoogleMap.addMarker(markerOptions);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, LocationDefaults.DEFAULT_ZOOM));
    }


    @Override
    public void onResume() {
        if (mMapView.getVisibility() == View.VISIBLE)
            mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView.getVisibility() == View.VISIBLE)
            mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView.getVisibility() == View.VISIBLE)
            mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView.getVisibility() == View.VISIBLE)
            mMapView.onLowMemory();
    }
}
