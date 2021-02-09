package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button add_btn, view_btn, update_btn;
    Switch switch_btn;
    TextView Name_box, Age_box;
    ListView mylist;
    ArrayAdapter customerArrayAdapter;
    DatabaseHelper databaseHelper;
    CustomerModel clickedCustomer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_btn = findViewById(R.id.add_btn);
        view_btn = findViewById(R.id.view_btn);
        switch_btn = findViewById(R.id.switch_box);
        update_btn = findViewById(R.id.update_btn);
        Name_box = findViewById(R.id.name_tb);
        Age_box = findViewById(R.id.age_tb);
        mylist = findViewById(R.id.ListView);
        databaseHelper = new DatabaseHelper(MainActivity.this);
        update_btn.setClickable(false);

        showAllData();

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Name_box.getText().toString().isEmpty() && Age_box.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"give required details",Toast.LENGTH_SHORT).show();
                }
                else{
                    CustomerModel customerModel = new CustomerModel(1,Name_box.getText().toString(), Integer.parseInt(Age_box.getText().toString()),switch_btn.isChecked());
                    Toast.makeText(MainActivity.this,customerModel.toString(),Toast.LENGTH_SHORT).show();

                    boolean success = databaseHelper.addOne(customerModel);
                    Toast.makeText(MainActivity.this,"success = " + success, Toast.LENGTH_SHORT).show();
                    showAllData();
                    Name_box.setText("");
                    Age_box.setText("");
                }
            }
        });

        view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllData();
                //Toast.makeText(MainActivity.this,newlist.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this).setTitle("")
                        .setMessage("Delete / update ? ")
                        .setPositiveButton("update", new DialogInterface.OnClickListener() {
                    @Override
                            public void onClick(DialogInterface dialog, int which) {
                                update_btn.setClickable(true);
                                clickedCustomer = (CustomerModel) parent.getItemAtPosition(position);
                                Toast.makeText(MainActivity.this, "change the values and click update", Toast.LENGTH_SHORT).show();
                                boolean updated = databaseHelper.updateOne(clickedCustomer, Name_box, Age_box, switch_btn, update_btn);
                            }
                        }).setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clickedCustomer = (CustomerModel) parent.getItemAtPosition(position);
                                boolean deleted = databaseHelper.deleteOne(clickedCustomer);
                                showAllData();
                                Toast.makeText(MainActivity.this, "Deleted " + clickedCustomer.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        showAllData();

    }

    private void showAllData() {
        customerArrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1,databaseHelper.getAll());
        mylist.setAdapter(customerArrayAdapter);
    }
}
