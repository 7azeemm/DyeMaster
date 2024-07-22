package me.dyemaster.guis.menus;

import me.dyemaster.guis.*;
import me.dyemaster.managers.DyeManager;
import me.dyemaster.models.Dye;
import me.dyemaster.models.Recipe;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.dyemaster.DyeMaster.getDyes;
import static me.dyemaster.DyeMaster.getPlugin;
import static me.dyemaster.storage.Actions.editDye;
import static me.dyemaster.storage.Actions.removeDye;
import static me.dyemaster.utils.CustomSkull.createPlayerHead;
import static me.dyemaster.utils.DyeUtils.saveID;
import static me.dyemaster.utils.RecipeUtils.registerRecipe;
import static me.dyemaster.utils.RecipeUtils.unregisterRecipe;

public class DyeOptionsMenu extends Menu {

    public DyeOptionsMenu(DyeManager manager) {
        super(manager);

        Dye dye = manager.getDye();
        manager.setSettings(null);
        manager.setAction(null);

        this.setTitle(net.md_5.bungee.api.ChatColor.of(dye.getSettings().getColor()) + dye.getSettings().getName());
        this.setSize(5 * 9);
        this.setFill(new Fill(Fill.Style.ALL, new int[]{40, 13, 20, 21, 23, 24}));
        this.setBackSlot(40);
        this.setParent(new Parent(HubMenu.class, new Parameter(DyeManager.class, manager)));

        //info button
        this.addButton(new Button(13) {
            @Override
            public ItemStack getItem() {
                ItemStack item = new ItemStack(Material.REDSTONE_TORCH);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Info");
                item.setItemMeta(meta);
                return item;
            }

            @Override
            public void onClick(Player player) {

            }
        });

        //give button
        this.addButton(new Button(20) {
            @Override
            public ItemStack getItem() {
                ItemStack item = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Give");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Gives dye to players.");
                lore.add(" ");
                lore.add(ChatColor.YELLOW + "Click to select a player!");
                meta.setLore(lore);
                item.setItemMeta(meta);
                return item;
            }

            @Override
            public void onClick(Player player) {
                new PlayerListMenu(manager).displayTo(player);
            }
        });

        //edit button
        this.addButton(new Button(21) {
            @Override
            public ItemStack getItem() {
                ItemStack item = new ItemStack(Material.REDSTONE);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Edit");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Edits dye settings.");
                lore.add(" ");
                lore.add(ChatColor.YELLOW + "Click to edit settings!");
                meta.setLore(lore);
                item.setItemMeta(meta);
                return item;
            }

            @Override
            public void onClick(Player player) {
                try {
                    manager.setSettings(dye.getSettings().clone());
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                manager.setAction(DyeManager.Action.EDIT);
                CreationMenu menu = new CreationMenu(manager);
                menu.displayTo(player);
            }
        });

