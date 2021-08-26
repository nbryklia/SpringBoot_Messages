package ru.messages;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

import static ru.messages.Sender.MessageSender.Send;
import static ru.messages.Reader.MessageReader.readQueue;
import static java.lang.System.exit;

@SpringBootApplication
public class AppRun implements CommandLineRunner {

    public static void main(String[] args){
        //args = new String[]{"send","tcp://127.0.0.1:6161", "QueueTest", "D:\\SpringBoot\\AcceptXml\\app.xml"};
        SpringApplication app = new SpringApplication(AppRun.class);
        //отключаем баннер
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run (String[] args) {
        if (args.length > 0) {
            //Передача первого аргумента коммандной строки
            // 0 - действие с сообщением, "Send" - отправить, "Read" - прочитать
            switch (args[0].toUpperCase(Locale.ROOT)) {
                case "SEND": {
                    if (args.length > 3) {
                        try {
                            //Передача следующих трех аргументов из командной строки в функцию
                            //1 - url, где находится ActiveMQ, по умолчанию tcp://localhost:61616
                            //2 - название очереди, куда отправляется сообщение, по умолчанию QueueTest
                            //3 - путь файла *.xml
                            Send(args[1], args[2], args[3]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Insufficient data");
                    }
                    break;
                }
                case "READ": {
                    if (args.length > 1) {
                        try {
                            //Передача следующего одного аргумента из командной строки в функцию
                            //1 - название очереди, куда отправляется сообщение
                            readQueue(args[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Insufficient data");
                    }
                    break;
                }
                default:
                    System.out.println("\"Send\" - отправить, \"Read\" - прочитать");
                    break;
            }
        }
        else System.out.println("\"Send\" - отправить, \"Read\" - прочитать");
        exit(0); // завершаем программу
    }
}
