package at.technikum.api.specifiaction;

import at.technikum.api.model.TourLog;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TourLogSpecifications {
    private TourLogSpecifications() {
    }
    public static Specification<TourLog> search(String searchQuery) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (searchQuery != null && !searchQuery.isEmpty()) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("date").as(String.class), "%" + searchQuery + "%"),
                        criteriaBuilder.like(root.get("comment").as(String.class), "%" + searchQuery + "%"),
                        criteriaBuilder.like(root.get("totalTime").as(String.class), "%" + searchQuery + "%"),
                        criteriaBuilder.like(root.get("difficulty").as(String.class), "%" + searchQuery + "%"),
                        criteriaBuilder.like(root.get("rating").as(String.class), "%" + searchQuery + "%")
                ));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
