package com.shimincraft.planvulcan.planvulcan;

import me.frep.vulcan.api.check.Check;
import me.frep.vulcan.api.event.VulcanPostFlagEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getScheduler;

public class VulcanListener implements Listener {
    private final VulcanStorage storage;
    private final Plugin plugin;

    private final HashMap<String, ViolationType> violationTypeHashMap = new HashMap<>();

    public VulcanListener(VulcanStorage storage, Plugin plugin) {
        this.storage = storage;
        this.plugin = plugin;
        violationTypeHashMap.put("combat", ViolationType.COMBAT_VIOLATION);
        violationTypeHashMap.put("movement", ViolationType.MOVEMENT_VIOLATION);
        violationTypeHashMap.put("autoclicker", ViolationType.AUTOCLICKER_VIOLATION);
        violationTypeHashMap.put("player", ViolationType.PLAYER_VIOLATION);
        violationTypeHashMap.put("timer", ViolationType.TIMER_VIOLATION);
        violationTypeHashMap.put("scaffold", ViolationType.SCAFFOLD_VIOLATION);
    }

    @EventHandler()
    public void onFlag(VulcanPostFlagEvent event) throws SQLException, ExecutionException, InterruptedException {
        storeViolationType(event.getCheck(), event.getPlayer());
    }

    private void storeViolationType(Check check, Player player) throws SQLException, ExecutionException, InterruptedException {
        ViolationType type = violationTypeHashMap.get(check.getCategory());
        if (type != null && player != null) {
            getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    storage.storeViolation(player.getUniqueId(), type, check.getVl());
                } catch (SQLException | ExecutionException | InterruptedException e) {
                    getLogger().severe("Failed to store violation: " + e.getMessage());
                }
            });
        } else {
            getLogger().warning("Unknown violation type: " + check.getCategory());
        }
    }
}
