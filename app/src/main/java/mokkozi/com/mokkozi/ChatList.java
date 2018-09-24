package mokkozi.com.mokkozi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Hashtable;

import static android.content.ContentValues.TAG;

public class ChatList extends AppCompatActivity {

    FirebaseDatabase database;
    String uid;
    String email;
    TextView match;
    DatabaseReference myRef;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        match = findViewById(R.id.match);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            uid = user.getUid();
            email= user.getEmail();
        }



        match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                flag = true;

                database.getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        try {
                            Log.d("flagflag1", dataSnapshot.child("flag").getValue().toString());
                            int f = Integer.parseInt(dataSnapshot.child("flag").getValue().toString());

                            if (f == 0) {
                                flag = false;

                            }
                            if(!flag) {
                                myRef = database.getReference("queue");


                                myRef.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                        ChatUser chatUser = dataSnapshot.getValue(ChatUser.class);

                                        if(chatUser.getNum() % 2 == 0) {
                                            int num = chatUser.getNum();
                                            database.getReference().child("queue").child("queue").child("email").setValue(email);
                                            database.getReference().child("queue").child("queue").child("uid").setValue(uid);
                                            database.getReference().child("queue").child("queue").child("num").setValue(num+1);
                                            database.getReference().child("users").child(uid).child("flag").setValue(1);
                                            finish();
                                        }
                                        else{
                                            int num = chatUser.getNum();
                                            database.getReference("users/"+uid).child("num").setValue(num/2);
                                            database.getReference("users/"+chatUser.getUid()).child("num").setValue(num/2);
                                            database.getReference("users/"+uid).child("opponent").setValue(chatUser.getEmail());
                                            database.getReference("users/"+chatUser.getUid()).child("opponent").setValue(email);
                                            database.getReference("users/"+uid).child("buk").setValue(1);
                                            database.getReference("users/"+chatUser.getUid()).child("buk").setValue(0);
                                            database.getReference().child("queue").child("queue").child("email").setValue("");
                                            database.getReference().child("queue").child("queue").child("uid").setValue("");
                                            database.getReference().child("queue").child("queue").child("num").setValue(num+1);
                                            database.getReference().child("users").child(uid).child("flag").setValue(1);
                                            Intent intent = new Intent(ChatList.this, ChatActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
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
                            else{
                                Toast.makeText(getApplicationContext(), "매칭하는 중입니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
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
            }
        });

//        myRef = database.getReference("user");
//
//        myRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                if(flag) {
//                    ChatUser chatUser = dataSnapshot.getValue(ChatUser.class);
//
//                    if (chatUser != null) {
//                        Log.i("asdfasdf", chatUser.getUid());
//                    } else {
//                        Log.d("asdfasdf", "nullnull");
//                    }
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {}
//        });
    }



}
