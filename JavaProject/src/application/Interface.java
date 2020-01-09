package application;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Interface extends Application {
	BaseDeTweets b = new BaseDeTweets();
	@Override
	public void start(Stage stage) throws FileNotFoundException {

		//Cr�ation de notre image
		Image image = new Image(new FileInputStream("Accueil.png")); 
		//D�claration de l'image de la page d'accueil
		ImageView imageView1 = new ImageView(image); 
		//Positionnement de l'image 
		imageView1.setY(25); 
		//D�finition de la taille de l'image
		imageView1.setFitHeight(580); 
		imageView1.setFitWidth(1000); 

		// Cr�ation de la barre de menu
		MenuBar menuBar = new MenuBar();

		// Cr�ation des diff�rends onglets
		Menu fileMenu = new Menu("Fichier");
		Menu fileAide = new Menu("Aide");

		// Cr�ation des sous-menus
		MenuItem recherche = new MenuItem("Recherche par mot cl�");
		MenuItem openFile = new MenuItem("Ouvrir fichier txt de base");
		MenuItem openTableau=new MenuItem("Ouvrir tableau enregistr� pr�c�demment");
		MenuItem sauvegarder=new MenuItem("Sauvegarder tableau");
		MenuItem exit = new MenuItem("Sortir");
		MenuItem aide = new MenuItem("Comment �a marche ?");


		// Ajout des sous-menu dans les onglets correspondant
		fileMenu.getItems().addAll(openFile, recherche,openTableau,sauvegarder,exit);
		fileAide.getItems().addAll(aide);

		// Ajout des onglets dans la barre de menu
		menuBar.getMenus().addAll(fileMenu, fileAide);

		//Cr�ation d'un conteneur
		BorderPane root = new BorderPane();
		//Positionnement de la barre de menu en haut de notre fen�tre
		root.setTop(menuBar);

		//On regroupe notre barre de menu et notre image
		Group root2 = new Group(menuBar, imageView1);

		//Cr�ation du contenu physique
		Scene scene = new Scene(root2, 1000, 600);

		//D�clenchement d'une action a l'appuie du sous-menu "Ouvrir fichier txt de base"
		openFile.setOnAction(new EventHandler<ActionEvent>() {
			//Cr�ation de l'�venement
			@Override
			public void handle(ActionEvent event) {
				//D�claration de variable
				String k = new String();
				String m = new String();
				Stage s = new Stage();

				//Cr�ation d'une boite de dialogue pour r�cup�rer une sasie de l'utilisateur
				TextInputDialog dialog = new TextInputDialog("");
				dialog.setTitle("Fen�tre de dialogue");
				dialog.setContentText("Nom du fichier txt :");
				Optional<String> result = dialog.showAndWait();
				//On v�rifie la pr�sence du fichier
				if (result.isPresent()){
					k=result.get();	
					File Fichier = new File(k);
					//Tant que le fichier n'existe pas, on reste bloqu� � cette fen�tre
					while(!Fichier.exists()){
						dialog.setTitle("Fen�tre de dialogue");
						dialog.setContentText("Nom du fichier txt :");
						result = dialog.showAndWait();
						if (result.isPresent()){
							k=result.get();
							Fichier = new File(k);}
					}
					//Ouverture d'une fen�tre nous permettant de choisir le tri de nos donn�es
					ChoiceDialog<String> dialog3 = new ChoiceDialog<>("Tri par retweets","Tri par pseudos","Tri par dates");
					dialog3.setTitle("Fen�tre de dialogue");
					dialog3.setContentText("Choisissez le tri qui vous int�resse (modifiable par la suite):");
					Optional<String> result3 = dialog3.showAndWait();
					//Une fois le choix effectu� on peut afficher la table
					if (result3.isPresent()){
						m=result3.get();
						TableView<tweet> table = new TableView<tweet>();

						//colonne pseudo
						TableColumn<tweet, String> PseudoCol
						= new TableColumn<tweet, String>("Pseudo");

						//Colonne description tweet
						TableColumn<tweet, String> DescriptionTweetCol
						= new TableColumn<tweet, String>("Description Tweet");

						//Colonne date tweet
						TableColumn<tweet, LocalDateTime> DateCol
						= new TableColumn<tweet, LocalDateTime>("Date");

						//Colonne du nombre de retweet
						TableColumn<tweet, Double> NbRetweetsCol
						= new TableColumn<tweet, Double>("Nombre de retweets");

						//Cr�ation des noms de colonne
						PseudoCol.setCellValueFactory(new PropertyValueFactory<>("pseudo"));
						DescriptionTweetCol.setCellValueFactory(new PropertyValueFactory<>("description"));
						DateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
						NbRetweetsCol.setCellValueFactory(new PropertyValueFactory<>("nbRetweets"));
						ObservableList<tweet> list = getListeTweets(k,m);
						table.setItems(list);

						//Ajout des donn�es
						table.getColumns().addAll(PseudoCol, DescriptionTweetCol, DateCol, NbRetweetsCol);
						StackPane root = new StackPane();
						root.setPadding(new Insets(5));
						root.getChildren().add(table);

						//On indique le titre de la fen�tre
						s.setTitle("Base de tweets");

						//Cr�ation de la fen�tre contenant notre recherche
						Scene scene = new Scene(root, 450, 300);
						s.setScene(scene);
						s.show();
					}
				}
			}});

		//D�clenchement d'une action a l'appuie du sous-menu "Sauvegarder tableau"
		sauvegarder.setOnAction(new EventHandler<ActionEvent>() {
			//Cr�ation de l'�venement
			@Override
			public void handle(ActionEvent event) {
				String nom_fichier = new String();
				Stage s = new Stage();

				//Saisie du nom de la sauvegarde
				TextInputDialog dialog = new TextInputDialog("");
				dialog.setTitle("Fen�tre de dialogue");
				dialog.setContentText("Nom de la sauvegarde :");

				// Traditional way to get the response value.#####################"
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()){
					nom_fichier=result.get();	
					b.sauvegarder(nom_fichier);

				}
			}
		});

		//D�clenchement d'une action a l'appuie du sous-menu "Recherche par mot cl�"
		recherche.setOnAction(new EventHandler<ActionEvent>() {
			//Cr�ation de l'�venement
			@Override
			public void handle(ActionEvent event) {
				String l = new String();
				String m = new String();
				Stage s = new Stage();

				//Saisie d'un mot clef qui ne doit pas comport� d'espace
				TextInputDialog dialog2 = new TextInputDialog("");
				dialog2.setTitle("Fen�tre de dialogue");
				dialog2.setContentText("Mot cl� que vous voulez rechercher dans les tweets :");
				Optional<String> result2 = dialog2.showAndWait();
				if (result2.isPresent()){
					l=result2.get();
					//Tant que le mot saisie n'est pas correct on recommence
					while(l.split("\\s+").length>1) {
						dialog2.setTitle("Fen�tre de dialogue");
						dialog2.setContentText("Mot cl� que vous voulez rechercher dans les tweets :");
						// Traditional way to get the response value.
						result2 = dialog2.showAndWait();
						if (result2.isPresent()){
							l=result2.get();}
					}
					//Ouverture d'une fen�tre nous permettant de choisir le tri de nos donn�es
					ChoiceDialog<String> dialog3 = new ChoiceDialog<>("Tri par pertinence (tfIdf)","Tri par retweets","Tri par pseudos","Tri par dates");
					dialog3.setTitle("Fen�tre de dialogue");
					dialog3.setContentText("Choisissez le tri qui vous int�resse (modifiable par la suite):");
					Optional<String> result3 = dialog3.showAndWait();
					//Une fois le choix effectu� on peut afficher la table
					if (result3.isPresent()){
						m=result3.get();
						TableView<tweet> table = new TableView<tweet>();

						//Colonne pseudo
						TableColumn<tweet, String> PseudoCol
						= new TableColumn<tweet, String>("Pseudo");

						//Colonne description tweet
						TableColumn<tweet, String> DescriptionTweetCol
						= new TableColumn<tweet, String>("Description Tweet");

						//Colonne date tweet
						TableColumn<tweet, LocalDateTime> DateCol
						= new TableColumn<tweet, LocalDateTime>("Date");

						//Colonne retweet
						TableColumn<tweet, Double> NbRetweetsCol
						= new TableColumn<tweet, Double>("Nombre de retweets");

						//Cr�ation des noms de colonne
						PseudoCol.setCellValueFactory(new PropertyValueFactory<>("pseudo"));
						DescriptionTweetCol.setCellValueFactory(new PropertyValueFactory<>("description"));
						DateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
						NbRetweetsCol.setCellValueFactory(new PropertyValueFactory<>("nbRetweets"));
						ObservableList<tweet> list = getListeTweetsRecherche(l,m);
						table.setItems(list);

						//Ajout des donn�es
						table.getColumns().addAll(PseudoCol, DescriptionTweetCol, DateCol, NbRetweetsCol);
						StackPane root = new StackPane();
						root.setPadding(new Insets(5));
						root.getChildren().add(table);

						//Ajout du titre de la table
						s.setTitle("Base de tweets");

						//Cr�ation de la fen�tre contenant notre recherche
						Scene scene = new Scene(root, 450, 300);
						s.setScene(scene);
						s.show();
					}
				}
			}});

		//D�clenchement d'une action a l'appuie du sous-menu "Ouvrir tableau enregistr� pr�c�demment"
		openTableau.setOnAction(new EventHandler<ActionEvent>() {
			//Cr�ation de l'�venement
			@Override
			public void handle(ActionEvent event) {
				String k = new String();
				String m = new String();
				Stage s = new Stage();

				//Cr�ation d'une boite de dialogue pour r�cup�rer une sasie de l'utilisateur
				TextInputDialog dialog = new TextInputDialog("");
				dialog.setTitle("Fen�tre de dialogue");
				dialog.setContentText("Nom du fichier contenant le tableau � ouvrir :");
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()){
					k=result.get();	
					File Fichier = new File(k);
					//Si le fichier n'existe pas on recommence
					while(!Fichier.exists()){
						dialog.setTitle("Fen�tre de dialogue");
						dialog.setContentText("Nom du fichier txt :");
						result = dialog.showAndWait();
						if (result.isPresent()){
							k=result.get();
							Fichier = new File(k);}
					}
					//On choisis la colonne de trie
					ChoiceDialog<String> dialog3 = new ChoiceDialog<>("Tri par retweets","Tri par pseudos","Tri par dates");
					dialog3.setTitle("Fen�tre de dialogue");
					dialog3.setContentText("Choisissez le tri qui vous int�resse (modifiable par la suite):");
					Optional<String> result3 = dialog3.showAndWait();
					//Une fois le choix effectu� on peut afficher la table
					if (result3.isPresent()){
						m=result3.get();
						TableView<tweet> table = new TableView<tweet>();

						//Colonne pseudo
						TableColumn<tweet, String> PseudoCol
						= new TableColumn<tweet, String>("Pseudo");

						//Colonne description tweet
						TableColumn<tweet, String> DescriptionTweetCol
						= new TableColumn<tweet, String>("Description Tweet");

						//Colonne date tweet
						TableColumn<tweet, LocalDateTime> DateCol
						= new TableColumn<tweet, LocalDateTime>("Date");

						//Colonne de retweet
						TableColumn<tweet, Double> NbRetweetsCol
						= new TableColumn<tweet, Double>("Nombre de retweets");

						//Cr�ation des noms de colonne
						PseudoCol.setCellValueFactory(new PropertyValueFactory<>("pseudo"));
						DescriptionTweetCol.setCellValueFactory(new PropertyValueFactory<>("description"));
						DateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
						NbRetweetsCol.setCellValueFactory(new PropertyValueFactory<>("nbRetweets"));
						
						ObservableList<tweet> list = getListeTweetsTab(k,m);
						table.setItems(list);

						//Ajout des donn�es
						table.getColumns().addAll(PseudoCol, DescriptionTweetCol, DateCol, NbRetweetsCol);
						StackPane root = new StackPane();
						root.setPadding(new Insets(5));
						root.getChildren().add(table);

						//Nom de la fen�tre
						s.setTitle("Base de tweets");

						//Cr�ation de la fen�tre contenant notre recherche
						Scene scene = new Scene(root, 450, 300);
						s.setScene(scene);
						s.show();
					}
				}
			}});

		//D�clenchement d'une action a l'appuie du sous-menu "Comment �a marche ?"
		aide.setOnAction(new EventHandler<ActionEvent>() {
			//Cr�ation de l'�venement
			@Override
			public void handle(ActionEvent event) {
				//Cr�ation de notre image
				try {
					Stage s = new Stage();
					Image image2 = new Image(new FileInputStream("Aide.png")); 
					//D�claration de l'image de la page d'accueil
					ImageView imageView2 = new ImageView(image2); 
					//D�finition de la taille de l'image
					imageView2.setFitHeight(600); 
					imageView2.setFitWidth(1000); 
					FlowPane rootAide = new FlowPane();
					//On encapsule l'image
					rootAide.getChildren().addAll(imageView2);
					//Cr�ation du contenu physique
					Scene scene2 = new Scene(rootAide, 1000, 600);

					// Positionnement de la fen�tre par rapport � la fen�tre principale
					s.setX(stage.getX() + 200);
					s.setY(stage.getY() + 100);

					////Nom de la fen�tre aide
					s.setTitle("Aide");
					s.setScene(scene2);
					//Affichage de la fen�tre
					s.show();

				} catch (FileNotFoundException e) {
					System.out.println("L'image n'existe pas !");
				}
			}
		});

		//D�clenchement d'une action a l'appuie du sous-menu "Sortir"
		exit.setOnAction(new EventHandler<ActionEvent>() {
			//Cr�ation de l'�venement
			@Override
			public void handle(ActionEvent event) {
				//Fermeture de la fen�tre
				System.exit(0);
			}
		});

		//Nom de la fen�tre principale
		stage.setTitle("Interface graphique");
		stage.setScene(scene);
		//Affichage de la fen�tre
		stage.show();
	};

	//Cr�ation de la fonction getListeTweet pour la r�alisation du tableau
	private ObservableList<tweet> getListeTweets(String nom,String i) {
		ArrayList<tweet> t = b.ouvrir(nom);
		b.setInfosTweet(t);
		t=b.nombreRetweets();
		b.setInfosTweet(t);
		//Switch pour l'appel des foncitons de tris
		switch(i) {
		case "Tri par retweets": 
			t = b.tweetParRetweets();
			b.setInfosTweet(t);
			break;	
		case "Tri par pseudos":
			t = b.tweetParPseudo();
			b.setInfosTweet(t);
			break;
		case "Tri par dates":
			t = b.tweetParDate();
			b.setInfosTweet(t);
			break;
		}
		//Conversion ArrayList en FX collection pour tableView
		ObservableList<tweet> list = FXCollections.observableArrayList(t);
		return list;
	}

	//Cr�ation de la fonction getListeTweet pour la r�alisation du tableau avec une sauvegarde
	private ObservableList<tweet> getListeTweetsTab(String nom,String i) {
		ArrayList<tweet> t = b.ouvrirArray(nom);
		b.setInfosTweet(t);
		t=b.nombreRetweets();
		b.setInfosTweet(t);
		//Switch pour l'appel des foncitons de tris
		switch(i) {
		case "Tri par retweets": 
			t = b.tweetParRetweets();
			b.setInfosTweet(t);
			break;	
		case "Tri par pseudos":
			t = b.tweetParPseudo();
			b.setInfosTweet(t);
			break;
		case "Tri par dates":
			t = b.tweetParDate();
			b.setInfosTweet(t);
			break;
		}
		//Conversion ArrayList en FX collection pour tableView
		ObservableList<tweet> list = FXCollections.observableArrayList(t);
		return list;
	}

	//Cr�ation de la fonction getListeTweet pour la recherche
	private ObservableList<tweet> getListeTweetsRecherche(String mot_recherche,String i) {
		ArrayList<tweet> t=b.rechercheMotCle(mot_recherche);
		//Switch pour l'appel des foncitons de tris
		switch(i) {
		case "Tri par pertinence (tfIdf)": 
			t = b.tweetParTfIdf();
			break;
		case "Tri par retweets": 
			t = b.tweetParRetweets();
			break;	
		case "Tri par pseudos":
			t = b.tweetParPseudo();
			break;
		case "Tri par dates":
			t = b.tweetParDate();
			break;
		}
		//Conversion ArrayList en FX collection pour tableView
		ObservableList<tweet> list = FXCollections.observableArrayList(t);
		return list;
	}

	//Programme principal
	public static void main(String[] args) {
		Application.launch(args);
	}

}