package com.nerdcastle.mdnazmulhasan.inventoryapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    TextInputLayout usernameWrapper;
    TextInputLayout passwordWrapper;
    String userId;
    String password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        usernameWrapper.setHint("Username");
        passwordWrapper.setHint("Password");

    }

    public void login(View view) throws JSONException {
        userId = usernameWrapper.getEditText().getText().toString();
        password = passwordWrapper.getEditText().getText().toString();

        JSONObject requestJsonObject=new JSONObject();
        requestJsonObject.put("UserId", userId);
        requestJsonObject.put("Password",password);
        String url = "http://dotnet.nerdcastlebd.com/Bazar/api/users";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,
                requestJsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Boolean result=jsonObject.getBoolean("ResultState");
                    String userId=jsonObject.getString("UserId");
                    String token=jsonObject.getString("Token");
                    SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE).edit();
                    editor.putString("Token",token);
                    editor.commit();
                    System.out.println(userId);
                    //Toast.makeText(getApplicationContext(),jsonObject.toString(), Toast.LENGTH_LONG).show();
                    if(result){
                      //  Toast.makeText(getApplicationContext(),result.toString(), Toast.LENGTH_LONG).show();
                        Intent i=new Intent(getApplicationContext(),DashboardActivity.class);
                        i.putExtra("UserId",userId);
                        i.putExtra("Token",token);
                        startActivity(i);
                    }
                    else if(!result){
                        Toast.makeText(getApplicationContext(),"Incorrect Username or Password", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError instanceof NoConnectionError) {
                    String msg = "No internet Access, Check your internet connection.";
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
                if(volleyError instanceof TimeoutError) {
                    String msg = "Request Timed Out, Pls try again";
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(request);

        //Toast.makeText(getApplicationContext(), request.toString(), Toast.LENGTH_LONG).show();
    }
}