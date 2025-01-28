package com.cyberhospital;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import com.opencsv.exceptions.CsvValidationException;

public class CyberHospital {
    private static List<Doctor> doctores = new ArrayList<>();
    private static List<Paciente> pacientes = new ArrayList<>();
    private static List<Cita> citas = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            CSVUtils.cargarDoctoresCSV(doctores, "doctores.csv");
            CSVUtils.cargarPacientesCSV(pacientes, "pacientes.csv");
            CSVUtils.cargarCitasCSV(citas, doctores, pacientes, "citas.csv");
        } catch (IOException | CsvValidationException e) {
            System.out.println("Error al cargar los datos: " + e.getMessage());
        }
        mostrarMenuPrincipal();
    }

    private static void mostrarMenuPrincipal() {
        while (true) {
            System.out.println("1. Dar de alta doctores");
            System.out.println("2. Dar de alta pacientes");
            System.out.println("3. Crear una cita");
            System.out.println("4. Relacionar una cita con un doctor y un paciente");
            System.out.println("5. Salir");
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
        System.out.println("¿Desea cancelar la operación? (Sí/No)");
        String cancelar = scanner.nextLine();
        if (cancelar.equalsIgnoreCase("Sí")) {
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
        System.out.println("¿Desea cancelar la operación? (Sí/No)");
        String cancelar = scanner.nextLine();
        if (cancelar.equalsIgnoreCase("Sí")) {
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
        System.out.println("¿Desea cancelar la operación? (Sí/No)");
        String cancelar = scanner.nextLine();
        if (cancelar.equalsIgnoreCase("Sí")) {
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
        System.out.println("¿Desea cancelar la operación? (Sí/No)");
        String cancelar = scanner.nextLine();
        if (cancelar.equalsIgnoreCase("Sí")) {
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
            CSVUtils.guardarDoctoresCSV(doctores, "doctores.csv");
            System.out.println("Guardando pacientes...");
            CSVUtils.guardarPacientesCSV(pacientes, "pacientes.csv");
            System.out.println("Guardando citas...");
            CSVUtils.guardarCitasCSV(citas, "citas.csv");
        } catch (IOException e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
        System.out.println("Saliendo del sistema...");
        System.exit(0);
    }
}
