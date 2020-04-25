package core;

import chat_library.Library;
import network.ServerSocketThread;
import network.ServerSocketThreadListener;
import network.SocketThread;
import network.SocketThreadListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.*;

public class ChatServer implements ServerSocketThreadListener, SocketThreadListener {

    public ChatServer(ChatServerListener listener) {
        this.listener = listener;
    }

    ServerSocketThread server;
    ChatServerListener listener;
    Vector<SocketThread> clients = new Vector<>();
    Logger logger = Logger.getLogger("");
    Handler handler;

    {
        try {
            handler = new FileHandler("chat_server_logs.log", true);
            handler.setLevel(Level.INFO);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(int port) {
        if (server == null || !server.isAlive()) {
            putLog("Server started at port " + port);
            putFileLog(Level.INFO, "Server started at port " + port);
            server = new ServerSocketThread(this, "Server", port, 2000);
        } else {
            putLog("Server has been already started!");
            putFileLog(Level.WARNING, "Attempt to start server while there is active one");
        }
    }

    public void stop() {
        if (server != null || server.isAlive()) {
            System.out.println("Server has been stopped");
            putFileLog(Level.INFO, "Server has been stopped");
            server.interrupt();
        } else {
            putFileLog(Level.WARNING, "Attempt to stop server when there is no servers running");
            putLog("Server is not running");
        }
    }

    public void putLog(String msg) {
        listener.onChatServerGetMessage(msg);
    }

    public void putFileLog(Level level, String msg) {
        logger.log(level, msg);
    }

    @Override
    public void onServerStart(ServerSocketThread thread) {
        putLog("Server has been started");
        putFileLog(Level.INFO, "Server has been started");
        SqlClient.connect();
    }

    @Override
    public void onServerStop(ServerSocketThread thread) {
        putFileLog(Level.INFO, "Server has been stopped");
        putLog("Server has been stopped");
    }

    @Override
    public void onServerSocketCreated(ServerSocketThread thread, ServerSocket server) {
        putLog("Server Socket has been created");
        putFileLog(Level.INFO, "Server Socket has been created");
    }

    @Override
    public void onServerTimeout(ServerSocketThread thread, ServerSocket server) {
    }

    @Override
    public void onSocketAccepted(ServerSocketThread thread, ServerSocket server, Socket socket) {
        putLog("Client has been connected");
        putFileLog(Level.INFO, "Client has been connected");
        String name = "Socket Thread " + socket.getInetAddress() + ":" + socket.getPort();
        new ClientThread(this, name, socket);
    }

    @Override
    public void onServerException(ServerSocketThread thread, Throwable exception) {
        putFileLog(Level.WARNING, "Exception on server: " + exception.getMessage());
        exception.printStackTrace();
    }

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {
    }

    @Override
    public void onSocketStop(SocketThread thread) {
        putFileLog(Level.INFO, "Client has been disconnected");
        putLog("Client has been disconnected");
        clients.remove(thread);
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        putFileLog(Level.INFO, "Client is ready to chat");
        putLog("Client is ready to chat");
        clients.add(thread);
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        ClientThread client = (ClientThread) thread;
        if (client.isAuthorized()) {
            handleAuthorizedMessage(client, msg);
        } else {
            handleNonAuthorizedMessage(client, msg);
        }
    }

    private void handleNonAuthorizedMessage(ClientThread client, String msg) {
        String[] arr = msg.split(Library.DELIMITER);
        if (arr.length != 3 || !arr[0].equals(Library.AUTH_REQUEST)) {
            client.msgFormatError(msg);
            return;
        }
        String login = arr[1];
        String password = arr[2];
        String nickname = SqlClient.getNickname(login, password);
        if (nickname == null) {
            putFileLog(Level.WARNING, "Invalid credentials for " + login);
            putLog("Invalid credentials for " + login);
            client.authFail();
            return;
        }
        client.authAccept(nickname);
        sendToAllAuthorizedClients(Library.getTypeBroadcast("Server", nickname + " connected"));
        sendToAllAuthorizedClients(Library.getUserList(getUsers()));
    }

    private void handleAuthorizedMessage(ClientThread client, String msg) {
        String[] arr = msg.split(Library.DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case Library.TYPE_BCAST_CLIENT:
                putFileLog(Level.INFO, "Client has sent a message");
                sendToAllAuthorizedClients(Library.getTypeBroadcast(client.getNickname(), arr[1]));
                break;
            default:
                putFileLog(Level.WARNING, "Unknown message format");
                client.sendMessage(Library.getMsgFormatError(msg));
        }
        //sendToAllAuthorizedClients(msg);
    }

    private void sendToAllAuthorizedClients(String msg) {
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            if (!client.isAuthorized()) {
                continue;
            }
            client.sendMessage(msg);
        }
    }

    private String getUsers() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            if (!client.isAuthorized()) continue;
            sb.append(client.getNickname()).append(Library.DELIMITER);
        }
        return sb.toString();
    }

    @Override
    public void onSocketException(SocketThread thread, Exception exception) {
        exception.printStackTrace();
    }
}

