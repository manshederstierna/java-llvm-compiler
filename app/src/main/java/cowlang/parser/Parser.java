package cowlang.parser;


import cowlang.ast.IntegerLiteral;
import cowlang.ast.StringLiteral;
import cowlang.lexer.Token;
import cowlang.lexer.TokenType;

import cowlang.ast.Expr;
import cowlang.ast.BinaryExpr;
import cowlang.ast.BinaryOperator;
import cowlang.ast.VariableExpr;

import cowlang.ast.AssignmentStmt;
import cowlang.ast.Stmt;
import cowlang.ast.YellStmt;
import cowlang.ast.WhisperStmt;
import cowlang.ast.WhenStmt;

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
			statements.add(parseStatement());
		}
		
		return statements;
	}
	
	
	private Stmt parseStatement(){
		if(match(TokenType.YELL)){
			return parseYellStatement();
		} 
		if(match(TokenType.WHISPER)){
			return parseWhisperStatement();
		} 
		
		if(match(TokenType.WHEN)){
			return parseWhenStatement();
		}
		
		return parseAssignment();
	}
	
	private Stmt parseWhenStatement(){
		consume(TokenType.LEFT_PAREN, "Expected '(' after 'when'.");
		
		Expr condition = parseExpression();
		
		consume(TokenType.RIGHT_PAREN, "Expected ')' after when condition.");
		
		List<Stmt> thenBranch = parseBlock();
		List<Stmt> otherwiseBranch = List.of();
		
		if(match(TokenType.OTHERWISE)){
			otherwiseBranch = parseBlock();
		}
		
		return new WhenStmt(condition, thenBranch, otherwiseBranch);
		
	}
	
	private List<Stmt> parseBlock(){
		consume(TokenType.LEFT_BRACE, "Expected '{' to start block.");
		
		List<Stmt> statements = new ArrayList<>();
		
		while(!check(TokenType.SEMICOLON) && !isAtEnd()){
			statements.add(parseStatement());
		}
		
		consume(TokenType.SEMICOLON,"Expected ';' before '}' at end of block.");
		 
		consume(TokenType.RIGHT_BRACE, "Expected '}' to end block.");
		
		return statements;

	}
	
	private Stmt parseYellStatement(){
		Expr value = parseExpression();
		
		consume(TokenType.SEMICOLON, "Expected ';' after yell statement.");
		
		return new YellStmt(value);
	}
	
	private Stmt parseWhisperStatement(){
		Expr value = parseExpression();
		
		consume(TokenType.SEMICOLON, "Expected ';' after whisper statement.");
		
		return new WhisperStmt(value);
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
		return parseComparison();
	}
	
	private Expr parseComparison(){
		Expr expression = parseAddition();
		
		while (match(
            TokenType.EQUAL_EQUAL,
            TokenType.NOT_EQUAL,
            TokenType.LESS,
            TokenType.LESS_EQUAL,
            TokenType.GREATER,
            TokenType.GREATER_EQUAL
		)) {
			Token operator = previous();
			Expr right = parseAddition();
			
			BinaryOperator binaryOperator = switch (operator.type()){
				case EQUAL_EQUAL -> BinaryOperator.EQUAL;
				case NOT_EQUAL -> BinaryOperator.NOT_EQUAL;
				case LESS -> BinaryOperator.LESS;
				case LESS_EQUAL -> BinaryOperator.LESS_EQUAL;
				case GREATER -> BinaryOperator.GREATER;
				case GREATER_EQUAL -> BinaryOperator.GREATER_EQUAL;
				
				default -> throw new IllegalStateException("Unexpected comparison operator: " + operator.type());
			};
			
			expression = new BinaryExpr(
                expression,
                binaryOperator,
                right
			);
		}
		return expression;
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
		
		if(match(TokenType.STRING)){
			String lexeme = previous().lexeme();
			String value = lexeme.substring(1,lexeme.length() - 1);
			return new StringLiteral(value);
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