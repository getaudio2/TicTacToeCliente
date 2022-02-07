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
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnConn, btnStart, btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;
    EditText txtIp, txtPort;
    TextView txtResult;
    List<Button> buttons;
    ThreadConnection conn;
    ThreadNewGame newGame;

    int cordX = 1;
    int cordY = 2;

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

        buttons = new ArrayList<Button>();

        btn00 = findViewById(R.id.button_00);
        btn00.setEnabled(false);

        btn01 = findViewById(R.id.button_01);
        btn01.setEnabled(false);

        btn02 = findViewById(R.id.button_02);
        btn02.setEnabled(false);

        btn10 = findViewById(R.id.button_10);
        btn10.setEnabled(false);

        btn11 = findViewById(R.id.button_11);
        btn11.setEnabled(false);

        btn12 = findViewById(R.id.button_12);
        btn12.setEnabled(false);

        btn20 = findViewById(R.id.button_20);
        btn20.setEnabled(false);

        btn21 = findViewById(R.id.button_21);
        btn21.setEnabled(false);

        btn22 = findViewById(R.id.button_22);
        btn22.setEnabled(false);

        buttons.add(btn00);
        buttons.add(btn01);
        buttons.add(btn02);
        buttons.add(btn10);
        buttons.add(btn11);
        buttons.add(btn12);
        buttons.add(btn20);
        buttons.add(btn21);
        buttons.add(btn22);

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
                enableBtn();
                break;
            case CONNECTION_KO:
                txtResult.setText("CONNECTED KO");
                break;
        }
    }

    public void updateCasillas(int cordX, int cordY) {
        for (Button btn: buttons) {
            String btnTag = btn.getTag().toString();
            int x = Character.getNumericValue(btnTag.charAt(4));
            int y = Character.getNumericValue(btnTag.charAt(5));

            if (cordX == x && cordY == y) {
                btn.setText("O");
            }
        }
    }

    @Override
    public void onClick(View v) {
        ((Button) v).setText("X");

        String btnTag = ((Button) v).getTag().toString();
        cordX = Character.getNumericValue(btnTag.charAt(4));
        cordY = Character.getNumericValue(btnTag.charAt(5));

        //disableBtn();

        Log.i("COORDENADAS ENVIADAS", "" + cordX + " " + cordY);
        //newGame.enviarCords(cordX, cordY);
    }

    public int getCordX() {
        return cordX;
    }

    public int getCordY() {
        return cordY;
    }

    public void enableBtn() {
        for (Button btn: buttons) {
            btn.setEnabled(true);
        }
        /*btn00.setEnabled(true);
        btn01.setEnabled(true);
        btn02.setEnabled(true);
        btn10.setEnabled(true);
        btn11.setEnabled(true);
        btn12.setEnabled(true);
        btn20.setEnabled(true);
        btn21.setEnabled(true);
        btn22.setEnabled(true);*/
    }

    public void disableBtn() {
        for (Button btn: buttons) {
            btn.setEnabled(false);
        }
        /*btn00.setEnabled(false);
        btn01.setEnabled(false);
        btn02.setEnabled(false);
        btn10.setEnabled(false);
        btn11.setEnabled(false);
        btn12.setEnabled(false);
        btn20.setEnabled(false);
        btn21.setEnabled(false);
        btn22.setEnabled(false);*/
    }
}