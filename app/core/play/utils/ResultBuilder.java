package core.play.utils;

import com.fasterxml.jackson.databind.JsonNode;
import core.objects.ResultMessage;
import core.utils.http.HttpStatus;
import core.utils.validation.Violation;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;

import java.util.List;

public class ResultBuilder {

    /* ========= Entry points ========= */
    public static Start of() {
        return new Builder();
    }

    public static AfterPayload of(final Object content) {
        return new Builder().content(content);
    }

    public static AfterMessage of(final String message) {
        return new Builder().message(message);
    }

    public static AfterMessage of(final String message, final List<Violation> violations) {
        return new Builder()
            .message(message)
            .violations(violations);
    }

    /* ========= Steps ========= */

    /**
     * Initial state
     */
    public interface Start {

        AfterPayload content(final Object obj);

        AfterMessage message(final String message);

        AfterMessage message(final String fmt, final Object... args);

        TerminalStatus status(final int status);

        TerminalStatus status(final HttpStatus status);

        Result ok();

        Result created();

        Result notFound();

        Result notImplemented();

    }

    /**
     * After content()
     */
    public interface AfterPayload {

        TerminalStatus status(final int status);

        TerminalStatus status(final HttpStatus status);

        Result ok();

        Result created();

    }

    /**
     * After message()
     */
    public interface AfterMessage {

        AfterMessage violations(final List<Violation> violations);

        TerminalStatus status(final int status);

        TerminalStatus status(final HttpStatus status);

        Result ok();

        Result created();

        Result badRequest();

        Result unauthorized();

        Result forbidden();

        Result notFound();

        Result internalServerError();

        Result notImplemented();

    }

    /**
     * After status()
     */
    public interface TerminalStatus {

        Result toResult();

    }

    /* ========= Builder ========= */
    private static final class Builder implements Start, AfterPayload, AfterMessage, TerminalStatus {

        private Integer status;
        private Object content;
        private String message;
        private List<Violation> violations;

        /* ----- content ----- */

        @Override
        public AfterPayload content(final Object obj) {
            this.content = obj;
            return this;
        }

        /* ----- message ----- */

        @Override
        public AfterMessage message(final String message) {
            this.message = message;
            return this;
        }

        @Override
        public AfterMessage message(final String fmt, final Object... args) {
            return message(String.format(fmt, args));
        }

        @Override
        public AfterMessage violations(final List<Violation> violations) {
            this.violations = violations;
            return this;
        }

        /* ----- status ----- */

        @Override
        public TerminalStatus status(final int status) {
            this.status = status;
            return this;
        }

        @Override
        public TerminalStatus status(final HttpStatus status) {
            if (status != null) {
                this.status = status.getCode();
                if (message == null && content == null && violations == null) {
                    message = status.getMessage();
                }
            }
            return this;
        }

        /* ----- quick status ----- */

        @Override
        public Result ok() {
            return status(200).toResult();
        }

        @Override
        public Result created() {
            return status(201).toResult();
        }

        @Override
        public Result badRequest() {
            return status(400).toResult();
        }

        @Override
        public Result unauthorized() {
            return status(401).toResult();
        }

        @Override
        public Result forbidden() {
            return status(403).toResult();
        }

        @Override
        public Result notFound() {
            return status(404).toResult();
        }

        @Override
        public Result internalServerError() {
            return status(500).toResult();
        }

        @Override
        public Result notImplemented() {
            return status(501).toResult();
        }

        /* ----- terminal ----- */

        @Override
        public Result toResult() {
            final int _status = (status != null) ? status : 200;
            if (_status == 204) {
                return Results.status(204);
            }

            final JsonNode json = buildBody();
            if (json == null) {
                return Results.status(_status);
            }
            return Results.status(_status, json);
        }

        private JsonNode buildBody() {
            if (content != null && message == null && violations == null) {
                return Json.toJson(content);
            }

            if (message == null && violations == null) {
                return null;
            }

            final ResultMessage obj = new ResultMessage(message, violations);
            return Json.toJson(obj);
        }

    }

}
