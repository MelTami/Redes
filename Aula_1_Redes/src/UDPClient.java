import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Base64;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.crypto.spec.SecretKeySpec;


class UDPClient {
	
	 // Criptografar
    public static String encrypt(String message) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
  //Criar Chave FIXA , deve ser igual no cliente e no servidor
    private static final byte[] fixedKey = "MinhaChaveSecret".getBytes(); // Chave 16bits , PRECISA TER 16 Letras !!!!
    private static SecretKey secretKey = new SecretKeySpec(fixedKey, "AES");
	
	public static void main(String args[]) throws Exception {
		//Create datagram socket
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		//Read a sentence from the console
		while(true) {
		BufferedReader inFromUser =	new BufferedReader(new InputStreamReader(System.in));
		String sentence = inFromUser.readLine();
		// Criptografando msg
		 String encryptedMessage = encrypt(sentence);
		 System.out.println("Mensagem criptografada: " + encryptedMessage);
		//Allocate buffers
		byte[] sendData = encryptedMessage.getBytes();
		byte[] receiveData = new byte[1024];
		
		//Send packet to the server
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
		clientSocket.send(sendPacket);
		//Get the response from the server
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		//Print the received response
		String modifiedSentence = new String(receivePacket.getData());
		System.out.println("RECEIVED FROM SERVER: " + modifiedSentence);
		}
	}
}
