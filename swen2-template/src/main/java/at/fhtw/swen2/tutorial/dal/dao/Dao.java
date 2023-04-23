package at.fhtw.swen2.tutorial.dal.dao;

import at.fhtw.swen2.tutorial.model.Tour;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface Dao<T> {
    Flux<Tour> findAll(Optional<String> searchQuery);
    Mono<T> findById(Long id);
    Mono<T> update(T entity);
    Mono<Tour> save(T entity);
    Mono<Void> delete(T entity);
}
