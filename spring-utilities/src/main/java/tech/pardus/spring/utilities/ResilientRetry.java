package tech.pardus.spring.utilities;

import java.io.Serializable;
import java.time.Duration;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

/**
 * @author deniz.toktay
 * @since May 5, 2021
 */
public class ResilientRetry implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private int timeOutDuration;

	private int attemptCount;

	private int attemptWaitDuration;

	private String retryName;

	private TimeLimiter tm = null;

	private Retry rt = null;

	public ResilientRetry() {
	}

	public ResilientRetry(int timeOutDuration, int attemptCount, int attemptWaitDuration, String retryName) {
		this.timeOutDuration = timeOutDuration;
		this.attemptCount = attemptCount;
		this.attemptWaitDuration = attemptWaitDuration;
		this.retryName = retryName;
	}

	public TimeLimiterConfig timeLimiterConfig() {
		return TimeLimiterConfig.custom().cancelRunningFuture(true).timeoutDuration(Duration.ofMillis(timeOutDuration))
		        .build();
	}

	public RetryConfig retryConfig() {
		return RetryConfig.custom().maxAttempts(attemptCount).waitDuration(Duration.ofMillis(attemptWaitDuration))
		        .build();
	}

	public TimeLimiter timeLimiter() {
		if (tm == null) {
			tm = TimeLimiter.of(timeLimiterConfig());
		}
		return tm;
	}

	public Retry retry() {
		if (rt == null) {
			rt = Retry.of(retryName, retryConfig());
		}
		return rt;
	}

	public int getTimeOutDuration() {
		return timeOutDuration;
	}

	public void setTimeOutDuration(int timeOutDuration) {
		this.timeOutDuration = timeOutDuration;
	}

	public int getAttemptCount() {
		return attemptCount;
	}

	public void setAttemptCount(int attemptCount) {
		this.attemptCount = attemptCount;
	}

	public int getAttemptWaitDuration() {
		return attemptWaitDuration;
	}

	public void setAttemptWaitDuration(int attemptWaitDuration) {
		this.attemptWaitDuration = attemptWaitDuration;
	}

	public String getRetryName() {
		return retryName;
	}

	public void setRetryName(String retryName) {
		this.retryName = retryName;
	}

}
