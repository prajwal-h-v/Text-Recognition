package com.prajwal.textrecognision;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class databaseHelper {
    private DatabaseReference mDatabase;

    public void readData() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}
