package com.personal.movie.exception;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

@Slf4j
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
  @Override
  public void handleUncaughtException(Throwable ex, Method method, Object... params) {
    log.error("비동기 작업 중 예외 발생: " + method.getName(), ex);
  }
}
