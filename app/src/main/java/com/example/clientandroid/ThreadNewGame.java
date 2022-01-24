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
            output.writeByte(HEADER_START);

            int a = input.readInt();
            int b = input.readInt();

            Log.i("AAAAAA", "" + a + " " + b);

            socket.close();
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
            output.writeInt(cordX);
            output.writeInt(cordY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
