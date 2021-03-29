---
marp: true
title: Apuntes DSS 2021 - Patrones de Diseño
description: Apuntes de Diseño de Sistemas Software, curso 2020/21 - Patrones de diseño
---

<!-- size: 16:9 -->
<!-- theme: default -->

<style>
h1 {
  text-align: center;
}
h2 {
  color: darkblue;
  text-align: center;
}
</style>

<style>
img[alt~="center"] {
  display: block;
  margin: 0 auto;
}
</style>

# DISEÑO DE SISTEMAS SOFTWARE

 
<style scoped>
h2 {
  text-align: left;
}
</style>


## Bloques

1. Principios de diseño OO
2. Patrones de diseño
3. Arquitectura de software

---

# PATRONES DE DISEÑO 

1. Introducción
2. Patrones del GoF
3. Otros patrones específicos

---
## Introducción

---
### Origen de los patrones de diseño

- Los patrones de diseño surgen a partir del libro *A Pattern Language: Towns, Buildings, Construction* de Cristopher Alexander.

- La inspiración del libro fueron las ciudades medievales, atractivas y armoniosas, que fueron construidas según regulaciones locales que requerían ciertas características, pero que permitían al arquitecto adaptarlas a situaciones particulares.

- En el libro se suministran reglas e imágenes y  se describen métodos exactos para construir diseños prácticos, seguros y atractivos a cualquier escala. También recopila modelos anteriores (con ventajas/desventajas) con el fin de usarlos en un futuro.

- El libro recomienda que las decisiones sobre la construcción de edificios se tomen de acuerdo al ambiente preciso de cada proyecto. 

 

---
### Diseño de software con patrones

- Conocer un lenguaje OO no te hace un buen diseñador. ¿Qué diferencia hay entre los diseñadores expertos y los novatos? Que los primeros usan recetas exitosas para los problemas habituales y no reinventan la rueda continuamente.

- Un grupo de expertos (_Gang of Four_) se basó en el trabajo de Alexander y lo aplicó al diseño de software, presentando el libro *Design Patterns* con un total de 23 patrones.

![w:150 center](./figuras/patronesGOF.jpg)


---
### Patrones de diseño

- Patrón de diseño: Una **solución general** a un **problema general** que puede adaptarse a un problema concreto

- La aplicación de patrones depende del **contexto**. 

- Ofrece un **vocabulario** de patrones (una jerga entre ingenieros de software)

- Los patrones **clásicos** son ampliamente conocidos: algunos muy aceptados y otros más discutidos...

- Deben usarse con cuidado. Deben simplificar el modelo, **no complicarlo**, por lo que deben surgir de manera natural.

- Han surgido nuevos patrones **específicos** de dominio: patrones de interfaces de usuario, patrones para la integración de aplicaciones empresariales, patrones de flujos de trabajo BPMN, patrones de concurrencia, etc.

---
## Patrones del Gang of Four

![w:200 center](./figuras/patronesGOF.jpg)

---

### Patrones creacionales

Corresponden a patrones de diseño de software que solucionan problemas de creación de instancias. Nos ayudan a encapsular y abstraer dicha creación. Vamos a ver:

- Factory Method
- Abstract Factory

Pero hay más...
- *Prototype*
- *Builder*
- *Singleton*

---

### Patrones estructurales

Son los patrones de diseño software que solucionan problemas de composición/agregación de clases y objetos. Vamos a ver:

- Composite
- Decorator
- Adapter

Pero hay más...
- *Facade*
- *Bridge*
- *Flyweight*
- *Proxy*


---
### Patrones de comportamiento
Son los relativos a la interacción y responsabilidades entre clases y objetos. Vamos a ver:

- Command
- Observer
- Strategy
- Visitor

Pero hay más...
- *Template method*, *Chain of Responsibility*, *Interpreter*
- *Iterator*, *Mediator*, *Memento*, *State*


---
<style scoped>
h3 {
  text-align: center;
  color: blue;
}
</style>

### [Factory Method](https://refactoring.guru/es/design-patterns/factory-method)

