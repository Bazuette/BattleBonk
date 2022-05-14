package libayon;

import libayon.account.AccountManager;
import libayon.command.CommandManager;
import libayon.event.EventManager;
import libayon.font.Font;
import libayon.gui.altmanager.GuiAltManager;
import libayon.gui.clickgui.ClickGui;
import libayon.irc.IRCClient;
import libayon.module.ModuleManager;
import libayon.overlay.Overlay;
import libayon.overlay.ToggledModules1;
import libayon.thealtening.AltService;
import libayon.utils.Strings;
import libayon.utils.config.Files;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Locale;
import org.lwjgl.opengl.Display;
import org.simpleyaml.configuration.file.YamlFile;
import viamcp.ViaMCP;

import java.io.File;
import java.io.IOException;

public class BattleBonk {

    public static BattleBonk instance;
    /**
     * @formatter:off Credits
     * <p>
     * Fonts: Slick's font manager edited by Russian412 and color system by me
     * Alt Manager: Russian412's Alt Manager with some small bug-fixes by me
     * The Altening Implementation: Russian412
     * <p>
     * Everything else is made by me
     * @formatter:on
     **/

    private final String clientName = "BattleBonk";
    private final String clientVersion = "1.86";
    private final String author = "Isaiah";
    private final String defaultUsername = "Isaiah";
    private final String packageBase = "libayon";
    private final boolean defaultHotbar = false;
    public YamlFile BattleBonkYaml = new YamlFile(getClientName() + "/battlebonk.yml");
    public YamlFile configYaml = new YamlFile(getClientName() + "/configs/default.yml");
    private EventManager eventManager;
    private CommandManager commandManager;
    private ModuleManager moduleManager;
    private IRCClient ircClient;
    private AccountManager accountManager;
    private AltService altService;
    private Font font;
    private Font US;
    private Font Vegan;
    private Font Roboto;
    private ClickGui clickGui;
    private Locale englishLocale;

    public BattleBonk() {
        instance = this;
    }

