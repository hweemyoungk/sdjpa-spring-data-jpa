package guru.springframework.jdbc.repositories;

import guru.springframework.jdbc.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles({"local"})
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;

    @Test
    void testEmptyResultException() {
        assertThrows(EmptyResultDataAccessException.class,() -> {
            bookRepository.readByTitle("FooBar4");
        });
    }

    @Test
    void testNullParam() {
        assertNull(bookRepository.getByTitle(null));
    }

    @Test
    void testNoException() {
        assertNull(bookRepository.getByTitle("Foo4"));
    }

    @Test
    void testBookStream() {
        AtomicInteger cnt = new AtomicInteger();
        bookRepository.findAllByTitleNotNull()
                .forEach(book -> cnt.incrementAndGet());
        assertThat(cnt.get()).isGreaterThan(4);
    }

    @Test
    void testBookFuture() throws ExecutionException, InterruptedException {
        Future<Book> bookFuture = bookRepository.queryByTitle("Clean Code");
        Book book = bookFuture.get();
        assertNotNull(book);
    }

    @Test
    void testBookQuery() {
        Book book = bookRepository.findBookByTitleWithQuery("Clean Code");
        assertThat(book).isNotNull();
    }

    @Test
    void testBookQueryNamedParam() {
        Book book = bookRepository.findBookByTitleWithQueryNamedParam("Clean Code");
        assertThat(book).isNotNull();
    }

    @Test
    void testBookNativeQuery() {
        Book book = bookRepository.findBookByTitleWithNativeQuery("Clean Code");
        assertThat(book).isNotNull();
    }

    @Test
    void testBookJpaNamedQuery() {
        Book book = bookRepository.jpaNamed("Clean Code");
        assertThat(book).isNotNull();
    }
}