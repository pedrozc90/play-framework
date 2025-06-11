package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.objects.HealthDto;
import controllers.objects.OrderDto;
import controllers.objects.RequestDto;
import mappers.OrderMapper;
import mappers.PurchaseOrderMapper;
import models.Order;
import models.PurchaseOrder;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.OrderService;
import services.PurchaseOrderService;

public class Application extends Controller {

    private static final OrderService orderService = OrderService.getInstance();
    private static final PurchaseOrderService purchaseOrderService = PurchaseOrderService.getInstance();
    private static final OrderMapper orderMapper = OrderMapper.getInstance();
    private static final PurchaseOrderMapper purchaseOrderMapper = PurchaseOrderMapper.getInstance();

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
    public static Result getOrder(final Long id) {
        final Order order = orderService.get(id);
        if (order == null) {
            return notFound("Order not found");
        }

        final OrderDto dto = orderMapper.toDto(order);
        final JsonNode response = Json.toJson(dto);
        return ok(response);
    }

    @Transactional
    public static Result order() {
        final JsonNode body = request().body().asJson();
        if (body == null) {
            return badRequest("Expecting Json data");
        }

        final RequestDto dto = Json.fromJson(body, RequestDto.class);
        if (dto == null) {
            return badRequest("Invalid Json data");
        }

        final PurchaseOrder purchaseOrder = purchaseOrderService.create(dto.getNumber(), body.toPrettyString());

        final JsonNode response = Json.toJson(purchaseOrderMapper.toDto(purchaseOrder));

        return created(response);
    }

}