    public void initialize() {
        try {
            ViaMCP.getInstance().start();
            ViaMCP.getInstance().initAsyncSlider();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupClientConfig();
        setupConfigurations();
        loadExistingConfigs();

        Display.setTitle(String.format("%1$s - %2$s | Loading...", clientName, clientVersion));
        Minecraft.getMinecraft().gameSettings.fullScreen = true;

        this.englishLocale = new Locale();

        this.ircClient = new IRCClient("chat.freenode.net", 6667, Minecraft.getMinecraft().getSession().getUsername(), "#WaveLengthBaseClient");

        new GuiAltManager(); // We create the instance.

        String clientFolder = new File(".").getAbsolutePath();

        clientFolder = (clientFolder.contains("jars") ? new File(".").getAbsolutePath().substring(0, clientFolder.length() - 2) : new File(".").getAbsolutePath()) + Strings.getSplitter() + clientName;

        String accountManagerFolder = clientFolder + Strings.getSplitter() + "alts";

        Files.createRecursiveFolder(accountManagerFolder);

        this.accountManager = new AccountManager(new File(accountManagerFolder));

        this.clickGui = new ClickGui();

        this.eventManager = new EventManager();

        this.moduleManager = new ModuleManager();
        this.commandManager = new CommandManager(".");

        commandManager.registerCommands(); // Moved here to make sure the CommandManager instance is created, else the
        // "commandManager" variable in the Command class would be null (since we are
        // getting the CommandManager instance from this class)

        this.altService = new AltService();

        switchToMojang();

        /** Setting a custom icon */

        /** Both 16x16 and 32x32 version encoded in Base64 */
        String icon16x16 = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAACgUlEQVQ4y63ST0iTcRgH8O/v3V62d+803dJt2A5jbQymC82IEs0M8lDdCjrmxSCiHTpJIEQkInRoEEHHPEiO6hBDYaKFVhuWomy6uflnsU3Z61ube6fb3unbSbGVnXxOD1++fOD34wGOezLxqHZ9aa5eiH4/yIpbPInNTp3dSm+qy/uy8kDLMoMjr5zPxYrT2pfPnsbudnUZRzyevveu7hep7Ur2w6h39HBfXg7wyaiJLgmU//XDB54hc7e0V5IZyRqtpAjSqbitvE+VB6qK6q2TrIQEt4eeKyvK+xdjdJKToD8ByGWKzf8CAveDEYSi6oKN4No5ApOOoMFEobWBwF4HbIulqtxGjD4S2MmLllT0c5OxhuDtXBWmVyl8WqQxElTDUkewPDPRkS/mDUcCS0uRS5W7nNzHOXCnbQeZjBzZrAy3W0uYSTei9HOZSW5sNh0JhObnrba6Glx/5IXs8iji9Hn80nTixI1JdDjfod6gxtrKquWfQJGPUzPT3zq94ZIYdD+GvfEMPoar8SXKwGY3I+h+gskEW5iamLh5GCD7y+LCwtXEeqLJMzR475S4YHTc6gHF1COb5cBSScy+6UPW0B5oa29zG01Wr81e//UAkCSJ6u/vTzU3N2tFUYQkSVCpVBgYGIDT6QRN0ygUCuA4DmazGePj4yu9vb3mP57AsiylVqshCALcbjd8Ph8MBgMCgQA0Gg0ikQiUSiX0ej0Yhjm44P1LlFKpVHJ4eJjleb6UTqcpv98va2lpoQAQl8slWa1WhEIhjI2NSbW1tWt//QHP85WEEEUwGNy1WCwyQghVKBRYuVxOwuFw3uFwKADs5nK5vEKhyOh0ujyOY34DP9cFwaB7rNEAAAAASUVORK5CYII=";
        String icon32x32 = "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";

        /** Calling the #setWindowIcon() method with the two encoded icons */
        Minecraft.getMinecraft().setWindowIcon(icon16x16, icon32x32);
    }

    public void setupClientConfig() {
        try {
            if (!BattleBonkYaml.exists()) {
                BattleBonkYaml.createNewFile();
                System.out.println("New file has been created: " + BattleBonkYaml.getFilePath() + "\n");
            } else {
                System.out.println(BattleBonkYaml.getFilePath() + " already exists, loading configurations...\n");
            }
            BattleBonkYaml.load(); // Loads the entire file
            // If your file has comments inside you have to load it with yamlFile.loadWithComments()
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void loadExistingConfigs() {
        if (!(BattleBonkYaml.getString("Current Config") == null)) {
            try {
                configYaml = YamlFile.loadConfiguration(new File(BattleBonkYaml.getString("Current Config")));
            } catch (IOException e) {
                BattleBonkYaml.set("Current Config", configYaml.getFilePath());
            }
        } else {
            BattleBonkYaml.set("Current Config", configYaml.getFilePath());
            saveBattleBonkConfig();
        }
    }

    public void setupConfigurations() {
        try {
            if (!configYaml.exists()) {
                configYaml.createNewFile();
                System.out.println("New file has been created: " + configYaml.getFilePath() + "\n");
            } else {
                System.out.println(configYaml.getFilePath() + " already exists, loading configurations...\n");
            }
            configYaml.load(); // Loads the entire file
            // If your file has comments inside you have to load it with yamlFile.loadWithComments()
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }


    public void afterMinecraft() {
        Display.setTitle(String.format("%1$s - %2$s", clientName, clientVersion));

        this.font = new Font(packageBase + ".font.fonts", "BwModelicaSS01-RegularCondensed", 50, 25, 30, 33);
        this.US = new Font(packageBase + ".font.fonts", "US", 50, 25, 30, 33);
        this.Vegan = new Font(packageBase + ".font.fonts", "Vegan", 50, 25, 30, 33);
        this.Roboto = new Font(packageBase + ".font.fonts", "Roboto", 50, 25, 30, 33);
        registerHuds();
        BattleBonk.instance.getFontRenderer().setFontSizeNormal(20);
    }

    private void registerHuds() {
        new Overlay();
        new ToggledModules1();
    }

    public String getClientName() {
        return clientName;
    }

    public YamlFile getBattleBonkYaml() {
        return BattleBonkYaml;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public String getAuthor() {
        return author;
    }

    public String getDefaultUsername() {
        return defaultUsername;
    }

    public EventManager getEventManager() {
        return eventManager;
    }


    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public IRCClient getIRCClient() {
        return ircClient;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public AltService getAltService() {
        return altService;
    }

    public YamlFile getConfigYaml() {
        return configYaml;
    }

    public void saveConfig() {
        try {
            configYaml.save();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBattleBonkConfig() {
        try {
            BattleBonkYaml.save();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public Font getVeganFont() {
        return Vegan;
    }

    public Font getUSFont() {
        return US;
    }

    public Font getRobotoFont() {
        return Roboto;
    }

    @Deprecated
    public Font getFontRenderer() {
        return font;
    }

    public Font getFont() {
        return font;
    }

    public String getPackageBase() {
        return packageBase;
    }

    public boolean isDefaultHotbar() {
        return defaultHotbar;
    }


    public ClickGui getClickGui() {
        return clickGui;
    }

    public Locale getEnglishLocale() {
        return englishLocale;
    }

    public void switchToMojang() {
        try {
            this.altService.switchService(AltService.EnumAltService.MOJANG);
        } catch (NoSuchFieldException e) {
            System.out.println("Couldn't switch to modank altservice");
        } catch (IllegalAccessException e) {
            System.out.println("Couldn't switch to modank altservice -2");
        }
    }

    public void switchToTheAltening() {
        try {
            this.altService.switchService(AltService.EnumAltService.THEALTENING);
        } catch (NoSuchFieldException e) {
            System.out.println("Couldn't switch to altening altservice");
        } catch (IllegalAccessException e) {
            System.out.println("Couldn't switch to altening altservice -2");
        }
    }

}