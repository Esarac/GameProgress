package exception;

public class ImpossiblePercentageException extends Exception{
	
	//Constructor
	public ImpossiblePercentageException() {
		super("The current percentage is out of range (0<=P<=1).");
	}
	
	public ImpossiblePercentageException(String message) {
		super(message);
	}
	
}
