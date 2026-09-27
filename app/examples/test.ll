@.fmt.yell = private unnamed_addr constant [4 x i8] c"%d\0A\00"
@.fmt.whisper = private unnamed_addr constant [3 x i8] c"%d\00"

declare i32 @printf(ptr, ...)

define i32 @main() {
entry:
 %tmp.0 = icmp sgt i32 3, 4
%tmp.1 = zext i1 %tmp.0 to i32 %var.x = alloca i32
 store i32 %tmp.1, ptr %var.x
 %tmp.2 = load i32, ptr %var.x
 %tmp.3 = icmp sgt i32 4, 3
%tmp.4 = zext i1 %tmp.3 to i32 %tmp.5 = add i32 %tmp.2, %tmp.4
 store i32 %tmp.5, ptr %var.x
 %tmp.6 = load i32, ptr %var.x
 call i32 (ptr, ...) @printf(ptr @.fmt.yell, i32 %tmp.6)
  ret i32 0
}
