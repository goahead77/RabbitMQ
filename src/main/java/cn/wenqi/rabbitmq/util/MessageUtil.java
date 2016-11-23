package cn.wenqi.rabbitmq.util;

/**
 * @author wenqi
 */
public class MessageUtil {

    public static String getMessage(String[] strings){
        if (strings==null || strings.length < 1)
            return "Hello World!";
        return joinStrings(strings);
    }

    public static String getSeverity(String[] args){
        return "Severity";
    }

    private static String joinStrings(String[] strings) {
        int length = strings.length;
        if (length == 0) return "";
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1; i < length; i++) {
            words.append(".").append(strings[i]);
        }
        return words.toString();
    }

    /**
     * 产生一个斐波那契数
     * @param n intNum
     * @return fib
     * @throws Exception
     */
    public static int fib(int n) throws Exception {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fib(n-1) + fib(n-2);
    }
}
