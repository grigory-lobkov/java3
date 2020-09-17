package spring.bankomat.controller;

import spring.bankomat.exception.NotEnoughMoneyException;
import spring.bankomat.exception.UnknownAccountException;

import java.math.BigDecimal;

public interface AccountService {

    /**
     * Вернуть остаток по счету
     *
     * @param accountId
     * @throws UnknownAccountException
     */
    BigDecimal balance(long accountId) throws UnknownAccountException;

    /**
     * Добавить деньги на счет
     *
     * @param accountId
     * @param amount
     * @throws UnknownAccountException
     */
    void deposit(long accountId, BigDecimal amount) throws NotEnoughMoneyException, UnknownAccountException;

    /**
     * Снять деньги со счета
     *
     * @param accountId счет
     * @param amount сумма остатка
     * @throws NotEnoughMoneyException
     * @throws UnknownAccountException
     */
    void withdraw(long accountId, BigDecimal amount) throws NotEnoughMoneyException, UnknownAccountException;

    /**
     * Перевести деньги с одного счета на другой
     *
     * @param from
     * @param to
     * @param amount
     * @throws NotEnoughMoneyException
     * @throws UnknownAccountException
     */
    void transfer(long from, long to, BigDecimal amount) throws NotEnoughMoneyException, UnknownAccountException;

}
