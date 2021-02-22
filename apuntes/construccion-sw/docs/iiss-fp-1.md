# FUNCIONES

## Programación funcional

### <span style="color:blue;"><a id="functional">Interfaces funcionales</a></span>

#### Ejemplo: Comparación de personas - versión con herencia

Ordenación de una lista de nombres de personas:

```java
List<String> nombres = Arrays.asList("Melchor","Gaspar","Baltasar");
Collections.sort(nombres);
```

¿Cómo anticipar en la implementación la posible __variabilidad__ del criterio de ordenación?

- Definir una clase para las personas
- Factorizar la función de comparación: delegar en `compareTo(other)`
- Aprovechamos que el `String` del nombre ya implementa la interfaz `Comparable`

Delegando hacia las subclases:

```java
class Persona implements Comparable {
    private int idPersona;
    private String nombre;
    private java.util.Date fechaNacimiento;

    public Persona() { }
    public Persona(int idPersona, String nombre) {
        this.idPersona = idPersona;
        this.nombre = nombre;
    }

    public int getIdPersona() { return idPersona;  }
    public void setIdPersona(int idPersona) { this.idPersona = idPersona; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public java.util.Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(java.util.Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @Override
    public String toString() {
        return String.format("ID=%1s, Nombre:%2s", idPersona, nombre);
    }

    @Override
    public int compareTo(Persona otra) {
        return nombre.compareTo(otra.getNombre());
    }
}
```

Construir y ordenar una colección de personas:

```java
List<Persona> personas = new ArrayList<Persona>();
personas.add(new Persona(1, "Melchor"));
personas.add(new Persona(2, "Gaspar"));
personas.add(new Persona(3, "Baltasar"));
Collections.sort(personas);
```

¿Y si deseamos ordenar por fecha?

- Alternativa 1: definir subclases `PersonaPorNombre`, `PersonaPorFechaNacimiento`...
  - Mucho código repetido (no cumple DRY)
  - Muchos cambios si se añade un nuevo criterio (no cumple OCP)

- Alternativa 2: No usar herencia, sino composición (no delegar hacia las subclases)

#### Ejemplo: Comparación de personas - versión con composición

Sin usar herencia:

```java
class OrdenarPersonaPorId implements java.util.Comparator<Persona> {
    public int compare(Persona o1, Persona o2) {
        return o1.getIdPersona() - o2.getIdPersona();
    }
}

Collections.sort(personas, new OrdenarPersonaPorId());
```

- Factorizar la función de comparación
- No delegar hacia las subclases
- Delegar en objeto de otra clase que implemente la interfaz `java.util.Comparator`

!!! question "¿Ventajas?"
    La función factorizada (por ejemplo, la implementación de `Comparator`) es sustituible en tiempo de ejecución mediante inyección de dependencias

#### Ejemplo: Comparación de personas - versión con clases anónimas

```java
Collections.sort(
  personas, new java.util.Comparator<Persona>() {
    public int compare(Persona o1, Persona o2) {
      return o1.getIdPersona() - o2.getIdPersona();
    }
  }
);
```

### Clases locales o internas

- Clases **locales** (_inner classes_): declaradas dentro de métodos
- Las clases locales pueden hacer referencia a identificadores declarados en la clase y a variables de solo lectura (`final`) del método en que se declaran

```java
public class EnclosingClass {
  public class InnerClass {
    public int incrementAndReturnCounter() {
      return counter++;
    }
  }

  private int counter;
  {
    counter = 0;
  }

  public int getCounter() {
    return counter;
  }

  public static void main(String[] args) {
    EnclosingClass enclosingClassInstance = new EnclosingClass();
    EnclosingClass.InnerClass innerClassInstance =
      enclosingClassInstance.new InnerClass();
    for( int i = enclosingClassInstance.getCounter();
         (i = innerClassInstance.incrementAndReturnCounter()) < 10; ) {
      System.out.println(i);
    }
  }
}
```

### Predicados

En Java 8, inspirado por la biblioteca _guava_, se incluyen predicados como una forma de interfaz funcional.

