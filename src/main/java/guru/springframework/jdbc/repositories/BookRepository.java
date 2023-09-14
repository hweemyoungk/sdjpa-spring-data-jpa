package guru.springframework.jdbc.repositories;

import guru.springframework.jdbc.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book jpaNamed(@Param("title") String t); // Comes from @NamedQuery of Book

    // Write native SQL
    @Query(value = "SELECT * FROM book WHERE title = :title", nativeQuery = true)
    Book findBookByTitleWithNativeQuery(@Param("title") String t);

    // Write HQL
    @Query("SELECT b FROM Book b where b.title = :title")
    Book findBookByTitleWithQueryNamedParam(@Param("title") String t);
    // Throws QueryCreationException if any given named parameter is not found in query.
    // Book findBookByTitleWithQueryNamed(@Param("title") String t, @Param("unknown") Integer whatisthis);

    // Write HQL
    @Query("SELECT b FROM Book b where b.title = ?1")
    Book findBookByTitleWithQuery(String title);
    Optional<Book> findBookByTitle(String title);
    Book readByTitle(String title);

    // Following @Nullable s @org.springframework.lang.NonNullApi of package-info.java
    @Nullable
    Book getByTitle(@Nullable String title);

    Stream<Book> findAllByTitleNotNull(); // where title is not null

    @Async
    Future<Book> queryByTitle(String title);
}
