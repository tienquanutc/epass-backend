package utils

import groovy.text.SimpleTemplateEngine
import model.User

import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.util.concurrent.CompletableFuture

public class Gmail {
    static resetPasswordBrokerUri
    static {
        resetPasswordBrokerUri = System.properties.get('resetpassword.broker.uri')
    }

    static void send(List<String> to, String subject, String body, boolean html = false) {
        Properties props = System.properties
        String host = props.getProperty('mail.smtp.host')
        String from = props.getProperty('mail.from.username')
        String pass = props.getProperty('mail.from.password')
        Session session = Session.getDefaultInstance(props)
        MimeMessage message = new MimeMessage(session)

        message.setFrom(new InternetAddress(from))
        List<InternetAddress> toAddress = new ArrayList<>(to.size())
        to.eachWithIndex { it, i ->
            toAddress[i] = new InternetAddress(it)
        }
        toAddress.each { message.addRecipients(Message.RecipientType.TO, it) }
        message.setSubject(subject)
        if (html)
            message.setContent(body, "text/html; charset=utf-8")
        else
            message.setText(body)
        Transport transport = session.getTransport('smtp')
        transport.connect(host, from, pass)
        transport.sendMessage(message, message.getAllRecipients())
        transport.close()
    }

    static void send(String to, String subject, String body, boolean html = false) {
        send([to], subject, body, html)
    }

    static CompletableFuture<Void> sendRecoveryMailAsync(User user, String token) {
        return CompletableFuture.supplyAsync {
            return new File(Gmail.classLoader.getResource('reset-password.html').toURI())
        }.thenApply { file ->
            def binding = [
                    user             : user,
                    resetpassword_uri: System.getProperty('resetpassword.broker.uri'),
                    token            : token
            ]
            return new SimpleTemplateEngine().createTemplate(file).make(binding).toString()
        }.thenAccept { body ->
            def to = [user.email]
            CompletableFuture.runAsync { send(to, 'EPassTeam recovery email', body, true) }
        }
    }
}
