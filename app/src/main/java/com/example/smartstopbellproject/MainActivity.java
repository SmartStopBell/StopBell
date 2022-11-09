package com.example.smartstopbellproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.Region;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    TextView getonBus;
    Button rezbtn;
    ImageButton bell;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private String bus4211;
    private String bus303;
    private String busNum;
    String userName = "user1";

    private BeaconManager beaconManager;
    private PendingIntent pendingIntent2;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION2 = 1;

    private List<Beacon> beaconList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        rezbtn = findViewById(R.id.rezbtn);
        getonBus = findViewById(R.id.getonBus);
        bell = findViewById(R.id.bell);

        // 퍼미션 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                        }
                    }
                });
                builder.show();
            }
        }


        //비콘 수신하는 부분
        Intent intent2 = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent2 = PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        //비콘 매니저 생성,
        beaconManager = BeaconManager.getInstanceForApplication(this);

        //비콘 매니저에서 layout 설정 'm:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25'
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        //beaconManager 설정 bind
        beaconManager.bind((BeaconConsumer) this);

        //beacon 을 활용하려면 블루투스 권한획득(Andoird M버전 이상)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }



        //카드 태그 창 띄우기
        CustomDialog.getInstance(this).showDefaultDialog();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        //rfid로 받은 버스 정보를 가지고 승차한 버스번호 출력
        databaseReference.child("bus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String busNum = snapshot.child("busnumber").getValue(String.class);
                    getonBus.setText(busNum);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 비콘 생성 후 시작. 실제 가장 필요한 소스
                Beacon beacon = new Beacon.Builder()
                        .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")  // uuid for beacon
                        .setId2("5709")  // major
                        .setId3("55555")  // minor
                        .setManufacturer(0x004C)  // Radius Networks. 0x0118 : Change this for other beacon layouts // 0x004C : for iPhone
                        .setTxPower(-65)  // Power in dB
                        .build();
                BeaconParser beaconParser = new BeaconParser()
                        .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
                BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
                beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {
                    @Override
                    public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                        super.onStartSuccess(settingsInEffect);
                        bell.setImageResource(R.drawable.bell_after);
                    }

                    @Override
                    public void onStartFailure(int errorCode) {
                        super.onStartFailure(errorCode);
                    }
                });
                beaconTransmitter.stopAdvertising();
            }
        });

        
        //하차 예약시 예약 버튼에 목적지 이름 출력
        databaseReference.child("reserve").child(userName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("stopname").getValue(String.class)!=null){
                    String goalStop = snapshot.child("stopname").getValue(String.class);
                    rezbtn.setText(goalStop);
                }else {
                    //하차 예약을 안 했을 시 예약 버튼 그대로
                    rezbtn.setText("예약");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

            

        //하차벨 클릭 시(즉시하차)

        //예약 버튼 클릭 시 노선도 화면으로 전환
        rezbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RouteActivity.class);
                intent.putExtra("busNumber", busNum);
                startActivity(intent);
            }
        });


    }
    @Override
    protected void onPause() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
        handler.sendEmptyMessage(0);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        //nfc 태그
        if (tag != null) {
            byte[] tagId = tag.getId();
            String busId = toHexString(tagId);
            //nfc태그 id값
            bus4211 = "A4BC653C";
            bus303 = "044F6292837280";

            //버스 승하차 태그
            databaseReference.child("bus").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (busId.equals(bus303)) {
                        busNum = "303";

                        //DB에 버스 정보가 들어간 상태에서 같은 버스 단말기 태그하면 DB정보 삭제(하차)
                        if (snapshot.child("busnumber").getValue(String.class) == busNum) {
                            databaseReference.child("bus").removeValue();

                        } else {
                            //버스 정보가 없을 때 or 다른 버스 단말기 태그하면 DB정보 입력(승차)
                            getonBus.setText(busNum);
                            databaseReference.child("bus").child("busnumber").setValue(busNum);
                        }

                    } else if (busId.equals(bus4211)) {
                        busNum = "4211";
                        if (snapshot.child("busnumber").getValue(String.class) == busNum) {
                            databaseReference.child("bus").removeValue();
                        } else {
                            getonBus.setText(busNum);
                            databaseReference.child("bus").child("busnumber").setValue(busNum);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                }
            });
        }
    } //onNewIntent end.

    public static final String CHARS = "0123456789ABCDEF";

    public static String toHexString(byte[] data) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; ++i) {
            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F))
                    .append(CHARS.charAt(data[i] & 0x0F));
        }
        return sb.toString();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind((BeaconConsumer) this);
    }

    @Override
    public void onBeaconServiceConnect() {
        // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
        // region에는 비콘들에 대응하는 Region 객체가 들어온다.
        beaconManager.addRangeNotifier((beacons, region) -> {
            if (beacons.size() > 0) {
                beaconList.clear();
                for (Beacon beacon : beacons) {
                    beaconList.add(beacon);
                }
                handler.sendEmptyMessage(0);

            }
        });
        handler.sendEmptyMessage(0);
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }
//
//    // 버튼이 클릭되면 textView 에 비콘들의 정보를 뿌린다.
//    public void OnButtonClicked(View view) {
//        // 아래에 있는 handleMessage를 부르는 함수. 맨 처음에는 0초간격이지만 한번 호출되고 나면
//        // 1초마다 불러온다.
//        handler.sendEmptyMessage(0);
//    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            // 비콘의 아이디와 거리를 측정하여 textView에 넣는다.
            for (Beacon beacon : beaconList) {
                String uuid = beacon.getId1().toString(); //beacon uuid
                int major = beacon.getId2().toInt(); //beacon major
                int minor = beacon.getId3().toInt();// beacon minor
                String address = beacon.getBluetoothAddress();
                if (major == 555) {
                    createNotification();
                    //beacon 의 식별을 위하여 major값으로 확인
                    //이곳에 필요한 기능 구현

                } else {
                    //나머지 비콘검색
//                    textView.append("ID 2: " + beacon.getId2() + " / " + "Distance : " + Double.parseDouble(String.format("%.3f", beacon.getDistance())) + "m\n");
//                    removeNotification();
                }

            }

        }
    };

    private void createNotification(){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Default");

        builder.setSmallIcon(R.drawable.busstop);
        builder.setFullScreenIntent(pendingIntent2, true);
        builder.setContentTitle("곧 목적지에 도착합니다.");

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        //다른 알림이 와도 알려주지 않음
        builder.setAutoCancel(true);
        //3초 뒤 자동으로 끔
        builder.setTimeoutAfter(3000);

// 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelName = "기본채널";
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("Default",channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }


        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }
}