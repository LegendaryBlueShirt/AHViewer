package application;

import view.model.FrameGroup;

public class AHAnim extends FrameGroup {
	private final int id;
	public final int[] frames;
	
	public AHAnim(int id, int[] frames) {
		this.id = id;
		this.frames = frames;
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return ""+id;
	}
}
