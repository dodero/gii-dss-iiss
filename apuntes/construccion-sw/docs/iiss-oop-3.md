# OBJETOS - Inyección de dependencias

<a id="inyeccion"></a>
<a id="caso3"></a>

## Caso 3 - Inyección de dependencias

### Caballeros de la mesa redonda

<a id="knights"></a>

#### Tomado de <a id="bibliografia#spring">Spring in Action</a>

Añadir pruebas unitarias a la solución siguiente:

```java
public class KnightOfTheRoundTable {
  private String name;
  private HolyGrailQuest quest;
  public KnightOfTheRoundTable(String name) {
    this.name = name;
    quest = new HolyGrailQuest();
  }
  public HolyGrail embarkOnQuest() throws GrailNotFoundException {
    return quest.embark();
  }
}

public class HolyGrailQuest {
  public HolyGrailQuest() {}
  public HolyGrail embark() throws GrailNotFoundException {
    HolyGrail grail = null;
    // Look for grail
    ...
    return grail;
  }
}
```

#### Construir pruebas con jUnit 3

¿Dónde está el acoplamiento?

```java
import junit.framework.TestCase;
public class KnightOfTheRoundTableTest extends TestCase {
  public void testEmbarkOnQuest() throws GrailNotFoundException {
    KnightOfTheRoundTable knight =
        new KnightOfTheRoundTable("CruzadoMagico");
    HolyGrail grail = knight.embarkOnQuest();
    assertNotNull(grail);
    assertTrue(grail.isHoly());
  }
}
```

- Instanciación de `HolyGrail`

- Cada vez que se prueba `KnightOfTheRoundTable`, también se prueba `HolyGrailQuest`.

- No se puede pedir a `HolyGrailQuest` que se comporte de otra forma (v.g. devolver null o elevar una excepción)

#### Ocultar la implementación detrás de una interfaz

```java
public interface Knight {
  Object embarkOnQuest() throws QuestFailedException;
}

public class KnightOfTheRoundTable implements Knight {
  private String name;
  private Quest quest;
  public KnightOfTheRoundTable(String name) {
    this.name = name;
    quest = new HolyGrailQuest();
  }
  public Object embarkOnQuest() throws QuestFailedException {
    return quest.embark();
  }
}

public interface Quest {
  abstract Object embark()
    throws QuestFailedException;
}

public class HolyGrailQuest implements Quest {
  public HolyGrailQuest() {}
  public Object embark() throws QuestFailedException {
    // Do whatever it means to embark on a quest
    return new HolyGrail();
  }
}
```

- El `Knight` aún recibe un tipo específico de `Quest`
- ¿Debe ser el caballero responsable de obtener un desafío?

#### Inyectar dependencias

```java hl_lines="10 11 12"
public class KnightOfTheRoundTable implements Knight {
  private String name;
  private Quest quest;
  public KnightOfTheRoundTable(String name) {
    this.name = name;
  }
  public Object embarkOnQuest() throws QuestFailedException {
    return quest.embark();
  }
  public void setQuest(Quest quest) {
    this.quest = quest;
  }
}
```

- El caballero no es el responsable de averiguar su misión.
- El caballero sólo sabe de su misión a través de la interfaz `Quest`.
- El caballero recibe la misión (se le inyecta) a través de `setQuest()`
- Puede asignársele cualquier implementación de `Quest`
    (`HolyGrailQuest`, `RescueDamselQuest`, etc.)

#### Construcción con spring

A través de un fichero de configuración XML le indicamos los valores inyectables:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN"
  "http://www.springframework.org/dtd/spring-beans.dtd">
<beans>
  <bean id="quest"
    class="HolyGrailQuest"/>
  <bean id="knight"
    class="KnightOfTheRoundTable">
    <constructor-arg>
      <value>CruzadoMagico</value>
    </constructor-arg>
    <property name="quest">
      <ref bean="quest"/>
    </property>
  </bean>
</beans>
```

La inyección de la dependencia concreta la hace el contenedor (_spring_ en este ejemplo):

```java
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;