        //toggle button
        this.addButton(new Button(23) {
            @Override
            public ItemStack getItem() {
                ItemStack item = new ItemStack(Material.REPEATER);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Toggle");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Toggles crafting recipe");
                lore.add(ChatColor.GRAY + "status.");
                lore.add(" ");
                lore.add(ChatColor.YELLOW + "Click to toggle!");
                meta.setLore(lore);
                item.setItemMeta(meta);
                return item;
            }

            @Override
            public void onClick(Player player) {
                ItemStack item = player.getOpenInventory().getTopInventory().getItem(13);
                Dye dye = manager.getDye();
                if (dye == null) {
                    System.out.println(ChatColor.RED + "Dye is not found or deleted.");
                    player.closeInventory();
                    return;
                }
                Boolean toggle = dye.getToggle();
                dye.setToggle(!toggle);
                toggle = !toggle;

                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                if (lore != null) {
                    if (toggle) {
                        lore.set(1, ChatColor.GRAY + "Status: " + ChatColor.GREEN + "Enabled");
                    } else lore.set(1, ChatColor.GRAY + "Status: " + ChatColor.RED + "Disabled");
                }

                meta.setLore(lore);
                item.setItemMeta(meta);

                editDye(dye.getId(), toggle, null);

                if (toggle) {
                    if (dye.getSettings().getRecipe() != null) {
                        registerRecipe(dye);
                    }
                } else {
                    unregisterRecipe(new NamespacedKey(getPlugin(), dye.getId().toString()));
                }
            }
        });

        //delete button
        this.addButton(new Button(24) {
            @Override
            public ItemStack getItem() {
                ItemStack item = new ItemStack(Material.CAULDRON);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Delete dye");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Deletes the dye.");
                lore.add(ChatColor.RED + "Note: You can't revert this action!!!");
                lore.add(" ");
                lore.add(ChatColor.YELLOW + "Click to delete!");
                meta.setLore(lore);
                item.setItemMeta(meta);
                return item;
            }

            @Override
            public void onClick(Player player) {
                Dye dye = manager.getDye();
                String name = dye.getSettings().getName();
                String color = dye.getSettings().getColor();

                unregisterRecipe(new NamespacedKey(getPlugin(), dye.getId().toString()));
                removeDye(dye);
                getDyes().remove(dye);

                player.sendMessage(net.md_5.bungee.api.ChatColor.of(color) + name + ChatColor.GREEN + " has been deleted successfully");

                new HubMenu(manager).displayTo(player);
            }
        });

    }

    @Override
    public void onOpen(Inventory inventory) {
        ItemStack item = inventory.getItem(13);
        Dye dye = getManager().getDye();
        Boolean toggle = dye.getToggle();
        String name = dye.getSettings().getName();
        String color = dye.getSettings().getColor();
        UUID creator = dye.getCreator();
        String date = dye.getCreatedDate();
        Recipe recipe = dye.getSettings().getRecipe();

        Player player = getPlugin().getServer().getPlayer(creator);
        ItemMeta meta = item.getItemMeta();

        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");
        if (toggle) {
            lore.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "Enabled");
        } else lore.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "Disabled");
        lore.add(ChatColor.GRAY + "Name: " + ChatColor.WHITE + name);
        lore.add(ChatColor.GRAY + "Color: " + net.md_5.bungee.api.ChatColor.of(color) + color);
        if (recipe != null) {
            lore.add(ChatColor.GRAY + "Recipe: " + ChatColor.GREEN + "Set");
        } else lore.add(ChatColor.GRAY + "Recipe: " + ChatColor.RED + "Not Set");
        if (player != null) {
            lore.add(ChatColor.GRAY + "Creator: " + ChatColor.WHITE + player.getDisplayName());
        } else lore.add(ChatColor.GRAY + "Creator: " + ChatColor.RED + "not found");
        lore.add(ChatColor.GRAY + "CreationDate: " + ChatColor.WHITE + date);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public class PlayerListMenu extends Menu {

        public PlayerListMenu(DyeManager manager) {
            super(manager);

            this.setTitle("Select a player");
            this.setSize(5 * 9);
            this.setBackSlot(40);
            this.setParent(new Parent(HubMenu.class, new Parameter(DyeManager.class, manager)));

            int index = 0;
            for (Player player : getPlugin().getServer().getOnlinePlayers()) {
                if (index == 4) break;
                this.addButton(new Button(index) {
                    @Override
                    public ItemStack getItem() {
                        ItemStack head = createPlayerHead(player);
                        saveID(head, player.getUniqueId());
                        this.setId(player.getUniqueId());
                        return head;
                    }

                    @Override
                    public void onClick(Player player) {
                        Dye dye = getManager().getDye();
                        player.closeInventory();

                        UUID ID = this.getId();
                        Player p = getPlugin().getServer().getPlayer(ID);
                        if (p == null) {
                            player.sendMessage(net.md_5.bungee.api.ChatColor.RED + "player is invalid");
                            return;
                        }

                        if (dye == null) {
                            p.sendMessage(ChatColor.RED + "Dye is not found or deleted.");
                            p.closeInventory();
                        }

                        dye.giveTo(player);

                        player.sendMessage(net.md_5.bungee.api.ChatColor.of(dye.getSettings().getColor()) + dye.getSettings().getName() + ChatColor.GREEN + " has been given to " + ChatColor.GOLD + player.getDisplayName());

                    }
                });
                index++;
            }
        }
    }
}
