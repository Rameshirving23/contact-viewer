package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String CUSTOMER_ID = "ID";
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String COLUMN_CUSTOMER_AGE = "CUSTOMER_AGE";
    public static final String COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";


    public DatabaseHelper(@Nullable Context context) {
        super(context, "customer.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " (" + CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CUSTOMER_NAME + " TEXT, " + COLUMN_CUSTOMER_AGE + " INT, " + COLUMN_ACTIVE_CUSTOMER + " BOOL)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(CustomerModel customerModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ACTIVE_CUSTOMER,customerModel.isActive());
        cv.put(COLUMN_CUSTOMER_AGE,customerModel.getAge());
        cv.put(COLUMN_CUSTOMER_NAME,customerModel.getName());

        long insert = db.insert(CUSTOMER_TABLE, null, cv);

        if(insert == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean deleteOne(CustomerModel customerModel){

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + CUSTOMER_ID + " = " + customerModel.getId();

        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean updateOne(final CustomerModel customerModel, final TextView name_box, final TextView age_box, final Switch Act_cus, Button update_btn){

        final SQLiteDatabase db = this.getWritableDatabase();
        name_box.setText(customerModel.getName());
        age_box.setText(String.valueOf(customerModel.getAge()));
        Act_cus.setChecked(customerModel.isActive());
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                String id = String.valueOf(customerModel.getId());
                cv.put(COLUMN_ACTIVE_CUSTOMER, Act_cus.isChecked());
                cv.put(COLUMN_CUSTOMER_AGE, Integer.parseInt(String.valueOf(age_box.getText())));
                cv.put(COLUMN_CUSTOMER_NAME, String.valueOf(name_box.getText()));

                db.update(CUSTOMER_TABLE, cv, "ID = ?", new String[]{id});

            }
        });
        return true;
    }

    public List<CustomerModel> getAll(){
        List<CustomerModel> returnlist = new ArrayList<>();
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            do{
                int customerID = cursor.getInt(0);
                String customerName = cursor.getString(1);
                int customerAge = cursor.getInt(2);
                boolean customerActive = cursor.getInt(3) == 1 ? true: false;

                CustomerModel customerModel = new CustomerModel(customerID, customerName, customerAge, customerActive);

                returnlist.add(customerModel);

            }while(cursor.moveToNext());
        }

        return returnlist;
    }
}
