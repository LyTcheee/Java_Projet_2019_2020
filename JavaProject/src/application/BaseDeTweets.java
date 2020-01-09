package application;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class BaseDeTweets {
	//preciser le type des objets du treeset dans les crochets
	private ArrayList<tweet> infosTweet;

	//initialisation
	public void initialise(){
		infosTweet=new ArrayList<tweet>();
	}

	//setters et getters
	public ArrayList<tweet> getInfosTweet() {
		return infosTweet;
	}

	public void setInfosTweet(ArrayList<tweet> infosTweet) {
		this.infosTweet = infosTweet;
	}

	//sauvegarde de la collection
	public void sauvegarder(String fichier) {
		try
		{
			FileOutputStream w =new FileOutputStream(fichier);
			ObjectOutputStream o = new ObjectOutputStream(w);
			o.writeObject(infosTweet);
			o.close();
			w.close();
		} catch (Exception e)
		{ System.out.println("Erreur");
		}
	}

	//ouverture du fichier classique et mise en place sous forme de array
	public ArrayList<tweet> ouvrir(String fichier) {
		//aide avec https://gist.github.com/einan/0435d84e75efdc110b2d

		//compteur du nombre de lignes
		int n = 0;
		infosTweet=new ArrayList<tweet>();
		BufferedReader br=null;
		//on essaye d'importer le fichier
		try {
			String ligne=null;
			FileInputStream r = new FileInputStream(fichier);
			InputStreamReader i = new InputStreamReader(r,
					Charset.forName("UTF-8"));
			br = new BufferedReader(i);
			ligne=br.readLine();
			//tant qu'il y a des lignes dans le fichier
			while(ligne!=null && n<1000000) {
				n++;
				//on split les diff�rentes colonnes qui sont s�par�es par des tabulations
				String[]tab=ligne.split("\t+");

				//si la taille du tableau est de 5, c'est un retweet
				if(tab.length==5) {
					//on ajoute le tweet dans la collection. 
					//nbTweets et tfIdf seront calcul�s dans une autre fonction, on leur met la valeur 0 pour le moment 
					infosTweet.add(new tweet(String.valueOf(n),tab[1],LocalDateTime.parse(tab[2].replace(' ', 'T')),tab[3],tab[4],(double) 0,(double) 0));

					//si la taille du tableau est de 4, c'est un tweet. On note "" dans pseudoRetweet
				}else if(tab.length==4) {
					infosTweet.add(new tweet(String.valueOf(n),tab[1],LocalDateTime.parse(tab[2].replace(' ', 'T')),tab[3],"",(double) 0,(double) 0));						
				}
				ligne=br.readLine();	
			}
			br.close();
		}catch (Exception e){
			System.out.println(e);
			return infosTweet;
		}
		return infosTweet;

	}




	//ouverture d'un tableau enregistr� pr�c�demment et mise en place sous forme de array
	public ArrayList<tweet> ouvrirArray(String fichier) {
		try {
			FileInputStream r = new FileInputStream(fichier);
			ObjectInputStream o = new ObjectInputStream(r);				
			ArrayList<tweet> infosTweet = (ArrayList) o.readObject();								
			o.close();
			r.close();
			return infosTweet;
		}catch (Exception e){
			System.out.println("Erreur d�entr�e-sortie");
			return null;
		}
	}

	//m�thode de recherche par mot cl�
	public ArrayList<tweet> rechercheMotCle(String mot) {
		//on supprime les accents du mot
		mot=sansAccent(mot).toLowerCase();


		//premi�re partie : calcul de idf. Pour le calculer, on a besoin de regrouper tous les mots des tweets dans une m�me collection
		Iterator<tweet> it = infosTweet.iterator();
		ArrayList<String> DescriptionsTweet = new ArrayList<String>();
		tfIdf tfI = new tfIdf();
		while (it.hasNext()) {
			tweet t = it.next();
			String[] motsLigne=t.getDescription().toLowerCase().split("[,\\s\\t?!.@]+");
			for (String word : motsLigne) {
				DescriptionsTweet.add(sansAccent(word));
			}
		}
		Double Idf_Mot=tfI.idf(DescriptionsTweet,mot);


		//seconde partie : s�lection des donn�es de la collection avec comme crit�re le mot recherch� 
		//et calcul de tf * l'idf trouv� pr�c�demment
		Iterator<tweet> it2 = infosTweet.iterator();
		//cr�ation d'un tableau o� on stocke tous les tweets contenant le mot
		ArrayList<tweet> infosTweet_recherche = new ArrayList<tweet>(); 
		tfIdf tfI2 = new tfIdf();
		//lecture des tweets de infosTweet
		while (it2.hasNext()) {
			tweet t = it2.next();
			//la var motLigne sert au calcul de tf
			String[] motsLigne=t.getDescription().toLowerCase().split("[,\\s\\t?!.@]+");
			//si le mot est trouv� dans le tweet, on l'ajoute � la collection et on calcule le tfIdf
			if(sansAccent(t.getDescription().toLowerCase()).contains(mot)) {
				Double tf__Idf = tfI2.tf(motsLigne, mot)*Idf_Mot;
				infosTweet_recherche.add(new tweet(t.getNumTweet(),t.getPseudo(),t.getDate(),t.getDescription(),t.getPseudoRetweet(),t.getNbRetweets(),tf__Idf));
			}
		}

		infosTweet=infosTweet_recherche;

		return infosTweet;
	}

	//m�thode calculant le nombre de retweets
	public ArrayList<tweet> nombreRetweets() {
		//Premi�re partie : on met tous les tweets de base(qui ne sont pas des retweets) dans un tableau
		ArrayList<tweet> TweetsDeBase = new ArrayList<tweet>(); 
		Iterator<tweet> it = infosTweet.iterator();
		while (it.hasNext()) {
			tweet t = it.next();
			if(t.getPseudoRetweet().equals("")) {
				//tweet de base trouv�
				TweetsDeBase.add(t);
				//on supprime ce tweet dans l'autre array
				it.remove();
			}
		}

		//deuxi�me partie: on regarde le nombre de retweets pr�sents dans infosTweet pour chaque tweet de base

		//on place un it�rateur sur le tableau que l'on vient de cr�er
		Iterator<tweet> it2 = TweetsDeBase.iterator();


		while(it2.hasNext()) {
			tweet t2=it2.next();
			//on cr�e une variable nb_retweet pour connaitre le nb de retweets du tweet
			double nb_retweets=0;
			//on place un nouvel i�rateur de infosTweet
			Iterator<tweet> it3 = infosTweet.iterator();
			while(it3.hasNext()) {
				tweet t3=it3.next();

				//si le tweet du tableau cr�� est �gal au tweet que l'on regarde dans infosTweet, on incr�mente nb_retweets
				if(t2.getDescription().equals(t3.getDescription())){
					nb_retweets++;
				}
			}
			t2.setNbRetweets(nb_retweets);
		}
		infosTweet=TweetsDeBase;
		return infosTweet;
	}

	//m�thode de tri des tweet par date
	public ArrayList<tweet> tweetParDate(){
		Collections.sort(infosTweet, tweet.tweetDateComparator);
		return infosTweet;
	}

	//m�thode de tri des tweet par pseudo
	public ArrayList<tweet> tweetParPseudo(){
		Collections.sort(infosTweet, tweet.tweetParpseudo);
		return infosTweet;
	}

	//m�thode de tri des tweet par tfidf
	public ArrayList<tweet> tweetParTfIdf(){
		Collections.sort(infosTweet, tweet.tweetPartfIdf);
		return infosTweet;
	}

	//m�thode de tri des tweet par retweets
	public ArrayList<tweet> tweetParRetweets(){
		Collections.sort(infosTweet, tweet.tweetParNbRetweets);
		return infosTweet;
	}


	//m�thode sans accent que nous avons cr��
	public static String sansAccent(String s) 
	{

		String strTemp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(strTemp).replaceAll("");
	}

}