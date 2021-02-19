package com.alysaa.geyserupdater.spigot.util;

import com.alysaa.geyserupdater.common.util.OSUtils;
import com.alysaa.geyserupdater.common.util.ScriptCreator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class CheckSpigotRestart {
    public static void checkYml() {
        FileConfiguration spigot = YamlConfiguration.loadConfiguration(new File(Bukkit.getServer().getWorldContainer(), "spigot.yml"));
        String scriptPath = spigot.getString("settings.restart-script");
        File script = new File(scriptPath);
        String scriptName;
        if (OSUtils.isWindows()) scriptName = "ServerRestartScript.bat";
        else if (OSUtils.isLinux() || OSUtils.isMac()) scriptName = "./ServerRestartScript.sh";
        else {
            System.out.println("Your OS is not supported for script checking!");
            return;
        }
        spigot = YamlConfiguration.loadConfiguration(new File(Bukkit.getServer().getWorldContainer(), "spigot.yml"));
        spigot.set("settings.restart-script", scriptName);
        try {
            spigot.save("spigot.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (script.exists())
            System.out.println("[GeyserUpdater] Has detected a restart script.");
        else {
            try {
                URI fileURI;
                fileURI = new URI(Bukkit.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                File jar = new File(fileURI.getPath());
                // Tell the createScript method the name of the server jar
                // and that a loop is not necessary because spigot has a restart system.
                ScriptCreator.createScript(jar.getName(), false);
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}