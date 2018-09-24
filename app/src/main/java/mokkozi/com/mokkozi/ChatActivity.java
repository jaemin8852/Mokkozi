package mokkozi.com.mokkozi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.Header;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class ChatActivity extends AppCompatActivity {

    ApplicationController application;
    static Api networkService;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    EditText etText;
    ImageView btnSend;
    String email;
    String uid;
    String person;
    FirebaseDatabase database;
    List<Chat> mChat;
    ImageView beforeBtn;
    String stText;
    Calendar c;
    SimpleDateFormat df;
    String formatDate;
    SimpleDateFormat tf;
    String formatTime;
    int num, buk;
    Handler mHandler = null;
    Context context;
    ImageView exitBtn;
    String opponent;
    TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        application = ApplicationController.getInstance();
        application.buildNetworkService("18265006.ngrok.io");
        networkService = ApplicationController.getInstance().getNetworkService();
        mRecyclerView = findViewById(R.id.my_recycler_view);
        beforeBtn = findViewById(R.id.beforeBtn);
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        exitBtn = findViewById(R.id.exitButton);
        name = findViewById(R.id.name);




        beforeBtn.setOnClickListener(new View.OnClickListener() {       //before 버튼 눌렀을 때, 종료
            @Override
            public void onClick(View view) {
                finish();
            }
        });




        if (user != null) {
            email = user.getEmail();
            uid = user.getUid();
        }

        context= this;

        database.getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    num = Integer.parseInt(dataSnapshot.child("num").getValue().toString());
                    buk = Integer.parseInt(dataSnapshot.child("buk").getValue().toString());
                    opponent = dataSnapshot.child("opponent").getValue().toString();

                    if(buk == 1) name.setText(opponent +" <남한>");
                    else name.setText(opponent + " <북한>");

                    Log.d("asdfasdf","numcho!!"+Integer.toString(num));

                    mRecyclerView.setHasFixedSize(true);

                    mLayoutManager = new LinearLayoutManager(context);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mChat = new ArrayList<>();
                    mAdapter = new MyAdapter(mChat, email, ChatActivity.this);
                    mRecyclerView.setAdapter(mAdapter);

                    DatabaseReference myRef = database.getReference("chats/"+num);
                    myRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Chat chat = dataSnapshot.getValue(Chat.class);

                            mChat.add(chat);
                            mRecyclerView.scrollToPosition(mChat.size() - 1);
                            mAdapter.notifyItemInserted(mChat.size() - 1);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        etText = findViewById(R.id.etText);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                stText = etText.getText().toString().trim();
                if (etText.equals("") || stText.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {


                    c = Calendar.getInstance();
                    df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    formatDate = df.format(c.getTime());
                    tf = new SimpleDateFormat("HH:mm");
                    formatTime= tf.format(c.getTime());



                    //Restaurant POST
                    Call<Result> postCall = networkService.post_information(stText);

                    postCall.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {

                            DatabaseReference myRef = database.getReference("chats/"+num).child(formatDate);
                            if (response.isSuccessful()) {
                                Log.i("adsf", "good!");
//                                Log.i("resgood", response.body().getOriginal_sentence());
                                Log.i("resgood", response.body().toString());
                                Log.i("resgood", response.body().getSentence());
                                Log.i("resgood", response.body().getMessage());
                                Log.i("resgood", response.body().getResult());
                                if(buk == 1) stText = response.body().getSentence();
                                Hashtable<String, String> chat = new Hashtable<String, String>();
                                chat.put("email", email);
                                chat.put("text", stText);
                                chat.put("time", formatTime);
                                myRef.setValue(chat);
                                etText.setText("");
                            } else {
                                int StatusCode = response.code();
                                Hashtable<String, String> chat = new Hashtable<String, String>();
                                chat.put("email", email);
                                chat.put("text", stText);
                                chat.put("time", formatTime);
                                myRef.setValue(chat);
                                etText.setText("");
                                try {
                                    Log.i("AAAAAAA", "Status Code : " + StatusCode + " Error Message : " + response.errorBody().string());
                                } catch (IOException e) {
                                    Log.i("adsf", "Nooooo");
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            Log.i(ApplicationController.TAG, "Fail Message : " + t.getMessage());
                            DatabaseReference myRef = database.getReference("chats/"+num).child(formatDate);

                            Hashtable<String, String> chat = new Hashtable<String, String>();
                            chat.put("email", email);
                            chat.put("text", stText);
                            chat.put("time", formatTime);
                            myRef.setValue(chat);
                            etText.setText("");
                        }
                    });

                }
            }
        });


        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formatDate = df.format(c.getTime());
                tf = new SimpleDateFormat("HH:mm");
                formatTime= tf.format(c.getTime());


                DatabaseReference myRef = database.getReference("chats/"+num).child(formatDate);
                Hashtable<String, String> chat = new Hashtable<String, String>();
                chat.put("email", email);
                chat.put("text", "---------- "+email+"님이 나가셨습니다. ----------");
                chat.put("time", formatTime);
                myRef.setValue(chat);

                database.getReference("users/"+uid).child("num").setValue(0);
                database.getReference("users/"+uid).child("buk").setValue(0);
                database.getReference("users/"+uid).child("opponent").setValue("");
                database.getReference().child("users").child(uid).child("flag").setValue(0);
                finish();
            }
        });


    }
 }
