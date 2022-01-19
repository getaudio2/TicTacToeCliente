package com.example.clientandroid;

import android.os.AsyncTask;

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
            output.writeByte(0x01);

            byte cap = input.readByte();
            int quiComença = input.readInt();

            if (quiComença == 1) {
                byte capM = input.readByte();
                int cord1 = input.readInt();
                int cord2 = input.readInt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String resposta){

    }
}
