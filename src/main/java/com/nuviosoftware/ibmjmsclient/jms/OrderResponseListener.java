package com.nuviosoftware.ibmjmsclient.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import com.ibm.mq.jms.MQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class OrderResponseListener {

    @JmsListener(destination = "LOCAL.SEND")
    public void receive(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        log.info("### 2 ### Se recibe Consulta DNI con el mensaje: {} para el documento: {}",
                textMessage.getText(), textMessage.getJMSCorrelationID());

        // do some business logic here, like updating the order in the database
        response(textMessage.getJMSCorrelationID());
    }

    
    @Autowired
    private JmsTemplate jmsTemplate;
    
    public void response(String dni) throws JMSException {
        String queueName = "LOCAL.REPLY";
        MQQueue orderRequestQueue = new MQQueue(queueName);

        jmsTemplate.convertAndSend(orderRequestQueue, "Se ha procesado la consulta DNI para el documento: "+dni, textMessage -> {
            textMessage.setJMSCorrelationID(dni);
            return textMessage;
        });
        
        log.info("### 1 ### Confirmacion de Consulta DNI {} enviada al usuario vía la queue "+queueName, dni);
    }
}
