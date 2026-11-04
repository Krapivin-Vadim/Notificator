package org.vadim.spec;

import org.springframework.data.jpa.domain.Specification;
import org.vadim.entity.Reminder;

import java.time.Instant;

public final class ReminderSpecification {
    private ReminderSpecification(){}

    public static Specification<Reminder> belongTo(Long accountId){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("account").get("id"), accountId);
    }

    public static Specification<Reminder> titleContains(String title){
        if (title == null || title.isBlank()) return Specification.unrestricted();
        String pattern = "%" + title.toLowerCase() + "%";
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("title")),
                        pattern);
    }

    public static Specification<Reminder> notifyAfter(Instant from){
        if (from == null) return Specification.unrestricted();
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("notifyTime"), from);
    }

    public static Specification<Reminder> notifyBefore(Instant before){
        if (before == null) return Specification.unrestricted();
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("notifyTime"), before);
    }
}
