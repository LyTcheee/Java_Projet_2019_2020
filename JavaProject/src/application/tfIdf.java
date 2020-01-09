package application;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

//nous avons trouv� cette classe sur le suite suivant : https://gist.github.com/guenodz/d5add59b31114a3a3c66 
//et l'avons adapt� � nos besoin en le restructurants.
public class tfIdf {
	//m�thode tf
	public Double tf(String[] doc, String term) {
		double result = 0;
		for (String word : doc) {
			if (term.equalsIgnoreCase(sansAccent(word))) {
				result++;
			}
		}
		return result / doc.length;
	}

	//m�thode idf
	public Double idf(ArrayList<String> docs, String term) {
		double n = 0;
		Iterator<String> it = docs.iterator();
		while (it.hasNext()) {
			String s = it.next();
			String[] doc=s.toLowerCase().split("[,\\s\\t?!.@]+");
			for (String mot : doc) {
				if (term.equalsIgnoreCase(sansAccent(mot))) {
					n++;
					break;
				}
			}
		}
		return Math.log(docs.size() / n);
	}

	//fonction sans accent qu enous avons cr��
	public static String sansAccent(String s) 
	{

		String strTemp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(strTemp).replaceAll("");
	}


}