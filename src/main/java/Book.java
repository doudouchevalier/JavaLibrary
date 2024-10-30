import java.util.Objects;
public class Book {
    //Attributs
    private static int isbnCounter = 0;
    private int nbAvailable = 1;
    private int nbCopies = 1;
    private final int ISBN;
    private String title;
    private String description;
    private String author;
    private float price;


    //Constructeur
    public Book(String title, String description, String author, float price){
        this.ISBN = isbnCounter++;
        setTitle(title);
        setDescription(description);
        setAuthor(author);
        setPrice(price);
    }


    //Getters
    public int getISBN() {
        return ISBN;
    }
    public int getNbCopies() {
        return nbCopies;
    }
    public String getTitle() {
        return title;
    }
    public int getNbAvailable() {
        return nbAvailable;
    }


    //Setters
    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Le livre doit comporter un titre.");
        }
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setAuthor(String author) {
        if (author == null || author.isEmpty()) {
            throw new IllegalArgumentException("Le livre doit comporter un titre.");
        }
        this.author = author;
    }
    public void setPrice(float price) {
        if (price < 0) {
            throw new IllegalArgumentException("Le prix ne peut pas être négatif.");
        }
        this.price = price;
    }
    public void setNbAvailable(int nbAvailable) {
        if (nbAvailable < 0 || nbAvailable > nbCopies){
            throw new IllegalArgumentException("Le nombre d'exemplaires disponibles doit être compris entre 0 " +
                    "et le nombre d'exemplaires total.");
        }
        this.nbAvailable = nbAvailable;
    }
    public void setNbCopies(int nbCopies) {
        if (nbCopies < 0){
            throw new IllegalArgumentException("Le nombre d'exemplaires doit être positif ou nul.");
        }
        this.nbCopies = nbCopies;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Book book = (Book) obj;
        return ISBN == book.ISBN;
    }
    @Override
    public String toString() {
        return "Book{" +
                "ISBN='" + ISBN + '\'' +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", nbCopies=" + nbCopies +
                ", nbAvailable=" + nbAvailable +
                '}';
    }
    @Override
    public int hashCode() {
        return Objects.hash(ISBN);
    }
}
