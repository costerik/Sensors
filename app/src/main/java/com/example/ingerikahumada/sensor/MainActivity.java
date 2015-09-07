package com.example.ingerikahumada.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager sm;
    private List<Sensor> ls;
    private Sensor s;
    private TextView tx, tx1, tx2, tx3;
    private Spinner spinner,spinner2;
    private Button btn,btn2;
    private int op=0,op2=0, delay=2;
    private SensorEventListener contextSensor;

    public Spinner loadSpinner(Spinner s, int arrayReference){
        ArrayAdapter<CharSequence> list=ArrayAdapter.createFromResource(this,arrayReference,android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(list);
        return s;
    }

    public void spinnerInterface(Spinner s){
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getId()) {
                    case R.id.spinner:
                        //op = parent.getItemAtPosition(position).toString();
                        op = position;
                        break;
                    default:
                        //op2 = parent.getItemAtPosition(position).toString();
                        op2 = position;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void typeSensor(int arg0){
        switch(arg0){
            case 0:
                if(sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null) {
                    s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                    sm.registerListener(this,s,delay);
                }
                break;
            default:
                if(sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)!=null) {
                    s = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                    sm.registerListener(this,s,delay);
                }
                break;
        }
    }

    public void delaySensorManager(int arg0){
        switch(arg0){
            case 0:
                delay=SensorManager.SENSOR_DELAY_FASTEST;
                break;
            case 1:
                delay=SensorManager.SENSOR_DELAY_GAME;
                break;
            case 2:
                delay=SensorManager.SENSOR_DELAY_NORMAL;
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tx=(TextView)findViewById(R.id.textview);
        tx1=(TextView)findViewById(R.id.textview_1);
        tx2=(TextView)findViewById(R.id.textview_2);
        tx3=(TextView)findViewById(R.id.textview_3);
        spinner=(Spinner)findViewById(R.id.spinner);
        spinner2=(Spinner)findViewById(R.id.spinner2);
        btn=(Button)findViewById(R.id.button);
        btn2=(Button)findViewById(R.id.button2);
        sm=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        s=sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        //ls=sm.getSensorList(Sensor.TYPE_ALL);
        contextSensor=this;

        spinner=loadSpinner(spinner, R.array.spinner_list);
        spinner2=loadSpinner(spinner2,R.array.spinner2_list);

        spinnerInterface(spinner);
        spinnerInterface(spinner2);

        delaySensorManager(delay);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delaySensorManager(op2);
                typeSensor(op);
                tx.setText(s.toString() + " Delay: " + delay);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sm.unregisterListener(contextSensor);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        sm.registerListener(this,s,delay);
        tx.setText(s.toString()+" Delay: "+ delay);
        Log.i("Sensor", s.toString());
    }

    @Override
    public void onPause(){
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        tx1.setText("X");
        tx2.setText("Y");
        tx3.setText("Z");
        tx1.setText(tx1.getText()  +":"+ event.values[0]);
        tx2.setText(tx2.getText()+":"+ event.values[1]);
        tx3.setText(tx3.getText()+":"+ event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
