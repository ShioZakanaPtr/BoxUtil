package org.boxutil.backends.console;

import de.unkrig.commons.nullanalysis.NotNull;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxEnum;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.Console;

public class ShowMultiPassChannel implements BaseCommand {
    public CommandResult runCommand(@NotNull String s, @NotNull BaseCommand.CommandContext commandContext) {
        if (s.isEmpty()) {
            BoxConfigs.setMultiPassMode(BoxEnum.MP_BEAUTY);
        } else {
            String upCase = s.toUpperCase();
            if (upCase.contentEquals("BEAUTY")) BoxConfigs.setMultiPassMode(BoxEnum.MP_BEAUTY);
            else if (upCase.contentEquals("COLOR")) BoxConfigs.setMultiPassMode(BoxEnum.MP_COLOR);
            else if (upCase.contentEquals("EMISSIVE")) BoxConfigs.setMultiPassMode(BoxEnum.MP_EMISSIVE);
            else if (upCase.contentEquals("POSITION")) BoxConfigs.setMultiPassMode(BoxEnum.MP_POSITION);
            else if (upCase.contentEquals("NORMAL")) BoxConfigs.setMultiPassMode(BoxEnum.MP_NORMAL);
            else if (upCase.contentEquals("TANGENT")) BoxConfigs.setMultiPassMode(BoxEnum.MP_TANGENT);
            else if (upCase.contentEquals("MATERIAL")) BoxConfigs.setMultiPassMode(BoxEnum.MP_MATERIAL);
            else if (upCase.contentEquals("BLOOM")) BoxConfigs.setMultiPassMode(BoxEnum.MP_BLOOM);
            else {
                Console.showMessage("Error: no such channel '" + s + "'! Valid channel: [\n\t'none'\n\tBEAUTY\n\tCOLOR\n\tEMISSIVE\n\tPOSITION\n\tNORMAL\n\tTANGENT\n\tMATERIAL\n\tBLOOM\n].");
                return CommandResult.ERROR;
            }
        }
        return CommandResult.SUCCESS;
    }
}
