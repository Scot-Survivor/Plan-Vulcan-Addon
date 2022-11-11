package com.shimincraft.planvulcan.planvulcan;

import com.djrapitops.plan.extension.CallEvents;
import com.djrapitops.plan.extension.DataExtension;
import com.djrapitops.plan.extension.annotation.*;
import com.djrapitops.plan.extension.icon.Color;
import com.djrapitops.plan.extension.icon.Family;
import com.djrapitops.plan.extension.icon.Icon;
import com.djrapitops.plan.extension.table.Table;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@PluginInfo(
        name = "Vulcan Statistics",
        iconName = "vial",
        iconFamily = Family.SOLID,
        color = Color.RED
)
@TabInfo(
        tab = "Vulcan",
        iconName = "skull-crossbones",
        iconFamily = Family.SOLID,
        elementOrder = {}
)
public class VulcanData implements DataExtension {
    protected final VulcanStorage storage;

    public VulcanData(VulcanStorage storage) {
        this.storage = storage;
    }

    @Override
    public CallEvents[] callExtensionMethodsOn() {
        return new CallEvents[]{
                CallEvents.PLAYER_JOIN,
                CallEvents.PLAYER_LEAVE,
                CallEvents.SERVER_PERIODICAL
        };
    }

    @NumberProvider(
            text = "Number of Violations",
            description = "Number of violations, a player has",
            iconName = "bookmark",
            iconColor = Color.GREEN,
            priority = 10,
            showInPlayerTable = true
    )
    @Tab("Vulcan")
    public long getNumberOfViolations(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        assert player != null;
        return storage.getTotalViolations(player.getUniqueId());
    }

    @TableProvider(tableColor = Color.RED)
    @Tab("Vulcan")
    public Table topViolationsProvider() {
        Table.Factory table = Table.builder()
                .columnOne("Users", Icon.called("users").build())
                .columnTwo("Violations", Icon.called("signal").build());

        storage.getTotalViolationCounts().forEach(table::addRow);
        return table.build();
    }

    @TableProvider(tableColor = Color.GREY)
    @Tab("Vulcan")
    public Table violationTotalTypesProvider() {
        Table.Factory table = Table.builder()
                .columnOne("Violation Type", Icon.called("users").build())
                .columnTwo("Count", Icon.called("target").build());
        table.addRow("Combat", storage.getSumViolation("vulcan_combat_violations"));
        table.addRow("Movement", storage.getSumViolation("vulcan_movement_violations"));
        table.addRow("Player", storage.getSumViolation("vulcan_player_violations"));
        return table.build();
    }
}
