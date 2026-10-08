package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.impl;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void enviarEmailActivacion(
            String destinatario,
            String token
    ) {

        String enlaceActivacion =
                "http://localhost:8080/api/v1/clientes/activar?token="
                        + token;

        MimeMessage mensaje = mailSender.createMimeMessage();

        try {

            MimeMessageHelper helper =
                    new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject("Bienvenido al sistema bancario");

            String html = """
                    <!DOCTYPE html>
                    <html>
                    <body>
                        <h1>¡Bienvenido!</h1>

                        <p>
                            Su cuenta ha sido creada correctamente.
                        </p>

                        <p>
                            Para activar su cuenta haga clic en el siguiente botón:
                        </p>

                        <p>
                            <a href="%s"
                               style="
                                   display:inline-block;
                                   padding:12px 20px;
                                   background-color:#007bff;
                                   color:white;
                                   text-decoration:none;
                                   border-radius:5px;
                               ">
                                Activar cuenta
                            </a>
                        </p>

                        <p>
                            El enlace de activación es válido durante 24 horas.
                        </p>
                    </body>
                    </html>
                    """.formatted(enlaceActivacion);

            helper.setText(html, true);

            mailSender.send(mensaje);

        } catch (MessagingException e) {

            throw new IllegalStateException(
                    "No se pudo enviar el email de activación",
                    e
            );
        }
    }
}