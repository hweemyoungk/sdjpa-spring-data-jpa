package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import guru.springframework.jdbc.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by jt on 8/28/21.
 */
@RequiredArgsConstructor
@Component
public class BookDaoImpl implements BookDao {
    private final BookRepository bookRepository;

    @Override
    public List<Book> findAllBooksSortByTitle(Pageable pageable) {
        return null;
    }

    @Override
    public List<Book> findAllBooks(Pageable pageable) {
        return null;
    }

    @Override
    public List<Book> findAllBooks(int pageSize, int offset) {
        return null;
    }

    @Override
    public List<Book> findAllBooks() {
        return null;
    }

    @Override
    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Book findBookByTitle(String title) {
        return bookRepository.findBookByTitle(title)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Book saveNewBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public Book updateBook(Book book) {
        AtomicReference<Book> authorAtomicReference = new AtomicReference<>();
        bookRepository.findById(book.getId()).ifPresent(foundBook -> {
            foundBook.setTitle(book.getTitle());
            foundBook.setAuthorId(book.getAuthorId());
            foundBook.setIsbn(book.getIsbn());
            foundBook.setPublisher(book.getPublisher());
            authorAtomicReference.set(bookRepository.save(foundBook));
        });
        return authorAtomicReference.get();
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
}
