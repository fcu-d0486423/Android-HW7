package com.example.user.myapplication;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lvPets = (ListView)findViewById(R.id.listview_pet);
        getHotelFromFirebase();
    }

    private void getHotelFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("/Infos/Info");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DataSnapshot dsSName = ds.child("Name");
                    DataSnapshot dsAKind = ds.child("Description");
                    DataSnapshot dsATel = ds.child("Tel");
                    String shelterName = (String) dsSName.getValue();
                    String kind = (String) dsAKind.getValue();
                    String tel  =(String)dsATel.getValue();

                    DataSnapshot dsImg = ds.child("Picture3");
                    String imgUrl = (String) dsImg.getValue();
                    Bitmap petImg = getImgBitmap(imgUrl);

                    Log.v("MyApplication", shelterName + ";" + kind);
                }
            }


            private Bitmap getImgBitmap(String imgUrl) {
                try {
                    URL url = new URL(imgUrl);
                    Bitmap bm = BitmapFactory.decodeStream(
                            url.openConnection()
                                    .getInputStream());
                    return bm;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("MyApplication", databaseError.getMessage());
            }
        });
    }
}

