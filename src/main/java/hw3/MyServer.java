package hw3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer extends Thread {
    private ServerSocket server;
    private Socket clientSocket;
    int port;
    public MyServer(int port){
        this.port=port;
        runServer(port);
    }

    public static void main(String[] args) {
        MyServer server = new MyServer(6060);
        server.start();
    }

    public void runServer (int port){
        try {
            server=new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Server has been started on port "+port);
            clientSocket=server.accept();
            System.out.println("Client has been connected");
            saveData(clientSocket, "save.ser");
            System.out.println("Reading file...");
            readSavedObject("save.ser");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveData(Socket clientSocket, String fileName) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new DataInputStream(clientSocket.getInputStream()));
        FileOutputStream out = new FileOutputStream(fileName);
        int x;
        while ((x=in.read())!=-1){
            out.write(x);
        }
        in.close();
        out.close();
    }

    private void readSavedObject(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
        Book book = (Book)in.readObject();
        book.selectPage(5);
        in.close();
    }
}
