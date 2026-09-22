package cowlang.ast;

public record AssignmentStmt(String name, Expr value) implements Stmt {}