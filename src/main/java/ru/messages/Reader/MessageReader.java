package ru.messages.Reader;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Service;
import javax.jms.*;
import java.io.*;

import static java.lang.System.exit;

@Service
public class MessageReader {

    public static void readQueue(String nameQueue) throws JMSException {
        String text;
        //создание фабрики для подключеник activeMQ
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        Connection con = factory.createConnection();
        Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //создание очереди из сообщений
        Queue queue = session.createQueue(nameQueue);
        MessageConsumer consumer = session.createConsumer(queue);
        //запуск соединения
        con.start();
        while(true) {
            text = "";
            Message msg = consumer.receive(1000);
            //перевод очереди в текст
            if (msg instanceof TextMessage) {
                TextMessage tm = (TextMessage) msg;
                text += tm;
                System.out.println(tm.getText());
                try {
                    System.out.println("Message: " + text);
                    if(!("".equals(text))) {
                        int count = 0;
                        //создание нового файла xml
                        while (!(new File("SendXml\\newFile" + count + ".xml").createNewFile()))
                            count++;
                        //открытие файла с последующим заполнением
                        File xmlFile = new File("SendXml\\newFile" + count + ".xml");
                        FileWriter fw = new FileWriter(xmlFile);
                        BufferedWriter bw = new BufferedWriter(fw);
                        //заполнение файла кодом xml
                        bw.write(tm.getText());
                        bw.close();
                        fw.close();
                    }else
                    {
                        System.out.println("Messages is null");
                        exit(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    exit(1);
                }
            } else {
                System.out.println("Queue Empty");
                //при завершении очереди выключение соединения
                con.stop();
                exit(0);
                break;
            }
        }
    }

    //метод для проверки алгоритма создания и заполнения файлка
    public static void main(String[] args){
        try {
            readQueue("QueueTest");
        }catch (JMSException e){
            e.printStackTrace();
            exit(1);
        }
    }
}