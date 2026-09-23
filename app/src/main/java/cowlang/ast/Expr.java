package cowlang.ast;

public sealed interface Expr permits IntegerLiteral,VariableExpr, BinaryExpr{}
