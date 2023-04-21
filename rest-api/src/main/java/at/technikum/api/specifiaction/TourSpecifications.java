package at.technikum.api.specifiaction;

import at.technikum.api.model.Tour;
import org.springframework.data.jpa.domain.Specification;

public class TourSpecifications {
    private TourSpecifications() {
    }
    public static Specification<Tour> search(String query) {
        return (root, query1, criteriaBuilder) -> {
            if (query == null || query.trim().isEmpty()) {
                return null;
            }

            String pattern = "%" + query.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("from")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("to")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("transportType")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("distance").as(String.class)), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("estimatedTime").as(String.class)), pattern)
            );
        };
    }
}
