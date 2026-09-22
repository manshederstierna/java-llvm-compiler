package cowlang.lexer;

//for myself: record = case class in Scala


public record Token(TokenType type, String lexeme, int line, int column){}


