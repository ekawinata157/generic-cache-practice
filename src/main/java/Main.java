import interfaces.CachingStrategy;
import model.Book;
import core.Cache;
import org.junit.Assert;
import strategy.LFUCachingStrategy;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        CachingStrategy<Book, String> cachingStrategy = new LFUCachingStrategy<>();
        Cache<Book, String> bookCache = new Cache<>(cachingStrategy, 3, String.class);
        Book book1 = new Book("1", "I want to be rich", "Mr. Pengen Pengen");
        Book book2 = new Book("2", "I want to be rich 2", "Mr. Pengen Pengen");
        Book book3 = new Book("3", "I want to be rich 3", "Mr. Pengen Pengen");
        Book book4 = new Book("4", "I want to be rich 4", "Mr. Pengen Pengen");

        bookCache.put(book1);
        bookCache.put(book2);
        bookCache.put(book3);

        bookCache.get("1");
        bookCache.get("1");

        bookCache.put(book4);

        Assert.assertFalse(bookCache.getIdToCacheableMap().containsKey("2"));
        Assert.assertTrue(bookCache.getIdToCacheableMap().containsKey("1"));
        Assert.assertTrue(bookCache.getIdToCacheableMap().containsKey("3"));
        Assert.assertTrue(bookCache.getIdToCacheableMap().containsKey("4"));
    }
}
