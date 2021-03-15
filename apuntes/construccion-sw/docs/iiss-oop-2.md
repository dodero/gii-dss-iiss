# OBJETOS - Delegación

<a id="caso2"></a>

## Caso 2 - Implementación de una orquesta

<a id="orquesta"></a>

### Delegación

#### Versión inicial: Orquesta v0.1

Criticar la solución siguiente (parte 1):

```java
  abstract class Instrumento {
      public void tocar() { }
      public static void afinarInstrumento(Instrumento i)
      {
         // Afinar en funcion del tipo de i
         if (i instanceof Viento)
            afinarViento(i);
         else if (i instanceof Cuerda)
            afinarCuerda(i);
         // Probar que esta afinado
         i.tocar();  
      }
      public static void afinarViento(Viento i)
      { System.out.println("afinal soplido"); }

      public static void afinarCuerda(Cuerda i)
      { System.out.println("afinar rasgado"); }
  }

  class Viento extends Instrumento {
      public void tocar() { soplar(); }
      public void afinar() { System.out.println("afinar soplido"); }
      public void soplar() { System.out.println("soplar"); }
  }

  class Cuerda extends Instrumento {
      public void tocar() { rasgar(); }
      public void afinar() { System.out.println("afinar rasgado"); }
      public void rasgar() { System.out.println("rasgar"); }
  }

  public class Orquesta {
    ArrayList<Instrumento> instrumentos;
    public Orquesta() {
      instrumentos = new ArrayList<Instrumento>(3); }
    public void tocar() {
       for (int i=0; i<instrumentos.size(); i++)
         instrumentos.get(i).tocar();
    }
    public static void main(String[] args) {
      instrumentos.add(new Viento());
      instrumentos.add(new Cuerda());
      for (int i=0; i<instrumentos.size(); i++)
         Instrumento.afinarInstrumento(
                instrumentos.get(i));
      tocar();
    }
  }
```

##### Críticas a la Orquesta v0.1

- __Acoplamiento__: método `static`
- __Cohesión__: ubicación de `main`

#### Implementación alternativa: Orquesta v0.2

Usar polimorfismo. Seguir criticando la implementación...

```java
  class Orquesta {
    ArrayList<Instrumento> instrumentos;
    public Orquesta() {
        instrumentos = new ArrayList<Instrumento>(3);
    }
    public void tocar() {
       for (int i=0; i<instrumentos.size(); i++)
         instrumentos.get(i).tocar();
    }
    public void afinar(Instrumento i) {
      i.afinar();  // Metodo polimorfico
      i.tocar();   // Prueba de que esta afinado
    }
  }
  public class PruebaOrquesta {
     public static void main(String[] args) {
        Orquesta orquesta = new Orquesta();
        orquesta.instrumentos.add(new Viento());
        orquesta.instrumentos.add(new Cuerda());
        orquesta.instrumentos.add(new Percusion());
        for (int i=0; i<instrumentos.size(); i++)
           orquesta.afinar(orquesta.instrumentos.get(i));
        orquesta.tocar();
     }
  }

  abstract class Instrumento {
      public void tocar() { };
      public void afinar() { };
  }

  class Viento extends Instrumento {
      public void tocar() { soplar(); }
      public void afinar() { System.out.println("afinar soplido"); }
      public void soplar() { System.out.println("soplar"); }
  }

  class Cuerda extends Instrumento {
      public void tocar() { rasgar(); }
      public void afinar() { System.out.println("afinar rasgado"); }
      public void rasgar() { System.out.println("rasgar"); }
  }

  class Percusion extends Instrumento {
      public void tocar() { golpear(); }
      public void afinar() { System.out.println("afinar golpeado"); }
      public void golpear() { System.out.println("golpear"); }
  }
```

##### Críticas a la Orquesta v0.2

- __Encapsulación__: método `add`
- __Encapsulación__: visibilidad de `Orquesta::instrumentos` (en C++ sería `friend`)
- __Flexibilidad__: la implementación `Orquesta::instrumentos` puede variar, pero no hay colección (agregado) en quien confíe `Orquesta` por delegación.

#### Implementación alternativa: Orquesta v0.3

Delegar las altas/bajas de `Instrumento` en la colección (agregado) de `Orquesta`:

