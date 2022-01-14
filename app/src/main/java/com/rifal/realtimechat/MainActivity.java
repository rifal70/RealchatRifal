package com.rifal.realtimechat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.core.Context;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button add_room, btnmap;
    EditText room_name;
    ListView listView;
    String name;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("DeviceModel", "onCreate: " + getDeviceName());

        btnmap = findViewById(R.id.btn_maps);
        add_room = findViewById(R.id.btn_add_room);
        room_name = findViewById(R.id.etNeme_room);
        listView = findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_rooms);
        listView.setAdapter(arrayAdapter);

//        MyFirebaseMessagingService.getToken(context);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Log.e("newToken", newToken);
            getPreferences(MODE_PRIVATE).edit().putString("fb", newToken).apply();
            request_user_name(newToken);
        });

        Toast.makeText(this, " " + FirebaseDatabase.getInstance().getReference().getRoot(), Toast.LENGTH_LONG).show();

        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (room_name.getText().length() == 0) {
                    Toast.makeText(MainActivity.this, "jangan sampe kosong njer", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(room_name.getText().toString(), "");
                    root.updateChildren(map);
                }
            }
        });

        btnmap.setOnClickListener(v->
        {
            Intent i = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(i);
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    set.add(((DataSnapshot) i.next()).getKey());
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent I = new Intent(getApplicationContext(), chatroom.class);
                I.putExtra("room_name", ((TextView) view).getText().toString());
                I.putExtra("user_name", name);
                startActivity(I);
            }
        });

    }

    private void request_user_name(String token) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Enter Name");
//        final EditText input_field = new EditText(this);
//        builder.setView(input_field);
//        builder.setPositiveButton("OK ", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                name = input_field.getText().toString();
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.cancel();
//                request_user_name(token);
//            }
//        });
//        builder.show();
        name = token;
    }


    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
