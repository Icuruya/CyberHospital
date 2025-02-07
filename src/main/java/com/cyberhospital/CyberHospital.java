package com.cyberhospital;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;

import com.cyberhospital.model.Cita;
import com.cyberhospital.model.Doctor;
import com.cyberhospital.model.Paciente;
import com.cyberhospital.service.AuthService;
import com.cyberhospital.service.CSVUtils;
import com.opencsv.exceptions.CsvValidationException;
import com.cyberhospital.model.Administrador;

import javax.imageio.stream.FileImageInputStream;


public class CyberHospital {
    private static List<Doctor> doctores = new ArrayList<>();
    private static List<Paciente> pacientes = new ArrayList<>();
    private static List<Cita> citas = new ArrayList<>();
    private static List<Administrador> administradores = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String DB_PATH = "./db/";

    public static void main(String[] args) {

        File directory = new File(DB_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            CSVUtils.cargarDoctoresCSV(doctores, DB_PATH + "doctores.csv");
            CSVUtils.cargarPacientesCSV(pacientes, DB_PATH + "pacientes.csv");
            CSVUtils.cargarCitasCSV(citas, doctores, pacientes, DB_PATH + "citas.csv");
            CSVUtils.cargarAdministradoresCSV(administradores, DB_PATH + "administradores.csv");

        } catch (IOException | CsvValidationException e) {
            System.out.println("Error al cargar los datos: " + e.getMessage());
        }

        AuthService authService = new AuthService(administradores);
        System.out.println("=== Bienvenido al sistema de CyberHospital ===");

        while (true) {
            System.out.println("Ingresa por favor el Usuario: ");
            String usuario = scanner.nextLine();
            System.out.println("Ingresa por favor la contraseña: ");
            String password = scanner.nextLine();

            if (authService.autenticar(usuario, password)) {
                mostrarMenuPrincipal();
        }  else{
            System.out.println("Credenciales invalidas");
            }
        }
    }

