package spring.bankomat2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.bankomat2.bus.ConsoleBus;
import spring.bankomat2.bus.IBus;
import spring.bankomat2.entity.Account;
import spring.bankomat2.entity.IEntity;
import spring.bankomat2.store.IStore;
import spring.bankomat2.store.JsonFileStore;

@Configuration
public class Config {

    @Bean
    public IEntity accountEntity() {
        System.out.println("accountEntity()");
        Account entity = new Account();
        entity.setHolder("qwe");
        return entity;
    }

    @Bean
    public IStore accountStore() throws Exception {
        System.out.println("accountStore()");
        IStore store = new JsonFileStore("accounts.json", accountEntity().getClass());
        return store;
    }

    @Bean
    public IBus accountBus() throws Exception {
        System.out.println("taskBus()");
        ConsoleBus bus = new ConsoleBus();
        bus.setEntityClass(accountEntity().getClass());
        bus.setStore(accountStore());
        return bus;
    }

}
