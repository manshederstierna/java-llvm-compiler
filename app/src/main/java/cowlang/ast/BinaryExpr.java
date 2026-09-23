package cowlang.ast;

public record BinaryExpr(Expr left,BinaryOperator operator,Expr right) implements Expr {}
