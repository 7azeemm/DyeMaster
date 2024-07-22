package me.dyemaster.guis.menus;

import me.dyemaster.DyeMaster;
import me.dyemaster.guis.Button;
import me.dyemaster.guis.Menu;
import me.dyemaster.guis.Parameter;
import me.dyemaster.guis.Parent;
import me.dyemaster.managers.ConfigManager;
import me.dyemaster.managers.DyeManager;
import me.dyemaster.models.Dye;
import me.dyemaster.models.Settings;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static me.dyemaster.DyeMaster.getDyes;
import static me.dyemaster.DyeMaster.getPlugin;
import static me.dyemaster.utils.RecipeUtils.reloadRecipes;

public class HubMenu extends Menu {

    final int[] ITEM_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    public HubMenu(DyeManager manager) {
        super(manager);

        if (getDyes().isEmpty()) {
            this.setTitle("Dye Master");
        } else {
            int pages = (int) Math.ceil((double) getDyes().size() / (double) ITEM_SLOTS.length);
            if (pages == 1) {
                this.setTitle("Dye Master");
            } else this.setTitle("Dye Master (1/" + pages + ")");
        }
        this.setSize(6 * 9);

        addButtons();
    }

    public HubMenu(DyeManager manager, int page) {
        super(manager);

        if (getDyes().isEmpty()) {
            this.setTitle("Dye Master");
        } else {
            int pages = (int) Math.ceil((double) getDyes().size() / (double) ITEM_SLOTS.length);
            if (pages == 1) {
                this.setTitle("Dye Master");
            } else this.setTitle("Dye Master (" + page + "/" + pages + ")");
        }
        this.setSize(6 * 9);
        this.setPage(page);

        addButtons();

    }

    public void addButtons() {

        DyeManager manager = getManager();
        manager.setAction(null);
        manager.setDye(null);

        List<Button> items = new ArrayList<>();
        for (Dye dye : getDyes()) {
            items.add(new Button() {
                @Override
                public ItemStack getItem() {
                    ItemStack item = dye.create();
                    manager.setDye(dye);
                    return item;
                }

                @Override
                public void onClick(Player player) {
                    if (dye == null) {
                        player.sendMessage(ChatColor.RED + "Dye is not found or deleted.");
                        player.closeInventory();
                    }
                    new DyeOptionsMenu(manager).displayTo(player);
                }
            });
        }

        if (items.isEmpty()) {
            this.addButton(new Button(22) {
                @Override
                public ItemStack getItem() {
                    ItemStack item = new ItemStack(Material.BEDROCK);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.RED + "No Dyes");
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GRAY + "Dyes can be created by server");
                    lore.add(ChatColor.GRAY + "admins to make minecraft armors");
                    lore.add(ChatColor.GRAY + "more beautiful and unique");
                    lore.add("");
                    lore.add(ChatColor.YELLOW + "Click on paper below to begin");
                    lore.add(ChatColor.YELLOW + "creating dyes!");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public void onClick(Player player) {

                }
            });
        }

        int page = this.getPage();
        int startIndex = (page - 1) * ITEM_SLOTS.length;
        int endIndex = Math.min(startIndex + ITEM_SLOTS.length, items.size());

        for (int i = startIndex; i < endIndex; i++) {
            items.get(i).setSlot(ITEM_SLOTS[i - startIndex]);
            this.addButton(items.get(i));
        }

        if (endIndex < items.size()) {
            this.addButton(new Button(53) {
                @Override
                public ItemStack getItem() {
                    ItemStack item = new ItemStack(Material.ARROW);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + "Next Page");
                    meta.setLore(List.of(ChatColor.YELLOW + "Page " + (page + 1)));
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public void onClick(Player player) {
                    incrementPage();
                    HubMenu newPage = new HubMenu(manager, getPage());
                    newPage.setPage(getPage());
                    newPage.displayTo(player);
                }
            });
        }

        if (page > 1) {
            this.addButton(new Button(45) {
                @Override
                public ItemStack getItem() {
                    ItemStack item = new ItemStack(Material.ARROW);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + "Previous Page");
                    meta.setLore(List.of(ChatColor.YELLOW + "Page " + (page - 1)));
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public void onClick(Player player) {
                    decremenetPage();
                    HubMenu newPage = new HubMenu(manager, getPage());
                    newPage.setPage(getPage());
                    newPage.displayTo(player);
                }
            });
        }

