package spring.calculator2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.calculator2.repo.AdvancedCalculator;
import spring.calculator2.repo.ICalculator;
import spring.calculator2.bus.ConsoleBus;
import spring.calculator2.bus.IBus;

@Configuration
public class Config {

    @Bean
    public ICalculator calc(){
        return new AdvancedCalculator();
    }

    @Bean
    public IBus bus(){
        return new ConsoleBus();
    }

}
