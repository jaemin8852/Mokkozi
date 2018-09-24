package mokkozi.com.mokkozi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Hashtable;

public class LoginActivity extends AppCompatActivity {
    ProgressBar pbLogin;
    TextView go_re_txt;
    Button login_btn;
    EditText id;
    EditText pwd;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String TAG = "LoginActivity ";
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        pbLogin = findViewById(R.id.pbLogin);
        go_re_txt = findViewById(R.id.go_register_txt);
        login_btn = findViewById(R.id.login_btn);
        id = findViewById(R.id.login_id_edt);
        pwd = findViewById(R.id.login_pass_edt);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    SharedPreferences sharedPreferences = getSharedPreferences("uid", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uid", user.getUid());
                    editor.putString("email", user.getEmail());
                    editor.apply();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        go_re_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : 회원가입 창이 만들어지면 거기로 보내면 됨.
            }
        });

         login_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //TODO : 회원이 맞는지 체크하고 참이면 메인 아니면 틀렸다는 토스트 메시지
                 pbLogin.setVisibility(View.VISIBLE);
                 mAuth.signInWithEmailAndPassword(id.getText().toString(), pwd.getText().toString())
                         .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                             @Override
                             public void onComplete(@NonNull Task<AuthResult> task) {
                                 Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());


                                 // If sign in fails, display a message to the user. If sign in succeeds
                                 // the auth state listener will be notified and logic to handle the
                                 // signed in user can be handled in the listener.
                                 if (!task.isSuccessful()) {
                                     pbLogin.setVisibility(View.GONE);
                                     Log.w(TAG, "signInWithEmail:failed", task.getException());
                                     Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                             Toast.LENGTH_SHORT).show();
                                 }
                                 else{
                                     pbLogin.setVisibility(View.GONE);
                                     /*Intent intent=new Intent(LoginActivity.this, ChatActivity_main.class);
                                     startActivity(intent);*/


                                 }

                                 // ...
                             }
                         });
             }
         });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
