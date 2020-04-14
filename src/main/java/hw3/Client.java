package hw3;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;

    public Client (String host, int port, String fileName){
        try {
            socket=new Socket(host,port);
            send(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Book book = new Book("ABC",100);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save_book.ser"));
        out.writeObject(book);
        out.close();
        new Client("127.0.0.1", 6060,"save_book.ser");
    }

    private void send (String fileName) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName));
        BufferedOutputStream out = new BufferedOutputStream(new DataOutputStream(socket.getOutputStream()));
        int x;
        while ((x=in.read())!=-1){
            out.write(x);
        }
        in.close();
        out.close();
    }
}
