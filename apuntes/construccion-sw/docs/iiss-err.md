# CALIDAD

## <span style="color:blue;">Calidad</span>

<a id="errores"></a>

### Tratamiento de errores

#### Códigos de error

Un ejemplo habitual de tratamiento de errores con __códigos de error__ en un lenguaje como C:

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

!!! failure Mala práctica
    Los programadores intentan evitar añadir nuevos motivos de error, porque eso significa tener que volver a compilar y desplegar todo el código.

### Excepciones

Muchos lenguajes usan __excepciones__ en lugar de códigos de error:

```java hl_lines="2 3 4"
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

!!! success "Ventaja"
    Las nuevas excepciones son derivadas de una clase base `Exception`, lo que facilita la definición de nuevos motivos de error.

!!! question "<span style="color:red;">¿Dónde se produce el error?</span>"
    Si se eleva una excepción en el ejemplo anterior, ¿en cuál de las instrucciones del bloque `try` se ha producido?

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

#### Excepciones en Java

##### Tipos de excepciones en Java 

**Checked**
: Instancias de clases derivadas de `java.lang.Throwable` (menos `RuntimeException`). Deben declararse en el método mediante `throws` y obligan al llamador a tratar la excepción.

**Unchecked**
: Instancias de clases derivadas de `java.lang.RuntimeException`. No se declaran en el método y no obligan al llamador a tratar la excepción.

!!! note "¿Qué implica elevar una excepción `e` en Java?"
    1. Deshacer (_roll back_) la llamada a un método...
    2. ...hasta que se encuentre un bloque catch para el tipo de `e` y...
    3. si no se encuentra, la excepción es capturada por la JVM, que detiene el programa

##### Tratamiento de excepciones en Java

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

#### Recomendaciones

Incluir el __contexto__ de la ejecución:

- Incluir información suficiente con cada excepción para determinar el motivo y la ubicación de un error
- No basta con el *stack trace*
- Escribir mensajes informativos: operación fallida y tipo de fallo

Los beneficios de las excepciones _checked_ en Java son mínimos: [¿por qué](https://testing.googleblog.com/2009/09/checked-exceptions-i-love-you-but-you.html). Se recomienda usar solamente excepciones __unchecked__.

- C\#, C++, Python o Ruby no ofrecen excepciones _checked_.
- Scala no usa excepciones _checked_ como Java: [Scala exception handling](https://madusudanan.com/blog/scala-tutorials-part-24-exception-handling/#Intro)
- Se paga el precio de violar el principio OCP (_Open-Closed Principle_): si lanzamos una excepción _checked_ desde un método y el `catch` está tres niveles por encima, hay que declarar la excepción en la signatura de todos los métodos que van entre medias. Esto significa que un cambio en un nivel bajo del software puede forzar cambios en niveles altos.

#### Transformación de excepciones

##### Transformar en unchecked

Muchas APIs de Java lanzan excepciones checked cuando deberían ser unchecked

__Ejemplo__: Al ejecutar una consulta mediante `executeQuery` en el API de JDBC se lanza una excepción `java.sql.SQLException` (de tipo checked) si la SQL es errónea.

**Solución**:

Transformar las excepciones checked en unchecked:

```java
  try {
    // Codigo que genera la excepcion checked
  } catch (Exception ex) {
    throw new RuntimeException("Unchecked exception", ex)
  }
```

##### Excepciones encapsuladas

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

!!! question "Excesiva duplicación de código"
    La llamada a `reportPortError()` se repite mucho.
    <span style="color:red;">¿Cómo evitar la excesiva duplicación?</span>

??? success "Solución: Excepción encapsulada"
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

#### Las excepciones son excepcionales

__Recomendación de uso__: Usar excepciones para problemas excepcionales (eventos inesperados)

!!! example "Excepciones tras la apertura de ficheros"
    <span style="color:red;">¿Usar excepciones cuando se intenta abrir un fichero para leer y el fichero no existe?</span>

Depende de si el fichero debe estar ahí

- Caso en que se debe lanzar una excepción:

  ```java
  public void open_passwd() throws FileNotFoundException {
    // This may throw FileNotFoundException...
    ipstream = new FileInputStream("/etc/passwd");
    // ...
  }
  ```

- Caso en que no se debe lanzar una excepción:

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

!!! tip "Solo para JDK $\geq$ 5.0"

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

El uso de `assert` es una buena forma de documentar, pero no resuelve el problema.

#### Optionals

- En la mayoría de lenguajes no hay forma satisfactoria de tratar con _nulls_ pasados como argumento accidentalmente.
- Para eso están los _options_ u _optionals_, disponibles actualmente en muchos lenguajes como:
    - [Scala `Option`](https://www.tutorialspoint.com/scala/scala_options.htm)
    - [Java 8](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html) `#!java java.util.Optional`
    - C++17 `#!cpp std::optional`
