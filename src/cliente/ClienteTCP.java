package cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * TODO: Complementa esta clase para que genere la conexion TCP con el servidor
 * para enviar un boleto, recibir la respuesta y finalizar la sesion
 */
public class ClienteTCP {
	//
	private Socket socketCliente;
    private BufferedReader entrada;
    private PrintWriter salida;
    static Scanner teclado = new Scanner(System.in); 
	/**
	 * Constructor
	 */
	public ClienteTCP(String ip, int puerto) {
		try {
            socketCliente = new Socket(ip, puerto);
            entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
        } catch (IOException e) {
            System.err.println("No se puede conectar al servidor.");
            System.exit(-1);
        }
	}

	/**
	 * @param combinacion que se desea enviar
	 * @return respuesta del servidor con la respuesta del boleto
	 */
	public String comprobarBoleto(int[] combinacion) {
		String cadenaCombinacion = "";
		for (int i = 0; i < combinacion.length; i++) {
			cadenaCombinacion += combinacion[i] + " ";
		}
		salida.println(cadenaCombinacion);
		//
		String respuesta = "";
		try {
            respuesta = entrada.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return respuesta;
        }
		return respuesta;
	}

	/**
	 * Sirve para finalizar la la conexion de Cliente y Servidor
	 */
	public void finSesion () {
		try {
            salida.close();
            entrada.close();
            socketCliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Cliente desconectado.");
	}
}
