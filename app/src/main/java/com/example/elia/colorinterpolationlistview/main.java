package com.example.elia.colorinterpolationlistview;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Random;

public class main extends AppCompatActivity implements View.OnClickListener {

    private final static int start = 5;
    private int current;

    private String[] string_color;
    private int[] int_color, int_compColor;

    private boolean validInput, numSet, startSet, endSet;

    private Button btnRandom, btnSetNum, btnSetColor1, btnSetColor2;

    private Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rand = new Random();
        rand.setSeed(System.currentTimeMillis());

        current = start;

        btnRandom = findViewById(R.id.btn_random);
        btnSetNum = findViewById(R.id.btn_setNum);
        btnSetColor1 = findViewById(R.id.btn_setColor1);
        btnSetColor2 = findViewById(R.id.btn_setColor2);

        randomizeColors();

        btnRandom.setOnClickListener(this);
        btnSetNum.setOnClickListener(this);
        btnSetColor1.setOnClickListener(this);
        btnSetColor2.setOnClickListener(this);
    }

    private void randomizeColors() {
        int color, colorComp, r, g, b;
        String hexColor;

        string_color = new String[current];
        int_color = new int[current];
        int_compColor = new int[current];

        //start color
        r = rand.nextInt(255);
        g = rand.nextInt(255);
        b = rand.nextInt(255);

        color = Color.rgb(r, g, b);
        hexColor = String.format("#%06X", (0xFFFFFF & color));

        r = 255 - r; g = 255 - g; b = 255 - g;
        colorComp = Color.rgb(r, g, b);

        string_color[0] = hexColor;
        int_color[0] = color;
        int_compColor[0] = colorComp;

        //end color
        r = rand.nextInt(255);
        g = rand.nextInt(255);
        b = rand.nextInt(255);

        color = Color.rgb(r, g, b);
        hexColor = String.format("#%06X", (0xFFFFFF & color));

        r = 255 - r; g = 255 - g; b = 255 - g;
        colorComp = Color.rgb(r, g, b);

        string_color[current - 1] = hexColor;
        int_color[current - 1] = color;
        int_compColor[current - 1] = colorComp;

        findMiddle();
    }

    private void findMiddle() {
        int[] color1 = new int[3], color2 = new int[3], newColor,
                newColorComp;
        double balance = (1 / (((double) current) - 1));
        int color, colorComp;
        String hexColor;

        //start color
        color1[0] = Color.red(int_color[0]);
        color1[1] = Color.green(int_color[0]);
        color1[2] = Color.blue(int_color[0]);

        Log.d("findMiddle", "balance: " + balance + " color1: r: " + color1[0] + " g: "
                + color1[1] + " b: " + color1[2]);

        //end color
        color2[0] = Color.red(int_color[current - 1]);
        color2[1] = Color.green(int_color[current - 1]);
        color2[2] = Color.blue(int_color[current - 1]);

        Log.d("findMiddle", "balance: " + balance + " color2: r: " + color2[0] + " g: "
                + color2[1] + " b: " + color2[2]);

        for(int i = 1; i < current - 1; i++) {
            newColor = new int[3];
            newColorComp = new int[3];

            newColor[0] = (int) Math.round((color2[0] - color1[0]) * (balance * i) + color1[0]);
            newColor[1] = (int) Math.round((color2[1] - color1[1]) * (balance * i) + color1[1]);
            newColor[2] = (int) Math.round((color2[2] - color1[2]) * (balance * i) + color1[2]);

            Log.d("findMiddle", "balance: " + balance + " color" + i + ": r: " +
                    color1[0] + " g: " + color1[1] + " b: " + color1[2]);

            newColorComp[0] = 255 - newColor[0];    //red
            newColorComp[1] = 255 - newColor[1];    //green
            newColorComp[2] = 255 - newColor[2];    //blue

            color = Color.rgb(newColor[0], newColor[1], newColor[2]);
            hexColor = String.format("#%06X", (0xFFFFFF & color));
            colorComp = Color.rgb(newColorComp[0], newColorComp[1], newColorComp[2]);


            string_color[i] = hexColor;
            int_color[i] = color;
            int_compColor[i] = colorComp;
        }
        Log.d("findMiddle", "end\n\n");

        ListAdapter listAdapter = new ListAdapter(this, string_color, int_color,
                int_compColor);

        ListView listView = findViewById(R.id.listView_container);
        listView.setAdapter(listAdapter);
    }

    private void setNumColors() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Set Number of Colors");
        alertDialogBuilder.setMessage("3 - 15");

        final EditText userInput = new EditText(this);
        alertDialogBuilder.setView(userInput);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String input = (userInput.getText()).toString();
                try {
                    int numColors = Integer.parseInt(input);
                    if(numColors >= 3 && numColors <= 15) {
                        changeNumColors(numColors);
                    }
                    else {
                        dialogInterface.cancel();
                    }
                }
                catch (NumberFormatException e) {
                    Log.d("NumberFormatException", e.toString());
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void changeNumColors(int numColors) {
        int color1, colorComp1, color2, colorComp2;
        String hexColor1, hexColor2;

        //grab first color
        color1 = int_color[0];
        colorComp1 = int_compColor[0];
        hexColor1 = string_color[0];

        //grab last color
        color2 = int_color[current - 1];
        colorComp2 = int_compColor[current - 1];
        hexColor2 = string_color[current - 1];

        //change number of colors displayed
        current = numColors;

        //reset arrays
        int_color = new int[current];
        int_compColor = new int[current];
        string_color = new String[current];

        //put first color back in
        int_color[0] = color1;
        int_compColor[0] = colorComp1;
        string_color[0] = hexColor1;

        //put last color back in
        int_color[current - 1] = color2;
        int_compColor[current - 1] = colorComp2;
        string_color[current - 1] = hexColor2;

        findMiddle();
    }

    private void setText1() {
        validInput = false;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Color 1");
        alertDialogBuilder.setMessage("Enter RGB value: #000000");

        final EditText userInput = new EditText(this);
        alertDialogBuilder.setView(userInput);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String input = (userInput.getText()).toString();
                if(input.length() == 7 && input.lastIndexOf('#') == 0 &&
                        input.matches("#[A-Fa-f0-9]+")) {
                    setColor1(input.toUpperCase());
                    validInput = true;
                }
                else {
                    dialogInterface.cancel();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void setColor1(String c) {
        int color = Color.parseColor(c), colorComp;
        int r = Color.red(color), g = Color.green(color), b = Color.blue(color);
        String hexColor;

        hexColor = String.format("#%06X", (0xFFFFFF & color));

        r = 255 - r; g = 255 - g; b = 255 - b;
        colorComp = Color.rgb(r, g, b);

        int_color[0] = color;
        int_compColor[0] = colorComp;
        string_color[0] = hexColor;

        findMiddle();
    }

    private void setText2() {
        validInput = false;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Color 2");
        alertDialogBuilder.setMessage("Enter RGB value: #000000");

        final EditText userInput = new EditText(this);
        alertDialogBuilder.setView(userInput);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String input = (userInput.getText()).toString();
                if(input.length() == 7 && input.lastIndexOf('#') == 0 &&
                        input.matches("#[A-Fa-f0-9]+")) {
                    setColor2(input.toUpperCase());
                }
                else {
                    dialogInterface.cancel();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void setColor2(String c) {
        int color = Color.parseColor(c), colorComp;
        int r = Color.red(color), g = Color.green(color), b = Color.blue(color);
        String hexColor;

        hexColor = String.format("#%06X", (0xFFFFFF & color));

        r = 255 - r; g = 255 - g; b = 255 - b;
        colorComp = Color.rgb(r, g, b);

        int_color[current - 1] = color;
        int_compColor[current - 1] = colorComp;
        string_color[current - 1] = hexColor;

        findMiddle();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_random:
                randomizeColors();
                break;
            case R.id.btn_setNum:
                setNumColors();
                break;
            case R.id.btn_setColor1:
                setText1();
                break;
            case R.id.btn_setColor2:
                setText2();
                break;
        }
    }
}
