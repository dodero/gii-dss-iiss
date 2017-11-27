# Construcción de Sistemas Software

## Problemáticas

- <span style="color:blue;">Variabilidad</span>
- <span style="color:blue;">Acoplamiento</span>
- <span style="color:blue;">Complejidad</span>
- <span style="color:blue;">Robustez</span>
- Reutilización
- Flexibilidad

## Principios

- Ocultación
- Cohesión
- Ortogonalidad
- Delegación

## Técnicas

- [Herencia](#herencia)
- [Polimorfismo](#polimorfismo)
- [Composición](#compisicion)
- [Inyección de dependencias](#inyeccion)
- [Refactoring](#refactoring)
  - [Código duplicado](#duplcode)
    - [Ortogonalidad y dependencias](#ortogonalidad)
- Calidad
  - [Aserciones](#assert)
  - [Contratos](#contracts)
  - [Errores y excepciones](#errores)
  - Depuración
- Mixins
- Anónimos y cierres
- Reflexión
- Metaprogramación

## Paradigmas

- Objetos
- Eventos
- Funcional
- Aspectos
- Contratos

## Casos prácticos

1. Ocultación de la implementación - [Recorrido de una lista](#recorridolista)
2. Delegación - [Implementación de una orquesta](#orquesta)
3. Inyección de dependencias - [Caballeros de la mesa redonda](#knights)
4. Código duplicado - [Cálculo de nóminas](#nominas)
5. Ortogonalidad con aspectos - [Editor de figuras](#aspectos)

# Caso 1 - Ocultación de la implementación

<a id="recorridolista"></a>

## Recorrido de una lista

### Versión inicial: Lista v0.1

__Abstracción__: La clase abstracta `List<T>`diferencia entre el *qué* y el *cómo*: Qué hace la lista vs. cómo se almacenan los elementos

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
> -- <cite>[E. Yourdon & L. Constantine](bibliografia.html#yourdon)</cite>

#### Críticas a Lista v0.1

- `List<T>` aglutina más de una responsabilidad: almacenar y recorrer. Implementación no cohesionada
- ¿Y si hay distintas implementaciones de `traverse()`? Si implementamos varias versiones de la lista, introducimos más dependencias (acoplamiento)

#### Problemáticas de Lista v0.1

- Baja __cohesión__
- Alta __variabilidad__ no bien tratada $\Rightarrow$ poca __flexibilidad__

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
- Alta __variabilidad__ no bien tratada $\Rightarrow$ poca __flexibilidad__, mala __reutilización__

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

- Muchas __dependencias__ $\Rightarrow$ __acoplamiento__
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
> -- <cite>[Eric Yourdon](bibliografia.html#yourdon)</cite>

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

## <span style="color:blue">Herencia: generalización y especialización</span>

<a id="herencia"></a>

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
> -- <cite>[Bruce Eckel](bibliografia.html#eckel)</cite>

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

- ¿De qué tipos van a ser los personales de acción? $\Rightarrow$ problema de _downcasting_
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

## <span style="color:blue">Polimorfismo</span>

<a id="polimorfismo"></a>

Fenómeno por el que, cuando se llama a una operación de un objeto del que no se sabe su tipo específico, se ejecuta el método adecuado de acuerdo con su tipo.

El polimorfismo se basa en:

- **Enlace dinámico**: se elige el método a ejecutar en tiempo de ejecución, en función de la clase de objeto; es la implementación del *polimorfismo*

- **Moldes (_casting_)**
  - *Upcasting*: Interpretar un objeto de una clase derivada como del mismo tipo que la clase base
  - *Downcasting*: Interpretar un objeto de una clase base como del
        mismo tipo que una clase derivada suya

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

## <span style="color:blue">Composición</span>

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

- Transitividad: `(x.compareTo(y)>0 and y.compareTo(z)>0)` $\Rightarrow$ `x.compareTo(z)>0`

- `x.compareTo(y)=0`$\Rightarrow$`sgn(x.compareTo(z))=sgn(y.compareTo(z))` $\forall$ `z`

- Consistencia con `equals` (no obligatoria): `(x.compareTo(y)=0)`$\Leftrightarrow$`(x.equals(y))`

Cuando una clase hereda de una clase concreta que implementa `Comparable` y le añade un campo significativo para la comparación, no se puede construir una implementación correcta de `compareTo`. La única alternativa entonces es la composición en lugar de la herencia.

Una alternativa a implementar `Comparable` es pasar un `Comparator` como parámetro (se prefiere __composición__ frente a __herencia__).

##### Implementación en Java 1.5

- Utilizando _templates_
- Delegar en `compareTo` y `equals` del tipo de id _envuelto_ (e.g. `String`)

```java
import java.util.*;
import java.io.*;

public final class BankAccount implements Comparable<BankAccount> {
  private final String id;
  public BankAccount (String number)  {
    this.id = number;
  }
  String getId() { return id; }
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
    return  ( this.id.equals((that.getId()) );
   }
  @Override
  public int hashCode() {
    int result = HashCodeUtil.SEED;
    result = HashCodeUtil.hash( result, id );
    return result;
   }
  @Override
  public String toString() {
    return id.toString();
  }
}
```

##### Implementación en Java 1.4

- No hay plantillas. La genericidad se consigue con `Object`. Hay que hacer casting.
- Cuidado con `Boolean` que no implementa `Comparable` en JDK 1.4

```java
import java.util.*;
import java.io.*;

public final class BankAccount implements Comparable {
    private final String id;
    public BankAccount (String number)  {
      this.id = number;
    }
    String getId() { return id; }
    public int compareTo(Object other) {
      if (this == other) return 0;
      BankAccount that = (BankAccount)other;
      assert this.equals(that) : "compareTo inconsistent with equals.";
      return this.id.compareTo(that.getId());
    }
  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof BankAccount)) return false;
    that = (BankAccount)other:
    return  ( this.id.equals(that.getId()) );
   }
  public int hashCode() {
    int result = HashCodeUtil.SEED;
    result = HashCodeUtil.hash( result, id );
    return result;
   }
   public String toString() {
      return id.toString();
   }
}
```

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

-   El caballero no es el responsable de averiguar su misión.
-   El caballero sólo sabe de su misión a través de la interfaz `Quest`.
-   El caballero recibe la misión (se le inyecta) a través de `setQuest()`
-   Puede asignársele cualquier implementación de `Quest`
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
    BeanFactory factory =
      new XmlBeanFactory(new FileInputStream("knight.xml"));
    KnightOfTheRoundTable knight =
      (KnightOfTheRoundTable) factory.getBean("knight");
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

Un _contenedor de dependencias_ en el framework debe responsabilizarse de crear las instancias de `Logger` e inyectarlas en su sitio (normalmente vía _reflexión_ o _introspección_)

### Dependencias en Java

Estándar de Java (JSR 330) para describir las dependencias de una clase con anotaciones

```java
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

### Decoradores en TypeScript

Los decoradores de TypeScript son una forma de modificar programáticamente la definición de una clase

La definición de una clase describe la _forma_ de la clase, es decir, sus métodos y propiedades. Sólo cuando se instancie la clase, estas propiedades y métodos estarán disponibles.


Los __decoradores__ permiten inyectar código en la definición real de una clase.

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

¿Cuál es la salida del siguiente código TypeScript?

```typescript
let instance_1 = new ClassWithSimpleDecorator();
let instance_2 = new ClassWithSimpleDecorator();
console.log(`instance_1: ${instance_1}`);
console.log(`instance_2 : ${instance_2}`);
```

```text
simpleDecorator called.
instance_1 : [object Object]
instance_2 : [object Object]
```

#### Decoradores mútiples

¿Cuál es la salida del siguiente código TypeScript?

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

```text
secondDecorator called.
simpleDecorator called.
instance_1 : [object Object]
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

# Caso 4 - Código duplicado

## Cálculo de nóminas

<a id="nominas"></a>

### Implementación de nóminas v0.1

- ¿Dónde hay código duplicado?

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

- Código duplicado en los constructores de las clases y subclases
- Refactorizar delegando hacia la superclase

### Nóminas v0.2

- Requisito: los trabajadores autónomos cobran por horas (no tienen un salario fijo bruto)
- Incluimos el método `computeMonthlySalary` para el cálculo de la nómina mensual
- ¿Están descohesionadas las clases?

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

- ¿Todos los empleados deben tener un salario anual bruto? Los autónomos no...
- El método de cálculo del salario está descohesionado


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

## <span style="color:blue;">Refactoring</span>

<a id="refactoring"></a>

> Refactoring is a disciplined technique for restructuring an existing body of code, altering its internal structure without changing its external behavior [@Refactoring]
>
> —- <cite>[M. Fowler](www.refactoring.com), www.refactoring.com</cite>

- Pequeñas transformaciones
- Mantienen el sistema funcional

## <span style="color:blue;">Código duplicado</span>

<a id="duplcode"></a>
###¿Por qué no duplicar?

- Mantenimiento
- Cambios (no sólo a nivel de código)
- Trazabilidad

### Causas de la duplicación

- __Impuesta__: No hay elección
- __Inadvertida__: No me he dado cuenta
- __Impaciencia__: No puedo esperar
- __Simultaneidad__: Ha sido otro

### <span style="color:blue;">Principio DRY – *Don't Repeat Yourself!*</span>

### Duplicación impuesta

La gestión del proyecto así nos lo exige:

- Representaciones múltiples de la información – v.g. un TAD para guardar elementos de distintos tipos; el esquema de una BD configurado en la BD y en el código fuente a través de un [ORM](http://www.agiledata.org/essays/mappingObjects.html)
- Documentación del código – v.g. código incrustado en javadocs
- Casos de prueba
- Características del lenguaje (v.g. C/C++ header files, IDL specs)

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

¿Dónde está la duplicación?

```java
  public class Line {
    public Point start;
    public Point end;
    public double length() {
       return start.distanceTo(end);
    }
  }
```  

¿Es conveniente aplicar siempre DRY?

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
> <cite>[B. Meyer](bibliografia.html#meyer)</cite>

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

## <span style="color:blue;">Ortogonalidad</span>

<a id="ortogonalidad"></a>

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

- Defectos aislados. Menor __fragilidad__ del sistema global
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

- Evitar datos globales y _singletons_: v.g. ¿y si hay que hacer una versión *multithreaded* de una aplicación?
- Usar métodos plantilla y estrategias — Aplicar DRY
- Desacoplar: Ley de <span>*Demeter*</span>—No hables con extraños
- Inyectar: pasar explícitamente el contexto (dependencia) como parámetro a los constructores

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

# Caso 5 - Ortogonalidad con aspectos

<a id="aspectos"></a>

## Toolkits y bibliotecas

- Usar metadatos (@tag) para propósitos específicos – v.g. persistencia de objetos, transacciones, etc.
- Aspect-Oriented Programming (AOP)

## Editor de figuras

### Ejemplo: editor de figuras

```java
class Line implements FigureElement{
  private Point p1, p2;

  Point getP1() { return p1; }
  Point getP2() { return p2; }

  void setP1(Point p1) { this.p1 = p1; }
  void setP2(Point p2) { this.p2 = p2; }
}

class Point implements FigureElement {
  private int x = 0, y = 0;

  int getX() { return x; }
  int getY() { return y; }

  void setX(int x) { this.x = x; }
  void setY(int y) { this.y = y; }
}
```

Hay que actualizar la pantalla tras mover los objetos

![figuras en pantalla](./figuras/aspectj-1.png)

Hay una colección de figuras que cambian periódicamente. Se deben monitorizar los cambios para refrescar el display.

```java
class Line {
  private Point p1, p2;

  Point getP1() { return p1; }
  Point getP2() { return p2; }

  void setP1(Point p1) {
    this.p1 = p1;
  }
  void setP2(Point p2) {
    this.p2 = p2;
  }
}

class Point {
  private int x = 0, y= 0;

  int getX() { return x; }
  int getY() { return y; }

  void setX(int x) {
    this.x = x;
  }
  void setY(int y) {
    this.y = y;
  }
}
```

Implementamos `MoveTracking`.  ¿Qué dependencias aparecen?

- `Line` $\dashrightarrow$ `MoveTracking`
- `Point` $\dashrightarrow$ `MoveTracking`

### Implementación sin aspectos

#### Versión 1 sin aspectos

Solo detecta el cambio de los extremos de una línea.

`Line` $\dashrightarrow$ `MoveTracking`

```java
class Line {
  private Point p1, p2;

  Point getP1() { return _p1; }
  Point getP2() { return _p2; }

  void setP1(Point p1) {
    this.p1 = p1;
    MoveTracking.setFlag(); // añadido
  }
  void setP2(Point p2) {
    this.p2 = p2;
    MoveTracking.setFlag(); // añadido
  }
}

class Point {
  private int x = 0, y= 0;

  int getX() { return x; }
  int getY() { return y; }

  void setX(int x) {
    this.x = x;
  }
  void setY(int y) {
    this.y = y;
  }
}

class MoveTracking {
  private static boolean flag = false;

  public static void setFlag() {
    flag = true;
  }

  public static boolean testAndClear() {
    boolean result = flag;
    flag = false;
    return result;
  }
}
```

#### Versión 2 sin aspectos

También detecta el cambio de coordenadas de un punto.

`Line` $\dashrightarrow$ `MoveTracking`
`Point` $\dashrightarrow$ `MoveTracking`

```java
class Line {
  private Point p1, p2;

  Point getP1() { return p1; }
  Point getP2() { return p2; }

  void setP1(Point p1) {
    this.p1 = p1;
    MoveTracking.setFlag();
  }
  void setP2(Point p2) {
    this.p2 = p2;
    MoveTracking.setFlag();
  }
}

class Point {
  private int x = 0, y = 0;

  int getX() { return x; }
  int getY() { return y; }

  void setX(int x) {
    this.x = x;
    MoveTracking.setFlag(); //añadido
  }
  void setY(int y) {
    this.y = y;
    MoveTracking.setFlag(); //añadido
  }
}

class MoveTracking {
  private static boolean flag = false;

  public static void setFlag() {
    flag = true;
  }

  public static boolean testAndClear() {
    boolean result = flag;
    flag = false;
    return result;
  }
}
```

#### Versión 3 sin aspectos

Las colecciones de figuras son complejas. Las estructuras de objetos son jerárquicas y se producen eventos asíncronos:

![colección de figuras](./figuras/aspectj-2.png)

La versión 2 hace que un cambio en cualquier elemento provoque un refresco de todas las figuras.

Mejor monitorizar las figuras que cambian...

Decidimos modificar la implementación: cambiar el método `setFlag` por `collectOne`, indicando la figura que se mueve.

```java
class Line {
  private Point p1, p2;

  Point getP1() { return p1; }
  Point getP2() { return p2; }

  void setP1(Point p1) {
    this.p1 = p1;
    MoveTracking.collectOne(this); // modificado
  }
  void setP2(Point p2) {
    this.p2 = p2;
    MoveTracking.collectOne(this); // modificado
  }
}

class Point {
  private int x = 0, y = 0;

  int getX() { return x; }
  int getY() { return y; }

  void setX(int x) {
    this.x = x;
    MoveTracking.collectOne(this); // modificado
  }
  void setY(int y) {
    this.y = y;
    MoveTracking.collectOne(this); // modificado
  }
}

class MoveTracking {
  private static Set movees = new HashSet();

  public static void collectOne(Object o) {
    movees.add(o);
  }

  public static Set getmovees() {
    Set result = movees;
    movees = new HashSet();
    return result;
  }
}
```

La no ortogonalidad de `MoveTracking` con respecto a `Line` y `Point` hace que la solicitud de un cambio de implementación (el seguimiento de los cambios en las figuras para el refresco en pantalla) provoque un camnbio en los otros módulos (clases).

El cambio de implementación del seguimiento de los cambios para el refresco en pantalla ha dado lugar a modificaciones en todas las clases: `Line`, `Point` y `MoveTracking`

### <span style="color:blue;">Programación orientada a aspectos</span>

La __programación orientada a aspectos__ (_AOP_) es un paradigma de programación cuyo objetivo es incrementar la modularidad (ortogonalidad) de las implementaciones mediante la separación de aspectos _transversales_ (_cross-cutting concerns_).

![terminología sobre AOP](./figuras/aspectj-terminology.png)

- __aspect__ = modularización de un aspecto de interés (_concern_) que afecta a varias clases o módulos
- __joinpoint__ = especificación declarativa de un punto en la ejecución de un programa (por ejemplo, la ejecución de un método, el manejo de una excepción, etc.)
- __advice__ = acción a tomar por la especificación de un aspecto dado en un determinado _joinpoint_
- __pointcut__ = predicado que define cuándo se aplica un _advice_ de un aspecto en un _jointpoint_ determinado. Se asocia un _advice_ con la expresión de un _pointcut_ y se ejecuta el _advice_ en todos los _joinpoint_ que cumplan la expresión del _pointcut_.

### Implementación con aspectos

En el ejemplo anterior, las clases `Line` y `Point` no se ven afectadas:

```java
class Line {
  private Point p1, p2;

  Point getP1() { return p1; }
  Point getP2() { return p2; }

  void setP1(Point p1) {
    this.p1 = p1;
  }
  void setP2(Point p2) {
    this.p2 = p2;
  }
}

class Point {
  private int x = 0, y = 0;

  int getX() { return x; }
  int getY() { return y; }

  void setX(int x) {
    this.x = x;
  }
  void setY(int y) {
    this.y = y;
  }
}
```

#### Versión 1 con aspectos

`Line` $\not\dashrightarrow$ `MoveTracking`

```java
aspect MoveTracking {
  private boolean flag = false;
  public boolean testAndClear() {
    boolean result = flag;
    flag = false;
    return result;
  }

  pointcut move():
    call(void Line.setP1(Point)) ||
    call(void Line.setP2(Point));

  after(): move() {
    flag = true;
  }
}
```

#### Versión 2 con aspectos

`Line` $\not\dashrightarrow$ `MoveTracking`
`Point` $\not\dashrightarrow$ `MoveTracking`

```java
aspect MoveTracking {
  private boolean flag = false;
  public boolean testAndClear() {
    boolean result = flag;
    flag = false;
    return result;
  }

  pointcut move():
    call(void Line.setP1(Point)) ||
    call(void Line.setP2(Point)) ||
    call(void Point.setX(int))   ||
    call(void Point.setY(int));

  after(): move() {
    flag = true;
  }
}
```

#### Versión 3 con aspectos

`Line` $\perp$ `MoveTracking`
`Point` $\perp$ `MoveTracking`

Versión más ortogonal. Todos los cambios están concentrados en un solo aspecto.

```java
aspect MoveTracking {
  private Set movees = new HashSet();
  public Set getmovees() {
    Set result = movees;
    movees = new HashSet();
    return result;
  }

  pointcut move(FigureElement figElt):
    target(figElt) &&
    (call(void Line.setP1(Point)) ||
     call(void Line.setP2(Point)) ||
     call(void Point.setX(int))   ||
     call(void Point.setY(int)));

  after(FigureElement fe): move(fe) {
    movees.add(fe);
  }
}
```

### Ejercicios: AspectJ y Spring AOP

- [Introducción a AspectJ](http://www.baeldung.com/aspectj)
- [Introducción a Spring AOP](http://www.baeldung.com/spring-aop)

## <span style="color:blue;">Aserciones</span>

<a id="assert"></a>

> There is a luxury in self-reproach. When we blame ourselves we feel no one else has a right to blame us.
>
> <cite>Oscar Wilde, The Picture of Dorian Gray</cite>


### Programación asertiva

Ejemplos de situaciones que "no van a ocurrir nunca":

- Con dos dígitos para el año basta
- Esta aplicación nunca va a usarse en el extranjero
- Este contador nunca va a ser negativo

Añadir __aserciones__ al código para chequear esas situaciones:

```java
    void writeString(String s) {
      assert(s != null);
      ...
    }
    ...
    for (int i = 0; i < num_entries-1; i++) {
      assert(sorted[i] <= sorted[i+i]);
    }
```

### Aserciones e invariantes

Las aserciones sirven para expresar invariantes

__Invariante__ = condición que se puede considerar cierta durante la ejecución de un programa o de parte del mismo. Es un predicado lógico que se debe mantener siempre cierto durante una cierta fase de la ejecución.

Por ejemplo, una _invariante de bucle_ es una condición que es cierta al principio y al final de cada ejecución de un bucle

### Aserciones en Java

Forma 1:

```java
    assert Expression1 ;
```

Forma 2:

```java
    assert Expression1 : Expression2 ;
```

- `Expression1` es `boolean`
- `Expression2` devuelve un valor que es pasado al constructor de `AssertionError`, que usa una representación en forma de string del valor como detalle del mensaje

En versiones antiguas del JDK, notificar al compilador que las acepte:

```shell
  javac -source 1.4 *.java
```

Las aserciones en Java imponen un alto coste en rendimiento y puede ser conveniente desabilitarlas en tiempo de ejecución:

```shell
  java [ -enableassertions | -ea  ] [:<package name>"..." | :<class name> ]
  java [ -disableassertions | -da ] [:<package name>"..." | :<class name> ]
```

#### No son para gestión de errores

```java
  try {
    BufferedReader in =
      new BufferedReader(new InputStreamReader(System.in));
    String input;
    System.out.print("Please Type Something here: ");
    input = in.readLine();
    assert((input.equalsIgnoreCase("Y") ||
            (input.equalsIgnoreCase("N"));   /* bad idea! */
    ...
  } catch (Exception ex) {
    System.out.print("We've had an Exception: " + ex.getMessage());
  }
```

#### Efectos colaterales

```java
  while (Iterator i.hasNext() {
    assert(i.next() != null); /* side effect */
    Object obj = i.next();
    // ...
  }

  while (Iterator i.hasNext() {
    Object obj = i.next();
    assert(obj != null);
    // ...
  }
```

### Tipos de invariantes

#### Invariantes internas

Sustituir los comentarios que indicaban invariantes:

```java
  if (i % 3 == 0) {
    ...
  } else if (i % 3 == 1) {
    ...
  } else { // We know (i % 3 == 2)
    ...
  }
```

Mejor con aserciones:

```java
  if (i % 3 == 0) {
    ...
  } else if (i % 3 == 1) {
    ...
  } else {
    assert i % 3 == 2 : i;
    ...
  }
```

#### Invariantes de control de flujo

Para **selectivas**:

```java
  switch(suit) {
    case Suit.CLUBS:
      ...
      break;
    case Suit.DIAMONDS:
      ...
      break;
    case Suit.HEARTS:
      ...
      break;
    case Suit.SPADES:
      ...
  }
```

- Añadir:

  ```java
    default:
      assert false : suit;
  ```

- o también:

  ```java
    default:
      throw new AssertionError(suit);
  ```

Puntos **inalcanzables**:

```java
  void foo() {
    for (...) {
      if (...)
        return;
    }
    assert false; // Execution should never reach this point!!!
  }
```

#### Invariantes de clase

Son un tipo de invariantes internas que se aplican a todas las instancias de una clase, en todos los momentos, excepto cuando una instancia está en transición de un estado consistente a otro.

Por ejemplo, en un árbol binario equilibrado, una invariante de clase puede indicar que está ordenado y equilibrado:

- Añadir código en Java:
  ```java
  // Returns true if this tree is properly balanced
  private boolean isBalanced() {
    ...
  }
  ```
- Todo constructor y método _público_ debe llamar a `assert isBalanced();` antes del `return`.

Es recomendable incluir comprobaciones de invariantes de clase al principio de los métodos de clases cuyo estado es modificable por otras clases (v.g. _setters_).

#### *Idiom* para definir aserciones finales

A veces hace falta guardar datos antes de hacer un cómputo, para poder luego comprobar una condición cuando el cómputo se haya completado. Ejemplo de cómo hacerlo con una _inner class_ que guarda el estado de variables:

```java
  void foo(int[] array) {
        // Manipulate array
        ...
        // At this point, array will contain exactly the ints that it did
        // prior to manipulation, in the same order.
    }

  void foo(final int[] array) {
        class DataCopy {
          private int[] arrayCopy;
          DataCopy() { arrayCopy = (int[])(array.clone()); }
          boolean isConsistent() { return Arrays.equals(array, arrayCopy); }
        }
        DataCopy copy = null;
        // Always succeeds; has side effect of saving a copy of array
        assert (copy = new DataCopy()) != null;
        ... // Manipulate array
        assert copy.isConsistent();
     }
```

## <span style="color:blue;">Programación por contratos</span>

<a id="contracts"></a>

### Contrato

- Un contrato entre dos partes define derechos y responsabilidades por ambas partes
- Define las repercusiones por incumplimiento del contrato

### Design By Contract (DBC)

- Desarrollado para lenguaje *Eiffel* por Bertrand Meyer
- Documentar y aceptar los derechos y responsabilidades de cada módulo de software para asegurar la correción de un programa
- Un programa correcto es aquél que hace nada más y nada menos que lo que dice hacer

### Precondiciones, postcondiciones e invariantes

#### Precondición

- Qué debe ser cierto antes de llamar a una rutina/método (sus requisitos)
- Una rutina jamás debe ser llamada si se violan sus precondiciones
- Es responsabilidad del que la llama hacer que se cumplan

#### Postcondición

- Qué garantiza la rutina: estado del mundo cuando la rutina/método termina
- Implica que la rutina debe finalizar: no puede haber bucles ifinitos

#### Invariante de clase

- Condición que se cumple para todas las instancias de la clase, desde la perspectiva del llamador
- Durante el procesamiento interno, la invariante puede no cumplirse, pero sí cuando la rutina termina y se devuelve el control al llamador
- Una clase no puede dar permiso de escritura sin restricciones sobre las propiedades (_data members_) que participan en la definición de la invariante

### Ejemplo: Raíz cuadrada en Eiffel

```eiffel
sqrt: DOUBLE is
  -- Square root routine
  require
    sqrt_arg_must_be_positive: Current >= 0;
  --- ...
  --- calculate square root here
  --- ...
  ensure
    ((Result*Result) - Current).abs <= epsilon*Current.abs;
  -- Result should be within error tolerance
end;
```

Si el usuario introduce un número negativo en la consola, es responsabilidad del código que llama a `sqrt` que dicho valor no se pase nunca a `sqrt`. Opciones:

- Terminar
- Emitir una advertencia y leer otro número
- Pasar el número a complejo (ponerlo  en positivo y añadir una _i_)

Si se llega a pasar un número negativo, Eiffel imprime el error `sqrt_arg_must_be_positive` en tiempo de ejecición y una traza de la pila (En otros lenguajes, como Java, se devolvería un `Nan`).

### Ejemplo: Cuenta Bancaria sin contratos

```eiffel
class ACCOUNT feature
    balance: INTEGER
    owner: PERSON
    minimum_balance: INTEGER is 1000
    open (who: PERSON) is
    -- Assign the account to owner who.
        do
            owner := who
        end
    deposit (sum: INTEGER) is
    -- Deposit sum into the account.
        do
            add (sum)
        end
    withdraw (sum: INTEGER) is
    -- Withdraw sum from the account.
        do
            add (-sum)
        end
    may_withdraw (sum: INTEGER): BOOLEAN is
     -- Is there enough money to withdraw sum?
         do
            Result := (balance >= sum + minimum_balance)
         end
feature {NONE}
    add (sum: INTEGER) is
    -- Add sum to the balance.
         do
            balance := balance + sum
        end
end -- class ACCOUNT
```

- `feature` son las operaciones de la clase
- `feature { NONE }` son privados
- `make` para definir el constructor

### Ejemplo: Cuenta Bancaria con contratos

```eiffel
class ACCOUNT create
    make
feature
    ... Attributes as before:
         balance , minimum_balance , owner , open ...
    deposit (sum: INTEGER) is
    -- Deposit sum into the account.
         require
            sum >= 0
         do
            add (sum)
         ensure
            balance = old balance + sum
        end
    withdraw (sum: INTEGER) is
    -- Withdraw sum from the account.
         require
            sum >= 0
            sum <= balance - minimum_balance
         do
            add (-sum)
         ensure
            balance = old balance - sum
        end
    may_withdraw ... -- As before
feature {NONE}
    add ... -- As before
    make (initial: INTEGER) is
    -- Initialize account with balance initial.
         require
            initial >= minimum_balance
         do
            balance := initial
         end
invariant
    balance >= minimum_balance
end -- class ACCOUNT
```

__Forma corta__ del contrato:

```eiffel
class interface ACCOUNT create
    make
feature
    balance: INTEGER
    ...
    deposit (sum: INTEGER) is
            -- Deposit sum into the account.
         require
            sum >= 0
         ensure
            balance = old balance + sum
    withdraw (sum: INTEGER) is
            -- Withdraw sum from the account.
         require
            sum >= 0
            sum <= balance - minimum_balance
         ensure
            balance = old balance - sum

    may_withdraw ...
end -- class ACCOUNT
```

### Ejemplo: Java + iContract

Java no permite especificar contratos (los _assert_ no son lo mismo). Así que hay que utilizar extensiones como _iContract_

__Ejemplo__: Inserción en una lista ordenada

```java
    /**
     * @invariant forall Node n in elements() |
     *    n.prev() != null
     *      implies
     *         n.value().compare To(n.prev().value()) > 0
     */
    public class OrderedList {
      /**
       * @pre contains(aNode) == false
       * @post contains(aNode) == true
       */
       public void insertNode(final Node aNode) {
         // ...
       }
       // ...
    }
```

Una postcondición puede necesitar expresarse con parámetros pasados a un método para verificar un comportamiento correcto.

Si el método puede cambiar el valor del parámetro pasado (parámetro mutable), el contrato puede incumplirse.

#### Parámetros inmutables

- Eiffel no permiten que se pueda cambiar el valor de un parámetro (es inmutable)
- En C++ usar `const`
- Opciones en Java:
  - Usar `final` para marcar un parámetro constante. Sin embargo, las subclases podrían redefinir los parámetros y volver a hacerlos mutables. Además `final` se aplica a la referencia, no al objeto en sí.
  - Usar `variable@pre` de _iContract_
- Muchos lenguajes funcionales (Lisp, Haskell, Erlang, Clojure, etc.) definen inmutabilidad por defecto

¿Por qué la inmutabilidad?

- Por rendimiento (v.g. `String` en Java): si es inmutable, para copiar un objeto basta con copiar la referencia (_interning_)
- Por _thread-safety_

#### Código perezoso

- Se recomienda escribir código "perezoso" para los contratos: ser estricto en lo que se acepta al empezar y prometer lo menos posible al terminar.
- Si un contrato indica que se acepta cualquier cosa y promete la luna a cambio, habrá que escribir un montón de código!

### _Dead programs tell no lies_

El DBC y la programación por contratos son una forma de gestionar los errores mediante
_early crash_.

Hay diversas técnicas de gestión de errores (que veremos más adelante), pero en general el principio básico es: cuando el código descubre que sucede algo que supuestamente es imposible o "no debería suceder", el programa ya no es viable: eutanasia.

- En Java se lanza una `RuntimeException` cuando sucede algo extraño en tiempo de ejecución.
- Se puede/debe hacer lo mismo con cualquier lenguaje



<!--
### Precondiciones con aserciones

#### Método público

```java
    /**
     * Sets the refresh rate.
     *
     * @param  rate refresh rate, in frames per second.
     * @throws IllegalArgumentException if rate <= 0 or
     *          rate > MAX_REFRESH_RATE.
     */
     public void setRefreshRate(int rate) {
       // Enforce specified precondition in public method
       if (rate <= 0 || rate > MAX_REFRESH_RATE)
          throw new IllegalArgumentException("Illegal rate: " + rate);
       setRefreshInterval(1000/rate);
     }
```

-   El método garantiza que siempre se harán chequeos de los argumentos, independientemente de si las aserciones están o no activadas
-  Añadir un assert para la precondición no es apropiado en este caso

#### Método no público

```java
    /**
     * Sets the refresh interval (must correspond to a legal frame rate).
     *
     * @param  interval refresh interval in milliseconds.
     */
     private void setRefreshInterval(int interval) {
       // Confirm adherence to precondition in nonpublic method
       assert interval > 0 && interval <= 1000/MAX_REFRESH_RATE : interval;
       ... // Set the refresh interval
     }
```

-   La aserción indica una precondición que debe ser cierta sin importar lo que el cliente haga con la clase
-   La aserción puede indicar un bug en la biblioteca


### Postcondiciones con aserciones

-   Pueden usarse en métodos públicos y no públicos

```java
    /**
     * Returns a BigInteger whose value is (1/this mod m).
     *
     * @param  m the modulus.
     * @return 1/this mod m.
     * @throws ArithmeticException m <= 0, or this BigInteger
     *         has no multiplicative inverse mod m
     *         (this BigInteger is not relatively prime to m).
     */
     public BigInteger modInverse(BigInteger m) {
       if (m.signum <= 0)
          throw new ArithmeticException("Modulus not positive: " + m);
       ... // Do the computation

       assert this.multiply(result).mod(m).equals(ONE) : this;
       return result;
     }
```
-->

<!--
### Cuestiones de Diseño

(LSP) Inheritance and polymorphism are the cornerstones of object-oriented languages and an area where contracts can really shine. Suppose you are using inheritance to create an "is-a-kind-of" relationship, where one class "is-a-kind-of" another class. You probably want to adhere to the Liskov Substitution Principle


Without a contract, all the compiler can do is ensure that a subclass conforms to a particular method signature. But if we put a base class contract in place, we can now ensure that any future subclass can't alter the meanings of our methods. For instance, you might want to establish a contract for setFont such as the following, which ensures that the font you set is the font you get:

  /**
  * @pre f != null
  * @post getFont() == f
  */
  public void setFont(final Font f) {
  // ...
-->

### Actividad: ¿Hay contratos en C++?

Ver el video de J. D. García sobre [Contracts programming after C++17](https://www.youtube.com/watch?v=IBas3S2HtdU): Desde el minuto 4'10''

### Aserciones versus contratos

- No hay soporte para propagar aserciones por una jerarquía de herencia: si se redefine un método con contrato, las aserciones que implementan el contrato no serán llamadas correctamente (excepto si se duplican en el código)
- No hay soporte para valores *antiguos*: si se implementara un contrato mediante aserciones, habría que añadir código a la precondición para guardar la información que quiera usarse en la postcondición. (v.g. `variable@pre` en *iContract* versus `old expression` en Eiffel)
- El sistema de runtime y las bibliotecas no están diseñadas para dar soporte a contratos, así que estos no se chequean. Y es precisamente en la frontera entre el cliente y la biblioteca donde hay más problemas.

### Redefinición de contratos

> A routine redeclaration [in a derivative] may only replace the original precondition by one equal or weaker, and the original post-condition by one equal or stronger
> 
> –– <cite>B. Meyer</cite>

Métodos de clase declaran *precondiciones* y *postcondiciones* al redefinir una operación en una subclase derivada

- las **precondiciones** sólo pueden sustituirse por otras más débiles/laxas
- las **postcondiciones** sólo pueden sustituirse por otras más fuertes/estrictas


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

¿Dónde se produce el error?

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
      // Treats any others exception
  }
  finally{
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

Excesiva duplicación de código: llamada a `reportError()`

Excepción encapsulada:

```java
    LocalPort port = new LocalPort(12);
    try {
      port.open();
    } catch (PortDeviceFailure e) {
      reportError(e);
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

__Ejemplo__: ¿Usar excepciones cuando se intenta abrir un fichero para leer y el fichero no existe?

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

## Uso de null

Obtener un _null_ cuando no se espera puede ser un quebradero de cabeza para el tratamiento de errores

### Principio general: no devolver null

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

### No devolver null

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

### No pasar valores null

```java
public class MetricsCalculator
{
  public double xProjection(Point p1, Point p2) {
  return (p2.x - p1.x) * 1.5;
}
```

¿Qué sucede si llamamos a `xProjection()` así...?

```java
  calculator.xProjection(null, new Point(12, 13))
```

Devolver null es malo, pero ¡pasar un valor null es peor!

¿Es mejor así...?

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

#### Alternativa con aserciones

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
