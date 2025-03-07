---
title: Práctica Colaborativa de Github
description: Asignaturas del grado en Ingeniería Informática 
---

# Práctica Colaborativa de GitHub

## Objetivos

- Comprender y aplicar los comandos básicos de Git en un flujo de trabajo colaborativo.
- Aprender a gestionar ramas, realizar merges y rebases, y utilizar el archivo .gitignore para excluir archivos del control de versiones.

La práctica debe hacerse en parejas.

## Herramientas

- Cuenta de GitHub
- Cliente de línea de comandos `git` instalado

## Preparación

1. Elegir un nombre de un grupo para la realización de las prácticas. La actividad para elección está disponible en el Campus Virtual.
2. Añadir a los estudiantes a la organización `https://github.com/uca-iiss`: Invitar a los estudiantes a unirse a `uca-iiss` para poder gestionar permisos y el acceso a los repositorios.
3. Gestionar los permisos de acceso: Controlar quién tiene acceso a cada repositorio, dando permisos de escritura a los estudiantes que deban colaborar en un repositorio específico.
4. Un estudiante crea un repositorio privado en `uca-iiss` llamado `GRUPO-practica-git`, donde GRUPO es el nombre oficial de un grupo de prácticas.
5. El profesor o el estudiante creador del repositorio puede invitar al otro compañero/a como colaborador con permisos de escritura.

Los repositorios para realizar las prácticas por todos los estudiantes serán privados y deben pertentecer a la organización `uca-iiss`.

## Ejercicios

1. Clonar el repositorio:

   - Cada estudiante clona el repositorio en su máquina local: `git clone <URL_del_repositorio>`.

2. Configuración inicial:

   - Dentro del repositorio clonado, crear un archivo llamado _autores.txt_ donde cada estudiante escriba su nombre y su identificador UCA.
   - Crear un archivo .gitignore. Añadir al archivo .gitignore lo necesario para excluir archivos temporales, logs, etc.
   - Añadir los archivos autores.txt y .gitignore al repositorio local: `git add autores.txt .gitignore`.
   - Confirmar los cambios con un mensaje descriptivo: `git commit -m "Añadido autores.txt y .gitignore"`.
   - Subir los cambios al repositorio remoto: `git push`.

3. Trabajo con ramas (Estudiante 1):

   - Un estudiante crea una rama llamada feature/descripcion: `git checkout -b feature/descripcion`.
   - En la rama feature/descripcion, crear un archivo _descripcion.md_ con una breve descripción del objetivo de la práctica y los nombres de los miembros del equipo.
   - Añadir el archivo descripcion.md al repositorio local: `git add descripcion.md`.
   - Confirmar los cambios: `git commit -m "Añadido archivo descripcion.md"`.
   - Subir la rama al repositorio remoto: `git push origin feature/descripcion`.

4. Trabajo con ramas (Estudiante 2):

   - El segundo estudiante crea una rama llamada feature/licencia: `git checkout -b feature/licencia`.
   - Añadir un archivo de licencia (v.g., LICENSE.md) al repositorio. Puedes copiar el contenido de una licencia de código abierto común (MIT, Apache 2.0, etc.).
   - Añadir el archivo LICENSE.md al repositorio local: `git add LICENSE.md`.
   - Confirmar los cambios: `git commit -m "Añadido archivo LICENSE.md"`.
   - Subir la rama al repositorio remoto: `git push origin feature/licencia`.

5. Merge y resolución de conflictos:

   - Desde la rama main, el primer estudiante hace un pull para asegurarse de tener los últimos cambios: `git pull`.
   - Desde la rama main, el primer estudiante hace merge de la rama feature/descripcion: `git merge feature/descripcion`.
   - Si hay conflictos (v.g., si ambos estudiantes modificaron el mismo archivo), resolverlos manualmente editando el archivo, indicando claramente la resolución del conflicto con un comentario.
   - Añadir los archivos modificados al _stage_: `git add <archivo_conflicto>`.
   - Confirmar el merge: `git commit -m "Merge de feature/descripcion y resueltos conflictos"`.
   - Subir los cambios a main: `git push`.

6. Rebase de ramas:

   - El segundo estudiante, desde su rama feature/licencia, realiza un rebase con la rama main: `git rebase origin/main`.
   - Si hay conflictos durante el rebase, resolverlos manualmente y continuar el rebase con `git add <archivo_conflicto>` y `git rebase --continue`.
   - Una vez completado el rebase, subir los cambios a la rama feature/licencia: `git push origin feature/licencia --force` (el force es necesario después de un rebase).

7. Merge final:

   - El primer estudiante, desde la rama main, hace un pull para asegurarse de tener los últimos cambios: `git pull`.
   - El primer estudiante hace merge de la rama feature/licencia en main: `git merge feature/licencia`.
   - Si hay conflictos, resolverlos y confirmar el merge.
   - Subir los cambios finales a main: `git push`.

8. Limpieza:

   - Eliminar las ramas feature/descripcion y feature/licencia localmente:
  
        ```bash
        git branch -d feature/descripcion
        git branch -d feature/licencia
        ```

   - Eliminar las ramas feature/descripcion y feature/licencia en el repositorio remoto:

        ```bash
        git push origin --delete feature/descripcion
        git push origin --delete feature/licencia
        ```

## Notas adicionales:

- Es fundamental que los mensajes de los commits sean descriptivos y claros.
- Se recomienda usar un editor de código con soporte para Git para facilitar la resolución de conflictos.
- Antes de realizar un push o un merge, siempre es aconsejable hacer un pull para evitar conflictos innecesarios.

Si tiene alguna duda sobre el enunciado de la práctica o encuentra algún error o aspecto mejorable, puede consultar al profesor e iniciar un [pull request](https://docs.github.com/es/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/about-pull-requests) en GitHub con la corrección o mejora propuesta.

## Criterios de Evaluación:

- [ ] Existencia del repositorio de prácticas en la organización, disponible para el primer estudiante.
- [ ] El segundo estudiante figura como colaborador del repositorio.
- [ ] Existencia del archivo autores.txt con el nombre e identificador UCA de ambos estudiantes.
- [ ] Existencia del archivo .gitignore con exclusiones típicas (*.log, tmp/, etc.)
- [ ] Existencia del archivo descripcion.md en la rama feature/descripcion con una descripción de la práctica.
- [ ] Existencia del archivo LICENSE.md en la rama feature/licencia con una licencia válida.
- [ ] La rama main contiene los cambios de feature/descripcion y feature/licencia.
- [ ] No existen las ramas feature/descripcion y feature/licencia en el repositorio remoto.
- [ ] En caso de conflicto, el archivo resultante en main contiene una resolución clara del mismo (v.g., un comentario indicando cómo se resolvió).
- [ ] El historial de commits muestra evidencia del merge y del rebase.
- [ ] Se proponen mejoras o correcciones al enunciado de la práctica mediante un pull request.
