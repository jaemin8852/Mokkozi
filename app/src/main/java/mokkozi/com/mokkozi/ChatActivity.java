package mokkozi.com.mokkozi;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

//import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
//import kr.co.shineware.nlp.komoran.core.Komoran;
//import kr.co.shineware.nlp.komoran.model.Token;


public class ChatActivity extends AppCompatActivity {

    ApplicationController application;
    static Api networkService;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    String[] myDataset = {"안녕", "오늘", "뭐했어", "영화볼래?"};
    EditText etText;
    Button btnSend;
    String email;
    FirebaseDatabase database;
    List<Chat> mChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
//        ButterKnife.bind(this);

        application = ApplicationController.getInstance();
        application.buildNetworkService("b41eb0a1.ngrok.io");
        //application.buildNetworkService("자신의 ip", 8000);
        networkService = ApplicationController.getInstance().getNetworkService();
        mRecyclerView = findViewById(R.id.my_recycler_view);
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();

//            // Check if user's email is verified
//            boolean emailVerified = user.isEmailVerified();
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            String uid = user.getUid();
        }
        etText = findViewById(R.id.etText);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String stText = etText.getText().toString();
                if (etText.equals("") || stText.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), email + "," + stText, Toast.LENGTH_SHORT).show();
                    // Write a message to the database

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formatDate = df.format(c.getTime());

                    DatabaseReference myRef = database.getReference("chats").child(formatDate);

                    Hashtable<String, String> chat = new Hashtable<String, String>();
                    chat.put("email", email);
                    chat.put("text", stText);
                    myRef.setValue(chat);
                    etText.setText("");

//                    Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
//                    komoran.setUserDic("user_data/dic.user");
//                    List<Token> tokens = komoran.analyze("청하는아이오아이출신입니다").getTokenList();
//                    for(Token token : tokens)
//                        Log.d("token : ", token.toString());
                }
            }
        });

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mChat = new ArrayList<>();
        mAdapter = new MyAdapter(mChat, email, ChatActivity.this);
        mRecyclerView.setAdapter(mAdapter);

            DatabaseReference myRef = database.getReference("chats");
            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Chat chat = dataSnapshot.getValue(Chat.class);

                    mChat.add(chat);
                    mRecyclerView.scrollToPosition(mChat.size() - 1);
                    mAdapter.notifyItemInserted(mChat.size() - 1);
                }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Restaurant POST
        Information information = new Information("소정아 이거 될 각이다", "helpmeplz!", "Idontwant@naver.com", "여자");

        Call<Information> postCall = networkService.post_information("소정아 이거 될 각이다", "helpmeplz!", "Idontwant@naver.com", "여자");

        postCall.enqueue(new Callback<Information>() {
            @Override
            public void onResponse(Call<Information> call, Response<Information> response) {
                if (response.isSuccessful()) {
                    Log.i("adsf", "good!");
//                    Log.i("adsf", response.body().getEmail());
//                    Log.i("adsf", response.body().getGender());
//                    Log.i("adsf", response.body().getId());
//                    Log.i("adsf", response.body().getPw());
                } else {
                    int StatusCode = response.code();
                    try {
                        Log.i("AAAAAAA", "Status Code : " + StatusCode + " Error Message : " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.i("adsf", "Nooooo");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Information> call, Throwable t) {
                Log.i(ApplicationController.TAG, "Fail Message : " + t.getMessage());
            }
        });
    }
 }
