namespace java com.zhp
typedef i16 integer

enum PointType{
    city, village, lake, mountain, landmark
}
exception ServiceException{
    1:i32 code,
    2:string msg
}
struct Point{
    1:required double latitude,
    2:optional double longitude,
    3:string name,
    4:PointType type
}

service DistanceServer{
    integer getDistance(1:Point x,2:Point y) throws(1:ServiceException e);
    list<string> findOccurences(1:string stringToMatch,2:string regex) throws(1:ServiceException e);
    void saveToLog(1:string msg,2:string fileName) throws(1:ServiceException e);
}