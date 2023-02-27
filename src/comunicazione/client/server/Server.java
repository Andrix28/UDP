package comunicazione.client.server;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Andrea Barbanera
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(6789);
            boolean attivo = true;
            byte[] bufferIN = new byte[1024];
            byte[] bufferOUT = new byte[1024];
            
            System.out.println("SERVER avviato...");
            while (attivo){
                //definizione del datagramma
                DatagramPacket receivePacket =
                        new DatagramPacket(bufferIN,bufferIN.length);
                try {
                    //attesa della ricezione dato del client
                    serverSocket.receive(receivePacket);
                    
                    //analisi del pacchetto ricevuto
                    String ricevuto = new String(receivePacket.getData());
                    int numCaratteri = receivePacket.getLength();
                    ricevuto=ricevuto.substring(0,numCaratteri); //elimina i caratteri in eccesso
                    System.out.println("RICEVUTO: " + ricevuto);
                    
                    //riconoscimento dei parametri del socket del client
                    InetAddress IPClient = receivePacket.getAddress();
                    int portaClient = receivePacket.getPort();
                    
                    //preparo il dato da spedire
                    String daSpedire = ricevuto.toUpperCase();
                    bufferOUT = daSpedire.getBytes();
                    //invio del Datagramma
                    DatagramPacket sendPacket =
                            new DatagramPacket(bufferOUT, bufferOUT.length, IPClient, portaClient);
                    serverSocket.send(sendPacket);
                    
                    //controllo termine esecuzione del server
                    if (ricevuto.equals("fine"))
                    {
                        System.out.println("SERVER IN CHIUSURA. Buona serata.");
                        attivo = false;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            serverSocket.close();
            
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
