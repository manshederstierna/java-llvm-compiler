package cowlang.ast;

import java.util.List;

public record LoopStmt(
	Expr count,
	List<Stmt> statements
	) implements Stmt {}