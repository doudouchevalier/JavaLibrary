import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class Library {
    //Attributs
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private static Library instance;


    //Constructeur
    public static Library getInstance(){
        if(instance==null){
            instance = new Library();
        }
        return instance;
    }
    public Library(){};


    //Connexion de l'utilisateur
    public int login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez votre identifiant utilisateur : ");
        String line = scanner.nextLine();
        int userID = Integer.parseInt(line);
        for (User user : users) {
            if (user.getIdentifier() == userID) {
                return userID;
            }
        }
        System.out.println("Utilisateur non trouvé.");
        return -1;
    }


    // Vérifier ce qui est contenu dans les listes
    public boolean isAvailable(int isbn){
        if (isbn < books.size() && books.get(isbn).getNbAvailable() > 0){
            return true;
        }
        else return false;
    }
    public boolean contains(int ISBN){
        return books.stream().anyMatch(b -> b.getISBN() == ISBN);
    }
    public boolean isRegistered(int userID){
        return users.stream().anyMatch(u -> u.getIdentifier() == userID);
    }


    //Actions utilisateur
    public void getAvailableBooks(){
        for (Book book : books){
            if (book.getNbAvailable() > 0){
                System.out.println(book.toString());
            }
        }
    }
    public boolean borrowBook(int ISBN, int userID) {
        User tenant = getUserById(userID);
        //le livre est dispo, l'utilisateur a moins de 3 livres, et il n'a pas l'ouvrage en sa possession
        if (tenant != null && isAvailable(ISBN)) {
            decrementNbAvailable(ISBN);
            tenant.borrowBook(ISBN);
            return true;
        }
        System.out.println("Vous ne pouvez pas emprunter cet ouvrage.");
        return false;
    }
    public boolean returnBook(int ISBN, int userID){
        User tenant = getUserById(userID);
        Book bookToReturn = getBookDetails(ISBN).orElse(null);
        if (tenant != null && bookToReturn != null && tenant.hasAlreadyRented(ISBN)){
            tenant.borrowBook(ISBN);
            incrementNbAvailable(ISBN);
            return true;
        }
        return false;
    }


    //Actions admin
    public void getAllBooks() {
        for (Book book : books) {
            System.out.println(book.toString());
        }
    }
    public void addCopy(int ISBN){
        incrementNbAvailable(ISBN);
        incrementNbCopies(ISBN);
    }
    public void addBook(Book book){
        if (!contains(book.getISBN())){
            books.add(book);
        }
        else{
            addCopy(book.getISBN());
        }
    }
    public void removeBook(int ISBN){
        Book bookToRemove = getBookDetails(ISBN).orElse(null);
        if (bookToRemove != null && bookToRemove.getNbCopies()>0){
            decrementNbAvailable(ISBN);
            decrementNbCopies(ISBN);
        }
    }
    public void addUser(User user, boolean isAdmin){
        if (!isRegistered(user.getIdentifier())){
            user.setAdmin(isAdmin);
            users.add(user);
        }
    }


    //Obtenir un livre
    public Optional<Book> getBookDetails(int isbn){
        return books.stream().filter(b -> b.getISBN() == isbn).findFirst();
    }
    //Conversion en chaine de caractères
    public String printBook(int ISBN){
        return getBookDetails(ISBN).map(Book::toString).orElse("Livre non-trouvé.");
    }

    //Obtenir un utilisateur
    public User getUserById(int id) {
        return users.stream().filter(u -> u.getIdentifier() == id).findFirst().orElse(null);
    }


    //Gestion des stocks
    public void incrementNbAvailable(int ISBN){
        books.get(ISBN).setNbAvailable(books.get(ISBN).getNbAvailable() + 1);
    }
    public void decrementNbAvailable(int ISBN){
        books.get(ISBN).setNbAvailable(books.get(ISBN).getNbAvailable() - 1);
    }
    public void incrementNbCopies(int ISBN){
        books.get(ISBN).setNbCopies(books.get(ISBN).getNbCopies()+1);
    }
    public void decrementNbCopies(int ISBN){
        books.get(ISBN).setNbCopies(books.get(ISBN).getNbCopies()-1);
    }


    //Getters
    public ArrayList<Book> getBooks() {
        return books;
    }
    public ArrayList<User> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        StringBuilder libraryInfo = new StringBuilder("La bibliothèque contient :\nLIVRES :\n");
        for (Book book : books) {
            libraryInfo.append(book.toString()).append("\n");
        }
        libraryInfo.append("UTILISATEURS :\n");
        for (User user : users) {
            libraryInfo.append(user.toString()).append("\n");
        }
        return libraryInfo.toString();
    }
}
