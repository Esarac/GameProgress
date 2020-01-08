package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import exception.ExistingElementException;
import exception.ImpossiblePercentageException;

public class Console implements Comparable<Console>, Listable<Game>{
	
	//Constant
	public static final String[] CONSOLES= {"Pc","Nintendo Switch","PlayStation4","Xbox One"};
	
	//Attribute
	private String name;
	private ArrayList<Game> games;
	
	//Constructor
	public Console(String name) {
		System.out.println(name);
		this.name=name;
		this.games=new ArrayList<Game>();
		
	}
	
	//Method
	public void addGame(String name, double progress, double extraProgress, String annotations, boolean marked) throws ExistingElementException, ImpossiblePercentageException{
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
	
	public Game searchGame(String name) {
		Game game=null;
		
		boolean found=false;
		for(int i=0; (i<games.size()) && (!found); i++){
			if(name.equals(games.get(i).getName())){
				game=games.get(i);
				found=true;
			}
		}
		
		return game;
	}
	
	public double calculateProgress(){
		double average=0;
		
		for(int i=0; i<games.size(); i++) {
			average+=games.get(i).getProgress();
		}
		average/=games.size();
		
		return average;
	}
	
	//Save
	public void saveConsole() {
		
	}
	
	//Load
	public void loadGames(String folderPath) throws IOException, ImpossiblePercentageException, ExistingElementException{
		boolean possible=true;
		
		File folder = new File(folderPath);
		for (final File fileEntry : folder.listFiles()) {
			
			String[] data=readClubs(folderPath+fileEntry.getName()).split("\n");
			
			if(data.length>=5) {
				
				String name=data[0];
				double progress=Double.parseDouble(data[1])/100;
				double extraProgress=Double.parseDouble(data[2])/100;
				String annotations=data[3];
				boolean marked=Boolean.parseBoolean(data[4]);
				
				Game game=new Game(name, progress, extraProgress, annotations, marked);
				addGame(game);
				
				for(int i=5; i<data.length; i++){
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
	private String readClubs(String path) throws IOException{//[File]
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
	public void setName(String name) {
		this.name=name;
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