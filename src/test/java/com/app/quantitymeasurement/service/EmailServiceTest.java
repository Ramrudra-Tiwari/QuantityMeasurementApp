package com.app.quantitymeasurement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Tests for EmailService
@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private static final String FROM = "noreply@qmapp.com";
    private static final String TO = "user@example.com";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "fromAddress", FROM);
    }

    // ===================== REGISTRATION =====================

    @Test
    void testSendRegistrationEmail() {
        emailService.sendRegistrationEmail(TO, "Alice");

        ArgumentCaptor<SimpleMailMessage> captor =
            ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender).send(captor.capture());

        SimpleMailMessage msg = captor.getValue();
        assertArrayEquals(new String[]{TO}, msg.getTo());
        assertEquals(FROM, msg.getFrom());
        assertTrue(msg.getSubject().toLowerCase().contains("welcome"));
    }

//    @Test
//    void testSendRegistrationEmail_SmtpFailure() {
//        doThrow(new MailSendException("fail"))
//            .when(mailSender).send(any());
//
//        assertDoesNotThrow(() ->
//            emailService.sendRegistrationEmail(TO, "Alice"));
//    }

    // ===================== LOGIN =====================

    @Test
    void testSendLoginNotificationEmail() {
        emailService.sendLoginNotificationEmail(TO);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    // ===================== FORGOT =====================

    @Test
    void testSendForgotPasswordEmail() {
        emailService.sendForgotPasswordEmail(TO);

        ArgumentCaptor<SimpleMailMessage> captor =
            ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender).send(captor.capture());

        assertArrayEquals(new String[]{TO}, captor.getValue().getTo());
        assertEquals(FROM, captor.getValue().getFrom());
    }

    // ===================== RESET =====================

    @Test
    void testSendPasswordResetEmail() {
        emailService.sendPasswordResetEmail(TO);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    // ===================== COMMON =====================

    @Test
    void testAllEmailsUseFromAddress() {
        emailService.sendRegistrationEmail(TO, "A");
        emailService.sendLoginNotificationEmail(TO);
        emailService.sendForgotPasswordEmail(TO);
        emailService.sendPasswordResetEmail(TO);

        ArgumentCaptor<SimpleMailMessage> captor =
            ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender, times(4)).send(captor.capture());

        captor.getAllValues().forEach(msg ->
            assertEquals(FROM, msg.getFrom()));
    }
}