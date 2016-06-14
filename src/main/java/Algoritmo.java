import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Algoritmo {
	
	public static final int NUMERO_DE_CLASSES = 25;
	public static final int QUANTIDADE_NEORONIOS_DE_ENTRADA = 63;
	public static final int RAIO_DE_VIZINHOS = 2;
	public static final int QUANTIDADE_DE_ITERACOES = 10000;

	public void executar(){
		List<Letra> letras = inicializaLetras();
		double[][] matrizW = new double[QUANTIDADE_NEORONIOS_DE_ENTRADA][NUMERO_DE_CLASSES];
		matrizW = inicializaMatrizDePesosAleatoriamente(matrizW);
		double taxaDeAprendizado = 0.6;
		for (int i = 0; i < QUANTIDADE_DE_ITERACOES; i++) {
			for (Letra letra : letras) {
				int classeComMenosDistancia = encontraMenorDistanciaEuclidiana(letra, matrizW);
				letra.setClasse(Integer.toString(classeComMenosDistancia));
				matrizW = atualizaPesos(classeComMenosDistancia, matrizW, taxaDeAprendizado, letra);
			}
			taxaDeAprendizado = taxaDeAprendizado > 0.01 ? taxaDeAprendizado - 0.01 : taxaDeAprendizado;
		}
		
		for(Letra letra : letras) {
			System.out.println(letra.getNome() + " - classe: " + letra.getClasse());
		}
	}
	
	private double[][] atualizaPesos(int classeComMenosDistancia, double[][] matrizW, double taxaDeAprendizado, Letra letra) {
		for (int i = 0; i < matrizW[classeComMenosDistancia].length; i++) {
			matrizW[classeComMenosDistancia][i] = matrizW[classeComMenosDistancia][i] + taxaDeAprendizado * (letra.getValores()[i] - matrizW[classeComMenosDistancia][i]);
			for (int j = 0; j < RAIO_DE_VIZINHOS; j++) {
				int vizinhosMaiores = i + j;
				int vizinhosMenores = i - j;
				if (vizinhosMaiores > RAIO_DE_VIZINHOS) {
					vizinhosMaiores = vizinhosMaiores - RAIO_DE_VIZINHOS;
				}
				
				if (vizinhosMenores < 0) {
					vizinhosMenores = vizinhosMenores + RAIO_DE_VIZINHOS;
				}
				matrizW[classeComMenosDistancia][vizinhosMaiores] = matrizW[classeComMenosDistancia][vizinhosMaiores] + taxaDeAprendizado * (letra.getValores()[i] - matrizW[classeComMenosDistancia][vizinhosMaiores]);
				matrizW[classeComMenosDistancia][vizinhosMenores] = matrizW[classeComMenosDistancia][vizinhosMenores] + taxaDeAprendizado * (letra.getValores()[i] - matrizW[classeComMenosDistancia][vizinhosMenores]);
			}
		}
		
		return matrizW;
	}

	private int encontraMenorDistanciaEuclidiana(Letra letra, double[][] matrizW) {
		double[] distanciasEncontradas = new double[NUMERO_DE_CLASSES];
		for (int i = 0; i < NUMERO_DE_CLASSES; i++) {
			double[] pessos = matrizW[i];
			double valorDistancia = 0D;
			for (int j = 0; j < pessos.length; j++) {
				valorDistancia += (pessos[j] - letra.getValores()[j]) * (pessos[j] - letra.getValores()[j]);
			}
			distanciasEncontradas[i] = valorDistancia;
		}
		
		double menorDistancia = Double.MAX_VALUE;
		int classeMenorDistancia = 0;
		for(int i = 0; i < distanciasEncontradas.length; i++) {
			if (distanciasEncontradas[i] < menorDistancia) {
				menorDistancia = distanciasEncontradas[i];
				classeMenorDistancia = i;
			}
		}
		
		return classeMenorDistancia;
		
	}

	private List<Letra> inicializaLetras() {
		List<Letra> letras = new ArrayList<>();
		for(ArquivosParaClassificar arquivo : ArquivosParaClassificar.values()){
			Letra l = new Letra(arquivo.nomeArquivo, arquivo.toString());
			letras.add(l);
		}
		return letras;
	}

	private double[][] inicializaMatrizDePesosAleatoriamente(double[][] matriz) {
		for (int linha = 0; linha < matriz.length; linha++) {
			for (int coluna = 0; coluna < matriz[linha].length; coluna++) {
				matriz[linha][coluna] = new Random().nextDouble() - 0.5;
			}
		}
		return matriz;
	}
	
	public static void main(String[] args) {
		new Algoritmo().executar();
	}

}
