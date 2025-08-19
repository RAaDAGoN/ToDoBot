package com.bot.todo.command;

import com.bot.todo.service.SendBotMessageService;
import com.bot.todo.service.TaskService;
import com.bot.todo.service.UserService;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.bot.todo.command.CommandName.*;

@Getter
@Component
public class CommandContainer {
    private final ListTasksCommand listTasksCommand;

    private final Map<String, Command> commandMap = new HashMap<>();
    private final Command unknownCommand;

    public CommandContainer(SendBotMessageService sendBotMessageService,
                            TaskService taskService,
                            UserService userService) {
        this.listTasksCommand = new ListTasksCommand(sendBotMessageService, taskService);

        commandMap.put(START.getCommandName(), new StartCommand(sendBotMessageService));
        commandMap.put(HELP.getCommandName(), new HelpCommand(sendBotMessageService));
        commandMap.put(NO.getCommandName(), new NoCommand(sendBotMessageService));
        commandMap.put(LISTTASK.getCommandName(), listTasksCommand);
        commandMap.put(ADDTASK.getCommandName(), new AddTaskCommand(sendBotMessageService, taskService, userService));
        commandMap.put(SELECTTASK.getCommandName(), new SelectTaskCommand(taskService, sendBotMessageService));
        commandMap.put(EDITTASK.getCommandName(), new EditTaskCommand(taskService, sendBotMessageService));

        this.unknownCommand = new UnknownMessage(sendBotMessageService);
    }

    public Command retrieveCommand(String commandIdentifier){
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }


}