- [TypeScript recomienda usar `undefined`](https://github.com/Microsoft/TypeScript/wiki/Coding-guidelines#null-and-undefined) (algo que no se ha inicializado) en lugar de `null` (algo que no está disponible)

!!! note "Lecturas recomendadas"
    - [ ] Leer el [Tutorial de `#!scala Option`](https://www.tutorialspoint.com/scala/scala_options.htm) en Scala

##### Scala `Option`

En Scala, `#!scala Option[T]` es un contenedor de un valor opcional de tipo T.

- Si el valor de tipo T está presente, `#!scala Option[T]` es una intancia de `#!scala Some[T]` que contiene el valor presente de tipo T.
- Si el valor está ausente, `#!scala Option[T]` es el objeto `#!scala None`.

!!! note "Valores vacíos en Scala"
    Conocer las diferencias entre [`Null`, `null`, `Nil`, `Nothing`, `None` y `Unit`](https://www.geeksforgeeks.org/scala-null-null-nil-nothing-none-and-unit/) en Scala

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

##### Java 8 `Optional`

!!! note "Lecturas recomendadas"
    - [ ] Leer [Java 8 Optional in Depth](https://www.mkyong.com/java8/java-8-optional-in-depth/).
    - [ ] Leer [Jugando con Optional en Java 8](https://www.adictosaltrabajo.com/2015/03/02/optional-java-8/).

__Ejemplo en Java 8 con sintaxis imperativa__

```java
private static Optional<Double> getDurationOfAlbumWithName(String name) {
    Album album;
    Optional<Album> albumOptional = getAlbum(name);
    if (albumOptional.isPresent()) { // equivalente a albumOptional == null
        album = albumOptional.get();
        Optional<List<Track>> tracksOptional = getAlbumTracks(album.getName());
        double duration = 0;
        if (tracksOptional.isPresent()) { // equivalente a tracksOptional == null
            List<Track> tracks = tracksOptional.get();
            for (Track track : tracks) {
                duration += track.getDuration();
            }
            return Optional.of(duration);
        } else {
            return Optional.empty();
        }
    } else {
        return Optional.empty();
    }
}
```

Al ejecutar varias operaciones seguidas que pueden devolver null, el nivel de anidamiento del código aumenta y queda menos claro (se mezcla código funcional con código de gestión de errores). Solución...

__Ejemplo en Java 8 con sintaxis *fluent*__

```java
Optional<Double> getDurationOfAlbumWithName(String name) {
    Optional<Double> duration = getAlbum(name)
            .flatMap((album) -> getAlbumTracks(album.getName()))
            .map((tracks) -> getTracksDuration(tracks));
    return duration;
}
```

- La función `map` comprueba si el `Optional` que recibe está vacío. Si lo está devuelve un `Optional` vacío y, si no, aplica la función que le hemos pasado por parámetro, pasándole el valor del `Optional`. Es decir, si el `Optional` está vacío, el método `map` no hace nada. Esto sirve para concatenar operaciones sin necesidad de comprobar en cada momento si el `Optional` está vacío.
- Cuando queremos encadenar distintas operaciones que devuelvan `Optional`, es necesario usar `flatMap`, ya que si no acabaríamos teniendo un `Optional<Optional<Double>>`.

```java
private static double getDurationOfAlbumWithName(String name) {
    return getAlbum(name)
            .flatMap((album) -> getAlbumTracks(album.getName()))
            .map((tracks) -> getTracksDuration(tracks))
            .orElse(0.0);
}
```

- Podríamos seguir devolviendo `Optional` por toda la aplicación, pero en algún momento tenemos que decidir qué hacer en caso de que el valor que queremos no estuviera presente. Para ello se usa `orElse()` para proporcionar un valor alternativo en caso de que el valor no estuviera presente.


__Ejemplo sin `Optional`__

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

__Ejemplo con `Optionals`__

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

##### Carencias de Optional

- El tratamiento de errores clásico del lenguaje C (con el que empezábamos este capítulo) se basa en devolver un valor especial que contiene un _código de error_ (normalmente negativo) que indica qué ha salido mal. Se podrían representar tantos motivos de error como posibles valores devueltos.
- Los `Optional` no ofrecen la posibilidad de que decir qué es lo que ha salido mal (en caso de que no haya valor a devolver).
- Por tanto, no son apropiados para métodos en los que pueden salir varias cosas mal y no solo una (que no exista un valor a devolver)
- Lenguajes como Scala proponen alternativas como `Either` y `Validation`.

```scala
object EitherLeftRightExample extends App {

  /**
   * A method to demonstrate how to declare that a method returns an Either,
   * and code that returns a Left or Right.
   */
  def divideXByY(x: Int, y: Int): Either[String, Int] = {
      if (y == 0) Left("Dude, can't divide by 0")
      else Right(x / y)
  }
  
  // a few different ways to use Either, Left, and Right
  println(divideXByY(1, 0))
  println(divideXByY(1, 1))
  divideXByY(1, 0) match {
      case Left(s) => println("Answer: " + s)
      case Right(i) => println("Answer: " + i)
  }

}
```
