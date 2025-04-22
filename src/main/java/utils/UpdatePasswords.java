package utils;

import org.mindrot.jbcrypt.BCrypt;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import entities.Account;

public class UpdatePasswords {
    public void HashedPassword() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mariadb");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        // Lấy tất cả tài khoản
        var accounts = em.createQuery("SELECT a FROM Account a", Account.class).getResultList();

        // Mã hóa lại mật khẩu
        for (Account account : accounts) {
            String plainPassword = account.getPassword(); // Mật khẩu plain text
            String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
            account.setPassword(hashedPassword);
            em.merge(account);
        }

        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}