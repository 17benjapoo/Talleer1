// Benjamin Araya Trigo - 22.250.820-7 - ICCI
package Taller1;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class Taller1 {

    static  int maximo = 100;
    static String[] nombres = new String[maximo];
    static String[] apellidos = new String[maximo];
    static String[] ruts = new String[maximo];
    static String[] paralelos = new String[maximo];
    static int cantidadAlumnos = 0;

    
    static String[] solicitudNombres = new String[maximo];
    static String[] solicitudApellidos = new String[maximo];
    static boolean[] solicitudProcesada = new boolean[maximo];
    static int cantidadSolicitudes = 0;

    static String[] miembroNombres = new String[maximo];
    static String[] miembroApellidos = new String[maximo];
    static String[] miembroRuts = new String[maximo];
    static String[] miembroParalelos = new String[maximo];
    static int cantidadMiembros = 0;

    static String[] rechazados = new String[maximo];
    static int cantidadRechazados = 0;

    
    static boolean archivosCargados = false;

   
    static int totalIntentos = 0;
    static int totalRechazados = 0;
    static int totalAdmitidosArchivo = 0;
    static int totalAdmitidosManual = 0;


    static int versionC1 = 0;
    static int versionC2 = 0;
    static int versionRechazados = 0;


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int opcion;

        do {

            mostrarMenu();

            opcion = leerEntero(scanner);

            if (opcion == 1) {
                cargarArchivos();
            }
            else if (opcion == 2) {
                procesarSolicitudes();
            }
            else if (opcion == 3) {
                inscripcionManual(scanner);
            }
            else if (opcion == 4) {
                administracionCurso(scanner);
            }
            else if (opcion == 5) {
                generarReportes(scanner);
            }
            else if (opcion == 6) {
                mostrarEstadisticas();
            }
            else if (opcion == 7) {
                System.out.println("Programa finalizado.");
            }
            else {
                System.out.println("Opcion invalida.");
            }

        } while (opcion != 7);

        scanner.close();
    }


    public static void mostrarMenu() {

        System.out.println();
        System.out.println("===== Sistema de Control del Grupo POO =====");
        System.out.println("1) Cargar archivos (Alumnos y Solicitudes)");
        System.out.println("2) Procesar solicitudes (Filtrado automatico)");
        System.out.println("3) Inscripcion manual al grupo");
        System.out.println("4) Administracion del curso");
        System.out.println("5) Generar reportes");
        System.out.println("6) Analisis estadistico");
        System.out.println("7) Salir");
        System.out.print("Ingrese opcion: ");
    }


    public static int leerEntero(Scanner scanner) {

        if (scanner.hasNextInt()) {

            int numero = scanner.nextInt();
            scanner.nextLine();

            return numero;

        } else {

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            return -1;
        }
    }


    public static void cargarArchivos() {

        // Reiniciamos los datos
        cantidadAlumnos = 0;
        cantidadSolicitudes = 0;

        for (int i = 0; i < maximo; i++) {
            solicitudProcesada[i] = false;
        }

        boolean alumnosCorrectos = cargarAlumnos();
        boolean solicitudesCorrectas = cargarSolicitudes();

        if (alumnosCorrectos && solicitudesCorrectas) {

            archivosCargados = true;

            System.out.println("Archivos cargados con exito!");
            System.out.println("- " + cantidadAlumnos + " alumnos en la lista.");
            System.out.println("- " + cantidadSolicitudes + " solicitudes de ingreso.");

        } else {

            archivosCargados = false;
            System.out.println("No fue posible cargar correctamente los archivos.");
        }
    }


    public static boolean cargarAlumnos() {

        try {

            File archivo = new File("Alumnos.txt");
            Scanner lector = new Scanner(archivo);

            while (lector.hasNextLine()) {

                String linea = lector.nextLine();

                if (cantidadAlumnos >= maximo) {

                    System.out.println("Se alcanzo la capacidad maxima de alumnos.");
                    break;
                }

                String[] partes = linea.split(";");

                if (partes.length == 4) {

                    String nombre = partes[0];
                    String apellido = partes[1];
                    String rut = partes[2];
                    String paralelo = partes[3];

                    if (!nombre.equals("")
                            && !apellido.equals("")
                            && !rut.equals("")
                            && (paralelo.equalsIgnoreCase("C1")
                            || paralelo.equalsIgnoreCase("C2"))) {

                        if (buscarAlumnoPorRut(rut) == -1) {

                            nombres[cantidadAlumnos] = nombre;
                            apellidos[cantidadAlumnos] = apellido;
                            ruts[cantidadAlumnos] = rut;
                            paralelos[cantidadAlumnos] = paralelo.toUpperCase();

                            cantidadAlumnos++;
                        }
                    }
                }
            }

            lector.close();

            return true;

        } catch (IOException e) {

            System.out.println("No se pudo abrir Alumnos.txt.");
            return false;
        }
    }


    public static boolean cargarSolicitudes() {

        try {

            File archivo = new File("Solicitudes.txt");
            Scanner lector = new Scanner(archivo);

            while (lector.hasNextLine()) {

                String linea = lector.nextLine();

                if (cantidadSolicitudes >= maximo) {

                    System.out.println("Se alcanzo la capacidad maxima de solicitudes.");
                    break;
                }

                String[] partes = linea.split("-");

                if (partes.length == 2) {

                    String nombre = partes[0];
                    String apellido = partes[1];

                    if (!nombre.equals("") && !apellido.equals("")) {

                        solicitudNombres[cantidadSolicitudes] = nombre;
                        solicitudApellidos[cantidadSolicitudes] = apellido;
                        solicitudProcesada[cantidadSolicitudes] = false;

                        cantidadSolicitudes++;
                    }
                }
            }

            lector.close();

            return true;

        } catch (IOException e) {

            System.out.println("No se pudo abrir Solicitudes.txt.");
            return false;
        }
    }


    public static void procesarSolicitudes() {

        if (!archivosCargados) {

            System.out.println("Primero debe cargar los archivos.");
            return;
        }

        System.out.println();
        System.out.println("Procesando solicitudes...");

        int admitidos = 0;
        int rechazados = 0;

        for (int i = 0; i < cantidadSolicitudes; i++) {

            if (!solicitudProcesada[i]) {

                solicitudProcesada[i] = true;
                totalIntentos++;

                int posicion = buscarAlumnoPorNombre(
                        solicitudNombres[i],
                        solicitudApellidos[i]
                );

                if (posicion != -1) {

                    boolean yaEsMiembro = buscarMiembroPorRut(ruts[posicion]) != -1;

                    if (!yaEsMiembro) {

                        if (cantidadMiembros < maximo) {

                            miembroNombres[cantidadMiembros] = nombres[posicion];
                            miembroApellidos[cantidadMiembros] = apellidos[posicion];
                            miembroRuts[cantidadMiembros] = ruts[posicion];
                            miembroParalelos[cantidadMiembros] = paralelos[posicion];

                            cantidadMiembros++;

                            totalAdmitidosArchivo++;
                            admitidos++;

                            System.out.println(
                                    "[OK]       " + nombres[posicion] + " " + apellidos[posicion] + " -> admitido en " + paralelos[posicion]
                            );

                        } else {

                            System.out.println(
                                    "No hay espacio para agregar mas miembros."
                            );
                        }

                    } else {

                        System.out.println(
                                "[DUPLICADO] "+ solicitudNombres[i] + " " + solicitudApellidos[i]+ " ya pertenece al grupo."
                        );
                    }

                } else {

                    String texto = solicitudNombres[i]
                            + " "
                            + solicitudApellidos[i]
                            + " - No pertenece a ningun paralelo del curso";

                    agregarRechazado(texto);

                    totalRechazados++;
                    rechazados++;

                    System.out.println(
                            "[RECHAZO]  "+ solicitudNombres[i] + " "+ solicitudApellidos[i]+ " -> no pertenece a ningun paralelo"
                    );
                }
            }
        }

        System.out.println();
        System.out.println(
                "Resumen: "
                + admitidos
                + " admitidos / "
                + rechazados
                + " rechazados."
        );
    }


    public static void inscripcionManual(Scanner scanner) {

        if (!archivosCargados) {

            System.out.println("Primero debe cargar los archivos.");
            return;
        }

        System.out.println();
        System.out.println("Como desea inscribir a la persona?");
        System.out.println("1) Por nombre completo");
        System.out.println("2) Por RUT");
        System.out.print("Ingrese opcion: ");

        int opcion = leerEntero(scanner);

        if (opcion == 1) {

            System.out.print("Ingrese nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Ingrese apellido: ");
            String apellido = scanner.nextLine();

            totalIntentos++;

            if (nombre.equals("") || apellido.equals("")) {

                System.out.println("El nombre y apellido no pueden estar vacios.");

                agregarRechazado(
                        nombre + " " + apellido
                        + " - Datos incompletos"
                );

                totalRechazados++;

                return;
            }

            int posicion = buscarAlumnoPorNombre(nombre, apellido);

            if (posicion != -1) {

                agregarMiembro(posicion, true);

            } else {

                System.out.println(
                        "La persona no pertenece a ningun paralelo del curso."
                );

                agregarRechazado(
                        nombre + " " + apellido + " - No pertenece a ningun paralelo del curso"
                );

                totalRechazados++;
            }

        }
        else if (opcion == 2) {

            System.out.print("Ingrese RUT: ");
            String rut = scanner.nextLine();

            totalIntentos++;

            if (rut.equals("")) {

                System.out.println("El RUT no puede estar vacio.");

                agregarRechazado(
                        "Sin nombre registrado, RUT: "
                        + rut
                );

                totalRechazados++;

                return;
            }

            int posicion = buscarAlumnoPorRut(rut);

            if (posicion != -1) {

                agregarMiembro(posicion, true);

            } else {

                System.out.println(
                        "El RUT " + rut+ " no pertenece a ningun paralelo del curso."
                );

                System.out.println(
                        "No tenemos su nombre, por lo que se registrara "+ "solo el RUT en los rechazados."
                );

                agregarRechazado(
                        "Sin nombre registrado, RUT: " + rut
                );

                totalRechazados++;
            }

        }
        else {

            System.out.println("Opcion invalida.");
        }
    }


    public static void agregarMiembro(int posicion, boolean manual) {

        boolean yaEsMiembro = buscarMiembroPorRut(ruts[posicion]) != -1;

        if (yaEsMiembro) {

            System.out.println(
                    "La persona ya pertenece al grupo."
            );

            return;
        }

        if (cantidadMiembros >= maximo) {

            System.out.println(
                    "No hay espacio disponible para nuevos miembros."
            );

            return;
        }

        miembroNombres[cantidadMiembros] = nombres[posicion];
        miembroApellidos[cantidadMiembros] = apellidos[posicion];
        miembroRuts[cantidadMiembros] = ruts[posicion];
        miembroParalelos[cantidadMiembros] = paralelos[posicion];

        cantidadMiembros++;

        if (manual) {
            totalAdmitidosManual++;
        }

        System.out.println(
                "[OK] "
                + nombres[posicion] + " "
                + apellidos[posicion]
                + " -> admitido en "
                + paralelos[posicion]
        );
    }


    public static void administracionCurso(Scanner scanner) {

        if (!archivosCargados) {

            System.out.println("Primero debe cargar los archivos.");
            return;
        }

        int opcion;

        do {

            System.out.println();
            System.out.println("--- Administracion del curso ---");
            System.out.println("1) Cambiar paralelo de un alumno");
            System.out.println("2) Eliminar alumno del curso");
            System.out.println("3) Inscribir alumno nuevo");
            System.out.println("4) Volver");
            System.out.print("Ingrese opcion: ");

            opcion = leerEntero(scanner);

            if (opcion == 1) {

                cambiarParalelo(scanner);

            }
            else if (opcion == 2) {

                eliminarAlumno(scanner);

            }
            else if (opcion == 3) {

                inscribirAlumnoNuevo(scanner);

            }
            else if (opcion == 4) {

                System.out.println("Volviendo...");

            }
            else {

                System.out.println("Opcion invalida.");
            }

        } while (opcion != 4);
    }


    public static void cambiarParalelo(Scanner scanner) {

        System.out.print("Ingrese RUT del alumno: ");
        String rut = scanner.nextLine();

        int posicion = buscarAlumnoPorRut(rut);

        if (posicion == -1) {

            System.out.println(
                    "No existe un alumno con ese RUT."
            );

            return;
        }

        System.out.println(
                "Alumno: "
                + nombres[posicion]
                + " "
                + apellidos[posicion]
                + " (actualmente en "
                + paralelos[posicion]
                + ")"
        );

        System.out.print("Nuevo paralelo (C1/C2): ");
        String nuevoParalelo = scanner.nextLine();

        if (!nuevoParalelo.equalsIgnoreCase("C1")
                && !nuevoParalelo.equalsIgnoreCase("C2")) {

            System.out.println(
                    "El paralelo debe ser C1 o C2."
            );

            return;
        }

        paralelos[posicion] = nuevoParalelo.toUpperCase();

        int posicionMiembro = buscarMiembroPorRut(rut);

        if (posicionMiembro != -1) {

            miembroParalelos[posicionMiembro] =
                    nuevoParalelo.toUpperCase();
        }

        if (guardarAlumnos()) {

            System.out.println(
                    "Paralelo actualizado! "+ "Cambios guardados en Alumnos.txt"
            );

        } else {

            System.out.println(
                    "El cambio se hizo en memoria, "+ "pero no pudo guardarse en el archivo."
            );
        }
    }


    public static void eliminarAlumno(Scanner scanner) {

        System.out.print("Ingrese RUT del alumno: ");
        String rut = scanner.nextLine();

        int posicion = buscarAlumnoPorRut(rut);

        if (posicion == -1) {

            System.out.println(
                    "No existe un alumno con ese RUT."
            );

            return;
        }

        String nombre = nombres[posicion];
        String apellido = apellidos[posicion];

        
        for (int i = posicion; i < cantidadAlumnos - 1; i++) {

            nombres[i] = nombres[i + 1];
            apellidos[i] = apellidos[i + 1];
            ruts[i] = ruts[i + 1];
            paralelos[i] = paralelos[i + 1];
        }

        cantidadAlumnos--;

        nombres[cantidadAlumnos] = "";
        apellidos[cantidadAlumnos] = "";
        ruts[cantidadAlumnos] = "";
        paralelos[cantidadAlumnos] = "";

        
        eliminarMiembroPorRut(rut);

        if (guardarAlumnos()) {

            System.out.println(
                    "Alumno " + nombre + " " + apellido+ " eliminado del curso."
            );

            System.out.println(
                    "Cambios guardados en Alumnos.txt"
            );

        } else {

            System.out.println(
                    "El alumno fue eliminado en memoria, " + "pero no pudo guardarse el archivo."
            );
        }
    }


    public static void eliminarMiembroPorRut(String rut) {

        int posicion = buscarMiembroPorRut(rut);

        if (posicion == -1) {
            return;
        }

        for (int i = posicion; i < cantidadMiembros - 1; i++) {

            miembroNombres[i] = miembroNombres[i + 1];
            miembroApellidos[i] = miembroApellidos[i + 1];
            miembroRuts[i] = miembroRuts[i + 1];
            miembroParalelos[i] = miembroParalelos[i + 1];
        }

        cantidadMiembros--;

        miembroNombres[cantidadMiembros] = "";
        miembroApellidos[cantidadMiembros] = "";
        miembroRuts[cantidadMiembros] = "";
        miembroParalelos[cantidadMiembros] = "";
    }


    public static void inscribirAlumnoNuevo(Scanner scanner) {

        if (cantidadAlumnos >= maximo) {

            System.out.println(
                    "No hay espacio disponible para nuevos alumnos."
            );

            return;
        }

        System.out.print("Ingrese nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Ingrese RUT: ");
        String rut = scanner.nextLine();

        System.out.print("Ingrese paralelo (C1/C2): ");
        String paralelo = scanner.nextLine();

        if (nombre.equals("")
                || apellido.equals("")
                || rut.equals("")
                || paralelo.equals("")) {

            System.out.println(
                    "Ningun campo puede quedar vacio."
            );

            return;
        }

        if (!paralelo.equalsIgnoreCase("C1")
                && !paralelo.equalsIgnoreCase("C2")) {

            System.out.println(
                    "El paralelo debe ser C1 o C2."
            );

            return;
        }

        if (buscarAlumnoPorRut(rut) != -1) {

            System.out.println(
                    "Ya existe un alumno con ese RUT."
            );

            return;
        }

        nombres[cantidadAlumnos] = nombre;
        apellidos[cantidadAlumnos] = apellido;
        ruts[cantidadAlumnos] = rut;
        paralelos[cantidadAlumnos] = paralelo.toUpperCase();

        cantidadAlumnos++;

        if (guardarAlumnos()) {

            System.out.println(
                    "Alumno inscrito correctamente."
            );

            System.out.println(
                    "El alumno NO entra automaticamente al grupo."
            );

        } else {

            System.out.println(
                    "El alumno fue agregado en memoria, "+ "pero no pudo guardarse el archivo."
            );
        }
    }


    public static boolean guardarAlumnos() {

        try {

            FileWriter escritorArchivo =
                    new FileWriter("Alumnos.txt");

            BufferedWriter escritor =
                    new BufferedWriter(escritorArchivo);

            for (int i = 0; i < cantidadAlumnos; i++) {

                escritor.write(
                        nombres[i]+ ";"+ apellidos[i]+ ";"+ ruts[i]+ ";"+ paralelos[i]
                );

                escritor.newLine();
            }

            escritor.close();

            return true;

        } catch (IOException e) {

            System.out.println(
                    "No se pudo escribir Alumnos.txt."
            );

            return false;
        }
    }


    public static void generarReportes(Scanner scanner) {

        if (!archivosCargados) {

            System.out.println(
                    "Primero debe cargar los archivos."
            );

            return;
        }

        int opcion;

        do {

            System.out.println();
            System.out.println("--- Generar reportes ---");
            System.out.println("1) Reporte Paralelo C1");
            System.out.println("2) Reporte Paralelo C2");
            System.out.println("3) Reporte de rechazados");
            System.out.println("4) Volver");
            System.out.print("Ingrese opcion: ");

            opcion = leerEntero(scanner);

            if (opcion == 1) {

                generarReporteParalelo("C1");

            }
            else if (opcion == 2) {

                generarReporteParalelo("C2");

            }
            else if (opcion == 3) {

                generarReporteRechazados();

            }
            else if (opcion == 4) {

                System.out.println("Volviendo...");

            }
            else {

                System.out.println("Opcion invalida.");
            }

        } while (opcion != 4);
    }


    public static void generarReporteParalelo(String paralelo) {

        int version;

        if (paralelo.equals("C1")) {

            versionC1++;
            version = versionC1;

        } else {

            versionC2++;
            version = versionC2;
        }

        String nombreArchivo =
                "Reporte" + paralelo + "-V" + version + ".txt";

        try {

            File carpeta = new File("Reportes");

            carpeta.mkdir();

            FileWriter escritorArchivo =
                    new FileWriter(
                            "Reportes/" + nombreArchivo
                    );

            BufferedWriter escritor =
                    new BufferedWriter(escritorArchivo);

            escritor.write(
                    "=== Miembros del grupo - Paralelo "
                    + paralelo
                    + " ==="
            );

            escritor.newLine();

            for (int i = 0; i < cantidadMiembros; i++) {

                if (miembroParalelos[i].equals(paralelo)) {

                    escritor.write(
                            miembroNombres[i]+ " "+ miembroApellidos[i]+ " - "+ miembroRuts[i]
                    );

                    escritor.newLine();
                }
            }

            escritor.close();

            System.out.println(
                    "Reporte generado: "+ "Reportes/"+ nombreArchivo
            );

        } catch (IOException e) {

            System.out.println(
                    "No se pudo generar el reporte."
            );
        }
    }


    public static void generarReporteRechazados() {

        versionRechazados++;

        String nombreArchivo =
                "Rechazados-V"
                + versionRechazados
                + ".txt";

        try {

            File carpeta = new File("Reportes");

            carpeta.mkdir();

            FileWriter escritorArchivo =
                    new FileWriter(
                            "Reportes/" + nombreArchivo
                    );

            BufferedWriter escritor =
                    new BufferedWriter(escritorArchivo);

            escritor.write("=== Solicitudes rechazadas ===");
            escritor.newLine();

            for (int i = 0; i < cantidadRechazados; i++) {

                escritor.write(rechazados[i]);
                escritor.newLine();
            }

            escritor.close();

            System.out.println(
                    "Reporte generado: "+ "Reportes/"+ nombreArchivo
            );

        } catch (IOException e) {

            System.out.println(
                    "No se pudo generar el reporte."
            );
        }
    }


    public static void mostrarEstadisticas() {

        if (!archivosCargados) {

            System.out.println(
                    "Primero debe cargar los archivos."
            );

            return;
        }

        System.out.println();
        System.out.println("--- Analisis estadistico ---");

        System.out.println(
                "Total de intentos de ingreso: "
                + totalIntentos
        );

        System.out.println(
                "Rechazados: "+ totalRechazados+ " (" + calcularPorcentaje(totalRechazados,totalIntentos)+ "%)"
        );

        int alumnosC1 = contarAlumnosPorParalelo("C1");
        int alumnosC2 = contarAlumnosPorParalelo("C2");

        System.out.println(
                "Alumnos por paralelo -> C1: "+ alumnosC1+ " | C2: "+ alumnosC2
        );

        System.out.println(
                "Porcentaje C1: "+ calcularPorcentaje(alumnosC1,cantidadAlumnos)+ "%"
        );

        System.out.println(
                "Porcentaje C2: "+ calcularPorcentaje(alumnosC2,cantidadAlumnos)+ "%"
        );

        System.out.println(
                "Admitidos por paralelo -> C1: "+ contarMiembrosPorParalelo("C1")+ " | C2: "+ contarMiembrosPorParalelo("C2")
        );

        System.out.println(
                "Tasa de admision: "+ calcularPorcentaje(cantidadMiembros,totalIntentos)+ "%"
        );

        System.out.println(
                "Inscripciones admitidas por archivo: "+ totalAdmitidosArchivo
        );

        System.out.println(
                "Inscripciones admitidas manualmente: "+ totalAdmitidosManual
        );
    }


    public static double calcularPorcentaje(
            int cantidad,
            int total) {

        if (total == 0) {
            return 0;
        }

        return cantidad * 100.0 / total;
    }


    public static int contarAlumnosPorParalelo(
            String paralelo) {

        int cantidad = 0;

        for (int i = 0; i < cantidadAlumnos; i++) {

            if (paralelos[i].equals(paralelo)) {
                cantidad++;
            }
        }

        return cantidad;
    }


    public static int contarMiembrosPorParalelo(
            String paralelo) {

        int cantidad = 0;

        for (int i = 0; i < cantidadMiembros; i++) {

            if (miembroParalelos[i].equals(paralelo)) {
                cantidad++;
            }
        }

        return cantidad;
    }


    public static int buscarAlumnoPorNombre(
            String nombre,
            String apellido) {

        for (int i = 0; i < cantidadAlumnos; i++) {

            if (nombres[i].equalsIgnoreCase(nombre)
                    && apellidos[i].equalsIgnoreCase(apellido)) {

                return i;
            }
        }

        return -1;
    }


    public static int buscarAlumnoPorRut(String rut) {

        for (int i = 0; i < cantidadAlumnos; i++) {

            if (ruts[i].equalsIgnoreCase(rut)) {

                return i;
            }
        }

        return -1;
    }


    public static int buscarMiembroPorRut(String rut) {

        for (int i = 0; i < cantidadMiembros; i++) {

            if (miembroRuts[i].equalsIgnoreCase(rut)) {

                return i;
            }
        }

        return -1;
    }


    public static void agregarRechazado(String texto) {

        if (cantidadRechazados >= maximo) {

            System.out.println(
                    "No hay espacio para guardar mas rechazados."
            );

            return;
        }

        rechazados[cantidadRechazados] = texto;
        cantidadRechazados++;
    }
}