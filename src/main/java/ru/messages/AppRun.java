package ru.messages;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Locale;
import static java.lang.System.exit;
import static ru.messages.Reader.MessageReader.readQueue;
import static ru.messages.Sender.MessageSender.send;

@SpringBootApplication
public class AppRun implements CommandLineRunner {

    public static void main(String[] args){
        SpringApplication.run(AppRun.class, args);
    }

    @Override
    public void run(String... args) {
        if (args.length > 0) {
            //Передача первого аргумента коммандной строки
            // 0 - действие с сообщением, "Send" - отправить, "Read" - прочитать
            switch (args[0].toUpperCase(Locale.ROOT)) {
                case "SEND": {
                    if (args.length > 2) {
                        try {
                            //Передача следующих трех аргументов из командной строки в функцию
                            //1 - название очереди, куда отправляется сообщение, по умолчанию QueueTest
                            //2 - путь файла *.xml
                            send(args[1], args[2]);
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
        } else System.out.println("\"Send\" - отправить, \"Read\" - прочитать");
        // завершаем программу
        exit(0);
    }
}