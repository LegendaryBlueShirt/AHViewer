package application;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

public class AHSprites {
	private static final byte[] MAGIC = {0x48, 0x49, 0x50, 0x00};
	
	final PacFile pacFile;
	public AHSprites(PacFile pacFile) {
		this.pacFile = pacFile;
	}
	
	public Image getSprite(int index) {
		try {
			ByteBuffer imageData = ByteBuffer.wrap(pacFile.getFileData(index));
			imageData.order(ByteOrder.LITTLE_ENDIAN);
			byte[] buffer = new byte[4];
			imageData.get(buffer);
			if(!new String(MAGIC).equals(new String(buffer))) {
				System.err.println("Header mismatch");
				return null;
			}
			imageData.getInt();
			int size = imageData.getInt();
			if(size != imageData.capacity()) {
				System.err.println("Filesize mismatch");
				return null;
			}
			imageData.getInt();
			int width = imageData.getInt();
			int height = imageData.getInt();
			imageData.getInt();
			imageData.getInt();
			int[] defaultPal = new int[256];
			imageData.order(ByteOrder.LITTLE_ENDIAN);
			for(int n = 0;n < 256;n++) {
				defaultPal[n] = imageData.getInt();
			}
			defaultPal[0] = defaultPal[0]&0xFFFFFF;
			int position = 0;
			byte[] raster = new byte[width*height];
			while(position < raster.length) {
				byte value = imageData.get();
				int count = imageData.get()&0xFF;
				for(int n = 0;n < count;n++) {
					raster[position++] = value;
				}
			}
			
			WritableImage img = new WritableImage(width, height);
			img.getPixelWriter().setPixels(0, 0, width, height, PixelFormat.createByteIndexedInstance(defaultPal), raster, 0, width);
			return img;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getNumSprites() {
		return pacFile.getNumFiles();
	}
}