```java
  class Orquesta {

    protected ArrayList<Instrumento> instrumentos;

    public Orquesta() {
        instrumentos = new ArrayList<Instrumento>(3);
    }
    public boolean addInstrumento(Instrumento i) {
       return instrumentos.add(i);
    }
    public boolean removeInstrumento(Instrumento i) {
       return instrumentos.remove(i);
    }
    public void tocar() {
       for (int i=0; i<instrumentos.size(); i++)
         instrumentos.get(i).tocar();
    }
    public void afinar(Instrumento i) {
      i.afinar();
      i.tocar(); // Prueba de que esta afinado
    }
  }

  public class PruebaOrquesta {
     public static void main(String[] args) {
        Orquesta orquesta = new Orquesta();
        orquesta.addInstrumento(new Viento());
        orquesta.addInstrumento(new Cuerda());
        orquesta.addInstrumento(new Percusion());
        for (int i=0; i<orquesta.instrumentos.size(); i++)
           orquesta.afinar(orquesta.instrumentos.get(i));
        orquesta.tocar();
     }
  }
```

##### Críticas a la Orquesta v0.3:

- __Acoplamiento__: `PruebaOrquesta` conoce la implementación basada en un `ArrayList` de la colección de instrumentos de la orquesta.
- __Variabilidad__: ¿La colección de instrumentos será siempre lineal?

#### Implementación alternativa: Orquesta v0.4

Definir una __interfaz__ para iterar en la colección de instrumentos:

```java
  class Orquesta {
    protected List<Instrumento> instrumentos;
    public Orquesta() {
       instrumentos = new ArrayList<Instrumento>(3);
    }
    public boolean addInstrumento(Instrumento i) {
       return instrumentos.add(i);
    }
    public boolean removeInstrumento(Instrumento i) {
       return instrumentos.remove(i);
    }
    public void tocar() {
       for (Iterator<Instrumento> i = instrumentos.iterator(); i.hasNext(); )
          i.next().tocar();
    }
    public void afinar(Instrumento i) {
       i.afinar();
       i.tocar(); // Prueba de que esta afinado
    }
  }

  public class PruebaOrquesta {
     public static void main(String[] args) {
        Orquesta orquesta = new Orquesta();
        orquesta.addInstrumento(new Viento());
        orquesta.addInstrumento(new Cuerda());
        orquesta.addInstrumento(new Percusion());
        for (Iterator<Instrumento> i = orquesta.instrumentos.iterator(); i.hasNext(); )
           orquesta.afinar(i.next());
        orquesta.tocar();
     }
  }
```

##### Críticas a la Orquesta v0.4

- __Ocultación__: el atributo `instrumentos` sigue sin ser privado.

Rehacemos la implementación, aprovechando que aparece una nueva versión del lenguaje (Java JDK 1.5) que permite iterar haciendo un __*for each*__ sobre una colección que implemente la interfaz `Iterable`. 


#### Implementación alternativa: Orquesta v0.5

Usando delegación + interfaces y el _for each_ de Java 1.5:

Criticar...

```java
  class Orquesta {
    private List<Instrumento> instrumentos;
    public Orquesta() {
       instrumentos = new ArrayList<Instrumento>(3);
    }
    public boolean addInstrumento(Instrumento i) {
       return instrumentos.add(i);
    }
    public boolean removeInstrumento(Instrumento i) {
       return instrumentos.remove(i);
    }
    public List<Instrumento> instrumentos() {
        return instrumentos;
    }
    public void tocar() {
       for (Instrumento i: instrumentos)
          i.tocar();
    }
    public void afinar(Instrumento i) {
       i.afinar();
       i.tocar(); // Prueba de que esta afinado
    }
  }

  public class PruebaOrquesta {
     public static void main(String[] args) {
        Orquesta orquesta = new Orquesta();
        orquesta.addInstrumento(new Viento());
        orquesta.addInstrumento(new Cuerda());
        orquesta.addInstrumento(new Percusion());
        for (Instrumento i: orquesta.instrumentos())
           orquesta.afinar(i);
        orquesta.tocar();
     }
  }
```

##### Críticas a la Orquesta v0.5:

- __Ocultación__: la interfaz del método `instrumentos()` sigue expuesta: el cliente sabe que devuelve una `List`.
- Hemos ocultado un poco la implementación de `instrumentos` (que es una `List`), pero ¿conviene saber que es una `List`? Quizá no hemos ocultado lo suficiente.

