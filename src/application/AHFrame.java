package application;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

public class AHFrame {
	private static final String KEY_DURATION = "duration";
	private final HashMap<String, Integer> attribs = new HashMap<String, Integer>();
	
	int[] images = new int[8];
	int[] boxes = new int[8];
	
	public AHFrame(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		
		for(int n = 0;n < 8;n++) {
			images[n] = buffer.getShort();
		}
		
		for(int n = 0;n < 8;n++) {
			boxes[n] = buffer.getShort();
		}
		
		setAttribute(KEY_DURATION, buffer.get(42));
	}
	
	public void setAttribute(String key, int value) {
		attribs.put(key, value);
	}
	
	public int getAttribute(String key) {
		return attribs.get(key);
	}
	
	public int getDuration() {
		return getAttribute(KEY_DURATION);
	}
	
	public int[] getImages() {
		return images;
	}
	
	public int[] getBoxes() {
		return boxes;
	}
}
