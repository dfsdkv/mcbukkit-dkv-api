package dfs.dkv.api.item;

import java.util.List;
import java.util.Set;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class NBT extends Item {
	public NBT(String itstr, List<String> e) {
		super(itstr, e);
	}
	public NBT(String it, String dt) {
		super(it, dt);
	}
	public NBT(String itstr) {
		super(itstr);
	}
	public NBT (ItemStack it) {
		super(it);
	}
	
	public NBT setAttr (String attr, Object val) {
		if (val != null) {
			NBTTagCompound c = getCompound();
			if (val instanceof String) {
				c.setString(attr, (String) val);
			} else if (val instanceof Integer) {
				c.setInt(attr, (int) val);
			} else if (val instanceof Short) {
				c.setShort(attr, (short) val);
			} else if (val instanceof Boolean) {
				c.setBoolean(attr, (boolean) val);
			} else if (val instanceof Long) {
				c.setLong(attr, (long) val);
			}
			setCompound(c);
		}
		return this;
	}
	public NBT setCompound (NBTTagCompound c) {
		net.minecraft.server.v1_8_R3.ItemStack i = getNMS();
		if (i == null) {
			return this;
		}
		i.setTag(c);
		item = CraftItemStack.asCraftMirror(i);
		return this;
	}




	public NBTTagCompound getCompound () {
		net.minecraft.server.v1_8_R3.ItemStack nms = getNMS();
		NBTTagCompound tag = null;
		if (nms != null) {
			if (!nms.hasTag()) {
				tag = new NBTTagCompound(); 
				nms.setTag(tag);
			} else {
				tag = nms.getTag();
			}
		}
		return tag;
	}
	public net.minecraft.server.v1_8_R3.ItemStack getNMS () {
		return CraftItemStack.asNMSCopy(item);
	}
	public String getString (String key) {
		NBTTagCompound c = getCompound();
		return (c != null ? c.getString(key) : null);
	}
	public boolean getBoolean (String key) {
		NBTTagCompound c = getCompound();
		return (c != null ? c.getBoolean(key) : null);
	}
	public int getInteger (String key) {
		NBTTagCompound c = getCompound();
		return (c != null ? c.getInt(key) : null);
	}
	public short getShort (String key) {
		NBTTagCompound c = getCompound();
		return (c != null ? c.getShort(key) : null);
	}
	public long getLong (String key) {
		NBTTagCompound c = getCompound();
		return (c != null ? c.getLong(key) : null);
	}
	public Set<String> getKeys () {
		NBTTagCompound c = getCompound();
		return (c != null ? c.c() : null);
	}

	public boolean hasKey (String key) {
		NBTTagCompound c = getCompound();
		return (c != null ? c.hasKey(key) == true : false);
	}
	
	
	
	
	public NBT remove (String key) {
		NBTTagCompound c = getCompound();
		if (c == null) {
			return this;
		}
		c.remove(key);
		setCompound(c);
		return this;
	}
}