package org.boxutil.backends.console;

import org.boxutil.config.BoxConfigs;
import de.unkrig.commons.nullanalysis.NotNull;
import org.lazywizard.console.BaseCommand;

public class ShowAAStatus implements BaseCommand {
    public CommandResult runCommand(@NotNull String s, @NotNull BaseCommand.CommandContext commandContext) {
        BoxConfigs.setAAShowEdge(Boolean.parseBoolean(s.toUpperCase()));
        return CommandResult.SUCCESS;
    }
}
