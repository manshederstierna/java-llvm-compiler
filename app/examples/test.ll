@.fmt.yell = private unnamed_addr constant [4 x i8] c"%d\0A\00"
@.fmt.whisper = private unnamed_addr constant [3 x i8] c"%d\00"

declare i32 @printf(ptr, ...)

define i32 @main() {
entry:
%loop.counter.0 = alloca i32
store i32 0, ptr %loop.counter.0
br label %loop.cond.0
loop.cond.0:
%tmp.0 = load i32, ptr %loop.counter.0
%tmp.1 = icmp slt i32 %tmp.0, 5
br i1 %tmp.1, label %loop.body.0, label %loop.end.0
loop.body.0:
 call i32 (ptr, ...) @printf(ptr @.fmt.yell, i32 7)
%tmp.2 = add i32 %tmp.0, 1
store i32 %tmp.2, ptr %loop.counter.0
br label %loop.cond.0
loop.end.0:
  ret i32 0
}
