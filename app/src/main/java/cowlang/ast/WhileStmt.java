package cowlang.ast;

import java.util.List;

public record WhileStmt(
	Expr condition,
	List<Stmt> statements
	) implements Stmt {}

