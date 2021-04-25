package util.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utilities {
    public static String scaleNumber(String number, int scale){
        if(number.contains(".")) {
            BigDecimal result = new BigDecimal(number).setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
            return result.toString();
        }
        return number;
    }
}
