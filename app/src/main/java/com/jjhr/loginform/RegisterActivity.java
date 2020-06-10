package com.jjhr.loginform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText txt_Email,txt_Password,txt_cfm_Password;
    Button btn_Register;
    TextView TextViewSignIn;
    String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txt_Email=findViewById(R.id.email_reg);
        txt_Password=findViewById(R.id.password_reg);
        txt_cfm_Password=findViewById(R.id.cfm_password_reg);
        btn_Register=findViewById(R.id.btn_register);
        TextViewSignIn=findViewById(R.id.text_btn_si);
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailStr=txt_Email.getText().toString();
                final String passStr=txt_Password.getText().toString();
                final String cfmPassStr=txt_cfm_Password.getText().toString();

                validateSuccess(emailStr,passStr,cfmPassStr);
            }
        });
        TextViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
    }

    private void validateSuccess(final String emailStr, final String passStr, final String cfmPassStr) {
        if (emailStr.isEmpty()||!isValidEmailId(emailStr.trim())){
            txt_Email.setError("Invalidate Email");
        }else if (passStr.isEmpty()){
            txt_Password.setError("Invalidate Password");
        }else if (cfmPassStr.isEmpty()||!cfmPassStr.matches(passStr)){
            txt_cfm_Password.setError("Invalidate Password");
        }else {

            StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    txt_Email.setText("");
                    txt_Password.setText("");
                    txt_cfm_Password.setText("");
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<String,String>();

                    params.put("email",emailStr);
                    params.put("password",passStr);
                    params.put("conform password",cfmPassStr);
                    return params;
                }
            };

            RequestQueue requestQueue= Volley.newRequestQueue(RegisterActivity.this);
            requestQueue.add(request);
        }
    }

    private boolean isValidEmailId(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}