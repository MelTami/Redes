// created on 29/09/2010 at 22:33
import java.net.*;
import java.util.Base64;

import javax.crypto.Cipher;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class UDPServer {
	//Criar Chave FIXA , deve ser igual no cliente e no servidor
	private static final byte[] fixedKey = "MinhaChaveSecret".getBytes(); // Chave 16bits , PRECISA TER 16 Letras !!!!
    private static SecretKey secretKey = new SecretKeySpec(fixedKey, "AES");
	
	// Metodo decriptografar
	public static String decrypt(String msg) throws Exception{
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE,secretKey);
		byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(msg));
		return new String(decrypted);
	}
	
	public static void main(String args[]) throws Exception {
		//Create server socket
		DatagramSocket serverSocket = new DatagramSocket(9876);
		System.out.println("Sv Iniciado");
		
		while(true) {
			byte[] receiveData = new byte[1024];
			//block until packet is sent by client
			DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivedPacket);
			//Get the information about the datagram of the client
			InetAddress IPAddress = receivedPacket.getAddress();
			int port = receivedPacket.getPort();
			//Descriptografar
			String encryptmsg = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
			String decrypted = decrypt(encryptmsg);
		
			//Get the data of the packet
			String sentence = decrypted;
			System.out.println("RECEIVED FROM CLIENT Encrypted "+IPAddress.getHostAddress()+": " + encryptmsg);
			System.out.println("Decrypted : "+ sentence);
			//Confirmação de recebimento
			byte[] sendData = new byte[encryptmsg.length()];
			sendData = encryptmsg.getBytes();
			//Send back the response to the client
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}
	}
}
