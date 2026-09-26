# Taller 01: Sistema de Control del Grupo POO
# Integrante: 
* Benjamín Araya - [Ingresa tu RUT aquí] - ICCI

Asignatura: Programación Orientada a Objetos

Semestre: II Semestre - 2026

Universidad Católica del Norte

Descripción del Proyecto
Este proyecto es un sistema de consola desarrollado en Java estructurado (sin uso de POO ni colecciones dinámicas) diseñado para administrar el acceso a un grupo de estudio. El sistema cruza automáticamente un archivo de alumnos oficialmente inscritos (Alumnos.txt) con un archivo de intentos de ingreso (Solicitudes.txt), permitiendo además la inscripción manual, administración de paralelos, generación de reportes físicos (en una carpeta Reportes/) y cálculo de estadísticas en tiempo real.

Requisitos Previos
Java Development Kit (JDK): Versión 21 (o superior).

Archivos de entrada: Los archivos Alumnos.txt y Solicitudes.txt deben estar ubicados en la raíz del proyecto (mismo nivel que la carpeta src o donde se ejecute el programa) siguiendo el formato establecido con separadores ; y - respectivamente.

Instrucciones de Ejecución (Clonación y Testeo)
Para clonar y ejecutar este proyecto en tu entorno local, sigue estos pasos desde la terminal de comandos:

Clonar el repositorio:

Bash
git clone [ENLACE_DE_TU_REPOSITORIO_AQUI]
cd [NOMBRE_DE_LA_CARPETA_DEL_REPOSITORIO]
Compilar el código fuente:
Dependiendo de la estructura de tus carpetas (por ejemplo, si el código está dentro de src/Clasestambien/), compila el archivo principal:

Bash
javac src/Clasestambien/Taller1.java
Ejecutar el programa:
Una vez compilado, ejecuta la clase principal:

Bash
java -cp src Clasestambien.Taller1
Uso del Sistema
Al ejecutar el programa, se desplegará el menú principal. Sigue este orden recomendado para testear todas las funcionalidades:

Opción 1 (Cargar archivos): Obligatorio antes de realizar cualquier otra acción. Carga los datos en los vectores estáticos.

Opción 2 (Procesar solicitudes): Filtra automáticamente a los admitidos y rechazados cruzando los datos.

Opción 3 (Inscripción manual): Prueba ingresar a un alumno por su nombre completo o simulando el caso especial ingresando solo un RUT que no existe en la lista.

Opción 4 (Administración): Permite cambiar a un alumno de paralelo (C1 a C2), eliminarlo del curso o agregar un alumno nuevo. Nota: Los cambios modificarán de forma persistente el archivo Alumnos.txt.

Opción 5 (Generar reportes): Crea de forma automática la carpeta Reportes/ (si no existe) y genera archivos .txt versionados (ej. ReporteC1-V1.txt, Rechazados-V1.txt) con el estado actual del grupo.

Opción 6 (Estadísticas): Muestra un análisis del porcentaje de rechazos, distribución por paralelos, tasa de admisión, entre otros.

Opción 7 (Salir): Finaliza de forma segura la ejecución del programa.

Estructura de Archivos Generados
Una vez ejecutada la opción de reportes, el sistema creará automáticamente la siguiente estructura:

Plaintext
Reportes/
├── ReporteC1-V1.txt
├── ReporteC2-V1.txt
└── Rechazados-V1.txt
Cada vez que se vuelva a solicitar un reporte, la versión (V2, V3, etc.) aumentará automáticamente para mantener un historial de los cambios.
