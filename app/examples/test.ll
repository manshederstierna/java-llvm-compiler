@.fmt.yell = private unnamed_addr constant [4 x i8] c"%d\0A\00"
@.fmt.whisper = private unnamed_addr constant [3 x i8] c"%d\00"

declare i32 @printf(ptr, ...)

define i32 @main() {
entry:
 %var.x = alloca i32
 store i32 0, ptr %var.x
 call i32 (ptr, ...) @printf(ptr @.fmt.yell, i32 1)
br label %while.cond.0
while.cond.0:
 %tmp.0 = load i32, ptr %var.x
 %tmp.1 = icmp slt i32 %tmp.0, 20
%tmp.2 = zext i1 %tmp.1 to i32 
%tmp.3 =  icmp ne i32 %tmp.2, 0
br i1 %tmp.3, label %while.body.0, label %while.end.0
while.body.0:
 %tmp.4 = load i32, ptr %var.x
 call i32 (ptr, ...) @printf(ptr @.fmt.yell, i32 %tmp.4)
 %tmp.5 = load i32, ptr %var.x
 %tmp.6 = add i32 %tmp.5, 1
 store i32 %tmp.6, ptr %var.x
br label %while.cond.0
while.end.0:
  ret i32 0
}
