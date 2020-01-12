package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import exception.ExistingElementException;
import exception.ImpossiblePercentageException;
import exception.InvalidCharException;

public class Console implements Comparable<Console>, Listable<Game>{
	
	//Constant
	public static final String[] CONSOLES= {"Pc","Nintendo Switch","PlayStation4","Xbox One"};
	
	//Attribute
	private String name;
	private ArrayList<Game> games;
	
	//Constructor
	public Console(String name) throws InvalidCharException {
		System.out.println(name);
		
		if(InvalidCharException.validateString(name)){this.name=name;}
		else{throw new InvalidCharException();}
		
		this.games=new ArrayList<Game>();
		
	}
	
	//Method
	public void addGame(String name, double progress, double extraProgress, String annotations, boolean marked) throws ExistingElementException, ImpossiblePercentageException, InvalidCharException{
		if(searchGame(name)==null){
			games.add(new Game(name, progress, extraProgress, annotations, marked));
		}
		else{
			throw new ExistingElementException();
		}
	}
	
	public void addGame(Game game) throws ExistingElementException {
		if(searchGame(game.getName())==null){
			games.add(game);
		}
		else{
			throw new ExistingElementException();
		}
	}
	
	public void deleteGame(String name) {
		boolean found=false;
		
		for(int i=0; (i<games.size()) && (!found); i++){
			if(name.equals(games.get(i).getName())){
				games.remove(i);
				found=true;
			}
		}
	}
	
	public void updateNameGame(String actualName, String newName) throws ExistingElementException, InvalidCharException{
		Game game=searchGame(actualName);
		
		if(game!=null) {
			if(searchGame(newName)==null || actualName.equals(newName)){
				game.setName(newName);
			}
			else{
				throw new ExistingElementException();
			}
		}
		
	}
	
	public Game searchGame(String name) {
		Game game=null;
		
		boolean found=false;
		for(int i=0; (i<games.size()) && (!found); i++){
			if(name.equalsIgnoreCase(games.get(i).getName())){
				game=games.get(i);
				found=true;
			}
		}
		
		return game;
	}
	
	public double calculateProgress(){
		double average=0;
		
		if(games.size()!=0){
			for(int i=0; i<games.size(); i++) {
				average+=games.get(i).getProgress();
			}
			average/=games.size();
		}
		else{
			average=1;
		}
		
		return average;
	}
	
	//Save
	public void saveConsole() throws FileNotFoundException {
		String path=Manager.CONSOLES_PATH+name+"/";
		File folder=new File(path);
		
		folder.mkdir();
		for(int i=0; i<games.size(); i++){
			games.get(i).saveGame(path);
		}
		
	}
	
	//Load
	public void loadGames(String folderPath) throws IOException, ImpossiblePercentageException, ExistingElementException, InvalidCharException{//[FILE]
		boolean possible=true;
		
		File folder = new File(folderPath);
		for (final File fileEntry : folder.listFiles()) {
			
			String[] data=read(folderPath+fileEntry.getName()).split("\n");
			
			if(data.length>=4) {
				
				String name=fileEntry.getName().split(Game.GAME_EXTENSION)[0];
				double progress=Double.parseDouble(data[0])/100;
				double extraProgress=Double.parseDouble(data[1])/100;
				String annotations;
				if(data[2].equals("[NO-DATA]")){annotations="";}
				else{annotations=data[2];}
				boolean marked=Boolean.parseBoolean(data[3]);
				
				Game game=new Game(name, progress, extraProgress, annotations, marked);
				addGame(game);
				
				for(int i=4; i<data.length; i++){
					String[] achievementData=data[i].split("	");
					if(achievementData.length==2){
						String achivementName=achievementData[0];
						boolean achivementCompleted=Boolean.parseBoolean(achievementData[1]);
						game.addAchievement(achivementName, achivementCompleted);
					}
				}
				
			}
			
	    }
		
	}
	
	//Read
	private String read(String path) throws IOException{//[FILE]
		String text="";
		
		File file=new File(path);
		if(file.exists()){
			file.createNewFile();
			FileReader fileReader=new FileReader(file);
			BufferedReader reader=new BufferedReader(fileReader);
			String actualLine;
			while((actualLine=reader.readLine())!=null){
				if( !((actualLine.isEmpty()) || ((actualLine.charAt(0)=='~')&&(actualLine.charAt(1)=='~'))) ){
					text+=actualLine+"\n";
				}
			}
			reader.close();
		}
		else{
			text=null;
		}
		
		return text;
	}
	
	//Compare
	public int compareTo(Console console) {
		return name.compareTo(console.name);
	}
	
	//Set
	public void setName(String name) throws InvalidCharException {
		if(InvalidCharException.validateString(name)){this.name=name;}
		else{throw new InvalidCharException();}
	}
	
	//Get
	public ArrayList<Game> getList(){
		return games;
	}
	
	public String toString(){
		return name;
	}
	
	public String getName() {
		return name;
	}
	
}
