package com.example.smartstopbellproject;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DB extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {

        //데이터베이스 설정
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //데이터베이스 쓰기
        public void writeNewUser(String userId, String name, String email) {
            User user = new User(name, email);

            mDatabase.child("users").child(userId).setValue(user);
        }

        //setValue()를 사용하면 지정된 위치에서 하위 노드를 포함하여 모든 데이터를 덮어쓴다
        mDatabase.child("users").child(userId).child("username").setValue(name);

        //데이터베이스 루트에는 리스너를 연결하지 않는 것이 좋다
        //데이터베이스 읽기
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Post post = dataSnapshot.getValue(Post.class);
                // ..
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mPostReference.addValueEventListener(postListener);

        //데이터 한 번만 읽기 -> get()은 리스너 사용하기
        mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });

        //데이터 삭제 : removeValue()를 호출

        //출처 : https://firebase.google.com/docs/database/android/read-and-write?hl=ko



    }

}
