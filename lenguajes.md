# Lenguajes

## Técnicas

 - Anónimos y cierres: Java lambdas, Ruby procs y blocks, Scala
 - Mixins:
 	- [Ruby modules](http://ruby-doc.com/docs/ProgrammingRuby/html/tut_modules.html)
 		- Ejemplo: [Comparable](https://en.wikipedia.org/wiki/Mixin#In_Ruby)
 	- [Scala traits](http://docs.scala-lang.org/tutorials/tour/traits)
	 	- Ejemplo: [Traits exercise](https://www.scala-exercises.org/std_lib/traits)
 - Reflexión
 - Metaprogramación

## Paradigmas
 - Objetos
 - Eventos
 - Funcional


## Ejercicios

1. Interfaces funcionales - [Formateo de informes](#informes)
2. [Ruby from other languages](https://www.ruby-lang.org/en/documentation/ruby-from-other-languages/)
3. [Scala tour](http://docs.scala-lang.org/tutorials/tour/tour-of-scala)
4. [Scala exercises](https://www.scala-exercises.org/std_lib/)


# Ejercicio 1 - Interfaces funcionales
<a id="informes"></a>
## Formateo de informes

### Versión en ruby

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

#### Procs
- Proc = objeto función = objeto que solo contiene un trozo de código 
 
```ruby
hello = lambda do	puts('Hello')	puts('I am inside a proc')end
```

#### Blocks

- Code block = Función anónima, cierre o lambda
- Es la parte `do` ... `end`
- Simplificada como  `{` ... `}`

```ruby
hello = lambda {	puts('Hello, I am inside a proc')}
```

- Parametrizable

```ruby
multiply = lambda {|x, y| x * y}

n = multiply.call(20, 3)puts(n)  #60n = multiply.call(10, 50)puts(n)  #500
```

#### Llamada 

```ruby
name = 'John'proc = Proc.new do	name = 'Mary'endproc.callputs(name)
```


#### Paso de bloques como parámetros

- Simplemente, se añade al final de la llamada a un método 
- ¿Dónde se llama al bloque? Donde el método indique con `yield`
- El bloque (realmente un objeto `Proc`) se pasa como una especie de parámetro invisible
 
```ruby
def run_it	puts("Before the yield")	yield	puts("After the yield")end
```
```ruby
run_it do	puts('Hello')	puts('Coming to you from inside the block')end
```
Salida:

```
Before the yieldHelloComing to you from inside the blockAfter the yield
```

- Llamada a un bloque con parámetros:

```ruby
def run_it_with_parameter	puts('Before the yield')	yield(24)	puts('After the yield')endrun_it_with_parameter do |x|	puts('Hello from inside the proc')	puts("The value of x is #{x}")end
```
Salida:

```
Before the yieldHello from inside the procThe value of x is 24After the yield
```

- Hacer explícito el bloque pasado como parámetro: _ampersand_

```ruby
def run_it_with_parameter(&block)	puts('Before the call')	block.call(24)	puts('After the call')end
```
Y para convertir un `Proc` en un bloque pasado como parámetro:

```ruby
my_proc = lambda {|x| puts("The value of x is #{x}")}run_it_with_parameter(&my_proc)
```

### Versión con interfaces funcionales (Ruby procs + blocks)

```ruby
class Report	attr_reader :title, :text	attr_accessor :formatter
		def initialize(&formatter)		@title = 'Monthly Report'		@text = [ 'Things are going', 'really, really well.' ]		@formatter = formatter	end
		def output_report		@formatter.call( self )	endend
```

Formateo HTML:

```ruby
HTML_FORMATTER = lambda do |context|	puts('<html>')	puts(' <head>')	puts("   <title>#{context.title}</title>")	puts(' </head>')	puts(' <body>')	context.text.each do |line|		puts("  <p>#{line}</p>" )	end	puts(' </body>')	puts

report = Report.new &HTML_FORMATTERreport.output_report
```

Formateo de texto:

```ruby
report = Report.new do |context|	puts("***** #{context.title} *****")	context.text.each do |line|		puts(line)	endend
```