package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.repositories.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by jt on 8/28/21.
 */
@RequiredArgsConstructor
@Component
public class AuthorDaoImpl implements AuthorDao {
    private final AuthorRepository authorRepository;

    @Override
    public Author getById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        return authorRepository.findAuthorByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Author saveNewAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Transactional
    @Override
    public Author updateAuthor(Author author) {
        AtomicReference<Author> authorAtomicReference = new AtomicReference<>();
        authorRepository.findById(author.getId()).ifPresent(foundAuthor -> {
            foundAuthor.setFirstName(author.getFirstName());
            foundAuthor.setLastName(author.getLastName());
            authorAtomicReference.set(authorRepository.save(foundAuthor));
        });
        return authorAtomicReference.get();
    }

    @Override
    public void deleteAuthorById(Long id) {
        authorRepository.deleteById(id);
    }
}
