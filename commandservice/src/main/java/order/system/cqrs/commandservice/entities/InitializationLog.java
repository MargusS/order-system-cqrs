package order.system.cqrs.commandservice.entities;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "initialization_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitializationLog {

    /**
     * The unique name of the task. 
     * We use this as the ID to prevent duplicate entries for the same task.
     * Example: "INITIAL_SEED_DATA_V1"
     */
    @Id
    private String taskName;

    private Instant executedAt;
}