public class KnightApp {
  public static void main(String[] args) throws Exception {
    BeanFactory factory = new XmlBeanFactory(new FileInputStream("knight.xml"));
    KnightOfTheRoundTable knight = (KnightOfTheRoundTable) factory.getBean("knight");
    knight.embarkOnQuest();
  }
}
```

#### Ejemplo: Logger

También se puede inyectar la dependencia en el constructor.

```java hl_lines="5"
import java.util.logging.Logger;

public class MyClass {
  private final static Logger logger;
  public MyClass(Logger logger) {
      this.logger = logger;
      // write an info log message
      logger.info("This is a log message.")
  }
}
```

Un _contenedor_ de dependencias en el framework debe responsabilizarse de crear las instancias de `Logger` e inyectarlas en su sitio (normalmente vía _reflexión_ o _introspección_)

#### Implementación final de la Orquesta v0.8

Los `new` de `PruebaOrquesta` de la versión v0.7 siguen introduciendo dependencias de `PruebaOrquesta` con respecto a los tipos concretos de `Instrumento`.

A través de un fichero de configuración `orquesta.xml` de Spring le indicamos los valores inyectables:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN"
  "http://www.springframework.org/dtd/spring-beans.dtd">
<beans>
  <bean id="trompeta"
    class="Viento"/>
  <bean id="violin"
    class="Cuerda"/>
  <bean id="tambor"
    class="Percusion"/>
  <bean id="viola"
    class="Cuerda"/>

  <bean id="cuarteto"
    class="Orquesta">
    <property name="instrumento1">
      <ref bean="trompeta"/>
    </property>
    <property name="instrumento2">
      <ref bean="violin"/>
    </property>
    <property name="instrumento3">
      <ref bean="viola"/>
    </property>
    <property name="instrumento4">
      <ref bean="tambor"/>
    </property>    
  </bean>
</beans>
```

La inyección de la dependencia concreta la hace el contenedor (_Spring_ en este ejemplo):

```java
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
public class PruebaOrquesta {
  public static void main(String[] args) throws Exception {
    BeanFactory factory =
      new XmlBeanFactory(new FileInputStream("orquesta.xml"));
    Orquesta orquesta =
      (Orquesta) factory.getBean("cuarteto");
    for (Instrumento i: orquesta)
           orquesta.afinar(i);
    orquesta.tocar();
  }
}
```

#### Dependencias en Java

