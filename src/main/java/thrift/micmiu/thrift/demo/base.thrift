namespace java com.zhp
#namespace * com.zhp 适用于所有语言
//包含文件 引用时 使用 前缀.xxx
include "chapter3.thrift"
typedef i32 int

typedef map<string,i32> myMap
const double PI=3.1415926;

const map<i32,string> CITYS={1:"shanghang",2:"beijing",3:"hangzhou"};

const list<string> lan=["java","php","c++"];

struct city{
    1:string name;
    2:i32 pop;
}
const city SHANG_HAI ={"name":"shang_hai","pop":1000};

//生成单独的POJO
struct Teacher{
    1: required bool zhp,
    2: optional byte bete,//i8 bete;
    3: optional i16  by16,
    4: required i32 by32=0,
    5: required i64 by64,
    6: optional double d=0.1,
    7: optional string str="zhp"
}

enum Position{
  CEO,CFO,PM,PD=9
}

//生成单独的POJO
struct Stuedent{
    1: string name,
    2: Teacher teacher,
    3: list<i32> score,
    4: set<string> names,
    5: map<string,i32> maps,
    6: Position po
}

union StuTea{
    1:string name;
    2:i32 age;

}

exception MyError2{
    1:i32 code,
    2:string msg
}

service ZhpService{
    void say(1:Teacher zhpS);
    void sayNum(1:StuTea st);
    set<i32> getNums(1:list<string> alist);

    void testEnum(1:Position p);

    list<string> getList(1:set<i32> ids) throws (1:MyError2 myerror,2:chapter3.MyError merror2);

    //单向 不等待结果的返回 非阻塞
    oneway void noWaiteResult(1:string id);
}

service PeopleDirectory{
    oneway void log(1:string  message);
    void reloadDatabase();
}
service EmployeeDirectory extends PeopleDirectory{
    Stuedent findStu(1:i32 int);
}



