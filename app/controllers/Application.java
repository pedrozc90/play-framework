package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.objects.HealthDto;
import controllers.objects.OrderDto;
import models.PurchaseOrder;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.PurchaseOrderService;
import utils.mappers.PurchaseOrderMapper;

public class Application extends Controller {

    private static final PurchaseOrderService service = PurchaseOrderService.getInstance();
    private static final PurchaseOrderMapper mapper = PurchaseOrderMapper.getInstance();

    public static Result index() {
        // return ok(index.render("Your application is ready."));
        return ok();
    }

    public static Result health() {
        final HealthDto dto = new HealthDto();
        final JsonNode response = Json.toJson(dto);
        return ok(response);
    }

    @Transactional
    public static Result order() {
        final JsonNode body = request().body().asJson();
        if (body == null) {
            return badRequest("Expecting Json data");
        }

        final OrderDto dto = Json.fromJson(body, OrderDto.class);
        if (dto == null) {
            return badRequest("Invalid Json data");
        }

        final PurchaseOrder purchaseOrder = service.create(dto.getNumber(), body.asText());

        final JsonNode response = Json.toJson(mapper.toDto(purchaseOrder));

        return created(response);
    }

}
