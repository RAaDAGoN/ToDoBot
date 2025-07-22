package com.bot.todo.config;

import com.bot.todo.command.CommandContainer;
import com.bot.todo.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppConfig {
    @Bean
    public CommandContainer commandContainer(SendBotMessageService service,
                                             UserService userService,
                                             TaskService taskService){
        return new CommandContainer(service, taskService, userService);
    }
}
