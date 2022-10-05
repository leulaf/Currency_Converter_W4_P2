package com.example.prob2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.EditText;
public class MainActivity extends AppCompatActivity {
    GestureDetector gestureDetector;
    private float mTouchPosition;
    private float mReleasePosition;
    public static TextView peso;
    public static TextView pound;
    public static TextView franc;
    public static TextView yen;
    public static Double convertPeso = 20.09;
    public static Double convertPound = 0.89;
    public static Double convertFranc = 0.98;
    public static Double convertYen = 144.67;
    public static EditText dollar;
    public static String STATE_DOLLARS = "dollars";
    public static int minUpVelocity = -3000;
    public static int minDownVelocity = 3000;
    public static float dollars = 0;

    // parses the number in the text if it's possible
    public static float tryParse(String text) {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // updates all text/edit text boxes
    public static void setAmount(){
        if(dollars < 0){
            dollars = 0;
        }
        String currDollars = String.format("%.02f", dollars);
        String currPesos = String.format("%.02f", dollars*convertPeso);
        String currPounds = String.format("%.02f", dollars*convertPound);
        String currFrancs = String.format("%.02f", dollars*convertFranc);
        String currYen = String.format("%.02f", dollars*convertYen);

        dollar.setText(currDollars);
        peso.setText(currPesos + " Pesos");
        pound.setText(currPounds + " Pounds");
        franc.setText(currFrancs + " Francs");
        yen.setText(currYen + " Yen");
    }

    // updates all text boxes except the edit text box, to prevent stack overflow
    public static void setEditAmount(){
        if(dollars < 0){
            dollars = 0;
        }
        String currPesos = String.format("%.02f", dollars*convertPeso);
        String currPounds = String.format("%.02f", dollars*convertPound);
        String currFrancs = String.format("%.02f", dollars*convertFranc);
        String currYen = String.format("%.02f", dollars*convertYen);

        peso.setText(currPesos + " Pesos");
        pound.setText(currPounds + " Pounds");
        franc.setText(currFrancs + " Francs");
        yen.setText(currYen + " Yen");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If we have a saved state then we can restore the dollar amount
        if (savedInstanceState != null) {
            dollars = savedInstanceState.getFloat(STATE_DOLLARS, 0);
            setAmount();
        }

        setContentView(R.layout.activity_main);
        gestureDetector = new GestureDetector(this, new GestureListener());
        dollar = (EditText) findViewById(R.id.dollar);
        peso = (TextView) findViewById(R.id.peso);
        pound = (TextView) findViewById(R.id.pound);
        franc = (TextView) findViewById(R.id.franc);
        yen = (TextView) findViewById(R.id.yen);

        dollar.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                float tempUSD = tryParse(dollar.getText().toString());

                // makes sure than the edit text doesn't contain a negative number
                if(tempUSD < 0){
                    dollars = tempUSD;
                    setAmount();
                }else{
                    dollars = tempUSD;
                    setEditAmount();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat(STATE_DOLLARS, dollars);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchPosition = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mReleasePosition = event.getY();

            if (mTouchPosition - mReleasePosition > 0) {
                // user scroll up
                dollars += 0.10;

            } else {
                //user scroll down
                dollars -= 0.10;
            }
        }

        setAmount();
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    // detects is the user flings up or down
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // user fling up
            if(velocityY < minUpVelocity){
                dollars += 0.9;
            }

            // user fling down
            if(velocityY > minDownVelocity){
                dollars -= 0.9;
            }
            setAmount();
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}