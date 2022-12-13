package com.example.yogaflex;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class YogaActivity extends AppCompatActivity {
    private static final String TAG = "YogaActivity started";

    private TextView datePicker;
    String[] slotAr;

    private String monthPay="",dayPay="";
    private int[] slot =new int[1];
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private HashMap<String,String> data=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga);

        try {
            init();
            click();

        }catch (Exception e){
            Log.d(TAG, "onCreate: error : "+e);
        }
    }

    private void init() {
        datePicker=findViewById(R.id.month_picker);
        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid();
        mRef= FirebaseDatabase.getInstance().getReference("user").child(user);

//
//        new DatePickerDialog(this, myDateListener, year, month, day);
//        pickDateBtn = findViewById(R.id.idBtnPickDate)
//        selectedDateTV = findViewById(R.id.idTVSelectedDate)

    }

    private void click() {
        Log.d(TAG, "click: ");
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        YogaActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dayPay=year+"-"+(monthOfYear + 1+"-"+dayOfMonth);
                                monthPay=year+"-"+(monthOfYear + 1);
                                Log.d(TAG, "onDateSet: "+monthPay);
                                datePicker.setText(monthPay);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });


        slotAr = new String[] {"6-7AM", "7-8AM", "8-9AM", "5-6PM"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, slotAr);
        AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.priceSpinner);editTextFilledExposedDropdown.setAdapter(genderAdapter);
        ((AutoCompleteTextView)findViewById(R.id.priceSpinner)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                slot[0] =i;
                Log.d(TAG, "onItemClick: "+i);
            }
        });
    }

    public void CompletePayment(View view) {
        int max=1,min=0;
        int result= (int) (Math.random() * (max - min + 1) + min);
        String status="";
        if (result==0){
            data.put("status","success");
            data.put("monthPay",monthPay);
            data.put("price","500");
            data.put("slot",slotAr[slot[0]]);
            Toast.makeText(this, "Payment success", Toast.LENGTH_SHORT).show();
        }else {
            data.put("status","fail");
            data.put("monthPay",monthPay);
            data.put("price","500");
            data.put("slot",slotAr[slot[0]]);
            Toast.makeText(this, "Payment Fail", Toast.LENGTH_SHORT).show();
        }
        mRef.child("payment").child(monthPay).child(dayPay).setValue(data);

    }

    public void logoutFn(View view) {
        mAuth.signOut();
        startActivity(new Intent(YogaActivity.this,MainActivity.class));
    }
}