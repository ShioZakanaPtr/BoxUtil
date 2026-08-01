package org.boxutil.backends.console;

import org.apache.log4j.Level;
import org.boxutil.backends.core.statictrail.BUtil_StaticTrailMemoryPool;
import org.boxutil.config.BoxConfigs;
import org.jetbrains.annotations.NotNull;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.Console;

public class ShowStaticTrailMemoryUsage implements BaseCommand {
    public CommandResult runCommand(@NotNull String args, @NotNull CommandContext context) {
        if (BUtil_StaticTrailMemoryPool.isNotSupported()) {
            Console.showMessage("'BoxUtil' static trail memory pool was not supported.", Level.WARN);
            return CommandResult.ERROR;
        }
        BoxConfigs.setShowStaticTrailMemoryUsage(Boolean.parseBoolean(args.toUpperCase()));
        return CommandResult.SUCCESS;
    }
}