![Factory Method, center](./figuras/guru/factory-method-mini-2x.png)

---

#### Ejemplo: Juego de laberinto

@startuml
top to bottom direction
scale 1024 width
scale 650 height
skinparam linetype ortho
skinparam classAttributeIconSize 0

enum Direccion {
  NORTE
  SUR
  ESTE
  OESTE
}

class JuegoLaberinto {
  {method} main()
  {method} crearLaberinto()
}

abstract class Sitio{
  {method} entrar()
}

class Sala{
  {field} numSala
  {method} getLado(Direccion): Sitio
  {method} setLado(Direccion, Sitio)
  {method} entrar()
}

class Puerta{
  {field} estaAbierta
  {method} otroLadoDesde(Sala)
  {method} entrar()
}

class Pared{
  {method} entrar()
}

class Laberinto{
  {method} agregarSala(Sala)
  {method} getSalaNum(int)
}

JuegoLaberinto .up.> Laberinto

Sala "lados" *-down-> Sitio
Sitio <|-down- Pared
Sitio <|-down- Puerta
Sitio <|-down- Sala
Laberinto "salas" *-right-> Sala

hide members
show methods
show Direccion members
show Sala members
show Puerta members

@enduml

---

```java
public interface Direccion {
    int NORTE = 0;
    int ESTE = 1;
    int SUR = 2;
    int OESTE = 3;
}

public class Laberinto {
    Laberinto() {};
    void agregarSala(Sala sala) {};
    Sala getSalaNum(int numSala) { ... };
}
```
---

```java
public abstract class Sitio {
    void entrar() {};
}

public class Sala extends Sitio {
    private Sitio lados[];
    int numSala;

    Sala() {};
    Sala(int numSala) {};
    Sitio getLado(int dir) { return lados[dir]; };
    void setLado(int dir, Sitio sitio) {};
    void entrar() {};
}
```

---

```java
public class Pared extends Sitio {
    Pared() {};
    void entrar() {};
}

public class Puerta extends Sitio {
    private Sala sala1;
    private Sala sala2;
    boolean estaAbierta;

    Puerta(Sala sala1, Sala sala2) { ... };
    void entrar() {};
    Sala otroLadoDesde(Sala unaSala) { ... };
}
```

---

```java
Laberinto crearLaberinto () {
  Laberinto miLab = new Laberinto();
  Sala hab1 = new Sala(1);
  Sala hab2 = new Sala(2);
  Puerta unaPuerta = new Puerta(hab1, hab2);
  miLab.agregarSala(hab1);
  miLab.agregarSala(hab2);
  hab1.setLado(Direccion.NORTE, new Pared());
  hab1.setLado(Direccion.ESTE, unaPuerta);
  hab1.setLado(Direccion.SUR, new Pared());
  hab1.setLado(Direccion.OESTE, new Pared());
  hab2.setLado(Direccion.NORTE, new Pared());
  hab2.setLado(Direccion.ESTE, new Pared());
  hab2.setLado(Direccion.SUR, new Pared());
  hab2.setLado(Direccion.OESTE, unaPuerta);
  return miLab;
}
```

---

#### Críticas

- Creación poco flexible: instancias concretas cableadas
- Supongamos $\exists$ SalaHechizada, PuertaHechizada. ¿Cómo cambiamos `crearLaberinto`?

---

#### Método de factoría

- El patrón _factory method_ define una interfaz para la creación de un objeto, pero dejando en manos de las subclases la decisión de qué clase concreta instanciar.

- Permite que una clase delegue en sus subclases las instanciaciones.

---

#### Estructura

![w:850 center](./figuras/guru/factory-method-structure-2x.png)

---

1. El **Producto** declara la interfaz, que es común a todos los objetos que puede producir la clase creadora y sus subclases.

2. Los **Productos Concretos** son distintas implementaciones de la interfaz de producto.

3. La clase **Creadora** declara el método fábrica que devuelve nuevos objetos de producto. Es importante que el tipo de retorno de este método coincida con la interfaz de producto.

4. Los **Creadores Concretos** sobrescriben el Factory Method base, de modo que devuelva un tipo diferente de producto.

