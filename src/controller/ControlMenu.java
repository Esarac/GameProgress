package controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import exception.ExistingElementException;
import exception.ImpossiblePercentageException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Achievement;
import model.Console;
import model.Game;
import model.Manager;

public class ControlMenu implements Initializable{
	
	//Constants
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			manager=new Manager();
		} catch (ExistingElementException | IOException | ImpossiblePercentageException e) {
			e.printStackTrace();//BAD
		}
	}
	
	public void generate() {
		
		list.getItems().clear();
		header.getChildren().clear();
		pane.getChildren().remove(information);
		
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
		header.getChildren().add(new Label("GameProgress"));
		
		header.setAlignment(Pos.CENTER);
		//...
		
		//LIST
		ArrayList<Console> consoles=manager.getList();
		for(int i=0; i<consoles.size(); i++){
			Console console=consoles.get(i);
			
			HBox itemBox=new HBox();
			itemBox.setSpacing(10);
			
			//IMAGE
			File img=new File(ICONS_PATH+console.getName()+".png");
			if(!img.exists()){
				img=new File(DEFAULT_ICONS_PATH+"Console.png");
			}
			try {
				String imgUrl=img.toURI().toURL().toString();
				itemBox.getChildren().add(new ImageView(new Image(imgUrl, 60, 60, false, true)));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			//...
			
			//NAME
			itemBox.getChildren().add(new Label(console.toString()));
			//...
			
			//PROGRESS
			String progress=(console.calculateProgress()*100)+"%";
			itemBox.getChildren().add(new Label(progress));
			//...
			
			//OnAction
			itemBox.setOnMouseClicked(event->{
				if(event.getButton()==MouseButton.PRIMARY){
					this.actualConsole=console;
					generate();
				}
				else if(event.getButton()==MouseButton.SECONDARY){
					ContextMenu consoleMenu = new ContextMenu();
					
					//Delete
					MenuItem delete = new MenuItem("Delete Console");
			        delete.setOnAction(dEvent->{
			        	manager.deleteConsole(console.getName());
			        	generate();
			        });
					//...
			        
					//Edit
					MenuItem edit = new MenuItem("Change name");
			        edit.setOnAction(eEvent->{
			        	TextInputDialog td = new TextInputDialog(); 
				        td.setHeaderText("Set the new name:");
				        
				        Optional<String> option=td.showAndWait();
				        if (option.isPresent()) {
				        	try {
								manager.editNameConsole(console.getName(),td.getEditor().getText());
								generate();
							}
				        	catch (ExistingElementException e) {
								ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
								Alert alert = new Alert(AlertType.NONE, "Console with this name all ready exist!", ok);
								alert.setHeaderText(null);
								alert.setTitle(null);
								alert.showAndWait();
							}
				        }
			        	
			        });
					//...
			        
			        consoleMenu.getItems().addAll(delete, edit);
			        consoleMenu.show(itemBox, event.getScreenX(), event.getScreenY());
				}
			});
			//...
			
			list.getItems().add(itemBox);
			
		}
		//...
		
	}
	
	public void generateConsole() {
		
		//HEADER
		Button back=new Button("<--");
		header.getChildren().add(back);
		back.setOnMouseClicked(event->{
			this.actualConsole=null;
			generate();
		});
		
		header.getChildren().add(new Label(actualConsole.toString()));
		
		header.setSpacing(300);
		header.setAlignment(Pos.CENTER_LEFT);
		//...		
		
		//List
		ArrayList<Game> games=actualConsole.getList();
		for(int i=0; i<games.size(); i++){
			Game game=games.get(i);
			
			HBox itemBox=new HBox();
			itemBox.setSpacing(10);
			
			//IMAGE
			File img=new File(ICONS_PATH+actualConsole.getName()+"/"+game.getName()+".png");
			if(!img.exists()){
				img=new File(DEFAULT_ICONS_PATH+"Game.png");
			}
			try {
				String imgUrl=img.toURI().toURL().toString();
				itemBox.getChildren().add(new ImageView(new Image(imgUrl, 60, 60, false, true)));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			//...
			
			//NAME
			itemBox.getChildren().add(new Label(game.toString()));
			//...
			
			//PROGRESS
			String progress=(game.getProgress()*100)+"%";
			itemBox.getChildren().add(new Label(progress));
			//...
			
			//OnAction
			itemBox.setOnMouseClicked(event->{
				this.actualGame=game;
				generate();
			});
			//...
			
			list.getItems().add(itemBox);
			
		}
		//...
		
	}
	
	public void generateGame() {
		
		//HEADER
		Button back=new Button("<--");
		header.getChildren().add(back);
		back.setOnMouseClicked(event->{
			this.actualGame=null;
			generate();
		});
		
		header.getChildren().add(new Label(actualGame.toString()));
		
		header.setSpacing(300);
		header.setAlignment(Pos.CENTER_LEFT);
		//...
		
		//INFO
		information=new VBox();
		information.setSpacing(10);
		pane.getChildren().add(0, information);
		
		HBox progress=new HBox();
		progress.setSpacing(10);
		information.getChildren().add(progress);
		//---
		progress.getChildren().add(new Label("progress"));
		Slider progressSlider=new Slider(0, 1, actualGame.getProgress());
		progress.getChildren().add(progressSlider);
		String numberP=(actualGame.getProgress()*100)+"%";
		progress.getChildren().add(new Label(numberP));
		
		HBox extraProgress=new HBox();
		extraProgress.setSpacing(10);
		information.getChildren().add(extraProgress);
		//---
		extraProgress.getChildren().add(new Label("extra progress"));
		Slider extraProgressSlider=new Slider(0, 1, actualGame.getExtraProgress());
		extraProgress.getChildren().add(extraProgressSlider);
		String numberEP=(actualGame.getExtraProgress()*100)+"%";
		extraProgress.getChildren().add(new Label(numberEP));
		
		HBox marked=new HBox();
		marked.setSpacing(10);
		information.getChildren().add(marked);
		//---
		marked.getChildren().add(new Label("marked"));
		Button markedButton=new Button("["+actualGame.isMarked()+"]");
		marked.getChildren().add(markedButton);
		
		VBox annotations=new VBox();
		annotations.setSpacing(10);
		information.getChildren().add(annotations);
		//---
		annotations.getChildren().add(new Label("annotations"));
		TextArea annotationsTextBox=new TextArea(actualGame.getAnnotations());
		annotations.getChildren().add(annotationsTextBox);
		//...
		
		//LIST
		ArrayList<Achievement> achievements=actualGame.getList();
		for(int i=0; i<achievements.size(); i++){
			Achievement achievement=achievements.get(i);
			
			HBox itemBox=new HBox();
			itemBox.setSpacing(10);
			
			//IMAGE
			File img=new File(ICONS_PATH+actualConsole.getName()+"/"+actualGame.getName()+"/"+achievement.getName()+".png");
			if(!img.exists()){
				img=new File(DEFAULT_ICONS_PATH+"Achievement.png");
			}
			try {
				String imgUrl=img.toURI().toURL().toString();
				itemBox.getChildren().add(new ImageView(new Image(imgUrl, 60, 60, false, true)));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			//...
			
			//NAME
			itemBox.getChildren().add(new Label(achievement.toString()));
			//...
			
			//COMPLETED
			Button completed=new Button("["+achievement.isCompleted()+"]");
			itemBox.getChildren().add(completed);
			
			completed.setOnMouseClicked(event->{
				achievement.setCompleted(!achievement.isCompleted());
				completed.setText("["+achievement.isCompleted()+"]");
			});
			//...
			
			list.getItems().add(itemBox);
			
		}
		//...
		
	}
	
}
