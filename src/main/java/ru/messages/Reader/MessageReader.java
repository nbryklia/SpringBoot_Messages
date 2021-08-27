package ru.messages.Reader;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import javax.jms.*;
import java.io.*;
import static java.lang.System.exit;

@Service
public class MessageReader {

        //Входной параметр - название очереди
    public static void readQueue(String nameQueue) throws JMSException, InterruptedException {
        String text;
        //создание фабрики для подключеник activeMQ
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        Connection con = factory.createConnection();
        Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //создание очереди из сообщений
        Queue queue = session.createQueue(nameQueue);
        Message mess = session.createTextMessage();
        MessageProducer producer = session.createProducer(queue);
        producer.send(mess);
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    TextMessage tm = (TextMessage) message;
                    if(!("".equals(tm.getText())) && tm.getText() != null) {
                        System.out.println("Message: " + tm.getText());
                        int count = 0;
                        //создание нового файла xml
                        if(!(new File("ReadXml").mkdir())){}
                        while (!(new File("ReadXml\\newFile" + count + ".xml").createNewFile()))
                            count++;
                        //открытие файла с последующим заполнением
                        File xmlFile = new File("ReadXml\\newFile" + count + ".xml");
                        FileWriter fw = new FileWriter(xmlFile);
                        BufferedWriter bw = new BufferedWriter(fw);
                        //заполнение файла кодом xml
                        bw.write(tm.getText());
                        bw.close();
                        fw.close();
                    }else
                    {
                        System.out.println("Messages is null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    exit(1);
                }
            }
        });
        //запуск соединения
        con.start();
        Thread.sleep(5000);
        con.close();

        /*while(true) {
            MessageListener msg = consumer.getMessageListener();
            //перевод очереди в текст
            if (msg instanceof TextMessage) {
                TextMessage tm = (TextMessage) msg;
                System.out.println(tm.getText());
                try {
                    System.out.println("Message: " + tm.getText());
                    if(!("".equals(tm.getText()))) {
                        int count = 0;
                        //создание нового файла xml
                        if(!(new File("ReadXml").mkdir())){}
                        while (!(new File("ReadXml\\newFile" + count + ".xml").createNewFile()))
                            count++;
                        //открытие файла с последующим заполнением
                        File xmlFile = new File("ReadXml\\newFile" + count + ".xml");
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
        }*/
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