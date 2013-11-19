package com.amurani.meterview;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class BatteryActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_layout);
		
		final MeterView meter = (MeterView) this.findViewById(R.id.meter);
		meter.runAsync(false);
		
		BroadcastReceiver br = new BroadcastReceiver(){
			public void onReceive(Context context, Intent intent){
				meter.setValue(intent.getIntExtra("level", 0), 100);
			}
		};
		
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(br, ifilter);
	}
}
