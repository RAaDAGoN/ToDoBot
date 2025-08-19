package com.bot.todo.config;

import com.bot.todo.command.CommandContainer;
import com.bot.todo.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public CommandContainer commandContainers(SendBotMessageService service,
                                             UserService userService,
                                             TaskService taskService){
        return new CommandContainer(service, taskService, userService);
    }
}
