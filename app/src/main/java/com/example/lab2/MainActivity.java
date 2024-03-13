package com.example.lab2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {
    //AdapterView
    ListView listView;
    private ActivityResultLauncher<Intent> launcher;
    private  ActivityResultLauncher<Intent> launcher2;
    Button btnThem, btnXoa, btnSua;
    String name = "", phoneNumber = "";
    Uri filePath;

    int pos, flag;
    //Adapter
    CustomAdapter adapter;
    //Data
    Model []models = {
            new Model(1, "Duong", "123123", true, null )
            };
    ArrayList<Model> modelArrayList = new ArrayList<>();


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo i = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id = item.getItemId();
        pos = i.position;
        if(id == R.id.Sua) {
            name = "";
            phoneNumber = "";
            String image = modelArrayList.get(pos).getFilePath().toString();
            String name = modelArrayList.get(i.position).getName();
            String phone = modelArrayList.get(i.position).getPhoneNumber();
            Intent intent = new Intent(MainActivity.this, SuaActivity.class);
            intent.putExtra("editName", name);
            intent.putExtra("editPhone", phone);
            intent.putExtra("imageUri", image);

            launcher2.launch(intent);
            return true;
        }
        else if(id == R.id.Xoa) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Bạn có muốn xóa không?")
                    .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            modelArrayList.remove(i.position);
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
            return true;
        }else if(id == R.id.Call) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            String phone = modelArrayList.get(i.position).getPhoneNumber();
            intent.setData(Uri.parse("tel:" + phone));
            if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this, new String [] {Manifest.permission.CALL_PHONE}, 1);
            }
            startActivity(intent);
            return true;
        } else if(id == R.id.sendSMS) {
            String phone = modelArrayList.get(i.position).getPhoneNumber();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("sms:" + phone));
            if(phone.isEmpty() == false) {
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, 1);
                }
            }
            startActivity(intent);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Mapping
        listView = findViewById(R.id.listView);
        btnThem = findViewById(R.id.buttonThem);
        btnXoa = findViewById(R.id.buttonXoa);
        btnSua = findViewById(R.id.buttonSua);

        //add item in arrayList
        for(int i = 0; i < models.length; i++) {
            modelArrayList.add(models[i]);
        }
        adapter = new CustomAdapter(modelArrayList, getApplicationContext());
        listView.setAdapter(adapter);



        //Option Menu
        registerForContextMenu(listView);

        //lay du lieu tu activity them
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result-> {
                    if(result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        name = data.getStringExtra("name");
                        phoneNumber = data.getStringExtra("phone");
                        filePath = data.getParcelableExtra("filePath");
                        flag = data.getIntExtra("flag", 0);
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

        launcher2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                results -> {
                    if(results.getResultCode() == RESULT_OK) {
                        Intent data = results.getData();
                        name = data.getStringExtra("editName");
                        phoneNumber = data.getStringExtra("editPhone");
                        filePath = data.getParcelableExtra("imageUri");
                        flag = data.getIntExtra("flag2", 0);
                    }
                });



        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    protected void onResume() {
        super.onResume();
        if(flag == 1) {
            Model model = new Model(models.length + 1, name, phoneNumber, false, filePath);
            modelArrayList.add(model);
            adapter.notifyDataSetChanged();
            flag = 0;
        }
        if(flag == 2) {
            Model model = new Model(modelArrayList.get(pos).getId(), name, phoneNumber, false, filePath);
            modelArrayList.set(pos, model);
            adapter.notifyDataSetChanged();

            flag = 0;
        }
    }

}
