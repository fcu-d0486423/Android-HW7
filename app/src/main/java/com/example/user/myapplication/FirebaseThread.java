package com.example.user.myapplication;


import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseThread extends Thread{
    private DataSnapshot dataSnapshot;
    public FirebaseThread(DataSnapshot dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
    }
    @Override
    public void run() {
        List<Pet> lsPets = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            DataSnapshot dsSName = ds.child("shelter_name");
            DataSnapshot dsAKind = ds.child("animal_kind");
            String shelterName = (String)dsSName.getValue();
            String kind = (String)dsAKind.getValue();
            DataSnapshot dsImg = ds.child("album_file");
            String imgUrl = (String) dsImg.getValue();
            Bitmap petImg = getImgBitmap(imgUrl);
            Pet aPet = new Pet();
            aPet.setShelter(shelterName);
            aPet.setKind(kind);
            aPet.setImgUrl(petImg);
            lsPets.add(aPet);

            Log.v("AdoptPet", shelterName + ";" + kind);
        }
    }
    private void getPetsFromFirebase() {
        FirebaseDatabase database =
                FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                new FirebaseThread(dataSnapshot).start();
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.v("AdoptPet", databaseError.getMessage());
            }
        });
    }

}
