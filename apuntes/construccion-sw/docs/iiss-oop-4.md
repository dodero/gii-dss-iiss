# OBJETOS - Código duplicado

<a id="caso4"></a>

## Caso 4 - Cálculo de nóminas

### Código duplicado

<a id="nominas"></a>

#### Implementación de nóminas v0.1

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

#### Nóminas v0.2

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
    void setSalary( float s ) { yearlyGrossSalary=s; }   
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

#### Nóminas v0.3

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

### <span style="color:blue;">Refactoring</span>

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

#### Motivos para refactoring

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

### <span style="color:blue;">Código duplicado</span>

!!! info "Lectura recomendada"
    Hunt & Thomas. [The Pragmatic Programmer](bibliografia.md#pragmatic), 1999.
    Capítulo *DRY—The Evils of Duplication*

#### ¿Por qué no duplicar?

- Mantenimiento
- Cambios (no sólo a nivel de código)
- Trazabilidad

#### Causas de la duplicación

- __Impuesta__: No hay elección
- __Inadvertida__: No me he dado cuenta
- __Impaciencia__: No puedo esperar
- __Simultaneidad__: Ha sido otro

#### <span style="color:blue;">Principio DRY – *Don't Repeat Yourself!*</span>

by [Hunt & Thomas (1999)](bibliografia.md#pragmatic)

> Copy and paste is a design error
>
> – <cite> McConnell (1998) </cite>

#### Duplicación impuesta

La gestión del proyecto así nos lo exige. Algunos ejemplos:

- Representaciones múltiples de la información:
    - varias implementaciones de un TAD que necesita guardar elementos de distintos tipos, cuando el lenguaje no permite genericidad
    - el esquema de una BD configurado en la BD y en el código fuente a través de un [ORM](http://www.agiledata.org/essays/mappingObjects.html)
- Documentación del código:
    - código incrustado en javadocs
- Casos de prueba:
    - pruebas unitarias con jUnit
- Características del lenguaje:
    - C/C++ header files
    - IDL specs

!!! note "Cómo evitaba Java la duplicación en sus _containers_"
    Cuando el lenguaje no tenía capacidad de usar tipos genéricos (hasta el JDK 1.4), podría aparecer la necesidad de duplicar código a la hora de implementar un TAD contenedor, pues habría que repetir todo el código de manejo del TAD para cada tipo de elemento contenido. Para evitarlo, Java usó un _workaround_: todas las clases en Java heredan de `Object`. Así una clase que implementara un TAD contenedor de elementos de otra clase, tan solo tenía que declarar los elementos contenidos de tipo `Object`. Más tarde (a partir del JDK 1.5) introdujo los tipos genéricos y ya no era necesario usar dicho _workaround_ basado en `Object` para evitar la duplicación




##### Técnicas de solución

- __Generadores de código__: para evitar duplicar representaciones múltiples de la información
- Herramientas de __ingeniería inversa__: para generar código a partir de un esquema de BD – v.g. [jeddict](https://jeddict.github.io/) para crear clases JPA, visualizar y modificar BDs y automatizar la generación de código Java EE.
- __Plantillas__: Tipos genéricos del lenguaje (Java, C++, TypeScript, etc.) o mediante un motor de plantillas – v.g. [Apache Velocity](http://velocity.apache.org/) template language ([VTL](http://velocity.apache.org/engine/2.0/user-guide.html#velocity-template-language-vtl-an-introduction))
- __Metadatos__: Anotaciones @ en Java, decoradores en TypeScript, etc.
- Herramientas de __documentación__ (v.g. [asciidoctor](http://asciidoctor.org/): [inclusión de ficheros](http://asciidoctor.org/docs/asciidoc-syntax-quick-reference/#include-files) y [formateo de código fuente](http://asciidoctor.org/docs/asciidoc-syntax-quick-reference/#source-code)).
- Herramientas de __[programación literaria](http://www.literateprogramming.com/)__
- Ayuda del __IDE__
- Herramientas de *property-based testing*, como [Hypothesis](https://pypi.org/project/hypothesis/) (python), [RapidCheck](https://github.com/emil-e/rapidcheck) (C++), [jqwik](https://jqwik.net/)  (Java) o [QuickCheck](https://en.wikipedia.org/wiki/QuickCheck) (originalmente para Haskell).

!!! note "Property-based testing"
    - ¿Cómo reducir la duplicación de código al programar pruebas unitarias?
    - Ejemplo de property-based testing con [Hypothesis](https://pypi.org/project/hypothesis/) en Python:
    ```python
    from hypothesis import given
    import hypothesis.strategies as some

    @given(some.lists(some.integers()))
    def test_list_size_is_invariant_across_sorting(a_list):
      original_length = len(a_list)
      a_list.sort()
      assert len(a_list) == original_length

    @given(some.lists(some.text()))
    def test_sorted_result_is_ordered(a_list):
      a_list.sort()
      for i in range(len(a_list) - 1):
        assert a_list[i] <= a_list[i + 1]
    ```
    - Leer el Consejo nº 71 del libro de [Hunt & Thomas (2020)](bibliografia.md#pragmatic2).


#### Duplicación inadvertida

Normalmente tiene origen en un diseño inapropiado.

Fuente de numerosos problemas de integración.

##### Ejemplo: código duplicado – versión 1

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

##### Ejemplo: aplicando memoization – versión 2

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

#### <span style="color:blue;">Principio de acceso uniforme</span>

> All services offered by a module should be available through a uniform notation, which does not betray whether they are implemented through storage or through computation
>
> <cite>[B. Meyer](bibliografia.md#meyer)</cite>

Conviene aplicar el principio de acceso uniforme para que sea más fácil añadir mejoras de rendimiento (v.g. caching)

##### Ejemplo: acceso uniforme en C# – versión 3

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

##### Ejemplo: acceso uniforme en Scala

Llamadas a métodos con paréntesis:

```scala
class Complejo(real: Double, imaginaria: Double) {
  def re() = real
  def im() = imaginaria
  override def toString() =
    "" + re() + (if (im() < 0) "" else "+") + im() + "i"
}

object NumerosComplejos {
  def main() : Unit = {
    val c = new Complejo(1.2, 3.4)
    println("Número complejo: " + c.toString())
    println("Parte imaginaria: " + c.im())
  }
}
```

Llamadas a métodos sin paréntesis, igual que si fueran atributos:

```scala
class Complejo(real: Double, imaginaria: Double) {
  def re = real
  def im = imaginaria
  override def toString() =
    "" + re + (if (im < 0) "" else "+") + im + "i"
}

object NumerosComplejos {
  def main() : Unit = {
    val c = new Complejo(1.2, 3.4)
    println("Número complejo: " + c)
    println("Parte imaginaria: " + c.im)
  }
}
```

#### Duplicación por impaciencia

- Los peligros del *copy&paste*
- "Vísteme despacio que tengo prisa" (_shortcuts make for long delays_). Ejemplos:
    - Meter el `main` de Java en cualquier clase
    - Fiasco del año 2000

#### Duplicación por simultaneidad

- No resoluble a nivel de técnicas de construcción
- Hace falta metodología, gestión de equipos + herramientas de comunicación

<a id="ortogonalidad"></a>

### <span style="color:blue;">Ortogonalidad</span>

Dos componentes A y B son ortogonales ($A \perp B$) si los cambios en uno no afectan al otro. Suponen más independencia y menos acoplamiento. Por ejemplo:

- La base de datos debe ser ortogonal a la interfaz de usuario
- En un helicóptero, los mandos de control no suelen ser ortogonales

!!! quote "A Nonorthogonal System ([Hunt, 2020](bibliografia.md#pragmatic2))"
    Helicopters have four basic controls. The cyclic is the stick you hold in your right hand. Move it, and the helicopter moves in the corresponding direction. Your left hand holds the collective pitch lever. Pull up on this and you increase the pitch on all the blades, generating lift. At the end of the pitch lever is the throttle. Finally you have two foot pedals, which vary the amount of tail rotor thrust and so help turn the helicopter.

    “Easy!,” you think. “Gently lower the collective pitch lever and you’ll descend gracefully to the ground, a hero.” However, when you try it, you discover that life isn’t that simple. The helicopter’s nose drops, and you start to spiral down to the left. Suddenly you discover that you’re flying a system where every control input has secondary effects. Lower the left-hand lever and you need to add compensating backward movement to the right-hand stick and push the right pedal. But then each of these changes affects all of the other controls again. Suddenly you’re juggling an unbelievably complex system, where every change impacts all the other inputs. Your workload is phenomenal: your hands and feet are constantly moving, trying to balance all the interacting forces.

    Helicopter controls are decidedly not orthogonal.

#### Beneficios de la ortogonalidad

##### Mayor productividad

- Es más fácil escribir un componente pequeño y auto-contenido que un bloque muy grande de código. El tiempo de desarrollo y __pruebas__ se reduce
- Se pueden combinar unos componentes con otros más fácilmente. Mayor __reutilización__.
- En teoría, si $A \perp B$, el componente A sirve para $m$ propósitos y B sirve para $n$, entonces $A \cup B$ sirve para $m \times n$ propósitos.
- La falta de cohesión perjudica la reutilización --> ¿y si hay que hacer una nueva versión gráfica de una aplicación de línea de comandos que lleva incrustada la escritura en consola con `System.out.println`? Pueden descohesionar!

##### Menor riesgo

- Defectos aislados, más fáciles de arreglar
- Menor __fragilidad__ del sistema global. Los problemas provocados por cambios en un área se limitan a ese área
- Más fácil de __probar__, pues será más fácil construir pruebas individuales de cada uno de sus componentes (por ejemplo, las técnicas de _[mocking](https://en.wikipedia.org/wiki/Mock_object)_ son más sencillas)

#### Niveles de aplicación de la ortogonalizad

La ortogonalidad es aplicable a:

- el diseño
- la codificación
- las pruebas
- bibliotecas
- la documentación

A nivel de _diseño_, los patrones de diseño y las arquitecturas como MVC facilitan la construcción de componentes ortogonales.

!!! note "Lectura recomendada"
    Leer el [Topic 10: Orthogonality](https://learning-oreilly-com.bibezproxy.uca.es/library/view/the-pragmatic-programmer/9780135956977/f_0028.xhtml#orthogonality) de ([Hunt, 2020](bibliografia.md#pragmatic2)).

#### Técnicas de codificación

Técnicas de codificación para fomentar la ortogonalidad:

- Hacer **refactoring**
- Codificar **patrones** de diseño: strategy, template method, etc.
- Evitar datos globales y __singletons__: ¿qué pasaría si hubiera que hacer una versión *multithreaded* de una aplicación?
- **Inyectar**: pasar explícitamente el contexto (dependencia) como parámetro a los constructores
- Usar **anotaciones** (Java), decoradores (TypeScript) o atributos (C#)
- **Desacoplar**: Ley de <span>*Demeter*</span>—No hables con extraños
- Usar programación orientada a **aspectos**

##### Desacoplar - ley de Demeter

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

!!! note "Lectura recomendada"
    Leer el [Topic 28: Decoupling](https://learning-oreilly-com.bibezproxy.uca.es/library/view/the-pragmatic-programmer/9780135956977/f_0049.xhtml#coupling) de ([Hunt, 2020](bibliografia.md#pragmatic2)).


##### Inyectar el contexto

Pasar explícitamente el contexto (dependencia) como parámetro a los constructores de la clase

######  Ejemplo: patrón estrategia

En el patrón de diseño _strategy_, pasar el contexto a la estrategia en su creación

######  Ejemplo: caballeros de la mesa redonda

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

##### Ley de Demeter para funciones

Los métodos de un objeto solo deben hacer llamadas a métodos...

1. __propios__ 
2. de objetos pasados como __parámetros__
3. de objetos __creados__ por ellos mismos
1. de objetos __declarados__ en el mismo método

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

##### Interfaces _fluent_

!!! warning "Excepción a la ley de Demeter"
    Hay una excepción notable a la prohibición de encadenar llamadas a funciones de la ley de Demeter. Esta regla no aplica si es muy poco probable que haya cambios en las cosas que se encadenan. En la práctica, cualquier parte de tu aplicación debe considerarse como algo que es probable que cambie; cualquier elemento de una biblioteca de un tercero debe considerarse volátil, en particular si quienes mantienen dicha biblioteca suelen cambiar su API de una versión a otra.

Las librerías que vienen con el lenguaje suelen ser bastante estables, así que ejemplos de código como el siguiente son aceptables como excepción a esta interpretación de la ley de Demeter:

```java
List<String> myList =
    Arrays.asList("a1", "a2", "b1", "c2", "c1");

myList
    .stream()
    .filter(s -> s.startsWith("c"))
    .map(String::toUpperCase)
    .sorted()
    .forEach(System.out::println);
```

Los métodos `stream`, `filter`, `map`, `sorted` y `forEach` son parte de las nuevas _interfaces funcionales_ para manejar _streams_, incorporadas a las colecciones (v.g. `List`) del lenguaje desde la versión Java 8. Este tipo de interfaces como la del API de streams de Java se conoce como [_fluent interfaces_](https://en.wikipedia.org/wiki/Fluent_interface).

!!! warning "La programación con streams se tratará en el capítulo sobre [Programación basada en Eventos](iiss-evp-1.md)"

!!! warning "Las interfaces funcionales se tratarán en el capítulo sobre [Programación Funcional](iiss-fp-1.md)"


##### Críticas a la ley de Demeter

 La ley de Demeter, ¿realmente ayuda a crear código más mantenible?

###### Ejemplo: pintar gráficos de grabadoras

- Pintar un gráfico con los datos registrados por una serie de grabadoras (`Recorder`) dispersas por el mundo.
- Cada grabadora está en una ubicación (`Location`), que tiene una zona horaria (`TimeZone`).
- Los usuarios seleccionan (`Selection`) una grabadora y pintan sus datos etiquetados con la zona horaria correcta...

  ```java
  public void plotDate(Date aDate, Selection aSelection) {
    TimeZone tz = aSelection.getRecorder().getLocation().getZone();
  }
  ```

###### Críticas

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

#### Ortogonalidad en toolkits y bibliotecas

Muchas bibliotecas actuales implementan la ortogonalidad a través de metadatos, o atributos o etiquetas (@ _tag_), también llamados [anotaciones](iiss-oop-3.md#dependencias-en-java) en Java y [decoradores](iiss-oop-3.md#decoradores-en-typescript) en TypeScript.

Los metadatos se emplean para proporcionar propósitos específicos, como v.g. persistencia de objetos, transacciones, etc. Por ejemplo, Spring o EJB utilizan anotaciones @ declarativas para expresar la transaccionalidad de una operación o la persistencia de una propiedad de una clase fuera del método que debe ejecutar dichas funcionalidades.

Otro método para implementar la ortogonalidad es usar [Aspectos](iiss-aop.md) y _Aspect-Oriented Programming_ (AOP). Este método es empleado por el framework Spring.

!!! warning "Estudiar ahora el capítulo [Aspectos](iiss-aop.md)"

!!! warning "Estudiar luego el capítulo [Calidad](iiss-err.md)"

