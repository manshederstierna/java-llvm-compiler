package cowlang.parser;

import cowlang.ast.AssignmentStmt;
import cowlang.ast.Expr;
import cowlang.ast.IntegerLiteral;
import cowlang.ast.Stmt;
import cowlang.lexer.Token;
import cowlang.lexer.TokenType;
import cowlang.ast.BinaryExpr;
import cowlang.ast.BinaryOperator;
import cowlang.ast.VariableExpr;

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
		return parseAddition();
	}
	
	private Expr parseAddition(){
		Expr expression = parseMultiplication();
		
		while(match(TokenType.PLUS, TokenType.MINUS)){
			Token operator = previous();
			Expr right = parseMultiplication();
			
			BinaryOperator binaryOperator;
			
			if(operator.type() == TokenType.PLUS){
				binaryOperator = BinaryOperator.ADD;
			} else{
				binaryOperator = BinaryOperator.SUBTRACT;
			}
			
			expression = new BinaryExpr(expression, binaryOperator, right);
		}
		
		return expression;
	}
	
	private Expr parseMultiplication(){
		Expr expression = parsePrimary();
		
		while(match(TokenType.STAR, TokenType.SLASH)){
			Token operator = previous();
			Expr right = parsePrimary();
			
			BinaryOperator binaryOperator;
			
			if(operator.type() == TokenType.STAR){
				binaryOperator = BinaryOperator.MULTIPLY;
			} else{
				binaryOperator = BinaryOperator.DIVIDE;
			}
			
			expression = new BinaryExpr(expression, binaryOperator, right);
		}
		
		return expression;
	}
	
	private Expr parsePrimary(){
		if(match(TokenType.INTEGER)){
			int value = Integer.parseInt(previous().lexeme());
			
			return new IntegerLiteral(value);
		}
		
		if(match(TokenType.IDENTIFIER)){
			return new VariableExpr(previous().lexeme());
		}
		
		if(match(TokenType.LEFT_PAREN)){
			Expr expression = parseExpression();
			
			consume(TokenType.RIGHT_PAREN, "Expected ')' after expression");
			return expression;
		}
		
		throw error(peek(),"Expected expression.");
	}
	
	
	private boolean match(TokenType... types){
		for(TokenType type : types){
			if(check(type)){
				advance();
				return true;
			}
		}
		
		return false;
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