    private static void mostrarMenuPrincipal() {
        while (true) {
            System.out.println("1. Dar de alta doctores");
            System.out.println("2. Dar de alta pacientes");
            System.out.println("3. Crear una cita");
            System.out.println("4. Relacionar una cita con un doctor y un paciente");
            System.out.println("5. Crear nuevo Usuario para ingresar al Sistema");
            System.out.println("6. Salir");
            int opcion = Integer.parseInt(scanner.nextLine());
            switch (opcion) {
                case 1:
                    darDeAltaDoctor();
                    break;
                case 2:
                    darDeAltaPaciente();
                    break;
                case 3:
                    crearCita();
                    break;
                case 4:
                    relacionarCita();
                    break;
                case 5:
                    agregarNuevoAdministrador();
                case 6:
                    salir();
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }

    private static void darDeAltaDoctor() {
        System.out.println("Ingrese el identificador del doctor:");
        String id = scanner.nextLine();
        System.out.println("Ingrese el nombre completo del doctor:");
        String nombreCompleto = scanner.nextLine();
        System.out.println("Ingrese la especialidad del doctor:");
        String especialidad = scanner.nextLine();
        System.out.println("¿Desea continuar con la operacion? (Y/N)");
        String cancelar = scanner.nextLine();
        if (cancelar.equalsIgnoreCase("N")) {
            return;
        }
        Doctor doctor = new Doctor(id, nombreCompleto, especialidad);
        doctores.add(doctor);
        System.out.println("Doctor dado de alta exitosamente");
    }

    private static void darDeAltaPaciente() {
        System.out.println("Ingrese el identificador del paciente:");
        String id = scanner.nextLine();
        System.out.println("Ingrese el nombre completo del paciente:");
        String nombreCompleto = scanner.nextLine();
        System.out.println("¿Desea continuar con la operacion? (Y/N)");
        String cancelar = scanner.nextLine();
        if (cancelar.equalsIgnoreCase("N")) {
            return;
        }
        Paciente paciente = new Paciente(id, nombreCompleto);
        pacientes.add(paciente);
        System.out.println("Paciente dado de alta exitosamente");
    }

    private static void crearCita() {
        System.out.println("Ingrese el identificador de la cita:");
        String id = scanner.nextLine();
        System.out.println("Ingrese la fecha y hora de la cita (YYYY-MM-DDTHH:MM):");
        LocalDateTime fechaHora = LocalDateTime.parse(scanner.nextLine());
        System.out.println("Ingrese el motivo de la cita:");
        String motivo = scanner.nextLine();
        System.out.println("Ingrese el identificador del doctor:");
        String idDoctor = scanner.nextLine();
        System.out.println("Ingrese el identificador del paciente:");
        String idPaciente = scanner.nextLine();
        System.out.println("¿Desea continuar con la operacion? (Y/N)");
        String cancelar = scanner.nextLine();
        if (cancelar.equalsIgnoreCase("N")) {
            return;
        }
        Doctor doctor = obtenerDoctor(idDoctor);
        Paciente paciente = obtenerPaciente(idPaciente);
        Cita cita = new Cita(id, fechaHora, motivo, doctor, paciente);
        citas.add(cita);
        System.out.println("Cita creada exitosamente");
    }

    private static void relacionarCita() {
        System.out.println("Ingrese el identificador de la cita:");
        String idCita = scanner.nextLine();
        System.out.println("Ingrese el identificador del doctor:");
        String idDoctor = scanner.nextLine();
        System.out.println("Ingrese el identificador del paciente:");
        String idPaciente = scanner.nextLine();
        System.out.println("¿Desea continuar con la operacion? (Y/N)");
        String cancelar = scanner.nextLine();
        if (cancelar.equalsIgnoreCase("N")) {
            return;
        }
        Cita cita = obtenerCita(idCita);
        Doctor doctor = obtenerDoctor(idDoctor);
        Paciente paciente = obtenerPaciente(idPaciente);
        if (cita != null && doctor != null && paciente != null) {
            cita.setDoctor(doctor);
            cita.setPaciente(paciente);
            System.out.println("Cita relacionada exitosamente");
        } else {
            System.out.println("Error al relacionar la cita");
        }
    }

    private static void agregarNuevoAdministrador() {
        System.out.println("Ingrese el nombre de Usuario: ");
        String usuario = scanner.nextLine();
        System.out.println("Ingrese la contraseña: ");
        String password = scanner.nextLine();
        System.out.println("Deseas continuar con la operacion? (Y/N)");
        String cancelar = scanner.nextLine();
        if (cancelar.equalsIgnoreCase("N")) {
            return;
        }
        Administrador administrador = new Administrador(usuario, password);
        administradores.add(administrador);
        System.out.println("Administrador agregado exitosamente");
    }
    private static Cita obtenerCita(String id) {
        for (Cita cita : citas) {
            if (cita.getId().equals(id)) {
                return cita;
            }
        }
        return null;
    }

    private static Doctor obtenerDoctor(String id) {
        for (Doctor doctor : doctores) {
            if (doctor.getId().equals(id)) {
                return doctor;
            }
        }
        return null;
    }

    private static Paciente obtenerPaciente(String id) {
        for (Paciente paciente : pacientes) {
            if (paciente.getId().equals(id)) {
                return paciente;
            }
        }
        return null;
    }

    private static void salir() {
        try {
            System.out.println("Guardando doctores...");
            CSVUtils.guardarDoctoresCSV(doctores, DB_PATH + "doctores.csv");
            System.out.println("Guardando pacientes...");
            CSVUtils.guardarPacientesCSV(pacientes, DB_PATH + "pacientes.csv");
            System.out.println("Guardando citas...");
            CSVUtils.guardarCitasCSV(citas, DB_PATH + "citas.csv");
            System.out.println("Guardando usuarios...");
            CSVUtils.guardarAdministradoresCSV(administradores, DB_PATH + "administradores.csv");
        } catch (IOException e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
        System.out.println("Saliendo del sistema...");
        System.exit(0);
    }
}
