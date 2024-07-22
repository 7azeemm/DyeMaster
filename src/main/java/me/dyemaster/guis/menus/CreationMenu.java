package me.dyemaster.guis.menus;

import me.dyemaster.DyeMaster;
import me.dyemaster.guis.*;
import me.dyemaster.listeners.onChatMessage;
import me.dyemaster.managers.DyeManager;
import me.dyemaster.models.Dye;
import me.dyemaster.models.Icon;
import me.dyemaster.models.Recipe;
import me.dyemaster.models.Settings;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Objects;

import static me.dyemaster.DyeMaster.getDyes;
import static me.dyemaster.DyeMaster.getPlugin;
import static me.dyemaster.listeners.onChatMessage.waitingForInput;
import static me.dyemaster.managers.AnvilManager.openAnvil;
import static me.dyemaster.storage.Actions.editDye;
import static me.dyemaster.storage.Actions.saveDye;
import static me.dyemaster.utils.ColorsUtils.hexToRGB;
import static me.dyemaster.utils.ColorsUtils.loreProcess;
import static me.dyemaster.utils.CustomSkull.createIcon;
import static me.dyemaster.utils.RecipeUtils.*;
import static org.bukkit.Bukkit.getServer;

public class CreationMenu extends Menu {

    public CreationMenu(DyeManager manager) {
        super(manager);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "Warning: changed settings will");
        lore.add(ChatColor.RED + "not be saved");

        this.setTitle("Dye Creation");
        this.setParent(new Parent(HubMenu.class, new Parameter(DyeManager.class, manager)));
        this.setSize(6 * 9);
        this.setBackSlot(45);
        this.setFill(new Fill(Fill.Style.ALL, new int[]{10, 19, 28, 16, 25, 34, 22, 45, 49}));
        this.setBackItemLore(lore);
        this.hasTask(true);

