package mokkozi.com.mokkozi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    TextView go_re_txt;
    Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        go_re_txt = findViewById(R.id.go_register_txt);
        login_btn = findViewById(R.id.login_btn);

        go_re_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To do : 회원가입 창이 만들어지면 거기로 보내면 됨.
            }
        });

         login_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //To do : 회원이 맞는지 체크하고 참이면 메인 아니면 틀렸다는 토스트 메시지
             }
         });
    }
}
