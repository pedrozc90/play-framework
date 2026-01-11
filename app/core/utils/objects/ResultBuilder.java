package core.utils.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;


public class ResultBuilder {

    public static StatusStep of(final String message) {
        return new Builder().of(message);
    }

    public static StatusStep of(final String fmt, final Object... args) {
        return new Builder().of(fmt, args);
    }

    public static StatusStep of(final Object obj) {
        return new Builder().of(obj);
    }

    public interface StatusStep {

        Result status(final int status);

        Result ok();

        Result created();

        Result noContent();

        Result badRequest();

        Result unauthorized();

        Result forbidden();

        Result notFound();

        Result notAcceptable();

        Result internalServerError();

    }

    public interface ContentStep {

        StatusStep of(final Object obj);

        StatusStep of(final String message);

        StatusStep of(final String fmt, final Object... args);

    }

    private static class Builder implements ContentStep, StatusStep {

        private int status;
        private JsonNode body;

        @Override
        public StatusStep of(final Object obj) {
            this.body = (obj != null) ? Json.toJson(obj) : null;
            return this;
        }

        @Override
        public StatusStep of(final String message) {
            final ResultMessage obj = new ResultMessage(message);
            return of(obj);
        }

        @Override
        public StatusStep of(final String fmt, final Object... args) {
            return of(String.format(fmt, args));
        }

        @Override
        public Result status(int status) {
            return Results.status(status, body);
        }

        @Override
        public Result ok() {
            return Results.ok(body); // 200
        }

        @Override
        public Result created() {
            return Results.created(body); // 201
        }

        @Override
        public Result noContent() {
            return Results.notFound(); // 204
        }

        @Override
        public Result badRequest() {
            return Results.badRequest(body); // 400
        }

        @Override
        public Result unauthorized() {
            return Results.unauthorized(body); // 401
        }

        @Override
        public Result forbidden() {
            return Results.forbidden(body); // 403
        }

        @Override
        public Result notFound() {
            return Results.notFound(body); // 404
        }

        @Override
        public Result notAcceptable() {
            return status(409);
        }

        @Override
        public Result internalServerError() {
            return Results.internalServerError(body);
        }

    }

    private static class ResultMessage {

        @JsonProperty(value = "message")
        private String message;

        public ResultMessage(final String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

    }

}
