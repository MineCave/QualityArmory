package me.zombie_striker.qg;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.zombie_striker.qg.ammo.Ammo;
import me.zombie_striker.qg.armor.ArmorObject;
import me.zombie_striker.qg.guns.Gun;
import me.zombie_striker.qg.handlers.IronSightsToggleItem;
import me.zombie_striker.qg.miscitems.MedKit;

public class ItemFact {

	public static List<String> getArmorLore(ArmorObject a, ItemStack current) {
		List<String> lore = new ArrayList<>();
		if (a.getCustomLore() != null)
			lore.addAll(a.getCustomLore());

		if (current != null && current.hasItemMeta() && current.getItemMeta().hasLore())
			for (String s : current.getItemMeta().getLore()) {
				if (ChatColor.stripColor(s).contains("UUID")) {
					lore.add(s);
					break;
				}
			}
		return lore;
	}

	public static List<String> getGunLore(Gun g, ItemStack current, int amount) {
		List<String> lore = new ArrayList<>();
		if (g.getCustomLore() != null)
			lore.addAll(g.getCustomLore());
		addVarientData(lore, g);
		lore.add(Main.S_ITEM_BULLETS + ": " + (amount) + "/" + (g.getMaxBullets()));
		if (Main.ENABLE_LORE_INFO) {
			lore.add(Main.S_ITEM_DAMAGE + ": " + g.getDamage());
			if (g.getAmmoType() != null)
				lore.add(Main.S_ITEM_AMMO + ": " + g.getAmmoType().getDisplayName());
		}
		if (Main.enableDurability)
			if (current == null) {
				lore.add(Main.S_ITEM_DURIB + ":" + g.getDurability() + "/" + g.getDurability());
			} else {
				lore = setDamage(g, lore, getDamage(current));
			}

		if (Main.ENABLE_LORE_HELP) {
			if (g.isAutomatic()) {
				lore.add(Main.S_LMB_SINGLE);
				lore.add(Main.S_RMB_RELOAD);
			} else {
				lore.add(Main.S_LMB_SINGLE);
				lore.add(Main.enableIronSightsON_RIGHT_CLICK ? Main.S_RMB_R1 : Main.S_RMB_R2);
				if (g.hasIronSights())
					lore.add(Main.enableIronSightsON_RIGHT_CLICK ? Main.S_RMB_A1 : Main.S_RMB_A2);
			}
		}

		if (current != null && current.hasItemMeta() && current.getItemMeta().hasLore())
			for (String s : current.getItemMeta().getLore()) {
				if (ChatColor.stripColor(s).contains("UUID")) {
					lore.add(s);
					break;
				}
			}
		return lore;
	}

	public static ItemStack addShopLore(ArmoryBaseObject obj, ItemStack current) {
		ItemMeta meta = current.getItemMeta();
		List<String> lore = meta.getLore();
		for (String loreS : new ArrayList<>(lore)) {
			if (loreS.startsWith(ChatColor.DARK_RED + "Crafts") || loreS.startsWith(ChatColor.RED + Main.S_ITEM_ING)
					|| loreS.startsWith(ChatColor.RED + "-"))
				lore.remove(loreS);

		}
		if (obj.getCraftingReturn() > 1)
			lore.add(ChatColor.DARK_RED + "Returns: " + obj.getCraftingReturn());
		lore.add(ChatColor.GOLD + "Price: " + obj.cost());
		meta.setLore(lore);
		ItemStack is = current;
		is.setItemMeta(meta);
		return is;
	}

	public static List<String> getCraftingGunLore(Gun g) {
		List<String> lore = getGunLore(g, null, 1);
		lore.addAll(getCraftingLore(g));
		return lore;
	}

