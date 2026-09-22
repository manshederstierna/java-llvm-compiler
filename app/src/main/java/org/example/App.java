package org.example;

import cowlang.lexer.Lexer;
import cowlang.lexer.Token;

import java.util.List;

public class App {
    public static void main(String[] args) {
        System.out.println("cowlang lexer test");

		String source = """
			// cowlang lexer test

			x <- 100;
			y <- x / 5; // division

			yell y;
        """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}