package com.example.lab2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

public class ThemActivity extends AppCompatActivity {
    EditText nameText, phoneNumberText;
    Button btnThoat, btnThem, btnThemAnh;
    public static final String TAG = "tag";
    private ActivityResultLauncher<Intent> imageLauncher;
    ImageView imageView;
    Uri ImageUri ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them);
        imageView = findViewById(R.id.imageView);
        btnThoat = findViewById(R.id.btnThoat);
        btnThem = findViewById(R.id.btnThem);
        nameText = findViewById(R.id.editView);
        btnThemAnh = findViewById(R.id.buttonThemImage);
        phoneNumberText = findViewById(R.id.editViewPhone);


        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checked()) {
                    Intent intent = new Intent(ThemActivity.this, MainActivity.class);
                    intent.putExtra("name", nameText.getText().toString());
                    intent.putExtra("phone", phoneNumberText.getText().toString());
                    intent.putExtra("filePath", ImageUri) ;
                    intent.putExtra("flag", 1);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        registerResult();
        btnThemAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickImage();
            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private boolean checked() {
        if(nameText.length() == 0) {
            nameText.setError("không được để trống");
            return false;
        }
        if(!nameText.getText().toString().matches("[a-zA-Z]+")) {
            nameText.setError("chỉ đuợc phép nhập chữ");
            return false;
        }
        if(phoneNumberText.length() == 0 ) {
            phoneNumberText.setError("không được để trống");
            return false;
        }
        if(phoneNumberText.length() > 10 ) {
            phoneNumberText.setError("số đt phải bằng 10");
            return false;
        }
        if(!phoneNumberText.getText().toString().matches("[0-9]+")) {
            phoneNumberText.setError("chỉ được phép nhập số ");
            return false;
        }
        return true;
    }
    private void pickImage() {

        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        imageLauncher.launch(intent);

    }

    private void registerResult() {
        imageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        try {
                            Uri imageUri = o.getData().getData();
                            ImageUri = imageUri;
                            imageView.setImageURI(imageUri);
                        }catch (Exception e) {
                            Toast.makeText(ThemActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}

