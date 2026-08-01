package org.boxutil.backends.console;

import org.boxutil.config.BoxConfigs;
import org.jetbrains.annotations.NotNull;
import org.lazywizard.console.BaseCommand;

public class ShowAAStatus implements BaseCommand {
    public CommandResult runCommand(@NotNull String args, @NotNull CommandContext context) {
        BoxConfigs.setAAShowEdge(Boolean.parseBoolean(args.toUpperCase()));
        return CommandResult.SUCCESS;
    }
}
