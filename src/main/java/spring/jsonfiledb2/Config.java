package spring.jsonfiledb2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.jsonfiledb2.bus.ConsoleBus;
import spring.jsonfiledb2.bus.IBus;
import spring.jsonfiledb2.pojo.Actor;
import spring.jsonfiledb2.pojo.IPojo;
import spring.jsonfiledb2.pojo.Task;
import spring.jsonfiledb2.repo.IRepo;
import spring.jsonfiledb2.repo.JsonFileRepo;

@Configuration
public class Config {

    @Bean
    public IPojo taskPojo() {
        System.out.println("taskPojo()");
        Task pojo = new Task();
        pojo.setName("qwe");
        return pojo;
    }

    @Bean
    public IRepo taskRepo() throws Exception {
        System.out.println("taskRepo()");
        IRepo repo = new JsonFileRepo();
        repo.init("tasks.json", taskPojo().getClass());
        return repo;
    }

    @Bean
    public IPojo actorPojo() {
        System.out.println("actorPojo()");
        Actor pojo = new Actor();
        return pojo;
    }

    @Bean
    public IRepo actorRepo() throws Exception {
        System.out.println("actorRepo()");
        IRepo repo = new JsonFileRepo();
        repo.init("actors.json", actorPojo().getClass());
        return repo;
    }

    @Bean
    public IBus taskBus() throws Exception {
        System.out.println("taskBus()");
        ConsoleBus bus = new ConsoleBus();
        bus.setPojoClass(taskPojo().getClass());
        bus.setRepo(taskRepo());
        return bus;
    }

}