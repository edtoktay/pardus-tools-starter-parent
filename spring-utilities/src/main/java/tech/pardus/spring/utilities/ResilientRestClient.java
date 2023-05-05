package tech.pardus.spring.utilities;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.retry.Retry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;

/**
 * @author deniz.toktay
 * @since May 5, 2021
 */
public class ResilientRestClient<RequestT, ResponseT> {

	private static final Logger log = LoggerFactory.getLogger(ResilientRestClient.class);

	private String errorMessage;

	private Supplier<Exception> supplierException = () -> {
		var rex = new ResilienceRetryException(errorMessage);
		return rex;
	};

	public ResponseT post(RequestT request, String url, Class<ResponseT> returnTypeClass, ResilientRetry config,
	        ResilienceErrorPersister errorHandler) throws Exception {
		var executor = Executors.newFixedThreadPool(config.getAttemptCount());
		try {
			var restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			CheckedFunction0<ResponseT> timeOutSupplier = () -> {
				Supplier<Future<ResponseT>> supplierz = () -> executor.submit(() -> {
					var header = new HttpHeaders();
					header.setContentType(MediaType.APPLICATION_JSON);
					header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
					var httpEntity = new HttpEntity<>(request, header);
					return restTemplate.postForObject(url, httpEntity, returnTypeClass);
				});
				Try<ResponseT> secondResult = Try.of(() -> config.timeLimiter().executeFutureSupplier(supplierz))
				        .onFailure(throwable -> {
					        log.error("POST Timeout in {} with {}", url, request.toString(), throwable);
				        });
				return secondResult.getOrElseThrow(supplierException);
			};
			CheckedFunction0<ResponseT> retryableSupplier = Retry.decorateCheckedSupplier(config.retry(),
			        timeOutSupplier);
			Try<ResponseT> result = Try.of(retryableSupplier).recover((throwable) -> {
				errorHandler.persistError(request.toString(), "Retry Failed", throwable);
				return null;
			});
			return result.getOrElseThrow(supplierException);
		} finally {
			executor.shutdown();
		}
	}

	public ResponseT get(String url, Class<ResponseT> returnTypeClass, ResilientRetry config,
	        ResilienceErrorPersister errorHandler) throws Exception {
		var executor = Executors.newFixedThreadPool(config.getAttemptCount());
		try {
			var restTemplate = new RestTemplate();
			CheckedFunction0<ResponseT> timeOutSupplier = () -> {
				Supplier<Future<ResponseT>> supplierz = () -> executor.submit(() -> {
					return restTemplate.getForObject(url, returnTypeClass);
				});
				Try<ResponseT> secondResult = Try.of(() -> config.timeLimiter().executeFutureSupplier(supplierz))
				        .onFailure(throwable -> {
					        log.error("GET Timeout in {} ", url, throwable);
				        });
				return secondResult.getOrElseThrow(supplierException);
			};
			CheckedFunction0<ResponseT> retryableSupplier = Retry.decorateCheckedSupplier(config.retry(),
			        timeOutSupplier);
			Try<ResponseT> result = Try.of(retryableSupplier).recover((throwable) -> {
				errorHandler.persistError(url, "Retry Failed", throwable);
				return null;
			});
			return result.getOrElseThrow(supplierException);
		} finally {
			executor.shutdown();
		}
	}

}
