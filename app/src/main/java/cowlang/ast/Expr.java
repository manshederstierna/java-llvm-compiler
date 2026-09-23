package cowlang.ast;

public sealed interface Expr permits IntegerLiteral,StringLiteral, VariableExpr, BinaryExpr{}
