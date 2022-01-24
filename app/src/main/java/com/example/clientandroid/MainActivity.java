package com.example.clientandroid;

import static com.example.clientandroid.model.DefaultConstants.*;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnConn, btnStart;
    EditText txtIp, txtPort;
    TextView txtResult;
    ThreadConnection conn;
    ThreadNewGame newGame;

    MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConn = findViewById(R.id.btn_conn);
        btnStart =  findViewById(R.id.btn_start);
        txtIp = findViewById(R.id.editTextIp);
        txtPort = findViewById(R.id.editTextPort);
        txtResult = findViewById(R.id.txtResult);
        instance = this;

        btnStart.setEnabled(false);

        btnConn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ip = txtIp.getText().toString();
                        int port = Integer.valueOf(txtPort.getText().toString());

                        if(port!=0 && !ip.equals("")){
                            conn = new ThreadConnection(ip, port, instance);
                            conn.execute();
                        }else{
                            Toast.makeText(getApplicationContext(), "El ip o port són incorrectes", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        btnStart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Socket socket = conn.getSocket();
                            newGame = new ThreadNewGame(socket, instance);
                            newGame.execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void updateUI(byte header){
        switch (header){
            case CONNECTION_OK:
                txtResult.setText("CONNECTED OK");
                btnStart.setEnabled(true);
                break;
            case CONNECTION_KO:
                txtResult.setText("CONNECTED KO");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        ((Button) v).setText("X");

        String btnTag = ((Button) v).getTag().toString();
        int cordX = Character.getNumericValue(btnTag.charAt(4));
        int cordY = Character.getNumericValue(btnTag.charAt(5));

        newGame.enviarCords(cordX, cordY);

        Log.i("COORDENADAS ENVIADAS", "" + cordX + " " + cordY);
    }
}