#### Implementación alternativa: Orquesta v0.6

Nos quedamos sólo con lo que nos interesa de la Orquesta: que es una colección iterable.

Eliminamos lo que no nos interesa: el resto de elementos de la interfaz `List` que explican la forma lineal de almacenar los instrumentos.

```java
  class Orquesta implements Iterable<Instrumento> {
    private List<Instrumento> instrumentos;
    public Orquesta() {
       instrumentos = new ArrayList<Instrumento>(3);
    }
    public boolean addInstrumento(Instrumento i) {
       return instrumentos.add(i);
    }
    public boolean removeInstrumento(Instrumento i) {
       return instrumentos.remove(i);
    }
    public Iterator<Instrumento> iterator() {
       return instrumentos.iterator();
    }
    public void tocar() {
       for (Instrumento i: this)
          i.tocar();
    }
    public void afinar(Instrumento i) {
      i.afinar();
      i.tocar(); // Prueba de que esta afinado
    }
  }

  public class PruebaOrquesta {
     public static void main(String[] args) {
        Orquesta orquesta = new Orquesta();
        orquesta.addInstrumento(new Viento());
        orquesta.addInstrumento(new Cuerda());
        orquesta.addInstrumento(new Percusion());
        for (Instrumento i: orquesta)
           orquesta.afinar(i);
        orquesta.tocar();
     }
  }
```

#### Implementación alternativa: Orquesta v0.7

Supongamos que queremos sustituir la implementación basada en una `List` por otra (quizá más eficiente) basada en un `Map`.

