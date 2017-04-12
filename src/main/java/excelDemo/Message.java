package excelDemo;

import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by peng.zhou1 on 2016/7/20.
 */
public class Message {
    private String phone;
    private String msg;
    private Date date;
    private String memGuid;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMemGuid() {
        return memGuid;
    }

    public void setMemGuid(String memGuid) {
        this.memGuid = memGuid;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return phone + " " + memGuid + " " + sdf.format(date) + "\n\t";


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        return phone.equals(message.phone);

    }

    @Override
    public int hashCode() {
        return phone.hashCode();
    }

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            Long start = System.currentTimeMillis();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (int count = 0; count < 2; count++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("source", "http://www.baidu.com" + i);
                map.put("gifId", count);
                map.put("gifUrl", "http://www.baidu.com/www/111/111.gif");
                map.put("previewUrl", "http://www.baidu.com/www/111/111.gif");
                list.add(map);
            }
            JSONObject.toJSONString(list);
            long time = System.currentTimeMillis() - start;
            System.out.println(time);
        }
    }
}
