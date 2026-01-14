package org.boxutil.backends.console;

import org.apache.log4j.Level;
import org.boxutil.backends.core.BUtil_InstanceDataMemoryPool;
import org.boxutil.config.BoxConfigs;
import de.unkrig.commons.nullanalysis.NotNull;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.Console;

public class ShowInstanceMemoryUsage implements BaseCommand {
    public CommandResult runCommand(@NotNull String s, @NotNull BaseCommand.CommandContext commandContext) {
        if (BUtil_InstanceDataMemoryPool.isNotSupported()) {
            Console.showMessage("'BoxUtil' instance memory pool was not supported.", Level.WARN);
            return CommandResult.ERROR;
        }
        BoxConfigs.setShowInstanceMemoryUsage(Boolean.parseBoolean(s.toUpperCase()));
        return CommandResult.SUCCESS;
    }
}
