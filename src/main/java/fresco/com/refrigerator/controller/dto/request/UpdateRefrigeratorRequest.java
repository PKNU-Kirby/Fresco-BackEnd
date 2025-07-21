package fresco.com.refrigerator.controller.dto.request;

public record UpdateRefrigeratorRequest(
        Long refrigeratorId,
        String name
) {
}
