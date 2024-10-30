import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

public class Main {
    public static void main(String[] args) {
        //Créer un Singleton Library
        Library library = Library.getInstance();

        //Chargement des livres et des utilisateurs
        loadBooks(library);
        loadUsers(library);

        //Connexion de l'utilisateur
        int userID = library.login();
        if (userID != -1) {
            System.out.println("Utilisateur " + userID + " connecté !");
            User currentUser = library.getUserById(userID);

            //Affichage du menu approprié
            Menu menu = new Menu(library, userID);
            menu.showMenu(currentUser.isAdmin()); //Affiche le menu utilisateur ou admin
        }
    }


    //Chargement des données contenues dans les fichiers JSON
    private static void loadBooks(Library library) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("catalogue/catalogue_livres.json")) {
            Type bookListType = new TypeToken<ArrayList<Book>>(){}.getType();
            ArrayList<Book> books = gson.fromJson(reader, bookListType);
            if (books != null) { // Vérification des données null
                books.forEach(library::addBook);
            }
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier JSON des livres.");
            e.printStackTrace();
        }
    }
    private static void loadUsers(Library library) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("catalogue/catalogue_users.json")) {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            ArrayList<User> users = gson.fromJson(reader, userListType);
            if (users != null) { // Vérification des données null
                users.forEach(user -> library.addUser(user, user.isAdmin()));
            }
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier JSON des utilisateurs.");
            e.printStackTrace();
        }
    }

}