package controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import exception.ExistingElementException;
import exception.ImpossiblePercentageException;
import exception.InvalidCharException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Achievement;
import model.Console;
import model.Game;
import model.Manager;

public class ControlMenu implements Initializable{
	
	//Constants
	public final static String BACK_SYMBOL="«";
	public final static String ADD_SYMBOL="+";
	public final static String MINIMIZE_SYMBOL="-";
	public final static String SAVE_SYMBOL="\u2BC2";
	public final static String MARKED_SYMBOL="!";
	
	public final static String ICONS_PATH="med/icon/consoles/";
	public final static String DEFAULT_ICONS_PATH="med/icon/default/";
	
	//Attributes
	private Manager manager;
	private Console actualConsole;
	public Game actualGame;
	
	//Nodes
	@FXML private HBox header;
	@FXML private VBox pane;
	private VBox information;
	@FXML private ListView<HBox> list;
	private ContextMenu itemMenu;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			manager=new Manager();
		} catch (ExistingElementException | IOException | ImpossiblePercentageException | InvalidCharException e) {
			e.printStackTrace();//BAD
		}
	}
	
	//Generators
	public void generate() {
		
		header.getChildren().clear();
		list.getItems().clear();
		pane.getChildren().remove(information);
		if(itemMenu!=null){itemMenu.hide();}
		
		header.setAlignment(Pos.CENTER);
		
		if(actualConsole==null){//Manager
			generateManager();
		}
		else if(actualGame==null){//Console
			generateConsole();
		}
		else{//Game
			generateGame();
		}
	}

	public void generateManager() {
		
		//HEADER
		//~APP NAME
		Label space=new Label();
		
		Label appName=new Label("GameProgress");
		appName.getStyleClass().add("title");
		//~...
		
		//~ADD
		Button add=new Button(ADD_SYMBOL);
		add.setOnMouseClicked(event->{
			
			TextField consoleName=onActionAddButton(add);
			consoleName.setOnKeyPressed(kEvent->{
				
        		if(kEvent.getCode().equals(KeyCode.ENTER)){
        			try {manager.addConsole(consoleName.getText()); generate();}
        			catch (ExistingElementException e) {showAlert("Console with this name all ready exist!");}
        			catch (InvalidCharException e) {showAlert("Invalid character used (/, \\, :, *, ?, \", <, >, | or is empty)");}
        		}
        	});
		});
		//~...
		header.getChildren().addAll(space,appName, add);
		//...
		
		//LIST
		ArrayList<Console> consoles=manager.getList();
		for(int i=0; i<consoles.size(); i++){
			Console console=consoles.get(i);
			
			HBox itemBox=generateItemBox(console.toString(), console.getName()+".png", "Console.png");
			
			//PROGRESS
			String progress=(int)(console.calculateProgress()*100)+"%";
			itemBox.getChildren().add(new Label(progress));
			//...
			
			//OnAction
			itemBox.setOnMouseClicked(event->{
				//Open
				if(event.getButton()==MouseButton.PRIMARY){
					this.actualConsole=console;
					generate();
				}
				//...
				//Option Menu
				else if(event.getButton()==MouseButton.SECONDARY){
					generateItemMenu();
					
					//Delete
					MenuItem delete = new MenuItem("Delete");
			        delete.setOnAction(dEvent->{
			        	manager.deleteConsole(console.getName());
			        	generate();
			        });
					//...
					//Name
					MenuItem name = new MenuItem("Change Name");
					name.setOnAction(eEvent->{
			        	Node nameNode=itemBox.getChildren().get(1);
			        	itemBox.getChildren().remove(1);
			        	
			        	//toTextField
			        	if(nameNode instanceof Label){
				        	TextField consoleName=new TextField(console.toString());
				        	itemBox.getChildren().add(1,consoleName);
				        	
				        	consoleName.setOnKeyPressed(kEvent->{
				        		
				        		if(kEvent.getCode().equals(KeyCode.ENTER)){
				        			try {
										manager.updateNameConsole(console.getName(),consoleName.getText());
										generate();
									}
				        			catch (ExistingElementException e) {showAlert("Console with this name all ready exist!");}
				        			catch (InvalidCharException e) {showAlert("Invalid character used (/, \\, :, *, ?, \", <, >, | or is empty)");}
				        		}
				        		
				        	});
			        	}
			        	//...
			        	//toLabel
			        	else if(nameNode instanceof TextField){
				        	itemBox.getChildren().add(1, new Label(console.toString()));
			        	}
			        	//...
			        	
			        });
					//...
			        //Image
			        MenuItem image = new MenuItem("Change Image");
			        image.setOnAction(eEvent->{
			        	//Choose File
			        	Stage stage = (Stage) pane.getScene().getWindow();
				        FileChooser fileChooser = new FileChooser();
						fileChooser.setTitle("Image Selector");
						fileChooser.getExtensionFilters().add(new ExtensionFilter("PNG", "*.png"));
						File imageFile=fileChooser.showOpenDialog(stage);
						//...
						//Delete
						File newImageFile=new File(ICONS_PATH+console.getName()+".png");
						newImageFile.delete();
						//...
						//Change
						imageFile.renameTo(newImageFile);
						generate();
						//...
						
			        });
			        //...
			        
			        itemMenu.getItems().addAll(delete, name, image);
			        itemMenu.show(itemBox, event.getScreenX(), event.getScreenY());
				}
				//...
			});
			//...
			
			list.getItems().add(itemBox);
			
		}
		//...
		
	}
	
	public void generateConsole() {
		
		//HEADER
		//~Back
		Button back=new Button(BACK_SYMBOL);
		back.setOnMouseClicked(event->{
			this.actualConsole=null;
			generate();
		});
		//~...
		//~Console Name
		Label consoleName=new Label(actualConsole.toString());
		consoleName.getStyleClass().add("title");
		//~...
		//~Add
		Button add=new Button(ADD_SYMBOL);
		add.setOnMouseClicked(event->{
			
			TextField gameName=onActionAddButton(add);
			gameName.setOnKeyPressed(kEvent->{
        		
        		if(kEvent.getCode().equals(KeyCode.ENTER)){
        			try {actualConsole.addGame(gameName.getText(), 0, 0, "", false);generate();}
        			catch (ExistingElementException e) {showAlert("Game with this name all ready exist!");}
        			catch(ImpossiblePercentageException e) {showAlert("Impossible percentage value");}
        			catch (InvalidCharException e) {showAlert("Invalid character used (/, \\, :, *, ?, \", <, >, | or is empty)");}
        		}
        	});
		});
		//~...
		header.getChildren().addAll(back,consoleName,add);
		//...		
		
		//List
		ArrayList<Game> games=actualConsole.getList();
		for(int i=0; i<games.size(); i++){
			Game game=games.get(i);
			
			HBox itemBox=generateItemBox(game.toString(), actualConsole.getName()+"/"+game.getName()+".png", "Game.png");
			
			//PROGRESS
			String progress=(int)(game.getProgress()*100)+"%";
			itemBox.getChildren().add(new Label(progress));
			//...
			
			//MARKED
			if(game.isMarked()){
				Label marker=new Label(MARKED_SYMBOL);
				itemBox.getChildren().add(marker);
				marker.getStyleClass().add("marker");
			}
			//...
			
			//OnAction
			itemBox.setOnMouseClicked(event->{
				//Open
				if(event.getButton()==MouseButton.PRIMARY){
					this.actualGame=game;
					generate();
				}
				//...
				//Option Menu
				else if(event.getButton()==MouseButton.SECONDARY){
					generateItemMenu();
					
					//Delete
					MenuItem delete = new MenuItem("Delete");
			        delete.setOnAction(dEvent->{
			        	actualConsole.deleteGame(game.getName());
			        	generate();
			        });
					//...
			        //Name
			        MenuItem name = new MenuItem("Change Name");
			        name.setOnAction(eEvent->{
			        	Node nameNode=itemBox.getChildren().get(1);
			        	itemBox.getChildren().remove(1);
			        	
			        	//toTextField
			        	if(nameNode instanceof Label){
				        	TextField gameName=new TextField(game.toString());
				        	itemBox.getChildren().add(1,gameName);
				        	
				        	gameName.setOnKeyPressed(kEvent->{
				        		
				        		if(kEvent.getCode().equals(KeyCode.ENTER)){
				        			try {
										actualConsole.updateNameGame(game.getName(),gameName.getText());
										generate();
									}
				        			catch (ExistingElementException e) {showAlert("Game with this name all ready exist!");}
				        			catch (InvalidCharException e) {showAlert("Invalid character used (/, \\, :, *, ?, \", <, >, | or is empty)");}
				        		}
				        		
				        	});
			        	}
			        	//...
			        	//toLabel
			        	else if(nameNode instanceof TextField){
				        	itemBox.getChildren().add(1, new Label(game.toString()));
			        	}
			        	//...
			        });
			        //...
			        //Image
			        MenuItem image = new MenuItem("Change Image");
			        image.setOnAction(eEvent->{
			        	//Choose File
			        	Stage stage = (Stage) pane.getScene().getWindow();
				        FileChooser fileChooser = new FileChooser();
						fileChooser.setTitle("Image Selector");
						fileChooser.getExtensionFilters().add(new ExtensionFilter("PNG", "*.png"));
						File imageFile=fileChooser.showOpenDialog(stage);
						//...
						//CreateDir
						new File(ICONS_PATH+actualConsole.getName()).mkdir();
						//...
						//Delete
						File newImageFile=new File(ICONS_PATH+actualConsole.getName()+"/"+game.getName()+".png");
						newImageFile.delete();
						//...
						//Change
						imageFile.renameTo(newImageFile);
						generate();
						//...
						
			        });
			        //...
			        
			        itemMenu.getItems().addAll(delete, name, image);
			        itemMenu.show(itemBox, event.getScreenX(), event.getScreenY());
				}
				//...
				
			});
			//...
			
			list.getItems().add(itemBox);
			
		}
		//...
		
	}
	
	public void generateGame() {
		
		//HEADER
		//~Game Name
		Label gameName=new Label(actualGame.toString());
		gameName.getStyleClass().add("title");
		//...
		//~Add
//		HBox buttonBox=new HBox();
		Button add=new Button(ADD_SYMBOL);
		add.setOnMouseClicked(event->{
			
			TextField achievementName=onActionAddButton(add);
			achievementName.setOnKeyPressed(kEvent->{
				
				if(kEvent.getCode().equals(KeyCode.ENTER)){
        			try {actualGame.addAchievement(achievementName.getText(), false);generate();}
        			catch (ExistingElementException e) {showAlert("Achievement with this name all ready exist!");}
        		}
			});
		});
		//~...
		//...
		
		//INFO
		information=new VBox();
		information.setSpacing(10);
		pane.getChildren().add(0, information);
		
		//~Progress
		HBox progress=new HBox();
		progress.setSpacing(10);
		progress.setAlignment(Pos.CENTER_LEFT);
		information.getChildren().add(progress);
		//---
		progress.getChildren().add(new Label("Progress"));
		//---
		Slider progressSlider=new Slider(0, 1, actualGame.getProgress());
		progressSlider.setBlockIncrement(0.01);
		progressSlider.setMajorTickUnit(0.01);
		progressSlider.setMinorTickCount(0);
		progressSlider.setSnapToTicks(true);
		progress.getChildren().add(progressSlider);
		//---
		String numberP=(int)(actualGame.getProgress()*100)+"%";
		Label progressLabel=new Label(numberP);
		progress.getChildren().add(progressLabel);
		//---
		progressSlider.valueProperty().addListener(event->{
			progressLabel.textProperty().setValue(
					String.valueOf((int)(progressSlider.valueProperty().getValue()*100)+"%")
			);
		});
		//~...
		
		//~Extra Progress
		HBox extraProgress=new HBox();
		extraProgress.setSpacing(10);
		extraProgress.setAlignment(Pos.CENTER_LEFT);
		information.getChildren().add(extraProgress);
		//---
		extraProgress.getChildren().add(new Label("Extra Progress"));
		//---
		Slider extraProgressSlider=new Slider(0, 1, actualGame.getExtraProgress());
		extraProgressSlider.setBlockIncrement(0.01);
		extraProgressSlider.setMajorTickUnit(0.01);
		extraProgressSlider.setMinorTickCount(0);
		extraProgressSlider.setSnapToTicks(true);
		extraProgress.getChildren().add(extraProgressSlider);
		//---
		String numberEP=(int)(actualGame.getExtraProgress()*100)+"%";
		Label extraProgressLabel=new Label(numberEP);
		extraProgress.getChildren().add(extraProgressLabel);
		//---
		extraProgressSlider.valueProperty().addListener(event->{
			extraProgressLabel.textProperty().setValue(
					String.valueOf((int)(extraProgressSlider.valueProperty().getValue()*100)+"%")
			);
		});
		//~...
		
		//~Marked
		HBox marked=new HBox();
		marked.setSpacing(10);
		marked.setAlignment(Pos.CENTER_LEFT);
		information.getChildren().add(marked);
		//---
		marked.getChildren().add(new Label("Marked"));
		CheckBox markedButton=new CheckBox();
		markedButton.setSelected(actualGame.isMarked());
		marked.getChildren().add(markedButton);
		//~...
		
		//~Annotations
		VBox annotations=new VBox();
		annotations.setSpacing(10);
		marked.setAlignment(Pos.CENTER_LEFT);
		information.getChildren().add(annotations);
		//---
		annotations.getChildren().add(new Label("Annotations"));
		TextArea annotationsTextBox=new TextArea(actualGame.getAnnotations());
		annotations.getChildren().add(annotationsTextBox);
		//~...
		
		//~Back
		Button back=new Button(BACK_SYMBOL);
		back.setOnMouseClicked(event->{
			
			//Save
			try {actualGame.updateGame(progressSlider.valueProperty().getValue(), extraProgressSlider.valueProperty().getValue(), annotationsTextBox.getText(), markedButton.isSelected());}
			catch (ImpossiblePercentageException e) {showAlert("Impossible percentage value");}
			//...
			this.actualGame=null;
			generate();
		});
		header.getChildren().addAll(back,gameName,add);
		//~...
		//~Save
//		Button save=new Button(SAVE_SYMBOL);
//		save.setOnMouseClicked(event->{
//			
//			try {actualGame.updateGame(progressSlider.valueProperty().getValue(), extraProgressSlider.valueProperty().getValue(), annotationsTextBox.getText(), markedButton.isSelected()); showAlert("Game has been saved");}
//			catch (ImpossiblePercentageException e) {showAlert("Impossible percentage value");}
//		});
//		buttonBox.getChildren().addAll(add,save);
		//~...
		//...
		
		//LIST
		ArrayList<Achievement> achievements=actualGame.getList();
		for(int i=0; i<achievements.size(); i++){
			Achievement achievement=achievements.get(i);
			
			HBox itemBox=generateItemBox(achievement.toString(), actualConsole.getName()+"/"+actualGame.getName()+"/"+achievement.getName()+".png", "Achievement.png");
			
			//IMAGE COLOR
			ImageView icon=(ImageView) itemBox.getChildren().get(0);
			changeImageOpasity(icon, achievement.isCompleted());
			//...
			
			
			//COMPLETED
			CheckBox completed=new CheckBox();
			completed.setSelected(achievement.isCompleted());
			itemBox.getChildren().add(completed);
			
			completed.setOnMouseClicked(event->{
				if(event.getButton()==MouseButton.PRIMARY){
					achievement.setCompleted(completed.isSelected());
					changeImageOpasity(icon, achievement.isCompleted());
				}
			});
			//...
			
			//OnAction
			itemBox.setOnMouseClicked(event->{
				if(event.getButton()==MouseButton.SECONDARY){
					generateItemMenu();
					
					//Delete
					MenuItem delete = new MenuItem("Delete");
			        delete.setOnAction(dEvent->{
			        	actualGame.deleteAchievement(achievement.getName());
			        	generate();
			        });
					//...
			        //Name
			        MenuItem name = new MenuItem("Change Name");
			        name.setOnAction(eEvent->{
			        	Node nameNode=itemBox.getChildren().get(1);
			        	itemBox.getChildren().remove(1);
			        	
			        	//toTextField
			        	if(nameNode instanceof Label){
			        		TextField achievementName=new TextField(achievement.toString());
			        		itemBox.getChildren().add(1,achievementName);
			        		
			        		achievementName.setOnKeyPressed(kEvent->{
			        			
			        			if(kEvent.getCode().equals(KeyCode.ENTER)){
			        				try {
										actualGame.updateNameAchievement(achievement.getName(),achievementName.getText());
										generate();
									}
			        				catch (ExistingElementException e) {
			        					showAlert("Achievement with this name all ready exist!");
			        				}
			        			}
			        			
			        		});
			        	}
			        	//...
			        	//toLabel
			        	else if(nameNode instanceof TextField) {
			        		itemBox.getChildren().add(1, new Label(achievement.toString()));
			        	}
			        	//...
			        });
			        //...
			        //Image
			        MenuItem image = new MenuItem("Change Image");
			        image.setOnAction(eEvent->{
			        	//Choose File
			        	Stage stage = (Stage) pane.getScene().getWindow();
				        FileChooser fileChooser = new FileChooser();
						fileChooser.setTitle("Image Selector");
						fileChooser.getExtensionFilters().add(new ExtensionFilter("PNG", "*.png"));
						File imageFile=fileChooser.showOpenDialog(stage);
						//...
						//CreateDir
						new File(ICONS_PATH+actualConsole.getName()).mkdir();
						new File(ICONS_PATH+actualConsole.getName()+"/"+actualGame.getName()).mkdir();
						//...
						//Delete
						File newImageFile=new File(ICONS_PATH+actualConsole.getName()+"/"+actualGame.getName()+"/"+achievement.getName()+".png");
						newImageFile.delete();
						//...
						//Change
						if(imageFile!=null){
							imageFile.renameTo(newImageFile);
						}
						generate();
						//...
						
			        });
			        //...
			        itemMenu.getItems().addAll(delete, name, image);
			        itemMenu.show(itemBox, event.getScreenX(), event.getScreenY());
				}
			});
			//...
			
			list.getItems().add(itemBox);
			
		}
		//...
		
	}
	
	//Supporters
	public void saveData(Stage stage){
		stage.setOnCloseRequest(event -> {
			manager.saveApp();
		});
	}
	
	public TextField onActionAddButton(Button add){
		add.setText(MINIMIZE_SYMBOL);
		add.setOnMouseClicked(mEvent->{
			generate();
		});
		
		HBox itemBox=new HBox();
		TextField itemName=new TextField();
		itemBox.getChildren().add(itemName);
		list.getItems().add(itemBox);
		
		return itemName;
	}
	
	public void showAlert(String message){
		ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
		Alert alert = new Alert(AlertType.NONE, message, ok);
		alert.setHeaderText(null);
		alert.setTitle(null);
		
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("/view/Style.css").toExternalForm());
		Stage stage = (Stage) dialogPane.getScene().getWindow();
		stage.getIcons().add(new Image("file:../../med/icon/Logo.png"));
		
		alert.showAndWait();
	}
	
	public void changeImageOpasity(ImageView image, boolean value){
		if(value){
			ColorAdjust colorAdjust = new ColorAdjust();
			colorAdjust.setBrightness(0);
			image.setEffect(colorAdjust);
		}
		else{
			ColorAdjust colorAdjust = new ColorAdjust();
			colorAdjust.setBrightness(-0.75);
			image.setEffect(colorAdjust);
		}
	}
	
	public HBox generateItemBox(String itemName, String imgPath,String defaultImgPath){
		HBox itemBox=new HBox();
		itemBox.setSpacing(10);
		itemBox.setAlignment(Pos.CENTER_LEFT);
		itemBox.getStyleClass().add("item-box");
		
		//Image
		File img=new File(ICONS_PATH+imgPath);
		if(!img.exists()){
			img=new File(DEFAULT_ICONS_PATH+defaultImgPath);
		}
		try {
			String imgUrl=img.toURI().toURL().toString();
			itemBox.getChildren().add(new ImageView(new Image(imgUrl, 60, 60, false, true)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//...
		
		//NAME
		itemBox.getChildren().add(new Label(itemName));
		//...
		
		return itemBox;
	}
	
	public void generateItemMenu(){
		if(itemMenu!=null){
			itemMenu.hide();
		}
		itemMenu = new ContextMenu();
	}
	
}
