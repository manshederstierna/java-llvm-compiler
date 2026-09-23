package org.example;

import cowlang.lexer.Lexer;
import cowlang.lexer.Token;
import cowlang.ast.Stmt;
import cowlang.parser.Parser;
import java.util.List;

import cowlang.codegen.LlvmIrGenerator;

import java.nio.file.Files;
import java.nio.file.Path;

public class App {
    public static void main(String... args) throws Exception {
		String source = """
			x <- 10;
			y <- x * 2 + 5;
			yell y;
        """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();
		Parser parser = new Parser(tokens);
		List<Stmt> statements = parser.parse();
		
		LlvmIrGenerator generator = new LlvmIrGenerator();
		String llvmIr = generator.generate(statements);
		System.out.println(llvmIr);
		
		Files.writeString(Path.of("program.ll"),llvmIr);
		
    }
}