package org.example;

import cowlang.lexer.Lexer;
import cowlang.lexer.Token;
import cowlang.ast.Stmt;
import cowlang.parser.Parser;
import java.util.List;

public class App {
    public static void main(String[] args) {
        System.out.println("cowlang lexer test");

		String source = """
			x <- 10;
			y <- x + 20 * 3;
			z <- (x + 20) * 3;
        """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();
		Parser parser = new Parser(tokens);
		List<Stmt> statements = parser.parse();
		
        for (Stmt statement : statements) {
            System.out.println(statement);
        }
    }
}