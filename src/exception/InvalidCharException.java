package exception;

public class InvalidCharException extends Exception{

	//Constructor
	public InvalidCharException() {
		super("The current string haves a invalid character (/, \\, *, ?, \", <, >, | or is empty).");
	}
	
	public InvalidCharException(String message) {
		super(message);
	}
	
	public static boolean validateString(String string){
		boolean valid=true;
		
		if((string!=null) && (!string.isEmpty())) {
			for(int i=0; (i<string.length()) && (valid); i++){
				char actualChar=string.charAt(i);
				if( (actualChar=='/') || (actualChar=='\\') || (actualChar==':') || (actualChar=='*')  || (actualChar=='?') || (actualChar=='"') || (actualChar=='<') || (actualChar=='>') || (actualChar=='|') ){
					valid=false;
				}
			}
		}
		else{
			valid=false;
		}
		
		return valid;
	}
	
}
