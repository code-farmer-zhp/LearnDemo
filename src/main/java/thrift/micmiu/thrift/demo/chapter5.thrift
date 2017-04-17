namespace * com.zhp.thrift
const double PI=3.1515926
struct MyStruct{
    1:bool mybool,
    2:byte mybyte,
    3:i16 myi16,
    4:i32 myi32,
    5:i64 myi64,
    6:double mydouble,
    7:string mystring,
    8:list<i32> mylist,
    9:set<i32> myset,
    10:map<i32,i32> mymap

}
union MyUnion{
    1:bool mybool,
    2:string mystring
}
enum MyEnum{
   ENUM1,
   ENUM2,
   ENUM3
}
exception MyError{
    1:i32 code,
    2:string msg
}
exception DivisionByZeroError{
    1:string msg
}
exception WrongTypeError{
    1:string msg
}

typedef i32 int

service MySecondService{
    oneway void log(1:string msg),
    int muilty(1:i32 x,2:i32 y),
    int getSize(1:string fileName) throws (1:MyError myerror),
    int divide(1:int num1,2:int num2) throws (1:DivisionByZeroError dbz,2:WrongTypeError wtr)
}