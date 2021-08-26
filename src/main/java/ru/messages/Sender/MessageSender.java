package ru.messages.Sender;

import javax.jms.*;

import com.sun.istack.NotNull;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;

import static java.lang.System.exit;
import static ru.messages.Reader.XMLReader.Read;

@Service
public class MessageSender {

    public static final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    public static final String QUEUE_NAME = "QueueTest";

    //Входные параметры:
    //url, по которому находится activeMQ, по умолчанию "tcp://127.0.0.1:61616"
    //название очереди, по умолчанию "QueueTest"
    //путь файла, который надо отправить
    public static void Send(String url, String queueName, @NotNull String filePath) throws JMSException {
        System.out.println("********** Sending messages in activeMQ started.");
        if ((url == "" || url == null) && ("".equals(queueName) || queueName == null)) {
            url = ACTIVEMQ_URL;
            queueName = QUEUE_NAME;
        }else {
            if ("".equals(url) || url == null)
                url = ACTIVEMQ_URL;
            if("".equals(queueName) || queueName == null)
                queueName = QUEUE_NAME;
        }
        // 1 Создаем фабрику соединений, используем имя пользователя и пароль по умолчанию,
        // и кодировка больше не отображается
        ConnectionFactory ConnectionFactory = new ActiveMQConnectionFactory(url);
        // 2 Подключаемся и начинаем
        System.out.println("********** connecting to ActiveMQ.");
        Connection connection = ConnectionFactory.createConnection();
        System.out.println("********** connection started.");
        connection.start();
        // 3 Создаем сеанс, этот метод имеет два параметра, первый - отправлять ли транзакцией,
        // а второй - знак по умолчанию
        System.out.println("********** Session started.");
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 4 Создаем очередь
        System.out.println("********** Queue started.");
        Queue queue = session.createQueue(queueName);
        // 5 Создать производителя
        MessageProducer messageProducer = session.createProducer(queue);
        // 6 отправка сообщения с кодом xml файла в MQ в очередь с именем queueName
        System.out.println("********** Message is sending.");
        try {
            messageProducer.send(session.createTextMessage(Read(filePath)));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        //7 Закрываем ресурс
        messageProducer.close();
        session.close();
        connection.close();
        System.out.println("********** Sending messages in activeMQ completed.");
    }

    public static void main(String[] args){
        try {
            Send("", "QueueTest", "AcceptXml\\World.xml");
        }catch (JMSException e){
            System.out.println("Error");
            exit(1);
        }
    }
}
