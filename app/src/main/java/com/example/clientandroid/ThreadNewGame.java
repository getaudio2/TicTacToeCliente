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
            int cordX = 3;
            int cordY = 3;

            output.writeByte(SEND_MOVE_OR_GET_MOVE);

            output.write(1);
            output.write(0);

            /*byte request = input.readByte();

            if (request == HEADER_START) {

                int i = input.read();
                Log.i("AAAAAA", "" + i);

                if (i == 1) {
                    byte anotherReq = input.readByte();
                    cordX = input.read();
                    cordY = input.read();
                    Log.i("AAAAAA", "" + cordX + " " + cordY);
                } else {

                }
            }*/

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
