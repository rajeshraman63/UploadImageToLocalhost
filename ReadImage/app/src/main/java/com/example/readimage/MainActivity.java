
package com.example.readimage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class MainActivity extends AppCompatActivity {


    public static final int REQUEST_CODE = 1;

    private ImageView imageView;
    private Button btn_selectImage;
    public String encoded_string;
    private EditText et_imageName;
    private Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        btn_selectImage = findViewById(R.id.btn_selectImage);
        et_imageName = findViewById(R.id.et_imageName);
        btn_send = findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpload();
            }
        });
    }

    private void onUpload(){
        String imageName = et_imageName.getText().toString();
//        String password = et_pass.getText().toString();
        String type = "upload";

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,imageName,encoded_string);

        //Toast.makeText(this, "Button is working ", Toast.LENGTH_SHORT).show();
    }


    public void pickImage(View v){

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); // CHECK IT

        String pictureDirectoryPath = pictureDirectory.getPath();

        Uri data = Uri.parse(pictureDirectoryPath);

        Toast.makeText(getApplicationContext(),""+pictureDirectoryPath,Toast.LENGTH_LONG).show();

        photoPickerIntent.setDataAndType(data,"image/*");

        startActivityForResult(photoPickerIntent,REQUEST_CODE);

    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        if(resultCode == Activity.RESULT_OK  && requestCode == REQUEST_CODE){

            try{
                Uri imageUri = data.getData();

                Toast.makeText(getApplicationContext()," photo add : " + imageUri,Toast.LENGTH_LONG).show();

                //encoded_string = encoder(imageUri.toString());

                InputStream inputstream;
                inputstream = getContentResolver().openInputStream(imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap image = BitmapFactory.decodeStream(inputstream);
                imageView.setImageBitmap(image);
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
                Toast.makeText(getApplicationContext()," imageString : " + imageString,Toast.LENGTH_LONG).show();
                encoded_string = imageString;

            }catch(FileNotFoundException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Unable to open image",Toast.LENGTH_LONG).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
