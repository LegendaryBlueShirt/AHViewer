package application;

import javafx.scene.paint.Color;
import view.model.Hitbox;

public class AHBox implements Hitbox {
	private short x, y, w, h, typeFlag;
	
	public AHBox(byte[] data) {
		typeFlag = data[1];
		x = (short) ((data[6]&0xff) | ((data[7]&0xff) << 8));
		y = (short) ((data[8]&0xff) | ((data[9]&0xff) << 8));
		w = (short) ((data[10]&0xff) | ((data[11]&0xff) << 8));
		h = (short) ((data[12]&0xff) | ((data[13]&0xff) << 8));
	}
	
	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return -y-h;
	}

	@Override
	public int getWidth() {
		return w;
	}

	@Override
	public int getHeight() {
		return h;
	}

	@Override
	public Color getColor() {
		switch(typeFlag) {
			case 0:
				return new Color(1,1,1,.8);
			case 1: //Upper Body
			case 2: //Lower Body
				return new Color(0,0,1,.8);
			case 6: //Projectile Reflect
				return new Color(0,1,1,.8);
			case 8: //Normal Attack
				return new Color(1,0,0,.8);
			case 4: //Normal Clash
			case 74: //Bhanri Clash
				return new Color(1,1,0,.8);
			default:
				System.out.println("Boxtype - "+typeFlag);
				return new Color(0,1,0,.8);
		}
	}

}
