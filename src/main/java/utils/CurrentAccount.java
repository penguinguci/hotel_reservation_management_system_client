package utils;

import dao.AccountDAOImpl;
import entities.Account;
import entities.Role;
import entities.Staff;
import interfaces.AccountDAO;

public class CurrentAccount {
    private static Account currentAccount;

    public static void setCurrentAccount(Account account) {
        currentAccount = account;
    }

    public static Account getCurrentAccount() {
        return currentAccount;
    }

    public static void clearSession() {
        currentAccount = null;
    }
}
