package com.example.lab2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CustomAdapter.CheckBoxCheckedListener{
    //AdapterView
    ListView listView;
    private ActivityResultLauncher<Intent> launcher;
    private  ActivityResultLauncher<Intent> launcher2;
    Button btnThem, btnXoa, btnSua;
    String name = "", phoneNumber = "";
    Uri filePath;
    ImageView imageView;
    int pos;
    //Adapter
    CustomAdapter adapter;
    //Data
    Model []models = {
            new Model(1, "Mot", "34567", false, null),
            new Model(2, "Hai", "0987", true, null),
            new Model(3, "Ba", "56789", true, null)
    };
    ArrayList<Model> modelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        btnThem = findViewById(R.id.buttonThem);
        btnXoa = findViewById(R.id.buttonXoa);
        btnSua = findViewById(R.id.buttonSua);
        modelArrayList = new ArrayList<>();



        for(int i = 0; i < models.length; i++) {
            modelArrayList.add(models[i]);
        }
        adapter = new CustomAdapter(modelArrayList, getApplicationContext());
        listView.setAdapter(adapter);

        adapter.setCheckBoxCheckedListener(this);

        //lay du lieu tu activity them
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result-> {
                    if(result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        name = data.getStringExtra("name");
                        phoneNumber = data.getStringExtra("phone");
                        filePath = data.getParcelableExtra("filePath");

                    }
                } );




        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = ""; phoneNumber =  "";
                Intent intent = new Intent(MainActivity.this, ThemActivity.class);
                launcher.launch(intent);
            }
        });

//        launcher2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if(result.getResultCode() == RESULT_OK) {
//                        Intent data = result.getData();
//                        name = data.getStringExtra("editName");
//                        phoneNumber = data.getStringExtra("editPhone");
//                        filePath = data.getParcelableExtra("imageUri");
//                    }
//                });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SuaActivity.class);
                intent.putExtra("editName", modelArrayList.get(pos).getName());
                intent.putExtra("editPhone", modelArrayList.get(pos).getPhoneNumber());
                intent.putExtra("imageUri", modelArrayList.get(pos).getFilePath());
                startActivity(intent);
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Bạn có muốn xóa không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modelArrayList.remove(pos);
                        adapter.notifyDataSetChanged();
                    }
                })
                        .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog mDialog = builder.create();
                mDialog.show();
            }
        });
    }


    protected void onResume() {
        super.onResume();
        if(name != "" && phoneNumber != "") {
            Model model = new Model(models.length + 1, name, phoneNumber, false, filePath);
            modelArrayList.add(model);

            adapter.notifyDataSetChanged();
        }

    }
    @Override
    public void getCheckBoxCheckedListener(int position) {
        pos = position;
    }
}