En la biblioteca Guava, los [`Iterators`](https://google.github.io/guava/releases/15.0/api/docs/com/google/common/collect/Iterators.html) tienen un método [`filter`](https://google.github.io/guava/releases/15.0/api/docs/com/google/common/collect/Iterators.html#filter) que recibe un objeto de tipo [`Predicate`](https://google.github.io/guava/releases/15.0/api/docs/com/google/common/base/Predicate.html).

Desde Java 8 existe una clase similar [`Predicate`](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html).

#### Ejemplo: partidos de una competición

=== "Con clases anónimas:"

  ```java
  final Predicate<Match> condition = new Predicate<Match>() {
    final Team team1 = new Team("Cadiz CF");
    final Team team2 = new Team("RC Betis");
    public boolean apply(Match match) {
      return ( match.getLocalTeam().equals(team1) && match.getVisitingTeam().equals(team2) );
    }
  };
  Iterator matchesByTeam = Iterators.filter(matches, condition);
  for (matches: matchesByTeam) { ... };
  ```

=== "Sin clases anónimas:"

  ```java
  class FilterByTeam implements Predicate<Match> {
    Team localTeam, visitingTeam;

    public FilterByTeam(Team t1, Team t2) {
        this.localTeam = t1;
        this.visitingTeam = t2;
    }

    public boolean apply(Match match) {
        return match.getLocalTeam().equals(t1) || match.getVisitingTeam().equals(t2);
    }
  }
  ```

Comprobar que, en un cierto grupo de la competición, un mismo partido no está repetido ni se enfrenta un equipo contra sí mismo:

!!! question "Guava y Java 8"
    Guava emplea `FluentIterable` para poder encadenar varios `Iterable` sin que haya problemas con el retorno de null en la programación _fluent_. La biblioteca estándar de Java 8 sustituye la solución del `FluentIterable` por los `Predicate` o por el uso de `StreamSupport` para resolver dicho problema.
    
    Lectura recomendada: [From Guava's FluentIterable via StreamSupport to Java 8 Streams](https://verhoevenv.github.io/2015/08/18/fluentiterable-streamsupport-java8.html)

```java
private void checkMatchesInGroup(List<Match> matchesInGroup) {
  for (Match match: matchesInGroup) {
      Team t1 = match.getLocalTeam();
      Team t2 = match.getVisitingTeam();
      assertNotSame(t1, t2);
      List<Match> firstLeg = FluentIterable.from(matchesInGroup)
                                            .filter(new FilterByTeam(t1, t2))
                                            .toImmutableList();
      assertTrue(firstLeg.size()==1);
      List<Match> secondLeg = FluentIterable.from(matchesInGroup)
                                            .filter(new FilterByTeam(t2, t1))
                                            .toImmutableList();
      assertTrue(secondLeg.size()==0);
  }
}
```

### Clases anónimas interiores

Son clases locales (_inner classes_) declaradas sin nombre; sirven para clases que solo aparecen una vez en la aplicación

=== "Java 7"

```java
class CalculationWindow extends JFrame {
  private volatile int result;
  public void calculateInSeparateThread(final URI uri) {
    new Thread(
      new Runnable() {
        void run() {
          calculate(uri);
          result = result + 10;
        }
      }
    ).start();
  }
}
```

=== "Java 8"

```java
class CalculationWindow extends JFrame {
  private volatile int result;
  public void calculateInSeparateThread(final URI uri) {
    // code () -> { /* code */ } is a closure
    new Thread(() -> {
      calculate(uri);
      result = result + 10;
    }).start();
  }
}
```

### <a id="callbacks">Retrollamadas (_callbacks_)</a>

Un _callback_ o retrollamada es un fragmento de código ejecutable que se pasa como argumento.

#### Implementaciones en C/C++

- Puntero a función:
    ```c
      int (*f)(void)
    ```
- Con puntero asociado a datos:
    ```c
      void (*f)(void *data)
    ```
- _functor_ en C++
    - clase que define `operator()`
    - es una clase y por tanto pueden contener un estado

##### Ejemplo de puntero a función

```c
/* The calling function takes a single callback as a parameter. */
void PrintTwoNumbers(int (*numberSource)(void)) {
    printf("%d and %d\n", numberSource(), numberSource());
}

/* Possible callbacks */
int overNineThousand(void) {
    return (rand() % 1000) + 9001;
}

int meaningOfLife(void) {
    return 42;
}

int main(void) {
    PrintTwoNumbers(&rand);
    PrintTwoNumbers(&overNineThousand);
    PrintTwoNumbers(&meaningOfLife);
    return 0;
}
```

##### Ejemplo de puntero a datos

```c
/* Type of function used for the callback */
typedef void (*event_cb_t)(const struct event *evt, void *userdata);

/* Define a function to register a callback */
int event_cb_register(event_cb_t cb, void *userdata);

/ * Register the callback */
static void my_event_cb(const struct event *evt, void *data)
{
    /* do stuff and things with the event */
}

event_cb_register(my_event_cb, &my_custom_data);

/* struct to store the callback in the event dispatcher */
struct event_cb {
    event_cb_t cb;
    void *data;
};

/* Execute the callback */
struct event_cb *callback;
...
callback->cb(event, callback->data);
```

##### Ejemplo de functor

```cpp
// this is a functor
struct add_x {
  add_x(int x) : x(x) {}
  int operator()(int y) { return x+y; }

  private:
    int x;
};

add_x add42(42);     // create an instance of the functor class
int i = add42(8);    // and "call" it
assert(i == 50);     // and it added 42 to its argument

std::vector<int> in; // assume this contains a bunch of values
std::vector<int> out;
// Pass a functor to std::transform, which calls the functor on every element 
// in the input sequence, and stores the result to the output sequence
std::transform(in.begin(), in.end(), out.begin(), add_x(1)); 
assert(out[i] == in[i] + 1); // for all i
```

##### Functor vs function pointer

```cpp
//Functor
struct add_x {
  add_x(int y):x(y){}
  int operator()(int y) { return x+y; }

  private:
    int x;
};

//Function pointer
int (func)(int x)
{
    return ++x;
}

std::vector<int> vec();
//fill vec with 1 2 3 4 5

int (*f)(int) = func; // function pointer initialization
std::transform(vec.begin(),vec.end(),f);        // pass function pointer
std::transform(vec.begin(),vec.end(),add_x(1)); // pass functor
```
