package com.bot.todo.command;

public enum CommandName {
    START("/start"),
    HELP("/help"),
    NO("nocommand"),
    ADDTASK("/addtask"),
    LISTTASK("/listtask"),
    SELECTTASK("/selecttask"),
    EDITTASK("/edittask");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
