package com.example.kazuhiroshigenobu.googlemaptraining;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CommentListView extends AppCompatActivity {

    private DatabaseReference reviewListRef;
    private DatabaseReference reviewRef;
    private DatabaseReference toiletRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list_view);



        toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");
        reviewRef = FirebaseDatabase.getInstance().getReference().child("reviews");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reviewListRef = FirebaseDatabase.getInstance().getReference().child("ReviewList").child(uid);

        reviewRidQuery();
    }

    private void reviewRidQuery(){
        reviewListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot child : dataSnapshot.getChildren()) {
                    final String ridKey = child.getKey(); //got rid

                    commnetsReviewInfoQuery(ridKey);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //get rid

    }

    private void  commnetsReviewInfoQuery(String ridKey){
        //get review Info

        reviewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void commentsToiletInfoQuery(){
        //get toilet Info


    }
}
