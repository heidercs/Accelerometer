package com.accelerometer.app;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.HashMap;
import java.util.List;


public class FragmentSensor extends Fragment implements SensorEventListener {
    public static int MY_DELAY=SensorManager.SENSOR_DELAY_NORMAL; //10^6 = 1 segundo
    public static int SIZE_BUFFER=100;

    private View view;
    private SensorManager sensorManager;
    private TextView textView;
    private ImageView imageView;
    private Spinner menuSensor;
    private Sensor sensorNow;

    private SensorData sensorData;
    private HashMap<String, Integer> hashSensors = new HashMap<String, Integer>();
    private boolean started = false;


    public FragmentSensor() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sensor, container, false);

        textView = (TextView)view.findViewById(R.id.textView);
        final ToggleButton toggleButton = (ToggleButton)view.findViewById(R.id.toggle);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onExecute(view);
            }
        });

        sensorData = new SensorData(SIZE_BUFFER);

        // Create a simple drawable and set it in the ImageView
        imageView = ((ImageView) view.findViewById(R.id.imageView));
        imageView.setImageDrawable(new DrawSignal(sensorData));

        displayAllSensors();

        /*TimerTask task = new TimerTask(){
            @Override
            public void run() {
                activateSensor();
            }
        };
        new Timer("running").schedule(task, 1000, 2000);*/

        return view;
    }

    public void displayAllSensors() {
        // Get the SensorManager
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        // List of Sensors Available
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // get the information of all sensors
        for(Sensor elem : sensorList)
        {
            hashSensors.put(elem.getName(), elem.getType());
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = new  ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, hashSensors.keySet().toArray());
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        menuSensor = (Spinner)view.findViewById(R.id.menuSensor);
        menuSensor.setAdapter(adapter);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (started) {
            Float x = (Float)sensorEvent.values[0];
            Float y = (Float)sensorEvent.values[1];
            Float z = (Float)sensorEvent.values[2];
            Long timestamp = System.currentTimeMillis();
            sensorData.addItem(timestamp, x, y, z);
            textView.setText(sensorData.toStringLastItem());
            imageView.invalidate(); // call to draw
        }
    }

    public void activateSensor()
    {
        String name_sensor = menuSensor.getSelectedItem().toString();
        int type = hashSensors.get(name_sensor);
        switch (type)
        {
            case Sensor.TYPE_ACCELEROMETER:
                sensorNow = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                textView.setText("Acc:");
                break;
            case Sensor.TYPE_ORIENTATION:
                sensorNow = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
                textView.setText("Ori: ");
                break;
            case Sensor.TYPE_GRAVITY:
                sensorNow = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
                textView.setText("Gra:");
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                sensorNow = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                textView.setText("Mag:");
                break;
        }
        sensorManager.registerListener(this, sensorNow, MY_DELAY ); //SensorManager.SENSOR_DELAY_NORMAL
    }

    public void onExecute(View view) {
        boolean checked = ((ToggleButton)view).isChecked();
        if(checked) { // start
            sensorData.clear();
            // save prev data if available
            started = true;
            activateSensor();
        }
        else { // stop
            started = false;
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (started == true) {
            sensorManager.registerListener(this, sensorNow, MY_DELAY); //SensorManager.SENSOR_DELAY_NORMAL
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (started == true) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
