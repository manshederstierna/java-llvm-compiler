package cowlang.lexer;

public enum TokenType {

    // Literals
    IDENTIFIER,
    INTEGER,
    STRING,

    // Keywords
    WHEN,
    OTHERWISE,
    LOOP,
    TIMES,
    WHILE,
    YELL,
    WHISPER,
    RETURN,

    // Special cowlang operators
    ASSIGN,          // <-
    FUNCTION_ARROW,  // ~>

    // Arithmetic
    PLUS,            // +
    MINUS,           // -
    STAR,            // *
    SLASH,           // /

    // Comparison
    EQUAL_EQUAL,     // ==
    NOT_EQUAL,       // !=
    LESS,            // <
    LESS_EQUAL,      // <=
    GREATER,         // >
    GREATER_EQUAL,   // >=

    // Delimiters
    LEFT_PAREN,      // (
    RIGHT_PAREN,     // )
    LEFT_BRACE,      // {
    RIGHT_BRACE,     // }
    COMMA,           // ,
    SEMICOLON,       // ;

    // End of input
    EOF
}