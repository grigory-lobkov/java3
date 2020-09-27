package spring.bankomat2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.bankomat2.bus.IBus;


/* 3 Домашняя работа
0. Переписать задания первого и второго урока с XML-конфигурации на Java Base конфигурацию

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
    static public ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

    /**
     * Точка входа в приложение
     *
     * Управление таблицей POJO-объектов в файле через консоль
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        // запуск программы
        context.getBean(IBus.class).start();

    }

}
