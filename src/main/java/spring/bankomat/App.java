package spring.bankomat;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spring.bankomat.entity.Account;
import spring.bankomat.entity.IEntity;
import spring.bankomat.store.IStore;


/* 2 Домашняя работа

1. Реализовать класс Account c полями:
     id – уникальный идентификатор счета,
     holder – владелец счета,
     amount – сумма на счете

2. Реализовать интерфейс AccountService, который производит манипуляцию со счетами пользователей.
Информация о счете должна хранится в файловой системе.

public class AccountService {
    void withdraw(int accountId, int amount) throws NotEnoughMoneyException, UnknownAccountException;
    void balance(int accountId) throws UnknownAccountException;
    void deposit(int accountId, int amount) throws NotEnoughMoneyException, UnknownAccountException;
    void transfer(int from, int to, int amount) throws NotEnoughMoneyException, UnknownAccountException;
}

3. Для доступа к файловому хранилищу реализовать интерфейс Store c помощью символьных потоков ввода/вывода.

 *  @param <E> - сущность, например, Account
public interface Store<E>{
    void write(E item);
    List<E> read();
}

4. Реализовать пользовательский класс для управления аккаунтами:
 - при вводе в консоле команды balance [id] – вывеси информацию о счёте
 - при вводе в консоле команды withdraw [id] [amount] – снять указанную сумму
 - при вводе в консоле команды deposite [id] [amount] – внести на счет указанную сумму
 - при вводе в консоле команды transfer [from] [to] [amount] – перевести сумму с одного счета на другой

5. Классы для AccountService и Store  должны быть бинами.
    При поднятии контекста запонить файловое хранилище 10 записями

*/

public class App {

    /**
     * Инициализация контекста приложения
     */
    static ApplicationContext context = new ClassPathXmlApplicationContext("bankomat-context.xml");

    /**
     * Бин объекта, класс которого определяется контекстом
     */
    //static public IEntity entity = context.getBean(Account.class);

    /**
     * Бин репозитория
     */
    //static public IStore<Account> store = (IStore<Account>)context.getBean("accountStore");

    /**
     * Бин шины взаимодействия с внешним миром
     */
    //static public IBus bus = context.getBean(IBus.class);

    /**
     * Точка входа в приложение
     *
     * Управление таблицей POJO-объектов в файле через консоль
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // инициализация хранилища
        //store.init("bankomat.json", entity);

        // запуск программы
        //bus.start(entity, store);
        System.out.println("");
    }

}
