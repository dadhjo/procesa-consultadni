package com.nuviosoftware.ibmjmsclient.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Slf4j
@Component
public class OrderResponseListener {

    @JmsListener(destination = "LOCAL.REPLY")
    public void receive(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        log.info("### 2 ### Consulta DNI recibe respuesta de RENIEC: {} para el documento: {}",
                textMessage.getText(), textMessage.getJMSCorrelationID());

        // do some business logic here, like updating the order in the database
    }
}
