package spring.bankomat2.controller;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import spring.bankomat2.entity.Account;
import spring.bankomat2.exception.NotEnoughMoneyException;
import spring.bankomat2.exception.UnknownAccountException;
import spring.bankomat2.store.IStore;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService, ApplicationContextAware {

    ApplicationContext context;
    IStore<Account> store = null;

    public AccountServiceImpl() {
    }

    public AccountServiceImpl(IStore<Account> store) {
        this.store = store;
    }

    /**
     * Вернуть остаток по счету
     *
     * @param accountId
     * @throws UnknownAccountException
     */
    public BigDecimal balance(long accountId) throws UnknownAccountException {
        Account acc = store.get(accountId);
        if (acc == null)
            throw new UnknownAccountException();
        return acc.getAmount();
    }

    /**
     * Добавить деньги на счет
     *
     * @param accountId
     * @param amount
     * @throws UnknownAccountException
     */
    public void deposit(long accountId, BigDecimal amount) throws UnknownAccountException {
        Account acc = store.get(accountId);
        if (acc == null)
            throw new UnknownAccountException();

        acc.setAmount(acc.getAmount().add(amount));
        store.update(acc);
    }

    /**
     * Снять деньги со счета
     *
     * @param accountId счет
     * @param amount    сумма остатка
     * @throws NotEnoughMoneyException
     * @throws UnknownAccountException
     */
    public void withdraw(long accountId, BigDecimal amount) throws NotEnoughMoneyException, UnknownAccountException {
        Account acc = store.get(accountId);
        if (acc == null)
            throw new UnknownAccountException();
        BigDecimal s = acc.getAmount();
        if (s.compareTo(amount) < 0)
            throw new NotEnoughMoneyException();

        acc.setAmount(s.subtract(amount));
        store.update(acc);
    }

    /**
     * Перевести деньги с одного счета на другой
     *
     * @param from
     * @param to
     * @param amount
     * @throws NotEnoughMoneyException
     * @throws UnknownAccountException
     */
    public void transfer(long from, long to, BigDecimal amount) throws NotEnoughMoneyException, UnknownAccountException {
        Account accFrom = store.get(from);
        if (accFrom == null)
            throw new UnknownAccountException();
        Account accTo = store.get(to);
        if (accTo == null)
            throw new UnknownAccountException();
        BigDecimal s = accFrom.getAmount();
        if (s.compareTo(amount) < 0)
            throw new NotEnoughMoneyException();

        accFrom.setAmount(s.subtract(amount));
        accTo.setAmount(accTo.getAmount().add(amount));
        store.update(accFrom);
        store.update(accTo);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        if (this.store == null)
            this.store = (IStore<Account>) context.getBean("accountStore");
    }
}