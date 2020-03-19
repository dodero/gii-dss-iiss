# OBJETOS

# Caso 1 - Ocultación de la implementación

<a id="recorridolista"></a>

## Recorrido de una lista

### Versión inicial: Lista v0.1

__Abstracción__: La clase abstracta `List<T>` diferencia entre el *qué* y el *cómo*: Qué hace la lista vs. cómo se almacenan los elementos

Criticar la implementación siguiente:

```java
  public abstract class List<T> {
    public void addFirst(T value) { ... };
    public void removeFirst() { ... };
    public void addLast(T value) { ... };
    public void removeLast() { ... };
    public T first() { ... };
    public T last() { ... };
    public boolean isEmpty() { ... };
    public int length() { ... };
    public List<T> clone() { ... };
    public boolean isEqualTo(List<T>) { ... };
    public abstract void traverse();
    // etc...
  }
```

#### <span style="color:blue">Cohesión</span>

> Cohesion refers to the degree to which the elements inside a module belong together
> 
> -- <cite>[E. Yourdon & L. Constantine](bibliografia.md#yourdon)</cite>

#### Críticas a Lista v0.1

- `List<T>` aglutina más de una responsabilidad: almacenar y recorrer. Implementación no cohesionada
- ¿Y si hay distintas implementaciones de `traverse()`? Si implementamos varias versiones de la lista, introducimos más dependencias (acoplamiento)

#### Problemáticas de Lista v0.1

- Baja __cohesión__
- Alta __variabilidad__ no bien tratada --> poca __flexibilidad__

### Implementación alternativa: Lista v0.2

Delegar funcionalidad hacia las subclases (vía __herencia__).

Criticar la implementación:

```java
  class ListForward<T> extends List<T> {
    //...
    public void traverse() { // recorrer hacia adelante };
  }
  class ListBackward<T> extends List<T> {
    //...
    public void traverse() { // recorrer hacia atras};
  }
```

#### Críticas a Lista v0.2

- ¿Qué operación hace `traverse()` con cada elemento individual (imprimir, sumar, etc.)? ¿Hay que especializar de nuevo para cada tipo de operación? 
- ¿Y si hay que especializar de nuevo el recorrido: sólo los pares, sólo los impares, etc.? 

#### Problemáticas de Lista v0.2

- Elevada __complejidad__
- Alta __variabilidad__ no bien tratada --> poca __flexibilidad__, mala __reutilización__

### Implementación alternativa: Lista v0.3

Ampliamos la interfaz...

```java
  public interface List<T> {
    public void addFirst(T value);
    public void removeFirst();
    public void addLast(T value);
    public void removeLast();
    public T first();
    public T last();
    public boolean isEmpty();
    public int length();
    public List<T> clone();
    public boolean isEqualTo(List<T>);
    public void traverseForward();
    public void traverseBackWard();
    public void traverseEvens(); //pares
    public void traverseOdds();  //impares
    // etc...
  }
```

#### Críticas a Lista v0.3

- Si hay que cambiar la operación básica que hace `traverse()` con cada elemento (imprimir, sumar, etc.), ¿cuántos métodos hay que cambiar? Hay muchas dependencias
- Cuanto más variedad de recorridos (la interfaz es mayor), menos flexibilidad para los cambios. Implementación poco flexible

#### Problemáticas de Lista v0.3

- Muchas __dependencias__ --> __acoplamiento__
- Poca __flexibilidad__

### Implementación alternativa: Lista v0.4

__Delegar__ hacia otra clase

```java
  public interface List<T> {
    void addFirst(T value);
    void removeFirst();
    void addLast(T value);
    void removeLast();
    T first();
    T last();
    boolean isEmpty();
    int length();
    List<T> clone();
    boolean isEqualTo(List<T>);
    Iterator<T> iterator();
  }

  public interface Iterator<E> {
    boolean hasNext();
    E next();
    void remove();
  }
```

#### Ventajas

- Mayor __cohesión__: Las responsabilidades están ahora separadas: `List` almacena, `Iterator` recorre. `List` está más cohesionada
- Uso de __delegación__: la responsabilidad de recorrer se ha delegado hacia otro sitio

## <span style="color:blue">Ocultar la implementación</span>

<a id="ocultacion"></a>

- __Cohesión__: módulos auto-contenidos, independientes y con un
    único propósito
- __Acoplamiento__: minimizar dependencias entre módulos
- __Abstracción__: diferenciar el *qué* y el *cómo*
- __Modularidad__: clases, interfaces y componentes/módulos

### Alta cohesión, bajo acoplamiento

> Cuando los componentes están aislados, puedes cambiar uno sin preocuparte por el resto. Mientras no cambies las interfaces externas, no habrá problemas en el resto del sistema
>
> -- <cite>[Eric Yourdon](bibliografia.md#yourdon)</cite>

### Modularidad

Reducir el acoplamiento usando módulos o componentes con distintas responsabilidades, agrupados en bibliotecas

### Técnicas de ocultación

Hay diversas técnicas para ocultar la implementación...

- __Encapsular__: agrupar en módulos y clases
- __Visibilidad__: `public`, `private`, `protected`, etc.
- __Delegación__: incrementar la cohesión extrayendo funcionalidad pensada para otros propósitos fuera de un módulo
- __Herencia__: delegar _en vertical_
- __Polimorfismo__: ocultar la implementación de un método, manteniendo la misma interfaz de la clase base
- __Interfaces__: usar interfaces bien documentadas

<a id="herencia"></a>

## <span style="color:blue">Herencia: generalización y especialización</span>

- **Reutilizar la interfaz**
    - Clase base y derivada son del mismo tipo
    - Todas las operaciones de la clase base están también disponibles en la derivada

- **Redefinir vs. reutilizar el comportamiento**
    - *Overriding* (redefinición): cambio de comportamiento
    - *Overloading* (sobrecarga): cambio de interfaz

- **Herencia pura vs. extensión**
    - Herencia pura: mantiene la interfaz tal cual (relación *es-un*)
    - Extensión: amplía la interfaz con nuevas funcionalidades(relación *es-como-un*). Puede causar problemas de _casting_.

> When you inherit, you take an existing class and make a special version of it. In general, this means that you’re taking a general-purpose class and specializing it for a particular need. [...] it would make no sense to compose a car using a vehicle object —a car doesn’t contain a vehicle, it is a vehicle. The _is-a_ relationship is expressed with inheritance, and the _has-a_ relationship is expressed with composition.
>
> -- <cite>[Bruce Eckel](bibliografia.md#eckel)</cite>

### Ejemplo: Aventura v0.1

```java
   public class PersonajeDeAccion {
     public void luchar() {}
   }

   public class Heroe extends PersonajeDeAccion {
     public void luchar() {}
     public void volar() {}
   }

   public class Creador {
     PersonajeDeAccion[] personajes() {
       PersonajeDeAccion[] x = {
         new PersonajeDeAccion(),
         new PersonajeDeAccion(),
         new Heroe(),
         new PersonajeDeAccion()
       };
       return x;
     }
   }

   public class Aventura {
     public static void main(String[] args) {
       PersonajeDeAccion[] cuatroFantasticos = new Creador().personajes();
       cuatroFantasticos[1].luchar();
       cuatroFantasticos[2].luchar(); // Upcast

       // En tiempo de compilacion: metodo no encontrado:
       //! cuatroFantasticos[2].volar();
       ((Heroe)cuatroFantasticos[2]).volar(); // Downcast
       ((Heroe)cuatroFantasticos[1]).volar(); // ClassCastException
       for (PersonajeDeAccion p: cuatroFantasticos)
           p.luchar; // Sin problema
       for (PersonajeDeAccion p: cuatroFantasticos)
           p.volar; // El 0, 1 y 3 van a lanzar ClassCastException
     }
   }
```

#### Críticas a Aventura v0.1

- ¿De qué tipos van a ser los personales de acción? --> problema de _downcasting_
- Hay que rediseñar la solución por ser insegura

```java
   interface SabeLuchar {
     void luchar();
   }
   interface SabeNadar {
     void nadar();
   }
   interface SabeVolar {
     void volar();
   }
   class PersonajeDeAccion {
     public void luchar() {}
   }
   class Heroe
       extends PersonajeDeAccion
       implements SabeLuchar,
                  SabeNadar,
                  SabeVolar {
     public void nadar() {}
     public void volar() {}
   }

   public class Aventura {
     static void t(SabeLuchar x)
        { x.luchar(); }
     static void u(SabeNadar x)
        { x.nadar(); }
     static void v(SabeVolar x)
        { x.volar(); }
     static void w(PersonajeDeAccion x)
        { x.luchar(); }
     public static void main(String[] args)
     {
       Heroe i = new Heroe();
       t(i); // Tratar como un SabeLuchar
       u(i); // Tratar como un SabeNadar
       v(i); // Tratar como un SabeVolar
       w(i); // Tratar como un PersonajeDeAccion
     }
   }
```

### Uso correcto de la herencia

Hay dos formas de contemplar la herencia:

- Como **tipo**:
    - Las clases son tipos y las subclases son subtipos
    - Las clases satisfacen la propiedad de __substitución__ (LSP, Liskov Substitution Principle): toda operación que funciona para un objeto de la clase C también debe funcionar para un objeto de una subclase de C

- Como **estructura**:
    - La herencia se usa como una forma cualquiera de estructurar programas
    - Esta visión es **errónea**, pues provoca que no se satisfaga la propiedad LSP

#### Ejemplo: herencia como estructura

```java
class Account {
  float balance;
  float getBalance() { return balance; }
  void transferIn (float amount) { balance -= amount; }
}

class VerboseAccount extends Account {
  void verboseTransferIn (float amount) {
    super.transferIn(amount);
    System.out.println("Balance: "+balance);
  };
}

class AccountWithFee extends VerboseAccount {
  float fee = 1;
  void transferIn (float amount) { super.verboseTransferIn(amount-fee); }
}
```

- Todos los objetos $a$ de la clase `Account` deben cumplir que si $b=a.getBalance()$ antes de ejecutar $a.transferIn(s)$ y  $b´=a.getBalance()$ después de ejecutar $a.transferIn(s)$, entonces $b+s=b´$.
- Sin embargo, con la estructura `AccountWihFee` < `VerboseAccount` < `Account`, un objeto de tipo `AccountWithFee` no funciona bien cuando se contempla como un objeto `Account`. Considérese la siguiente secuencia:

```java
void f(Account a) {
  float before = a.getBalance();
  a.transferIn(10);
  float after = a.getBalance();
  // Suppose a is of type AccountWithFee:
  //   before + 10 != after    !!
  //   before + 10-1 = after
}
```

## <span style="color:blue">Polimorfismo</span>

<a id="polimorfismo"></a>

Fenómeno por el que, cuando se llama a una operación de un objeto del que no se sabe su tipo específico, se ejecuta el método adecuado de acuerdo con su tipo.

El polimorfismo se basa en:

- **Enlace dinámico**: se elige el método a ejecutar en tiempo de ejecución, en función de la clase de objeto; es la implementación del *polimorfismo*

- **Moldes (_casting_)**
    - *Upcasting*: Interpretar un objeto de una clase derivada como del mismo tipo que la clase base
    - *Downcasting*: Interpretar un objeto de una clase base como del mismo tipo que una clase derivada suya

# Caso 2 - Delegación

<a id="orquesta"></a>

## Implementación de una orquesta

### Versión inicial: Orquesta v0.1

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

#### Críticas a la Orquesta v0.1

- __Acoplamiento__: método `static`
- __Cohesión__: ubicación de `main`

### Implementación alternativa: Orquesta v0.2

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

#### Críticas a la Orquesta v0.2

- __Encapsulación__: método `add`
- __Encapsulación__: visibilidad de `Orquesta::instrumentos` (en C++ sería `friend`)
- __Flexibilidad__: la implementación `Orquesta::instrumentos` puede variar, pero no hay colección (agregado) en quien confíe `Orquesta` por delegación.

### Implementación alternativa: Orquesta v0.3

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

#### Críticas a la Orquesta v0.3:

- __Acoplamiento__: `PruebaOrquesta` conoce la implementación basada en un `ArrayList` de la colección de instrumentos de la orquesta.
- __Variabilidad__: ¿La colección de instrumentos será siempre lineal?

### Implementación alternativa: Orquesta v0.4

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

#### Críticas a la Orquesta v0.4

- __Ocultación__: el atributo `instrumentos` sigue sin ser privado.

Rehacemos la implementación, aprovechando que aparece una nueva versión del lenguaje (Java JDK 1.5) que permite iterar haciendo un __*for each*__ sobre una colección que implemente la interfaz `Iterable`. 


### Implementación alternativa: Orquesta v0.5

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

#### Críticas a la Orquesta v0.5:

- __Ocultación__: la interfaz del método `instrumentos()` sigue expuesta: el cliente sabe que devuelve una `List`.
- Hemos ocultado un poco la implementación de `instrumentos` (que es una `List`), pero ¿conviene saber que es una `List`? Quizá no hemos ocultado lo suficiente.

### Implementación alternativa: Orquesta v0.6

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

### Implementación alternativa: Orquesta v0.7

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
       for (Instrumento i: this)
          i.tocar();
    }
    public void afinar(Instrumento i) {
      i.afinar();
      i.tocar(); // Prueba de que esta afinado
    }
  }

  public class Instrumentos {
    private List instrumentos;
    public Instrumentos(int numero) {
      instrumentos = new ArrayList<numero>();
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

### Implementación final: Orquesta v0.8

Los `new` de `PruebaOrquesta` siguen introduciendo dependencias de `PruebaOrquesta` con respecto a los tipos concretos de `Instrumento`.

!!! warning "Ver antes el apartado [inyección de dependencias](#inyeccion)"

#### Construcción con spring

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

## <span style="color:blue">Composición y dependencias</span>

<a id="composicion"></a>

Delegación _en horizontal_ hacia otras clases cuya interfaz es bien conocida

- Los objetos miembro __delegados__ son cambiables en tiempo de ejecución sin afectar al código cliente ya existente
- Alternativa más flexible que la herencia. Ejemplo: `Cola extends ArrayList` implica que una cola va a implementarse como un `ArrayList` para toda la vida, sin posibilidad de cambio en ejecución

### <span style="color:blue">Composición vs. Herencia</span>

- **Composición** (delegación _en horizontal_)
    - Sirve cuando hacen falta las características de una clase existente dentro de una nueva, pero no su interfaz.
    - Los objetos miembro privados pueden cambiarse en tiempo de ejecución.
    - Los cambios en el objeto miembro no afectan al código del cliente.

- **Herencia** (delegación _en vertical_)
    - Sirve para hacer una versión especial de una clase existente, reutilizando su interfaz.
    - La relación de herencia en los lenguajes de programación _suele ser_ __estática__ (definida en tiempo de compilación) y no __dinámica__ (que pueda cambiarse en tiempo de ejecución).
    - Permite re-interpretar el tipo de un objeto en tiempo de ejecución.


### Ejemplo: implementación de identificadores

#### Handler en Java

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

#### Implementación utilizando `Comparable`

`java.lang.Comparable` es una interfaz implementada por `String`, `File`, `Date`, etc. y todas las llamadas _clases de envoltura_ del JDK (i.e. `Integer`, `Long`, etc.)

##### Métodos de la interfaz

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

##### Invariantes

- Anticonmutativa: `sgn(x.compareTo(y)) = -sgn(y.compareTo(x))`

- Transitividad: `(x.compareTo(y)>0 and y.compareTo(z)>0)` --> `x.compareTo(z)>0`

- `x.compareTo(y)=0` --> `sgn(x.compareTo(z))=sgn(y.compareTo(z))` $\forall$ `z`

- Consistencia con `equals` (no obligatoria): `(x.compareTo(y)=0)` <-- `(x.equals(y))`

##### Identificador de BankAccount: Implementación en Java 1.5

- Utilizando _templates_
- Delegar en `compareTo` y `equals` del tipo de id _envuelto_ (e.g. `String`)

##### Identificador de BankAccount: Implementación en Java 1.4

- No hay plantillas. La genericidad se consigue con `Object`. Hay que hacer casting.
- Cuidado con `Boolean` que no implementa `Comparable` en JDK 1.4

```java tab="Java ≥ 1.5"
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

```java tab="Java ≤ 1.4"
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

<a id="inyeccion"></a>

# Caso 3 - Inyección de dependencias

## Caballeros de la mesa redonda

<a id="knights"></a>

### Tomado de <a id="bibliografia#spring">Spring in Action</a>

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

### Construir pruebas con jUnit 3

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

### Ocultar la implementación detrás de una interfaz

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

### Inyectar dependencias

```java
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

### Construcción con spring

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

### Ejemplo: Logger

También se puede inyectar la dependencia en el constructor.

```java
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

### Implementación final de la Orquesta v0.8

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

### Dependencias en Java

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

### Ejercicio: Identificador de BankAccount con inyección de dependencias

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

### Decoradores en TypeScript

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

#### Declaración

```typescript
function simpleDecorator(constructor: Function) {
  console.log('simpleDecorator called.');
}
```

#### Uso

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

#### Decoradores mútiples

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


#### Factorías de decoradores

- Los decoradores pueden aceptar parámetros
- Una factoría de decoradores es una función que devuelve el propio decorador.

##### Ejemplo de factoría de decoradores

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

#### Tipos de decoradores

- Decoradores de clases
- Decoradores de propiedades
- Decoradores de propiedades estáticas
- Decoradores de métodos
- Decoradores de parámetros

!!! info "Lectura recomendada"
    Nathan Rozentals: <a href="https://www.packtpub.com/mapt/book/application_development/9781786468710">Mastering TypeScript</a>, Packt Publishing, 2nd edition, 2017

# Caso 4 - Código duplicado

## Cálculo de nóminas

<a id="nominas"></a>

### Implementación de nóminas v0.1

??? question "En la siguiente implementación, ¿dónde hay código duplicado?"
    - Código duplicado en los constructores de las clases y subclases
    - Refactorizar delegando hacia la superclase

```java
  public class Empleado {
    Comparable id;
    String name;
    public Empleado(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public void print() {
        System.out.println(id+" "+name);
    }
  }
  public class Autonomo extends Empleado {
    String vatCode;
    public Autonomo(String id, String name, String vat) {
        this.id = id;
        this.name = name;
        this.vatCode = vat;
    }
    public void print() {
        System.out.println(id+" "+name+" "+vatCode);
    }
  }
  public class Prueba {
    public static void main(String[] args) {
      Empleado e = new Empleado("0001","Enrique");
      Empleado a = new Autonomo("0002","Ana","12345-A");
      e.print();  
      a.print();  
    }
  }
```

### Nóminas v0.2

- Requisito: los trabajadores autónomos cobran por horas (no tienen un salario fijo bruto)
- Incluimos el método `computeMonthlySalary` para el cálculo de la nómina mensual

??? question "¿Están descohesionadas las clases?"
    - ¿Todos los empleados deben tener un salario anual `yearlyGrossSalary` bruto? Los autónomos no...
    - El método de cálculo del salario está descohesionado

```java
  public class Empleado {
    Comparable id;
    String name;
    float yearlyGrossSalary;
    public Empleado(String id, String name) {
        this.id = id;
        this.name = name;
    }
    float setSalary( float s ) { yearlyGrossSalary=s; }   
    public void print() {
        System.out.print(id+" "+name);
    }
    public float computeMonthlySalary() {
        return yearlyGrossSalary/12;
    }
  }
  public class Autonomo extends Empleado {
    String vatCode;
    float workingHours;
    public Autonomo(String id, String name, String vat) {
        super(id,name);
        this.vatCode = vat;
        this.workingHours = 0.0;
    }
    public float computeMonthlySalary() {
        return workingHours*Company.getHourlyRate()*(1.0+Company.getVatRate());
    }
    @Override
    public void print() {
        super.print();
        System.out.print(" "+vatCode);
    }
  }
  public class Prueba {
    public static void main(String[] args) {
      Empleado e = new Empleado("0001", "Enrique");
      Empleado a = new Autonomo("0001", "Ana", "12345-A");
      e.print();  System.out.println();
      a.print();  System.out.println();
    }
  }
```

### Nóminas v0.3

```java
  public abstract class Empleado {
    /* ... */
    public abstract float computeMonthlySalary();
  }
  public class Plantilla extends Empleado {
    float yearlyGrossSalary;
    /* ... */
    float setSalary( float s ) { yearlyGrossSalary=s; }
    public float computeMonthlySalary() {
        return yearlyGrossSalary/12;
    }
  }
  public class Autonomo extends Empleado {
    String vatCode;
    float workingHours;
    public Autonomo(String id, String name, String vat) {
        super(id,name);
        this.vatCode = vat;
        this.workingHours = 0.0;
    }
    public void addWorkingHours(float workingHours){
      this.workingHours += workingHours;
    }
    public float computeMonthlySalary() {
        return workingHours*Company.getHourlyRate()*(1.0+Company.getVatRate());
    }
    @Override
    public void print() {
        super.print();
        System.out.print(" "+vatCode);
    }
  }
  public class Prueba {
    public static void main(String[] args) {
      Empleado e = new Plantilla("0001", "Pepe");
      e.setSalary(25000.0);
      Empleado a = new Autonomo("0001", "Ana", "12345-A");
      a.addWorkingHours(30.0);
      e.print(); System.out.println(" Salario: "+e.computeMonthlySalary()+" EUR");
      a.print(); System.out.println(" Salario: "+a.computeMonthlySalary()+" EUR");
    }
  }
```

<a id="refactoring"></a>

## <span style="color:blue;">Refactoring</span>

Hacer _refactoring_ es hacer pequeñas transformaciones en el código que mantienen el sistema funcional, sin añadir nuevas funcionalidades.

> Refactoring is a disciplined technique for restructuring an existing body of code, altering its internal structure without changing its external behavior
>
> — <cite>[M. Fowler](http://www.refactoring.com/), www.refactoring.com</cite>
>
> A change made to the internal structure of the software to make it easier to understand and cheaper to modify without changing its observable behavior
>
> – <cite>[M. Fowler (2008): Refactoring...](bibliografia.md#refactoring)</cite>

!!! info "Lectura recomendada"
    Hunt & Thomas. [The Pragmatic Programmer](bibliografia.md#pragmatic), 1999.
    Capítulo: *Refactoring*

### Motivos para refactoring

- Código duplicado
- Rutinas demasiado largas
- Bucles demasiado largos o demasiado anidados
- Clases poco cohesionadas
- Interfaz de una clase con un nivel de abstracción poco consistente
- Demasiados parámetros en una lista de parámetros
- Muchos cambios en una clase tienden a estar compartimentalizados (afectan solo a una parte)
- Muchos cambios requieren modificaciones en paralelo a varias clases
- Hay que cambiar jerarquías de herencia en paralelo
- Hay que cambiar muchas sentencias _case_ en paralelo
- Etc.

!!! info "Lectura recomendada"
    McConnell. [Code Complete](bibliografia.md#codecomplete), 2004.

??? question "¿Cuál es la primera razón para hacer refactoring?"
    - **Código duplicado**

<a id="duplcode"></a>

## <span style="color:blue;">Código duplicado</span>

!!! info "Lectura recomendada"
    Hunt & Thomas. [The Pragmatic Programmer](bibliografia.md#pragmatic), 1999.
    Capítulo *DRY—The Evils of Duplication*

### ¿Por qué no duplicar?

- Mantenimiento
- Cambios (no sólo a nivel de código)
- Trazabilidad

### Causas de la duplicación

- __Impuesta__: No hay elección
- __Inadvertida__: No me he dado cuenta
- __Impaciencia__: No puedo esperar
- __Simultaneidad__: Ha sido otro

### <span style="color:blue;">Principio DRY – *Don't Repeat Yourself!*</span>

by [Hunt & Thomas (1999)](bibliografia.md#pragmatic)

> Copy and paste is a design error
>
> – <cite> McConnell (1998) </cite>

### Duplicación impuesta

La gestión del proyecto así nos lo exige. Algunos ejemplos:

- Representaciones múltiples de la información:
    - un TAD para guardar elementos de distintos tipos;
    - el esquema de una BD configurado en la BD y en el código fuente a través de un [ORM](http://www.agiledata.org/essays/mappingObjects.html)
- Documentación del código:
    - código incrustado en javadocs
- Casos de prueba:
    - pruebas unitarias con jUnit
- Características del lenguaje:
    - C/C++ header files
    - IDL specs

#### Técnicas de solución

- __Generadores de código__: para evitar duplicar representaciones múltiples de la información
- Herramientas de __ingeniería inversa__: para generar código a partir de un esquema de BD – v.g. [jeddict](https://jeddict.github.io/) para crear clases JPA, visualizar y modificar BDs y automatizar la generación de código Java EE.
- __Plantillas__: Tipos genéricos del lenguaje (Java, C++, TypeScript, etc.) o mediante un motor de plantillas – v.g. [Apache Velocity](http://velocity.apache.org/) template language ([VTL](http://velocity.apache.org/engine/2.0/user-guide.html#velocity-template-language-vtl-an-introduction))
- __Metadatos__: Anotaciones @ en Java, decoradores en TypeScript, etc.
- Herramientas de __documentación__ (v.g. [asciidoctor](http://asciidoctor.org/): [inclusión de ficheros](http://asciidoctor.org/docs/asciidoc-syntax-quick-reference/#include-files) y [formateo de código fuente](http://asciidoctor.org/docs/asciidoc-syntax-quick-reference/#source-code)).
- Herramientas de __[programación literaria](http://www.literateprogramming.com/)__
- Ayuda del __IDE__

### Duplicación inadvertida

Normalmente tiene origen en un diseño inapropiado.

Fuente de numerosos problemas de integración.

#### Ejemplo: código duplicado – versión 1

```java
  public class Line {
    public Point start;
    public Point end;
    public double length;
  }
``` 

<span style="color:red;">¿Dónde está la duplicación?</span>

Realmente `length` ya está definido con `start`y `end`. <span style="color:red;">¿Mejor así...?</span>

```java
  public class Line {
    public Point start;
    public Point end;
    public double length() {
       return start.distanceTo(end);
    }
  }
```  

<span style="color:red;">¿Es conveniente aplicar siempre DRY?</span>

A veces se puede optar por violar DRY por razones de rendimiento.

#### Ejemplo: aplicando memoization – versión 2

[_Memoization_](https://en.wikipedia.org/wiki/Memoization): cachear los resultados de cómputos costosos

```java
  public class Line {
    private boolean changed;
    private double length;
    private Point start;
    private Point end;

    public void setStart(Point p) { start = p; changed = true; }
    public void setEnd(Point p)   { end   = p; changed = true; }
    public Point getStart() { return start; }
    public Point getEnd() { return end; }
    public double getLength() {
       if (changed) {
          length = start.distanceTo(end);
          changed = false;
       }
       return length;
    }
  }
```

La técnica de memoization es menos problemática si queda dentro de los límites de la clase/módulo.

Otras veces no merece la pena violar DRY por rendimiento: ¡las cachés y los optimizadores de código también hacen su labor!

### <span style="color:blue;">Principio de acceso uniforme</span>

> All services offered by a module should be available through a uniform notation, which does not betray whether they are implemented through storage or through computation
>
> <cite>[B. Meyer](bibliografia.md#meyer)</cite>

Conviene aplicar el principio de acceso uniforme para que sea más fácil añadir mejoras de rendimiento (v.g. caching)

#### Ejemplo: acceso uniforme en C# – versión 3

```csharp
public class Line {
  private Point Start;
  private Point End;
  private double Length;

  public Point Start {
    get { return Start; }
    set { Start = value; }
  }

  public Point End {
    get { return End; }
    set { Start = value; }
  }

  public double Length {
    get { return Start.distanceTo(End); }
  }
}
```

### Duplicación por impaciencia

- Los peligros del *copy&paste*
- "Vísteme despacio que tengo prisa" (_shortcuts make for long delays_). Ejemplos:
    - Meter el `main` de Java en cualquier clase
    - Fiasco del año 2000

### Duplicación por simultaneidad

- No resoluble a nivel de técnicas de construcción
- Hace falta metodología, gestión de equipos + herramientas de comunicación

<a id="ortogonalidad"></a>

## <span style="color:blue;">Ortogonalidad</span>

Dos componentes A y B son ortogonales ($A \perp B$) si los cambios en uno no afectan al otro. Suponen más independencia y menos acoplamiento. Por ejemplo:

- La base de datos debe ser ortogonal a la interfaz de usuario
- En un helicóptero, los mandos de control no suelen ser ortogonales

### Beneficios de la ortogonalidad

#### Mayor productividad

- Es más fácil escribir un componente pequeño y auto-contenido que un bloque muy grande de código. El tiempo de desarrollo y __pruebas__ se reduce
- Se pueden combinar unos componentes con otros más fácilmente. Mayor __reutilización__.
- Si $A \perp B$, el componente A sirve para $m$ propósitos y B sirve para $n$, entonces $A \cup B$ sirve para $m \times n$ propósitos.
- La falta de cohesión perjudica la reutilización – v.g. ¿y si hay que hacer una nueva versión gráfica de una aplicación de línea de comandos? (los `System.out.println` pueden descohesionar)

#### Menor riesgo

- Defectos aislados, más fáciles de arreglar
- Menor __fragilidad__ del sistema global, los problemas provocados por cambios en un área se limitan a ese área
- Más fácil de __probar__, pues será más fácil construir pruebas individuales de cada uno de sus componentes (e.g. _mocking_ es más sencillo)

### Niveles de aplicación de la ortogonalizad

La ortogonalidad es aplicable a:

- la gestión de proyectos
- el diseño
- la codificación
- las pruebas
- la documentación

A nivel de _diseño_, los patrones de diseño y las arquitecturas como MVC facilitan la construcción de componentes ortogonales.

### Técnicas de codificación

Técnicas de codificación para fomentar la ortogonalidad:

- Hacer **refactoring**
- Codificar **patrones** de diseño: strategy, template method, etc.
- Evitar datos globales y __singletons__: ¿qué pasaría si hubiera que hacer una versión *multithreaded* de una aplicación?
- **Inyectar**: pasar explícitamente el contexto (dependencia) como parámetro a los constructores
- Usar **anotaciones** (Java), decoradores (JavaScript) o atributos (C#)
- **Desacoplar**: Ley de <span>*Demeter*</span>—No hables con extraños
- Usar programación orientada a **aspectos**

#### Desacoplar - ley de Demeter

Al pedir un servicio a un objeto, el servicio debe ser realizado de parte nuestra, no que nos devuelva un tercero con el que tratar para realizarlo

__Ejemplo__:

```java
  public boolean canWrite(User user) {
    if (user.isAnonymous())
      return false;
    else {
      return user.getGroup().hasPermission(Permission.WRITE);
    }
  }
```

Refactorización: definir un método `User.hasPermission()`

#### Inyectar el contexto

Pasar explícitamente el contexto (dependencia) como parámetro a los constructores de la clase

##### Ejemplo: patrón estrategia

En el patrón de diseño _strategy_, pasar el contexto a la estrategia en su creación

##### Ejemplo: caballeros de la mesa redonda

```java
public interface Knight {
  Object embarkOnQuest() throws QuestFailedException;
}

public class KnightOfTheRoundTable implements Knight {
  private String name;
  private Quest quest;
  public KnightOfTheRoundTable(String name, Quest quest) {
    this.name = name;
    this.quest = quest;
  }
  public Object embarkOnQuest() throws QuestFailedException {
    return quest.embark();
  }
  public void setQuest(Quest quest) {
    this.quest = quest;
  }
}

public interface Quest {
  abstract Object embark()
    throws QuestFailedException;
}
```

#### Ley de Demeter para funciones

Los métodos de un objeto solo deben hacer llamadas a métodos...

caso 1. __propios__ 
caso 2. de objetos pasados como __parámetros__
caso 3. de objetos __creados__ por ellos mismos
caso 4. de objetos __declarados__ en el mismo método

```java
class Demeter {
  private A a;
  private int func();
  public void example (B b);

  void example(B b) {
    C c;
    int f = func();  // (caso 1)
    b.invert();      // (caso 2)
    a = new A();
    a.setActive();   // (caso 3)
    c.print();       // (caso 4)
}
```

#### Críticas a la ley de Demeter

 La ley de Demeter, ¿realmente ayuda a crear código más mantenible?

##### Ejemplo: pintar gráficos de grabadoras

- Pintar un gráfico con los datos registrados por una serie de grabadoras (`Recorder`) dispersas por el mundo.
- Cada grabadora está en una ubicación (`Location`), que tiene una zona horaria (`TimeZone`).
- Los usuarios seleccionan (`Selection`) una grabadora y pintan sus datos etiquetados con la zona horaria correcta...

  ```java
  public void plotDate(Date aDate, Selection aSelection) {
    TimeZone tz = aSelection.getRecorder().getLocation().getZone();
  }
  ```

##### Críticas

- Multiplicidad de dependencias: `plotDate` $\dashrightarrow$ `Selection`, `Recorder`, `Location`, `TimeZone`.
- Si cambia la implementación de `Location` de forma que ya no incluye directamente una `TimeZone`, hay que cambiar `plotDate`
- Añadir un método *delegado* `getTimeZone` a `Selection`. Así `plotDate` no se entera de si la `TimeZone` le llega desde `Recorder` o desde un objeto contenido en `Recorder`.

  ```java
  public void plotDate(Date aDate, TimeZone tz) {
    /* ... */
  }
  plotDate(someDate, someSelection.getTimeZone());
  ```
  Ahora `plotDate` $\dashrightarrow$ `Selection`, `TimeZone`, pero se han eliminado las restantes dependencias.
- Costes de espacio y ejecución de métodos *wrapper* que reenvían la petición al objeto delegado: violar la ley de Demeter para mejorar el __rendimiento__
- Otros ejemplos de mejora del rendimiento: desnormalización de BBDD

## Toolkits y bibliotecas

- Usar metadatos (@tag) para propósitos específicos – v.g. persistencia de objetos, transacciones, etc.
- [Aspect-Oriented Programming (AOP)](iiss-aop.md)

!!! warning "Ver ahora el capítulo [Aspectos](iiss-aop.md)"

!!! warning "Ver luego el capítulo [Contratos](iiss-dbc.md)"

## <span style="color:blue;">Errores y Excepciones</span>

<a id="errores"></a>

### Tratamiento de errores

#### Códigos de error

Un ejemplo habitual de tratamiento de errores con __códigos de error__:

```java
if (deletePage(page) == E_OK) {
  if (registry.deleteReference(page.name) == E_OK) {
    if (configKeys.deleteKey(page.name.makeKey()) == E_OK){
      logger.log("page deleted");
    } else {
      logger.log("configKey not deleted");
    }
  } else {
    logger.log("deleteReference from registry failed");
  }
} else {
  logger.log("delete failed");
  return E_ERROR;
}
```

Con esta técnica creamos _imanes de dependencias_:

```java
public enum Error {
  OK,
  INVALID,
  NO_SUCH,
  LOCKED,
  OUT_OF_RESOURCES,
  WAITING_FOR_EVENT;
}
```

Los programadores intentan evitar añadir nuevos motivos de error, porque eso significa tener que volver a compilar y desplegar todo el código.

#### Excepciones

Usar __excepciones__ en lugar de códigos de error:

```java
try {
  deletePage(page);
  registry.deleteReference(page.name);
  configKeys.deleteKey(page.name.makeKey());
}
catch (Exception e) {
  logger.log(e.getMessage());
}
```

¿No queda más claro?

Ventaja: las nuevas excepciones son derivadas de una clase base `Exception`, lo que facilita la definición de nuevos motivos de error.

<span style="color:red;">¿Dónde se produce el error?</span>

#### Separar la función y el tratamiento de errores

```java
public void delete(Page page) {
  try {
    deletePageAndAllReferences(page);
  }
  catch (Exception e) {
    logError(e);
  }
}

private void deletePageAndAllReferences(Page page) throws Exception {
  deletePage(page);
  registry.deleteReference(page.name);
  configKeys.deleteKey(page.name.makeKey());
}

private void logError(Exception e) {
  logger.log(e.getMessage());
}
```

¿No queda más fácil de comprender, modificar y depurar?

### Excepciones en Java

- **Checked**: instancias de clases derivadas de `java.lang.Throwable` (menos `RuntimeException`). Deben declararse en el método mediante `throws` y obligan al llamador a tratar la excepción.

- **Unchecked**: instancias de clases derivadas de `java.lang.RuntimeException`. No se declaran en el método y no obligan al llamador a tratar la excepción.

Elevar una excepción `e` implica:

- Deshacer (_roll back_) la llamada a un método
- hasta que se encuentre un bloque catch para el tipo de `e`
- y, si no se encuentra, la excepción es capturada por la JVM, que detiene el programa

#### Tratamiento de excepciones en Java

```java
  try {
      /* guarded region that can send
        IOException or Exception */
  }
  catch (IOException e) {
      /* decide what to do when an IOException
        or a sub-class of IOException occurs */
  }
  catch (Exception e) {
      // Treats any other exceptions
  }
  finally {
      // in all cases execute this
  }
```

### Recomendaciones

Incluir el __contexto__ de la ejecución:

- Incluir información suficiente con cada excepción para determinar el motivo y la ubicación de un error
- No basta con el *stack trace*
- Escribir mensajes informativos: operación fallida y tipo de fallo

Usar solamente excepciones __unchecked__

- C\#, C++, Python o Ruby no ofrecen excepciones _checked_.
- Los beneficios de las checked en Java son mínimos
- Se paga el precio de violar el principio OCP (Open-Closed Principle): si lanzamos una excepción _checked_ desde un método y el `catch` está tres niveles por encima, hay que declarar la excepción en la signatura de todos los métodos que van entre medias. Esto significa que un cambio en un nivel bajo del software puede forzar cambios en niveles altos

### Transformación de excepciones

#### Checked vs unchecked

Muchas APIs de Java lanzan excepciones checked cuando deberían ser unchecked

__Ejemplo__: Al ejecutar una consulta mediante `executeQuery` en el API de JDBC se lanza una excepción `java.sql.SQLException` (de tipo checked) si la SQL es errónea.

##### Solución

Transformar las excepciones checked en unchecked:

```java
  try {
    // Codigo que genera la excepcion checked
  } catch (Exception ex) {
    throw new RuntimeException("Unchecked exception", ex)
  }
```

#### Excepciones encapsuladas

Criticar la siguiente implementación:

```java
  ACMEPort port = new ACMEPort(12);
  try {
    port.open();
  } catch (DeviceResponseException e) {
    reportPortError(e);
    logger.log("Device response exception", e);
  } catch (ATM1212UnlockedException e) {
    reportPortError(e);
    logger.log("Unlock exception", e);
  } catch (GMXError e) {
    reportPortError(e);
    logger.log("Device response exception");
  } finally {
    ...
  }
```

Excesiva duplicación de código: llamada a `reportPortError()`

Excepción encapsulada:

```java
    LocalPort port = new LocalPort(12);
    try {
      port.open();
    } catch (PortDeviceFailure e) {
      reportPortError(e);
      logger.log(e.getMessage(), e);
    } finally {
      ...
    }

    public class LocalPort {
      private ACMEPort innerPort;
      public LocalPort(int portNumber) {
        innerPort = new ACMEPort(portNumber);
      }
      public void open() throws PortDeviceFailure {
        try {
          innerPort.open();
        } catch (DeviceResponseException e) {
          throw new PortDeviceFailure(e);
        } catch (ATM1212UnlockedException e) {
          throw new PortDeviceFailure(e);
        } catch (GMXError e) {
          throw new PortDeviceFailure(e);
        }
      }
      ...
    }
```

- La encapsulación de excepciones es recomendable cuando se usa un API de terceros, para minimizar las dependencias con respecto al API elegido. 
- También facilitan la implementación de _mocks_ del componente que proporciona el API para construir pruebas.

### Las excepciones son excepcionales

__Recomendación de uso__: Usar excepciones para problemas excepcionales (eventos inesperados)

__Ejemplo__: <span style="color:red;">¿Usar excepciones cuando se intenta abrir un fichero para leer y el fichero no existe?</span>

Depende de si el fichero debe estar ahí

- Usando excepciones:

  ```java
  public void open_passwd() throws FileNotFoundException {
    // This may throw FileNotFoundException...
    ipstream = new FileInputStream("/etc/passwd");
    // ...
  }
  ```

- Sin usar excepciones:

  ```java
  public boolean open_user_file(String name)
      throws FileNotFoundException {
    File f = new File(name);
    if (!f.exists())
      return false;
    ipstream = new FileInputStream(f);
    return true;
  }
  ```

<a id="null"></a>

### Uso de null

Obtener un _null_ cuando no se espera puede ser un quebradero de cabeza para el tratamiento de errores

#### Principio general: no devolver null

Este código puede parecer inofensivo, pero es maligno:

```java
public void registerItem(Item item) {
  if (item != null) {
    ItemRegistry registry = peristentStore.getItemRegistry();
    if (registry != null) {
      Item existing = registry.getItem(item.getID());
      if (existing.getBillingPeriod().hasRetailOwner()) {
        existing.register(item);
      }
    }
  }
}
```

¿Qué pasa si `persistentStore` es null?

- Peligro de `NullPointerException`
- ¿Se nos ha olvidado añadir un `if null`?
- El problema no es que se haya olvidado uno, sino que hay demasiados
- En su lugar, elevar una excepción o devolver un objeto *especial*

#### No devolver null

Evitar:

```java
List<Employee> employees = getEmployees();
if (employees != null) {
  for(Employee e : employees) {
    totalPay += e.getPay();
  }
}
```

Mejor así:

```java
List<Employee> employees = getEmployees();
for(Employee e : employees) {
  totalPay += e.getPay();
}

public List<Employee> getEmployees() {
  if( /* there are no employees */ )
    return Collections.emptyList();
}
```

#### No pasar valores null

```java
public class MetricsCalculator
{
  public double xProjection(Point p1, Point p2) {
  return (p2.x - p1.x) * 1.5;
}
```

<span style="color:red;">¿Qué sucede si llamamos a `xProjection()` así...?</span>

```java
  calculator.xProjection(null, new Point(12, 13))
```

Devolver null es malo, pero ¡pasar un valor null es peor!

<span style="color:red;">¿Es mejor así...?</span>

```java
public class MetricsCalculator
{
  public double xProjection(Point p1, Point p2) {
    if (p1 == null || p2 == null) {
      throw InvalidArgumentException(
               "Invalid argument for MetricsCalculator.xProjection");
    }
    return (p2.x - p1.x) * 1.5;
  }
}
```

¿Qué curso de acción tomar ante un `InvalidArgumentException`? ¿Hay alguno?

##### Alternativa con aserciones

(*solo para JDK $\geq$ 5.0*)

```java
public class MetricsCalculator
{
  public double xProjection(Point p1, Point p2) {
    assert p1 != null : "p1 should not be null";
    assert p2 != null : "p2 should not be null";
    return (p2.x - p1.x) * 1.5;
  }
}
```

Es una buena forma de documentar, pero no resuelve el problema

### Optionals

- En la mayoría de lenguajes no hay forma satisfactoria de tratar con _nulls_ pasados como argumento accidentalmente.
- Para eso están los _options_ u _optionals_, disponibles actualmente en muchos languajes:
    - [Scala `Option`](https://www.tutorialspoint.com/scala/scala_options.htm)
    - [Java 8](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html) `java.util.Optional`
    - C++17 `std::optional`
- [TypeScript recomienda usar `undefined`](https://github.com/Microsoft/TypeScript/wiki/Coding-guidelines#null-and-undefined) (algo que no se ha inicializado) en lugar de `null` (algo que no está disponible)

#### [Scala Options](https://www.tutorialspoint.com/scala/scala_options.htm)

En Scala, `Option[T]` es un contenedor de un valor opcional de tipo T.

- Si el valor de tipo T está presente, `Option[T]` es una intancia de `Some[T]` que contiene el valor presente de tipo T.
- Si el valor está ausente, `Option[T]` es el objeto `None`.

```scala
object Demo {
   def main(args: Array[String]) {
      val a:Option[Int] = Some(5)
      val b:Option[Int] = None

      println("a.isEmpty: " + a.isEmpty )  //false
      println("b.isEmpty: " + b.isEmpty )  //true
   }
}
```

```scala
object Demo {
   def main(args: Array[String]) {
      val capitals = Map("France" -> "Paris", "Japan" -> "Tokyo")

      println("show(capitals.get( \"Japan\")) : " + show(capitals.get( "Japan")) )
      println("show(capitals.get( \"India\")) : " + show(capitals.get( "India")) )
   }

   def show(x: Option[String]) = x match {
      case Some(s) => s
      case None => "?"
   }
}
```

#### Java 8 Optionals

From [Java 8 Optional in Depth](https://www.mkyong.com/java8/java-8-optional-in-depth/)

##### Ejemplo sin `Optional`

Programa de prueba:

```java
public class MobileTesterWithoutOptional {
  public static void main(String[] args) {
    ScreenResolution resolution = new ScreenResolution(750,1334);
    DisplayFeatures dfeatures = new DisplayFeatures("4.7", resolution);
    Mobile mobile = new Mobile(2015001, "Apple", "iPhone 6s", dfeatures);

    MobileService mService = new MobileService();

    int mobileWidth = mService.getMobileScreenWidth(mobile);
    System.out.println("Apple iPhone 6s Screen Width = " + mobileWidth);

    ScreenResolution resolution2 = new ScreenResolution(0,0);
    DisplayFeatures dfeatures2 = new DisplayFeatures("0", resolution2);
    Mobile mobile2 = new Mobile(2015001, "Apple", "iPhone 6s", dfeatures2);
    int mobileWidth2 = mService.getMobileScreenWidth(mobile2);
    System.out.println("Apple iPhone 16s Screen Width = " + mobileWidth2);
  }
}
```

Dependencias: `MobileService` $\dashrightarrow$ `DisplayFeatures`, `ScreenResolution`

Cantidad de código _boilerplate_ para comprobar los nulos en la clase principal:

```java
public class MobileService {
  public int getMobileScreenWidth(Mobile mobile){
    if(mobile != null){
      DisplayFeatures dfeatures = mobile.getDisplayFeatures();
      if(dfeatures != null){
        ScreenResolution resolution = dfeatures.getResolution();
        if(resolution != null){
          return resolution.getWidth();
        }
      }
    }
    return 0;
  }
}
```

Clases de utilidad:

```java
public class ScreenResolution {
  private int width;
  private int height;

  public ScreenResolution(int width, int height){
    this.width = width;
    this.height = height;
  }
  public int getWidth() {
    return width;
  }
  public int getHeight() {
    return height;
  }
}

public class DisplayFeatures {
  private String size; // In inches
  private ScreenResolution resolution;

  public DisplayFeatures(String size, ScreenResolution resolution){
    this.size = size;
    this.resolution = resolution;
  }
  public String getSize() {
    return size;
  }
  public ScreenResolution getResolution() {
    return resolution;
  }
}

public class Mobile {
  private long id;
  private String brand;
  private String name;
  private DisplayFeatures displayFeatures;
  // Likewise we can see Memory Features, Camera Features etc.

  public Mobile(long id, String brand, String name, DisplayFeatures displayFeatures){
    this.id = id;
    this.brand = brand;
    this.name = name;
    this.displayFeatures = displayFeatures;
  }
  public long getId() {
    return id;
  }
  public String getBrand() {
    return brand;
  }
  public String getName() {
    return name;
  }
  public DisplayFeatures getDisplayFeatures() {
    return displayFeatures;
  }
}
```

##### Ejemplo con `Optionals`

Uso de métodos de `Optional` en el programa de prueba:

```java
public class MobileTesterWithOptional {
  public static void main(String[] args) {
    ScreenResolution resolution = new ScreenResolution(750,1334);
    DisplayFeatures dfeatures = new DisplayFeatures("4.7", Optional.of(resolution));
    Mobile mobile = new Mobile(2015001, "Apple", "iPhone 6s", Optional.of(dfeatures));

    MobileService mService = new MobileService();

    int width = mService.getMobileScreenWidth(Optional.of(mobile));
    System.out.println("Apple iPhone 6s Screen Width = " + width);

    Mobile mobile2 = new Mobile(2015001, "Apple", "iPhone 6s", Optional.empty());
    int width2 = mService.getMobileScreenWidth(Optional.of(mobile2));
    System.out.println("Apple iPhone 16s Screen Width = " + width2);
  }
}
```

Menos código _boilerplate_ en la clase principal:

```java
public class MobileService {
  public Integer getMobileScreenWidth(Optional<Mobile> mobile){
    return mobile.flatMap(Mobile::getDisplayFeatures)
      .flatMap(DisplayFeatures::getResolution)
      .map(ScreenResolution::getWidth)
      .orElse(0);
  }
}
```


Clases de utilidad:

```java
import java.util.Optional;

public class DisplayFeatures {
  private String size; // In inches
  private Optional<ScreenResolution> resolution;
  public DisplayFeatures(String size, Optional<ScreenResolution> resolution){
    this.size = size;
    this.resolution = resolution;
  }
  public String getSize() {
    return size;
  }
  public Optional<ScreenResolution> getResolution() {
    return resolution;
  }
}

public class Mobile {
  private long id;
  private String brand;
  private String name;
  private Optional<DisplayFeatures> displayFeatures;
  // Like wise we can see MemoryFeatures, CameraFeatures etc.
  // For simplicity, using only one Features
  public Mobile(long id, String brand, String name, Optional<DisplayFeatures> displayFeatures){
    this.id = id;
    this.brand = brand;
    this.name = name;
    this.displayFeatures = displayFeatures;
  }
  public long getId() {
    return id;
  }
  public String getBrand() {
    return brand;
  }
  public String getName() {
    return name;
  }
  public Optional<DisplayFeatures> getDisplayFeatures() {
    return displayFeatures;
  }
}
```

### Fronteras

Tensión proveedor-cliente

- Los proveedores de packages y frameworks quieren amplia aplicabilidad
- Los clientes quieren una interfaz centrada en sus necesidades particulares

__Ejemplo__: La interfaz [`java.util.Map`](http://docs.oracle.com/javase/6/docs/api/java/util/Map.html)

```java
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

Construimos un `Map` y lo pasamos.

- Diseño A: Ninguno de los receptores deberá poder borrar algo del map. ¡Pero hay un `clear()`!
- Diseño B: solo algunos tipos de objetos deben poderse guardar. ¡Los tipos no están restringidos!

¿La interfaz `Map` es siempre satisfactoria? ¿seguro que no va a cambiar?

- JDK < 5.0:

  ```java
  Map sensors = new HashMap();
  ...
  Sensor s = (Sensor)sensors.get(sensorId);
  ```

- JDK $\geq$ 5.0:

  ```java
  Map<Sensor> sensors = new HashMap<Sensor>();
  ...
  Sensor s = sensors.get(sensorId);
  ```

__Conclusión__: Map<Sensor> ofrece más de lo que necesitamos

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

__Interfaces de frontera__: No todo uso de `Map` o interfaz de
frontera debe quedar encapsulado. Sólo es un consejo para no ’pasarla’
con métodos que no vamos a necesitar.
