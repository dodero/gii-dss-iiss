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
 - [Refactoring](#refactoring): código duplicado, dependencias
 - Calidad: errores, depuración
 - Bibliotecas y frameworks
 - Documentación 
 - Reflexión
 - Metaprogramación

## Paradigmas
 - <span style="color:blue;">Objetos</span>
 - <span style="color:blue;">Eventos</span>
 - <span style="color:blue;">Funcional</span>
 - Aspectos
 - Etc



## Casos prácticos

1. Ocultación de la implementación - [Recorrido de una lista](#recorridolista)
2. Delegación - [Implementación de una orquesta](#orquesta)
3. Inyección de dependencias - [Caballeros de la mesa redonda](#knights)
4. Código duplicado - [Cálculo de nóminas](#nominas)
 

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
<a id="ocultacion"></a>

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
<a id="herencia"></a>

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
<a id="polimorfismo"></a>

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
<a id="composicion"></a>

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

####Implementación utilizando `Comparable`

`java.lang.Comparable` es una interfaz implementada por `String`, `File`, `Date`, etc. y todas las llamadas _clases de envoltura_ del JDK (i.e. `Integer`, `Long`, etc.)

__Métodos de la interfaz__

(JDK 1.4):

```java
public interface Comparable {
  public int compareTo(Object o); //throws ClassCastException
}  
```

(JDK 1.5):

```java
public interface Comparable<T> {
  public int compareTo(T o); //throws ClassCastException
}  
```


__Invariantes:__

 - Anticonmutativa: `sgn(x.compareTo(y)) = -sgn(y.compareTo(x))`

 - Transitividad: `(x.compareTo(y)>0 and y.compareTo(z)>0)` $$\Rightarrow$$
`x.compareTo(z)>0`

 - `x.compareTo(y)=0` $$\Rightarrow$$ 
   `sgn(x.compareTo(z))=sgn(y.compareTo(z))` $$\forall$$ `z`

 - Consistencia con `equals` (no obligatoria): `(x.compareTo(y)=0)`
     $$\Leftrightarrow$$ `(x.equals(y))`

 - Cuando una clase hereda de una clase concreta que implementa Comparable y le añade un campo significativo para la comparación, no se puede construir una implementación correcta de `compareTo`. La única alternativa entonces es la composición en lugar de la herencia.

 - Una alternativa a implementar `Comparable`es pasar un `Comparator` como parámetro.

__Implementación en Java 1.5__:

 - Utilizando _templates_
 - Delegar en `compareTo` y `equals` del tipo de id _envuelto_ (e.g. `String`)
  
```java
import java.util.*;
import java.io.*;
  
public final class BankAccount implements Comparable<BankAccount> {
    private String id;
    public BankAccount (String number)  {
      this.id = number;
    }
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
		that = (BankAccount)other:
		return	( this.id.equals((that.getId()) );
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

__Implementación en Java 1.4__:

 - No hay plantillas. La genericidad se consigue con `Object`. Hay que hacer casting.
 - Cuidado con `Boolean` que no implementa `Comparable` en JDK 1.4


```java
import java.util.*;
import java.io.*;
  
public final class BankAccount implements Comparable {
    private String id;
    public BankAccount (String number)  {
      this.id = number;
    }
    public int compareTo(Object other) {
      if (this == other) return 0;
      that = (BankAccount)other:
      assert this.equals(that) : "compareTo inconsistent with equals.";
      return this.id.compareTo(that.getId());
    }
	public boolean equals(Object other) {
		if (this == other) return true;
		if (!(other instanceof BankAccount)) return false;
		that = (BankAccount)other:
		return	( this.id.equals(that.getId()) );
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

### Diseño

Ocultando la implementación con interfaces:

```java
public interface Knight {
  Object embarkOnQuest() throws QuestFailedException;
}

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

-   El caballero no es el responsable de averiguar su misión.
-   El caballero sólo sabe de su misión a través de la interfaz `Quest`.
-   El caballero recibe la misión (se le inyecta) a través de `setQuest()`
-   Puede asignársele cualquier implementación de `Quest`
    (`HolyGrailQuest`, `RescueDamselQuest`, etc.)

### Construcción con spring

A través de un fichero de configuración XML le indicamos los valores inyectables:

```xml
<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN"	"http://www.springframework.org/dtd/spring-beans.dtd"><beans>	<bean id="quest"		class="HolyGrailQuest"/>	<bean id="knight"		class="KnightOfTheRoundTable">		<constructor-arg>			<value>CruzadoMagico</value>		</constructor-arg>		<property name="quest">			<ref bean="quest"/>		</property>	</bean></beans>
```

La inyección de la dependencia concreta la hace el contenedor (_spring_ en este ejemplo):

```java
import org.springframework.beans.factory.BeanFactory;import org.springframework.beans.factory.xml.XmlBeanFactory;public class KnightApp {	public static void main(String[] args) throws Exception {		BeanFactory factory =			new XmlBeanFactory(new FileInputStream("knight.xml"));		KnightOfTheRoundTable knight =			(KnightOfTheRoundTable) factory.getBean("knight");		knight.embarkOnQuest();	}}
```


### Ejemplo: Logger 

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

Un _contenedor de dependencias_ en el framework debe responsabilizarse de crear las instancias de `Logger` e inyectarlas en su sitio (normalmente vía _reflexión_)

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

Esta clase sigue usando `new` para ciertos elementos de la interfaz.
Esto significa que no pensamos reemplazarlos ni al hacer pruebas.
      

#Caso 4 - código duplicado
##Cálculo de nóminas
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
- Ahora incluimos un método para el cálculo de la nómina mensual...      
      
### Nóminas v0.2

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
    /* ... */ 
    public float computeMonthlySalary() {
        return workingHours*Company.getHourlyRate()*(1.0+Company.getVatRate());
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


##Refactoring
<a id="refactoring"></a>

> Refactoring is a disciplined technique for restructuring an existing body of code, altering its internal structure without changing its external behavior [@Refactoring]
> 
> —- <cite>[M. Fowler](www.refactoring.com), www.refactoring.com</cite>

-   Pequeñas transformaciones
-   Mantienen el sistema funcional
   

## Código duplicado

####¿Por qué no?

-   Mantenimiento
-   Cambios (no sólo a nivel de código)
-   Trazabilidad

###Causas de la duplicación

-   __Impuesta__: No hay elección
-   __Inadvertida__: No me he dado cuenta
-   __Impaciencia__: No puedo esperar
-   __Simultaneidad__: Ha sido otro


###Principio DRY

> **D**on't **R**epeat **Y**ourself!

###Duplicación impuesta

La gestión del proyecto así nos lo exige:

-   Representaciones múltiples de la información
-   Documentación del código
-   Casos de prueba
-   Características del lenguaje (v.g. C/C++ header files, IDL specs)

####Técnicas de solución

-   Generadores de código: para evitar duplicar representaciones múltiples de la información
-   Herramientas de ingeniería inversa: para generar código a partir de un esquema de BD
-   Plantillas: Java, C++, etc.
-   Metadatos: En Java, anotaciones @
-   [Programación literaria](http://www.literateprogramming.com/)
-   Ayuda del IDE

#### ¿Siempre aplicar DRY?

**Ejemplo: versión 1**

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

A veces se puede optar por violar DRY por razones de rendimiento. 


**Ejemplo: versión 2**

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

Es menos problemático si queda dentro de los límites de la clase/módulo.

Otras veces no merece la pena violar DRY por rendimiento: ¡las cachés y los optimizadores de código también hacen su labor!


###Principio de acceso uniforme

> All services offered by a module should be available through a uniform notation, which does not betray whether they are implemented through storage or through computation
>
> <cite>[B. Meyer](#meyer)</cite>

**Ejemplo: versión 3 en C#**

```csharp
public class Line {	private Point Start;
	private Point End;	private double Length;	public Point Start {		get { return Start; }		set { Start = value; }	}

	public Point End {		get { return End; }		set { Start = value; }	}
		public double Length {		get { return Start.distanceTo(End); }	}}   
```

### Otros motivos de duplicación

__Impaciencia__

- Los peligros del *copy&paste*
- *Vísteme despacio que tengo prisa*
- v.g. Fiasco del año 2000

__Simultaneidad__

-   No resoluble a nivel de técnicas de construcción
-   Hace falta metodología, gestión de equipos + herramientas de comunicación


## Ortogonalidad

Dos componentes A y B son ortogonales ($A \perp B$) si los cambios en uno no afectan al otro. Suponen más independencia, menos acoplamiento. Por ejemplo:

  -   La base de datos debe ser ortogonal a la interfaz de usuario
  -   En un helicóptero, los mandos de control no suelen ser ortogonales


###Beneficios

Mayor **productividad**:

 -  Es más fácil escribir un componente pequeño y auto-contenido que un bloque muy grande de código. El tiempo de desarrollo y pruebas se reduce
 -  Se puede combinar unos componentes con otros más fácilmente. Mayor reutilización.
 -  Si $$A \perp B$$, el componente A sirve para $$m$$ propósitos y B sirve para $$n$$, entonces $$A \cup B$$ sirve para $$m \times n$$ propósitos.
 -  La falta de cohesión perjudica la reutilización. v.g. ¿y si hay que hacer una nueva versión gráfica de una aplicación de línea de comandos? (los `System.out.println` pueden descohesionar)

Menor <span>**riesgo**</span>:

 -  Defectos aislados. Menor fragilidad del sistema global
 -  Más fácil de probar, pues será más fácil construir pruebas individuales de cada uno de sus componentes (e.g. _mocking_ es más sencillo)


La ortogonalidad es aplicable a la gestión de proyectos, el diseño, la codificación, las pruebas y la documentación.

A nivel de diseño, patrones y arquitecturas como MVC facilitan la construcción de componentes ortogonales.

###Codificación

Técnicas de codificación para fomentar la ortogonalidad:

-   Evitar datos globales y _singletons_: v.g. ¿y si hay que hacer una versión
    *multithreaded* de una aplicación?
-   Usar métodos plantilla y estrategias — Aplicar DRY
-   Desacoplar: Ley de <span>*Demeter*</span>—No hables con extraños
-   Inyectar: pasar explícitamente el contexto (dependencia) como parámetro a los constructores

#### Desacoplar - ley de Demeter

Al pedir un servicio a un objeto, el servicio debe ser realizado de parte nuestra, no que nos devuelva un tercero con el que tratar para realizarlo

__Ejemplo__:

```java
	public boolean canWrite(User user) {		if (user.isAnonymous())			return false;		else {			return user.getGroup().hasPermission(Permission.WRITE);		}	}
```

- Definir un método `User.hasPermission()`

<!--
####Ejemplo:

Pintar un gráfico con los datos registrados por una serie de grabadoras (_Recorder_) dispersas por el mundo. Cada grabadora está en una ubicación (_Location_) y una zona horaria (_TimeZone_). Los usuarios seleccionan una grabadora y pintan sus datos etiquetados con la zona horaria correcta...

```java
  public void plotDate(Date aDate, Selection aSelection) {
    TimeZone tz = aSelection.getRecorder().getLocation().getZone();
  }
```      

####Críticas

  -  Multiplicidad de dependencias: `plotDate` $$\dashrightarrow$$ `Selection`, `Recorder`, `Location`, `TimeZone`. Si cambia la implementación de `Location` de forma que ya no incluye directaemnte una `TimeZone`, hay que cambiar `plotDate`
    
  - Añadir un método `getTimeZone` a `Selection`. Así `plotDate` no se entera de si la `TimeZone` le llega desde `Recorder` o desde un objeto contenido en `Recorder`. 
   

  - ```java
	  public void plotDate(Date aDate, TimeZone tz) {
	     /* ... */
	  }
	  plotDate(someDate, someSelection.getTimeZone());
	```     
-->     
      

#### Inyectar el contexto

Pasar explícitamente el contexto (dependencia) como parámetro a los constructores de la clase

__Ejemplo__:

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

  1. _propios_  
  2. de objetos pasados como _parámetros_
  3. de objetos _creados_ por ellos mismos
  4. de objetos _declarados_ en el mismo método
  
```java
class Demeter {
  private A a;
  private int func();
  public void example (B b);
  
  void example(B b) {
    C c;
    int f = func();     // (1)
    b.invert();			// (2)
    a = new A();
    a.setActive();      // (3)
    c.print();	        // (4)
}
```

####Críticas

 -   ¿Realmente ayuda a crear código más mantenible?
 -   Costes de espacio y ejecución de métodos <span>*wrapper*</span> que reenvían la petición al objeto delegado.
 -   Violar la ley para mejorar el rendimiento


###Toolkits y bibliotecas

-   Usar metadatos (@tag) para propósitos específicos: e.g. persistencia de objetos, transacciones, etc.
-   Aspect-Oriented Programming (AOP)

