package cowlang.codegen;

import cowlang.ast.AssignmentStmt;
import cowlang.ast.BinaryExpr;
import cowlang.ast.BinaryOperator;
import cowlang.ast.Expr;
import cowlang.ast.IntegerLiteral;
import cowlang.ast.Stmt;
import cowlang.ast.VariableExpr;
import cowlang.ast.YellStmt;
import cowlang.ast.WhisperStmt;

import java.util.HashMap;
import java.util.List;
import java.util.Map

public class LlvmIrGenerator{
	private final StringBuilder output = new StringBuilder();
	
	private final Map<String, String> varibles = new HashMap<>();
	
	private int temporaryCounter = 0;
	
	public String generate(List<Stmt> statements){
		output.setLength(0);
		variables.clear();
		temporaryCounter = 0;
		
		
		//just LLVM boilerplate 
		
		output.append(
			"@.fmt.yell = private unnamed_addr constant [4 x i8] c\"%d\\0A\\00\"\n"
        );

        output.append(
                "@.fmt.whisper = private unnamed_addr constant [3 x i8] c\"%d\\00\"\n\n"
        );

        output.append(
                "declare i32 @printf(ptr, ...)\n\n"
        );

        output.append(
                "define i32 @main() {\n"
        );

        output.append(
                "entry:\n"
        );
		
		//transform all statements to LLVM instructions 
		for (Stmt statement : statements) {
            generateStatement(statement);
        }
		
		
		//more boilerplate
		output.append(
                "  ret i32 0\n"
        );

        output.append(
                "}\n"
        );

        return output.toString();
	}
	
	private void generateStatement(Stmt statement){
		if(statement instanceof AssignmentStmt assignment){
			generateAssignment(assignment);
			return;
		} 
		
		if(statement instanceof YellStmt yell){
			generateYell(yell)
			return;
		}
		
		if(statement instanceof WhisperStmt whisper){
			generateWhisper(whisper);
			return;
		}
		
		throw new RuntimeException(
            "LLVM generation not implemented for: "
				+ statement.getClass().getSimpleName()
		);
	}
	
	
	private void generateAssignment(AssignmentStmt assignment){
		String value = generateExpression(assignment.value());
		
		String pointer = variables.get(assignment.name());
		
		if(pointer == null){
			pointer = "%var." + assignment.name();
			variable.put(assignment, pointer);
			
			output.append(" " + pointer + " = alloca i32\n");
		}
		
		output.append(
			" store i32 "
				+ value
				+ ", ptr"
				+ pointer
				+ "\n"
		);
	}
}