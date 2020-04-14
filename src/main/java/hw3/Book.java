package hw3;

import java.io.Serializable;

public class Book implements Serializable {

    int pages;
    String label;

    public Book (String label, int pages){
        this.label=label;
        this.pages=pages;
    }

    public void selectPage (int page){
        System.out.println("Book "+ label +" has been opened on page "+ page);
    }
}
