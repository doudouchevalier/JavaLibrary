import java.util.ArrayList;

public class User {
    //Attributs
    private Library library;
    private boolean isAdmin = false;
    private final int identifier;
    private static int idCounter = 0;
    ArrayList<Integer> borrowedBooks = new ArrayList<>(3);


    //Constructeur
    public User(){
        this.identifier = idCounter++;
    }


    //Déterminer ce que l'utilisateur possède
    public boolean canRentMoreBooks() {
        if (borrowedBooks.size() >= 3) {
            System.out.println("L'utilisateur a déjà emprunté trois livres");
            return false;
        }
        return true;
    }
    public boolean hasAlreadyRented(int isbn){
        for(int i : borrowedBooks){
            if(i == isbn){
                return true;
            }
        }
        return false;
    }


    //Actions utilisateur
    public boolean borrowBook(int ISBN){
        if (canRentMoreBooks() && !hasAlreadyRented(ISBN)){
            borrowedBooks.add(ISBN);
            return true;
        }
        return false;
    }
    public boolean returnBook(int ISBN){
        if (hasAlreadyRented(ISBN)){
            for (int b : borrowedBooks){
                if (b == ISBN){
                    borrowedBooks.remove(b);
                    return true;
                }
            }
        }
        return false;
    }


    //Setter
    public void setAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }


    //Getters
    public int getIdentifier() {
        return identifier;
    }
    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public String toString() {
        StringBuilder userInfo = new StringBuilder("L'utilisateur " + identifier + " a emprunté:\n");
        String user = "ID : " + identifier + " a emprunté :\n";
        for(int ISBN : borrowedBooks){
            userInfo.append(library.printBook(ISBN)).append("\n");
        }
        return userInfo.toString();
    }
}
