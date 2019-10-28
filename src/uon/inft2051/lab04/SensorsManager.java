package uon.inft2051.lab04;


import com.codename1.system.NativeLookup;
import com.codename1.ui.Display;
import com.codename1.ui.Graphics;
import com.codename1.sensors.*;

import java.util.Random;

public class SensorsManager {

    public static final int TYPE_ACCELEROMETER = 1;

    private long lastUpdate = 0;
    private float lastX, lastY, lastZ;
    public static final int SHAKE_THRESHOLD = 600;

    private int type, samplingRate;

    public SensorListener listener;

    public static SensorsManager accel;

    private static SensorsNative sensors;

    public void init()
    {
        // Get the sensor manager. If null is returned, the device does not support an accelerometer
        SensorsManager sensorsManager = SensorsManager.getSensorsManager(SensorsManager.TYPE_ACCELEROMETER);
        if (sensorsManager != null)
        {
            // Register a callback. This is an 'anonymous object' which implements the callback as a method
            sensorsManager.registerListener(new SensorListener() {
                @Override
                public void onSensorChanged(long timestamp, float x, float y, float z) {

                    long curTime = timestamp;

                    if ((curTime - lastUpdate) > 100) {
                        long diffTime = (curTime - lastUpdate);
                        lastUpdate = curTime;

                        float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;

                        if(speed >= SHAKE_THRESHOLD){
                            // Here is where the character will have a chance to dodge
                        }

                        lastX = x;
                        lastY = y;
                        lastZ = z;
                    }
                }
            });
        }
    }
    

    SensorsManager(int type){
        this.type = type;
    }

    public static SensorsManager getSensorsManager(int type, int samplingRate){
        sensors = (SensorsNative) NativeLookup.create(SensorsNative.class);
        if (sensors != null && sensors.isSupported()){
            SensorsManager sensor = new SensorsManager(type);
            if (type == TYPE_ACCELEROMETER){
                accel = sensor;
            }

            sensors.init(type);
            if(samplingRate > 0)
                sensors.setSamplingRate(samplingRate);
        }

        return null;
    }

    public static SensorsManager getSensorsManager(int type) {
        return getSensorsManager(type, 0);
    }

    public void registerListener(SensorListener listener) {
        if(this.listener == null && listener == null) {
            return;
        }
        if(listener != null){
            sensors.registerListener(type);
        }
        this.listener = listener;
    }

    public void deregisterListener(SensorListener listener) {
        sensors.deregisterListener(type);
        this.listener = null;
    }
}

