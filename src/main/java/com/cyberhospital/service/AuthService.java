package com.cyberhospital.service;

import com.cyberhospital.model.Administrador;
import java.util.List;

public class AuthService {
    private List<Administrador> administradores;

    public AuthService(List<Administrador> administradores) {
        this.administradores = administradores;
    }

    public boolean autenticar(String usuario, String password) {
        for (Administrador admin : administradores) {
            if (admin.getUsuario().equals(usuario) && admin.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
