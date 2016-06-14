public enum ArquivosParaClassificar {
	
	A1("a1.txt"), A2("a2.txt"),	A3("a3.txt"), 
	B1("b1.txt"), B2("b2.txt"),	B3("b3.txt"),
	C1("c1.txt"), C2("c2.txt"), C3("c3.txt"), 
	D1("d1.txt"), D2("d2.txt"),	D3("d3.txt"),
	E1("e1.txt"), E2("e2.txt"),	E3("e3.txt"),
	J1("j1.txt"), J2("j2.txt"), J3("j3.txt"),
	K1("k1.txt"), K2("k2.txt"),	K3("k3.txt");
	
	private static final String CAMINHO_ARQUIVOS_CLASSIFICACAO = "C:\\Java\\workspace\\kohonen\\src\\main\\resources\\";
	String nomeArquivo;
	
	private ArquivosParaClassificar(String nomeArquivo) {
		this.nomeArquivo = CAMINHO_ARQUIVOS_CLASSIFICACAO + nomeArquivo;
	}
}
