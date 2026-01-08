package es.uca.dss.ejercicios.knightsoftheroundtable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Knight knight() {
        return new KnightOfTheRoundTable(quest());
    }

    @Bean public Quest quest() {
        return new HolyGrailQuest();
    }
}
