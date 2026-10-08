package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service;

public interface EmailService {

    void enviarEmailActivacion(String destinatario, String token);
}