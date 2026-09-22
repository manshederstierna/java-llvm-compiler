package cowlang.lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer{
    private final String source;
	private final List<Token> tokens = new ArrayList<>();
	
	
	private int start = 0;
	private int current = 0;
	
	private int line = 1;
	private int column = 1;
	private int startColumn = 1;
	
	public Lexer(String source){
	    this.source = source;	
	}
	
	
	public List<Token> tokenize(){
		
	    while(!isAtEnd()){
		    start = current;
            startColumn = column;

			scanToken();
		}
		
		tokens.add(new Token(TokenType.EOF, "",line,column));
		return tokens;
	}
	
	
	private void scanToken(){
		
	    char c = advance();
		
		switch(c){
			case '+':
			    addToken(TokenType.PLUS);
				break;
				
			case '-':
				addToken(TokenType.MINUS);
				break;
				
			case '*':
				addToken(TokenType.STAR);
				break;
				
			case '/':
				addToken(TokenType.SLASH);
				break;
				
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;

            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;

            case '{':
                addToken(TokenType.LEFT_BRACE);
                break;

            case '}':
                addToken(TokenType.RIGHT_BRACE);
                break;

            case ',':
                addToken(TokenType.COMMA);
                break;

            case ';':
                addToken(TokenType.SEMICOLON);
                break;	
				
			case ' ':
				break;
			
			case '\t':
				break;
				
			case '\r':
				break;
				
				
			case '\n':
				line++;
				column = 1;
				break;
				
			default:
				throw new RuntimeException(
					"Unexpected character '" + c +
					"' at " + line + ":" + startColumn
				);
		}

		 
	}
	
	
	private char advance(){
		char c = source.charAt(current);
		current++;
		column++;
		
		return c;
	}
	
	
	private boolean isAtEnd(){
		return current >= source.length();
	}
	
	private void addToken(TokenType type){
		String lexeme = source.substring(start,current);
		
		tokens.add(
			new Token(type, lexeme, line, startColumn)
		);
	}
	
}