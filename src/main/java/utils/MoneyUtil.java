package utils;

public class MoneyUtil {
    public static String formatCurrency(double amount) {
        return String.format("%,.0f VND", amount);
    }
}