---

<!--

@startuml
top to bottom direction
scale 700 width
scale 600 height

class Product
class Creator
class ConcreteProduct
class ConcreteCreator

Creator : factoryMethod()
Creator : anOperation()
ConcreteCreator : factoryMethod()

Creator <|–down- ConcreteCreator
Product <|–down- ConcreteProduct
ConcreteProduct <- ConcreteCreator

hide members
show methods

note top of Creator
The Creator is a class that contains
the implementation for all of the
methods to manipulate products,
except for the factory method.
end note

note right of Creator
The abstract factoryMethod()
is what all Creator subclasses
must implement.
end note

note right of ConcreteCreator
The ConcreteCreator
implements the
factoryMethod(), which is
the method that actually
produces products.
end note

note “The ConcreteCreator is responsible for\ncreating one or more concrete products. It\nis the only class that has the knowledge of\nhow to create these products.” as n1
ConcreteProduct .. n1
ConcreteCreator .. n1

note “All products must implement\nthe same interface so that the\nclasses which use the products\ncan refer to the interface,\nnot the concrete class.” as n2
n2 . ConcreteProduct
n2 . Product

@enduml

---

-->

#### Ventajas

- Se evita un acoplamiento fuerte entre el creador y los productos concretos.
- SRP: Se puede mover el código de creación de producto a un lugar del programa, haciendo que el código sea más fácil de mantener.
- OCP: Se pueden incorporar nuevos tipos de productos en el programa sin descomponer el código cliente existente.

---

#### Implementación de `JuegoLaberinto`

```java
public class JuegoLaberinto {
  JuegoLaberinto() {};
  // factory methods:
  Laberinto makeLaberinto() { return new Laberinto(); }
  Sala makeSala(int numSala) { return new Sala(numSala); }
  Pared makePared() { return new Pared(); }
  Puerta makePuerta(Sala sala1, Sala sala2) {
    return new Puerta(sala1, sala2);
  }
  Laberinto crearLaberinto () { ... }
}
```

---

```java
Laberinto crearLaberinto () {
  Laberinto miLab = makeLaberinto();
  Sala hab1 = makeSala(1);
  Sala hab2 = makeSala(2);
  Puerta unaPuerta = makePuerta(hab1, hab2);
  miLab.agregarSala(hab1);
  miLab.agregarSala(hab2);
  hab1.setLado(Direccion.NORTE, makePared());
  hab1.setLado(Direccion.ESTE, unaPuerta);
  hab1.setLado(Direccion.SUR, makePared());
  hab1.setLado(Direccion.OESTE, makePared());
  hab2.setLado(Direccion.NORTE, makePared());
  hab2.setLado(Direccion.ESTE, makePared());
  hab2.setLado(Direccion.SUR, makePared());
  hab2.setLado(Direccion.OESTE, unaPuerta);
  return miLab;
}
```

---

```java
public class JuegoLaberintoMinado extends JuegoLaberinto {
  Pared makePared() {
    return new ParedMinada();
  }
  Sala makeSala(int numSala) {
    return new SalaMinada(numSala);
  }
}

public class JuegoLaberintoHechizado extends JuegoLaberinto {
  Sala makeSala(int numSala) {
    return new SalaHechizada(numSala, lanzarHechizo());
  }
  Puerta makePuerta(Sala sala1, Sala sala2) {
    return new PuertaHechizada(sala1, sala2);
  }
  private Hechizo lanzarHechizo() { ... }
}
```

---
<style scoped>
h3 {
  text-align: center;
  color: blue;
}
</style>

### [Command](https://refactoring.guru/es/design-patterns/command)

![Command, center](./figuras/guru/command-mini-2x.png)

<!--

@startuml
class Client
class Invoker
class Command <<interface>>
class Receiver
class ConcreteCommand

Invoker : setCommand()
Command : execute()
Command : undo()
Receiver : action()
ConcreteCommand : execute()
ConcreteCommand : undo()

Client -> Receiver
Client -> ConcreteCommand
Receiver <- ConcreteCommand
Invoker -> Command
Command <|.. ConcreteCommand

