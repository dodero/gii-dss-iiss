# ASERCIONES Y CONTRATOS

## Aserciones y contratos

### <span style="color:blue;">Aserciones</span>

<a id="assert"></a>

!!! quote  "Oscar Wilde, The Picture of Dorian Gray"
    <cite>There is a luxury in self-reproach. When we blame ourselves we feel no one else has a right to blame us.</cite>




#### Programación asertiva

Ejemplos de situaciones que _no van a ocurrir nunca_:

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
      assert(sorted[i] <= sorted[i+1]);
    }
```

#### Aserciones e invariantes

Las aserciones sirven para expresar **invariantes**.

__Invariante__
: Condición que se puede considerar cierta durante la ejecución de un programa o de parte del mismo. Es un predicado lógico que se debe mantener siempre cierto durante una cierta fase de la ejecución.

Por ejemplo, una _invariante de bucle_ es una condición que es cierta al principio y al final de cada ejecución de un bucle

#### Aserciones en Java

Forma 1:

```java
assert Expression1;
```

Forma 2:

```java
assert Expression1 : Expression2;
```

- `Expression1` es `boolean`
- `Expression2` devuelve un valor que es pasado al constructor de `AssertionError`, que usa una representación en forma de `String` del valor como detalle del mensaje

En versiones antiguas del JDK, notificar al compilador que las acepte:

```shell
javac -source 1.4 *.java
```

Las aserciones en Java imponen un alto coste en rendimiento y puede ser conveniente desabilitarlas en tiempo de ejecución:

```shell
java [ -enableassertions | -ea  ] [:<package name>"..." | :<class name> ]
java [ -disableassertions | -da ] [:<package name>"..." | :<class name> ]
```

##### ¿Gestión de errores?

Las aserciones no son para gestión de errores:

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

##### Efectos colaterales

Cuidado con los efectos colaterales de las expresiones de una aserción:

```java hl_lines="2"
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

#### Tipos de invariantes

##### Invariantes internas

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

##### Invariantes de control de flujo

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

Añadir:

```java
default:
  assert false : suit;
```

o también (protección aunque se deshabiliten las aserciones, pero sin coste extra):

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

##### Invariantes de clase

Son un tipo de invariantes internas que se aplican a todas las instancias de una clase, en todos los momentos, excepto cuando una instancia está en transición de un estado consistente a otro.

Por ejemplo, en un árbol binario equilibrado, una invariante de clase puede indicar que está ordenado y equilibrado:

1. Añadir código en Java:

  ```java
  // Returns true if this tree is properly balanced
  private boolean isBalanced() {
    ...
  }
  ```

2. Todo constructor y método _público_ debe llamar a `#!java assert isBalanced();` antes del `return`.

Es recomendable incluir comprobaciones de invariantes de clase al principio de los métodos de clases cuyo estado es modificable por otras clases (v.g. _setters_).

##### *Idiom* para definir aserciones finales

A veces hace falta guardar datos antes de hacer un cómputo, para poder luego comprobar una condición cuando el cómputo se haya completado.

Ejemplo de cómo hacerlo con una _inner class_ que guarda el estado de variables:

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

### <span style="color:blue;">Programación por contratos</span>

<a id="contracts"></a>

#### Contrato

- Un contrato entre dos partes define derechos y responsabilidades por ambas partes
- Define las repercusiones por incumplimiento del contrato

#### Design By Contract (DBC)

- Desarrollado para lenguaje *Eiffel* por Bertrand Meyer
- Documentar y aceptar los derechos y responsabilidades de cada módulo de software para asegurar la correción de un programa
- Un programa correcto es aquél que hace nada más y nada menos que lo que dice hacer

#### Precondiciones, postcondiciones e invariantes

##### Precondición

- Qué debe ser cierto antes de llamar a una rutina/método (sus requisitos)
- Una rutina jamás debe ser llamada si se violan sus precondiciones
- Es responsabilidad del que la llama hacer que se cumplan

##### Postcondición

- Qué garantiza la rutina: estado del mundo cuando la rutina/método termina
- Implica que la rutina debe finalizar: no puede haber bucles infinitos

##### Invariante de clase

- Condición que se cumple para todas las instancias de la clase, desde la perspectiva del llamador
- Durante el procesamiento interno, la invariante puede no cumplirse, pero sí cuando la rutina termina y se devuelve el control al llamador
- Una clase no puede dar permiso de escritura sin restricciones sobre las propiedades (_data members_) que participan en la definición de la invariante

#### Ejemplo: Raíz cuadrada en Eiffel

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

Si se llega a pasar un número negativo, Eiffel imprime el error `sqrt_arg_must_be_positive` en tiempo de ejecución y una traza de la pila (En otros lenguajes, como Java, se devolvería un `Nan`).

#### Ejemplo: Cuenta Bancaria

##### Cuenta Bancaria sin contratos

```eiffel
class ACCOUNT
feature
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

##### Cuenta Bancaria con contratos

```eiffel
class ACCOUNT
create
    make
