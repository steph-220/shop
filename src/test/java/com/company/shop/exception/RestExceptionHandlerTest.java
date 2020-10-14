package com.company.shop.exception;

import com.company.shop.model.ErrorResponse;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class RestExceptionHandlerTest {

    @InjectMocks
    private RestExceptionHandler restExceptionHandler;

    @Test
    void handleBadRequestException() {
        BadRequestException badRequestException = new BadRequestException(RandomString.make());

        ResponseEntity<ErrorResponse> errorResponse = restExceptionHandler.handleBadRequestException(badRequestException);

        assertNotNull(errorResponse.getBody());
        assertEquals(badRequestException.getMessage(), errorResponse.getBody().getErrorMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getBody().getErrorCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatusCode().value());
    }

    @Test
    void handleException() {
        Exception exception = new Exception(RandomString.make());

        ResponseEntity<ErrorResponse> errorResponse = restExceptionHandler.handleException(exception);

        assertNotNull(errorResponse.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorResponse.getBody().getErrorMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getBody().getErrorCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatusCode().value());
    }

    @Test
    void handleMethodArgumentNotValidException() {
        MethodArgumentNotValidException methodArgumentNotValidException = Mockito.mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.getFieldErrors()).thenReturn(
                Collections.singletonList(new FieldError("name", "name", "must not be blank")));
        Mockito.when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorResponse> errorResponse = restExceptionHandler.handleMethodArgumentNotValidException(methodArgumentNotValidException);

        assertNotNull(errorResponse.getBody());
        assertEquals(1, errorResponse.getBody().getValidationErrors().size());
        assertEquals("'name' must not be blank", errorResponse.getBody().getValidationErrors().get(0));
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getBody().getErrorCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatusCode().value());

    }
}