package models;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 *
 * @author Dasha
 *
 * This is the class with the book model
 */
public abstract class Book {

    private int bookId, amountInStock, amountSold;
    protected BigDecimal price;
    protected String title, authorName;

    public enum Genre {
        COMEDY, DRAMA, HORROR, TRAGEDY, FICTION, BIOGRAPHY
    };
    protected static Genre genre;
    protected LocalDate dateOfPublication;
    protected File cover;

    /**
     * Default constructor
     *
     * @param title
     * @param authorName
     * @param genre
     * @param price
     * @param dateOfPublication
     * @param amountInStock
     * @param amountSold
     */
    public Book(String title, String authorName, Genre genre, BigDecimal price, 
            LocalDate dateOfPublication, int amountInStock, int amountSold) {
        setBookId(bookId);
        setTitle(title);
        setAuthorName(authorName);
        setPrice(price);
        setGenre(genre);
        setDateOfPublication(dateOfPublication);
        setAmountInStock(amountInStock);
        setAmountSold(amountSold);
        setCover(new File("./src/images/placeholder-cover.png"));
        this.amountSold = amountSold;

    }
    
    public Book(String title, String authorName, Genre genre, BigDecimal price, LocalDate dateOfPublication, 
            int amountInStock, int amountSold, File cover) throws IOException {
        setBookId(bookId);
        setTitle(title);
        setAuthorName(authorName);
        setPrice(price);
        setGenre(genre);
        setDateOfPublication(dateOfPublication);
        setAmountInStock(amountInStock);
        setAmountSold(amountSold);
        setCover(cover);
        copyImageFile();

    }

    /**
     * This method validate the price. If the price is less then 0, Illegal
     * argument exception is thrown.
     *
     * @param price
     */
    public void setPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price should be greater then 0");
        } else {
            this.price = price;
        }
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    /**
     * This method validate the title parameter. The first letter should be
     * capital or the Illegal argument exception is thrown. If the title field
     * is empty the Illegal argument exception is thrown.
     *
     * @param title
     */
    public void setTitle(String title) {
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Please, enter the title");
        } else if (!title.matches("[A-Z].*")) {
            throw new IllegalArgumentException("Title must start with an upper case");
        } else {
            this.title = title;
        }
    }

    /**
     * This method validate the amount in stock parameter. The number should be
     * greater then zero.
     *
     * @param amountInStock
     */
    public void setAmountInStock(int amountInStock) {
        if (amountInStock < 0) {
            throw new IllegalArgumentException("Stock amount should be greater then or equal to 0");
        } else {
            this.amountInStock = amountInStock;
        }
    }

    /**
     * This method validate the amount sold parameter. The number should be
     * greater or equal to zero.
     *
     * @param amountSold
     */
    public void setAmountSold(int amountSold) {
        if (amountSold < 0) {
            throw new IllegalArgumentException("Please, enter the amount sold");
        } else {
            this.amountSold = amountSold;
        }
    }

    /**
     * This method validate the authors name parameter. The first letter should
     * be capital or the Illegal argument exception is thrown.
     *
     * @param authorName
     */
    public void setAuthorName(String authorName) {
        if (authorName.isEmpty()) {
            throw new IllegalArgumentException("Please, enter the author name");
        } else if (!authorName.matches("[A-Z].*")) {
            throw new IllegalArgumentException("Author's name must start with an upper case");

        } else {
            this.authorName = authorName;
        }
    }

    /**
     * This method validate the date of publication. It should be not greater
     * then todays date.
     *
     * @param dateOfPublication
     */
    public void setDateOfPublication(LocalDate dateOfPublication) {
        if (dateOfPublication.isBefore(LocalDate.now()) || dateOfPublication.isEqual(LocalDate.now())) {
            this.dateOfPublication = dateOfPublication;
        } else {
            throw new IllegalArgumentException("The book can't be from the future");
        }
    }

    public void setCover(File cover) {
        this.cover = cover;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    
    

    /**
     * This method will store the book cover image to the local server server
     *
     * @throws IOException
     */
    public void copyImageFile() throws IOException {
        Path sourcePath = cover.toPath();

        String uniqueFileName = getUniqueFileName(cover.getName());

        Path targetPath = Paths.get("./src/images/" + uniqueFileName);

        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        cover = new File(targetPath.toString());
    }

    /**
     *
     * @param name
     * @return
     */
    private String getUniqueFileName(String name) {
        String newName;

        SecureRandom rng = new SecureRandom();

        do {
            newName = "";

            for (int count = 1; count <= 32; count++) {
                int nextChar;

                do {
                    nextChar = rng.nextInt(123);
                } while (!validCharacterValue(nextChar));

                newName = String.format("%s%c", newName, nextChar);
            }
            newName += name;

        } while (!uniqueFileInDirectory(newName));

        return newName;
    }

    /**
     *
     * @param fileName
     * @return
     */
    public boolean uniqueFileInDirectory(String fileName) {
        File directory = new File("./src/images/");

        File[] dir_contents = directory.listFiles();

        for (File file : dir_contents) {
            if (file.getName().equals(fileName)) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param asciiValue
     * @return
     */
    public boolean validCharacterValue(int asciiValue) {

        //0-9 = ASCII range 48 to 57
        if (asciiValue >= 48 && asciiValue <= 57) {
            return true;
        }

        //A-Z = ASCII range 65 to 90
        if (asciiValue >= 65 && asciiValue <= 90) {
            return true;
        }

        //a-z = ASCII range 97 to 122
        if (asciiValue >= 97 && asciiValue <= 122) {
            return true;
        }

        return false;
    }

    public int getBookId() {
        return bookId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Genre getGenre() {
        return genre;
    }

    public LocalDate getDateOfPublication() {
        return dateOfPublication;
    }

    public File getCover() {
        return cover;
    }

    public int getAmountInStock() {
        return amountInStock;
    }

    public int getAmountSold() {
        return amountSold;
    }

    /**
     * This method returns Book title by authorName in genre genre cost price,
     * publication date is dateOfPublication
     *
     * @return
     */
    @Override
    public String toString() {
        return "Book " + title + " by " + authorName + " in genre " + genre
                + " cost " + price + ", publication date is " + dateOfPublication;
    }

    public abstract void insertIntoDB() throws SQLException;

}