note left of Client
The Client is responsible for
creating a ConcreteCommand and
setting its Receiver.
end note

note bottom of Receiver
The Receiver knows how to
perform the work needed to
carry out the request. Any class
can act as a Receiver.
end note

note bottom of ConcreteCommand
The ConcreteCommand defines a binding between an action
and a Receiver. The Invoker makes a request by calling
execute() and the ConcreteCommand carries it out by
calling one or more actions on the Receiver.
end note

note left of Invoker
The Invoker holds
a command and at
some point asks the
command to carry
out a request by
calling its execute()
method.
end note

note top of Command
Command declares an interface for all commands. A
command is invoked through its execute() method,
which asks a receiver to perform its action.
end note

note right of ConcreteCommand::execute()
The execute method invokes the action(s)
on the receiver needed to fulfill the
request;

public void execute() {
  receiver.action()
}

end note
@enduml
-->

---

#### Estructura

![w:850 center](./figuras/guru/command-structure-2x.png)

---

#### Comportamiento

@startuml
scale 700 width
scale 600 height

participant "editor: Client" as editor
participant "cmdDraw: DrawCommand" as cmdDraw
participant "menuItem: Invoker" as menuItem
participant "image: Receiver" as image

[--> editor: <<create>>
activate editor
editor --> cmdDraw : new DrawCommand(image)
editor -> menuItem: setCommand(cmdDraw)
activate menuItem
deactivate editor
deactivate menuItem

...

[--> menuItem: executeCommand()
activate menuItem

menuItem -> cmdDraw: execute()
activate cmdDraw
cmdDraw -> image: draw()
activate image
deactivate cmdDraw

@enduml


<!--
#### Cliente/Servidor

@startuml
scale 700 width
scale 600 height

participant client
participant anInvoker

box "Server"
participant aCommand
participant aServer
participant aReceiver
end box

activate client
client -> aCommand: new ConcreteCommand()
activate aCommand
deactivate aCommand

client -> anInvoker: add(aCommmand)
activate anInvoker
deactivate client

anInvoker -> aCommand: getData()
activate aCommand

anInvoker <-- aCommand  : ok
deactivate aCommand

client <- anInvoker : send(aCommand)
activate client
deactivate anInvoker

client -> aServer : accept(aCommand)
activate aServer
deactivate client

aCommand <- aServer: execute(this)
activate aCommand

aCommand -> aReceiver: action()
activate aReceiver

@enduml

-->

---
<style scoped>
h3 {
  text-align: center;
  color: blue;
}
</style>

### [Composite](https://refactoring.guru/es/design-patterns/composite)

![Composite, center](./figuras/guru/composite-mini-2x.png)

<!--
@startuml
class Client
class Component
class Leaf
class Composite

Component : operation()
Component : add(Component)
Component : remove(Component)
Component : getChild(int)

Leaf : operation()

Composite : operation()
Composite : add(Component)
Composite : remove(Component)
Composite : getChild(int)

Client -> Component
Component <|– Leaf
Component <|– Composite
Component “0..*” <–o “1” Composite

note top of Client
The Client uses the
Component interface to
manipulate the objects in the
composition.
end note

note top of Component
The Component defines an
interface for all objects in
the composition: both the
composite and the leaf nodes.
end note

note top of Component
The Component may implement a
default behavior for add(), remove(),
getChild() and its operations.
end note

note bottom of Leaf
A Leaf has no
children.
end note

note left of Leaf
Note that the Leaf also
inherits methods like add(),
remove() and getChild(), which
do not necessarily make a lot of
sense for a leaf node. We are
going to come back to this issue.
end note

note bottom of Leaf
A Leaf defines the behavior for the
elements in the composition. It does
this by implementing the operations
the Composite supports.
end note

note bottom of Composite
The Composite’s role is to define
behavior of the components
having children and to store child
components.
end note

note right of Composite
The Composite also
implements the Leaf-
related operations.
Note that some of
these may not make
sense on a Composite,
so in that case an
exception might be
generated.
end note
@enduml

-->

---

#### Estructura

![h:600 center](./figuras/guru/composite-structure-2x.png)

---

<style scoped>
h3 {
  text-align: center;
  color: blue;
}
</style>

### [Adapter](https://refactoring.guru/es/design-patterns/adapter)

![Adapter, center](./figuras/guru/adapter-mini-2x.png)

<!--
@startuml
class Client
class Target <<interface>>
class Adapter
class Adaptee
Target : request()
Adapter : request()
Adaptee : specificRequest()

Client -> Target
Target <|.. Adapter
Adapter -> Adaptee
note on link
Adapter is composed
with the Adapter.
end note

note bottom of Client
The client sees only the
Target interface
end note

note “The Adapter implements\nthe Target interface.” as n1
Target .. n1
n1 .. Adapter

note bottom of Adaptee
All requests get
delegated to the
Adaptee.
end note
@enduml
-->

---

#### Estructura: Adaptador de objetos

![w:850 center](./figuras/guru/object-adapter-structure-2x.png)

---

#### Estructura: Adaptador de clases

![w:850 center](./figuras/guru/class-adapter-structure-2x.png)

---
<style scoped>
h3 {
  text-align: center;
  color: blue;
}
</style>

### [Decorator](https://refactoring.guru/es/design-patterns/decorator)

![Decorator, center](./figuras/guru/decorator-mini-2x.png)

<!--
@startuml
skinparam componentStyle uml2

class Component
class ConcreteComponent
class Decorator
class ConcreteDecoratorA
class ConcreteDecoratorB

Component : methodA()
Component : methodB()
Component : // otherMethods()

ConcreteComponent : methodA()
ConcreteComponent : methodB()
ConcreteComponent : // otherMethods()

Decorator : methodA()
Decorator : methodB()
Decorator : // otherMethods()

ConcreteDecoratorA : Component wrappedObject
ConcreteDecoratorA : methodA()
ConcreteDecoratorA : methodB()
ConcreteDecoratorA : newBehavior()
ConcreteDecoratorA : // otherMethods()

ConcreteDecoratorB : Component wrappedObject
ConcreteDecoratorB : Object newState
ConcreteDecoratorB : methodA()
ConcreteDecoratorB : methodB()
ConcreteDecoratorB : // otherMethods()

Component <|– ConcreteComponent
Component <|– Decorator
Decorator <|– ConcreteDecoratorA
Decorator <|– ConcreteDecoratorB
Decorator –> Component : component
note right on link
Each component can be used on its
own, or wrapped by a decorator
component
end note

note bottom of ConcreteComponent
The ConreteComponent
is the object we are going
to dynamically add new
behavior to it. It extends
Component.
end note

note bottom of Decorator
Decorators implement the
same interface or abstract
class as the component they
are going to decorate.
end note

note bottom of ConcreteDecoratorB
Decorators can extend the
state of the component
end note

note bottom of ConcreteDecoratorB
Decorators can add new methods;
however, new behavior is typically
added by doing computation
before or after an existing method
in the component.
end note

note bottom of ConcreteDecoratorA
The ConcreteDecorator has an
instance variable for the thing
it decorates (the Component the
Decorator wraps).
end note
@enduml
-->

---

#### Estructura

![h:600 center](./figuras/guru/decorator-structure-2x.png)

---
<style scoped>
h3 {
  text-align: center;
  color: blue;
}
</style>

### [Strategy](https://refactoring.guru/es/design-patterns/strategy)

![Strategy, center](./figuras/guru/strategy-mini-2x.png)

---

#### Estructura

![h:500 center](./figuras/guru/strategy-structure-2x.png)

---
<style scoped>
h3 {
  text-align: center;
  color: blue;
}
</style>

### [Observer](https://refactoring.guru/es/design-patterns/observer)

![Observer, center](./figuras/guru/observer-mini-2x.png)

---

<!--
@startuml
class Client
class Invoker
class Command <<interface>>
class Receiver
class ConcreteCommand

Invoker : setCommand()
Command : execute()
Command : undo()
Receiver : action()
ConcreteCommand : execute()
ConcreteCommand : undo()

Client -> Receiver
Client -> ConcreteCommand
Receiver <- ConcreteCommand
Invoker -> Command
Command <|.. ConcreteCommand

note left of Client
The Client is responsible for
creating a ConcreteCommand and
setting its Receiver.
end note

note bottom of Receiver
The Receiver knows how to
perform the work needed to
carry out the request. Any class
can act as a Receiver.
end note

note bottom of ConcreteCommand
The ConcreteCommand defines a binding between an action
and a Receiver. The Invoker makes a request by calling
execute() and the ConcreteCommand carries it out by
calling one or more actions on the Receiver.
end note

note left of Invoker
The Invoker holds
a command and at
some point asks the
command to carry
out a request by
calling its execute()
method.
end note

note top of Command
Command declares an interface for all commands. A
command is invoked through its execute() method,
which asks a receiver to perform its action.
end note

note right of ConcreteCommand::execute()
The execute method invokes the action(s)
on the receiver needed to fulfill the
request;

public void execute() {
 receiver.action()
}

end note
@enduml
-->

#### Estructura

![h:450 center](./figuras/guru/observer-structure-2x.png)

---
<style scoped>
h3 {
  text-align: center;
  color: blue;
}
</style>

### [Visitor](https://refactoring.guru/es/design-patterns/visitor)

![Visitor, center](./figuras/guru/visitor-mini-2x.png)

---

#### Estructura

![h:600 center](./figuras/guru/visitor-structure-2x.png)


---
## Otros patrones específicos

---

### Data Acess Object (DAO)

- Se usa para abstraer y encapsular los accesos a las fuentes de datos, con independencia del soporte concreto de almacenamiento. Su alternativa es el patrón *Active Record*.

![width:800px](./figuras/dao_uml.webp)

---
![width:700px](./figuras/dao_code.png)


---
### Data Transfer Object (DTO)

- Se usa para crear objetos planos (POJO) que puedan ser enviados o recuperados desde servidores remotos en una única invocación. Un DTO no tiene más comportamiento que almacenar y entregar sus propios datos (métodos *getters* y *setters*). 

![width:800px](./figuras/dto_uml.png)

---
![width:750px](./figuras/dto_code.png)


---

# Para profundizar sobre patrones
- Martin Fowler – [Patterns in Enterprise Software](https://martinfowler.com/articles/enterprisePatterns.html): Catálogos de patrones a distintos niveles
    - Martin Fowler – [Patterns of Enterprise Application Architecture (EAA)](https://martinfowler.com/eaaCatalog/)
    - Hohpe y Woolf – [Enterprise Integration Patterns (EIP)](http://www.enterpriseintegrationpatterns.com/)
    - Buschmann y otros – [Pattern-Oriented Software Architecture (POSA)](http://www.amazon.com/exec/obidos/ASIN/0471958697) Volume 1: A system of patterns
- Peter Norvig – [Design Patterns in Dynamic Programming](http://www.norvig.com/design-patterns/design-patterns.pdf): Implementaciones más simples para los patrones de diseño del GoF en lenguajes dinámicos

---
# Para profundizar sobre patrones

- David Arno – [Are design patterns compatible with modern software techniques?](http://www.davidarno.org/2013/06/17/are-design-patterns-compatible-with-modern-software-techniques/)
- Implementaciones de los patrones de diseño del GoF en diversos lenguajes de programación:
    - Kamran Ahmed – [Design Patterns for Humans!](https://github.com/kamranahmedse/design-patterns-for-humans/blob/master/README.md): Explicación de los patrones de diseño del GoF implementados en PHP
    - Márk Török – [Design Patterns in TypeScript](https://github.com/torokmark/design_patterns_in_typescript)
    - Bogdab Vliv - [Design Patterns in Ruby](https://bogdanvlviv.com/posts/ruby/patterns/design-patterns-in-ruby.html)
- Lewis y Fowler – [Microservicios](https://martinfowler.com/articles/microservices.html)
- Chris Richardson - [Microservices patterns](https://microservices.io/)
