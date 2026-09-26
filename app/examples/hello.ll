@.fmt.yell = private unnamed_addr constant [4 x i8] c"%d\0A\00"
@.fmt.whisper = private unnamed_addr constant [3 x i8] c"%d\00"

declare i32 @printf(ptr, ...)

define i32 @main() {
entry:
 %var.x = alloca i32
 store i32 10, ptr %var.x
 %tmp.0 = load i32, ptr %var.x
 %tmp.1 = mul i32 %tmp.0, 2
 %tmp.2 = add i32 %tmp.1, 5
 %var.y = alloca i32
 store i32 %tmp.2, ptr %var.y
 %tmp.3 = load i32, ptr %var.y
 call i32 (ptr, ...) @printf(ptr @.fmt.yell, i32 %tmp.3)
  ret i32 0
}
