package com.huf.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.regex.Pattern;

public class DateTest {
    @Test
    public void test2(){
        BigDecimal mintPrice = new BigDecimal("0.000000000000000000");
        System.out.println(mintPrice.compareTo(new BigDecimal(1)));
    }
    @Test
    public void getInbNotIna(){
        List<Integer> a = Arrays.asList(6936, 6935, 6934, 6933, 6932, 6931, 6929, 6928, 6927, 6925, 6924, 4998, 4997, 4996, 4995, 4994, 4993, 4991, 4990, 4989, 4988, 4987, 4986, 4985, 4984, 4983, 4982, 4981);
        List<Integer> b = Arrays.asList(5000, 6941, 7034, 7062, 5002, 7047, 7174, 6936, 4998, 7079, 7022, 7251, 7153, 7043, 5005, 4993, 7252, 7051, 7320, 7038, 7026, 7072, 7266, 7031, 4985, 5003, 7330, 7156, 4989, 7226, 7054, 7036, 4988, 7071, 5007, 4996, 7020, 7040, 7333, 6945, 7241, 4987, 4990, 7272, 7274, 6935, 7260, 4981, 7058, 7237, 7063, 7172, 6942, 7323, 7276, 7068, 6931, 5009, 7078, 7050, 7242, 7322, 7069, 7271, 7032, 7073, 7042, 7049, 7065, 5014, 7273, 7048, 6925, 7161, 7162, 5015, 7041, 7325, 5012, 7227, 7246, 5006, 7326, 7077, 4984, 7044, 7057, 7238, 7028, 7259, 7319, 7005, 7158, 7045, 7056, 7228, 7027, 5001, 7053, 7064, 7061, 7076, 7244, 7033, 6937, 5013, 7269, 7067, 7074, 7261, 7268, 7275, 7175, 7331, 7270, 7150, 7176, 7052, 7025, 7255, 6928, 7250, 4997, 7012, 7070, 7152, 4999, 6943, 7262, 7155, 7231, 7021, 7253, 7327, 7154, 5008, 7167, 7173, 7236, 7030, 6939, 7267, 7019, 4991, 7254, 7055, 7235, 4986, 6929, 7164, 6924, 7247, 4983, 7334, 7060, 5011, 4994, 7170, 7059, 7151, 6944, 7159, 7075, 6980, 7039, 7166, 7256, 5010, 7234, 7318, 7037, 7324, 7257, 6946, 7035, 7171, 4995, 4982, 6927, 7160, 7258, 7169, 7046);
        List<Integer> c = new ArrayList<>();
        for (Integer num : b) {
            if (a.contains(num)) {
                continue;
            }
            c.add(num);
        }
        System.out.println(c);

    }
    @Test
    public void testTimeZone(){
        String pattern = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime localDateTime = LocalDateTime.parse("2022-12-27 14:45:00", DateTimeFormatter.ofPattern(pattern));
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime zdt = ZonedDateTime.of(localDateTime, zoneId);
        System.out.println(zdt.toInstant().toEpochMilli());
        System.out.println(new Date().getTime());
        System.out.println("haha");
    }
    public String solution(String inputString) {
        // 在这⾥写代码
        Stack<Character> list = new Stack<>();
        for(int i = 0;i<inputString.length();i++){
            list.add(inputString.charAt(i));
            if(list.peek() == ')'){
                list.pop();
                String str = "";
                while(list.peek() != '('){
                    str += list.pop();
                }
                list.pop();
                for(int j = 0;j<str.length();j++){
                    list.add(str.charAt(j));
                }
            }
        }
        String result = "";
        for(int i = 0;i<list.size();i++){
            result += list.get(i);
        }

        return result;
    }

    @Test
    public void test1(){
        System.out.println(solution("foo(bar)blim"));
    }

    @Test
    public void testTimeParse(){
        Timestamp timestamp = Timestamp.from(Instant.ofEpochMilli(1672849095000L));
        System.out.println(timestampToString(timestamp,"Asia/Shanghai"));
    }

    @Test
    public void testPattern(){
        String regex = "@/order/sorting_[0-9]-button";
        String button = "@/order/sorting_1-button";
        boolean isMatch = Pattern.matches(regex, button);
        System.out.println(isMatch);
    }

    @Test
    public void testBoolean(){
        Boolean b = null;
        boolean c = b;
        System.out.println(c);
    }

    private String timestampToString(Timestamp timestamp, String timeZone){
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
        ZoneId zoneId = ZoneId.of(timeZone);
        return fmt.format(timestamp.toLocalDateTime().atZone(TimeZone.getDefault().toZoneId()).withZoneSameInstant(zoneId));
    }
}
