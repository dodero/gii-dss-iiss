# EVENTOS ASÍNCRONOS

## Programación asíncrona

### <a id="eventos">Eventos</a>

Las operaciones disparan eventos de diferentes tipos, que son escuchados por los manejadores (_listeners_) de eventos, que los clientes han registrado en un bus de eventos.

![Eventos](./figuras/eventos.png)

<small>figura por <cite>Javier Vélez Reyes, [Programación asíncrona en JavaScript](https://github.com/javiervelezreyes/Talleres.uca.programacion-asincrona)</cite></small>


**Ventajas**:

- Aplicaciones más interactivas
- Mejor uso de los recursos

**Estrategias**:

Estrategias para escribir aplicaciones basadas en eventos:

1. Máquinas de estados finitos (FSM)
2. Implementaciones del patrón _Observer_
3. Sistemas de Publicación/suscripción (PubSub)
4. Streams y programación reactiva

#### Máquinas de estados

- Una FSM es una especificación de cómo manejar eventos.
- Lista de estados + estado actual
- Para cada estado, transiciones a otros estados significativos
- Las transiciones vienen definidas por **eventos**
- Cada transición delimita una acción con la respuesta a cada evento

#### Patrón Observer

- **Observable**: fuente de eventos
- **Observadores**: lista de clientes interesados en los eventos

Los observadores se registran ellos mismos en cada observable $\Rightarrow$ produce acoplamiento.

Las acciones de callback son gestionadas por los observables, que suele mantener una lista interna de observadores $\Rightarrow$ produce cuellos de botella.


#### PubSub

- Los sistemas de PubSub son Observers generalizados.
- Los publicadores y los suscriptores se conectan por **canales**
- Suelen implementarse en bibliotecas aparte de mensajería o _Message Queues_ (MQ)
- Cada canal tiene un nombre, empleado por publicadores y sucriptores para desacoplarse entre sí
- La comunicación puede hacerse asíncrona

Ejemplos de bibliotecas de PubSub/MQ:

- RabbitMQ
- ZeroMQ
- NATS
- Apache ActiveMQ
- etc.

Protocolos de MQ:

- AMQP
- MQTT
- STOMP
- etc.

¿Cómo crear sistemas que respondan a combinaciones de eventos? Hay que añadir la dimensión del _tiempo_ al procesamiento de los eventos

Los eventos deben disparar reacciones en el código, pero no es fácil conectar las acciones con los eventos. Para facilitarlo, se usan _streams_...

#### Streams y Rx

Los datos fluyen por pipelines y se consumen siguiendo modelos _push_ o _pull_

![Streams](./figuras/streams.png)

<small>figura por <cite>Javier Vélez Reyes, [Programación asíncrona en JavaScript](https://github.com/javiervelezreyes/Talleres.uca.programacion-asincrona)</cite></small>


Un stream trata a los eventos como colleciones de datos, de forma que pueden ser tratados como cualquier otra colección: manipularlos, combinarlos, filtrarlos, etc.

Además, los streams pueden ser asíncronos.

**Programación reactiva (Rx)**

Es un paradigma, parte de la programación asíncrona: la disponibilidad de información nueva conduce la lógica del programa, en vez de dejar que el control de flujo sea dirigido por un hilo de ejecución.

- Modelo de **Observables**: tratar a los streams de eventos asíncronos con las mismas operaciones sencillas de composición que se usan para las colecciones de datos.
- Bibliotecas de programación Rx: [reactivex.io](http://reactivex.io/)
- [Principios](http://reactivex.io/intro.html)

**Observables**

Los Observables se pueden:

- Crear: `Create`, `Defer`, `From`, `Interval`, `Just`, `Range`, `Repeat`, `Start`, `Timer`
- Transformar: `Buffer`, `FlatMap`, `GroupBy`, `Map`, `Scan`, `Window`
- Filtrar: `Debounce`, `Distinct`, `ElementAt`, `Filter`, `IgnoreElements`, `Last`, `Sample`, `Skip`, `SkipLast`, `Take`, `TakeLast`
- Combinar: `And`/`Then`/`When`, `CombineLatest`, `Join`, `Merge`, `StartWith`, `Switch`, `Zip`
- Etc... [Operadores Rx](http://reactivex.io/documentation/operators.html)

> LECTURA recomendada: [The introduction to Reactive Programming you've been missing (by @andrestaltz)](https://gist.github.com/staltz/868e7e9bc2a7b8c1f754)


¿Qué añade un Observable al patrón Observer? Añade a un Observer asíncrono la semántica de un _Iterable_ síncrono:

- `onCompleted()`: para que el publicador avise al suscriptor que no hay más datos disponibles en el stream (los Iterables simplemente acaban su iteración)
- `onError()`: para que el productor avise al suscriptor que ha ocurrido un error (en su lugar, los Iterables elevan excepciones)
 

Ejemplos de frameworks de streaming:

- Apache Kafka
- NATS Streaming
- Spark Streaming
- Amazon Kinesis
- Apache Pulsar
  

!!! info "Lectura recomendada"
    Thomas & Hunt. [The Pragmatic Programmer, 2nd edition](bibliografia.md#pragmatic2), 2022.
    Capítulo: *Transforming Programming*
