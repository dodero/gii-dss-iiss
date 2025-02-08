# Patrones de diseño en Ruby

## Visitor

**Ejemplo: Cómputo sobre una colección**
Imprimir el resultado de un cierto cómputo (por ejemplo, multiplicar por dos) sobre todos los valores de una colección de nodos con un valor entero

### Ejemplo: Doblar nodos - versión 1**

```ruby
class IntegerNode
  def initialize(value)
    @value = value
  end

  def double
    @value * 2
  end
end

class Collection
  def initialize
    @nodes = []
    @nodes << IntegerNode.new(2)
    @nodes << IntegerNode.new(3)
  end

  def print_double
    @nodes.each do |node|
      puts node.double
    end
  end
end

collection = Collection.new
collection.print_double # => 4 6
```

Ahora intentamos triplicar los valores. Habrá que cambiar la clase `IntegerNode` para definir una función `triple`. Pero `IntegerNode` contiene el conocimiento de cómo se imprime el valor `triple`. Si más tarde tenemos orto tipo de nodo `FloatNode`, dicho nodo tendrá conocimiento sobre cómo duplicar y triplicar el valor.

Los nodos tan solo almacenan información. La representación de los datos debería estar separada de los propios datos. Entonces `IntegerNode` y `FloatNode` no deberían conocer cómo duplicar y triplicar el valor.

Para sacar el código de la representación de los datos fuera de los nodos, puede emplearse el **patrón visitor** con **double dispatch**

### Single dispatch

La invocación a un método con _single dispatch_ se basa solo en un criterio: la clase del objeto. La mayoría de lenguajes de programación usan _single dispatch_

```ruby
node.double
```

### Double dispatch

Con _double dispatch_, la ejecución de un método depende de dos cosas: la clase del objeto y la clase del objeto de entrada.

Otros lenguajes, como Java, dan soporte a la _sobrecarga_ de métodos para tener dos métodos con el mismo nombre y diferentes argumentos. En el siguiente ejemplo, el método a invocar se decide en función de la clase de `node` y la clase del valor del argumento (`Integer` o `String`).

```ruby
class Node
   def double(Integer value); value *2; end
   def double(String value); Integer.parseInt(value) * 2; end
end

node.double(2)
node.double("51")
```

En ruby no pueden haber dos métodos con el mismo nombre y diferente signatura porque el segundo sobreescribiría el primero.

Para solventar esta limitación, el nombre del métodos suele incluir el nombre de la clase.

Usamos un poco de las capacidades de introspección de Ruby:

```ruby
class Node
  def accept value
   method_name = "visit_#{value.class}"
   send method_name
  end

  def visit_Integer value
   value * 2
  end

  def visit_String value
    value.to_i * 2
  end
end
```

### Patrón visitor en Ruby

Volvemos al problema de recorrer la colección, empleando el _double dispatch_ para separar la infromación del nodo de la representación de datos usada.

Se define un método `accept` en cada `Node` que acepta un visitor y llama a `visit`.

```ruby
class Node
  def accept visitor
    raise NotImpelementedError.new
  end
end

module Visitable
  def accept visitor
    visitor.visit self
  end
end

class IntegerNode < Node
  include Visitable

  attr_reader :value
  def initialize value
    @value = value
  end
end

class Collection < Node
  def initialize
    @nodes = []
    @nodes << IntegerNode.new(2)
    @nodes << IntegerNode.new(3)
  end

  def accept visitor
    @nodes.each do |node|
      node.accept visitor
    end
  end
end

class DoublerVisitor
  def visit subject
    puts subject.value * 2
  end
end

class TriplerVisitor
  def visit subject
    puts subject.value * 3
  end
end

collection = Collection.new
puts "Doubler:"
collection.accept DoublerVisitor.new
puts "Tripler:"
collection.accept TriplerVisitor.new

# =>
Doubler:
4
6
Tripler:
6
9
```

Si añadimos un nuevo tipo de nodo `StringNode`, el método `visit` cambia. Se decide el método a invocar en función de la clase del argumento.

```ruby
class Node
  def accept visitor
    raise NotImpelementedError.new
  end
end

module Visitable
  def accept visitor
    visitor.visit(self)
  end
end

class IntegerNode < Node
  include Visitable

  attr_reader :value
  def initialize value
    @value = value
  end
end

class StringNode < Node
  include Visitable

  attr_reader :value
  def initialize value
    @value = value
  end
end

class Collection < Node
  def initialize
    @nodes = []
    @nodes << IntegerNode.new(2)
    @nodes << StringNode.new("3")
  end

  def accept visitor
    @nodes.each do |node|
      node.accept visitor
    end
  end
end

class BaseVisitor
  def visit subject
    method_name = "visit_#{subject.class}".intern
    send(method_name, subject )
  end
end

class DoublerVisitor < BaseVisitor
  def visit_IntegerNode subject
    puts subject.value * 2
  end

  def visit_StringNode subject
    puts subject.value.to_i * 2
  end
end

class TriplerVisitor < BaseVisitor
  def visit_IntegerNode subject
    puts subject.value * 3
  end

  def visit_StringNode subject
    puts subject.value.to_i * 3
  end
end

collection = Collection.new
puts "Doubler:"
collection.accept DoublerVisitor.new
puts "Tripler:"
collection.accept TriplerVisitor.new

# =>
Doubler:
4
6
Tripler:
6
9
```