package me.dyemaster.guis;

import me.dyemaster.managers.DyeManager;
import me.dyemaster.utils.MenuEdgesUtils;
import me.dyemaster.utils.UpdateGUIRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static me.dyemaster.DyeMaster.getPlugin;
import static me.dyemaster.listeners.onChatMessage.waitingForInput;

public class Menu {

    DyeManager manager;

    public DyeManager getManager() {
        return manager;
    }

    public void setManager(DyeManager manager) {
        this.manager = manager;
    }

    public Menu(DyeManager manager) {
        this.manager = manager;
        manager.setSwitchingMenus(true);
    }

    private Inventory inventory;
    private String title;
    private int size;
    private Parent parent;
    private int backSlot = -1;
    private ArrayList<String> backItemLore;
    private Fill fill;
    private boolean cancel = true;
    private boolean extraButtonsRegistered = false;
    private final List<Integer> blackListedSlots = new ArrayList<>();
    private final List<Material> blackListedMaterials = new ArrayList<>();
    private List<Integer> edgeSlots = new ArrayList<>();
    private int page = 1;
    private boolean hasTask;
    private BukkitRunnable task;

    public void hasTask(boolean hasTask) {
        this.hasTask = hasTask;
    }

    public void setBackItemLore(ArrayList<String> backItemLore) {
        this.backItemLore = backItemLore;
    }

    public void incrementPage() {
        this.page++;
    }

    public void onOpen(Inventory menu) {
    }

    public void decremenetPage() {
        this.page--;
    }

    private final List<Integer> clickableSlots = new ArrayList<>();

    private final List<Button> buttons = new ArrayList<>();

    public final List<Button> getButtons() {
        return buttons;
    }

    protected final void addBlackListedSlots(int[] slots) {
        for (int i : slots) this.blackListedSlots.add(i);
    }

    protected final void addBlackListedMaterials(Material[] materials) {
        this.blackListedMaterials.addAll(Arrays.asList(materials));
    }

    protected final void addClickableSlots(int[] slots) {
        for (int i : slots) this.clickableSlots.add(i);
    }

    protected final void addButton(Button button) {
        this.buttons.add(button);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public void setBackSlot(int backSlot) {
        this.backSlot = backSlot;
    }


    public void setFill(Fill fill) {
        this.fill = fill;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public Button getButtonBySlot(int slot) {
        return getButtons().stream()
                .filter(button -> button.getSlot() == slot)
                .findFirst()
                .orElse(null);
    }

    public List<Integer> getBlackListedSlots() {
        return blackListedSlots;
    }

    public List<Material> getBlackListedMaterials() {
        return blackListedMaterials;
    }

    public List<Integer> getEdgeSlots() {
        return edgeSlots;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public final void displayTo(Player player) {
        final Inventory inventory = Bukkit.createInventory(player, this.size,
                ChatColor.translateAlternateColorCodes('&', this.title));

        for (final Button button : this.buttons)
            inventory.setItem(button.getSlot(), button.getItem());

        if (this.parent != null && !this.extraButtonsRegistered && backSlot != -1) {
            this.extraButtonsRegistered = true;

            final Button returnBackButton = new Button(backSlot) {
                @Override
                public ItemStack getItem() {
                    final ItemStack item = new ItemStack(Material.ARROW);
                    final ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + "Go Back");
                    if (backItemLore != null) meta.setLore(backItemLore);
                    item.setItemMeta(meta);

                    return item;
                }

                @Override
                public void onClick(Player player) {
                    try {
                        Map<Class<?>, Object> hashmap = parent.toHashmap();
                        Class<?>[] classArray = hashmap.keySet().toArray(new Class<?>[0]);
                        Object[] objectArray = hashmap.values().toArray(new Object[0]);
                        final Menu newMenuInstance = (Menu) parent.getParentClass().getConstructor(classArray).newInstance(objectArray);

                        newMenuInstance.displayTo(player);

                    } catch (final ReflectiveOperationException ex) {
                        ex.printStackTrace();
                    }
                }
            };

            this.buttons.add(returnBackButton);
            inventory.setItem(returnBackButton.getSlot(), returnBackButton.getItem());
        }

        this.edgeSlots = MenuEdgesUtils.fill(inventory, fill);

        DyeManager manager = (DyeManager) player.getMetadata("DyeManager").getFirst().value();

        if (manager.getMenu() != null) player.closeInventory();

        setManager(manager);
        manager.setMenu(this);
        waitingForInput.remove(player.getUniqueId());

        player.openInventory(inventory);

        manager.setSwitchingMenus(false);

        onOpen(inventory);

        if (hasTask) {
            if (title.equals("Add or edit lore")) {
                task = new UpdateGUIRunnable(inventory, 1);
            } else {
                task = new UpdateGUIRunnable(inventory, 0);
            }
            task.runTaskTimer(getPlugin(), 0L, 5L); // Schedule to run every second (20 ticks)
        }

    }
}
