package com.poulpicious.pui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.poulpicious.util.Fonts;

public class PUIGauge extends PUI {

	private PUIProgressBar progress;
	private PUIButton minusButton;
	private PUIButton plusButton;

	private ArrayList<GaugeListener> gaugeListeners;

	public PUIGauge(Color background, Color foreground, float value, float maxValue, float growth, float x, float y,
			float w, float h) {
		super(x, y, w, h);
		
		System.out.println("Gauge loading : " + value + " / " + maxValue);

		this.gaugeListeners = new ArrayList<GaugeListener>();

		minusButton = new PUIButton("-", Fonts.mediumFont, x, 0);

		minusButton.addMouseActionListener(new MouseActionListener() {
			@Override
			public void onMouseClick(int button) {
				boolean b = true;
				for (GaugeListener gl : gaugeListeners) {
					b = gl.onSub();
					if (!b)
						break;
				}
				
				if (b)
					progress.decrement();
			}
		});

		plusButton = new PUIButton("+", Fonts.mediumFont, x + w - minusButton.getBounds().getWidth() - 20, 0);
		plusButton.move(plusButton.getBounds().getWidth(), 0);

		plusButton.addMouseActionListener(new MouseActionListener() {
			@Override
			public void onMouseClick(int button) {
				boolean b = true;
				for (GaugeListener gl : gaugeListeners) {
					b = gl.onAdd();
					if (!b)
						break;
				}
				if (b)
					progress.increment();
			}
		});

		progress = new PUIProgressBar(background, foreground, value, maxValue, growth,
				x + minusButton.getBounds().getWidth() + 10, 0,
				w - plusButton.getBounds().getWidth() - minusButton.getBounds().getWidth() - 20, h);

		this.addComponent(minusButton);
		this.addComponent(plusButton);
		this.addComponent(progress);
	}

	@Override
	protected void renderSelf(Graphics g) {
		super.renderSelf(g);
	}

	public void addGaugeListener(GaugeListener gl) {
		this.gaugeListeners.add(gl);
	}
}
