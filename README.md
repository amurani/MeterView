# MeterView

This provided code allows a developer to use a circular meter to show progress in their application as opposed to the default progress indicator provided by android.

The view allows for some customization in the xml. One can customize:
1. `spacing` which is the distance between the meter and the edges of the view. Much like padding.
2. `lineWidth` which is the thikcness of the meter
3. `lineColor` which is the color of the meter
4. `pathColor` which is the color of the path the meter follows

For example:

`<com.amurani.meterview.MeterView
    	android:id="@+id/meter"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="5"
        meterview:spacing="10"
        meterview:lineWidth="5"
        meterview:lineColor="#0af"
        meterview:pathColor="#ccc" />`

In the JAVA section, it is used as any other view:
`MeterView meter = (MeterView) this.findViewById(R.id.meter);`

Its object allows access to the following methods:
1. `prepare(int stop, int interval)`. This defines the ending value and the refresh interval
2. `start()`. This starts the meter.
3. `pause()`. This pauses the meter.
4. `stop()`. This stops and resets the meter.
5. `runAsync(boolean runasync)`. This makes the meter run asychronously.
6. `setValue(int value, int total)`. This allows you to define your own value for the meter or update from a Thread or loop or what have you.
7. `getValue()`. This gives a percentage value of the meter's progress.
8. `setOnFinishListener(OnFinishListener f)`. This method allows one to define a custom action to be performed once the meter completes a cycle.

Simple implementation of how to use the View are:
1. in a timer,

![](https://f.cloud.github.com/assets/2022520/2366032/a992adf2-a6ec-11e3-98bf-b655cd0f21fe.png)

2. when monitoring downloads,

![](https://f.cloud.github.com/assets/2022520/2366037/db0d1a5c-a6ec-11e3-9fdf-008fd901e3d1.png)

3. display the battery level,

![](https://f.cloud.github.com/assets/2022520/2366039/e7dfaff6-a6ec-11e3-9f6f-09efef8cf325.png)

More needs to be added and the code is not perfect. Improvements are greatly encouraged. Use the code as you please for I can't guaranten future or maintaian it myself.

(_Sorry for the image size BTW_)
