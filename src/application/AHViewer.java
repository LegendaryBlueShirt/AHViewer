package application;

import java.io.IOException;
import java.util.Optional;

import javafx.scene.control.ButtonType;
import view.FrameViewer;


public class AHViewer extends FrameViewer {
	
	public static void main(String[] args) {
		launch(args);
	}

	static LoadDialog loadDialog;
	@Override
	protected void showDirectoryChooser() {
		if(loadDialog == null) {
			loadDialog = new LoadDialog();
		}
		Optional<ButtonType> result = loadDialog.showAndWait();
		if(result.get() == ButtonType.OK) {
			switch(loadDialog.getMode()) {
			case AH3LMSSS:
				presenter = new AHPresenter();
				break;
			default:
				break;
			}
			presenter.setRootDir(loadDialog.getFolder());
			getView().setPresenter(presenter);
			setCharacterList(presenter.getCharacters());
			try {
				start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected String getTitle() {
		return "Arcana Heart 3 Lovemax Six Stars!!!!!! FrameDisplay";
	}
}
