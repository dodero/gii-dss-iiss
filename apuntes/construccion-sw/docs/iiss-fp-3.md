# FUNCIONES

## Programación funcional

### <span style="color:blue;"><a id="mixins">Mixins</a></span>

En POO, un __mixin__ es una clase con métodos disponibles para otras clases sin tener que ser madre de estas otras (es decir, sin usar la herencia)

- Es una alternativa a la herencia múltiple
- Incluye una __interfaz__ con métodos ya implementados
- No se _heredan_ sino que se __incluyen__
- Un mixin es una (sub)clase, luego define un comportamiento y un __estado__
- Es una forma de implementar el principio de inversión de dependencias (DIP)

#### <a id="modules">Ruby modules</a>

En Ruby los mixins se implementan mediante módulos (`module`).

- Un módulo no puede tener instancias (porque no es una clase)
- Un módulo puede incluirse (`include`) dentro de la definición de una clase

##### Ejemplo: herencia vs mixins

```ruby
module Debug
  def whoAmI?
    "#{self.class.name} (\##{self.object_id}): #{self.to_s}"
  end
end
class MusicWork
  def initialize(title)
    @title=title
  end
  def to_s
    @title
  end
end
class Phonograph < MusicWork
  include Debug
end
class EightTrack < MusicWork
  include Debug
end
ph = Phonograph.new("West End Blues")
et = EightTrack.new("Surrealistic Pillow")
ph.whoAmI?  # => "Phonograph (#70315984363660): West End Blues"
et.whoAmI?  # => "EightTrack (#70315996611260): Surrealistic Pillow"
```

##### Ejemplo: Comparable en scala

En Scala se puede implementar el equivalente a la interfaz `Comparable` de Java mediante traits:

```scala
trait Ord {
  def < (that: Any): Boolean 
  def <=(that: Any): Boolean =  (this < that) || (this == that)
  def > (that: Any): Boolean = !(this <= that)
  def >=(that: Any): Boolean = !(this < that)
}

class Fecha(d: Int, m: Int, a: Int) extends Ord {
  def anno = a
  def mes = m
  def dia = d
  override def toString(): String = s"$dia-$mes-$anno"
  override def equals(that: Any): Boolean =
    that.isInstanceOf[Fecha] && {
      val o = that.asInstanceOf[Fecha]
      o.dia== dia && o.mes == mes && o.anno== anno
    }
  def <(that: Any): Boolean = {
    if (!that.isInstanceOf[Fecha])
      sys.error("no se puede comparar" + that + " y una fecha")
    val o = that.asInstanceOf[Fecha]
    (anno < o.anno) ||
    (anno== o.anno && (mes < o.mes ||
                       (mes == o.mes && dia < o.dia)))
  }  
}

object MiApp {
  def main(args: Array[String]) : Unit = {
    val f1 = new Fecha(12,4,2009)
    val f2 = new Fecha(12,4,2019)
    println(s"$f1 es posterior a $f2? ${f1>=f2}")
  }
}
```

##### Ejemplo: Comparable en ruby

