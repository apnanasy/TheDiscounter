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
 *
 * This is the main activity for the discounter
 */
public class CalcActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {

    private EditText etPamount; // User entered parts amount
    private EditText etLamount; // User entered labor amount
    private EditText etDamount; // User entered discount amount
    private TextView tvPapplied; // Displayed discount applied to parts
    private TextView tvLapplied; // Displayed discount applied to labor
    private Button bCalc; // Button to calculate applied discount
    private Button bReset; // Button to reset all entered information
    private ToggleButton tbChoice; // Toggle to use parts or labor
    private ScrollView scrollview; // View to display line items
    private LinearLayout scrollLayout; // Layout to organize line items
    private Double parts; // Parts amount used for calculation
    private Double labor; // Labor amount used for calculation
    private Double discount; // discount amount used for calculation


    /**
     * Constructor linking to the UI
     * @param savedInstanceState
     */
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
        createLine(); // Create the first line in the scrollview
        bCalc.setOnClickListener(this);
        bReset.setOnClickListener(this);
    }

    /**
     * This function creates a new line for user input
     */
    public void createLine() {
        LinearLayout ll = new LinearLayout(getApplicationContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,2f));
        tv.setText("Line " + scrollLayout.getChildCount()+":");
        EditText et = new EditText(this);
        et.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
        et.setEms(10);
        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et.setOnFocusChangeListener(this);
        et.setId(scrollLayout.getChildCount() + 1);
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

    /**
     * This function calls create line whenever the user clicks on the bottom line item
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus == true) {
            if(v.getId() == scrollLayout.getChildCount()){
                createLine();
            }
        }
    }

    /**
     * This function tests user input and puts in memory if usable
     * @return true if user input usable
     */
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
            for (int c = 0;c < scrollLayout.getChildCount();c += 1) {
                LinearLayout ll = (LinearLayout) scrollLayout.getChildAt(c);
                EditText et = (EditText) ll.getChildAt(1);
                 String hello = et.getText().toString();
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * Perfoms all calculations and pust them to the UI
     */
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
        for(int x = 0;x < scrollLayout.getChildCount();x++) {
            LinearLayout ll = (LinearLayout) scrollLayout.getChildAt(x);
            EditText et = (EditText) ll.getChildAt(1);

            TextView tv = (TextView) ll.getChildAt(2);
            if (!et.getText().toString().equals("")) {
                Double item = Double.parseDouble(et.getText().toString());

                Double applied;
                if (tbChoice.isChecked() == true) {
                    applied = item / parts * papplied;
                } else {
                    applied = item / labor * lapplied;
                }
                tv.setText(Double.valueOf(df.format(applied)).toString());
            } else { tv.setText("");}

        }
    }

    /**
     * Sets everything back to when app was started
     */
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
        createLine();
    }
}


