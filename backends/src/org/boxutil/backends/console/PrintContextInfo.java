package org.boxutil.backends.console;

import org.boxutil.define.BoxDatabase;
import org.jetbrains.annotations.NotNull;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.Console;

public class PrintContextInfo implements BaseCommand {
    public CommandResult runCommand(@NotNull String args, @NotNull CommandContext context) {
        BoxDatabase.GLState state = BoxDatabase.getGLState();
        Console.showMessage(state.getPrintInfo());
        state.print();

        BoxDatabase.CLState clState = BoxDatabase.getCLState();
        if (clState != null) {
            Console.showMessage("\n--------------------------------\n\n" + clState.getPrintInfo());
            clState.print();
        }
        return CommandResult.SUCCESS;
    }
}
