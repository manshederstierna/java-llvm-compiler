package cowlang.ast;

import java.util.List;

public record WhenStmt(
	Expr condition,
	List<Stmt> thenBranch,
	List<Stmt> otherwiseBranch
	) implements Stmt {}