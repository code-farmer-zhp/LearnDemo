package thrift.zhp.thrift;


import com.alibaba.fastjson.JSONObject;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        TTransport socket = new TSocket("localhost", 8089, 1000);
        socket.open();
        TFramedTransport tFramedTransport = new TFramedTransport(socket);
        TCompactProtocol protocol = new TCompactProtocol(tFramedTransport);
        MySecondService.Client client = new MySecondService.Client(protocol);
        try {
            double divide = client.divide(64, 0);
            System.out.println(divide);
            socket.close();
        } catch (TException e) {
            e.printStackTrace();
        }
        String res="{\"recode\":\"10000\",\"remsg\":\"成功\",\"data\":{\" plat_no \":\"100\"\" query_order_no \":\"10001\",\" status \":\"1\"},\"signdata\":\"….\"}";
        JSONObject jsonObject = JSONObject.parseObject(res);
        JSONObject data = jsonObject.getJSONObject("data");
        String status = data.getString("status");
    }
}
