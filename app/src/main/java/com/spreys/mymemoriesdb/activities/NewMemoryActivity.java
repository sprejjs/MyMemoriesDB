package com.spreys.mymemoriesdb.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.spreys.mymemoriesdb.R;
import com.spreys.mymemoriesdb.db.MemoryDbHelper;
import com.spreys.mymemoriesdb.model.Memory;

import java.io.IOException;
import java.io.InputStream;

public class NewMemoryActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private ImageView selectedImageView;
    private EditText titleEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_memory_activity);

        this.selectedImageView = (ImageView) findViewById(R.id.new_memory_selected_image);
        this.titleEditText = (EditText) findViewById(R.id.new_memory_title);
    }

    public void openGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
    }

    public void openCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }

    public void cancel(View view) {
        finish();
    }

    public void save(View view) {
        Bitmap image = ((BitmapDrawable)selectedImageView.getDrawable()).getBitmap();
        new MemoryDbHelper(this).addMemory(new Memory(titleEditText.getText().toString(), image));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                selectedImageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            selectedImageView.setImageBitmap(imageBitmap);
        }
    }
}
