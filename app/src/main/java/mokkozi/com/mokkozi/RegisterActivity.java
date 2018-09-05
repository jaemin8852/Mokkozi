package mokkozi.com.mokkozi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    EditText pwd1;
    EditText pwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        pwd1 = findViewById(R.id.register_pass_edt);
        pwd2 = findViewById(R.id.confirm_pass_edt);
        /*버튼 내에서 동작
        if(pwd1.getText().toString().equals(pwd2.getText().toString())) {

        }*/

    }
}
