# FUNCIONES

## Programación funcional

### <a id="anonimas">Funciones anónimas</a>

#### <a id="lambda">Funciónes anónimas o *lambdas*</a>

- Función o subrutina definida y (posiblemente) llamada sin necesidad de asociarla a un identificador o nombre
- Se suelen pasar como argumento a funciones de orden superior
- Son funciones anidadas que permiten acceder a variables definidas en el ámbito de la contenedora (variables no locales a la función anónima)
- Muchos lenguajes las introducen a través de la palabra reservada `lambda`

!!! note "Expresión lambda"
    Una expresión *lambda* es una función anónima (con o sin parámetros) que es llamada sin necesidad de asociarle un nombre explícito. Sirven para pasarlas como argumento a funciones de orden superior, momento en el cual los parámetros de la función anónima toman un valor en el contexto de ejecución de la función contenedora que la recibe y ejecuta. Por tanto, las funciones anónimas permiten acceder a variables (no locales) definidas en el ámbito de la contenedora.

#### Lambdas en los lenguajes

Mecanismos de los lenguajes para implementar funciones anónimas:

- En C++: funciones anónimas, objetos función (_functors_) o [funciones lambda](http://en.cppreference.com/w/cpp/language/lambda) (desde C++11)
- En Java 8: [expresiones lambda](http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/Lambda-QuickStart/index.html)
- En Ruby: [blocks, procs y lambdas](https://www.blackbytes.info/2016/02/ruby-procs-and-lambdas/)
- En C\#: [delegates](https://msdn.microsoft.com/en-us/library/ms173171.aspx) (métodos anónimos y expresiones lambda)
- En Python: [generators, comprehensions, lambda expressions](https://docs.python.org/2/howto/functional.html)

##### Java

=== "Sintaxis"

```java
  ( argumentos ) -> expresión
```

=== "Ejemplos"

```java
  (int x, int y) -> x + y
  () -> 42
  (String s) -> { System.out.println(s); }
```

##### Ruby

=== "Blocks"

```ruby 
  { | argumentos | expresión }
```

=== "Lambdas"

```ruby
  ->(argumentos) { expresión }
```

=== "Ejemplos"

```ruby
  [1, 2, 3].each { |num| puts num * 2 }
  # 2 4 6

  times_two = ->(x) { x * 2 }
  times_two.call(10)
  # 20
```

##### C++

=== "Sintaxis"

```c++
[capture](parameters) -> return_type { function_body }
```

=== "Entorno de referencia"

| Capture |  Descripción |
| --- | --- |
| `[]`      | Sin variables externas definidas. Intentar utilizar cualquier variable externa es un error |
| `[x, &y]` | `x` se captura por valor, `y` por referencia |
| `[&]`     | Todas las variables externas que sean utilizadas son capturadas implícitamente por referencia |
| `[=]`     | Todas las variable externas que sean utilizadas son capturadas implícitamente por valor |
| `[&, x]`  | `x` se captura explícitamente por valor; el resto, por referencia |
| `[=, &z]` | `z` se captura explícitamente por referencia; el resto, por valor |


#### <a id="cierres">Cierres de funciones o _closures_</a>

- __Cierre__: Función o referencia a función junto con un _entorno de referencia_
    - La diferencia entre una función normal y un cierre es que un cierre depende de una o varias **variables libres**.
    - Un cierre permite acceder a las variables libres incluso cuando se invoca desde fuera de su ámbito léxico
- __Entorno de referencia__: tabla que guarda una referencia a cada una de las variables no locales (_libres_) de la función
    - __Variable libre__ (_free_): notación lógica matemática que especifica los lugares de una expresión donde tiene lugar una sustitución
    - __Variable asignada__ (_bound_): variable que era libre previamente pero a la que le ha sido asignado un valor o conjunto de valores

!!! note "Closures en Scala"
    - [ ] Seguir el tutorial sobre [Closures](https://madusudanan.com/blog/scala-tutorials-part-30-closures/)

#### Lambdas y cierres en C++ 

```cpp
std::vector<int> some_list; // assume that contains something
int total = 0;
for (int i=0;i<5;++i) some_list.push_back(i);
std::for_each(
  begin(some_list),
  end(some_list),
  [&total](int x) { total += x; }
);
// Computes the total of all elements in the list.
/* Variable total is stored as a part of the lambda function's closure.
   Since it is a reference to the stack variable total, it can change
   its value. */
```

- Un _cierre_ en C++ se expresa mediante la parte [_capture_]
- El _entorno de referencia_ se expresa por el conjunto de variables externas indicadas dentro del cierre
- Las variables del entorno de referencia en C++ pueden ser capturadas por valor (`[=]`) o por referencia (`[&]`)
- Las variables externas capturadas son inmutables por defecto; si no, usar `mutable` después de los (_parameters_)


!!! note "Lectura: Lambdas en C++"
    Lecturas recomendadas 

    - [ ] [Lambda Functions in C++11 - the Definitive Guide](https://www.cprogramming.com/c++11/c++11-lambda-closures.html)
    - [ ] La historia de las lambdas en C++: [Parte 1](https://www.bfilipek.com/2019/02/lambdas-story-part1.html) y [Parte 2](https://www.bfilipek.com/2019/03/lambdas-story-part2.html)


#### Funciones anónimas en Java

=== "Clases anónimas (Java 7)"

```java hl_lines="8 19"
public class ComparatorTest { 
  public static void main(String[] args) {

    List<Person> personList = Person.createShortList();

    Collections.sort(personList, new Comparator<Person>(){
      public int compare(Person p1, Person p2){
        return p1.getLastname().compareTo(p2.getLastname());
      }
    });

    System.out.println("=== Sorted Asc Lastname ===");
    for(Person p:personList){
      p.printName();
    }

    Collections.sort(personList, new Comparator<Person>(){
      public int compare(Person p1, Person p2){
        return p2.getLastname().compareTo(p1.getLastname());
      }
    });

    System.out.println("=== Sorted Desc Lastname ===");
    for(Person p:personList){
      p.printName();
    }


  }
}
```

=== "Lambdas (Java 8)"

```java 
public class ComparatorTest { 
  public static void main(String[] args) {

    List<Person> personList = Person.createShortList();

    // Print Asc
    System.out.println("=== Sorted Asc Lastname ===");
    Collections.sort(personList, (Person p1, Person p2) ->
      p1.getLastname().compareTo(p2.getLastname()));

    for(Person p:personList){
      p.printName();
    }

    // Print Desc
    System.out.println("=== Sorted Desc Lastname ===");
    Collections.sort(personList, (p1,  p2) ->
      p2.getLastname().compareTo(p1.getLastname()));

    for(Person p:personList){
      p.printName();
    }

  }
}
```

!!! note "Ejercicio"
    [Mejorando código con expresiones lambda](http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/Lambda-QuickStart/index.html\#section3)

##### Captura de variables en lambdas

Una expresión lambda en Java puede **capturar** (o no) variables de instancia no locales (atributos de la clase contenedora) y variables locales (declaradas o no `final`, pero cuyo valor no es modificado) del ámbito contenedor.

- Ejemplo: Lambda que captura variables locales `final`:
```java
final BigDecimal bd = new BigDecimal(1);
final BigDecimal x = new BigDecimal(2);
Function<BigDecimal, BigDecimal> func = (a) -> bd.multiply(a).add(x);

for (int j = 0; j < 999999999; j++) {
      func.apply(new BigDecimal(j));
}
```

-  Ejemplo: Lambda que captura variables locales no declaradas `final` pero cuyo valor no es modificado:
```java 
BigDecimal bd = new BigDecimal(1);
BigDecimal x = new BigDecimal(2);
 // Se puede consultar x pero no se podría cambiar el valor de x:
Function<BigDecimal, BigDecimal> func = (a) -> { bd.multiply(a).add(x); /* x = new BigDecimal(0); */ };

for (int j = 0; j < 999999999; j++) {
      func.apply(new BigDecimal(j));
}
```

- Ejemplo: Lambda que captura variables de instancia de la clase contenedora:
```java
public class LambdaInstanceCapturing implements Runnable {
    private BigDecimal bd = new BigDecimal(1);

    @Override
    public void run() {
        Function<BigDecimal, BigDecimal> func = (a) -> bd.multiply(a);

        for (int j = 0; j < 999999999; j++) {
            func.apply(new BigDecimal(j));
        }
    }
}
```

##### Lambdas y clases anónimas internas

En Java, una expresión lambda y una clase anónima interna (_inner class_) tienen un propósito similar, pero son diferentes en un aspecto: el **ámbito** (_scope_) de la definición de las variables locales.

Cuando se usa una inner class, se crea un nuevo ámbito para dicha clase. Se pueden ocultar las variables locales para el ámbito contenedor instanciando nuevas variables con el mismo nombre. También se puede usar la palabra reservada `this` dentro de una clase anónima para hacer referencia a su instancia.

Sin embargo, las expresiones lambda trabajan con el ámbito contenedor. No se pueden ocultar las variables del ámbito contenedor dentro del cuerpo de la expresión lambda. En tal caso, la palabra reservada `this` hace referencia a una instancia de la clase contenedora.

En el ejemplo siguiente, ¿qué valor devuelve `scopeExperiment()`?:

```java
@FunctionalInterface
public interface ClaseFuncional{
  String method(String string);
}

private String variable = "Valor de la contenedora";

//...

public String scopeExperiment() {

  ClaseFuncional unaInnerClass = new ClaseFuncional() {
      String variable = "Valor de la inner class";
      @Override
      public String method(String string) {
          return this.variable;
            /*  Con o sin this, no hay lambdas ni variables libres */
      }
  };
  String resultadoInnerClass = unaInnerClass.method("");

  ClaseFuncional unaLambda = parametro -> {
      String variable= "Valor de la lambda";
      return this.variable; 
        /* Con this, el cierre de la variable libre se produce
           con el valor de ClaseFuncional::variable */
  };
  String resultadoLambda = unaLambda.method("");

  return "resultadoInnerClass = " + resultadoInnerClass +
    ",\nresultadoLambda = " + resultadoLambda;
}
```

El valor será:

```text
resultadoInnerClass  = Valor de la inner class,
resultadoLambda = Valor de la contenedora
```


#### Funciones anónimas en Ruby

##### <a id="blocks">Bloques (_blocks_)</a>

=== "Sintaxis `do` ... `end`"

  ```ruby
  some_list = [ 10, 20, 30 ]
  some_list.map do |i|
      i += 1
  end
  ```

=== "Sintaxis `{` ... `}`"

  ```ruby
  some_list = [ 10, 20, 30 ]
  some_list.map { |i| i += 1 }
  ```

El método `map` itera y aplica un bloque repetitivamente a cada elemento de una colección (representado por el parámetro `i`)

###### Ejemplo: búsqueda en una lista

=== "Sin bloques:"


  ```ruby
  class SongList
    def with_title(title)
      for i in 0...@songs.length
        return @songs[i] if title == @songs[i].name
      end
      return nil
    end
  end
  ```

=== "Con bloques (sintaxis `do` ... `end`):"

  ```ruby
  class SongList
    def with_title(title)
      @songs.find do |song|
        title == song.name
      end
    end
  end
  ```

=== "Con bloques (sintaxis `{` ... `}`):"

  ```ruby
  class SongList
    def with_title(title)
      @songs.find { |song| title == song.name }
    end
  end
  ```

El método `find` itera y aplica el test del bloque a cada elemento `song` de la colección.

###### Ejecución de bloques

- El bloque debe aparecer al lado de una llamada a método
- No se ejecuta el bloque, sino que se recuerda el contexto (variables locales, objeto actual, etc.) en que aparece
- Cuando se ejecuta el método, el bloque es invocado donde aparezca `yield`
- El control vuelve al método después del `yield`
- Al bloque se le pueden pasar parámetros

**Ejemplo: fibonacci**

```ruby
def fib_up_to(max)
  i1, i2 = 1, 1
  while i1 <= max
    yield i1
    i1, i2 = i2, i1+i2
  end
end
fib_up_to(1000) {|f| print f, " " }

#Salida => 1 1 2 3 5 8 13 21 34 55 89 144 233 377 610 987
```

**Ejemplo de `yield`:**

```ruby
def three_times
  yield
  yield
  yield
end
three_times { puts "Hello" }
```

**Ejemplo: implementación de `Array.find`**

```ruby
class Array
  def find
    for i in 0...size
      value = self[i]
      return value if yield(value)
    end
    return nil
  end
end
```

**Ejemplos: iterar con bloques**

- Iterar sobre un array con `each`

  `Array#each`: recibe un array y aplica el bloque a cada item, sin modificar el array ni crear un nuevo objeto; devuelve el mismo array.

  ```ruby
  [ 1, 3, 5, 7, 9 ].each {|i| printf i, " " }
  #Salida => 1 3 5 7 9
  Array a = [ 1, 2, 3, 4 ]
  a.each {|i| puts i*2 }
  #Salida => 2 4 6 8
  #Devuelve => [1, 2, 3, 4]
  a
  #Devuelve => [1, 2, 3, 4]
  ```

- Iterar sobre un fichero con `each`

  `File#each`: recibe el contenido de un fichero de texto y aplica el bloque a cada línea.

  ```ruby
  f = File.open("testfile")
  f.each do |line|
    puts line
  end
  f.close
  f = File.open("testfile")
  f.each {|line| puts line}
  f.close
  ```

- Iterar sobre un array con `collect`

  `Array#collect`: aplica el bloque a todos los items y devuelve el nuevo array modificado; hace lo mismo que `Array#map`

  ```ruby
  ["H", "A", "L"].collect {|x| x.succ }
  # Salida => [''I'', ''B'', ''M'']
  Array a = [ 1, 2, 3, 4 ]
  a.collect {|i| puts i*2}
  #Salida => 2 4 6 8
  #Devuelve => [nil, nil, nil, nil]
  a.collect {|i| i.succ}
  #Devuelve => [2, 3, 4, 5]
  a
  #Devuelve => [1, 2, 3, 4]
  ```

##### <a id="proc">Procs y lambdas</a>

- En Ruby, una función anónima o _lambda_ es simplemente un tipo especial de objeto `Proc`
- Definición de procs/lambdas:

  ```ruby
  # sin argumentos:
  say_something = -> { puts "This is a lambda" }
  say_something = lambda { puts "This is a lambda" }
  say_otherwise = Proc.new { puts "This is a proc" }
  # con argumentos:
  times_two = ->(x) { x * 2 }
  ```

- Varias formas de llamar a la lambda (es preferible `call`)

  ```ruby
  say_something = -> { puts "This is a lambda" }
  say_something.call
  say_something.()
  say_something[]

  say_otherwise = Proc.new { puts "This is a proc" }
  say_otherwise.call

  times_two = ->(x) { x * 2 }
  times_two.call(10)
  ```

- Los `proc` no se preocupan de los argumentos:

  ```ruby
  t = Proc.new { |x,y| puts "I don't care about args!" }
  t.call #Salida: I don't care about args!
  t.call(10) #Salida: I don't care about args!
  t.call(10,10) #Salida: I don't care about args!
  t.call(10,10) #Salida: I don't care about args!

  s = ->(x,y) { puts "I care about args" }
  s.call # ArgumentError: wrong number of arguments (given 0, expected 2)
  s.call(10) # ArgumentError: wrong number of arguments (given 1, expected 2)
  s.call(10,10) # Salida: I care about args
  ```

- Los `proc` retornan del método actual; los lambda retornan de la función anónima:

  ```ruby
  # funciona:
  my_lambda = -> { return 1 }
  puts "Lambda result: #{my_lambda.call}"

  # eleva una exceción:
  my_proc = Proc.new { return 1 }
  puts "Proc result: #{my_proc.call}"
  ```

- Si el `proc` está dentro de un método, la llamada a `return` es equivalente a retornar de ese método:

  ```ruby
  def call_proc
    puts "Before proc"
    my_proc = Proc.new { return 2 }
    my_proc.call
    puts "After proc"
  end

  puts call_proc
  # Prints "Before proc" but not "After proc"

  def call_lambda
    puts "Before lambda"
    my_lambda = lambda { return 2 }
    my_lambda.call
    puts "After lambda"
  end

  puts call_lambda
  # Prints "Before lambda" and "After lambda"
  ```

**Diferencias entre `Proc` y `lambda`:**

- Las lambdas se definen con `-> {}` y los procs con `Proc.new {}`
- Los `Proc` retornan del método actual, las lambdas retornan de la propia función lambda
- Los `Proc` no se preocupan del número correcto de argumentos, las lambdas elevan una excepción

##### Paso de bloques como parámetros

- Simplemente, se añade al final de la llamada a un método 
- ¿Dónde se llama al bloque? Donde el método indique con `yield`
- El bloque (realmente un objeto `Proc`) se pasa como una especie de parámetro no declarado

**Ejemplos de paso de bloques:**

- Llamada a un bloque sin parámetros

  ```ruby
  def run_it
    puts("Before the yield")
    yield
    puts("After the yield")
  end
  ```

  ```ruby
  run_it do
    puts('Hello')
    puts('Coming to you from inside the block')
  end

  # Salida =>
  #  Before the yield
  #  Hello
  #  Coming to you from inside the block
  #  After the yield
  ```

- Cualquier método puede recibir un bloque como parámetro implícito, pero no lo ejecuta si no hace `yield`:

  ```ruby
  def run_it
  end

  run_it do
    puts('Hello')
  end

  # => No genera salida
  ```

- Con `yield`:

  ```ruby
  def run_it
    yield if block_given?
  end

  run_it do
    puts('Hello')
  end
  # Salida =>
  #   Hello
  ```

- Llamada a un bloque con parámetros:

  ```ruby
  def run_it_with_parameter
    puts('Before the yield')
    yield(24)
    puts('After the yield')
  end

  run_it_with_parameter do |x|
    puts('Hello from inside the proc')
    puts("The value of x is #{x}")
  end

  # Salida =>
  #  Before the yield
  #  Hello from inside the proc
  #  The value of x is 24
  #  After the yield
  ```

- Hacer explícito el bloque pasado como parámetro usando _ampersand_: explicitamos que se espera que el método reciba un parámetro de tipo bloque

  ```ruby
  def run_it_with_parameter(&block)
    puts('Before the call')
    block.call(24)
    puts('After the call')
  end
  ```

- Convertir un `Proc` o un lambda en un bloque pasado como parámetro:

  ```ruby
  my_proc = Proc.new {|x| puts("The value of x is #{x}")}
  run_it_with_parameter(&my_proc)
  my_lambda = lambda {|x| puts("The value of x is #{x}")}
  run_it_with_parameter(&my_lambda)

  # Salida (en ambos casos) =>
  #  Before the call
  #  The value of x is 24
  #  After the call
  ```

!!! note "Lecturas recomendadas"
    - M. Williams: [Java SE 8: Lambda Quick Start](http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/Lambda-QuickStart/index.html), Oracle Learning Library, 2013.
    - D. Thomas & A. Hunt: [Programming Ruby. The Pragmatic Programmer's Guide](http://www.ruby-doc.org/docs/ProgrammingRuby/), Addison-Wesley, 2005.

<a id="informes"></a>

##### Ejemplo de interfaces funcionales: Formateo de informes

**Versión en ruby**

```ruby
class Report
  attr_reader :title, :text
  attr_accessor :formatter
  def initialize(formatter)
    @title = 'Informe mensual'
    @text = ['Todo marcha', 'muy bien.']
    @formatter = formatter
  end
  def output_report()
    @formatter.output_report(self)
  end
end
class HTMLFormatter
  def output_report(context)
    puts('<html>')
    puts(' <head>')
    # Output The rest of the report ...
    puts(" <title>#{context.title}</title>")
    puts(' </head>')
    puts(' <body>')
    context.text.each do |line|
      puts(" <p>#{line}</p>")
    end
    puts(' </body>')
    puts('</html>')
  end
end
  
class PlainTextFormatter
  def output_report(context)
    puts("***** #{context.title} *****")
    context.text.each do |line|
```

**Versión con interfaces funcionales (Ruby procs + blocks)**

```ruby
class Report
  attr_reader :title, :text
  attr_accessor :formatter
  
  def initialize(&formatter)
    @title = 'Monthly Report'
    @text = [ 'Things are going', 'really, really well.' ]
    @formatter = formatter
  end
  
  def output_report
    @formatter.call( self )
  end
end
```

**Formateo HTML**

```ruby
HTML_FORMATTER = lambda do |context|
  puts('<html>')
  puts(' <head>')
  puts("   <title>#{context.title}</title>")
  puts(' </head>')
  puts(' <body>')
  context.text.each do |line|
    puts("  <p>#{line}</p>" )
  end
  puts(' </body>')
  puts

report = Report.new &HTML_FORMATTER
report.output_report
```

**Formateo de texto**

```ruby
report = Report.new do |context|
  puts("***** #{context.title} *****")
  context.text.each do |line|
    puts(line)
  end
end
```
