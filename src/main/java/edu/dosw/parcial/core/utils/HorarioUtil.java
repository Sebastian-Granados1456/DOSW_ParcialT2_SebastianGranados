package edu.dosw.parcial.core.utils;

import java.time.DayOfWeek;
import java.time.LocalTime;

public final class HorarioUtil {

    private HorarioUtil() {
    }

    public static boolean esDiaDisponible(DayOfWeek dia, String diasDisponible) {
        if (dia == null || diasDisponible == null || diasDisponible.isBlank()) {
            return false;
        }
        String texto = normalizar(diasDisponible);
        if (texto.contains("lunes a domingo") || texto.contains("todos los dias") || texto.equals("todos")) {
            return true;
        }
        if (texto.contains("lunes a sabado")) {
            return dia.getValue() >= DayOfWeek.MONDAY.getValue() && dia.getValue() <= DayOfWeek.SATURDAY.getValue();
        }
        if (texto.contains("lunes a viernes")) {
            return dia.getValue() >= DayOfWeek.MONDAY.getValue() && dia.getValue() <= DayOfWeek.FRIDAY.getValue();
        }
        if (texto.contains("lunes a jueves")) {
            return dia.getValue() >= DayOfWeek.MONDAY.getValue() && dia.getValue() <= DayOfWeek.THURSDAY.getValue();
        }
        if (texto.contains("martes a sabado")) {
            return dia.getValue() >= DayOfWeek.TUESDAY.getValue() && dia.getValue() <= DayOfWeek.SATURDAY.getValue();
        }
        if ((texto.contains("fin de semana") || texto.contains("fines de semana"))
                && (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY)) {
            return true;
        }
        return switch (dia) {
            case MONDAY -> texto.contains("lunes") || texto.contains("monday");
            case TUESDAY -> texto.contains("martes") || texto.contains("tuesday");
            case WEDNESDAY -> texto.contains("miercoles") || texto.contains("wednesday");
            case THURSDAY -> texto.contains("jueves") || texto.contains("thursday");
            case FRIDAY -> texto.contains("viernes") || texto.contains("friday");
            case SATURDAY -> texto.contains("sabado") || texto.contains("saturday");
            case SUNDAY -> texto.contains("domingo") || texto.contains("sunday");
        };
    }

    public static boolean esHoraDisponible(LocalTime hora, String horario) {
        if (hora == null || horario == null || horario.isBlank()) {
            return false;
        }
        try {
            String[] partes = horario.split("(?i)\\s*(-|\\ba\\b|\\bto\\b)\\s*");
            if (partes.length < 2) {
                return false;
            }
            LocalTime inicio = parsearHora(partes[0].trim());
            LocalTime fin = parsearHora(partes[1].trim());
            return !hora.isBefore(inicio) && !hora.isAfter(fin);
        } catch (Exception e) {
            return false;
        }
    }

    public static LocalTime parsearHora(String valor) {
        String texto = valor.trim().toUpperCase();
        boolean esPm = texto.contains("PM");
        boolean esAm = texto.contains("AM");
        String limpio = texto.replace("AM", "").replace("PM", "").trim();
        String[] partes = limpio.split(":");
        int horas = Integer.parseInt(partes[0].trim());
        int minutos = partes.length > 1 ? Integer.parseInt(partes[1].trim()) : 0;
        if (esPm && horas < 12) {
            horas += 12;
        }
        if (esAm && horas == 12) {
            horas = 0;
        }
        return LocalTime.of(horas, minutos);
    }

    public static String normalizar(String input) {
        if (input == null) {
            return "";
        }
        return input.toLowerCase()
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .trim();
    }
}
