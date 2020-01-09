package application;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Scanner;

public class tweet implements Serializable
{
	private String numTweet;
	private String pseudo;
	private LocalDateTime date;
	private String description;
	private String pseudoRetweet;
	private Double nbRetweets;
	//tf servira à classer la recherche par mot clé suivant la pertinence du tweet (en l'occurrence la frquence du mot recherché dans chaque tweet)
	private Double tfIdf;

	//méthode afficher
	public void afficher() {
		System.out.println("Pseudo :"+this.pseudo);
		System.out.println("Date :"+this.date);
		System.out.println("Description"+this.description);
		System.out.println("Retweet"+this.nbRetweets);
		System.out.println("TFIdf"+this.tfIdf);
	}


	//initialisation des tweets
	public tweet(String numTweet, String pseudo, LocalDateTime date, String description, String pseudoRetweet,
			Double nbRetweets, Double tfIdf) {
		super();
		this.numTweet = numTweet;
		this.pseudo = pseudo;
		this.date = date;
		this.description = description;
		this.pseudoRetweet = pseudoRetweet;
		this.nbRetweets = nbRetweets;
		this.tfIdf = tfIdf;
	}


	//setters et getters
	public String getNumTweet() {
		return numTweet;
	}



	public void setNumTweet(String numTweet) {
		this.numTweet = numTweet;
	}



	public String getPseudo() {
		return pseudo;
	}



	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}



	public LocalDateTime getDate() {
		return date;
	}



	public void setDate(LocalDateTime date) {
		this.date = date;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getPseudoRetweet() {
		return pseudoRetweet;
	}



	public void setPseudoRetweet(String pseudoRetweet) {
		this.pseudoRetweet = pseudoRetweet;
	}



	public Double getNbRetweets() {
		return nbRetweets;
	}



	public void setNbRetweets(Double nbRetweets) {
		this.nbRetweets = nbRetweets;
	}



	public Double getTfIdf() {
		return tfIdf;
	}



	public void setTfIdf(Double tfIdf) {
		this.tfIdf = tfIdf;
	}


	public String toString() {
		String affichage = ("Numéro :"+numTweet+"\n Date :"+date+"\n Pseudo :"+pseudo+"\n Description :"+description+"\n Pseudo Retweet :"+pseudoRetweet+"\n Nombre de retweets :"+nbRetweets+"\n TfIdf :"+tfIdf+"\n -----------");
		return affichage;
	}


	//comparateurs pour trier les tweets par la suite

	//comparateur par date
	public static Comparator<tweet> tweetDateComparator = new Comparator<tweet>() {

		public int compare(tweet t1, tweet t2) {
			LocalDateTime tweetDate1 = t1.getDate();
			LocalDateTime tweetDate2 = t2.getDate();

			//dates décroissantes	
			return tweetDate2.compareTo(tweetDate1);
		}};


		//comparateur par pseudo
		public static Comparator<tweet> tweetParpseudo = new Comparator<tweet>() {

			public int compare(tweet t1, tweet t2) {

				String pseudo1 = t1.getPseudo().toUpperCase();
				String pseudo2 = t2.getPseudo().toUpperCase();
				LocalDateTime tweetDate1 = t1.getDate();
				LocalDateTime tweetDate2 = t2.getDate();
				int res = pseudo1.compareTo(pseudo2);
				/*For ascending order*/
				if (res==0) {
					return tweetDate2.compareTo(tweetDate1);
				}else {
					return res;
				}

				/*For descending order*/
				//rollno2-rollno1;
			}
		};


		//comparateur par tfIdf
		public static Comparator<tweet> tweetPartfIdf = new Comparator<tweet>() {

			public int compare(tweet t1, tweet t2) {

				Double tf1 = t1.getTfIdf();
				Double tf2 = t2.getTfIdf();
				LocalDateTime tweetDate1 = t1.getDate();
				LocalDateTime tweetDate2 = t2.getDate();
				int res = tf2.compareTo(tf1);

				if (res==0) {
					return tweetDate2.compareTo(tweetDate1);
				}else {
					return res;
				}

			}
		};

		//comparateur par nombre de retweets
		public static Comparator<tweet> tweetParNbRetweets = new Comparator<tweet>() {

			public int compare(tweet t1, tweet t2) {

				Double tf1 = t1.getNbRetweets();
				Double tf2 = t2.getNbRetweets();
				LocalDateTime tweetDate1 = t1.getDate();
				LocalDateTime tweetDate2 = t2.getDate();
				int res = tf2.compareTo(tf1);

				if (res==0) {
					return tweetDate2.compareTo(tweetDate1);
				}else {
					return res;
				}

			}};



}