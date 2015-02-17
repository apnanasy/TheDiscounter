package com.work.adam.discounter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adam on 2/14/2015.
 */
public class CalcActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {

    private EditText etPamount;
    private EditText etLamount;
    private EditText etDamount;
    private TextView tvPapplied;
    private TextView tvLapplied;
    private Button bCalc;
    private Button bReset;
    private ToggleButton tbChoice;
    private ScrollView scrollview;
    private LinearLayout scrollLayout;
    private Double parts;
    private Double labor;
    private Double discount;
    private int counter;
    private List<Double> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linlayout);
        etPamount = (EditText) findViewById(R.id.etPamount);
        etLamount = (EditText) findViewById(R.id.etLamount);
        etDamount = (EditText) findViewById(R.id.etDamount);
        tvPapplied = (TextView) findViewById(R.id.tvPapplied);
        tvLapplied = (TextView) findViewById(R.id.tvLapplied);
        bCalc = (Button) findViewById(R.id.bCalc);
        bReset = (Button) findViewById(R.id.bReset);
        tbChoice = (ToggleButton) findViewById(R.id.tbChoice);
        scrollview = (ScrollView) findViewById(R.id.scrollView);
        scrollLayout = (LinearLayout) findViewById(R.id.scrollLayout);
        items = new ArrayList<Double>();
        counter = 1;
        createLine();
        bCalc.setOnClickListener(this);
        bReset.setOnClickListener(this);
    }


    public void createLine() {
        LinearLayout ll = new LinearLayout(getApplicationContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,2f));
        tv.setText("Line " + counter + ":");
        EditText et = new EditText(this);
        et.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
        et.setEms(10);
        et.setInputType(InputType.TYPE_CLASS_PHONE);
        //et.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et.setOnFocusChangeListener(this);
        et.setId(counter);
        TextView tv2 = new TextView(this);
        tv2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,2f));
        tv2.setText("");
        ll.addView(tv);
        ll.addView(et);
        ll.addView(tv2);
        scrollLayout.addView(ll);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bCalc:
                if (setInput()) {
                   calculate();
                }
                break;
            case R.id.bReset:
                reset();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus == true) {
            if(v.getId() == counter){
                counter += 1;
                createLine();
            }
        }
    }

    public boolean setInput() {
        Context context = getApplicationContext();
        try {
            discount = Double.parseDouble(etDamount.getText().toString());
        } catch (IllegalArgumentException e) {
            String message = "Enter Discount!";
            Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            return false;
        }

        try {
            parts = Double.parseDouble(etPamount.getText().toString());
        } catch (IllegalArgumentException e) {
            parts = 0.0;
        }

        try {
            labor = Double.parseDouble(etLamount.getText().toString());
        } catch (IllegalArgumentException e) {
            labor = 0.0;
        }

        if (parts == 0 && labor == 0) {
            String message = "Enter Parts or Labor!";
            Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            return false;
        }
        try {
            for (int c = 0;c < counter;c += 1) {
                LinearLayout ll = (LinearLayout) scrollLayout.getChildAt(c);
                EditText et = (EditText) ll.getChildAt(1);
                 String hello = et.getText().toString();
                if (!et.getText().toString().equals("") ) {
                    items.add(Double.parseDouble(et.getText().toString()));
                    //TextView tv = (TextView) ll.getChildAt(2);
                    //tv.setText(items.get(items.size() - 1).toString());
                }
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public void calculate(){
        double total = parts + labor;
        DecimalFormat df = new DecimalFormat("#.##");
        Double lapplied = 0.0;
        Double papplied = 0.0;
        if(labor != 0.0) {
            lapplied = labor / total * discount;
            tvLapplied.setText(Double.valueOf(df.format(lapplied)).toString());
        }
        if (parts != 0.0) {
            papplied = parts / total * discount;
            tvPapplied.setText(Double.valueOf(df.format(papplied)).toString());
        }
        for(int x = 0;x < items.size();x++) {
            Double item = items.get(x);
            Double applied;
            if(tbChoice.isChecked() == true) {
                applied = item / parts * papplied;
            } else {
                applied = item / labor * lapplied;
            }
            LinearLayout ll = (LinearLayout)scrollLayout.getChildAt(x);
            TextView tv = (TextView)ll.getChildAt(2);
            tv.setText(Double.valueOf(df.format(applied)).toString());
        }

    }

    public void reset() {
        etPamount.setText("");
        etLamount.setText("");
        etDamount.setText("");
        tvPapplied.setText("");
        tvLapplied.setText("");
        scrollLayout.removeAllViews();
        parts = 0.0;
        labor = 0.0;
        discount = 0.0;
        items.clear();
        counter = 1;
        createLine();
    }
}