feature
    -- ... Attributes as before:
      balance, minimum_balance, owner, open ...
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
    -- Initialize account with initial balance.
      require
        initial >= minimum_balance
      do
        balance := initial
      end
invariant
    balance >= minimum_balance

end -- class ACCOUNT
```

<!--
__Forma corta__ del contrato:

```eiffel
class interface ACCOUNT
create
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
    may_withdraw (...): BOOLEAN is ...
end -- class ACCOUNT
```
-->

#### Contratos en los lenguajes

!!! note "Contratos en Java"
    - _iContract_, inactiva, recuperada en Java Contract Suite o [JContractS](http://jcontracts.sourceforge.net/)
    - _Contracts for Java_ o [Cofoja](https://github.com/nhatminhle/cofoja)
    - Etc.

!!! note "Actividad: Contratos en Scala"
    - Scala permite especificar aserciones (`assert`), precondiciones (`require`), postcondiciones (`ensuring`) e invariantes (`assume`).
    - Ejemplo:
    ```scala
    def divide(x: Int, y: Int): Int = {
      require(x > y, s"$x > $y")
      require(y > 0, s"$y > 0")

      x / y
    } ensuring (_ * y == x)
    ```
    - [ ] Seguir el tutorial [Design by Contract](https://madusudanan.com/blog/scala-tutorials-part-29-design-by-contract/)

!!! note "Actividad: ¿Hay contratos en C++?"
    Aún no hay contratos en C++17 ni en C++20.

    - [ ] Ver el video de J. D. García sobre [Contracts programming after C++17](https://www.youtube.com/watch?v=IBas3S2HtdU): Desde el minuto 4'10''


##### Ejemplo: Java + iContract

Java no permite especificar contratos (los _assert_ no son lo mismo). Así que hay que utilizar extensiones como _iContract_

__Ejemplo__: Inserción en una lista ordenada

```java
/**
  * @invariant forall Node n in elements() |
  *    n.prev() != null
  *      implies
  *         n.value().compareTo(n.prev().value()) > 0
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

- Eiffel no permite que se pueda cambiar el valor de un parámetro (es inmutable)
- En C++ usar `const`
- Opciones en Java:
    - Usar `final` para marcar un parámetro constante. Sin embargo, las subclases podrían redefinir los parámetros y volver a hacerlos mutables. Además `final` se aplica a la referencia, no al objeto en sí.
    - Usar `variable@pre` de _iContract_
- Muchos lenguajes funcionales (Lisp, Haskell, Erlang, Clojure, etc.) definen inmutabilidad por defecto

##### ¿Por qué la inmutabilidad?

- Por rendimiento (v.g. `String` en Java): si es inmutable, para copiar un objeto basta con copiar la referencia (_interning_)
- Por _thread-safety_ para código concurrente

#### Código perezoso

- Se recomienda escribir código "perezoso" para los contratos: ser estricto en lo que se acepta al empezar y prometer lo menos posible al terminar.
- Si un contrato indica que se acepta cualquier cosa y promete la luna a cambio, habrá que escribir un montón de código!

#### _Dead programs tell no lies_

El diseño y la programación basada en contratos son una forma de incrementar la calidad del código mediante
_early crash_.

Hay otras técnicas (que veremos más adelante), pero en general el principio básico es: cuando el código descubre que sucede algo que supuestamente es imposible o "no debería suceder", el programa ya no es viable: eutanasia.

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


#### Aserciones versus contratos en Java

- No hay soporte para __propagar__ aserciones por una jerarquía de herencia: si se redefine un método con contrato, las aserciones que implementan el contrato no serán llamadas correctamente (excepto si se duplican en el código)
- No hay soporte para valores **antiguos**: si se implementara un contrato mediante aserciones, habría que añadir código a la precondición para guardar la información que quiera usarse en la postcondición. (v.g. `variable@pre` en *iContract* versus `old expression` en Eiffel)
- El sistema de **runtime** y las **bibliotecas** no están diseñadas para dar soporte a contratos, así que estos **no se chequean**. Y es precisamente en la frontera entre el cliente y la biblioteca donde hay más problemas.

#### Redefinición de contratos

!!! quote "Redefinición de contratos"
    A routine redeclaration [in a derivative] may only replace the original precondition by one equal or weaker, and the original post-condition by one equal or stronger

    <cite>Bertrand Meyer</cite>

Los métodos de clase declaran *precondiciones* y *postcondiciones* al redefinir una operación en una subclase derivada.

- Las **precondiciones** sólo pueden sustituirse por otras más débiles/laxas. Los métodos pueden redefinirse con implementaciones que _aceptan_ un rango _más amplio_ de entradas.
- Las **postcondiciones** sólo pueden sustituirse por otras más fuertes/estrictas. Los métodos pueden redefinirse con implementaciones que _generan_ un rango _más estrecho_ de salidas.
- Las **invariantes** sólo pueden sustituirse por otras más fuertes/estrictas. Las clases e interfaces pueden _derivarse_ para _restringir_ el conjunto de estados válidos. Un objeto debe tener un estado _consistente_ con cualquiera de sus superclases o interfaces.

