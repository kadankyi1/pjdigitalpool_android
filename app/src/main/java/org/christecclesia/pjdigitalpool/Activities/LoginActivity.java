package org.christecclesia.pjdigitalpool.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText m_phone_edittext, m_password_edittext;
    private Button m_login_button;
    private TextView m_signup_textview, m_forgotpassword_textview;
    private ContentLoadingProgressBar m_loggingin_contentloadingprogressbar;
    private ScrollView m_formholder_scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        m_formholder_scrollview = findViewById(R.id.loginactivity_formholder_scrollview);
        m_phone_edittext = findViewById(R.id.loginactivity_textinputedittext_phone);
        m_password_edittext = findViewById(R.id.loginactivity_textinputedittext_password);
        m_login_button = findViewById(R.id.loginactivity_button_login);
        m_loggingin_contentloadingprogressbar = findViewById(R.id.loginactivity_loader_contentloadingprogressbar);
        m_signup_textview = findViewById(R.id.loginactivity_textview_signup);
        m_forgotpassword_textview = findViewById(R.id.loginactivity_textview_forgotpassword);

        m_login_button.setOnClickListener(this);
        m_signup_textview.setOnClickListener(this);
        m_forgotpassword_textview.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == m_login_button.getId()){
            if(!m_phone_edittext.getText().toString().trim().isEmpty()
                    && !m_password_edittext.getText().toString().trim().isEmpty()){
                call_sign_in_api(m_phone_edittext.getText().toString().trim(), m_password_edittext.getText().toString().trim());
            }
        } else if(view.getId() == m_signup_textview.getId()){
            onBackPressed();
        } else if(view.getId() == m_forgotpassword_textview.getId()){
            Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            startActivity(intent);
        }
    }

    private void call_sign_in_api(String phone_number, String password){

        if(!this.isFinishing() && getApplicationContext() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    m_formholder_scrollview.setVisibility(View.INVISIBLE);
                    m_loggingin_contentloadingprogressbar.setVisibility(View.VISIBLE);
                }
            });

            Util.show_log_in_console("SignupActivity", "\n phone_number: " + phone_number
                    + "\n password: " + password);

            AndroidNetworking.post(Util.LINK_LOGIN)
                    .addBodyParameter("phone_number", phone_number)
                    .addBodyParameter("password", password)
                    .setTag("signup_request")
                    .setPriority(Priority.HIGH)
                    .build().getAsString(new StringRequestListener() {
                @Override
                public void onResponse(final String response) {
                    if(!LoginActivity.this.isFinishing() && getApplicationContext() != null){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray array = jsonObject.getJSONArray("data_returned");

                                    final JSONObject o = array.getJSONObject(0);
                                    int myStatus = o.getInt("1");
                                    final String statusMsg = o.getString("2");

                                    // GENERAL ERROR
                                    if(myStatus != 1){
                                        Toast.makeText(getApplicationContext(), statusMsg, Toast.LENGTH_LONG).show();
                                        m_loggingin_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                        m_formholder_scrollview.setVisibility(View.VISIBLE);
                                        return;
                                    } else {

                                        //STORING THE USER DATA
                                        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_TOKEN, o.getString("3"));
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "An error occurred. Try again later", Toast.LENGTH_LONG).show();
                                            m_loggingin_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                            m_formholder_scrollview.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }

                @Override
                public void onError(ANError anError) {
                    if(!LoginActivity.this.isFinishing() && getApplicationContext() != null){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();
                                m_loggingin_contentloadingprogressbar.setVisibility(View.INVISIBLE);
                                m_formholder_scrollview.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            });
        }
    }

}
