package mokkozi.com.mokkozi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    int flag = 0;
    EditText id;
    EditText pwd1;
    EditText pwd2;
    ImageButton previous_btn;
    Button next_btn;
    Handler mHandler = null;
    FrameLayout framelayout1;
    FrameLayout framelayout2;
    FrameLayout framelayout3;
    RadioGroup rg;
    EditText name;
    Button birthdaybtn;
    int y = -1, m = -1, d = -1;
    RadioButton selectedRdo = null;
    CheckBox checkBox1, checkBox2;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String TAG = "RegisterActivity ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        id = findViewById(R.id.register_id_edt);
        pwd1 = findViewById(R.id.register_pass_edt);
        pwd2 = findViewById(R.id.confirm_pass_edt);
        next_btn = findViewById(R.id.register_next_btn);
        previous_btn = findViewById(R.id.register_previous_btn);
        framelayout1 = findViewById(R.id.frame_layout1);
        framelayout2 = findViewById(R.id.frame_layout2);
        framelayout3 = findViewById(R.id.frame_layout3);
        rg = findViewById(R.id.gender);
        name = findViewById(R.id.name);
        birthdaybtn = findViewById(R.id.birthday);
        checkBox1 = findViewById(R.id.checkBox);
        checkBox2 = findViewById(R.id.checkBox2);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        id.requestFocus();

        previous_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //뒤로 가기 버튼을 눌렀을 때
                previous_func();
            }
        });

        birthdaybtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(1); // 날짜 설정 다이얼로그 띄우기
            }
        });

        RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

        }};

        rg.setOnCheckedChangeListener(radioGroupButtonChangeListener);


        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //다음 버튼을 눌렀을 때
                //        flag가 0이면 이메일 입력 받는 창으로 가기
                //        flag가 1이면 인증하는 창으로 가기
                //        flag가 2이면 서버로 데이터 전송 후, 인텐트 삭제하고 로그인 화면으로 돌아가기

                if(flag == 0){
                    if(id.getText().toString().trim().length() == 0){
                        Toast.makeText(getApplicationContext(), "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                        id.requestFocus();
                    }
                    else if(pwd1.getText().toString().trim().length() < 6){
                        Toast.makeText(getApplicationContext(), "비밀번호는 6자리 이상 입력하세요.", Toast.LENGTH_SHORT).show();
                        pwd1.requestFocus();
                    }
                    else if(!pwd1.getText().toString().trim().equals(pwd2.getText().toString().trim())){
                        Toast.makeText(getApplicationContext(), "비밀번호와 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        pwd2.requestFocus();
                    }
                    else {
                        next_btn.setY(880);
                        framelayout1.setVisibility(View.GONE);
                        framelayout2.setVisibility(View.VISIBLE);
                        flag = 1;
                    }
                }
                else if(flag == 1){
                    //TODO 서버와 통신

                    if(name.getText().toString().length() == 0){
                        Toast.makeText(getApplicationContext(), "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                        name.requestFocus();
                    }
                    else if(y == -1) {
                        Toast.makeText(getApplicationContext(), "생년월일을 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                    else if(!checkBox1.isChecked() || !checkBox2.isChecked()) {
                        Toast.makeText(getApplicationContext(), "이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.d("asdf", Integer.toString(rg.getCheckedRadioButtonId()));
                        selectedRdo = findViewById(rg.getCheckedRadioButtonId());
                        String gender_str = selectedRdo.getText().toString();
                        String name_str = name.getText().toString();

                        mAuth.createUserWithEmailAndPassword(id.getText().toString(), pwd1.getText().toString())
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, R.string.auth_failed,
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            finish();
                                        }

                                        // ...
                                    }
                                });

//                        Retrofit retrofit = new Retrofit.Builder()
//                                .baseUrl(Api.BASE_URL)
//                                .addConverterFactory(GsonConverterFactory.create())
//                                .build();
//
//                        Api api = retrofit.create(Api.class);
//
//                        Call<List<Information>> call = api.getsignupInformations();
//
//                        call.enqueue(new Callback<List<Information>>() {
//                            @Override
//                            public void onResponse(Call<List<Information>> call, Response<List<Information>> response) {
//                                List<Information> info = response.body();
//                            }
//
//                            @Override
//                            public void onFailure(Call<List<Information>> call, Throwable t) {
//
//                            }
//                        });
                    }
                }
            }
        });

//        mHandler = new Handler();
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //여기서 x
//               mHandler.post(new Runnable () {
//
//                    @Override
//                    public void run() {
//                        while(true) {
//                            try {
//                                Thread.sleep(100);
//                            }catch (Exception e){
//                                e.getStackTrace();
//                            }
//                            if (pwd1.getText().toString().equals(pwd2.getText().toString())) {
//                              Log.d("AAAAAAA", "is equal!!!!!!!!");
//                            }
//                            else {
//                                Log.d("hello", "NOT now!!");
//                            }
//                        }
//                    }
//                });
//            }
//        });
//        t.start();
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                DatePickerDialog dpd = new DatePickerDialog
                        (RegisterActivity.this, // 현재화면의 제어권자
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear, int dayOfMonth) {
                                        Toast.makeText(getApplicationContext(),
                                                year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일 을 선택했습니다",
                                                Toast.LENGTH_SHORT).show();
                                        y = year;
                                        m = monthOfYear+1;
                                        d=dayOfMonth;
                                    }
                                }
                                , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                                //    호출할 리스너 등록
                                2001, 1, 1); // 기본값 연월일
                return dpd;
        }

        return super.onCreateDialog(id);
    }

    @Override
    public void onBackPressed() {
        previous_func();
    }

    public void previous_func(){
        //        flag가 0이면 인텐트 삭제하고 돌아가기
        //        flag가 1이면 이메일 입력 받는 창으로 가기
        //        flag가 2이면 이름과 성별 받는 창으로 가기

        if(flag == 0){
            finish();
        }
        else if(flag == 1){
            next_btn.setY(665);
            framelayout1.setVisibility(View.VISIBLE);
            framelayout2.setVisibility(View.GONE);
            id.requestFocus();
            flag = 0;
        }
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
