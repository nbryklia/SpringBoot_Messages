package ru.messages.Reader;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Service;
import javax.jms.*;
import java.io.*;
import static java.lang.System.exit;

@Service
public class MessageReader {

        //Входной параметр - название очереди
    public static void readQueue(String nameQueue) throws JMSException, InterruptedException {
        //создание фабрики для подключеник activeMQ
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        Connection con = factory.createConnection();
        Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //создание очереди из сообщений
        Queue queue = session.createQueue(nameQueue);
        MessageConsumer consumer = session.createConsumer(queue);
        //запуск соединения
        con.start();
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    TextMessage tm = (TextMessage) message;
                    if(!("".equals(tm.getText())) && tm.getText() != null) {
                        System.out.println("Message:\n " + tm.getText());
                        try(FileOutputStream fos=new FileOutputStream("ReadXml/" +
                                tm.getJMSMessageID().replaceAll(":", "") + ".xml")) {
                            byte[] buffer = tm.getText().getBytes();
                            fos.write(buffer, 0, buffer.length);}
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    exit(1);
                }
            }
        });
        Thread.sleep(15000);
        con.close();
    }

    //метод для проверки алгоритма создания и заполнения файлка
    public static void main(String[] args){
        try {
            readQueue("QueueTest");
        }catch (JMSException | InterruptedException e){
            e.printStackTrace();
            exit(1);
        }
    }
}