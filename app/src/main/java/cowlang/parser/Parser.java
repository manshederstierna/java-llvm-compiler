package cowlang.parser

import cowlang.ast.AssignmentStmt;
import cowlang.ast.Expr;
import cowlang.ast.IntegerLiteral;
import cowlang.ast.Stmt;
import cowlang.lexer.Token;
import cowlang.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;


public class Parser{
	private final List<Token> tokens;
	private int current = 0;
	
	public Parser(List<Token> tokens){
		this.tokens = tokens;
	}
	
	
	public List<Stmt> parse(){
		List<Stmt> statements = new ArrayList<>();
		
		while(!isAtEnd()){
			statements.add(parseStatements());
		}
		
		return statements;
	}
	
	
	private Stmt parseStatements(){
		return parseAssignment();
	}
	
	private Stmt parseAssignment(){
		Token name = consume(
			TokenType.IDENTIFIER,
			"Expected variable name."
		);
		
		consume(
			TokenType.ASSIGN,
			"Expected '<-' after variable name."
		);
		
		Expr value = parseExpression();
		
		consume(
			TokenType.SEMICOLON,
			"Expected ';' after assignment."
		);
		
		return new AssignmentStmt(name.lexeme(), value);
	}
	
	
	private Expr parseExpression(){
		Token token = consume(
			TokenType.INTEGER,
			"Expected integer expression."
		);
		
		int value = Integer.parseInt(token.lexeme());
		
		return new IntegerLiteral(value);
		
	}
	
	private Token consume(TokenType type, String message){
		if(check(type)){
			return advance();
		}
		
		throw error(peek(),message);
	}
	
	private boolean check(TokenType type){
		if(isAtEnd()){
			return type == TokenType.EOF;
		}
		
		return peek().type() == type;
	}
	
	private Token advance(){
		if(!isAtEnd()){
			current++;
		}
		return previous();
	}
	
	private boolean isAtEnd(){
		return peek().type() == TokenType.EOF;
	}
	
	private Token peek(){
		return tokens.get(current);
	}
	
	private Token previous(){
		return tokens.get(current - 1);
	}
	
	private RuntimeException error(Token token, String message){
		return new RuntimeException(
			"Parser error at " 
				+ token.line()
				+ ":"
				+ token.column()
				+ ": "
				+ message
		);
	}
}