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
import java.util.Map;

public class LlvmIrGenerator{
	private final StringBuilder output = new StringBuilder();
	
	private final Map<String, String> variables = new HashMap<>();
	
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
			generateYell(yell);
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
			variables.put(assignment.name(), pointer);
			
			output.append(" " + pointer + " = alloca i32\n");
		}
		
		output.append(
			" store i32 "
				+ value
				+ ", ptr "
				+ pointer
				+ "\n"
		);
	}
	
	private String generateExpression(Expr expression){
		if(expression instanceof IntegerLiteral integer){
			return Integer.toString(integer.value());
		}
		
		if(expression instanceof VariableExpr variable){
			return generateVariableExpression(variable);
		}
		
		if(expression instanceof BinaryExpr binary){
			return generateBinaryExpression(binary);
		}
		
		throw new RuntimeException("LLVM generation now yet implemented for expression: " + expression.getClass().getSimpleName());
	}
	
	private String generateVariableExpression(VariableExpr variable){
		String pointer = variables.get(variable.name());
		
		if(pointer == null){
			throw new RuntimeException("Unknown variable: " + variable.name());
		}
		
		String temporary = nextTemporary();
		
		output.append(
			" "
			+ temporary
			+ " = load i32, ptr "
			+ pointer 
			+ "\n"
		);
		
		return temporary;
	}
	
	private String generateBinaryExpression(BinaryExpr binary){
		String left = generateExpression(binary.left());
		String right = generateExpression(binary.right());
		
		String instruction = switch(binary.operator()){
			case ADD -> "add";
			case SUBTRACT -> "sub";
			case MULTIPLY -> "mul";
			case DIVIDE -> "sdiv";
			
			default -> throw new RuntimeException("LLVM for this binary operator not yet implemented: operator = " + binary.operator());
		};
		
		String temporary = nextTemporary();
		
		output.append(
			" "
				+ temporary
				+ " = "
				+ instruction
				+ " i32 "
				+ left 
				+ ", "
				+ right
				+ "\n"
		);
		
		return temporary;
	}
	
	private void generateYell(YellStmt yell){
		String value = generateExpression(yell.value());
		
		output.append(
			" call i32 (ptr, ...) @printf("
						+ "ptr @.fmt.yell, i32 "
						+ value
						+ ")\n"
		);
	}
	
	private void generateWhisper(WhisperStmt whisper){
		String value = generateExpression(whisper.value());
		
		output.append(
			" call i32 (ptr, ...) @printf("
						+ "ptr @.fmt.whisper, i32 "
						+ value
						+ ")\n"
		);
	}
	
	private String nextTemporary(){
		String name = "%tmp." + temporaryCounter;
		
		temporaryCounter++;
		
		return name;
	}
}