        //create button
        if (manager.getAction() == DyeManager.Action.CREATE) {
            this.addButton(new Button(49) {
                @Override
                public ItemStack getItem() {
                    ItemStack item = new ItemStack(Material.GREEN_CONCRETE);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + "Create");
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.YELLOW + "Click to create!");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public void onClick(Player player) {
                    Settings settings = manager.getSettings();

                    ArrayList<String> lore = new ArrayList<>();
                    if (settings.getName() == null) {
                        lore.add(ChatColor.BOLD + (ChatColor.RED + "● Name \n"));
                    }
                    if (settings.getIcon() == null) {
                        lore.add(ChatColor.BOLD + (ChatColor.RED + "● Icon \n"));
                    }
                    if (settings.getColor() == null) {
                        lore.add(ChatColor.BOLD + (ChatColor.RED + "● Color \n"));
                    }

                    if (!lore.isEmpty()) {
                        ItemStack item = player.getOpenInventory().getTopInventory().getItem(this.getSlot());
                        ItemMeta meta = Objects.requireNonNull(item).getItemMeta();
                        lore.addFirst(ChatColor.RED + "Requires: \n");
                        lore.add(" ");
                        lore.add(ChatColor.YELLOW + "Click to create!");
                        meta.setLore(lore);
                        item.setItemMeta(meta);

                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return;
                    }

                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

                    player.closeInventory();

                    Dye dye = new Dye(player.getUniqueId(), settings);
                    getDyes().add(dye);
                    saveDye(dye);
                    if (settings.getRecipe() != null) registerRecipe(dye);

                    player.removeMetadata("DyeManager", getPlugin());

                    player.sendMessage(ChatColor.GREEN + "Dye Created successfully.");

                }
            });
        }

        //edit button
        if (manager.getAction() == DyeManager.Action.EDIT) {
            this.setParent(new Parent(DyeOptionsMenu.class, new Parameter(DyeManager.class, manager)));
            this.setTitle("Edit dye");
            this.addButton(new Button(49) {
                @Override
                public ItemStack getItem() {
                    ItemStack item = new ItemStack(Material.GREEN_CONCRETE);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + "Save");
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.YELLOW + "Click to save!");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public void onClick(Player player) {
                    Settings settings = manager.getSettings();
                    Dye dye = manager.getDye();

                    editDye(dye.getId(), null, settings);
                    dye.setSettings(settings);
                    if (dye.getSettings().getRecipe() != null) {
                        unregisterRecipe(new NamespacedKey(getPlugin(), dye.getId().toString()));
                        registerRecipe(dye);
                    }

                    new DyeOptionsMenu(manager).displayTo(player);

                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                }
            });
        }

        //name button
        this.addButton(new Button(10) {
            @Override
            public ItemStack getItem() {
                ItemStack item = new ItemStack(Material.NAME_TAG);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Name");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Set a custom name for");
                lore.add(ChatColor.GRAY + "your dye.");
                lore.add(" ");
                lore.add(ChatColor.YELLOW + "Click to input a name!");
                meta.setLore(lore);
                item.setItemMeta(meta);
                return item;
            }

            @Override
            public void onClick(Player player) {
                openAnvil(player, "Name", manager.getSettings().getName(), DyeManager.Action.NAME, manager.getAction());
            }
        });

        //icon button
        this.addButton(new Button(19) {
            @Override
            public ItemStack getItem() {
                ItemStack item = new ItemStack(Material.ITEM_FRAME);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Icon");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Icon can be a vanilla minecraft dye");
                lore.add(ChatColor.GRAY + "or a custom skull.");
                lore.add(" ");
                lore.add(ChatColor.YELLOW + "Click to select an icon!");
                meta.setLore(lore);
                item.setItemMeta(meta);
                return item;
            }

            @Override
            public void onClick(Player player) {
                new IconMenu(manager).displayTo(player);
            }
        });

        //color button
        this.addButton(new Button(28) {
            @Override
            public ItemStack getItem() {
                ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Color");
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Colors should be in");
                lore.add(ChatColor.GRAY + "a hex code format.");
                lore.add(ChatColor.GRAY + "Example: " + ChatColor.WHITE + "#FFFFFF");
                lore.add(" ");
                lore.add(ChatColor.YELLOW + "Click to input a color code!");
                meta.setLore(lore);
                item.setItemMeta(meta);
                return item;
            }

            @Override
            public void onClick(Player player) {
                openAnvil(player, "Color", manager.getSettings().getColor(), DyeManager.Action.COLOR, manager.getAction());
            }
        });

        //lore button
        this.addButton(new Button(16) {
            @Override
            public ItemStack getItem() {
                ItemStack item = new ItemStack(Material.MOJANG_BANNER_PATTERN);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Lore");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Set a custom lore for");
                lore.add(ChatColor.GRAY + "your dye.");
                lore.add(" ");
                lore.add(ChatColor.YELLOW + "Click to add or edit lore.");
                meta.setLore(lore);
                meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
                item.setItemMeta(meta);
                return item;
            }

            @Override
            public void onClick(Player player) {
                new LoreMenu(manager).displayTo(player);
            }
        });

        //recipe button
        this.addButton(new Button(25) {
            @Override
            public ItemStack getItem() {
                ItemStack item = new ItemStack(Material.BOOK);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Recipe");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Define the crafting recipe.");
                lore.add(" ");
                lore.add(ChatColor.YELLOW + "Click to configure the recipe!");
                meta.setLore(lore);
                item.setItemMeta(meta);
                return item;
            }

            @Override
            public void onClick(Player player) {
                new RecipeMenu(manager).displayTo(player);
            }
        });

        //loot button
        this.addButton(new Button(34) {
            @Override
            public ItemStack getItem() {
                ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Mob loot");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.DARK_RED + "COMING SOON!");
                meta.setLore(lore);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                item.setItemMeta(meta);
                return item;
            }

            @Override
            public void onClick(Player player) {

            }
        });
    }

    @Override
    public void onOpen(Inventory menu) {
        Settings settings = getManager().getSettings();
        ItemStack dye = menu.getItem(22);
        if (dye == null || dye.getType() == Material.AIR) dye = new ItemStack(Material.STONE_BUTTON);
        ItemMeta meta = dye.getItemMeta();

        String name = settings.getName();
        String color = settings.getColor();
        Icon icon = settings.getIcon();
        ArrayList<String> lore = settings.getLore();
        Recipe recipe = settings.getRecipe();

        if (icon != null) {
            dye = createIcon(icon);
            meta = dye.getItemMeta();
        }

        if (name != null) {
            if (color != null) {
                meta.setDisplayName(net.md_5.bungee.api.ChatColor.of(color) + name);
            } else {
                meta.setDisplayName(name);
            }
        } else {
            meta.setDisplayName(" ");
        }

        meta.setLore(lore);

        dye.setItemMeta(meta);
        menu.setItem(22, dye);

        ItemStack oldName = menu.getItem(10);
        ItemStack oldIcon = menu.getItem(19);
        ItemStack oldColor = menu.getItem(28);
        ItemStack oldLore = menu.getItem(16);
        ItemStack oldRecipe = menu.getItem(25);

        ItemMeta oldNameMeta = oldName.getItemMeta();
        ItemMeta oldIconMeta = oldIcon.getItemMeta();
        ItemMeta oldColorMeta = oldColor.getItemMeta();
        ItemMeta oldLoreMeta = oldLore.getItemMeta();
        ItemMeta oldRecipeMeta = oldRecipe.getItemMeta();

        if (name != null) {
            {
                ArrayList<String> loree = new ArrayList<>();
                loree.add(ChatColor.GRAY + "Set a custom name for");
                loree.add(ChatColor.GRAY + "your dye.");
                loree.add(" ");
                loree.add(ChatColor.GRAY + "name: " + net.md_5.bungee.api.ChatColor.WHITE + name);
                loree.add(" ");
                loree.add(ChatColor.YELLOW + "Click to input a name!");
                oldNameMeta.setLore(loree);
            }
        }

        if (color != null) {
            {
                ArrayList<String> loree = new ArrayList<>();
                loree.add(ChatColor.GRAY + "Colors should be in");
                loree.add(ChatColor.GRAY + "a hex code format.");
                loree.add(ChatColor.GRAY + "Example: " + ChatColor.WHITE + "#FFFFFF");
                loree.add(" ");
                loree.add(net.md_5.bungee.api.ChatColor.GRAY + "Color: " + net.md_5.bungee.api.ChatColor.of(color) + color);
                loree.add(" ");
                loree.add(ChatColor.YELLOW + "Click to input a color code!");
                oldColorMeta.setLore(loree);
            }

            ((LeatherArmorMeta) oldColorMeta).setColor(Color.fromRGB(hexToRGB(color)));
        }

        if (icon != null) {
            {
                ArrayList<String> loree = new ArrayList<>();
                loree.add(ChatColor.GRAY + "Icon can be a vanilla minecraft dye");
                loree.add(ChatColor.GRAY + "or a custom skull.");
                loree.add(" ");
                if (icon.getHeadType() == Material.PLAYER_HEAD) {
                    loree.add(net.md_5.bungee.api.ChatColor.GRAY + "icon: " + net.md_5.bungee.api.ChatColor.WHITE + "Custom Skull");
                } else {
                    String str = icon.getHeadType().name()
                            .replace("_", " ").toLowerCase();
                    StringBuilder b = new StringBuilder(str);
                    int i = 0;
                    do {
                        b.replace(i, i + 1, b.substring(i, i + 1).toUpperCase());
                        i = b.indexOf(" ", i) + 1;
                    } while (i > 0 && i < b.length());
                    loree.add(net.md_5.bungee.api.ChatColor.GRAY + "icon: " + net.md_5.bungee.api.ChatColor.WHITE + b);
                }
                loree.add(" ");
                loree.add(ChatColor.YELLOW + "Click to select an icon!");
                oldIconMeta.setLore(loree);
            }
        }

        if (lore != null) {
            {
                ArrayList<String> loree = new ArrayList<>();
                loree.add(ChatColor.GRAY + "Set a custom lore for");
                loree.add(ChatColor.GRAY + "your dye.");
                loree.add(" ");
                loree.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "Set");
                loree.add(" ");
                loree.add(ChatColor.YELLOW + "Click to add or edit lore.");
                oldLoreMeta.setLore(loree);
            }
        }

        if (recipe != null) {
            {
                ArrayList<String> loree = new ArrayList<>();
                loree.add(ChatColor.GRAY + "Define the crafting recipe.");
                loree.add(" ");
                loree.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "Set");
                loree.add(" ");
                loree.add(ChatColor.YELLOW + "Click to configure the recipe!");
                oldRecipeMeta.setLore(loree);
            }
        }

        oldName.setItemMeta(oldNameMeta);
        oldIcon.setItemMeta(oldIconMeta);
        oldColor.setItemMeta(oldColorMeta);
        oldLore.setItemMeta(oldLoreMeta);
        oldRecipe.setItemMeta(oldRecipeMeta);
    }

    public class IconMenu extends Menu {

        public IconMenu(DyeManager manager) {
            super(manager);

            this.setTitle("Choose an icon");
            this.setSize(5 * 9);
            this.setBackSlot(40);
            this.setParent(new Parent(CreationMenu.class, new Parameter(DyeManager.class, manager)));
            this.setFill(new Fill(Fill.Style.ALL, new int[]{21, 23, 40}));

            //SkullIcon
            this.addButton(new Button(23) {
                @Override
                public ItemStack getItem() {
                    ItemStack item = new ItemStack(Material.PLAYER_HEAD);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + "Custom Skull");
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.RED + "Note: Url must be in this format: ");
                    lore.add(ChatColor.GRAY + "https://textures.minecraft.net/");
                    lore.add(ChatColor.GRAY + "texture/[TEXTURE_URL]");
                    lore.add(" ");
                    lore.add(ChatColor.YELLOW + "Click to insert Skull texture!");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public void onClick(Player player) {
                    DyeManager.Action action = manager.getAction();
                    manager.setAction(DyeManager.Action.SKULL);

                    player.closeInventory();

                    waitingForInput.put(player.getUniqueId(), action);
                    getServer().getPluginManager().registerEvents(new onChatMessage(), DyeMaster.getPlugin());

                    player.sendTitle("", ChatColor.GOLD + "Paste head texture in chat!", 0, 100, 20);
                    player.sendMessage(ChatColor.GOLD + "Paste head texture in chat!");
                }
            });

            //VanillaIcon button
            this.addButton(new Button(21) {
                @Override
                public ItemStack getItem() {
                    ItemStack item = new ItemStack(Material.GRASS_BLOCK);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + "Vanilla dyes");
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.YELLOW + "Click to select a vanilla dye!");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public void onClick(Player player) {
                    new VanillaDyes(manager).displayTo(player);
                }
            });
        }
    }

    public class VanillaDyes extends Menu {

        public static Material[] dyes = new Material[]{
                Material.WHITE_DYE, Material.LIGHT_GRAY_DYE, Material.GRAY_DYE,
                Material.BLACK_DYE, Material.BROWN_DYE, Material.RED_DYE,
                Material.ORANGE_DYE, Material.YELLOW_DYE, Material.LIME_DYE,
                Material.GREEN_DYE, Material.CYAN_DYE, Material.LIGHT_BLUE_DYE,
                Material.BLUE_DYE, Material.PURPLE_DYE, Material.MAGENTA_DYE, Material.PINK_DYE
        };

        static int[] dyesIndices = {
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29
        };

        public VanillaDyes(DyeManager manager) {
            super(manager);

            this.setTitle("Select an icon");
            this.setSize(5 * 9);
            this.setBackSlot(40);
            this.setFill(new Fill(Fill.Style.EDGES, new int[]{40}));
            this.setParent(new Parent(CreationMenu.class, new Parameter(DyeManager.class, manager)));

            for (int i = 0; i < dyes.length; i++) {
                int finalI = i;
                this.addButton(new Button(dyesIndices[finalI]) {

                    Material type;

                    @Override
                    public ItemStack getItem() {
                        this.type = dyes[finalI];
                        return new ItemStack(dyes[finalI]);
                    }

                    @Override
                    public void onClick(Player player) {
                        Icon icon = new Icon(this.type, null);
                        manager.getSettings().setIcon(icon);
                        new CreationMenu(manager).displayTo(player);
                    }
                });
            }
        }
    }

    public class LoreMenu extends Menu {

        public LoreMenu(DyeManager manager) {
            super(manager);

            this.setTitle("Add or edit lore");
            this.setSize(6 * 9);
            this.setBackSlot(49);
            this.setParent(new Parent(CreationMenu.class, new Parameter(DyeManager.class, manager)));
            this.setFill(new Fill(Fill.Style.ALL, new int[]{14, 23, 25, 32, 49, 20}));
            this.hasTask(true);

            //AddLineButton
            this.addButton(new Button(14) {
                @Override
                public ItemStack getItem() {
                    ItemStack item = new ItemStack(Material.GLOW_ITEM_FRAME);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + "Add line");
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GRAY + "Add a line to the lore.");
                    lore.add(" ");
                    lore.add(ChatColor.YELLOW + "Click to insert a line!");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public void onClick(Player player) {
                    DyeManager.Action action = manager.getAction();
                    manager.setAction(DyeManager.Action.LORE);

                    player.closeInventory();

                    waitingForInput.put(player.getUniqueId(), action);
                    getServer().getPluginManager().registerEvents(new onChatMessage(), DyeMaster.getPlugin());

                    player.sendMessage(ChatColor.GOLD + "Type line in chat!");
                    player.sendTitle("", ChatColor.GOLD + "Type line in chat!", 0, 100, 20);
                }
            });

            //AddBlankLineButton
            this.addButton(new Button(23) {
                @Override
                public ItemStack getItem() {
                    ItemStack item = new ItemStack(Material.PAPER);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + "Add blank line");
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GRAY + "Add an empty line to the lore.");
                    lore.add(" ");
                    lore.add(ChatColor.YELLOW + "Click to insert an empty line!");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public void onClick(Player player) {
                    Settings settings = manager.getSettings();

                    String lore = settings.getRawLore();
                    lore = lore == null ? " " : lore + "\\n";

                    settings.setRawLore(lore);
                    ArrayList<String> process = loreProcess(lore, settings.getColor());
                    settings.setLore(process);
                    onOpen(player.getOpenInventory().getTopInventory());
                }
            });

            //DeleteButton
            this.addButton(new Button(32) {
                @Override
                public ItemStack getItem() {
                    ItemStack item = new ItemStack(Material.CAULDRON);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + "Delete last line");
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GRAY + "Delete the last line in the lore.");
                    lore.add(" ");
                    lore.add(ChatColor.YELLOW + "Click to delete the last line!");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public void onClick(Player player) {
                    Settings settings = manager.getSettings();
                    String lore = settings.getRawLore();

                    if (lore == null) {
                        player.sendMessage(ChatColor.RED + "There is no line to remove!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return;
                    }

                    int lastNewLineIndex = lore.lastIndexOf("\\n");
                    lore = lastNewLineIndex != -1 ? lore.substring(0, lastNewLineIndex) : null;

                    settings.setRawLore(lore);
                    if (lore != null) {
                        ArrayList<String> process = loreProcess(lore, settings.getColor());
                        settings.setLore(process);
                    } else settings.setLore(null);
                    onOpen(player.getOpenInventory().getTopInventory());
                }
            });

            //GuideButton
            this.addButton(new Button(25) {
                @Override
                public ItemStack getItem() {
                    ItemStack item = new ItemStack(Material.REDSTONE_TORCH);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + "Guide");
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GOLD + "Customize your dye item's lore");
                    lore.add(ChatColor.GOLD + "using the following codes:");
                    lore.add("");
                    lore.add(ChatColor.YELLOW + "[color] " + ChatColor.GRAY + "- Replaces this with the chosen dye color.");
                    lore.add(ChatColor.YELLOW + "[color:#RRGGBB] " + ChatColor.GRAY + "- Use a specific hex color.");
                    lore.add(ChatColor.YELLOW + "&<color_code> " + ChatColor.GRAY + "- Use Minecraft formatting codes.");
                    lore.add(ChatColor.YELLOW + "\\n " + ChatColor.GRAY + "- Inserts a new line.");
                    lore.add("");
                    lore.add(ChatColor.GRAY + "Type the codes directly into the chat.");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public void onClick(Player player) {

                }
            });
        }

        @Override
        public void onOpen(Inventory inventory) {
            Settings settings = getManager().getSettings();
            ItemStack dye = inventory.getItem(20);
            if (dye == null || dye.getType() == Material.AIR) dye = new ItemStack(Material.STONE_BUTTON);
            ItemMeta meta = dye.getItemMeta();

            String name = settings.getName();
            String color = settings.getColor();
            Icon icon = settings.getIcon();
            ArrayList<String> lore = settings.getLore();

            if (icon != null) {
                dye = createIcon(icon);
                meta = dye.getItemMeta();
            }

            if (name != null) {
                if (color != null) {
                    meta.setDisplayName(net.md_5.bungee.api.ChatColor.of(color) + name);
                } else {
                    meta.setDisplayName(name);
                }
            } else {
                meta.setDisplayName("[NAME]");
            }

            meta.setLore(lore);

            dye.setItemMeta(meta);
            inventory.setItem(20, dye);
        }
    }

    public class RecipeMenu extends Menu {

        public RecipeMenu(DyeManager manager) {
            super(manager);

            this.setTitle("Define Crafting recipe");
            this.setSize(5 * 9);
            this.setParent(new Parent(CreationMenu.class, new Parameter(DyeManager.class, manager)));
            this.setBackSlot(40);
            this.setFill(new Fill(Fill.Style.ALL, new int[]{40, 12, 13, 14, 21, 22, 23, 30, 31, 32, 25}));
            this.setCancel(false);
            this.addClickableSlots(new int[]{12, 13, 14, 21, 22, 23, 30, 31, 32});//clickable slots in menu
            this.addBlackListedSlots(new int[]{});
            this.addBlackListedMaterials(new Material[]{});

            this.addButton(new Button(25) {
                @Override
                public ItemStack getItem() {
                    ItemStack item = new ItemStack(Material.RED_CONCRETE);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.RED + "Invalid Recipe");
                    item.setItemMeta(meta);
                    return item;
                }

                @Override
                public void onClick(Player player) {
                    InventoryView view = player.getOpenInventory();
                    ItemStack item = view.getItem(25);

                    if (item.getType() == Material.RED_CONCRETE) {
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        player.sendMessage(ChatColor.RED + "Please specify the recipe!");
                    } else if (item.getType() == Material.GREEN_CONCRETE) {
                        Recipe recipe = saveRecipe(view.getTopInventory());
                        manager.getSettings().setRecipe(recipe);
                        new CreationMenu(manager).displayTo(player);
                    }
                }
            });
        }

        @Override
        public void onOpen(Inventory inventory) {
            Settings settings = getManager().getSettings();
            int[][] slots = {
                    {12, 13, 14},
                    {21, 22, 23},
                    {30, 31, 32}
            };

            Recipe recipe = settings.getRecipe();
            if (recipe != null) {
                Material[][] grid = recipe.getSlots();
                for (int i = 0; i < grid.length; i++) {
                    for (int y = 0; y < grid[i].length; y++) {
                        if (grid[i][y] == null) continue;
                        inventory.setItem(slots[i][y], new ItemStack(grid[i][y]));
                    }
                }
                updateStatus(inventory);
            }
        }

        public static void updateStatus(Inventory menu) {
            int[] slots = new int[]{
                    12, 13, 14,
                    21, 22, 23,
                    30, 31, 32
            };

            int slotsFound = 0;
            for (int slot : slots) {
                if (menu.getItem(slot) != null) {
                    slotsFound++;
                }
            }

            ItemStack createItem = menu.getItem(25);
            ItemMeta createItemMeta = createItem.getItemMeta();

            if (slotsFound == 0) {
                if (createItem.getType() == Material.RED_CONCRETE) return;
                createItem.setType(Material.RED_CONCRETE);
                createItemMeta.setDisplayName(ChatColor.RED + "Invalid Recipe");
                createItem.setItemMeta(createItemMeta);
            } else {
                createItem.setType(Material.GREEN_CONCRETE);
                createItemMeta.setDisplayName(ChatColor.GREEN + "Click to save the recipe");
                createItem.setItemMeta(createItemMeta);
            }
        }
    }
}
