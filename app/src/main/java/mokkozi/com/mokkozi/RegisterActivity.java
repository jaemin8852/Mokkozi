package mokkozi.com.mokkozi;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    int flag = 0;
    EditText id;
    EditText pwd1;
    EditText pwd2;
    Button previous_btn;
    Button next_btn;
    Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        id = findViewById(R.id.register_id_edt);
        pwd1 = findViewById(R.id.register_pass_edt);
        pwd2 = findViewById(R.id.confirm_pass_edt);
        next_btn = findViewById(R.id.register_next_btn);
        previous_btn = findViewById(R.id.regiset_previous_btn);

        id.requestFocus();

        previous_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To do : flag가 0이면 인텐트 삭제하고 돌아가기
                //        flag가 1이면 전화번호 입력 받는 창으로 가기
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To do : flag가 0이면 인증하는 창으로 가기
                //        flag가 1이면 서버로 데이터 전송 후, 인텐트 삭제하고 로그인 화면으로 돌아가기
            }
        });

        mHandler = new Handler();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //여기서 x
               mHandler.post(new Runnable () {

                    @Override
                    public void run() {
                        while(true) {
                            if (pwd1.getText().toString().equals(pwd2.getText().toString())) {
                              Log.d("AAAAAAA", "is equal!!!!!!!!");
                            }
                            else {
                                Log.d("hello", "NOT now!!");
                            }
                        }
                    }
                });
            }
        });
        t.start();
    }
}
