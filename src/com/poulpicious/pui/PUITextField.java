package com.poulpicious.pui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

import com.poulpicious.util.Fonts;

public class PUITextField extends PUIPanel implements KeyListener {

	private PUILabel content;
	private String mask;
	private String realContent;

	private float cursorPosition;
	private int cursorIndex;

	private boolean masked;
	// private PUILabel

	public PUITextField(String placeholder, Input input, float x, float y, float w, float h) {
		super(x, y, w, h);
		input.addMouseListener(new PUIMouseListener(this));
		input.addKeyListener(this);

		content = new PUILabel(placeholder, Fonts.formFont, 5, 0);
		content.setTextColor(Color.black);
		content.getBounds().setWidth(this.getBounds().getWidth());
		content.getBounds().setHeight(content.getFont().getHeight("."));

		this.addComponent(content);
		this.centerContentOnY();

		this.backgroundColor = Color.white;

		this.cursorPosition = this.bounds.getX() + 5;

		this.realContent = content.getText();

		this.addMouseActionListener(new MouseActionListener() {
			@Override
			public void onMouseClick(int button) {
				float mx = input.getMouseX() - content.getBounds().getX();

				float nextCursorX = content.getBounds().getX();
				
				if (mx > content.getFont().getWidth(content.getText())) {
					cursorPosition = content.getFont().getWidth(content.getText()) + content.getBounds().getX();
					cursorIndex = content.getText().length();
					return;
				}
				for (int i = 0; i < content.getText().length(); i++) {
					float textWidth = content.getFont().getWidth(content.getText().substring(0, i));

					if (mx < textWidth)
						break;

					nextCursorX = textWidth + content.getBounds().getX();
					cursorIndex = i;
				}

				cursorPosition = nextCursorX;
			}
		});

		mask = "";
		for (int i = 0; i < realContent.length(); i++)
			mask += "*";
	}

	@Override
	protected void renderSelf(Graphics g) {
		if (hasFocus) {
			float height = Fonts.mediumFont.getHeight("|");
			Fonts.mediumFont.drawString(cursorPosition, this.bounds.getCenterY() - height / 2, "|",
					this.content.getTextColor());
		}

		realContent = this.content.getText();

		if (masked) {
			this.content.setText(mask);
		}
	}

	@Override
	protected void postRender(Graphics g) {
		this.content.setText(realContent);
	}

	@Override
	public void update(Input input, int delta) {
		if (!hasFocus)
			return;

		realContent = this.content.getText();

		if (cursorIndex < 0)
			cursorIndex = 0;
		else if (cursorIndex > this.content.getText().length())
			cursorIndex = this.content.getText().length();

		String value = (masked) ? mask : this.content.getText();
		cursorPosition = this.bounds.getX() + this.content.getFont().getWidth(value.substring(0, cursorIndex)) + 5;

		this.content.setText(realContent);
	}

	@Override
	public void inputEnded() {
	}

	@Override
	public void inputStarted() {
	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void setInput(Input input) {
	}

	@Override
	public void keyPressed(int key, char c) {
		if (!hasFocus)
			return;

		if (key == Input.KEY_BACK) {
			String value = content.getText();
			if ((cursorIndex > 0) && (value.length() > 0)) {
				float width = content.getFont().getWidth("" + content.getText().charAt(cursorIndex - 1));
				if (cursorIndex < value.length()) {
					value = value.substring(0, cursorIndex - 1) + value.substring(cursorIndex);
				} else {
					value = value.substring(0, cursorIndex - 1);
				}
				mask = mask.substring(0, mask.length() - 1);
				cursorIndex--;
				cursorPosition -= width;
			}

			this.content.setText(value);
		} else if (key == Input.KEY_LEFT) {
			cursorIndex--;
		} else if (key == Input.KEY_RIGHT) {
			cursorIndex++;
		} else if ((c < 127) && (c > 31)) {

			String value = this.content.getText();
			if (cursorIndex < value.length()) {
				value = value.substring(0, cursorIndex) + c + value.substring(cursorIndex);
			} else {
				value = value.substring(0, cursorIndex) + c;
			}
			mask = mask + "*";
			this.content.setText(value);
			cursorIndex++;
		}
	}

	@Override
	public void keyReleased(int key, char c) {
	}

	public boolean isMasked() {
		return masked;
	}

	public void setMasked(boolean masked) {
		this.masked = masked;
	}

	public String getText() {
		return realContent;
	}

	public PUILabel getContent() {
		return content;
	}

	public void setContent(PUILabel content) {
		this.content = content;
	}

}
