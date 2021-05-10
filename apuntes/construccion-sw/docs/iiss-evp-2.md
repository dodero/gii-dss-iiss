# EVENTOS ASÍNCRONOS

## Programación asíncrona

La programación asíncrona promueve la definición de operaciones **no bloqueantes**.

![modelos de ejecución](./figuras/modelos-ejecucion.png)

<small>figura por <cite>Javier Vélez Reyes, [Programación asíncrona en JavaScript](https://github.com/javiervelezreyes/Talleres.uca.programacion-asincrona)</cite></small>

Las funciones no bloqueantes afectan a:

- El **estado** del programa
- La lógica de **continuación** del programa

 programación secuencial   |  programación asíncrona
:-------------------------:|:-------------------------:
![Modelo de programación secuencial](./figuras/prog-secuencial.png) | ![Modelo de programación asíncrona](./figuras/prog-asincrona.png)
<small>figura por <cite>Javier Vélez Reyes, [Programación asíncrona en JavaScript](https://github.com/javiervelezreyes/Talleres.uca.programacion-asincrona)</cite></small>


### Modelos de paso de <a id="continuaciones">continuaciones</a>

Aumentar la aridad de la función no bloqueante en 1 argumento adicional, donde se indica la lógica de continuación.

#### Callbacks

La lógica de continuación se indica mediante una función de **retrollamada** o _callback_.

![Paso de callback](./figuras/cont-callback.png)
<small>figura por <cite>Javier Vélez Reyes, [Programación asíncrona en JavaScript](https://github.com/javiervelezreyes/Talleres.uca.programacion-asincrona)</cite></small>


Ejemplos: implementaciones de _listener_ del [ejercicio introductorio](../iiss-evp-1/)

- Con clases anónimas
- Con adaptadores
- Con funciones anónimas o lambdas

##### Ejemplo: Ajax + jQuery callbacks

- [Ajax](http://learn.jquery.com/ajax/)
- [jQuery](http://devdocs.io/jquery/)
- [jQuery Callbacks object](http://devdocs.io/jquery-callbacks-object/)

<!--
##### Ejemplo: callback en TypeScript

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

-->

##### Ejemplo: callback en Javascript

**Versión síncrona**:

```javascript
// Versión síncrona
function main() {
    r1 = serv1("datos iniciales");
    r2 = serv2(r1);
    // También se podría haber escrito r2 = serv2(serv1("datos iniciales"))
    console.log("Resultado final: { " + r2 + " }");
}

function serv1(parametros) {
    return "Tardo en calcular r1 a partir de { " + parametros + " }";
}

function serv2(resultado1) {
    return "Tardo en calcular r2 a partir de { " + resultado1 + " }";
}
```

**Ejemplo con _callbacks_**

```javascript
// Versión asíncrona.
// Las funciones asinc1() y asinc2() admiten un callback
// como segundo parámetro, al cual llamarán pasándole el resultado del cómputo
function main() {
    asinc1("datos iniciales", function(r1){
        // Tenemos el resultado de asinc1
        asinc2(r1, function(r2) {
            console.log("Resultado final: { " + r2 + " }");
        });
    });
}

function asinc1(parametros, callback) {
    r1 = "Tardo en calcular r1 a partir de { " + parametros + " }";
    callback(r1);
}

function asinc2(resultado1, callback) {
    r2 = "Tardo en calcular r2 a partir de { " + resultado1 + " }";
    callback(r2);
}
```

##### _Callback Hell_:

El uso de callbacks hace el código complejo, repetitivo y difícil de entender, especialmente cuando el tamaño del código crece.

- La anidación empeora si se necesita el resultado de una función para llamar a otra: funciones que son parámetros de otras funciones, que son parámetros de otras, etc.
- El código fuente se va indentando más y más para luego ir deshaciendo esa indentación a medida que se cierran llaves y paréntesis.
- La lógica está al revés: las funciones no devuelven resultados, sino que pasan esos resultados como parámetros a otras funciones; las funciones que manejan la respuesta son también pasadas como parámetros
- El flujo de gestión de errores también se complica y [no pueden usarse excepciones](https://basarat.gitbook.io/typescript/future-javascript/promise#callback-style-code).

<!--
#### Thunks

**[thunk](https://en.wikipedia.org/wiki/Thunk)** = subrutina empleada para inyectar un cómputo adicional en otra subrutina

- Difieren un cómputo hasta que éste se necesita
- Insertan una operación al principio o al final de otra
- Aumentan la aridad de la función no bloqueante en una fase adicional de evaluación parcial para recibir la lógica de continuación
- Un thunk puede guardar su estado final para evitar recalcularlo (_memoization_)

![Paso de thunk](./figuras/cont-thunk.png)

<small>figura por <cite>Javier Vélez Reyes, [Programación asíncrona en JavaScript](https://github.com/javiervelezreyes/Talleres.uca.programacion-asincrona)</cite></small>

##### Lenguajes: Ruby

- Cualquier función en Ruby puede recibir un **bloque** `do`... `end` como argumento adicional (no explícito) a la llamada
- Un _thunk_ es como un bloque con un `yield` al final

-->

#### <a id="promesas">Promesas</a>

Modelo de [futuros y promesas](https://en.wikipedia.org/wiki/Futures_and_promises)

- **Futuro**: marcador de posición (_placeholder_), de solo lectura, para una variable que representa el resultado de un cómputo asíncrono
- **Promesa**: contenedor de una asignación escribible (solo para inicialización), que fija el valor de un _futuro_.

Los futuros y promesas sirven para desacoplar un valor (el futuro) de cómo éste se calculó (la promesa), permitiendo así la paralelización de los cálculos.

![Promesas](./figuras/promesas.png)

<small>figura por <cite>Javier Vélez Reyes, [Programación asíncrona en JavaScript](https://github.com/javiervelezreyes/Talleres.uca.programacion-asincrona)</cite></small>


El cliente recibe como respuesta inmediata una abstracción de datos (la `Promise`) que representa un compromiso de valor futuro, con inyectores (`then`, `catch`) para incluir la lógica de continuación.

Las promesas se pueden resolver (_resolve_) o rechazar (_reject_):

Se pueden encadenar cálculos usando futuros _computables_ o _escuchables_, que sirven para indicar a un thread que ejecute una determinada tarea y, cuando termine, se dirija a hacer otra tarea usando el resultado de la tarea anterior.

##### Promesas en Javascript

```javascript
const promise = new Promise((resolve, reject) => {
    // las funciones resolve/reject controlan el destino de la promesa
});
```

**Ejemplo con promesas**:

```javascript
// Versión con promesas
// Ahora asinc1 y asinc2 se supone que devuelven una promesa (que solo resuelve)
function main() {
    asinc1("datos iniciales")
    .then(function(r1){ return asinc2(r1); })
    .then(function(r2){
        console.log("Resultado final: " + r2); 
    }).catch(function(err){
        console.log("Error: "+ err.message)
    });
}

// Lo anterior puede escribirse más conciso:
function main() {
    asinc1("datos iniciales")
    .then(asinc2)
    .then(function(r2){
        console.log("Resultado final: " + r2); 
    }).catch(function(err){
        console.log("Error: "+ err.message)
    });
}

function asinc1(parametros) {
    return new Promise((resolve, reject) => {
        resolve("Tardo en calcular r1 a partir de { " + parametros + " }");
    });
}

function asinc2(resultado1) {
    return new Promise((resolve, reject) => {
        resolve("Tardo en calcular r2 a partir de { " + resultado1 + " }");
    });
}

// Si asinc2 devolviera un error
function asinc2(resultado1) {
    return new Promise((resolve, reject) => {
        reject( new Error("Ha habido un error en el cálculo de r2 a partir de { " + resultado1 + " }"));
    });
}
// Salida => "Error: Ha habido un error en el cálculo de r2 a partir de { Tardo en calcular r1 a partir de { datos iniciales } }"

// Si asinc1 devolviera un error
function asinc1(parametros) {
    return new Promise((resolve, reject) => {
        reject( new Error("Ha habido un error en el cálculo de r1 a partir de { " + parametros + " }"));
    });
}
// Salida => "Error: Ha habido un error en el cálculo de r1 a partir de { datos iniciales }"
```


**Solución al _Callback Hell_**: 

- Las promesas evitan la anidación y hacen más simple el manejo de errores.
- La ventaja de las promesas es que se pueden [encadenar](https://basarat.gitbook.io/typescript/future-javascript/promise#chain-ability-of-promises).
- Una promesa tiene un método `then()`:

    - `.then()` recibe una función, que será ejecutada automáticamente cuando la promesa se resuelva. Esta función recibirá como parámetro el valor de la promesa (el resultado esperado).
    - `.then()` devuelve una nueva promesa, que se resolverá cuando se ejecute la función que le habíamos asociado.
    - Se pueden encadenar varios `.then()` para simular un código secuencial, conforme se van resolviendo promesas.
  
- Una promesa tiene un método `catch()`:

    - Se puede agregar la gestión de errores de cualquier parte de la cadena de llamadas asíncronas con un solo `.catch()`
    - `.catch()` devuelve una promesa nueva, creando una cadena de promesas
    - Cualquier error síncrono generado en un `then` o un `catch` hace que la promesa se rechace, y se llame al `catch` más apropiado


<!--
##### Lenguajes: TypeScript

En TypeScript, una `Promise<T>` es un objeto que, en su creación, recibe una función (anónima o no) que acepta dos callbacks (`resolve` y `reject`) y devuelve un valor de tipo `T`.

###### Ejemplo: definición de promesa que resuelve

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

###### Ejemplo: uso para resolver

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

###### Ejemplo: definición de promesa que rechaza

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

###### Ejemplo: uso para rechazar

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

##### Comparación callbacks-promises en TypeScript

###### Mecanismo de los callback

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

###### Sintaxis (_fluent_) de las promesas

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

-->

##### Sintaxis `async`/`await`

- El prefijo `await` hace que se espere a que se llame a la función asíncrona antes de continuar con la ejecución del programa.
- Esto genera un flujo de ejecución de la lógica del programa más fácil de leer y de seguir, pausando la ejecución hasta que se cumpla la promesa.

`async`/`await` es azúcar sintáctico para usar promesas con una nueva sintaxis que las oculta y las hace parecer código síncrono:

  - `await` delante de una llamada a una función entiende que esa función retorna una promesa.
  - La ejecución se pausa y sólo se reanuda cuando la promesa haya sido resuelta.
  - Entonces `await` devuelve como resultado el valor de la promesa.


**Ejemplo con async/await en Javascript**

```javascript
async function main() {
    r1 = await asinc1("datos iniciales");
    r2 = await asinc2(r1);
    console.log("Resultado final: { " + r2 + " }");
}
```

Comparar esta versión asíncrona con la versión síncrona inicial.

<!--

###### Lenguajes: TypeScript


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

-->

<!--
###### Comparación sintaxis then/catch y async/await

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

-->

#### Futuros

##### Futuros en Java

En Java hay definida una interfaz explícita para los futuros:

- Desde Java 5: [`java.util.concurrent.Future`](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Future.html) 
- Desde Java 8, inspirado por los [`ListenableFuture`](https://github.com/google/guava/wiki/ListenableFutureExplained) de Guava: [`java.util.concurrent.CompletableFuture`](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/CompletableFuture.html)

**Ejemplo: `Future` en Java**

```java
import java.util.concurrent.*;

public class Main {
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
}
```

**Ejemplo: `CompletableFuture` en Java**

```java
import java.util.concurrent.*;
import java.util.function.*;

public class Main {
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
}    
```

<!--
#### Lenguajes, JavaScript

> LECTURA recomendada: [Promises/A+](https://promisesaplus.com/): An open standard for sound, interoperable JavaScript promises.

-->
