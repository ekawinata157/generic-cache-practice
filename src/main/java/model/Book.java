package model;

import annotation.CacheID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book {
    @CacheID
    private String isbn;
    private String title;
    private String author;
}
