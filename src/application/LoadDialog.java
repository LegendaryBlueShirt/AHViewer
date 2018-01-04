package application;

import java.io.File;
import java.io.IOException;
import java.util.List;

import application.AHCharacters.AHCharacter;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class LoadDialog extends Dialog<ButtonType> {
	private boolean folderOk = false;
	public ObjectBinding<GameType> binding;
	
	@FXML
    RadioButton modeAh3lmsss;        
    @FXML
    TextField ahHome;
    
    @FXML protected void handleLoadAction(ActionEvent event) {
    		if(folderOk) {
    			setResult(ButtonType.OK);
    		}
        close();
    }
    
    @FXML protected void handleCancelAction(ActionEvent event) {
    		setResult(ButtonType.CLOSE);
    		close();
    }
    
    DirectoryChooser fileChooser = new DirectoryChooser();
    @FXML protected void showFolderChooser(ActionEvent event) {
	    	fileChooser.setTitle("Open Resource File");
	    	fileChooser.setInitialDirectory(new File("."));
	    File selectedFolder = fileChooser.showDialog(getDialogPane().getScene().getWindow());
	    if(selectedFolder != null) {
	    	ahHome.setText(selectedFolder.getPath());
	    }
    }
    
    public File getFolder() {
    		return new File(ahHome.getText());
    }
    
    public GameType getMode() {
    		return binding.getValue();
    }
	
	public LoadDialog() {
		super();
		
		setTitle("Loading Options");
		setResult(ButtonType.CANCEL);
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoadDialog.fxml"));
			loader.setController(this);
			Parent root = loader.load();
			//Parent root = FXMLLoader.load(getClass().getResource("LoadDialog.fxml"));
			//root.setController(this);
	        getDialogPane().setContent(root);
	        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            Node closeButton = getDialogPane().lookupButton(ButtonType.CLOSE);
            closeButton.managedProperty().bind(closeButton.visibleProperty());
            closeButton.setVisible(false);
	        
	        binding = new ObjectBinding<GameType>() {
		        	{
		        		super.bind(modeAh3lmsss.selectedProperty());
		        	}

				@Override
				protected GameType computeValue() {
					if(modeAh3lmsss.isSelected()) {
						return GameType.AH3LMSSS;
					}
					return GameType.UNKNOWN;
				}
	        };
	        binding.addListener(new ChangeListener<GameType>(){
				@Override
				public void changed(ObservableValue<? extends GameType> observable, GameType oldValue, GameType newValue) {
					ahHome.setText(ahHome.getText());
				}});;
				ahHome.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					File validatedFolder = validateFolder(new File(newValue));
					if(validatedFolder == null) {
						folderOk = false;
					} else {
						ahHome.setText(validatedFolder.getPath());
						folderOk = true;
					}
				}});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private File validateFolder(File folder) {
		File packFile, tempFolder;
		switch(getMode()) {
		case AH3LMSSS:
			List<AHCharacter> stcharacters = AHCharacters.getCharacters();
			for(AHCharacter character: stcharacters) {
				tempFolder = new File(folder, "dummy");
				do {
					tempFolder = tempFolder.getParentFile();
					packFile = new File(tempFolder, character.getDataFile());
				} while(!packFile.exists() && (tempFolder.getParentFile() != null));
				if(!packFile.exists()) {
					return null;
				}
				folder = tempFolder;
			}
			break;
		case UNKNOWN:
			return null;
		default:
			break;
		}
		return folder;
	}
}
