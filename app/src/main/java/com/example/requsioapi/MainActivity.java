package com.example.requsioapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private Button btnRecuperar;
    private EditText etCEP;
    private TextView txResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRecuperar = findViewById(R.id.btnRec);
        etCEP = findViewById(R.id.etNum);
        txResultado = findViewById(R.id.Resultado);

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cep = etCEP.getText().toString();
                MyAsyncTaks task = new MyAsyncTaks();
                String urlAPI ="https://viacep.com.br/ws/"+cep+"/json";
                task.execute(urlAPI);

            }
        });
    }
    class MyAsyncTaks extends AsyncTask<String,Void,String>{



        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer stringBuffer = null;
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                inputStream = connection.getInputStream();
                inputStreamReader= new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader (inputStreamReader);
                stringBuffer = new StringBuffer();
                String linha ="";
                while ((linha = reader.readLine()) != null){
                    stringBuffer.append(linha);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringBuffer.toString();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            txResultado.setText(s);

            String logradouro = null;
            String cep = null;
            String complemento = null;
            String bairro = null;
            String localidade = null;
            String uf = null;

            try {
                JSONObject jsonObject = new JSONObject(s);
                logradouro = jsonObject.getString("logradouro");
                cep = jsonObject.getString("cep");
                complemento = jsonObject.getString("complemento");
                bairro = jsonObject.getString("bairro");
                localidade = jsonObject.getString("localidade");
                uf = jsonObject.getString("uf");


            } catch (JSONException e) {
                e.printStackTrace();
            }
            txResultado.setText(logradouro+"/"+cep+"/"+complemento+"/"+bairro+"/"+localidade+"/"+uf+".");


        }
    }

}