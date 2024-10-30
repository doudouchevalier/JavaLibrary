import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    //Attributs
    private Library library;
    private int userID;
    private Scanner scanner;


    //Constructeur
    public Menu(Library library, int userID) {
        this.library = library;
        this.userID = userID;
        this.scanner = new Scanner(System.in);
    }


    //Affichage du menu
    private void displayMenuOptions(boolean isAdmin) {
        System.out.println(isAdmin ? "\n===== Menu Administrateur =====" : "\n===== Menu Utilisateur =====");
        if (isAdmin){
            System.out.println("1. Afficher les livres de la bibliothèque");
        }
        else {
            System.out.println("1. Afficher les livres disponibles");
        }
        System.out.println("2. Voir les détails d'un livre");
        System.out.println("3. Louer un livre");
        System.out.println("4. Rendre un livre");
        if (isAdmin) {
            System.out.println("5. Ajouter un livre");
            System.out.println("6. Supprimer un livre");
            System.out.println("7. Ajouter un administrateur");
            System.out.println("8. Quitter");
        } else {
            System.out.println("5. Quitter");
        }
        System.out.print("Choisissez une option : ");
    }
    public void showMenu(boolean isAdmin) {
        while (true) {
            displayMenuOptions(isAdmin);
            int choice = getValidatedChoice();
            if (choice == -1) continue; // Choix invalide, demander à nouveau

            if (!handleUserChoice(choice, isAdmin)) {
                System.out.println("Déconnexion...");
                break;
            }
        }
    }



    //Vérifier les commandes de l'utilisateur connecté
    private int getValidatedChoice() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Veuillez entrer un nombre valide.");
            scanner.next(); // consomme l'entrée invalide
            return -1;
        }
    }
    private boolean handleUserChoice(int choice, boolean isAdmin) {
        scanner.nextLine();  // consomme la nouvelle ligne
        switch (choice) {
            case 1 -> {
                if (isAdmin) library.getAllBooks(); //ADMIN : afficher tous les livres
                else library.getAvailableBooks(); //NON ADMIN : afficher seulement ceux disponibles
            }
            case 2 -> handleBookDetails(); //Obtenir les détails d'un livre
            case 3 -> handleBookBorrow(); //Emprunter un livre
            case 4 -> handleBookReturn(); //Rendre un livre
            case 5 -> {
                if (!isAdmin) return false; //NON ADMIN : quitter
                handleBookAdd(); //ADMIN : Ajouter un livre
            }
            case 6 -> { if (isAdmin) handleBookRemove(); } //Supprimer un livre de la bibliothèque
            case 7 -> { if (isAdmin) handleAdminAddition(); } //Ajouter un admin
            case 8 -> { if (isAdmin) return false; } //L'utilisateur fait le choix de quitter.
            default -> System.out.println("Choix invalide. Réessayez.");
        }
        return true;
    }
    private int promptForISBN(String promptMessage) {
        System.out.print(promptMessage);
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Veuillez entrer un ISBN valide.");
                scanner.next(); // consomme l'entrée invalide
            }
        }
    }


    //Actions à effectuer
    private void handleBookDetails() {
        int ISBN = promptForISBN("Entrez l'ISBN du livre : ");
        System.out.println(library.getBookDetails(ISBN).toString());
    }
    private void handleBookBorrow() {
        int ISBN = promptForISBN("Entrez l'ISBN du livre à louer : ");
        library.borrowBook(ISBN, userID);
        saveBooks();
    }
    private void handleBookReturn() {
        int ISBN = promptForISBN("Entrez l'ISBN du livre à rendre : ");
        library.returnBook(ISBN, userID);
        saveBooks();
    }
    private void handleBookAdd() {
        int ISBN = promptForISBN("Entrez l'ISBN du livre à ajouter : ");
        if (library.contains(ISBN)) {
            library.addCopy(ISBN);
        } else {
            library.addBook(handleBookCreation());
        }
        saveBooks();
    }
    private Book handleBookCreation() {
        scanner.nextLine();  // nettoie l'entrée
        System.out.print("Titre du livre : ");
        String title = scanner.nextLine();
        System.out.print("Description : ");
        String description = scanner.nextLine();
        System.out.print("Auteur : ");
        String author = scanner.nextLine();
        System.out.print("Prix : ");
        int price = scanner.nextInt();
        return new Book(title, description, author, price);
    }
    private void handleBookRemove() {
        int ISBN = promptForISBN("Entrez l'ISBN du livre à supprimer : ");
        library.removeBook(ISBN);
        saveBooks();
    }
    private void handleAdminAddition() {
        library.addUser(new User(), true);
        saveUsers();
    }


    //Sauvegarde des données lorsqu'elles sont modifiées
    private void saveBooks() {
        saveToFile("catalogue/catalogue_livres.json", library.getBooks());
    }
    private void saveUsers() {
        saveToFile("catalogue/catalogue_users.json", library.getUsers());
    }
    private <T> void saveToFile(String filePath, T data) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
