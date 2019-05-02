package com.example.readimage;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundWorker extends AsyncTask<String,Void,String> {

    AlertDialog alertDialog;

    Context context;

    BackgroundWorker(Context c){
        context = c;
    }

    @Override
    protected String  doInBackground(String... params) {

        String type = params[0];

//        192.168.16.149

        String upload_url = "http://192.168.16.149/Php_Codes/insertPhoto3.php";


        if(type.equals("upload")){

            String imageName = params[1];
            String encoded_string = params[2];

            try {
                URL url = new URL(upload_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                // to send the data from the android to the server
                OutputStream outputStream = httpURLConnection.getOutputStream();  // connection to write on the server

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = URLEncoder.encode("image_name","UTF-8") + "=" +URLEncoder.encode(imageName,"UTF-8") + "&" +
                        URLEncoder.encode("encoded_string","UTF-8") + "=" +URLEncoder.encode(encoded_string,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                String result = "";
                String line = "";

                while ((line=bufferedReader.readLine())!=null){
                    result += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        // super.onPreExecute();
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Upload Status");
        alertDialog.setCancelable(true);
    }

    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(aVoid);
        alertDialog.setMessage(result);
        alertDialog.show();

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
