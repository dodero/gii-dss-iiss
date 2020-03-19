# EVENTOS ASÍNCRONOS

- [Programación asíncrona](#programaci%C3%B3n-as%C3%ADncrona)
    - [<a id="listeners">Ejercicio introductorio - Listeners</a>](#a-idlistenersejercicio-introductorio---listenersa)
        - [Implementando listeners en Java](#implementando-listeners-en-java)
        - [Ejemplo de listener con y sin lambdas](#ejemplo-de-listener-con-y-sin-lambdas)
        - [Ejercicio](#ejercicio)
    - [Modelos de programación asíncrona](#modelos-de-programaci%C3%B3n-as%C3%ADncrona)
    - [Modelo de paso de continuaciones](#modelo-de-paso-de-continuaciones)
        - [Callbacks](#callbacks)
        - [Thunks](#thunks)
        - [Promesas](#promesas)
    - [Modelos basados en eventos](#modelos-basados-en-eventos)
        - [Eventos](#eventos)
        - [Streams](#streams)
        
## <a id="listeners">Ejercicio introductorio - Listeners</a>

### Implementando listeners en Java

#### Listener con un solo control

```java
public class DemoKeyEvents {
   public static void main(String[] args) {
      DemoKeyEventsFrame frame = new DemoKeyEventsFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setTitle("DemoKeyEvents");
      frame.pack();
      frame.setVisible(true);
   }
}

class DemoKeyEventsFrame extends JFrame implements KeyListener
{
   private JTextField enterField;

   public DemoKeyEventsFrame() {
      enterField = new JTextField(10);
      enterField.addKeyListener(this);

      JPanel panel = new JPanel();
      panel.add(new JLabel("Enter some text:"));
      panel.add(enterField);
      panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      this.setContentPane(panel);
   }

   public void keyPressed(KeyEvent ke) {
      System.out.println(ke.paramString());

      final KeyStroke BEL = KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK);
      KeyStroke k = KeyStroke.getKeyStroke(ke.getKeyCode(), ke.getModifiers());
      if (k == BEL)
      {
         System.out.println("Ctrl-A pressed!");
         Toolkit.getDefaultToolkit().beep();
      }
   }

   public void keyReleased(KeyEvent ke) {
      System.out.println(ke.paramString());
   }

   public void keyTyped(KeyEvent ke) {
      System.out.println(ke.paramString());
   }
}
```

#### Listener con varios controles

```java
public class DemoKeyEvents2 {
   public static void main(String[] args) {
      DemoKeyEvents2Frame frame = new DemoKeyEvents2Frame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setTitle("DemoKeyEvents2");
      frame.pack();
      frame.setVisible(true);
   }
}

class DemoKeyEvents2Frame extends JFrame implements KeyListener {
   private JTextField pro;
   private JTextField con;

   // constructor
   public DemoKeyEvents2Frame() {

      // construct and configure components
      pro = new JTextField(10);
      con = new JTextField(10);

      // add listeners
      pro.addKeyListener(this);
      con.addKeyListener(this);

      // arrange components
      // add components to panels

      JPanel banner = new JPanel();
      banner.add(new JLabel("WHAT'S YOUR OPINION ON ANCHOIVES?"));

      JPanel proPanel = new JPanel();
      proPanel.add(new JLabel("Pro:"));
      proPanel.add(pro);

      JPanel conPanel = new JPanel();
      conPanel.add(new JLabel("Con:"));
      conPanel.add(con);

      // put panels in a content pane panel
      JPanel contentPane = new JPanel();
      contentPane.setLayout(new GridLayout(3, 1));
      contentPane.add(banner);
      contentPane.add(proPanel);
      contentPane.add(conPanel);

      // make panel this JFrame's content pane
      this.setContentPane(contentPane);
   }

   public void keyPressed(KeyEvent ke) {}   // do nothing
   public void keyReleased(KeyEvent ke) {}  // do nothing
   public void keyTyped(KeyEvent ke) {
      Object source = ke.getSource();

      // check if event occurred on pro component
      if (source == pro)
         System.out.println("Pro : " + ke.paramString());

      // check if event occurred on con component
      else if (source == con)
         System.out.println("Con : " + ke.paramString());
   }
}
```

#### Listener con clases anónimas

```java
public class DemoKeyEvents3 {
   public static void main(String[] args) {
      DemoKeyEvents3Frame frame = new DemoKeyEvents3Frame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setTitle("DemoKeyEvents3");
      frame.pack();
      frame.setVisible(true);
   }
}

class DemoKeyEvents3Frame extends JFrame {
   private JTextField enterField;

   // constructor
   public DemoKeyEvents3Frame() {
      enterField = new JTextField(10);

      enterField.addKeyListener(new KeyListener() {
          public void keyPressed(KeyEvent ke) {
            System.out.println(ke.paramString());

            final KeyStroke BEL = KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK);
            KeyStroke k = KeyStroke.getKeyStroke(ke.getKeyCode(), ke.getModifiers());
            if (k == BEL) {
               System.out.println("Ctrl-A pressed!");
               Toolkit.getDefaultToolkit().beep();
            }
          }

          public void keyReleased(KeyEvent ke) {
            System.out.println(ke.paramString());
          }

          public void keyTyped(KeyEvent ke) {
            System.out.println(ke.paramString());
          }
      });

      JPanel panel = new JPanel();
      panel.add(new JLabel("Enter some text:"));
      panel.add(enterField);
      panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      this.setContentPane(panel);
   }

}
```

#### Listener con adaptador

- No hay que redefinir todos los métodos de `KeyListener`
- Los métodos de `KeyAdapter` están definidos pero vacíos

```java
class DemoKeyEvents4Frame extends JFrame {
   private JTextField enterField;

   // constructor
   public DemoKeyEvents4Frame()
   {
      enterField = new JTextField(10);

      enterField.addKeyListener(new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent ke) {
            System.out.println(ke.paramString());

            final KeyStroke BEL = KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK);
            KeyStroke k = KeyStroke.getKeyStroke(ke.getKeyCode(), ke.getModifiers());
            if (k == BEL) {
               System.out.println("Ctrl-A pressed!");
               Toolkit.getDefaultToolkit().beep();
            }
          }

          @Override
          public void keyReleased(KeyEvent ke) {
            System.out.println(ke.paramString());
          }

          @Override
          public void keyTyped(KeyEvent ke) {
            System.out.println(ke.paramString());
          }
      });

      JPanel panel = new JPanel();
      panel.add(new JLabel("Enter some text:"));
      panel.add(enterField);
      panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      this.setContentPane(panel);
   }

}
```

#### Listener con lambdas y dispatch table

```java
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Test {

    @FunctionalInterface
    interface KeyAction {
        public void doAction(); //un sólo método abstracto
    }

    public static void main( String[] args) {

        HashMap<Integer, KeyAction> keyDispatcher = new HashMap<Integer, KeyAction>();

        //Crear instancias de FunctionalInterface mediante lambdas
        keyDispatcher.put(KeyEvent.VK_W, () -> moveUp());
        keyDispatcher.put(KeyEvent.VK_S, () -> moveDown());
        keyDispatcher.put(KeyEvent.VK_A, () -> moveLeft());
        keyDispatcher.put(KeyEvent.VK_D, () -> moveRight());

        // Using a JTextField out of simplicity
        JTextField field = new JTextField();
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                try{
                    keyDispatcher.get(arg0.getKeyCode()).doAction();
                } catch (NullPointerException e) {
                    System.out.println("That button doesn't do anything yet...");
                }
            }
        });

        JFrame frame = new JFrame("Listener Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(field, BorderLayout.NORTH);
        frame.pack();
        frame.setVisible(true);
    }

    private static void moveUp() {
        System.out.println("Moving up");
    }
    private static void moveDown() {
        System.out.println("Moving down");
    }
    private static void moveLeft() {
        System.out.println("Moving left");
    }
    private static void moveRight() {
        System.out.println("Moving right");
    }
}
```

### Ejemplo de listener con y sin lambdas

```java
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This simple Swing program demonstrates how to use Lambda expressions in
 * action listener.
 *
 * @author www.codejava.net
 */
public class ListenerLambdaExample extends JFrame {

    private JButton button = new JButton("Click Me!");

    public ListenerLambdaExample() {
        super("Listener Lambda Example");

        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(button);

        // Java 7 - tradicional, sin lambdas
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.out.println("Handled by anonymous class listener");
            }
        });

        // Java 8 - con lambdas
        button.addActionListener(e -> System.out.println("Handled by Lambda listener"));

        button.addActionListener(e -> {
            System.out.println("Handled Lambda listener");
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 100);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ListenerLambdaExample().setVisible(true);
            }
        });
    }
}
```

### Ejercicio

- Refactorizar `DemoKeyEvents2` con adaptador, interfaces funcionales (lambdas) y tabla de dispatch

## Modelos de programación asíncrona

La programación asíncrona promueve la definición de operaciones **no bloqueantes**.

>![modelos de ejecución](./figuras/modelos-ejecucion.png)
>
<small>por <cite>Javier Vélez Reyes, [Programación asíncrona en JavaScript](https://github.com/javiervelezreyes/Talleres.uca.programacion-asincrona)</cite></small>

Las funciones no bloqueantes afectan a:

- El **estado** del programa
- La lógica de **continuación** del programa

 programación secuencial   |  programación asíncrona
:-------------------------:|:-------------------------:
![Modelo de programación secuencial](./figuras/prog-secuencial.png) | ![Modelo de programación asíncrona](./figuras/prog-asincrona.png)

## Modelo de paso de continuaciones

Aumentar la aridad de la función no bloqueante en 1 argumento adicional, donde se indica la lógica de continuación.

### Callbacks

La lógica de continuación se indica mediante una función de **retrollamada** o _callback_.

![Paso de callback](./figuras/cont-callback.png)

Ejemplos: implementaciones de _listener_ del [ejercicio introductorio](#a-idlistenersejercicio-introductorio---listenersa)

- Con clases anónimas
- Con adaptadores
- Con funciones anónimas o lambdas

#### Ejemplo: Ajax + jQuery callbacks

- [Ajax](http://learn.jquery.com/ajax/)
- [jQuery](http://devdocs.io/jquery/)
- [jQuery Callbacks object](http://devdocs.io/jquery-callbacks-object/)

#### Ejemplo: callback en TypeScript

```typescript
function delayedResponseWithCallback(callback: Function) {
    function delayedAfterTimeout() {
        console.log(`delayedAfterTimeout`);
        callback();
    }
    setTimeout(delayedAfterTimeout, 1000);
}

function callDelayedAndWait() {
    function afterWait() {
        console.log(`afterWait`);
    }
    console.log(`calling delayedResponseWithCallback`);
    delayedResponseWithCallback(afterWait);
    console.log(`after calling delayedResponseWithCallback`);
}
callDelayedAndWait();
```

Salida:

```text
calling delayedResponseWithCallback
after calling delayedResponseWithCallback
delayedAfterTimeout
afterWait
```

El uso de callbacks hace el código complejo, repetitivo y difícil de entender, especialmente cuando el tamaño del código crece.

### Thunks

**[thunk](https://en.wikipedia.org/wiki/Thunk)** = subrutina empleada para inyectar un cómputo adicional en otra subrutina

- Difieren un cómputo hasta que éste se necesita
- Insertan una operación al principio o al final de otra
- Aumentan la aridad de la función no bloqueante en una fase adicional de evaluación parcial para recibir la lógica de continuación
- Un thunk puede guardar su estado final para evitar recalcularlo (_memoization_)

![Paso de thunk](./figuras/cont-thunk.png)

#### Lenguajes: Ruby

- Cualquier función en Ruby puede recibir un **bloque** `do`... `end` como argumento adicional (no explícito) a la llamada
- Un _thunk_ es como un bloque con un `yield` al final

### Promesas

Modelo de [futuros y promesas](https://en.wikipedia.org/wiki/Futures_and_promises)

- **Futuro**: marcador de posición (_placeholder_), de solo lectura, para una variable que representa el resultado de un cómputo asíncrono
- **Promesa**: contenedor de una asignación escribible (solo para inicialización), que fija el valor de un _futuro_.

En programación funcional, los futuros y promesas sirven para desacoplar un valor (el futuro) de cómo éste se calculó (la promesa), permitiendo así la paralelización de los cálculos.

![Promesas](./figuras/promesas.png)

El cliente recibe como respuesta inmediata una abstracción de datos (la promesa) que representa un compromiso de valor futuro, con inyectores (then, catch) para incluir la lógica de continuación.

Se pueden encadenar cálculos usando futuros _computables_ o _escuchables_, que sirven para indicar a un thread que ejecute una determinada tarea y, cuando termine, se dirija a hacer otra tarea usando el resultado de la tarea anterior.

#### Lenguajes: TypeScript

En TypeScript, una `Promise<T>` es un objeto que, en su creación, recibe una función (anónima o no) que acepta dos callbacks (`resolve` y `reject`) y devuelve un valor de tipo `T`.

##### Ejemplo: definición de promesa que resuelve

```typescript
function delayedPromise(): Promise<void> {
    return new Promise<void>
    (
        // función anónima con dos callbacks como argumentos
        (   resolve : () => void,   //callback para resolver
            reject: () => void      //callbak para rechazar
        ) => {
            // función que resuelve tras un timeout de 1 sg.
            function afterTimeout() {
                resolve();
            }
            setTimeout(afterTimeout, 1000);
        }
    );
}
```

##### Ejemplo: uso para resolver

```typescript
function callDelayedPromise() {
    console.log(`calling delayedPromise`);
    delayedPromise().then(
        // función anónima a llamar cuando se resuelva la promesa
        () => { console.log(`delayedPromise.then()`) }
    );
}

callDelayedPromise();
```

Salida:

    calling delayedPromise
    delayedPromise.then()

##### Ejemplo: definición de promesa que rechaza

```typescript
function errorPromise(): Promise<void> {
    return new Promise<void>
    (
        (   resolve: () => void,
            reject: () => void
        ) => {
            reject();
        }
    );
}
```

##### Ejemplo: uso para rechazar

```typescript
function callErrorPromise() {
    console.log(`calling errorPromise`);
    errorPromise().then(
        () => { console.log(`no error.`) }
    ).catch(
        () => { console.log(`an error occurred`)}
    );
}

callErrorPromise();
```

Salida:

    calling errorPromise
    an error occurred

#### Comparación callbacks-promises en TypeScript

##### Mecanismo de los callback

```typescript
function standardCallback() {
    function afterCallbackSuccess() {
    // execute this code
    }
    function afterCallbackError() {
    // execute on error
    }
    // invoke async function
    invokeAsync(afterCallbackSuccess, afterCallbackError);
}
```

##### Sintaxis (_fluent_) de las promesas:

```typescript
function usingPromises() {
    // invoke async function
    delayedPromise().then(
        () => {
            // execute on success
        }
    ).catch (
        () => {
            // execute on error
        }
    );
}
```

#### Async/await

- El prefijo `await` hace que se espere a que se llame a la función asíncrona antes de continuar con la ejecución del programa.
- Esto genera un flujo de ejecución de la lógica del programa más fácil de leer y de seguir, pausando la ejecución hasta que se cumpla la promesa.

```typescript
function awaitDelayed(): Promise<void> {
    return new Promise<void> (
        (   resolve: () => void,
            reject: () => void ) =>
            {
                function afterWait() {
                    console.log(`calling resolve`);
                    resolve();
                }
                setTimeout(afterWait, 1000);
            }
        );
}

async function callAwaitDelayed() {
    console.log(`call awaitDelayed`);
    await awaitDelayed();
    console.log(`after awaitDelayed`);
}
callAwaitDelayed();
```

Salida:

```text
call awaitDelayed
calling resolve
after awaitDelayed
```

##### Comparación sintaxis then/catch y async/await

Promesas que usan then/catch para definir funciones anónimas a llamar dependiendo del resultado éxito/fracaso de la ejecución:

```typescript
function simplePromises() {
    // invoke async function
    delayedPromise().then(
        () => {
            // execute on success
        }
    ).catch (
        () => {
            // execute on error
        }
    );
    // code here does NOT wait for async call
}
```

Sintaxis async/await, más legible y menos propensa a errores:

```typescript
async function usingAsyncSyntax() {
    try {
        await delayedPromise();
        // execute on success
    } catch(error) {
        // execute on error
    }
    // code here waits for async call
}
```

#### Lenguajes: Java

En Java hay definida una interfaz explícita para los futuros:

- Desde Java 5: [`java.util.concurrent.Future`](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Future.html) 
- Desde Java 8, inspirado por los [`ListenableFuture`](https://github.com/google/guava/wiki/ListenableFutureExplained) de Guava: [`java.util.concurrent.CompletableFuture`](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/CompletableFuture.html)

##### Ejemplo: `Future` en Java

```java
// Callable<V> = Interfaz funcional que representa a una operación sin args
// y que devuelve un resultado de tipo V (permite checked exceptions)
public static class MyCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        Thread.sleep(1000);
        return 1;
    }
}

public static void main(String[] args) throws Exception{
    ExecutorService exec = Executors.newSingleThreadExecutor();
    Future<Integer> f = exec.submit(new MyCallable());
    System.out.println(f.isDone()); //False
    System.out.println(f.get()); //Waits until the task is done, then prints 1
}
```

##### Ejemplo: `CompletableFuture` en Java

```java
// Supplier<T> = Interfaz funcional que representa a una operación sin args
// y que devuelve un resultado de tipo T (no permite checked exceptions)
public static class MySupplier implements Supplier<Integer> {
    @Override
    public Integer get() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //Do nothing
        }
        return 1;
    }
}

public static class PlusOne implements Function<Integer, Integer> {
    @Override
    public Integer apply(Integer x) {
        return x + 1;
    }
}

public static void main(String[] args) throws Exception {
    ExecutorService exec = Executors.newSingleThreadExecutor();
    CompletableFuture<Integer> f = CompletableFuture.supplyAsync(new MySupplier(), exec);
    System.out.println(f.isDone()); // False
    CompletableFuture<Integer> f2 = f.thenApply(new PlusOne());
    System.out.println(f2.get()); // Waits until the "calculation" is done, then prints 2
}
```

### Lenguajes, JavaScript

> LECTURA recomendada: [Promises/A+](https://promisesaplus.com/): An open standard for sound, interoperable JavaScript promises.

## Modelos basados en eventos

Las operaciones disparan eventos de diferentes tipos, que son escuchados por los manejadores (_listeners_) de eventos, que los clientes han registrado en un bus de eventos

### Eventos

![Eventos](./figuras/eventos.png)

### Streams

Los datos fluyen por pipelines y se consumen siguiendo modelos _push_ o _pull_

![Streams](./figuras/streams.png)

#### Observables

> LECTURA recomendada: [The introduction to Reactive Programming you've been missing (by @andrestaltz)](https://gist.github.com/staltz/868e7e9bc2a7b8c1f754)
