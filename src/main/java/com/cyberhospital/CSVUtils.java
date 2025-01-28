package com.cyberhospital;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class CSVUtils {

    public static void guardarDoctoresCSV(List<Doctor> doctores, String filePath) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            for (Doctor doctor : doctores) {
                String[] data = {doctor.getId(), doctor.getNombreCompleto(), doctor.getEspecialidad()};
                writer.writeNext(data);
            }
        }
    }

    public static void cargarDoctoresCSV(List<Doctor> doctores, String filePath) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                Doctor doctor = new Doctor(line[0], line[1], line[2]);
                doctores.add(doctor);
            }
        }
    }

    public static void guardarPacientesCSV(List<Paciente> pacientes, String filePath) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            for (Paciente paciente : pacientes) {
                String[] data = {paciente.getId(), paciente.getNombreCompleto()};
                writer.writeNext(data);
            }
        }
    }

    public static void cargarPacientesCSV(List<Paciente> pacientes, String filePath) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                Paciente paciente = new Paciente(line[0], line[1]);
                pacientes.add(paciente);
            }
        }
    }

    public static void guardarCitasCSV(List<Cita> citas, String filePath) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            for (Cita cita : citas) {
                String[] data = {cita.getId(), cita.getFechaHora().toString(), cita.getMotivo(), cita.getDoctor().getId(), cita.getPaciente().getId()};
                writer.writeNext(data);
            }
        }
    }

    public static void cargarCitasCSV(List<Cita> citas, List<Doctor> doctores, List<Paciente> pacientes, String filePath) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                final String doctorId = line[3];
                final String pacienteId = line[4];
                Doctor doctor = doctores.stream().filter(d -> d.getId().equals(doctorId)).findFirst().orElse(null);
                Paciente paciente = pacientes.stream().filter(p -> p.getId().equals(pacienteId)).findFirst().orElse(null);
                Cita cita = new Cita(line[0], LocalDateTime.parse(line[1]), line[2], doctor, paciente);
                citas.add(cita);
            }
        }
    }
}
