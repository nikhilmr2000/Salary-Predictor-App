package com.example.salarypredictiom;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    boolean expB =false;
    boolean nameB = false;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    final OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button predict = findViewById(R.id.predict);
        predict.setBackgroundColor(Color.GREEN);

        Button exit = findViewById(R.id.exit);
        EditText exp = findViewById(R.id.exp);
        EditText name = findViewById(R.id.name);
        TextView ctext = findViewById(R.id.cname);
        TextView etext = findViewById(R.id.ename);

        Button predictBut = (Button) findViewById(R.id.predict);
        exit.setBackgroundColor(Color.RED);

        MainActivity activity = new MainActivity();

        // on below line we are initializing our variables.
        Button closeapp = findViewById(R.id.exit);
        // on below line we are adding click listener for our button
        closeapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are finishing activity.
                MainActivity.this.finish();

                // on below line we are exiting our activity
                System.exit(0);
            }
        });

    ///ADD
        TextView output=findViewById(R.id.output);
    etext.setVisibility(View.INVISIBLE);
    output.setVisibility(View.INVISIBLE);

    predictBut.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (exp.getText().toString().length() <= 0) {
                expB = true;
                etext.setVisibility(View.VISIBLE);
            } else {
                etext.setVisibility(View.INVISIBLE);
                expB = false;
            }
            if (name.getText().toString().length() <= 0) {
                ctext.setVisibility(View.VISIBLE);
                nameB = true;
            } else {
                ctext.setVisibility(View.INVISIBLE);
                nameB = false;
            }

            if (expB == false && nameB == false) {
                Log.i("name", name.getText().toString());
                Log.i("exp", exp.getText().toString());


                float experience = Float.parseFloat(exp.getText().toString());

                String json = "{\"exp\":" + experience + "}";

                //MediaType JSON = MediaType.parse("application/json; charset=utf-8;");
                // RequestBody body = RequestBody.create("application/json; charset=utf-8",JSON);
                // put your json here
                RequestBody body = RequestBody.create(JSON,json);
                Log.i("body", body.toString());

                Request request = new Request.Builder()
                        .url("http://nikhilmr2000.pythonanywhere.com/predict")
                        .post(body)
                        .build();
                Log.i("resp", request.toString());

               /* Response response=null;
                    try {
                        response=client.newCall(request).execute();
                        Log.i("Val","Passed");
                    } catch (IOException e) {
                        Log.i("Val","Failed");
                        e.printStackTrace();
                    }*/
                new MyAsyncTask().execute(request);

            }
        }
        });


    }
    class MyAsyncTask extends AsyncTask<Request, Void, Response> {

        @Override
        protected Response doInBackground(Request... requests) {
            Response response = null;
            try {
                response = client.newCall(requests[0]).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            try {
                if(response.body() != null) {
                    String s = response.peekBody(2048).string();
                    TextView output=findViewById(R.id.output);
                    output.setVisibility(View.VISIBLE);
                    output.setText("Rs: "+s+" P/M");
                    Log.i("val",response.peekBody(2048).string());
                }
                else{
                    Log.i("val",response.peekBody(2048).string());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
