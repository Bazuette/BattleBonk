package libayon.command.commands;

import libayon.command.Command;

public class VClipCommand extends Command {

    public VClipCommand() {
        super("vclip", "vclip <number>", "Clip vertically");
    }

    @Override
    public String executeCommand(String line, String[] args) {
        if (args.length < 1)
            return getSyntax("&c");
        float y = Float.parseFloat(args[0]);
        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + y, mc.thePlayer.posZ);
        return String.format("Clipped player to " + y);
    }

}