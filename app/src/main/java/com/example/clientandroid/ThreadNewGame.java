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
                Log.i("TESTAR", "capinici " + request);

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
                        Log.i("COORDENADAS HEADER START", "" + cordX + " " + cordY);
                    }
                    output.writeByte(SEND_MOVE_OR_GET_MOVE);
                    output.writeInt(2);
                    output.writeInt(0);

                } else if (request == SEND_MOVE_OR_GET_MOVE) {
                    cordX = input.readInt();
                    cordY = input.readInt();
                    instance.updateCasillas(cordX, cordY);
                    Log.i("COORDENADAS SM OR GM", "" + cordX + " " + cordY);
                    output.writeByte(SEND_MOVE_OR_GET_MOVE);
                    output.writeInt(1);
                    output.writeInt(1);
                } else if (request == GET_MOVE_AND_GET_WINNER) {
                    cordX = input.readInt();
                    cordY = input.readInt();
                    instance.updateCasillas(cordX, cordY);
                    ganador = input.readInt();
                } else if (request == GET_WINNER) {
                    ganador = input.readInt();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String resposta){

    }

    public void enviarCords(int cordX, int cordY) {
        try {
            output.write(cordX);
            output.write(cordY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
