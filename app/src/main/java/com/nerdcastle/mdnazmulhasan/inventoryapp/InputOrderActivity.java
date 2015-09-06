package com.nerdcastle.mdnazmulhasan.inventoryapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import static android.graphics.Color.*;

public class InputOrderActivity extends Activity {
    int i = 0;
    List<EditText> allEds = new ArrayList<EditText>();
    List<String> allEdsData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_order);
        String url="http://dotnet.nerdcastlebd.com/Bazar/api/products?brandId=6";
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                createDynamicForm(response);
            }

            private void createDynamicForm(JSONArray response) {
                LinearLayout ll = (LinearLayout) findViewById(R.id.mainlayout);
                for (int i = 0; i < response.length(); i++) {
                    TextView tv = new TextView(getApplicationContext());
                    try {
                        tv.setText(response.getJSONObject(i).getString("ProductName"));
                        tv.setTextColor(BLACK);
                        ll.addView(tv);
                        EditText et = new EditText(getApplicationContext());
                        et.setTextColor(BLACK);
                        allEds.add(et);
                        et.setId(i);
                        et.setInputType(InputType.TYPE_CLASS_NUMBER);
                        et.setTag(response.getJSONObject(i));
                        et.setBackgroundResource(R.drawable.rounded_edittext);
                        ll.addView(et);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Button add_btn = new Button(getApplicationContext());
                add_btn.setText("Submit");
                ll.addView(add_btn);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(request);

    }

}