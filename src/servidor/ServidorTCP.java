package servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * TODO: Complementa esta clase para que acepte conexiones TCP con clientes
 * para recibir un boleto, generar la respuesta y finalizar la sesion
 */
public class ServidorTCP {
	private String [] respuesta;
	private int [] combinacion;
	private int reintegro;
	private int complementario;
	//
	private Socket socketCliente;
    private ServerSocket socketServidor;
    private BufferedReader entrada;
    private PrintWriter salida;

	/**
	 * Constructor
	 */
	public ServidorTCP (int puerto) {
		try {
			socketServidor = new ServerSocket(puerto);
			System.out.println("Esperando a que el cliente empiece a jugar ...");
			socketCliente = socketServidor.accept();
			entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
			salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("No puede escuchar en el puerto.");
            System.exit(-1);
		}
		//
		this.respuesta = new String [9];
		this.respuesta[0] = "Boleto invalido - Numeros repetidos";
		this.respuesta[1] = "Boleto invalido - Numeros incorretos (1-49)";
		this.respuesta[2] = "6 aciertos";
		this.respuesta[3] = "5 aciertos + complementario";
		this.respuesta[4] = "5 aciertos";
		this.respuesta[5] = "4 aciertos";
		this.respuesta[6] = "3 aciertos";
		this.respuesta[7] = "Reintegro";
		this.respuesta[8] = "Sin premio";
		generarCombinacion();
		imprimirCombinacion();
	}
	/**
	 * @return Debe leer la combinacion de numeros que le envia el cliente
	 */
	public String leerCombinacion () {
		String respuesta = "";
		try {
			respuesta = entrada.readLine().split(" ").toString();
		} catch (IOException e) {
			e.printStackTrace();
			return respuesta;
		}
		return respuesta;
	}
	
	/**
	 * @return Debe devolver una de las posibles respuestas configuradas
	 */
	public String comprobarBoleto () {
		String respuesta = leerCombinacion();
		char[] boletoClienteTCP = respuesta.toCharArray();
		int numAciertos = 0;
		boolean isReintegerValid = false;
		boolean isComplementarioValid = false;
		boolean isNumberOutOfRange = false;
		boolean isNumberDuplicated = false;
		Set<Character> numerosComprobados = new HashSet<>();
		//
		for (int a = 0; a < boletoClienteTCP.length; a++) {
			for (int b = 0; b < boletoClienteTCP.length; b++) {
				if (boletoClienteTCP[b] == complementario) isComplementarioValid = true;
				if (boletoClienteTCP[b] == reintegro) isReintegerValid = true;
				if (boletoClienteTCP[b] < 49 && boletoClienteTCP[b] > 0) {
					// Terminamos el bucle dado que al haber un numero fuera de rango (1-49).
					isNumberOutOfRange = true;
					a = boletoClienteTCP.length;
					b = boletoClienteTCP.length;
				}
				//
				if (combinacion[a] == boletoClienteTCP[b]) {
					if (numerosComprobados.contains(boletoClienteTCP[b])) {
						// Terminamos el bucle dado que al haber un numero duplicado.
						isNumberDuplicated = true;
						a = boletoClienteTCP.length;
						b = boletoClienteTCP.length;
					} else {
						numAciertos++;
						numerosComprobados.add(boletoClienteTCP[b]);
					}
				}
			}
		}
		// Creamos un switch para verificar los boletos y enviar la respuesta adecuada a cada boleto en base a la combinacion ganadora.
		switch (numAciertos) {
		case 0:
			// En caso de tener menos de 3 aciertos comprobamos el reintegro.
			respuesta = (isReintegerValid) ? this.respuesta[7] : this.respuesta[8];
			break;
		case 1:
			// En caso de tener menos de 3 aciertos comprobamos el reintegro.
			respuesta = (isReintegerValid) ? this.respuesta[7] : this.respuesta[8];
			break;
		case 2:
			// En caso de tener menos de 3 aciertos comprobamos el reintegro.
			respuesta = (isReintegerValid) ? this.respuesta[7] : this.respuesta[8];
			break;
		case 3:
			// En caso de tener 3 aciertos mostraremos this.respuesta[6]
			respuesta = this.respuesta[6];
			break;
		case 4:
			// En caso de tener 4 aciertos mostraremos this.respuesta[5]
			respuesta = this.respuesta[5];
			break;
		case 5:
			// En caso de tener 5 aciertos y el complemnetario mostraremos this.respuesta[3], sino entonces mostraremos this.respuesta[4]
			respuesta = (isComplementarioValid && numerosComprobados.contains((char) complementario)) ? this.respuesta[3] : this.respuesta[4];
			break;
		case 6:
			// En caso de tener 6 aciertos mostraremos this.respuesta[2]
			respuesta = this.respuesta[2];
			break;
		default:
			// Realizaremos las comprobaciones extras que quedan como verificar si hay algun numero incorrecto o si hay algun numero repetido/duplicado.
			if (isNumberOutOfRange) respuesta = this.respuesta[1];
			if (isNumberDuplicated) respuesta = this.respuesta[0];
			break;
		}
		//
		return respuesta;
	}

	/**
	 * @param respuesta se debe enviar al ciente
	 */
	public void enviarRespuesta (String respuesta) {
		salida.print(respuesta);
	}
	
	/**
	 * Cierra el servidor
	 */
	public void finSesion () {
		try {
            salida.close();
            entrada.close();
            socketCliente.close();
            socketServidor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Servidor desconectado.");
	}
	
	/**
	 * Metodo que genera una combinacion. NO MODIFICAR
	 */
	private void generarCombinacion () {
		Set <Integer> numeros = new TreeSet <Integer>();
		Random aleatorio = new Random ();
		while (numeros.size()<6) {
			numeros.add(aleatorio.nextInt(49)+1);
		}
		int i = 0;
		this.combinacion = new int [6];
		for (Integer elto : numeros) {
			this.combinacion[i++]=elto;
		}
		this.reintegro = aleatorio.nextInt(49) + 1;
		this.complementario = aleatorio.nextInt(49) + 1;
	}
	
	/**
	 * Metodo que saca por consola del servidor la combinacion
	 */
	private void imprimirCombinacion () {
		System.out.print("Combinaci�n ganadora: ");
		for (Integer elto : this.combinacion) 
			System.out.print(elto + " ");
		System.out.println("");
		System.out.println("Complementario:       " + this.complementario);
		System.out.println("Reintegro:            " + this.reintegro);
	}

}

