# cowlang

**cowlang** is a small programming language and compiler written from scratch in Java.

The compiler uses a hand-written lexer and recursive-descent parser to build an abstract syntax tree, then translates the program into LLVM IR. The generated LLVM can be assembled and compiled into a native executable using LLVM and Clang.

The project is primarily built as a way to learn how compilers work internally, from source code all the way down to native machine code.

## Example

```cowlang
x <- 3 > 4;
x <- x + (4 > 3);

yell x;
```

Output:

```text
1
```

Comparisons evaluate to integers:

```text
false -> 0
true  -> 1
```

## Language

cowlang currently supports:

- Integer literals
- Variables
- Assignment using `<-`
- Arithmetic expressions
- Comparison expressions
- Parentheses
- `yell` for output with a newline
- `whisper` for output without a newline
- `when` / `otherwise` parsing
- `//` comments

### Assignment

```cowlang
x <- 10;
y <- x + 5;
```

Variables are created when they are first assigned.

### Arithmetic

```cowlang
x <- 10 + 5;
y <- x * 2;
z <- (x + y) / 3;
```

Supported operators:

```text
+
-
*
/
```

Normal operator precedence is used.

For example:

```cowlang
x <- 10 + 5 * 2;
```

is interpreted as:

```text
10 + (5 * 2)
```

### Comparisons

```cowlang
x <- 10 > 5;
y <- 3 == 4;
```

Supported comparison operators:

```text
==
!=
<
<=
>
>=
```

Comparison results are represented as integers in cowlang:

```text
true  = 1
false = 0
```

This means comparisons can be used inside other expressions:

```cowlang
x <- 5 + (10 > 3);
```

which evaluates to:

```text
6
```

### Output

`yell` prints a value followed by a newline:

```cowlang
yell 42;
```

`whisper` prints without a newline:

```cowlang
whisper 42;
```

## Compiler architecture

The compiler currently consists of four main stages:

```text
cowlang source code
        |
        v
      Lexer
        |
        v
      Tokens
        |
        v
      Parser
        |
        v
       AST
        |
        v
 LLVM IR Generator
        |
        v
    LLVM IR (.ll)
        |
        v
      LLVM
        |
        v
      Clang
        |
        v
 Native executable
```

### Lexer

The lexer converts source code into tokens.

For example:

```cowlang
x <- 10 + 5;
```

becomes conceptually:

```text
IDENTIFIER("x")
ASSIGN("<-")
INTEGER("10")
PLUS("+")
INTEGER("5")
SEMICOLON(";")
```

### Parser

The parser is a hand-written recursive-descent parser.

Expression precedence is handled through separate parsing levels for comparisons, addition/subtraction and multiplication/division.

For example:

```cowlang
x <- 10 + 20 * 3;
```

produces an AST equivalent to:

```text
Assignment
└── x
    └── Add
        ├── 10
        └── Multiply
            ├── 20
            └── 3
```

### LLVM IR generation

The backend traverses the AST and emits textual LLVM IR.

A cowlang expression such as:

```cowlang
x <- 3 > 4;
```

can generate LLVM IR similar to:

```llvm
%tmp.0 = icmp sgt i32 3, 4
%tmp.1 = zext i1 %tmp.0 to i32

%var.x = alloca i32
store i32 %tmp.1, ptr %var.x
```

LLVM comparisons return an `i1`, which is converted to `i32` so that cowlang can represent boolean results as `0` and `1`.

## Building

### Requirements

The project requires:

- Java
- Gradle
- LLVM
- Clang

The Gradle wrapper is included in the repository.

### Build the compiler

On Linux/macOS:

```bash
./gradlew build
```

On Windows:

```powershell
.\gradlew.bat build
```

## Running the compiler

A cowlang source file uses the `.cow` extension.

Example:

```text
app/examples/hello.cow
```

The Java compiler can be run through Gradle.

On Linux:

```bash
./gradlew run --args="examples/hello.cow"
```

On Windows:

```powershell
.\gradlew.bat run --args="examples\hello.cow"
```

The compiler generates an LLVM `.ll` file.

## Native compilation

The generated LLVM IR can be assembled using `llvm-as`:

```bash
llvm-as program.ll -o program.bc
```

The resulting LLVM bitcode can then be compiled using Clang:

```bash
clang program.bc -o program
```

Run the resulting executable:

```bash
./program
```

On Windows, the resulting file can instead be compiled as an `.exe`.

## Compiler scripts

The repository contains helper scripts intended to automate the compilation pipeline.

### Windows

```text
compile.ps1
```

The PowerShell script automates the process from:

```text
.cow
 -> .ll
 -> .bc
 -> .exe
```

### Linux

```text
compile.sh
```

The Bash version provides the equivalent compilation workflow on Linux.

## Current status

The compiler can currently compile basic integer programs from cowlang source code into LLVM IR and then into native executables.

Working features include:

- Lexical analysis
- Recursive-descent parsing
- Abstract syntax tree generation
- Integer variables
- Arithmetic expressions
- Expression precedence
- Parenthesized expressions
- Comparison expressions
- Integer output
- LLVM IR generation
- Native compilation through LLVM and Clang

Some syntax is already represented in the frontend but does not yet have complete LLVM code generation.

## Planned features

Future work includes:

- LLVM control flow for `when` / `otherwise`
- `while` loops
- `loop N times`
- Functions using `~>`
- Function parameters
- `return`
- String code generation
- Variable scopes
- Semantic analysis
- Improved type checking
- Better compiler diagnostics
- AST and token dump options
- Compiler optimizations
- Automated tests

## Goals

cowlang is not intended to compete with production programming languages.

The goal of the project is to explore and implement the major components of a compiler:

- Lexical analysis
- Parsing
- Syntax trees
- Semantic analysis
- Symbol handling
- Intermediate representations
- Control flow
- Code generation
- Native compilation

Rather than relying on a parser generator or an existing compiler frontend, the major compiler stages are implemented manually in order to better understand how they work.

## License

This project is currently developed for educational and experimental purposes.