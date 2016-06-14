import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LeitorDeArquivo {
	
	
	public static List<String> leArquivo(String path){
		List<String> arquivo = new ArrayList<String>();
		try {
			Scanner in = new Scanner(new FileReader(path));
			while (in.hasNextLine()) {
				String line = in.nextLine();
				arquivo.add(line);
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return arquivo;
		
	}

}
