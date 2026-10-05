@.fmt.yell = private unnamed_addr constant [4 x i8] c"%d\0A\00"
@.fmt.whisper = private unnamed_addr constant [3 x i8] c"%d\00"

declare i32 @printf(ptr, ...)

define i32 @main() {
entry:
 %var.x = alloca i32
 store i32 4, ptr %var.x
 %var.y = alloca i32
 store i32 3, ptr %var.y
 %tmp.0 = load i32, ptr %var.x
 %tmp.1 = load i32, ptr %var.y
 %tmp.2 = mul i32 %tmp.0, %tmp.1
 %tmp.3 = add i32 %tmp.2, 2
 %var.z = alloca i32
 store i32 %tmp.3, ptr %var.z
 %tmp.4 = load i32, ptr %var.z
 %var.a = alloca i32
 store i32 %tmp.4, ptr %var.a
 %tmp.5 = load i32, ptr %var.a
 %tmp.6 = load i32, ptr %var.a
 %tmp.7 = add i32 %tmp.5, %tmp.6
 %tmp.8 = load i32, ptr %var.z
 %tmp.9 = add i32 %tmp.7, %tmp.8
 %var.b = alloca i32
 store i32 %tmp.9, ptr %var.b
 %tmp.10 = load i32, ptr %var.a
 call i32 (ptr, ...) @printf(ptr @.fmt.yell, i32 %tmp.10)
 %tmp.11 = load i32, ptr %var.b
 call i32 (ptr, ...) @printf(ptr @.fmt.yell, i32 %tmp.11)
 %tmp.12 = load i32, ptr %var.z
 call i32 (ptr, ...) @printf(ptr @.fmt.yell, i32 %tmp.12)
 %tmp.13 = load i32, ptr %var.z
 %tmp.14 = icmp sge i32 %tmp.13, 14
%tmp.15 = zext i1 %tmp.14 to i32 
%tmp.16 =  icmp ne i32 %tmp.15, 0
br i1 %tmp.16, label %then.0, label %else.0
then.0:
 %tmp.17 = load i32, ptr %var.x
 %tmp.18 = add i32 %tmp.17, 6
 store i32 %tmp.18, ptr %var.x
 %tmp.19 = load i32, ptr %var.x
 call i32 (ptr, ...) @printf(ptr @.fmt.yell, i32 %tmp.19)

 br label %end.0
else.0:
 %tmp.20 = load i32, ptr %var.x
 %tmp.21 = sub i32 %tmp.20, 2
 store i32 %tmp.21, ptr %var.x
 %tmp.22 = load i32, ptr %var.x
 call i32 (ptr, ...) @printf(ptr @.fmt.yell, i32 %tmp.22)

 br label %end.0
end.0:
 %tmp.23 = load i32, ptr %var.b
 %tmp.24 = load i32, ptr %var.a
 %tmp.25 = icmp sgt i32 %tmp.23, %tmp.24
%tmp.26 = zext i1 %tmp.25 to i32 
%tmp.27 =  icmp ne i32 %tmp.26, 0
br i1 %tmp.27, label %then.1, label %else.1
then.1:
 %tmp.28 = load i32, ptr %var.b
 call i32 (ptr, ...) @printf(ptr @.fmt.yell, i32 %tmp.28)
 %tmp.29 = load i32, ptr %var.b
 %tmp.30 = add i32 %tmp.29, 1
 store i32 %tmp.30, ptr %var.b

 br label %end.1
else.1:

 br label %end.1
end.1:
 %tmp.31 = load i32, ptr %var.a
 call i32 (ptr, ...) @printf(ptr @.fmt.yell, i32 %tmp.31)
 %tmp.32 = load i32, ptr %var.b
 call i32 (ptr, ...) @printf(ptr @.fmt.yell, i32 %tmp.32)
 %tmp.33 = load i32, ptr %var.x
 call i32 (ptr, ...) @printf(ptr @.fmt.yell, i32 %tmp.33)
  ret i32 0
}
