package gg.leon.playermenu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class PlayerMenu extends JavaPlugin implements Listener, TabExecutor {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("profile").setExecutor(this);
        getLogger().info("PlayerMenuPlugin aktiviert!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PlayerMenuPlugin deaktiviert!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cNur Spieler können das nutzen!");
            return true;
        }

        Player player = (Player) sender;
        openProfileMenu(player);
        return true;
    }

    private void openProfileMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8» §bDein Profil");

        inv.setItem(10, createItem(Material.IRON_SWORD, "§aKills", "§7" + getKills(player)));
        inv.setItem(11, createItem(Material.SKELETON_SKULL, "§cTode", "§7" + getDeaths(player)));
        inv.setItem(12, createItem(Material.EXPERIENCE_BOTTLE, "§eLevel", "§7" + player.getLevel()));
        inv.setItem(13, createItem(Material.CLOCK, "§bSpielzeit", "§7" + getPlaytime(player)));

        inv.setItem(16, getPlayerHead(player));

        inv.setItem(22, createItem(Material.BARRIER, "§cSchließen", "§7Klicke, um das Menü zu schließen."));

        player.openInventory(inv);
    }

    private ItemStack createItem(Material mat, String name, String... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getPlayerHead(Player player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(player);
        meta.setDisplayName("§b" + player.getName());
        meta.setLore(List.of("§7Das bist du!"));
        skull.setItemMeta(meta);
        return skull;
    }

    private int getKills(Player player) {
        return player.getStatistic(Statistic.PLAYER_KILLS);
    }

    private int getDeaths(Player player) {
        return player.getStatistic(Statistic.DEATHS);
    }

    private String getPlaytime(Player player) {
        int ticks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        int hours = minutes / 60;
        return hours + "h " + (minutes % 60) + "m";
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("Profil")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;

            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem().getType() == Material.BARRIER) {
                player.closeInventory();
            }
        }
    }
}
