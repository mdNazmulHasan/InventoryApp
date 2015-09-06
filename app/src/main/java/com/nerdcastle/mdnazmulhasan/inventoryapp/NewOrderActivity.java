package com.nerdcastle.mdnazmulhasan.inventoryapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewOrderActivity extends AppCompatActivity {
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_order);
        linear = (LinearLayout) findViewById(R.id.brand);
        final LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        String url="http://dotnet.nerdcastlebd.com/Bazar/api/brands";
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int totalBrand=response.length();

                for(int i=0;i<totalBrand;i++){

                    try {
                        createDynamicButton(response, totalBrand, i, param);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getApplicationContext(), String.valueOf(totalBrand), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
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

    private void createDynamicButton(JSONArray response, int totalBrand, int i, LinearLayout.LayoutParams param) throws JSONException {
        Button[] btn = new Button[totalBrand];
        btn[i] = new Button(getApplicationContext());
        btn[i].setText(response.getJSONObject(i).getString("BrandName"));
        btn[i].setTextColor(Color.parseColor("#000000"));
        btn[i].setTextSize(20);
        btn[i].setHeight(100);
        btn[i].setTag(response.getJSONObject(i));
        btn[i].setLayoutParams(param);
        btn[i].setPadding(15, 5, 15, 5);
        linear.addView(btn[i]);
        btn[i].setOnClickListener(handleOnClick(btn[i]));
    }

    View.OnClickListener handleOnClick(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),button.getTag().toString(), Toast.LENGTH_LONG).show();
                JSONObject butttonData= (JSONObject) button.getTag();
                try {
                    String id=butttonData.getString("BrandId");
                    Toast.makeText(getApplicationContext(),"brandId"+id, Toast.LENGTH_LONG).show();
                    int brandId=Integer.parseInt(id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
