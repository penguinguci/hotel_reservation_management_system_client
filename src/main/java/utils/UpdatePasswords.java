package utils;

import org.mindrot.jbcrypt.BCrypt;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import entities.Account;

public class UpdatePasswords {
    public void hashedPassword() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mariadb");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // Lấy tất cả tài khoản
        var accounts = em.createQuery("SELECT a FROM Account a", Account.class).getResultList();

        // Mã hóa lại mật khẩu nếu chưa được băm
        for (Account account : accounts) {
            String currentPassword = account.getPassword();

            // Kiểm tra xem mật khẩu đã được băm chưa
            if (!isHashed(currentPassword)) {
                // Nếu chưa băm, thì băm mật khẩu và cập nhật
                String hashedPassword = BCrypt.hashpw(currentPassword, BCrypt.gensalt());
                account.setPassword(hashedPassword);
                em.merge(account);
                System.out.println("Updated password for account: " + account.getUsername());
            } else {
                System.out.println("Password for account " + account.getUsername() + " is already hashed.");
            }
        }

        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    // Phương thức kiểm tra xem mật khẩu đã được băm bởi BCrypt hay chưa
    private boolean isHashed(String password) {
        if (password == null) {
            return false;
        }
        // Mật khẩu băm bởi BCrypt bắt đầu bằng $2a$ hoặc $2b$ và có độ dài 60 ký tự
        return password.matches("^\\$2[ab]\\$\\d{2}\\$.{53}$");
    }
}