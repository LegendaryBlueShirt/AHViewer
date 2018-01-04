package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import application.AHCharacters.AHCharacter;
import view.Surface;
import view.interfaces.FramePresenter;
import view.interfaces.ViewerWindow;
import view.model.CharacterDef;
import view.model.Data;
import view.model.FrameGroup;
import view.model.Hitbox;

public class AHPresenter implements FramePresenter {
	private File rootDir;
	private ActFile currentCharacterAct;
	private AHSprites spriteSource;
	
	private AHAnim anim;
	int currentFrame = 0;
	
	@Override
	public void setRootDir(File root) {
		this.rootDir = root;
	}

	@Override
	public List<AHCharacter> getCharacters() {
		return AHCharacters.getCharacters();
	}

	@Override
	public void load(CharacterDef character, LoadCallback loader) {
		AHCharacter ahCharacter = (AHCharacter) character;
		File dataFile = new File(rootDir, ahCharacter.getDataFile());
		File packFile = new File(rootDir, ahCharacter.getPacFile());
		
		currentCharacterAct = new ActFile(dataFile);
		try {
			PacFile mainPacFile = PacFile.unpack(packFile);
			PacFile spritePacFile = new PacFile(mainPacFile.getFileHandle(), mainPacFile.getFileOffset(1));
			spriteSource = new AHSprites(spritePacFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loader.onLoadComplete();
	}

	@Override
	public List<AHAnim> getFrameGroups() {
		if(currentCharacterAct != null) {
			return currentCharacterAct.getAnims();
		}
		return null;
	}

	@Override
	public void setFrameGroup(FrameGroup group) {
		this.anim = (AHAnim) group;
		currentFrame = 0;
	}

	@Override
	public void setTime(int sequenceTime) {
		if(anim == null) {
			return;
		}
		currentFrame = currentCharacterAct.getFrameForTime(anim, sequenceTime);
	}

	@Override
	public void advanceFrame() {
		if(anim == null) {
			return;
		}
		if((currentFrame+1) < anim.frames.length) {
			currentFrame++;
		}
	}

	@Override
	public void retreatFrame() {
		if(currentFrame > 0) {
    			currentFrame--;
		}
	}

	@Override
	public void renderCurrentFrame(ViewerWindow window) {
		AHFrame frame = currentCharacterAct.getFrame(anim.frames[currentFrame]);
		for(int n = 0;n < 8;n++) {
			if(frame.images[n] == 0)
				continue;
			Surface surface = window.getSurface();
			AHSprite sprite = currentCharacterAct.getSprite(frame.images[n]);
			if(sprite.getSprNo() > spriteSource.getNumSprites())
				continue;
			surface.setSprite(spriteSource.getSprite(sprite.getSprNo()-1), sprite.getAxisX(), sprite.getAxisY());
			surface.setPosition(0, 0, 0);
		}
	}
	
	@Override
	public List<Hitbox> getCurrentHitboxes() {
		List<Hitbox> boxes = new ArrayList<Hitbox>();
		
		if(anim != null) {
			AHFrame frame = currentCharacterAct.getFrame(anim.frames[currentFrame]);
			for(int n = 0;n < 8;n++) {
				if(frame.boxes[n] == 0) {
					continue;
				}
				boxes.add(currentCharacterAct.getBox(frame.boxes[n]));
			}
		}
		
		return boxes;
	}

	@Override
	public List<Data> getCurrentData() {
		List<Data> data = new ArrayList<Data>();
		
		if(anim != null) {
			AHFrame frame = currentCharacterAct.getFrame(anim.frames[currentFrame]);
			
			data.add(new Data(String.format("Sequence %d/%d", currentFrame+1, anim.frames.length), false));
			data.add(new Data(String.format("Frame %d    Duration %d", currentCharacterAct.getTimeForFrame(anim, currentFrame), frame.getDuration()), false));
		}
		
		return data;
	}

}
