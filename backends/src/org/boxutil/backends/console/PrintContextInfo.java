package org.boxutil.backends.console;

import com.fs.starfarer.api.Global;
import org.boxutil.define.BoxDatabase;
import de.unkrig.commons.nullanalysis.NotNull;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.Console;

public class PrintContextInfo implements BaseCommand {
    public CommandResult runCommand(@NotNull String s, @NotNull BaseCommand.CommandContext commandContext) {
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
