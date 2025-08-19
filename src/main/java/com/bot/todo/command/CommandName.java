package com.bot.todo.command;

public enum CommandName {
    START("/start"),
    HELP("/help"),
    NO("noCommand"),
    ADDTASK("/addTask"),
    LISTTASK("/listtask"),
    SELECTTASK("/selectTask"),
    EDITTASK("/editTask");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
