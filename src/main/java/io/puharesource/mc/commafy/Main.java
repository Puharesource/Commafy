package io.puharesource.mc.commafy;

import com.comphenix.protocol.ProtocolLibrary;
import io.puharesource.mc.commafy.commands.CommandCommafy;
import io.puharesource.mc.commafy.protocol.listeners.ProtocolChatListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main extends JavaPlugin {
    public void onEnable() {
        loadConfig();

        ProtocolLibrary.getProtocolManager().addPacketListener(new ProtocolChatListener(this));
        getCommand("commafy").setExecutor(new CommandCommafy(this));
    }

    private void loadConfig() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        File config = new File(getDataFolder(), "config.yml");

        try {
            if (!config.exists()) Files.copy(getResource("config.yml"), config.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        getConfig();
        saveConfig();
    }
}