JSR 330 es un estándar de Java para describir las dependencias de una clase con `@Inject` y otras anotaciones. Hay diversas implementaciones de [JSR 330](http://javax-inject.github.io/javax-inject/).

```java hl_lines="2 4 5"
public class MyPart {
  @Inject private Logger logger;
  // inject class for database access
  @Inject private DatabaseAccessClass dao;
  @Inject
  public void createControls(Composite parent) {
    logger.info("UI will start to build");
    Label label = new Label(parent, SWT.NONE);
    label.setText("Eclipse 4");
    Text text = new Text(parent, SWT.NONE);
    text.setText(dao.getNumber());
  }
}
```

Esta clase sigue usando `new` para ciertos elementos de la interfaz. Esto significa que no pensamos reemplazarlos ni siquiera para hacer pruebas.

#### Ejercicio: Identificador de BankAccount con inyección de dependencias

Supongamos que queremos obtener un listado ordenado por fecha de creación de todas las cuentas bancarias.

<span style="color:red;">¿Cómo afecta este cambio a la versión de `BankAccount` ya implementada con JDK 1.5? <br> Resolver mediante inyección de dependencias</span>

`BankAcccount.java`:

```java
import java.util.*;
import java.io.*;
import java.time.*;

public final class BankAccount implements Comparable<BankAccount> {
  private final String id;
  private LocalDate creationDate;
  private Comparator comparator;

  public BankAccount(String number) {
    this.id = number;
    comparator = new BankAccountComparatorById();
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate date) {
    this.creationDate = date;
  }

  public String getId() {
    return id;
  }

  public void setComparator(Comparator cmp) {
    comparator = cmp;
  }

  @Override
  public int compareTo(BankAccount other) {
    if (this == other)
      return 0;
    assert this.equals(other) : "compareTo inconsistent with equals.";
    return comparator.compare(this, other);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other)
      return true;
    if (!(other instanceof BankAccount))
      return false;
    BankAccount that = (BankAccount) other;
    return this.id.equals(that.getId());
  }

  @Override
  public String toString() {
    return id.toString();
  }
}
```

`BankAcccountComparatorById.java`:

```java
import java.util.*;

class BankAccountComparatorById implements Comparator<BankAccount> {
    public int compare(BankAccount o1, BankAccount o2) {
        return o1.getId().compareTo(o2.getId());
    }
}
```

`BankAcccountComparatorByCreationDate.java`:

```java
import java.util.*;

class BankAccountComparatorByCreationDate implements Comparator<BankAccount> {
    public int compare(BankAccount o1, BankAccount o2) {
        return o1.getCreationDate().compareTo(o2.getCreationDate());
    }
}
```

Ahora podría definirse una anotación del tipo `@comparator(BankAccountComparatorById.className)` o `@compareById` que inyecte a `BankAccount` una dependencia `BankAccountComparatorById` en `BankAccount.comparator`.


!!! info "Creación de anotaciones en Java"
    - Ejemplo de cómo [crear una anotación a medida en Java](https://www.baeldung.com/java-custom-annotation)

#### Decoradores en TypeScript

- Los decoradores de TypeScript son una forma de modificar programáticamente la definición de una clase.

- La definición de una clase describe la __forma__ de la clase, es decir, sus métodos y propiedades. Sólo cuando se instancie la clase, estas propiedades y métodos estarán disponibles.

- Los decoradores permiten __inyectar__ código en la definición real de una clase.

Pueden emplearse sobre:

- definiciones de clase
- definiciones de propiedades
- definiciones de funciones
- parámetros de métodos

Los decoradores de TypeScript se llaman __atributos__ en C# y __anotaciones__ en Java

Los decoradores de TypeScript son una característica __experimental__ del compilador y se han propuesto como parte del estándar __ECMAScript 7__. Deben activarse modificando el parámetro `experimentalDecorators` en `tsconfig.json`:

```json
{
  "compilerOptions": {
    "module": "commonjs",
    "target": "es3",
    "sourceMap": true,
    "experimentalDecorators": true
  },
  "exclude": [
    "node_modules"
  ]
}
```

##### Declaración

```typescript
function simpleDecorator(constructor: Function) {
  console.log('simpleDecorator called.');
}
```

##### Uso

```typescript
@simpleDecorator
class ClassWithSimpleDecorator {

}
```

??? question "¿Cuál es la salida del siguiente código TypeScript?"
    ```text
    simpleDecorator called.
    instance_1 : [object Object]
    instance_2 : [object Object]
    ```


```typescript
let instance_1 = new ClassWithSimpleDecorator();
let instance_2 = new ClassWithSimpleDecorator();
console.log(`instance_1: ${instance_1}`);
console.log(`instance_2 : ${instance_2}`);
```

##### Decoradores mútiples

??? question "¿Cuál es la salida del siguiente código TypeScript?"
    ```text
    secondDecorator called.
    simpleDecorator called.
    instance_1 : [object Object]
    ```

```typescript
function simpleDecorator(constructor: Function) {
  console.log('simpleDecorator called.');
}

function secondDecorator(constructor: Function) {
  console.log('secondDecorator called.')
}

@simpleDecorator
@secondDecorator
class ClassWithMultipleDecorators {
}

let instance_1 = new ClassWithMultipleDecorators();
console.log(`instance_1: ${instance_1}`);
```


##### Factorías de decoradores

- Los decoradores pueden aceptar parámetros
- Una factoría de decoradores es una función que devuelve el propio decorador.

######  Ejemplo de factoría de decoradores

```typescript
function decoratorFactory(name: string) {
  return function (constructor: Function) {
    console.log(`decorator function called with: ${name}`);
  }
}

@decoratorFactory('testName')
class ClassWithDecoratorFactory {
}
```

Salida:

```text
decorator function called with: testName
```

##### Tipos de decoradores

- Decoradores de clases
- Decoradores de propiedades
- Decoradores de propiedades estáticas
- Decoradores de métodos
- Decoradores de parámetros

!!! info "Lectura recomendada"
    Nathan Rozentals: <a href="https://www.packtpub.com/mapt/book/application_development/9781786468710">Mastering TypeScript</a>, Packt Publishing, 2nd edition, 2017
