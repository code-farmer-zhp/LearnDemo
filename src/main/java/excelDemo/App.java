package excelDemo;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Hello world!
 */
public class App {
    /**
     * @return
     * @throws IOException
     * @Title: getWeebWork
     * @Description: TODO(根据传入的文件名获取工作簿对象(Workbook))
     */
    public static Workbook getWeebWork(File file) throws IOException {
        FileInputStream fileStream = new FileInputStream(file);
        return new XSSFWorkbook(fileStream);
    }

    private static String getStringCellValue(Cell cell) {
        String strCell;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                DecimalFormat df = new DecimalFormat("#");
                strCell = df.format(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        return strCell;
    }


    public static void main(String[] args) throws IOException {
        File dir = new File("D:\\fcm_db_sql");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        File[] files = dir.listFiles();
        Map<String, List<Message>> messageMap = new HashMap<String, List<Message>>();
        for (File file : files) {
            Workbook workbook = getWeebWork(file);
            Sheet sheet = workbook.getSheetAt(0);
            int rownum = sheet.getLastRowNum();//获取总行数
            for (int i = 2; i <= rownum; i++) {
                Row row = sheet.getRow(i);
                Cell celldata = row.getCell(1);
                String phone = getStringCellValue(celldata);
                //System.out.print(phone + "\t");
                celldata = row.getCell(2);
                String msg = getStringCellValue(celldata);
                //System.out.print(msg + "\t");
                celldata = row.getCell(3);
                Date date = celldata.getDateCellValue();

                //System.out.print(sdf.format(date) + "\t");

                //System.out.println();
                Message message = new Message();
                message.setPhone(phone);
                message.setMsg(msg);
                message.setDate(date);
                List<Message> messageList = messageMap.get(phone);
                if (messageList == null) {
                    messageList = new ArrayList<Message>();
                    messageList.add(message);
                    messageMap.put(phone, messageList);
                } else {
                    messageList.add(message);
                }
            }
        }
        /**
         * 读取Excel表中的所有数据
         */
        Set<Message> phoneSet = new HashSet<Message>();
        Set<Message> noSend = new HashSet<Message>();
        Set<Message> timeBigFive = new HashSet<Message>();
        Workbook workbook = getWeebWork(new File("D:/phone.xlsx"));
        Sheet sheet = workbook.getSheetAt(0);
        int rownum = sheet.getLastRowNum();//获取总行数
        for (int i = 2; i <= rownum; ) {
            Row row = sheet.getRow(i);
            Cell celldata = row.getCell(2);
            String memGuid = getStringCellValue(celldata);
            //System.out.print(memGuid + "\t");
            celldata = row.getCell(3);
            String phone = getStringCellValue(celldata);
            //System.out.print(phone + "\t");
            celldata = row.getCell(4);
            Date bindTime = celldata.getDateCellValue();
            // System.out.print(sdf.format(bindTime) + "\t");
            //System.out.println();
            Cell typeCell = row.getCell(5);
            String type = getStringCellValue(typeCell);
            i = i + 2;
            if (type.equals("2")) {
                continue;
            }
            Message message = new Message();
            message.setPhone(phone);
            message.setDate(bindTime);
            message.setMemGuid(memGuid);
            List<Message> messages = messageMap.get(phone);

            if (messages != null) {
                //有发短信
                boolean isSend = false;
                for (Message msg : messages) {
                    Date sendTime = msg.getDate();
                    //当天发过短信
                    if (sdf.format(sendTime).equals(sdf.format(bindTime))) {
                        isSend = true;
                        long diffTime = bindTime.getTime() - sendTime.getTime();
                        //绑定时间减去发送时间 大于5分钟的
                        if (diffTime > 10 * 60 * 1000) {
                            if (!msg.getMsg().contains("注册")) {
                                if (msg.getMsg().contains("尊敬的用户，您的验证码为")) {
                                    msg.setMemGuid(memGuid);
                                    timeBigFive.add(message);
                                    if(phone.equals("18910154271")){
                                        System.out.println(msg.getMsg()+" "+sendTime);
                                    }
                                }
                            }

                        }
                        /*if (diffTime > 0 && diffTime <= 5 * 60 * 1000) {
                            //System.out.println(message);
                        } else {
                            if (!message.getMsg().contains("注册")) {
                                if (message.getMsg().contains("尊敬的用户，您的验证码为")) {
                                    message.setMemGuid(memGuid);

                                }
                            }
                        }*/
                        break;

                    }
                }
                //但是当天没有发短信
                if (!isSend) {
                    noSend.add(message);
                }
            } else {
                //没有发短信的
                phoneSet.add(message);
            }

        }
        //System.out.println(map.values());
        //System.out.println(phoneSet);
        //System.out.println(noSend);
       // System.out.println(timeBigFive);

    }
}
