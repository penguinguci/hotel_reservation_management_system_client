package ultilities;

import dao.CustomerDAOImpl;
import dao.StaffDAOImpl;
import entities.Customer;
import interfaces.CustomerDAO;
import interfaces.StaffDAO;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class GenerateString {
    @SneakyThrows
    public static String generateStaffId() {
        String prefix = "NV" + LocalDate.now().getYear() +
                String.format("%02d", LocalDate.now().getMonthValue());

        StaffDAO staffDao = new StaffDAOImpl();
        long count = staffDao.countByPrefix(prefix);

        return prefix + String.format("%03d", count + 1);
    }


    private static final AtomicInteger sequence = new AtomicInteger(0);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

    public static synchronized String generateReservationId() {
        String timestamp = dateFormat.format(new Date());
        int seq = sequence.incrementAndGet() % 1000;
        return String.format("RS%s%03d", timestamp, seq);
    }

    public static synchronized String generateCustomerID() {
        String timestamp = dateFormat.format(new Date());
        int seq = sequence.incrementAndGet() % 1000;
        return String.format("KH%s%03d", timestamp, seq);
    }

    public static String generateUserName(String name) {
        String[] parts = name.split(" ");
        StringBuilder userName = new StringBuilder();
        for (String part : parts) {
            userName.append(part.charAt(0));
        }
        userName.append(System.currentTimeMillis() % 10000);
        return userName.toString();
    }

    public static String generateOrderID() {
        String timestamp = dateFormat.format(new Date());
        int seq = sequence.incrementAndGet() % 1000;
        return String.format("HD%s%03d", timestamp, seq);
    }

    public static String generateNameFileOrder() {
        String timestamp = dateFormat.format(new Date());
        int seq = sequence.incrementAndGet() % 1000;
        return String.format("%s%03d", timestamp, seq);
    }
}
