import java.util.List;

public class Letra {

	private int[] valores;
	private String classe;
	private String nome;
	
	public Letra(String caminhoArquivo, String nome) {
		this.nome = nome;
		popularAPartirDoArquivo(caminhoArquivo);
	}

	private void popularAPartirDoArquivo(String caminhoArquivo) {
		valores = new int[Algoritmo.QUANTIDADE_NEORONIOS_DE_ENTRADA];
		List<String> arquivo = LeitorDeArquivo.leArquivo(caminhoArquivo);
		int positionParaAdicionar = 0;
		for (String linha : arquivo) {
			for (int i = 0; i < linha.length(); i++) {
				char caracter = linha.charAt(i);
				if (caracter == '#') {
					valores[positionParaAdicionar] = 1;
				} else if (caracter == '.') {
					valores[positionParaAdicionar] = -1;
				}
				positionParaAdicionar++;
			}
		}
	}

	public int[] getValores() {
		return valores;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getNome() {
		return nome;
	}
}
