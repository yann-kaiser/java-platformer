package com.poulpicious.pui;

public interface GaugeListener {
	public default boolean onAdd() {
		return true;
	}

	public default boolean onSub() {
		return true;
	}

}
