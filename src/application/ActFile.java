package application;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class ActFile {
	private static final int NANIMS = 2816;
	private static final int ANIMSIZE = 256;
	
	private final ByteBuffer data;
	
	private List<AHAnim> anims = new ArrayList<AHAnim>();
	private AHFrame[] frames;
	private AHBox[] boxes;
	private AHSprite[] sprites;
	
	public ActFile(File inputFile) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Unpk3.decompress(inputFile, baos);
		data = ByteBuffer.wrap(baos.toByteArray());
		data.order(ByteOrder.LITTLE_ENDIAN);
		
		byte[] lengths = new byte[NANIMS];
		data.position(NANIMS*ANIMSIZE);
		data.get(lengths);
		
		int nFrames = data.getInt();
		int nBoxes = data.getInt();
		int nImages = data.getInt();
		
		frames = new AHFrame[nFrames];
		boxes = new AHBox[nBoxes];
		sprites = new AHSprite[nImages];
		
		for(int n = 0;n < nFrames;n++) {
			byte[] frameData = new byte[92];
			data.get(frameData);
			frames[n] = new AHFrame(frameData);
		}
		
		for(int n = 0;n < nBoxes;n++) {
			byte[] boxData = new byte[14];
			data.get(boxData);
			boxes[n] = new AHBox(boxData);
		}
		
		for(int n = 0;n < nImages;n++) {
			byte[] spriteData = new byte[44];
			data.get(spriteData);
			sprites[n] = new AHSprite(spriteData);
		}
		
		for(int n = 0;n < NANIMS;n++) {
			data.position(n*ANIMSIZE);
			if(lengths[n] == 0) {
				continue;
			}
			int[] frames = new int[lengths[n]&0xff];
			for(int x = 0;x < lengths[n];x++) {
				frames[x] = data.getShort()&0xFFFF;
			}
			anims.add(new AHAnim(n, frames));
		}
		
		try {
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<AHAnim> getAnims() {
		return anims;
	}
	
	public AHFrame getFrame(int index) {
		return frames[index-1];
	}
	
	public AHBox getBox(int index) {
		return boxes[index-1];
	}
	
	public AHSprite getSprite(int index) {
		return sprites[index-1];
	}
	
	public int getSequenceDurationTotal(AHAnim anim) {
		int total = 0;
		for(int frameIndex: anim.frames) {
			AHFrame frame = frames[frameIndex];
			total += frame.getDuration();
		}
		return total;
	}
	
	public int getTimeForFrame(AHAnim anim, int currentFrame) {
		int time = 0;
		for(int n = 0;n < currentFrame;n++) {
			AHFrame frame = frames[anim.frames[n]];
			time += frame.getDuration();
		}
		return time;
	}
	
	public int getFrameForTime(AHAnim anim, int time) {
		int currentTime = time%getSequenceDurationTotal(anim);
		int frameNum = -1;
		for(int frameIndex: anim.frames) {
			AHFrame frame = frames[frameIndex];
			frameNum++;
			currentTime -= frame.getDuration();
			if(currentTime < 0)
				break;
		}
		return frameNum;
	}
}
