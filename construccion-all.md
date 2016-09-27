# Construcción de Sistemas Software

## Temas

### __Problemáticas__
 - <span style="color:blue;">Variabilidad</span>
 - <span style="color:blue;">Acoplamiento</span>
 - Complejidad
 - Robustez
 - Reutilización
 - Flexibilidad

### __Principios y técnicas__
 - <span style="color:blue;">Ocultación</span>
 - <span style="color:blue;">Cohesión</span>
 - <span style="color:blue;">Delegación</span>
 - <span style="color:blue;">Refactoring</span>
 - Bibliotecas
 - Documentación 
 - Reflexión
 - Metaprogramación

### __Paradigmas__
 - <span style="color:blue;">Objetos</span>
 - Eventos
 - Aspectos
 - Funcional
 - Etc


## Casos prácticos

1. [Recorrido de una lista](#recorridolista)
2. [Implementación de una orquesta](#orquesta)

# Caso 1
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

####Críticas:

-  __Variabilidad__: ¿Y si hay distintas implementaciones de `traverse()`? Si implementamos varias versiones de la lista, introducimos más dependencias (acoplamiento)
-  __Cohesión__: `List<T>` aglutina más de una responsabilidad: almacenar y recorrer. Implementación no cohesionada


### Implementación alternativa: Lista v0.2

Delegar funcionalidad hacia las subclases (vía herencia).

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

####Críticas

-   ¿Qué operación hace `traverse()` con cada elemento individual (imprimir, sumar, etc.)? ¿Hay que especializar de nuevo para cada tipo de operación?
-   ¿Y si hay que especializar de nuevo el recorrido: sólo los pares, sólo los impares, etc.?


### Implementación alternativa: Lista v0.3

Ampliar la interfaz

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

####Críticas:

-   __Dependencias__: Si hay que cambiar la operación básica que hace `traverse()` con cada elemento (imprimir, sumar, etc.), ¿cuántos métodos hay que cambiar?
-   __Flexibilidad__: Cuánto más variedad de recorridos (la interfaz es mayor), menos flexibilidad para los cambios


### Implementación alternativa: Lista v0.4

Delegar hacia otra clase

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

-  __Cohesión__: Las responsabilidades están separadas: `List` almacena, `Iterator` recorre. `List` está más cohesionada


## Ocultar la implementación

-   __Cohesión__: módulos auto-contenidos, independientes y con un
    único propósito
-   __Acoplamiento__: minimizar dependencias entre módulos
-   __Abstracción__: diferenciar el *qué* y el *cómo*
-   __Modularidad__: clases, interfaces y componentes/módulos


### Alta cohesión, bajo acoplamiento

> Cuando los componentes están aislados, puedes cambiar uno sin preocuparte por el resto. Mientras no cambies las interfaces externas, no habrá problemas en el resto del sistema
> 
> -- <cite>[Eric Yourdon](#yourdon)</cite>

### Modularidad

Reducir el acoplamiento usando módulos o componentes con distintas responsabilidades, agrupados en bibliotecas

### Técnicas de ocultación

Hay diversas técnicas para ocultar la implementación...

-  __Encapsular__: agrupar en módulos y clases
-  __Visibilidad__: `public`, `private`, `protected`, etc.
-  __Delegación__: incrementar la cohesión extrayendo funcionalidad pensada para otros propósitos fuera de un módulo
-  __Herencia__: delegar _en vertical_
-  __Polimorfismo__: ocultar la implementación de un método, manteniendo la misma interfaz de la clase base
-  __Interfaces__ bien documentadas


## Herencia: Generalización y especialización

-   **Reutilizar la interfaz**
    -   Clase base y derivada son del mismo tipo
    -   Todas las operaciones de la clase base están también disponibles
        en la derivada

-   **Redefinir vs. reutilizar el comportamiento**
    -   *Overriding* (redefinición): cambio de comportamiento
    -   *Overloading* (sobrecarga): cambio de interfaz

-   **Herencia pura vs. extensión**
    -   Herencia pura: mantiene la interfaz tal cual (relación *es-un*)
    -   Extensión: amplía la interfaz con nuevas funcionalidades
        (relación *es-como-un*). Puede causar problemas de _casting_.


> When you inherit, you take an existing class and make a special version of it. In general, this means that you’re taking a general-purpose class and specializing it for a particular need. [...] it would make no sense to compose a car using a vehicle object —a car doesn’t contain a vehicle, it is a vehicle. The _is-a_ relationship is expressed with inheritance, and the _has-a_ relationship is expressed with composition.
> 
> -- <cite>[Bruce Eckel](#eckel)</cite>

### Ejemplo: *downcasting* y *upcasting*
   

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

#### Críticas:
- Hay que rediseñar la solución


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

##Polimorfismo

Fenómeno por el que, cuando se llama a una operación de un objeto del que no se sabe su tipo específico, se ejecuta el método adecuado de acuerdo con su tipo.

El polimorfismo se basa en:

-   **Enlace dinámico**: se elige el método a ejecutar en tiempo de
    ejecución, en función de la clase de objeto; es la implementación
    del *polimorfismo*

-   **Moldes (_casting_)**
    -   *Upcasting*: Interpretar un objeto de una clase derivada como
        del mismo tipo que la clase base
    -   *Downcasting*: Interpretar un objeto de una clase base como del
        mismo tipo que una clase derivada suya




# Caso 2
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
      { /* ... */ };
      public static void afinarCuerda(Cuerda i)
      { /* ... */ };
  }
  class Viento extends Instrumento {
      public void tocar() { soplar(); }
  }
  class Cuerda extends Instrumento {
      public void tocar() { rascar(); }
  }

  public class Orquesta {
    ArrayList<Instrumento> instrumentos;
    public Orquesta { instrumentos = new ArrayList<Instrumento>(3); }
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

####Críticas:

- __Acoplamiento__: método `static`
- __Cohesión__: ubicación de `main`


### Implementación alternativa: Orquesta v0.2

Usar polimorfismo. Seguir criticando la implementación...

```java
  class Orquesta {
    ArrayList<Instrumento> instrumentos;
    public Orquesta {
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
  public PruebaOrquesta {
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
  //class Viento ...
  //class Cuerda ...
  class Percusion extends Instrumento {
      public void tocar() { golpear(); }
      public void afinar() {
        golpear(); golpear(); /* y afinar... */
      };
  }
```      

####Críticas:

-   __Encapsulación__: método `add`
-   __Encapsulación__: visibilidad de `Orquesta::instrumentos` (en C++ sería `friend`)
-   __Flexibilidad__: la implementación `Orquesta::instrumentos` puede variar, pero no hay colección (agregado) en quien confíe `Orquesta` por delegación.
   

### Implementación alternativa: Orquesta v0.3

Delegar las altas/bajas de `Instrumento` en la colección (agregado) de `Orquesta`:

```java
  class Orquesta {

    private ArrayList<Instrumento> instrumentos;

    public Orquesta {
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

  public PruebaOrquesta {
     public static void main(String[] args) {
        Orquesta orquesta = new Orquesta();
        orquesta.addInstrumento(new Viento());
        orquesta.addInstrumento(new Cuerda());
        orquesta.addInstrumento(new Percusion());
        for (int i=0; i<orquesta.instrumentos.size(); i++)
           orquesta.afinar(instrumentos.get(i));
        orquesta.tocar();
     }
  }
```      

####Críticas:

-  __Acoplamiento__: `PruebaOrquesta` conoce la implementación basada en un `ArrayList` de la colección de instrumentos de la orquesta.
-  __Variabilidad__: ¿La colección de instrumentos será siempre lineal?


### Implementación alternativa: Orquesta v0.4

Definir una __interfaz__ para iterar en la colección de instrumentos:

```java
  class Orquesta {
    private List<Instrumento> instrumentos;
    public Orquesta {
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
  
  public PruebaOrquesta {
     public static void main(String[] args) {
        Orquesta orquesta = new Orquesta();
        orquesta.addInstrumento(new Viento());
        orquesta.addInstrumento(new Cuerda());
        orquesta.addInstrumento(new Percusion());
        for (Iterator<Instrumento> i = instrumentos.iterator(); i.hasNext(); )
           orquesta.afinar(i.next());
        orquesta.tocar();
     }
  }
```

####Críticas:

- __Flexibilidad__: Aparece una nueva versión del lenguaje (Java JDK 1.5) que permite iterar haciendo un _for each_  sobre una colección que implemente la interfaz `Iterable`. Rehacemos la implementación:


### Implementación alternativa: Orquesta v0.5

Usando delegación + interfaces y el _for each_ de Java 1.5:

Criticar...

```java
  class Orquesta {
    private List<Instrumento> instrumentos;
    public Orquesta {
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
  
  public PruebaOrquesta {
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

####Críticas:

-   __Ocultación__: la interfaz del método `instrumentos()` sigue expuesta: el cliente sabe que devuelve una `List`.


### Implementación final: Orquesta v0.6

```java
  class Orquesta implements Iterable<Instrumento> {
    private List<Instrumento> instrumentos;
    public Orquesta {
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
  
  public PruebaOrquesta {
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

## Composición

Delegación _en horizontal_ hacia otras clases cuya interfaz es bien conocida

-   Los objetos miembro __delegados__ soon cambiables en tiempo de
    ejecución sin afectar al código cliente ya existente
-   Alternativa más flexible que la herencia. Ejemplo: `Cola extends
    ArrayList` implica que una cola va a implementarse como un ArrayList
    para toda la vida, sin posibilidad de cambio en ejecución

### Composición vs. Herencia

-   **Composición** (delegación _en horizontal_)
    -   Sirve cuando hacen falta las características de una clase
        existente dentro de una nueva, pero no su interfaz.
    -   Los objetos miembro privados pueden cambiarse en tiempo
        de ejecución.
    -   Los cambios en el objeto miembro no afectan al código del cliente.

-   **Herencia** (delegación _en vertical_)
    -   Sirve para hacer una versión especial de una clase existente,
        reutilizando su interfaz.
    -   La relación de herencia en los lenguajes de programación _suele ser_ 
       __estática__ (definida en tiempo de compilación) y no __dinámica__ 
       (que pueda cambiarse en tiempo de ejecución).
    -   Permite re-interpretar el tipo de un objeto en tiempo
        de ejecución.
     
     
##Refactoring


> Refactoring is a disciplined technique for restructuring an existing body of code, altering its internal structure without changing its external behavior [@Refactoring]
> 
> —- <cite>M. Fowler,  www.refactoring.com</cite>

-   Pequeñas transformaciones
-   Mantienen el sistema funcional
   
