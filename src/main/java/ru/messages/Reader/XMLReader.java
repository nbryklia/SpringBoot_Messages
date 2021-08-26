package ru.messages.Reader;

import java.io.*;

public class XMLReader {

    //метод для прочтения файла
    public static String Read(String filePath) throws FileNotFoundException {
        File xmlFile;
        //переменная для хранения текста сообщения
        String text = "";
        try {
            //открытие xml файла для последующего чтения
            xmlFile = new File(filePath);
            FileReader fr = new FileReader(xmlFile);
            BufferedReader br = new BufferedReader(fr);
            //чтение файла построчно
            String line = br.readLine();
            while(line != null){
                //запись строки в строковую переменную
                text = text.concat(line + '\n');
                line = br.readLine();
            }
        } catch (IOException e){ e.printStackTrace(); }
        return text;
    }

    public static void main(String[] args){
        try {
            System.out.print(Read("AcceptXml\\World.xml"));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
}