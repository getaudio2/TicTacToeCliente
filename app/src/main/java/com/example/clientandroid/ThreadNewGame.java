package com.example.clientandroid;

import static com.example.clientandroid.model.DefaultConstants.*;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ThreadNewGame extends AsyncTask<Void, Void, String> {

    Socket socket;
    MainActivity instance;

    private DataInputStream input;
    private DataOutputStream output;

    private int cordXClient = 3;
    private int cordYClient = 3;

    public ThreadNewGame(Socket socket, MainActivity instance) throws IOException {
        this.socket = socket;
        this.instance = instance;
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {

            int ganador = 2;

            output.writeByte(HEADER_START);

            while(ganador == 2) {
                byte request = input.readByte();
                Log.i("Server", " request: " + request);

                int cordX = 0;
                int cordY = 0;

                if (request == HEADER_START) {
                    int i = input.readInt();
                    Log.i("TESTAR", "quicomenca " + i);

                    if (i == 1) {
                        byte headermove = input.readByte();
                        cordX = input.readInt();
                        cordY = input.readInt();
                        instance.updateCasillas(cordX, cordY);
                        //habilitarBotones();
                        Log.i("COORDENADAS HEADER START", "" + cordX + " " + cordY);
                    }
                    output.writeByte(SEND_MOVE_OR_GET_MOVE);
                    while(cordXClient == 3 && cordYClient == 3){
                        cordXClient = instance.getCordX();
                        cordYClient = instance.getCordY();
                    }
                    cordXClient = 3;
                    cordYClient = 3;

                    output.writeInt(instance.getCordX());
                    output.writeInt(instance.getCordY());

                    instance.setCordX(3);
                    instance.setCordY(3);
                    //habilitarBotones();

                } else if (request == SEND_MOVE_OR_GET_MOVE) {
                    cordX = input.readInt();
                    cordY = input.readInt();
                    instance.updateCasillas(cordX, cordY);
                    //habilitarBotones();
                    Log.i("COORDENADAS SM OR GM", "" + cordX + " " + cordY);
                    output.writeByte(SEND_MOVE_OR_GET_MOVE);
                    while(cordXClient == 3 && cordYClient == 3){
                        cordXClient = instance.getCordX();
                        cordYClient = instance.getCordY();
                    }
                    cordXClient = 3;
                    cordYClient = 3;

                    output.writeInt(instance.getCordX());
                    output.writeInt(instance.getCordY());

                    instance.setCordX(3);
                    instance.setCordY(3);
                    //habilitarBotones();
                } else if (request == GET_MOVE_AND_GET_WINNER) {
                    cordX = input.readInt();
                    cordY = input.readInt();
                    instance.updateCasillas(cordX, cordY);
                    Log.i("COORDENADAS GM AND GW","" + cordX + " " + cordY);
                    ganador = input.readInt();
                    Log.i("SE ACABO", "Ganador (Server): " + ganador);
                } else if (request == GET_WINNER) {
                    ganador = input.readInt();
                    Log.i("SE ACABO", "Ganador (Cliente): " + ganador);
                }
            }
            socket.close();
            Log.i("SOCKET TANCAT", "S'ha acabat la partida");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String resposta){

    }

}
