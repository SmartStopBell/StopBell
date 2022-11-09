package com.example.smartstopbellproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ServiceCompat;
import androidx.core.content.ContextCompat;

import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.AdvertisingSetCallback;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;

public class RouteActivity extends AppCompatActivity {

    ListView listview;
    ListViewAdapter adapter;
    TextView busNum;
    ListViewItem selectedItem;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    String userName = "user1";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        Button btnCancel = findViewById(R.id.btnCancel);
        busNum = findViewById(R.id.busNum);
        ImageButton back = findViewById(R.id.back);

        // Adapter 생성
        adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter 달기
        listview = findViewById(R.id.listview);
        listview.setAdapter(adapter);

        //예약 시 실행되는 비콘
        Beacon beacon1 = new Beacon.Builder()
                .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")  // uuid for beacon
                .setId2("1")  // major
                .setId3("1")  // minor
                .setManufacturer(0x004C)  // Radius Networks. 0x0118 : Change this for other beacon layouts // 0x004C : for iPhone
                .setTxPower(-65)  // Power in dB
                .build();
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
        BeaconTransmitter beaconTransmitter1 = new BeaconTransmitter(getApplicationContext(), beaconParser);

        //예약 취소 시 실행되는 비콘
        Beacon beacon2 = new Beacon.Builder()
                .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")  // uuid for beacon
                .setId2("2")  // major
                .setId3("1")  // minor
                .setManufacturer(0x004C)  // Radius Networks. 0x0118 : Change this for other beacon layouts // 0x004C : for iPhone
                .setTxPower(-65)  // Power in dB
                .build();
        BeaconParser beaconParser2 = new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
        BeaconTransmitter beaconTransmitter2 = new BeaconTransmitter(getApplicationContext(), beaconParser2);


        //버스 번호 & 노선 출력
        DatabaseReference route = firebaseDatabase.getReference("busroute");
        databaseReference.child("bus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String busnum = snapshot.getValue(String.class);
                    busNum.setText(busnum);

                    route.child(busnum).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String stopId = snapshot.getKey();
                                String stopname = snapshot.child("stopname").getValue(String.class);
                                Integer position = snapshot.child("position").getValue(Integer.class);
                                adapter.addItem(stopId, stopname, position);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        /* 정민이꺼
        busNum = findViewById(R.id.busNum);
        busNum.setText("ABCDEFGHIJKLMNOPQRSTUVWXYZZZZZ");
        busNum.setSingleLine();
        busNum.setMarqueeRepeatLimit(-1);
        busNum.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        busNum.setSelected(true);
         */


        //AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(RouteActivity.this);
        builder.setTitle("정류장 이름");
        builder.setMessage("목적지로 예약하시겠습니까?");
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //취소 버튼 클릭시 동작
            }
        });

        //예약 버튼 클릭 시 동작
        builder.setPositiveButton("예약", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                adapter.setSelectedItem(selectedItem.getPosition());
                Toast.makeText(getApplicationContext(), "예약완료",Toast.LENGTH_LONG).show();

                // db에 예약 정보 추가
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (!snapshot.child("reserve").exists()) {
                            userName = "user1";
                            databaseReference.child("reserve").child(userName).setValue(selectedItem);
                        } else if (snapshot.child("reserve").exists()) {
                            userName = "user1";
                            databaseReference.child("reserve").child(userName).setValue(selectedItem);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    }
                });
                // 예약취소 버튼 활성화
                btnCancel.setVisibility(View.VISIBLE);
                Integer a = adapter.selectedPosition;


                // 예약 비콘 major, minor값 설정
                Beacon beacon1 = new Beacon.Builder()
                        .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")  // uuid for beacon
                        .setId2("55555")  // major
                        .setId3(a.toString())  // minor -> position값
                        .setManufacturer(0x004C)  // Radius Networks. 0x0118 : Change this for other beacon layouts // 0x004C : for iPhone
                        .setTxPower(-65)  // Power in dB
                        .build();

                // 예약 비콘 startAvertising
                beaconTransmitter1.startAdvertising(beacon1, new AdvertiseCallback() {
                    @Override
                    public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                        super.onStartSuccess(settingsInEffect);
                        // 예약취소 비콘은 stopAdvertising
                        beaconTransmitter2.stopAdvertising();
                    }
                    @Override
                    public void onStartFailure(int errorCode) {
                        super.onStartFailure(errorCode);
                    }
                });
            }
        });

        //listview item 클릭 이벤트
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (btnCancel.getVisibility() == View.VISIBLE) return;

                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);
                selectedItem = item;
                builder.setTitle(selectedItem.getStopname());
                AlertDialog alertDialog = builder.create(); //빌더 사용해서 alertDialog 객체 생성
                alertDialog.show(); //alertDialog창 띄우기
            }
        });


        //예약취소 버튼 클릭 이벤트
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "예약취소", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                // DB에서 예약 정보 삭제
                databaseReference.child("reserve").child(userName).removeValue();

                Integer a = adapter.selectedPosition;
                Integer b = a+1000;

                // 예약취소 비콘 major, minor값 설정
                Beacon beacon2 = new Beacon.Builder()
                        .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")  // uuid for beacon
                        .setId2("55555")  // major
                        .setId3(b.toString())  // minor -> position값 + 1000
                        .setManufacturer(0x004C)  // Radius Networks. 0x0118 : Change this for other beacon layouts // 0x004C : for iPhone
                        .setTxPower(-65)  // Power in dB
                        .build();

                // 예약취소 비콘 startAvertising
                beaconTransmitter2.startAdvertising(beacon2, new AdvertiseCallback() {
                    @Override
                    public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                        super.onStartSuccess(settingsInEffect);
                        // 예약 비콘은 stopAdvertisiong
                        beaconTransmitter1.stopAdvertising();
                    }
                    @Override
                    public void onStartFailure(int errorCode) {
                        super.onStartFailure(errorCode);
                    }
                });
            }
        });


        // 뒤로가기 버튼 클릭 시 main
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 예약 정보 화면에 띄우기
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {

                    // 예약한 userName이 존재하면 값 가져옴
                    if (snapshot.child("reserve").child(userName).exists()) {
                        Long str = (Long) snapshot.child("reserve").child(userName).child("position").getValue();
                        adapter.setSelectedItem(Integer.parseInt(str.toString()));
                        btnCancel.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    } //onCreate end
}