__Nota__: La interfaz [`java.util.Map`](http://docs.oracle.com/javase/6/docs/api/java/util/Map.html) declara los métodos siguientes:

```
clear() void – Map
containsKey(Object key) boolean – Map
containsValue(Object value) boolean – Map
entrySet() Set – Map
equals(Object o) boolean – Map
get(Object key) Object – Map
getClass() Class<? extends Object> – Object
hashCode() int – Map
isEmpty() boolean – Map
keySet() Set – Map
notify() void – Object
notifyAll() void – Object
put(Object key, Object value) Object – Map
putAll(Map t) void – Map
remove(Object key) Object – Map
size() int – Map
toString() String – Object
values() Collection – Map
wait() void – Object
wait(long timeout) void – Object
wait(long timeout, int nanos) void – Object
```

Pero ¡`Map` no implementa `Iterable`!

Existe una cierta tensión proveedor-cliente en la **frontera** de la interfaz

- Los proveedores de packages y frameworks quieren ampliar aplicabilidad
- Los clientes quieren una interfaz centrada en sus necesidades particulares

Construimos un `Map` y lo pasamos.

- Primera opción: Ninguno de los receptores deberá poder borrar algo del map. Pero ¡hay un `clear()` en el `Map`!
- Segunda opción: solo algunos tipos de objetos deben poderse guardar. Pero ¡los tipos de objeto a guardar no están restringidos en un `Map`!

¿La interfaz `Map` es siempre satisfactoria? ¿seguro que no va a cambiar?

- JDK < 5.0:

```java
      Map sensors = new HashMap();
      ...
      Sensor s = (Sensor)sensors.get(sensorId);
```

- JDK >= 5.0:

```java
      Map<Sensor> sensors = new HashMap<Sensor>();
      ...
      Sensor s = sensors.get(sensorId);
```

__Conclusión__: `Map<Sensor>` ofrece más de lo que necesitamos

```java
      public class Sensors {
        private Map sensors = new HashMap();
        public Sensor getById(String id) {
          return (Sensor) sensors.get(id);
        }
        //...
      }
```

- La interfaz `Map` queda oculta
- Filtramos los métodos que no nos sirven
- Más fácil de hacer evolucionar sin impacto en el resto de la aplicación
- El casting queda confinado en la clase Sensors, que es más seguro


__<span style="color:blue">Interfaces de frontera<span>__: No todo uso de `Map` o interfaz de frontera debe quedar encapsulado. Sólo es un consejo para no _pasar la interfaz_ con métodos que no vamos a necesitar.

Así que proponemos esta implementación de la Orquesta:

```java
  class Orquesta implements Iterable<Instrumento> {
    private Instrumentos instrumentos;
    public Orquesta() {
       instrumentos = new Instrumentos(3);
    }
    public boolean addInstrumento(Instrumento i) {
       return instrumentos.add(i);
    }
    public boolean removeInstrumento(Instrumento i) {
       return instrumentos.remove(i);
    }
    public Iterator<Instrumento> iterator() {
       return instrumentos.iterator();
    }
    public void tocar() {
       for (Instrumento i: instrumentos)
          i.tocar();
    }
    public void afinar(Instrumento i) {
      i.afinar();
      i.tocar(); // Prueba de que esta afinado
    }
  }

  public class Instrumentos implements Iterable<Instrumento> {
    private List instrumentos;
    public Instrumentos(int numero) {
      instrumentos = new ArrayList<numero>();
    }
    public Iterator<Instrumento> iterator() {
       return instrumentos.iterator();
    }
    public Instrumento addInstrumento(Instrumento i) {
      return instrumentos.add(i);
    }
    public Instrumento removeInstrumento(Instrumento i) {
      return instrumentos.remove(i);
    }
  }

  public class PruebaOrquesta {
     public static void main(String[] args) {
        Orquesta orquesta = new Orquesta();
        orquesta.addInstrumento(new Viento());
        orquesta.addInstrumento(new Cuerda());
        orquesta.addInstrumento(new Percusion());
        for (Instrumento i: orquesta)
           orquesta.afinar(i);
        orquesta.tocar();
     }
  }
```

Esta implementación sí que podemos adaptarla más fácilmente para cambiar el `List` por un `Map`, pues la responsabilidad de ser iterable ha quedado confinada en `Instrumentos`, que desacopla `Orquesta` y la implementación elegida (`List`, `Map`, etc.) para la colección de instrumentos.

#### Implementación final: Orquesta v0.8

Los `new` de `PruebaOrquesta` siguen introduciendo dependencias de `PruebaOrquesta` con respecto a los tipos concretos de `Instrumento`.

!!! warning "Ver antes el apartado [inyección de dependencias](../iiss-oop-3/#inyeccion)"

##### Construcción con spring

A través de un fichero de configuración `orquesta.xml` le indicamos los valores inyectables:

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

La inyección de la dependencia concreta la hace el contenedor (_spring_ en este ejemplo):

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

Un _bean_ es una clase/componente reutilizable en Java que tiene una interfaz bien definida, según una especificación estándar de Java, que permite a un contenedor gestionar su _ciclo de vida_ (crearlos, cambiarles valores de sus propiedades, destruirlos, etc.) Los _beans_ son usados por muchos frameworks, entre otros Spring:

- [Spring Bean](https://www.baeldung.com/spring-bean)
- [Spring FactoryBean](http://www.baeldung.com/spring-factorybean)

### <span style="color:blue">Composición y dependencias</span>

<a id="composicion"></a>

Delegación _en horizontal_ hacia otras clases cuya interfaz es bien conocida

- Los objetos miembro __delegados__ son cambiables en tiempo de ejecución sin afectar al código cliente ya existente
- Alternativa más flexible que la herencia. Ejemplo: `Cola extends ArrayList` implica que una cola va a implementarse como un `ArrayList` para toda la vida, sin posibilidad de cambio en ejecución

#### <span style="color:blue">Composición vs. Herencia</span>

- **Composición** (delegación _en horizontal_)
    - Sirve cuando hacen falta las características de una clase existente dentro de una nueva, pero no su interfaz.
    - Los objetos miembro privados pueden cambiarse en tiempo de ejecución.
    - Los cambios en el objeto miembro no afectan al código del cliente.

- **Herencia** (delegación _en vertical_)
    - Sirve para hacer una versión especial de una clase existente, reutilizando su interfaz.
    - La relación de herencia en los lenguajes de programación _suele ser_ __estática__ (definida en tiempo de compilación) y no __dinámica__ (que pueda cambiarse en tiempo de ejecución).
    - Permite re-interpretar el tipo de un objeto en tiempo de ejecución.


#### Ejemplo: implementación de identificadores

##### Handler en Java

```java
  interface Handler{
      String toString();
      int compareTo(Handler otro);
  }
  class IdentificadorNumerico implements Handler {
    private int id;
    IdentificadorNumerico (String id) throws NumberFormatException {
      this.id = new Integer(id).intValue();
    }
    public String toString() {
      return new Integer(id).toString();
    }
    public int compareTo(Handler otro) {
      return toString().compareTo(otro.toString());
    }
  }
```

##### Implementación por herencia

`java.lang.Comparable` es una interfaz implementada por `String`, `File`, `Date`, etc. y todas las llamadas _clases de envoltura_ del JDK (i.e. `Integer`, `Long`, etc.)

######  Métodos de la interfaz

- JDK 1.4:

```java
public interface Comparable {
  public int compareTo(Object o); //throws ClassCastException
}
```

- JDK 1.5:

```java
public interface Comparable<T> {
  public int compareTo(T o); //throws ClassCastException
}
```

######  Invariantes

- Anticonmutativa: `sgn(x.compareTo(y)) = -sgn(y.compareTo(x))`

- Transitividad: `(x.compareTo(y)>0 and y.compareTo(z)>0)` --> `x.compareTo(z)>0`

- `x.compareTo(y)=0` --> `sgn(x.compareTo(z))=sgn(y.compareTo(z))` $\forall$ `z`

- Consistencia con `equals` (no obligatoria): `(x.compareTo(y)=0)` <-- `(x.equals(y))`

######  Identificador de BankAccount: Implementación en Java ≥ 1.5

- Utilizando _templates_ (**polimorfismo paramétrico**)
- Delegar en `compareTo` y `equals` del tipo de id _envuelto_ (e.g. `String`)

```java 
import java.util.*;
import java.io.*;

public final class BankAccount implements Comparable<BankAccount> {
  private final String id;
  public BankAccount (String number)  {
    this.id = number;
  }
  public String getId() { return id; }
  @Override
  public int compareTo(BankAccount other) {
    if (this == other) return 0;
    assert this.equals(other) : "compareTo inconsistent with equals.";
    return this.id.compareTo(other.getId());
  }
  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof BankAccount)) return false;
    BankAccount that = (BankAccount)other;
    return this.id.equals(that.getId());
   }
  @Override
  public String toString() {
    return id.toString();
  }
}
```

######  Identificador de BankAccount: Implementación en Java ≤ 1.4

- No hay plantillas (polimorfismo paramétrico).
- La genericidad se consigue con `Object`. Hay que hacer casting.
- Cuidado con `Boolean` que no implementa `Comparable` en JDK 1.4


```java
import java.util.*;
import java.io.*;

public final class BankAccount implements Comparable {
  private final String id;
  public BankAccount (String number)  {
    this.id = number;
  }
  public String getId() { return id; }
  public int compareTo(Object other) {
    if (this == other) return 0;
    assert (other instanceof BankAccount) : "compareTo comparing objects of different type";
    BankAccount that = (BankAccount)other;
    assert this.equals(that) : "compareTo inconsistent with equals.";
    return this.id.compareTo(that.getId());
  }
  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof BankAccount)) return false;
    BankAccount that = (BankAccount)other;
    return this.id.equals(that.getId());
  }
  public String toString() {
      return id.toString();
  }
}
```

##### Implementación por composición/delegación

Cuando una clase hereda de una clase concreta que implementa `Comparable` y le añade un campo significativo para la comparación, no se puede construir una implementación correcta de `compareTo`. La única alternativa entonces es la composición en lugar de la herencia.

Una alternativa (no excluyente) a implementar `Comparable` es pasar un `Comparator` como parámetro (se prefiere __composición__ frente a __herencia__):

- Si `BankAccount` implementa `Comparable`:

```java
class BankAccountComparator implements java.util.Comparator<BankAccount> {
    public int compare(BankAccount o1, BankAccount o2) {
        return o1.compareTo(o2);
    }
}
```

- Si `BankAccount` no implementa `Comparable`:

```java
class BankAccountComparator implements java.util.Comparator<BankAccount> {
    public int compare(BankAccount o1, BankAccount o2) {
        return compare(o1.getId(), o2.getId());
    }
}
```

<span style="color:red;">¿Qué ventajas tiene la opción que usa __Composición__ frente a la que usa __Herencia (estática)__?</span>

La respuesta está en la [inyección de dependencias](#inyeccion)...
