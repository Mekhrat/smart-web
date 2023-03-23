package kz.kaznu.smart.utils;

import java.util.HashMap;
import java.util.Map;

public class ActivateUtils {
    private static Map<String, String> codes = new HashMap<>();

    public static String activateMessageText(String code) {
        return String.format("SMART дүкеніне тіркелу үшін <a href=\"http://localhost:8000/activate/%s\">сілтеме</a> бойынша өтіңіз!", code);
    }

    public static void addActivateCodeForEmail(String code, String email) {
        codes.put(code, email);
    }

    public static String getEmailByActivateCode(String code) {
        return codes.remove(code);
    }
}
