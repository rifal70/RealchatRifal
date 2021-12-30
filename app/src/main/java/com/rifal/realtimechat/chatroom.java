package com.rifal.realtimechat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class chatroom extends AppCompatActivity {

    private Button btn_send_msg;
    private EditText input_msg;
    private TextView chat_conversation, chat_conversation2;
    private String user_name, room_name;
    private DatabaseReference root;
    private String temp_key;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList();
    ArrayList<String> mylist = new ArrayList<String>();
    private String chat_msg, chat_user_name;
    ListView listView;

    chatadapter chatadapter;
    RecyclerView rvList;
    RecyclerView.LayoutManager ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        btn_send_msg = findViewById(R.id.button);
        input_msg = findViewById(R.id.editText);
        chat_conversation = findViewById(R.id.textView);
        chat_conversation2 = findViewById(R.id.textView2);
        rvList = findViewById(R.id.rv_chat);

        listView = findViewById(R.id.listView);
        user_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        setTitle("Room - " + room_name);

        root = FirebaseDatabase.getInstance().getReference().child(room_name);

//        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_rooms);
//        listView.setAdapter(arrayAdapter);

        rvList.setHasFixedSize(true);
        ll = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(ll);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentDateandTime2 = sdf2.format(new Date());

        Log.d("date", "onCreate: " + currentDateandTime);

        rvList.smoothScrollToPosition(999999999);

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (input_msg.getText().length() == 0) {
                    Toast.makeText(chatroom.this, "jangan sampe kosong njer", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> map = new HashMap<String, Object>();
                    temp_key = root.push().getKey();
                    root.updateChildren(map);

                    DatabaseReference message_root = root.child(temp_key);
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("date", currentDateandTime);
                    map2.put("time", currentDateandTime2);
                    map2.put("name", user_name);
                    map2.put("msg", input_msg.getText().toString());
                    message_root.updateChildren(map2);

                    input_msg.setText("");

                    rvList.smoothScrollToPosition(999999999);

//                    Intent i = new Intent(chatroom.this, chatroom.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    i.putExtra("room_name", room_name);
//                    i.putExtra("user_name", user_name);
//                    startActivity(i);
                }
            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Set<String> set = new HashSet<String>();
//
//                Iterator i = dataSnapshot.getChildren().iterator();
//                while (i.hasNext()) {
//                    chat_msg = (String) ((DataSnapshot) i.next()).getValue();
//                    chat_user_name = (String) ((DataSnapshot) i.next()).getValue();
//
//                    set.add(chat_user_name + "    " + chat_msg);
////                    set.add(String.valueOf(((DataSnapshot) i.next()).getValue()));
//                }
//                list_of_rooms.clear();
//                list_of_rooms.addAll(set);

                mylist.clear();
                for (DataSnapshot dns : dataSnapshot.getChildren()) {
                    dns.child("date").getValue();
                    Log.d("AAA", "onDataChange: " + dns);

                    mylist.add(String.valueOf(dns)); //this adds an element to the list.
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(mylist);
                chatadapter = new chatadapter(chatroom.this, list_of_rooms, genProductAdapterListener());
                rvList.setAdapter(chatadapter);
                chatadapter.notifyDataSetChanged();


                Log.d("array", "onDataChange: " + list_of_rooms);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private AdapterOnItemClickListener genProductAdapterListener() {
        return new AdapterOnItemClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        };
    }

    private void append_chat_conversation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            chat_msg = (String) ((DataSnapshot) i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot) i.next()).getValue();
//            chat_conversation.append(chat_user_name.substring(0,15) + "\n");
//            chat_conversation2.append(chat_msg + "\n");
        }

        chat_conversation.setText(chat_user_name);
        chat_conversation2.setText(chat_msg);
        Log.d("TAG", "chat_msg: " + chat_msg + dataSnapshot.getValue());
        Log.d("TAG", "chat_user_name: " + chat_user_name + dataSnapshot.getKey());
    }
}