Una manera de implementar un `Comparable` en ruby mediante el módulo [Comparable](https://ruby-doc.org/core-2.2.3/Comparable.html):

```ruby
class Student
  include Comparable # The class Student 'inherits' Comparable module using include keyword
  attr_accessor :name, :score

  def initialize(name, score)
    @name = name
    @score = score
  end

  # Including the Comparison module, requires the implementing class to define the <=> comparison operator
  # Here's the comparison operator. We compare 2 student instances based on their scores.

  def <=>(other)
    @score <=> other.score
  end

  # Here's the good bit - I get access to <, <=, >,>= and other methods of the Comparable Interface for free.
end

s1 = Student.new("Peter", 100)
s2 = Student.new("Jason", 90)
s3 = Student.new("Maria", 95)

s1 > s2 #true
s1 <= s2 #false
s3.between?(s1,s2) #true
```

- La clase que incluye el módulo `Comparable` tiene que implementar:
 
    - el método `<=>`: es un método que incluye los siguientes operadores/métodos: `<, <=, ==, >, >=, between?` 
    - el atributo-criterio de comparación

- En `x <=> y`, `x` es el receptor del mensaje/método e `y` es el argumento

##### Ejemplo: Adaptador de interfaz en Ruby

**Interfaz americana**

```ruby
class Renderer
  def render(text_object)
    text = text_object.text
    size = text_object.size_inches
    color = text_object.color

    # render the text ...
  end
end
```

```ruby
class TextObject
  attr_reader :text, :size_inches, :color

  def initialize(text, size_inches, color)
    @text = text
    @size_inches = size_inches
    @color = color
  end
end
```

**Interfaz británica**

```ruby
## british_text_object.rb
class BritishTextObject
  attr_reader :string, :size_mm, :colour

  # ...
end
```

**Adaptador de interfaz: versión clásica**

```ruby
class BritishTextObjectAdapter < TextObject
  def initialize(bto)
    @bto = bto
  end

  def text
    return @bto.string
  end

  def size_inches
    return @bto.size_mm / 25.4
  end

  def color
    return @bto.colour
  end
end
```

##### Adaptador de interfaz: versión con módulos

```ruby
## Make sure the original class is loaded
require 'british_text_object'

## Now add some methods to the original class
class BritishTextObject
  def color
    return colour
  end

  def text
    return string
  end

  def size_inches
    return size_mm / 25.4
  end
end
```

- `require` carga la clase original
- la reescritura de métodos modifica la clase, no la declara de nuevo
- se puede hacer incluso con las clases built-in de la biblioteca de Ruby

##### Adaptador de interfaz: instancia única

```ruby
bto = BritishTextObject.new('hello', 50.8, :blue)

class << bto
  def color
    colour
  end

  def text
    string
  end

  def size_inches
    return size_mm/25.4
  end
end
```

o bien:

```ruby
def bto.color
  colour
end

def bto.text
  string
end
## ...
```

- Modifica el comportamiento solo de 1 instancia
- Ruby llama a esto _singleton methods_ y _singleton class_ (no es exactamente lo mismo que el patrón singleton del GoF)
- Ruby primero busca los métodos definidos en una clase singleton y luego en la clase regular que ha sido redefinida

!!! note "Tutoriales y ejercicios recomendados: Ruby"
    - [Ruby from other languages](https://www.ruby-lang.org/en/documentation/ruby-from-other-languages/)
    - Tutorial: [Ruby modules](http://ruby-doc.com/docs/ProgrammingRuby/html/tut_modules.html)

### <a id="traits">Scala Traits</a>

Un __trait__ es una forma de separar las dos principales responsabilidades de una clase: definir el __estado__ de sus instancias y definir su __comportamiento__.

- Las clases y los objetos en Scala pueden extender un `#!scala trait`
- Los `#!scala trait`de Scala son similares a las `#!java interface` de Java.

!!! note "Java default methods"
    - Desde Java 8, las interfaces pueden incorporar [métodos por defecto](https://www.baeldung.com/java-static-default-methods) que hacen que las interfaces de Java se comporten más como un trait.
    - Sirven para implementar herencia múltiple

- Los `#!scala trait` no pueden instanciarse
- Los métodos definidos en una clase tienen precedencia sobre los de un `#!scala trait`
- Los `#!scala trait` no tienen estado propio, sino el del objeto o la instancia de la clase a la que se aplica

#### Ejemplo: Iterador como un `#!scala trait`

```scala
trait Iterator[A] {
  def hasNext: Boolean
  def next(): A
}

class IntIterator(to: Int) extends Iterator[Int] {
  private var current = 0
  override def hasNext: Boolean = current < to
  override def next(): Int =  {
    if (hasNext) {
      val t = current
      current += 1
      t
    } else 0
  }
}

val iterator = new IntIterator(10)
println(iterator.next())  // prints 0
println(iterator.next())  // prints 1
```

#### Ejemplo: Subtipos

Implementación del _polimorfismo de inclusión_ o herencia simple con traits:

```scala
import scala.collection.mutable.ArrayBuffer

trait Pet {
  val name: String
}

class Cat(val name: String) extends Pet
class Dog(val name: String) extends Pet

val dog = new Dog("Harry")
val cat = new Cat("Sally")

val animals = ArrayBuffer.empty[Pet]
animals.append(dog)
animals.append(cat)
animals.foreach(pet => println(pet.name))  // Prints Harry Sally
```

#### Ejemplo: Similarity

```scala
trait Similarity {
  def isSimilar(x: Any): Boolean
  def isNotSimilar(x: Any): Boolean = !isSimilar(x)
}
```

```scala
class Point(xc: Int, yc: Int) extends Similarity {
  var x: Int = xc
  var y: Int = yc
  def isSimilar(obj: Any) =
    obj.isInstanceOf[Point] &&
    obj.asInstanceOf[Point].x == x
}
object TraitsTest extends App {
  val p1 = new Point(2, 3)
  val p2 = new Point(2, 4)
  val p3 = new Point(3, 3)
  println(p1.isNotSimilar(p2))  //false
  println(p1.isNotSimilar(p3))  //true
  println(p1.isNotSimilar(2))   //true
}
```

- El polimorfismo (de inclusión) usa la herencia (simple)
- Los mixin son un mecanismo de **reutilización de código** sin herencia

¿Usar traits con comportamiento va contra el principio general de que la [herencia de comportamiento](https://en.wikipedia.org/wiki/Composition_over_inheritance#Benefits) es una mala idea?

- Odersky llama _mixin traits_ a los traits con comportamiento
- Para ser un mixin genuino, debería mezclar comportamiento y no interfaces heredadas

!!! note "Lectura recomendada: Scala mixins"
    Leer [Scala Mixins: The right way](http://baddotrobot.com/blog/2014/09/22/scala-mixins/)

#### Ejemplo: Iterator

Cómo reutilizar comportamiento de varios tipos de iteradores a través de un mixin:

```scala
abstract class AbsIterator {
  type T
  def hasNext: Boolean
  def next: T
}

trait RichIterator extends AbsIterator {
  def foreach(f: T => Unit) { while (hasNext) f(next) }
}

class IntIterator(to: Int) extends AbsIterator {
  type T = Int
  private var n = 0
  def hasNext = n < to
  def next = { val t = n; n += 1; t }
}

class StringIterator(s: String) extends AbsIterator {
  type T = Char
  private var i = 0
  def hasNext = i < s.length()
  def next = { val ch = s charAt i; i += 1; ch }
}

object StringIteratorTest {
  class Iter extends StringIterator("HOLA") with RichIterator
  val iter = new Iter
  iter foreach println
}

object IntIteratorTest {
  class Iter extends IntIterator(10) with RichIterator
  val iter = new Iter
  iter foreach println
}
```

- La implementación anterior usa polimorfismo paramétrico (`#!scala type T`).
- [Unit](http://www.scala-lang.org/api/current/scala/Unit.html) en scala: subtipo de `#!scala AnyVal`; solo hay un valor `()` que es de tipo `#!scala Unit`.
- Un método que devuelve `#!scala Unit` es análogo a un método Java que devuelve `#!java void`

#### Diferencia con clase abstracta

- [Scala traits vs abstract classes](http://stackoverflow.com/questions/1991042/what-is-the-advantage-of-using-abstract-classes-instead-of-traits)
- Los constructores de un `#!scala trait` no pueden tener parámetros (de momento)

##### Reglas

[To trait or not to trait](http://www.artima.com/pins1ed/traits.html#12.7)

- Si no se va a reutilizar el comportamiento --> clase concreta
- Si se va a reutilizar en varias clases no relacionadas entre sí --> trait (mixin)
- Si hace falta heredarlo en código Java --> clase abstracta
- Si se va a distribuir compilado y se va a heredar --> clase abstracta
- Si importa mucho la eficiencia --> clase (los traits se compilan a interfaces y son algo más lentas de llamar)
- Si no se sabe --> empezar por un trait y cambiarlo cuando se sepa

!!! note "Tutoriales y ejercicios recomendados: Scala"
    - [Scala traits](https://docs.scala-lang.org/tour/traits.html)
        - Ejemplo: [Traits exercise](https://www.scala-exercises.org/std_lib/traits)
        - Ejemplo: [Stackable Traits pattern](http://www.artima.com/scalazine/articles/stackable_trait_pattern.html)
    - [Herencia vs. composición con Scala mixins](http://baddotrobot.com/blog/2014/09/22/scala-mixins/)
    - [Scala tour](http://docs.scala-lang.org/tutorials/tour/tour-of-scala)
    - [Scala exercises](https://www.scala-exercises.org/std_lib/)