	public static List<String> getCraftingLore(ArmoryBaseObject a) {
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.RED + Main.S_ITEM_ING + ": ");
		for (ItemStack is : a.getIngredients()) {
			StringBuilder sb = new StringBuilder();
			sb.append(ChatColor.RED + "-" + is.getAmount() + " " + is.getType().name());
			if (is.getDurability() != 0)
				sb.append(":" + is.getDurability());
			lore.add(sb.toString());
		}
		if (a.getCraftingReturn() > 1) {
			lore.add(ChatColor.DARK_RED + "Crafts " + a.getCraftingReturn());
		}
		return lore;
	}

	public static ItemStack getGun(int durib) {
		Gun g = Main.gunRegister.get(MaterialStorage.getMS(Main.guntype, durib, 0, null));
		return getGun(g);
	}

	public static ItemStack getGun(int durib, int varient) {
		Gun g = Main.gunRegister.get(MaterialStorage.getMS(Main.guntype, durib, varient, null));
		return getGun(g);
	}

	public static ItemStack getGun(MaterialStorage durib) {
		Gun g = Main.gunRegister.get(durib);
		return getGun(g);
	}

	public static ItemStack getIronSights() {

		ItemStack ironsights = new ItemStack(Main.guntype, 1, (short) IronSightsToggleItem.getData());
		ItemMeta im = ironsights.getItemMeta();
		im.setDisplayName(IronSightsToggleItem.getItemName());
		try {
			im.setUnbreakable(true);
		} catch (Error | Exception e34) {
		}
		try {
			im.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE);
		} catch (Error | Exception e34) {
		}
		ironsights.setItemMeta(im);
		return ironsights;
	}

	public static ItemStack getGun(Gun g) {
		ItemStack is = new ItemStack(g.getItemData().getMat(), 0, (short) g.getItemData().getData());
		if (g.getItemData().getData() < 0)
			is.setDurability((short) 0);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(g.getDisplayName());
		List<String> lore = getGunLore(g, null, g.getMaxBullets());
		im.setLore(lore);
		try {
			im.setUnbreakable(true);

		} catch (Error e3) {
		}
		try {
			im.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE);
		} catch (Error e) {
		}

		is.setItemMeta(im);
		if (Main.enableVisibleAmounts)
			is.setAmount(g.getMaxBullets() > 64 ? 64 : g.getMaxBullets());
		else
			is.setAmount(1);

		is = addGunRegister(is);
		return is;
	}

	public static ItemStack getArmor(ArmorObject a) {
		ItemStack is = new ItemStack(a.getItemData().getMat(), 0, (short) a.getItemData().getData());
		if (a.getItemData().getData() < 0)
			is.setDurability((short) 0);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(a.getDisplayName());
		List<String> lore = getArmorLore(a, is);
		addVarientData(lore, a);
		im.setLore(lore);
		try {
			im.setUnbreakable(true);
		} catch (Error e) {
		}
		try {
			im.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE);
		} catch (Error e) {
		}

		is.setItemMeta(im);
		return is;
	}

	public static ItemStack getAmmo(Material m, int data, String extraValues) {
		return getAmmo(m, data, 0, extraValues);
	}

	public static ItemStack getAmmo(Material m, int data, int varient, String extraValues) {
		if (Main.ammoRegister.containsKey(MaterialStorage.getMS(m, data, varient, extraValues)))
			return getAmmo(Main.ammoRegister.get(MaterialStorage.getMS(m, data, varient, extraValues)));
		return null;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getAmmo(Ammo a) {
		ItemStack is = new ItemStack(a.getItemData().getMat(), 0, (short) a.getItemData().getData());
		if (a.getItemData().getData() < 0)
			is.setDurability((short) 0);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(a.getDisplayName());
		try {
			im.setUnbreakable(true);
		} catch (Error e) {
		}
		try {
			im.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE);
		} catch (Error e) {
		}
		List<String> lore = new ArrayList<String>();
		lore.addAll(a.getCustomLore());
		addVarientData(lore, a);

		if (a.isSkull()) {
			((SkullMeta) im).setOwner(a.getSkullOwner());
		}

		is.setItemMeta(im);
		is.setAmount(a.getMaxAmount() > 64 ? 64 : a.getMaxAmount());

		return is;
	}

	public static ItemStack getObject(ArmoryBaseObject a) {
		ItemStack is = new ItemStack(a.getItemData().getMat(), 0, (short) a.getItemData().getData());
		if (a.getItemData().getData() < 0)
			is.setDurability((short) 0);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(a.getDisplayName());

		try {
			im.setUnbreakable(true);
		} catch (Error e) {
		}
		try {
			im.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE);
		} catch (Error e) {
		}
		List<String> lore = new ArrayList<String>();
		if (a instanceof MedKit) {
			if (Main.ENABLE_LORE_HELP) {
				lore.add(Main.S_MEDKIT_LORE_INFO);
			}
		}
		if (a.getCustomLore() != null)
			lore.addAll(a.getCustomLore());
		addVarientData(lore, a);
		im.setLore(lore);

		is.setItemMeta(im);
		is.setAmount(a.getCraftingReturn());

		return is;
	}

	public static int getDamage(ItemStack is) {
		for (String lore : is.getItemMeta().getLore()) {
			if (lore.startsWith(Main.S_ITEM_DURIB + ":")) {
				return Integer.parseInt(lore.split(":")[1].split("/")[0]);
			}
		}
		return -1;
	}

	public static ItemStack damage(Gun g, ItemStack is) {
		return setDamage(g, is, getDamage(is) - 1);
	}

	public static ItemStack setDamage(Gun g, ItemStack is, int damage) {
		ItemMeta im = is.getItemMeta();
		im.setLore(setDamage(g, im.getLore(), damage));
		is.setItemMeta(im);
		return is;
	}

	public static List<String> setDamage(Gun g, List<String> lore, int damage) {
		boolean foundLine = false;
		double k = ((double) damage) / g.getDurability();
		ChatColor c = k > 0.5 ? ChatColor.DARK_GREEN : k > 0.25 ? ChatColor.GOLD : ChatColor.DARK_RED;
		for (int j = 0; j < lore.size(); j++) {
			if (ChatColor.stripColor(lore.get(j)).startsWith(Main.S_ITEM_DURIB)) {
				lore.set(j, c + Main.S_ITEM_DURIB + ":" + damage + "/" + g.getDurability());
				foundLine = true;
				break;
			}
		}
		if (!foundLine) {
			lore.add(c + Main.S_ITEM_DURIB + ":" + damage + "/" + g.getDurability());
		}
		return lore;
	}

	public static ItemStack addGunRegister(ItemStack base) {
		ItemMeta im = base.getItemMeta();
		try {
			im.setLocalizedName("" + UUID.randomUUID());
		} catch (Exception | Error e) {
			List<String> lore = im.getLore();
			lore.add(ChatColor.DARK_GRAY + "UUID" + UUID.randomUUID().toString());
			im.setLore(lore);
		}
		base.setItemMeta(im);
		return base;
	}

	public static boolean sameGun(ItemStack is1, ItemStack is2) {
		try {
			if (is1.hasItemMeta() && is1.getItemMeta().hasLocalizedName())
				if (is2.hasItemMeta() && is2.getItemMeta().hasLocalizedName())
					return is1.getItemMeta().getLocalizedName().equals(is2.getItemMeta().getLocalizedName());
		} catch (Exception | Error e1) {
			if (is1.hasItemMeta() && is1.getItemMeta().hasLore())
				if (is2.hasItemMeta() && is2.getItemMeta().hasLore())
					for (String s : is1.getItemMeta().getLore())
						if (s.contains("UUID"))
							return is2.getItemMeta().getLore().contains(s);
		}
		return false;
	}

	public static void addVarientData(List<String> lore, ArmoryBaseObject object) {
		if (object.getItemData().isVarient())
			lore.add(Main.S_ITEM_VARIENTS + " " + object.getItemData().getVarient());
	}

	public static int getAmount(ItemStack is) {
		if (is != null) {
			if (is.hasItemMeta() && is.getItemMeta().hasLore()) {
				for (String lore : is.getItemMeta().getLore()) {
					if (lore.contains(Main.S_ITEM_BULLETS)) {
						return Integer.parseInt(lore.split(":")[1].split("/")[0].trim());
					}
				}
				return 0;
			} else {
				if (Main.enableVisibleAmounts)
					return is.getAmount();
			}
		}
		return 0;
	}

}