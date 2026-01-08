# Ejercicio Propuesto Inyección de dependencias

## a) Rediseñar la interfaz Quest del ejemplo KnightsOfTheRoundTable para que la genericidad no se implemente con java.lang.Object

Para hacer esto hay varias soluciones. En primer lugar, sería a través de genéricos usando para elegir el tipo de retorno de la quest.

### Quest.java

```java
public interface Quest<T>{
    T embark() throws QuestFailedException;
}
```

Esto provoca cambios en `Knight` y `KnightOfTheRoundTable`, siendo necesario hacerlos genéricos también.

```java
public interface Knight<T>{
    T embarkOnQuest() throws QuestFailedException;
}

public class KnightOfTheRoundTable<T> implements Knight<T> {
    private Quest<T> quest;

    public KnightOfTheRoundTable(Quest<T> quest) {
        this.quest = quest;
    }

    public T embarkOnQuest()
            throws QuestFailedException {
        return quest.embark();
    }
}
```

Esta conversión de tipos ser haría en tiempo de compilación.

Otra forma de conseguir algo más de genericidad en el tipo de la quest es crear una interfaz QuestItem y que la implementen todos los objetos que pueda devolver una quest. De tal manera que quedaría así.

### QuestItem.java

```java
public interface QuestItem {}
```

### HolyGrailItem.java

```java
public class HolyGrail implements QuestItem{}
```

### Quest.java

```java
public interface Quest{
    QuestItem embark() throws QuestFailedException;
}
```

```java
public class KnightOfTheRoundTable implements Knight {
    private Quest quest;

    @Autowired
    public KnightOfTheRoundTable(Quest quest) {
        this.quest = quest;
    }

    public QuestItem embarkOnQuest()
            throws QuestFailedException {
        return quest.embark();
    }
}

public class KnightOfTheRoundTable implements Knight {
    private Quest quest;

    @Autowired
    public KnightOfTheRoundTable(Quest quest) {
        this.quest = quest;
    }

    public QuestItem embarkOnQuest()
            throws QuestFailedException {
        return quest.embark();
    }
}
```

## Inyección de dependencias

Crearemos un fichero `AppConfig.java` para crear los beans

```java
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
```

Creamos dos beans un `Knight` y una `Quest` que se inyectarán.

### Test

```java
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = AppConfig.class)
class KnightsoftheroundtableApplicationTests {
 @Autowired
 private Knight knight;

 @Test
 void KnightConstructor() {
  assert(knight instanceof Knight);
  assertNotNull(knight.embarkOnQuest());
  assert(knight.embarkOnQuest() instanceof QuestItem);
 }
}
```

Ejecutamos el test usando

```shell
mvn test
```
