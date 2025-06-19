import java.io.Serializable;
import java.util.UUID;

public abstract class AbstractEntity implements Reportable, Serializable {
    private final UUID id = UUID.randomUUID();

    public UUID getId() {
        return id;
    }
}
