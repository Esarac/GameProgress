package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import exception.ExistingElementException;
import exception.ImpossiblePercentageException;
import exception.InvalidCharException;

public class Game implements Listable<Achievement>{

	//Constant
	public final static String GAME_EXTENSION=".gme";
	
	//Attribute
	private String name;
	private double progress;
	private double extraProgress;
	private String annotations;
	public boolean marked;
	private ArrayList<Achievement> achievements;
	
	//Constructor
	public Game(String name, double progress, double extraProgress, String annotations, boolean marked) throws ImpossiblePercentageException, InvalidCharException{
		System.out.println("	"+name+" "+progress+" "+extraProgress+" "+annotations+" "+marked);
		if((0<=progress)&&(progress<=1)) {this.progress=progress;}
		else {throw new ImpossiblePercentageException();}
		
		if((0<=extraProgress)&&(extraProgress<=1)) {this.extraProgress=extraProgress;}
		else {throw new ImpossiblePercentageException();}
		
		if(InvalidCharException.validateString(name)){this.name=name;}
		else{throw new InvalidCharException();}
		
		this.annotations=annotations;
		this.marked=marked;
		this.achievements=new ArrayList<Achievement>();
		
	}
	
	//Method
	public void addAchievement(String name, boolean completed) throws ExistingElementException{
		if(searchAchievement(name)==null){
			achievements.add(new Achievement(name, completed));
		}
		else{
			throw new ExistingElementException();
		}
	}
	
	public void deleteAchievement(String name) {
		boolean found=false;
		
		for(int i=0; (i<achievements.size()) && (!found); i++){
			if(name.equals(achievements.get(i).getName())){
				achievements.remove(i);
				found=true;
			}
		}
	}
	
	public void updateNameAchievement(String actualName, String newName) throws ExistingElementException{
		Achievement achievement=searchAchievement(actualName);
		
		if(achievement!=null) {
			if(searchAchievement(newName)==null || actualName.equals(newName)){
				achievement.setName(newName);
			}
			else{
				throw new ExistingElementException();
			}
		}
	}
	
	public Achievement searchAchievement(String name) {
		Achievement achievement=null;
		
		boolean found=false;
		for(int i=0; (i<achievements.size()) && (!found); i++){
			if(name.equals(achievements.get(i).getName())){
				achievement=achievements.get(i);
				found=true;
			}
		}
		
		return achievement;
	}
	
	//Save
	public void saveGame(String consolePath) throws FileNotFoundException {//[FILE]
		String text="\n~~Progress\n"+(progress*100);
		text+="\n~~ExtraProgress\n"+(extraProgress*100);
		if(annotations.isEmpty()){text+="\n~~Annotations\n[NO-DATA]";}
		else{text+="\n~~Annotations\n"+annotations;}
		
		text+="\n~~Marked\n"+marked;
		
		text+="\n~~Achievements (Name | Completed)\n";
		for(int i=0; i<achievements.size(); i++){
			text+="\n"+achievements.get(i).getName()+"	"+achievements.get(i).isCompleted();
		}
		
		File file=new File(consolePath+name+GAME_EXTENSION);
		PrintWriter writer=new PrintWriter(file);
		writer.append(text);
		writer.close();
	}
	
	//Set
	public void updateGame(double progress, double extraProgress, String annotations, boolean marked) throws ImpossiblePercentageException {
		
		if((0<=progress)&&(progress<=1)) {this.progress=progress;}
		else {throw new ImpossiblePercentageException();}
		
		if((0<=extraProgress)&&(extraProgress<=1)) {this.extraProgress=extraProgress;}
		else {throw new ImpossiblePercentageException();}
		
		this.annotations=annotations;
		this.marked=marked;
		
	}
	
	public void setName(String name) throws InvalidCharException {
		if(InvalidCharException.validateString(name)){this.name=name;}
		else{throw new InvalidCharException();}
	}
	
	public void setMarked(boolean marked) {
		this.marked=marked;
	}
	
	//Get
	public ArrayList<Achievement> getList(){
		return achievements;
	}
	
	public String getName() {
		return name;
	}
	
	public double getProgress() {
		return progress;
	}
	
	public double getExtraProgress() {
		return extraProgress;
	}
	
	public String getAnnotations() {
		return annotations;
	}
	
	public boolean isMarked() {
		return marked;
	}
	
	public String toString() {
		return name;
	}
	
}
