package org.example;

import cowlang.lexer.Lexer;
import cowlang.lexer.Token;
import cowlang.ast.Stmt;
import cowlang.parser.Parser;
import java.util.List;

import cowlang.codegen.LlvmIrGenerator;

import java.nio.file.Files;
import java.nio.file.Path;

import java.io.IOException;


public class App {
    public static void main(String... args) throws Exception {
		if(args.length != 1){
			System.err.println("Usage: cowlang <file.cow>");
			System.exit(1);
		}
		
		Path sourcePath = Path.of(args[0]);
		
		String source;
		
		try{
			source = Files.readString(sourcePath);
		} catch (IOException e){
			System.err.println("Could not read file:" + args[0]);
			System.exit(1);
			return;
		}
		
		String fileName = sourcePath.getFileName().toString();
		String baseName;
		
		if(fileName.endsWith(".cow")){
			baseName = fileName.substring(0, fileName.length() - 4);
		} else{
			System.err.println("cowlang compiler can only compile .cow files, please try again");
			System.exit(1);
			return;
		}
		
		Path outputPath = sourcePath.resolveSibling(baseName + ".ll");

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();
		
		Parser parser = new Parser(tokens);
		List<Stmt> statements = parser.parse();
		
		LlvmIrGenerator generator = new LlvmIrGenerator();
		String llvmIr = generator.generate(statements);
		
		Files.writeString(outputPath,llvmIr);
		System.out.println("Generated: " + outputPath);
		
    }
}