package cliente;

import java.util.Scanner;


/**
 * No debes tocar este c�digo
 */
public class PrimitivaCliente {
	public static void main(String[] args) {
		Scanner sc = new Scanner (System.in);
		ClienteTCP canal = new ClienteTCP("localhost",5555);
		int [] combi = new int [6];
		System.out.println("Bienvenido a la Loteria Primitiva");
		do {
			System.out.println("Escriba su combinacion de 6 numeros (uno por linea):");
			int num;
			for (int i = 0; i < combi.length; i++) {
				combi[i] = sc.nextInt();
			}
			System.out.println(canal.comprobarBoleto(combi));
			System.out.print("�Desea volver a jugar? (s/n)");
		} while (sc.next().toLowerCase().equals("s"));
		canal.finSesion();
	}
}
