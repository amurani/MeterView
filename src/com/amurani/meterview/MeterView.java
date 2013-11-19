package com.amurani.meterview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

interface OnFinishListener {
	public void onFinish();
}

public class MeterView extends View implements Runnable {
	
	private int angle = 0, spacing = 0, lineWidth = 0;
	private int lineColor = 0, pathColor = 0;
	private boolean runasync = true, count = false, done = false;
	private int start = 0, stop;
	private int interval = 1000, value = 0, total = 1;
	private Thread thread = new Thread(this);
	private Handler handler = new Handler();
	private OnFinishListener f = new OnFinishListener() {
		public void onFinish() { }
	};
	
	public MeterView(Context context) {
		super(context);
	}
	
	public MeterView(Context context, AttributeSet attributes) {
		super(context, attributes);
		TypedArray a = context.obtainStyledAttributes(attributes, R.styleable.MeterViewAttr);
		this.angle = a.getInteger(R.styleable.MeterViewAttr_angle, 0);
		this.spacing = a.getInteger(R.styleable.MeterViewAttr_spacing, 0);
		this.lineWidth = a.getInteger(R.styleable.MeterViewAttr_lineWidth, 0);
		this.lineColor = a.getInteger(R.styleable.MeterViewAttr_lineColor, 0);
		this.pathColor = a.getInteger(R.styleable.MeterViewAttr_pathColor, 0);
		a.recycle();
	}
	
	public void runAsync(boolean runasync) {
		this.runasync = runasync;
	}
	
	public void setValue(int value, int total) {
		this.angle = (value * 360) / total;
		this.invalidate();
	}
	
	public int getValue() {  return this.angle; }
	
	public void onDraw(Canvas canvas) {
		int width = this.getWidth();
		int height = this.getHeight();
		int minLength = (width < height) ? width : height;
		int textSize = minLength / 4;
		
		int left = (width > minLength) ?  spacing + (width - minLength) / 2 : spacing;
		int top = (height > minLength) ? spacing + (height - minLength) / 2 : spacing;
		int right = (width > minLength) ? width - left : minLength - left;
		int bottom = (height > minLength) ? height - top : minLength - top;
		
		Paint p = new Paint();
		p.setTextSize(textSize);
		p.setStrokeWidth(lineWidth);
		p.setAntiAlias(true);
		p.setStyle(Paint.Style.STROKE);
		p.setTextAlign(Paint.Align.CENTER);
		
		RectF area = new RectF(left, top, right, bottom);
		
		// Draw path
		p.setColor(pathColor);
		canvas.drawArc(area, -90, 360, false, p);
		
		// Draw actual value
		p.setColor(lineColor);
		canvas.drawArc(area, -90, angle, false, p);
		
		int offsetX = getWidth() / 2;
		int offsetY = (getHeight() - ((int) p.ascent() + (int) p.descent())) / 2;
		int num = (angle * 100) / 360;
		String value = (num == 0 || num == 100) ? String.valueOf("") : String.valueOf(num);
		p.setStyle(Paint.Style.FILL);
		if(minLength >= textSize || !value.matches("0") || !value.matches("100"))
			canvas.drawText(value, offsetX, offsetY, p);
		
		if(num >= 100 && !this.runasync) {
			this.setValue(0, 1);
			this.f.onFinish();
		}
	}
	
	public int hex2rgb(String hex) {
		int r = (hex.length() < 6) ? Integer.valueOf(hex.substring(1, 2) + hex.substring(1, 2), 16) : Integer.valueOf(hex.substring(1, 3), 16);
		int g = (hex.length() < 6) ? Integer.valueOf(hex.substring(2, 3) + hex.substring(2, 3), 16) : Integer.valueOf(hex.substring(3, 5), 16);
		int b = (hex.length() < 6) ? Integer.valueOf(hex.substring(3, 4) + hex.substring(3, 4), 16) : Integer.valueOf(hex.substring(5, 7), 16);
		return Color.rgb(r, g, b);
	}
	
	public void run() {
		while (this.runasync) {
			try {
				if (this.count) this.value += 1;
				
				if (this.value >= this.total) {
					this.count = false;
					this.done = true;
				}
				
				if (this.done) {
					this.handler.post(new Runnable() {
						public void run() {
							value = start;
							total = stop;
							f.onFinish();
						}
					});
					this.done = false;
				}
				
				Thread.sleep(this.interval);
			} catch (InterruptedException e) {
				Log.i("HIR", e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				Log.i("HIR", "Error!");
			}
			
			this.handler.post(new Runnable() {
				public void run() {
					setValue(value, total);
				}
			});
		}
	}
	
	
	public void prepare(int stop, int interval) {
		this.stop = stop;
		this.value = this.start;
		this.total = this.stop;
		this.interval  = interval;
		
		if (this.thread.isAlive()) { 
			this.thread = new Thread(this);
			this.done = false;
		}
		
		this.thread.start();
		
	}
	
	public void start() { this.count = true; }
	public void pause() { this.count = false; }
	public void stop() { this.count = false; this.done = false; setValue(0, 1); }
	public void setOnFinishListener(OnFinishListener f) { this.f = f; }
}