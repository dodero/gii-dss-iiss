# Diseño de Sistemas Software

Apuntes de la asignatura Diseño de Sistemas Software del Grado en Ingeniería Informática de la Universidad de Cádiz.

Curso 2024-25

## Slides

Para generar las slides hay que instalar [marp-cli](https://github.com/marp-team/marp-cli) y sus dependencias. Por ejemplo, para instalar `marp-cli` y las dos dependencias necesarias para generar estas slides (`markdown-it-plantuml`y `markdown-it-table-of-contents`) hay que ejecutar los siguientes comandos:

```bash
npm install --save-dev @marp-team/marp-cli
npm install --save-dev markdown-it-plantuml
npm install --save-dev markdown-it-table-of-contents
```

Se recomienda instalar los paquetes y sus dependencias en la carpeta `marp/`

- Para generar los slides en PDF usar el siguiente comando.

`marp/pdf-build [FILENAME]`

- Para generar los slides en HTML usar el siguiente comando:

`marp/html-build [FILENAME]`

Los PDF se generan por defecto en la carpeta `pdf/`. Los ficheros HTML e imágenes se generan por defecto en la carpeta `html/`. Estas carpetas deben crearse antes de generar los ficheros.
