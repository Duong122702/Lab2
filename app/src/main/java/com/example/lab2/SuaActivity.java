package com.example.lab2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SuaActivity extends AppCompatActivity {

    TextView editName2, editPhone2;
    ImageView imageView2;
    Button btnThoat2,btnSua2, btnThemAnh;
    private ActivityResultLauncher<Intent> launcher;
    int flag;
    Uri imagePath;
    private static final int REQUEST_CODE_PERMISSION = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua);

        editName2 = findViewById(R.id.editView2);
        editPhone2 = findViewById(R.id.editViewPhone2);
        imageView2 = findViewById(R.id.imageView2);
        btnThoat2 = findViewById(R.id.btnThoat2);
        btnSua2 = findViewById(R.id.btnSua);
        btnThemAnh = findViewById(R.id.buttonThemImage2);

        Bundle bundle = getIntent().getExtras();
        editName2.setText(bundle.getString("editName"));
        editPhone2.setText(bundle.getString("editPhone"));

        if(ActivityCompat.checkSelfPermission(SuaActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SuaActivity.this, new String [] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }else {
            String imagePath = bundle.getString("imageUri");
            Uri fileImage = Uri.parse(imagePath);
            try {
                imageView2.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(fileImage)));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
//        Toast.makeText(this, imagePath, Toast.LENGTH_LONG).show();


        registerResult();
        btnSua2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checked() == true) {
                    Intent intent = new Intent(SuaActivity.this, MainActivity.class);
                    intent.putExtra("editName", editName2.getText().toString());
                    intent.putExtra("editPhone", editPhone2.getText().toString());
                    intent.putExtra("imageUri", imagePath) ;
                    intent.putExtra("flag2", 2);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        btnThemAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        btnThoat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void pickImage() {
        Intent intent =  new Intent(MediaStore.ACTION_PICK_IMAGES);
        launcher.launch(intent);
    }
    private void registerResult() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                try {
                    Uri image = o.getData().getData();
                    imagePath = image;
                    imageView2.setImageURI(image);
                }
                catch (Exception e) {
                    Toast.makeText(SuaActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean checked() {

        if(editName2.length() == 0) {
            editName2.setError("không được để trống");
            return false;
        }
        if(!editName2.getText().toString().matches("[a-zA-Z]+")) {
            editName2.setError("chỉ đuợc phép nhập chữ");
            return false;
        }
        if(editPhone2.length() == 0 ) {
            editPhone2.setError("không được để trống");
            return false;
        }
        if(editPhone2.length() == 10 ) {
            editPhone2.setError("số đt phải bằng 10");
            return false;
        }
        if(!editPhone2.getText().toString().matches("[0-9]+")) {
            editPhone2.setError("chỉ được phép nhập số ");
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_PERMISSION) {
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Uri uri = getIntent().getParcelableExtra(Intent.EXTRA_TEXT);
                try {
                    imageView2.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(uri)));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }else {

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}