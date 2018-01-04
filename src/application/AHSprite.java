package application;

public class AHSprite {
	private int sprNo, ax, ay;
	
	public AHSprite(byte[] data) {
		sprNo = (data[2]&0xff) | ((data[3]&0xff) << 8);
		ax = (short) ((data[4]&0xff) | ((data[5]&0xff) << 8));
		ay = (short) ((data[6]&0xff) | ((data[7]&0xff) << 8));
	}
	
	public int getSprNo() {
		return sprNo;
	}
	
	public int getAxisX() {
		return ax;
	}
	
	public int getAxisY() {
		return -ay;
	}
}
