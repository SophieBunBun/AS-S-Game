package data;

import java.util.List;

public record PillRequest(
        int id,
        String clientName,
        String request,
        List<PlantEffect> effects
) {
}
