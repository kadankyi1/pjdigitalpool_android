package org.christecclesia.pjdigitalpool.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.christecclesia.pjdigitalpool.Inc.MyLifecycleHandler;
import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText m_first_name_editext, m_last_name_edittext, m_country_edittext, m_phone_edittext, m_email_edittext, m_password_edittext, m_password_confirm_editText;
    private Button m_signup_button;
    private TextView m_login_textview;
    private ContentLoadingProgressBar m_signingup_contentloadingprogressBar;
    private ScrollView m_formholder_scrollview;
    private NumberPicker.OnValueChangeListener mNumberSetListener;
    private int default_country = 0;
    private String country_code = "", country = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        m_formholder_scrollview = findViewById(R.id.signupactivity_formholder_scrollview);
        m_login_textview = findViewById(R.id.signupactivity_textview_login);
        m_first_name_editext = findViewById(R.id.signupactivity_textinpputedittext_firstname);
        m_last_name_edittext = findViewById(R.id.signupactivity_textinputedittext_lastname);
        m_country_edittext = findViewById(R.id.signupactivity_textinputedittext_country);
        m_phone_edittext = findViewById(R.id.signupactivity_textinputedittext_phone);
        m_email_edittext = findViewById(R.id.signupactivity_textinputedittext_email);
        m_password_edittext = findViewById(R.id.signupactivity_textinputedittext_password);
        m_password_confirm_editText = findViewById(R.id.signupactivity_textinputedittext_confirmpassword);
        m_signup_button = findViewById(R.id.signupactivity_button_signup);
        m_signingup_contentloadingprogressBar = findViewById(R.id.signupactivity_loader_contentloadingprogressbar);

        m_signup_button.setOnClickListener(this);
        m_login_textview.setOnClickListener(this);

        final String[] countriesStringArraySet = getResources().getStringArray(R.array.countries_array_starting_with_choose_country);
        final List<String> countriesStringArrayList = Arrays.asList(countriesStringArraySet);
        final List<String> countriesCodesStringArrayList = Arrays.asList(getResources().getStringArray(R.array.countries_array_starting_with_choose_country));

        m_country_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberSetListener = openNumberPickerForCountries(mNumberSetListener, 0, countriesStringArraySet.length-1, true, getResources().getStringArray(R.array.countries_array_starting_with_choose_country), default_country);
            }
        });

        mNumberSetListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                default_country = newVal;
                m_country_edittext.setText(countriesStringArrayList.get(newVal));
                country = countriesStringArrayList.get(newVal);
                country_code = countriesCodesStringArrayList.get(newVal);
            }
        };

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == m_signup_button.getId()){
            if(!m_first_name_editext.getText().toString().trim().isEmpty()
                    && !m_last_name_edittext.getText().toString().trim().isEmpty()
                    && !country.trim().isEmpty()
                    && !country_code.trim().isEmpty()
                    && !m_phone_edittext.getText().toString().trim().isEmpty()
                    && !m_country_edittext.getText().toString().trim().isEmpty()
                    && !m_country_edittext.getText().toString().trim().isEmpty()
                    && !m_password_edittext.getText().toString().trim().isEmpty()
                    && !m_password_confirm_editText.getText().toString().trim().isEmpty()
                    && m_password_edittext.getText().toString().equals(m_password_confirm_editText.getText().toString())){
                call_sign_up_api(m_first_name_editext.getText().toString().trim(), m_last_name_edittext.getText().toString().trim(),
                        m_phone_edittext.getText().toString().trim(), m_country_edittext.getText().toString().trim(),
                        m_password_edittext.getText().toString().trim());
            } else if(!m_password_edittext.getText().toString().equals(m_password_confirm_editText.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Complete the form", Toast.LENGTH_LONG).show();
            }
        } else if(view.getId() == m_login_textview.getId()){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            startActivity(intent);
        }
    }

    public NumberPicker.OnValueChangeListener openNumberPickerForCountries(NumberPicker.OnValueChangeListener  mNumberSetListener, int minNumber, int maxNumber, Boolean disNumbersOnUiToUser, String[] displayStringsValues, int defaultCountry) {

        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog_picker);
        Button b1 = d.findViewById(R.id.dialog_button);
        final NumberPicker np = d.findViewById(R.id.numberpicker);
        np.setMaxValue(maxNumber);
        np.setMinValue(minNumber);
        np.setWrapSelectorWheel(false);
        np.setValue(defaultCountry);
        if(disNumbersOnUiToUser){
            np.setDisplayedValues(displayStringsValues);
        }
        np.setOnValueChangedListener(mNumberSetListener);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

        return mNumberSetListener;
    }

    private void call_sign_up_api(String first_name, String last_name, String phone_number, String country, String password){

        if(!this.isFinishing() && getApplicationContext() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    m_formholder_scrollview.setVisibility(View.INVISIBLE);
                    m_signingup_contentloadingprogressBar.setVisibility(View.VISIBLE);
                }
            });

            Util.show_log_in_console("SignupActivity","\n first_name: " + first_name
                    + "\n last_name: " + last_name
                    + "\n phone_number: " + phone_number
                    + "\n country: " + country
                    + "\n password: " + password);

            AndroidNetworking.post(Util.LINK_SIGNUP)
                    .addBodyParameter("first_name", first_name)
                    .addBodyParameter("last_name", last_name)
                    .addBodyParameter("phone_number", phone_number)
                    .addBodyParameter("country", country)
                    .addBodyParameter("password", password)
                    .setTag("signup_request")
                    .setPriority(Priority.HIGH)
                    .build().getAsString(new StringRequestListener() {
                @Override
                public void onResponse(final String response) {
                    if(!SignupActivity.this.isFinishing() && getApplicationContext() != null){
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
                                        m_signingup_contentloadingprogressBar.setVisibility(View.INVISIBLE);
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
                                            m_signingup_contentloadingprogressBar.setVisibility(View.INVISIBLE);
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
                    if(!SignupActivity.this.isFinishing() && getApplicationContext() != null){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();
                                m_signingup_contentloadingprogressBar.setVisibility(View.INVISIBLE);
                                m_formholder_scrollview.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            });
        }
    }

}
