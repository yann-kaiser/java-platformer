package com.poulpicious.pui;

import java.util.LinkedHashMap;

import org.newdawn.slick.Graphics;

public class PUITable extends PUIPanel {
	
	private LinkedHashMap<String, PUITableCell> cells;
	private float borderSize;
	
	private float cellHeight;

	public PUITable(float x, float y, float w, float h) {
		super(x, y, w, h);
		
		this.cells = new LinkedHashMap<String, PUITableCell>();
		this.setBackgroundTransparent();
		
		borderSize = 5f;
		this.cellHeight = 50f;
	}
	
	@Override
	protected void renderSelf(Graphics g) {
		float y = 0;
		
		for (PUITableCell cell : cells.values()) {
			cell.move(0, this.bounds.getY() - cell.getBounds().getY() + y);
			cell.render(g);
			
			y += cell.getBounds().getHeight();
			y += borderSize;
		}
	}
	
	public void addCell(String id, String content) {
		this.cells.put(id, new PUITableCell(content, this.bounds.getX(), 0, this.bounds.getWidth(), cellHeight));
	}
	
	public void removeCell(String id) {
		this.cells.remove(id);
	}
	
	public PUITableCell getCell(String id) {
		return this.cells.get(id);
	}
	
	public int getCellCount() {
		return this.cells.size();
	}
	
	public void clearCells() {
		this.cells.clear();
	}
	

}
