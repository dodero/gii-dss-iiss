<a id="aspectos"></a>

# ASPECTOS

<a id="caso5"></a>

## Caso 5 - Editor de figuras

### Ortogonalidad con aspectos

#### Ejemplo: editor de figuras

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

#### Implementación sin aspectos

##### Versión 1 sin aspectos

Solo detecta el cambio de los extremos de una línea.

`Line` $\dashrightarrow$ `MoveTracking`

```java hl_lines="9 13 31"
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

##### Versión 2 sin aspectos

También detecta el cambio de coordenadas de un punto.

- `Line` $\dashrightarrow$ `MoveTracking`
- `Point` $\dashrightarrow$ `MoveTracking`

```java hl_lines="25 29"
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

##### Versión 3 sin aspectos

Las colecciones de figuras son complejas. Las estructuras de objetos son jerárquicas y se producen eventos asíncronos:

![colección de figuras](./figuras/aspectj-2.png)

La versión 2 hace que un cambio en cualquier elemento provoque un refresco de todas las figuras.

Mejor monitorizar las figuras que cambian...

Decidimos modificar la implementación: cambiar el método `setFlag` por `collectOne`, indicando la figura que se mueve.

```java hl_lines="9 13 25 29 34 36 40"
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
- __advice__ = acción a tomar por la especificación de un aspecto dado en un determinado _joinpoint_.
  - Interceptan la ejecución de un _joinpoint_. Hay una cadena de interceptores alrededor de cada _joinpoint_.
  - Tipos de _advice_: _after_, _before_, _around_, etc. 
- __pointcut__ = predicado que define cuándo se aplica un _advice_ de un aspecto en un _jointpoint_ determinado. Se asocia un _advice_ con la expresión de un _pointcut_ y se ejecuta el _advice_ en todos los _joinpoint_ que cumplan la expresión del _pointcut_.

#### Implementación con aspectos

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

##### Versión 1 con aspectos

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

##### Versión 2 con aspectos

- `Line` $\not\dashrightarrow$ `MoveTracking`
- `Point` $\not\dashrightarrow$ `MoveTracking`

```java hl_lines="12 13"
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

Ejemplos de pointcut:

```java
call(void Figure.set*(..))

call(public * Figure.* (..))
```


##### Versión 3 con aspectos

- `Line` $\perp$ `MoveTracking`
- `Point` $\perp$ `MoveTracking`

Versión más ortogonal. Todos los cambios están concentrados en un solo aspecto.

```java hl_lines="2 3 9 10 16 17"
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

### Lecturas recomendadas de AspectJ

- [Lenguaje de AspectJ](https://www.eclipse.org/aspectj/doc/next/progguide/printable.html#language)
- [Introducción a AspectJ](https://www.eclipse.org/aspectj/doc/next/progguide/printable.html#starting-aspectj)

### Ejercicios: AspectJ y Spring AOP

- [Introducción a AspectJ](http://www.baeldung.com/aspectj)
- [Introducción a Spring AOP](http://www.baeldung.com/spring-aop)
