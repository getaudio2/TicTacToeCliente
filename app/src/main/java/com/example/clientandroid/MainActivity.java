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

    MainActivity instance;

    /**
    * Button array
    * */
    private Button[][] buttons = new Button[3][3];
    /**
    * Player turn
    * */
    private boolean playerTurn = true;
    /**
     * Puerto
     * */
    //private static final int SERVERPORT = 5000;
    /**
     * HOST
     * */
    //private static final String ADDRESS = "10.0.2.2";

    //private Context context = this;

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
                            ThreadNewGame newGame = new ThreadNewGame(socket, instance);
                            newGame.execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        /*for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }*/
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
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (playerTurn) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][1].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }


    /**
     * Clase para interactuar con el servidor
     * */
    /*class MyATaskCliente extends AsyncTask<String,Void,String>{*/

        /**
         * Ventana que bloqueara la pantalla del movil hasta recibir respuesta del servidor
         * */
        /*ProgressDialog progressDialog;*/

        /**
         * muestra una ventana emergente
         * */
        /*@Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setTitle("Connecting to server");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }*/

        /**
         * Se conecta al servidor y trata resultado
         * */
        /*@Override
        protected String doInBackground(String... values){

            try {
                //Se conecta al servidor
                InetAddress serverAddr = InetAddress.getByName(ADDRESS);
                Log.i("I/TCP Client", "Connecting...");
                Socket socket = new Socket(serverAddr, SERVERPORT);
                Log.i("I/TCP Client", "Connected to server");

                //envia peticion de cliente
                Log.i("I/TCP Client", "Send data to server");
                PrintStream output = new PrintStream(socket.getOutputStream());
                String request = values[0];
                output.println(request);

                //recibe respuesta del servidor y formatea a String
                Log.i("I/TCP Client", "Received data to server");
                InputStream stream = socket.getInputStream();
                byte[] lenBytes = new byte[256];
                stream.read(lenBytes,0,256);
                String received = new String(lenBytes,"UTF-8").trim();
                Log.i("I/TCP Client", "Received " + received);
                Log.i("I/TCP Client", "");
                //cierra conexion
                socket.close();
                return received;
            }catch (UnknownHostException ex) {
                Log.e("E/TCP Client", "" + ex.getMessage());
                return ex.getMessage();
            } catch (IOException ex) {
                Log.e("E/TCP Client", "" + ex.getMessage());
                return ex.getMessage();
            }
        }*/

        /**
         * Oculta ventana emergente y muestra resultado en pantalla
         * */
        /*@Override
        protected void onPostExecute(String value){
            progressDialog.dismiss();
            Log.e("", "" + value);
        }
    }*/
}