package order.system.cqrs.commandservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import order.system.cqrs.commandservice.entities.InitializationLog;

public interface InitializationLogRepository extends JpaRepository<InitializationLog, String> {
}
