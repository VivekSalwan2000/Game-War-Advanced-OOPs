package connection.Server;

import connection.Clients.RequestType;
import connection.protocols.InvalidProtocolRequestException;
import connection.protocols.Protocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler extends Thread {

    private static final AtomicInteger CLIENT_COUNTER = new AtomicInteger(0);
    private final int clientNumber;
    private Socket clientSocket;
    ObjectOutputStream serverOutputStream;
    ObjectInputStream serverInputStream;
    ObjectInputStream dataObjectInputStream;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.clientNumber = CLIENT_COUNTER.incrementAndGet();

        try{
                serverOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                serverInputStream = new ObjectInputStream((clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getClientNumber(){
        return clientNumber;
    }

    @Override
    public void run() {

        Protocol protocol = null;
        try {
            boolean condition=false;
            if (serverInputStream != null && serverOutputStream != null) {
                protocol = new Protocol(serverOutputStream, serverInputStream);
                condition=true;
            }

            while (condition) {
                RequestType requestType = (RequestType) serverInputStream.readObject();
                if (requestType == RequestType.TERMINATE_SERVER){
                    condition = false;
                break;
            }
                try{
                    protocol.processInput(requestType);
                }catch (InvalidProtocolRequestException e){
                    System.out.println("Invalid protocol request. Try again");
                }
            }
        } catch (IOException e) {
            System.err.println("IO exception caught");
        }
        catch (ClassNotFoundException e){
            System.err.println("Class Not found execution occurred");
        }
        finally {
            if (protocol != null) {
                protocol.shutdown();
            }
        }
    }


}
