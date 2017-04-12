namespace java com.micmiu.thrift.demo

typedef i32 int

exception MyError{
    1:int error_code,
    2:string error_desc
}
service MyFirstService{
  // ”oneway”标识符表示client发出请求后不必等待回复（非阻塞）直接进行下面的操作，
  // ”oneway”方法的返回值必须是void
 oneway void log(1:string filename),
 int multiply(1:int number1,2:int number2),
 int get_log_size(1:string filename) throws(1:MyError error)

}