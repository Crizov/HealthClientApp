package M5.seshealthpatient.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import M5.seshealthpatient.Models.DoctorUser;
import M5.seshealthpatient.Models.PatientUser;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import M5.seshealthpatient.R;

public class ViewInformation extends BaseActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;

    private ListView mListView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_view_information;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("View Information");

        mListView = (ListView) findViewById(R.id.listview);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out

                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            PatientUser uInfo = new PatientUser();

            PatientUser user = ds.child(userID).getValue(PatientUser.class);
            if (user == null) {
                uInfo.setName(""); //set the name
                uInfo.setPhone(""); //set the phone
                uInfo.setWeight(""); //set the weight
                uInfo.setHeight("");//set the height
                uInfo.setDoctorID(""); //set the doctorID
                uInfo.setMedicalCondition("");

            } else {
                uInfo = user;
            }

            ArrayList<String> array = new ArrayList<>();
            array.add("Name:        " + uInfo.getName());
            array.add("Age:     " + uInfo.getAge());
            array.add("Sex:     " + uInfo.getSex());
            array.add("Number:  " + uInfo.getPhone());
            array.add("Weight:      " + uInfo.getWeight() + "kg");
            array.add("Height:      " + uInfo.getHeight() + "cm");
            array.add("Condition: " + uInfo.getMedicalCondition());

            // get doctor's user account and set their name as the selected doctor
            DoctorUser doctor = ds.child(uInfo.getDoctorID()).getValue(DoctorUser.class);
            array.add("Doctor:      " + doctor.getName());


            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
            mListView.setAdapter(adapter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
