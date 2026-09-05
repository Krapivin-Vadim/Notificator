package org.vadim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vadim.entity.Reminder;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
}
