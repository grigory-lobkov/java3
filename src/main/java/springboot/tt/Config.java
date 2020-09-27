package springboot.tt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springboot.tt.bus.ConsoleBus;
import springboot.tt.bus.IBus;
import springboot.tt.entity.*;
import springboot.tt.store.IStore;
import springboot.tt.store.JsonFileStore;

@Configuration
public class Config {

    @Bean
    public IEntity userEntity() {
        User entity = new User();
        return entity;
    }

    @Bean
    public IStore userStore() throws Exception {
        IStore store = new JsonFileStore();
        store.init("users.json", userEntity().getClass());
        return store;
    }

    @Bean
    public IEntity attachmentEntity() {
        Attachment entity = new Attachment();
        return entity;
    }

    @Bean
    public IStore attachmentStore() throws Exception {
        IStore store = new JsonFileStore();
        store.init("attachments.json", attachmentEntity().getClass());
        return store;
    }

    @Bean
    public IEntity priorityTypeEntity() {
        PriorityType entity = new PriorityType();
        return entity;
    }

    @Bean
    public IStore priorityTypeStore() throws Exception {
        IStore store = new JsonFileStore();
        store.init("priorities.json", priorityTypeEntity().getClass());
        return store;
    }

    @Bean
    public IBus userBus() throws Exception {
        ConsoleBus bus = new ConsoleBus();
        bus.setEntityClass(userEntity().getClass());
        bus.setStore(userStore());
        return bus;
    }

}
