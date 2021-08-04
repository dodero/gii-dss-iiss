# OBJETOS - Principios

<a id="caso1"></a>

## Caso 1 - Recorrido de listas

<a id="recorridolista"></a>

### Ocultación de la implementación

#### Versión inicial: Lista v0.1

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

##### <span style="color:blue">Cohesión</span>

> Cohesion refers to the degree to which the elements inside a module belong together
> 
> -- <cite>[E. Yourdon & L. Constantine](bibliografia.md#yourdon)</cite>

##### Críticas a Lista v0.1

- `List<T>` aglutina más de una responsabilidad: almacenar y recorrer. Implementación no cohesionada
- ¿Y si hay distintas implementaciones de `traverse()`? Si implementamos varias versiones de la lista, introducimos más dependencias (acoplamiento)

##### Problemáticas de Lista v0.1

- Baja __cohesión__
- Alta __variabilidad__ no bien tratada --> poca __flexibilidad__

#### Implementación alternativa: Lista v0.2

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

##### Críticas a Lista v0.2

- ¿Qué operación hace `traverse()` con cada elemento individual (imprimir, sumar, etc.)? ¿Hay que especializar de nuevo para cada tipo de operación? 
- ¿Y si hay que especializar de nuevo el recorrido: sólo los pares, sólo los impares, etc.? 

##### Problemáticas de Lista v0.2

- Elevada __complejidad__
- Alta __variabilidad__ no bien tratada --> poca __flexibilidad__, mala __reutilización__

#### Implementación alternativa: Lista v0.3

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

##### Críticas a Lista v0.3

- Si hay que cambiar la operación básica que hace `traverse()` con cada elemento (imprimir, sumar, etc.), ¿cuántos métodos hay que cambiar? Hay muchas dependencias
- Cuanto más variedad de recorridos (la interfaz es mayor), menos flexibilidad para los cambios. Implementación poco flexible

##### Problemáticas de Lista v0.3

- Muchas __dependencias__ --> __acoplamiento__
- Poca __flexibilidad__

#### Implementación alternativa: Lista v0.4

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

##### Ventajas

- Mayor __cohesión__: Las responsabilidades están ahora separadas: `List` almacena, `Iterator` recorre. `List` está más cohesionada
- Uso de __delegación__: la responsabilidad de recorrer se ha delegado hacia otro sitio

### <span style="color:blue">Ocultar la implementación</span>

<a id="ocultacion"></a>

- __Cohesión__: módulos auto-contenidos, independientes y con un
    único propósito
- __Acoplamiento__: minimizar dependencias entre módulos
- __Abstracción__: diferenciar el *qué* y el *cómo*
- __Modularidad__: clases, interfaces y componentes/módulos

#### Alta cohesión, bajo acoplamiento

> Cuando los componentes están aislados, puedes cambiar uno sin preocuparte por el resto. Mientras no cambies las interfaces externas, no habrá problemas en el resto del sistema
>
> -- <cite>[Eric Yourdon](bibliografia.md#yourdon)</cite>

#### Modularidad

Reducir el acoplamiento usando módulos o componentes con distintas responsabilidades, agrupados en bibliotecas

#### Técnicas de ocultación

Hay diversas técnicas para ocultar la implementación...

- __Encapsular__: agrupar en módulos y clases
- __Visibilidad__: `public`, `private`, `protected`, etc.
- __Delegación__: incrementar la cohesión extrayendo funcionalidad pensada para otros propósitos fuera de un módulo
- __Herencia__: delegar _en vertical_
- __Polimorfismo__: ocultar la implementación de un método, manteniendo la misma interfaz de la clase base
- __Interfaces__: usar interfaces bien documentadas

<a id="herencia"></a>

### <span style="color:blue">Herencia: generalización y especialización</span>

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

#### Overriding

En general, en un lenguaje OO es posible sobreescribir o redefinir (_override_) los métodos heredados de una superclase. En algunos lenguajes es obligatorio (en otros es recomendado) especificar explícitamente cuándo un método es redefinido.

##### Ejemplo en Scala

```scala hl_lines="4"
class Complejo(real: Double, imaginaria: Double) {
  def re = real
  def im = imaginaria
  override def toString() =
    "" + re + (if (im < 0) "" else "+") + im + "i"
}
```

##### Ejemplo en Java

¿Qué sucede si no ponemos `@Override` a los métodos redefinidos?

!!! warning "Disclaimer"

    Este ejemplo en Java es realmente la implementación de un diseño incorrecto, pues hay una doble dependencia entre las clases `Real` y `Complejo`. La frontera entre Diseño e Implementación queda aquí un poco difusa.

```java hl_lines="28 32"
class Real {
  double re;
  public Real(double real) {
      re = real;
  }
  public double getRe() { return re; }
  /* Probar a comentar el siguiente método y mantener el
     Override de Complejo::sum(Real other) */
  public Real sum(Real other) {
    return new Real(re + other.getRe());
  }
  /* Probar a comentar el siguiente método y mantener el
     Override de Complejo::sum(Complejo other) */
  public Complejo sum(Complejo other) {
    return new Complejo( re + other.getRe(), other.getIm() );
  }
  public String toString() {
    return String.format("%.1f", re);
  }
}

class Complejo extends Real {
  double im;
  public Complejo(double real, double imaginaria) {
    super(real);
    im = imaginaria;
  }
  @Override
  public Complejo sum(Real other) {
    return new Complejo( re + other.getRe(), im );
  }
  @Override
  public Complejo sum(Complejo other) {
    return new Complejo( re + other.getRe(), im + other.getIm() );
  }
  public Double getIm() { return im; }
  public String toString() {
    return String.format("%.1f", re) + ((im < 0)? "" : "+") + String.format("%.1f", im) + "i";
  }    
}

public class MyClass {
  public static void main(String args[]) {
      Real r = new Real(7.6);
      Complejo c = new Complejo(1.2, 3.4);
      System.out.println("Número real: " + r);
      System.out.println("Número complejo: " + c);
      System.out.println("Número complejo: " + c.sum(r) );
      System.out.println("Número complejo: " + r.sum(c) );
  }    
}
```
Si no se añade `@Override`, podemos llegar a confundirnos y hacer sobrecarga cuando queríamos haber hecho sobreescritura.

### Casting

#### Ejemplo: Aventura v0.1

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

##### Críticas a Aventura v0.1

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

#### Uso correcto de la herencia

Hay dos formas de contemplar la herencia:

- Como **tipo**:
    - Las clases son tipos y las subclases son subtipos
    - Las clases satisfacen la propiedad de __substitución__ (LSP, Liskov Substitution Principle): toda operación que funciona para un objeto de la clase C también debe funcionar para un objeto de una subclase de C

- Como **estructura**:
    - La herencia se usa como una forma cualquiera de estructurar programas
    - Esta visión es **errónea**, pues provoca que no se satisfaga la propiedad LSP

##### Ejemplo: herencia como estructura

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
- Sin embargo, con la estructura `AccountWithFee` < `VerboseAccount` < `Account`, un objeto de tipo `AccountWithFee` no funciona bien cuando se contempla como un objeto `Account`. Considérese la siguiente secuencia:

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

### <span style="color:blue">Polimorfismo</span>

<a id="polimorfismo"></a>

Fenómeno por el que, cuando se llama a una operación de un objeto del que no se sabe su tipo específico, se ejecuta el método adecuado de acuerdo con su tipo.

El polimorfismo se basa en:

- **Enlace dinámico**: se elige el método a ejecutar en tiempo de ejecución, en función de la clase de objeto; es la implementación del *polimorfismo*

- **Moldes (_casting_)**
    - *Upcasting*: Interpretar un objeto de una clase derivada como del mismo tipo que la clase base
    - *Downcasting*: Interpretar un objeto de una clase base como del mismo tipo que una clase derivada suya
