package ru.studiotg.minesweeper.util.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        StringBuilder msg = new StringBuilder();
        msg.append("Exception: ").append(ex.getMessage()).append("\n");
        msg.append("Method: ").append(method.getName()).append("\n");
        for(Object param : params) {
            msg.append("Param: ").append(param).append("\n");
        }
        log.error(msg.toString());
    }
}