        //config button
        this.addButton(new Button(48) {
            @Override
            public ItemStack getItem() {
                ItemStack item = new ItemStack(Material.REPEATER);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Plugin Settings");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Allows you to edit and control");
                lore.add(ChatColor.GRAY + "various plugin settings.");
                lore.add("");
                lore.add(ChatColor.YELLOW + "Click to edit plugin settings!");
                meta.setLore(lore);
                item.setItemMeta(meta);
                return item;
            }

            @Override
            public void onClick(Player player) {
                new Config(manager).displayTo(player);
            }
        });

        //create button
        this.addButton(new Button(50) {
            @Override
            public ItemStack getItem() {
                ItemStack item = new ItemStack(Material.PAPER);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Create Dye");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Create a new custom dye.");
                lore.add("");
                lore.add(ChatColor.YELLOW + "Click to create!");
                meta.setLore(lore);
                item.setItemMeta(meta);
                return item;
            }

            @Override
            public void onClick(Player player) {
                manager.setSettings(new Settings());
                manager.setAction(DyeManager.Action.CREATE);
                new CreationMenu(manager).displayTo(player);
            }
        });

        //close button
        this.addButton(new Button(49) {
            @Override
            public ItemStack getItem() {
                ItemStack item = new ItemStack(Material.BARRIER);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.RED + "Close");
                item.setItemMeta(meta);
                return item;
            }

            @Override
            public void onClick(Player player) {
                player.closeInventory();
            }
        });
    }

    class Config extends Menu {
        public Config(DyeManager manager) {
            super(manager);

            this.setTitle("Plugin Settings");
            this.setBackSlot(49);
            this.setSize(6 * 9);
            this.setParent(new Parent(HubMenu.class, new Parameter(DyeManager.class, manager)));

            ConfigManager config = DyeMaster.getConfigManager();

            this.addButton(new Button(21) {
                @Override
                public ItemStack getItem() {
                    ItemStack item = new ItemStack(Material.GLOW_INK_SAC);
                    ItemMeta meta = item.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GRAY + "Toggle whether dye name");
                    lore.add(ChatColor.GRAY + "will be colored");
                    meta.setLore(lore);
                    boolean configValue = getPlugin().getConfig().getBoolean("DyeNameColor");
                    if (configValue) {
                        meta.setDisplayName(ChatColor.GREEN + "Dye name color");
                    } else {
                        meta.setDisplayName(ChatColor.RED + "Dye name color");
                    }
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public void onClick(Player player) {
                    boolean newValue = !getPlugin().getConfig().getBoolean("DyeNameColor");
                    getPlugin().getConfig().set("DyeNameColor", newValue);
                    getPlugin().saveConfig();
                    config.setDyeNameColor(newValue);

                    ItemStack item = player.getOpenInventory().getItem(21);
                    ItemMeta meta = item.getItemMeta();
                    if (newValue) {
                        meta.setDisplayName(ChatColor.GREEN + "Dye name color");
                        player.sendMessage(ChatColor.GREEN + "Dye name color is now enabled!");
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    } else {
                        meta.setDisplayName(ChatColor.RED + "Dye name color");
                        player.sendMessage(ChatColor.RED + "Dye name color is now disabled!");
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    }
                    player.getOpenInventory().setItem(30, getButtonBySlot(30).getItem());
                    item.setItemMeta(meta);
                    reloadRecipes(getDyes());
                }
            });

            this.addButton(new Button(30) {
                @Override
                public ItemStack getItem() {
                    ItemStack item = new ItemStack(Material.GRAY_DYE);
                    ItemMeta meta = item.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GRAY + "Click to enable!");
                    meta.setLore(lore);
                    boolean configValue = getPlugin().getConfig().getBoolean("DyeNameColor");
                    if (configValue) {
                        item.setType(Material.LIME_DYE);
                        meta.setDisplayName(ChatColor.GREEN + "Dye name color");
                    } else {
                        meta.setDisplayName(ChatColor.RED + "Dye name color");
                    }
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public void onClick(Player player) {
                    boolean newValue = !getPlugin().getConfig().getBoolean("DyeNameColor");
                    getPlugin().getConfig().set("DyeNameColor", newValue);
                    getPlugin().saveConfig();
                    config.setDyeNameColor(newValue);

                    ItemStack item = player.getOpenInventory().getItem(30);
                    ItemMeta meta = item.getItemMeta();
                    if (newValue) {
                        item.setType(Material.LIME_DYE);
                        meta.setDisplayName(ChatColor.GREEN + "Dye name color");
                        player.sendMessage(ChatColor.GREEN + "Dye name color is now enabled!");
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    } else {
                        item.setType(Material.GRAY_DYE);
                        meta.setDisplayName(ChatColor.RED + "Dye name color");
                        player.sendMessage(ChatColor.RED + "Dye name color is now disabled!");
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    }
                    player.getOpenInventory().setItem(21, getButtonBySlot(21).getItem());
                    item.setItemMeta(meta);
                    reloadRecipes(getDyes());
                }
            });

            this.addButton(new Button(23) {

                @Override
                public ItemStack getItem() {
                    ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
                    ItemMeta meta = item.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GRAY + "Toggle whether armor name");
                    lore.add(ChatColor.GRAY + "will be colored");
                    meta.setLore(lore);
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    boolean configValue = getPlugin().getConfig().getBoolean("ArmorNameColor");
                    if (configValue) {
                        meta.setDisplayName(ChatColor.GREEN + "Armor name color");
                    } else {
                        meta.setDisplayName(ChatColor.RED + "Armor name color");
                    }
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public void onClick(Player player) {
                    boolean newValue = !getPlugin().getConfig().getBoolean("ArmorNameColor");
                    getPlugin().getConfig().set("ArmorNameColor", newValue);
                    getPlugin().saveConfig();
                    config.setArmorNameColor(newValue);

                    ItemStack item = player.getOpenInventory().getItem(23);
                    ItemMeta meta = item.getItemMeta();
                    if (newValue) {
                        meta.setDisplayName(ChatColor.GREEN + "Armor name color");
                        player.sendMessage(ChatColor.GREEN + "Armor name color is now enabled!");
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    } else {
                        meta.setDisplayName(ChatColor.RED + "Armor name color");
                        player.sendMessage(ChatColor.RED + "Armor name color is now disabled!");
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    }
                    player.getOpenInventory().setItem(32, getButtonBySlot(32).getItem());
                    item.setItemMeta(meta);
                    reloadRecipes(getDyes());
                }
            });

            this.addButton(new Button(32) {
                @Override
                public ItemStack getItem() {
                    ItemStack item = new ItemStack(Material.GRAY_DYE);
                    ItemMeta meta = item.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GRAY + "Click to enable!");
                    meta.setLore(lore);
                    boolean configValue = getPlugin().getConfig().getBoolean("ArmorNameColor");
                    if (configValue) {
                        item.setType(Material.LIME_DYE);
                        meta.setDisplayName(ChatColor.GREEN + "Armor name color");
                    } else {
                        meta.setDisplayName(ChatColor.RED + "Armor name color");
                    }
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public void onClick(Player player) {
                    boolean newValue = !getPlugin().getConfig().getBoolean("ArmorNameColor");
                    getPlugin().getConfig().set("ArmorNameColor", newValue);
                    getPlugin().saveConfig();
                    config.setArmorNameColor(newValue);

                    ItemStack item = player.getOpenInventory().getItem(32);
                    ItemMeta meta = item.getItemMeta();
                    if (newValue) {
                        item.setType(Material.LIME_DYE);
                        meta.setDisplayName(ChatColor.GREEN + "Armor name color");
                        player.sendMessage(ChatColor.GREEN + "Armor name color is now enabled!");
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    } else {
                        item.setType(Material.GRAY_DYE);
                        meta.setDisplayName(ChatColor.RED + "Armor name color");
                        player.sendMessage(ChatColor.RED + "Armor name color is now disabled!");
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    }
                    player.getOpenInventory().setItem(23, getButtonBySlot(23).getItem());
                    item.setItemMeta(meta);
                    reloadRecipes(getDyes());
                }
            });
        }
    }
}

