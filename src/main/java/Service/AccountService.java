package Service;
import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService()
    {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO)
    {
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account)
    {
        return this.accountDAO.addAccount(account);
    }

    public String getAccountUsernameById(int account_id)
    {
        return null;
    }
}
