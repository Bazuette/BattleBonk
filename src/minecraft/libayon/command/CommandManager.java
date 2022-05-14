package libayon.command;

import libayon.BattleBonk;
import libayon.command.commands.*;
import libayon.event.EventListener;
import libayon.event.events.client.KeyPressedEvent;
import libayon.event.events.player.MessageSentEvent;
import libayon.irc.IRCClient;
import libayon.utils.Lists;
import libayon.utils.player.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager extends EventListener {

    private final List<Command> commands;

    private final String trigger;

    private final Minecraft mc;

    public CommandManager(String trigger) {
        this.commands = new ArrayList<Command>();

        this.trigger = trigger;

        this.mc = Minecraft.getMinecraft();

        BattleBonk.instance.getEventManager().registerListener(this);
    }

    public String getTrigger() {
        return trigger;
    }

    public void registerCommand(Command command) {
        commands.add(command);
    }

    public void registerCommands() {
        registerCommand(new ConfigCommand());
        registerCommand(new HelpCommand());
        registerCommand(new IRCCommand());
        registerCommand(new BindCommand());
        registerCommand(new XRayCommand());
        registerCommand(new FontCommand());
        registerCommand(new ToggleCommand());
        registerCommand(new VClipCommand());
    }

    public List<Command> getCommands() {
        return commands;
    }

    public String getHelpMessage(String command) {
        return String.format("&cThe command &e%1$s&c does not exist.", command);
    }

    /**
     * @param name Command's name or alias
     * @return a command which name's is @param name, or has @param name as alias
     */
    public Command getCommand(String name) {
        for (int i = 0; i < commands.size(); i++) {
            Command command = commands.get(i);
            if (command.getName().equalsIgnoreCase(name) || Arrays.stream(command.getAliases()).anyMatch(name::equalsIgnoreCase))
                return command;
        }

        return null;
    }

    /**
     * @param clasz the command's class
     * @return a command which class is @param clasz
     */
    public Command getCommand(Class<? extends Command> clasz) {
        for (int i = 0; i < commands.size(); i++) {
            Command command = commands.get(i);
            if (command.getClass().equals(clasz))
                return command;
        }

        return null;
    }

    @Override
    public void onMessageSent(MessageSentEvent event) {
        if (event.isCancelled())
            return;

        String[] args = event.getMessage().split(" (?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
        args = Arrays.stream(args).map(s -> s.replace("\"", "")).toArray(size -> new String[size]);

        String line = Lists.stringArrayToString(" ", args);
        String commandLine = Lists.stringArrayToString(" ", args);
        String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);

        if (!(args[0].startsWith(trigger))) {
            if (args[0].startsWith("@")) {
                IRCClient ircClient = BattleBonk.instance.getIRCClient();
                if (ircClient != null && ircClient.isActive()) {
                    try {
                        ircClient.sendMessage(ircClient.getChannel(), commandLine.substring(1));
                        PlayerUtils.sendMessage(String.format("%1$s&6YOU &7(&e&o%2$s&7)&7: &e%3$s", ircClient.getPrefix(), ircClient.getUsername(), commandLine.substring(1)));
                    } catch (IOException e) {
                        e.printStackTrace();
                        PlayerUtils.sendMessage(String.format("%1$sCan't send the chat message. (%2$s)", ircClient.getPrefix(), e.getMessage()));
                    }
                    event.setCancelled(true);
                }
            }
            return;
        }

        String name = args[0].substring(1);

        event.setCancelled(true);

        Command command = getCommand(name);

        String result = getHelpMessage(args[0]);

        if (command == null)
            ;
        else
            result = command.executeCommand(line, commandArgs);

        if (result != null && !(result.trim().isEmpty()))
            PlayerUtils.sendMessage(result);
    }

    /**
     * This will open the chat with the trigger when the period key is pressed.
     */
    @Override
    public void onKeyPressed(KeyPressedEvent event) {
        if (event.getKey() == Keyboard.KEY_PERIOD) {
            mc.displayGuiScreen(new GuiChat(trigger));
        } else if (event.getKey() == Keyboard.KEY_AT) {
            mc.displayGuiScreen(new GuiChat("@"));
        }
    }

}