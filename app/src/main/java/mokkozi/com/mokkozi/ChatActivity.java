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
                if(etText.equals("") || stText.isEmpty()){
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), email+"," +stText, Toast.LENGTH_SHORT).show();
                    // Write a message to the database

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formatDate = df.format(c.getTime());

                    DatabaseReference myRef = database.getReference("chats").child(formatDate);

                    Hashtable<String, String> chat = new Hashtable<String, String>();
                    chat.put("email", email);
                    chat.put("text", stText);
                    myRef.setValue(chat);
                }
            }
        });

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager= new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mChat = new ArrayList<>();
        mAdapter = new MyAdapter(mChat);
        mRecyclerView.setAdapter(mAdapter);

        DatabaseReference myRef = database.getReference("chats");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);

                mChat.add(chat);
                mAdapter.notifyItemInserted(mChat.size()-1);
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

        //sendHttpWithMsg("http://5ba91c1f.ngrok.io/api/signup/");
//        try {
//            sendHttpWithMsg();
//        }catch (Exception e){
//            e.getStackTrace();
//        }
      // POST("http://dca9b1b9.ngrok.io/");

//        new NetworkTask(new JSONObject()).execute();

    }

//    public static String POST(String url1) {
//
//        //Restaurant POST
//        Information information = new Information("소정아 이거 될 각이다", "helpmeplz!", "Idontwant@naver.com", "여자");
//
//        Call<Information> postCall = networkService.post_information(information);
//
//        postCall.enqueue(new Callback<Information>() {
//            @Override
//            public void onResponse(Call<Information> call, Response<Information> response) {
//                if (response.isSuccessful()) {
//
//                } else {
//                    int StatusCode = response.code();
//                    try {
//                        Log.i(ApplicationController.TAG, "Status Code : " + StatusCode + " Error Message : " + response.errorBody().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Information> call, Throwable t) {
//                Log.i(ApplicationController.TAG, "Fail Message : " + t.getMessage());
//            }
//        });
//    return "Adsf";



                             //        String json = "";
//        try {
//            URL url = new URL(url1);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestProperty("User-Agent", "");
//            connection.setRequestMethod("POST");                                                            //여기는 한번 coonect 시험해본 곳
//            connection.setDoInput(true);
//            connection.connect();
//
//
//
//
//            Log.d("REQUESTOUTPUT", "requesting");
//
//
//        } catch (IOException e) {
//            // writing exception to log
//            e.printStackTrace();
//        }
//
//        InputStream is = null;                                                            //여기부터는 Json 통신 시험해본 곳
//        String result = "";
//        try {
//            URL urlCon = new URL(url1);
//            HttpURLConnection httpCon = (HttpURLConnection) urlCon.openConnection();
//            Log.d("asdf", httpCon.toString());



            // build jsonObject
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.accumulate("id", "tetestete");
//            jsonObject.accumulate("password", "@naver1107");
//            jsonObject.accumulate("email", "asdf@naver.com");
//            jsonObject.accumulate("gender", "여자");

            // convert JSONObject to JSON to String
//            Log.d("asdf", String.valueOf(jsonObject));
//            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // Set some headers to inform server about the type of the content
           // httpCon.setRequestProperty("Accept", "application/json");
//            httpCon.setRequestProperty("Content-type", "application/json; charset=UTF-8");
           // httpCon.setRequestProperty("User-Agent", "");
//            httpCon.setRequestMethod("POST");

            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
//            httpCon.setDoOutput(true);
            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
//            httpCon.setDoInput(true);
           // httpCon.connect();
//            Log.d("asdf", "two");
            //OutputStream os = httpCon.getOutputStream();
//            Log.d("AAAA", json.toString());
            //byte[] outputBytes = jsonObject.toString().getBytes("UTF-8");
            //Log.d("dsds", outputBytes.toString());

           // os.write(json.getBytes("UTF-8"));
            //os.write("123456789".getBytes("UTF-8"));
            //os.close();
//            os.flush();
            // receive response as inputStream
//            try {
//                String otherParametersUrServiceNeed =  "{\"jsonrpc\": \"1.0\", \"method\": \"HelloMethod\", \"params\": \"{}\" }";
//
//                DataOutputStream wr = new DataOutputStream(httpCon.getOutputStream());
//
//                Log.d("asdf", "0");
//                //wr.writeBytes(otherParametersUrServiceNeed);
//                //wr.writeBytes(URLEncoder.encode(json, "UTF-8"));
//                wr.writeBytes(json);
//
//
//            //wr.flush();
//            wr.close();
//
//
//                is = httpCon.getInputStream();
//                // convert inputstream to string
//                if (is != null)
//                    ;
//                    //result = convertInputStreamToString(is);
//                else
//                    result = "Did not work!";
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                httpCon.disconnect();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
            //Log.d("InputStream", e.getLocalizedMessage());
       //     ;
//        }


//
//        return result;

//    public String sendHttpWithMsg() throws MalformedURLException {
//        String otherParametersUrServiceNeed =  "{\"jsonrpc\": \"1.0\", \"method\": \"HelloMethod\", \"params\": \"{}\" }";
//        String request = "http://5ba91c1f.ngrok.io/api/signup/";
//
//        try {
//
//            Log.d("asdf", "3");
//
//            URL url = new URL(request);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoOutput(true);
//            connection.setDoInput(true);
//            connection.setInstanceFollowRedirects(false);
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            connection.setRequestProperty("charset", "utf-8");
//            connection.setRequestProperty("Content-Length", "" + Integer.toString(request.getBytes().length));
//            connection.setUseCaches(false);
//
//
//            Log.d("asdf", "2");
//
//            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
//
//            Log.d("asdf", "0");
//
//
//            wr.writeBytes(otherParametersUrServiceNeed);
//
//            Log.d("asdf", "1");
//
//            JSONObject jsonParam = new JSONObject();
//            jsonParam.put("id", "1234Hello");
//            jsonParam.put("password", "@naver1107");
//            jsonParam.put("email", "1234@naver.com");
//            jsonParam.put("gender", "여자");
//
//            Log.d("asdf", jsonParam.toString());
//
//            wr.writeBytes(jsonParam.toString());
//
//            wr.flush();
//            wr.close();
//        }
//        catch (Exception e){
//            Log.d("asdf", e.getStackTrace().toString());
//        }
//        return json;
//    }
 //   }
//    class NetworkTask extends AsyncTask<Void, Void, String> {
//
//        private JSONObject jsonObject;
//
//        public NetworkTask(JSONObject jsonObject ) {
//            this.jsonObject = jsonObject;
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            String result = POST("http://5ba91c1f.ngrok.io/api/signup/");
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            //super.onPostExecute(s);
//
//            Log.d("MOKKOZI HTTP RES",s); // -> a
//        }
//    }
 }
