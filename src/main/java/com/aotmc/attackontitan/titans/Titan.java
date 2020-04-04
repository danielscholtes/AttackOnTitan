package com.aotmc.attackontitan.titans;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.util.Utils;

import net.md_5.bungee.api.ChatColor;

public class Titan {
	
	private Giant giant;
	private Zombie zombie;
	private Slime slime;
	private int size;
	private TitanType titanType;
	private int grabTaskID = -1;
	private boolean isGrabbing = false;
	private Chicken grabEntity;
	private TitanData data;
	private UUID grabbedPlayer;

	public Titan(Location spawnLocation, TitanType titanType, int size, TitanData data) {
		this.size = size;
		this.titanType = titanType;
		this.data = data;
		spawnTitan(spawnLocation);
	}


	@SuppressWarnings("deprecation")
	public void spawnTitan(Location spawnLocation) {
		zombie = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
		zombie.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 0, false, false));
		zombie.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999999, 1, false, false));
		zombie.setSilent(true);
		zombie.setCustomNameVisible(false);
		zombie.setCanPickupItems(false);
		zombie.setInvulnerable(true);
		zombie.setTarget(null);
		zombie.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
		zombie.getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
		if (zombie.getVehicle() != null) {
			zombie.getVehicle().remove();
		}
		
		giant = (Giant) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.GIANT);
		giant.setSilent(true);
		giant.setCustomNameVisible(true);
		giant.setCustomName(ChatColor.translateAlternateColorCodes('&', "&6" + size + " Meter Titan"));
		giant.setCanPickupItems(false);
		giant.setInvulnerable(true);
		giant.setAI(false);
		
		slime = (Slime) spawnLocation.getWorld().spawnEntity(spawnLocation.add(0D, 7.8D, 0D), EntityType.SLIME);
		slime.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 0, false, false));
		slime.setSize(5);
		slime.setCustomNameVisible(false);
		slime.setCanPickupItems(false);
		slime.setAI(false);
		slime.setGravity(false);
		slime.setMaxHealth(20);
		slime.setHealth(slime.getMaxHealth());
	}
	 
	public int getSize() {
		return size;
	}
	 
	public TitanType getType() {
		return titanType;
	}
	
	public Giant getGiant() {
		return this.giant;
	}
	public Slime getSlime() {
		return this.slime;
	}
	
	public Zombie getZombie() {
		return this.zombie;
	}

	public void syncEntities() {
		for (int i = 1; i <= 10; i++) {
			if (this.zombie.getLocation().add(0D, i, 0D).getBlock() != null && this.zombie.getLocation().add(0D, i, 0D).getBlock().getType() != Material.AIR) {
				this.zombie.teleport(slime.getLocation().add(0D, -7.8D, 0D));
				break;
			}
		}
		this.giant.teleport(this.zombie);
		double newX;
		double newZ;
		float nang = this.giant.getLocation().getYaw() + 90;

		if (nang < 0) {
			nang += 360;
		}

		newX = Math.cos(Math.toRadians(nang));
		newZ = Math.sin(Math.toRadians(nang));

		Location loc = new Location(this.giant.getWorld(), this.giant.getLocation().getX() - newX, this.giant.getLocation().getY(),
				this.giant.getLocation().getZ() - newZ, this.giant.getLocation().getYaw(), this.giant.getLocation().getPitch());
		
		this.slime.teleport(loc.add(0D, 7.8D, 0D));
		if (isGrabbing && grabEntity != null) {
			double newXGrab;
			double newZGrab;
			float nangGrab = getGiant().getLocation().getYaw() - 60;

			if (nangGrab < 0) {
				nangGrab += 360;
			}

			newXGrab = Math.cos(Math.toRadians(nangGrab)) * 3.7;
			newZGrab = Math.sin(Math.toRadians(nangGrab)) * 3.7;
			Method[] methods = ((Supplier<Method[]>) () -> {
			    try {
			        Method getHandle = Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".entity.CraftEntity").getDeclaredMethod("getHandle");
			        return new Method[] {
			                getHandle, getHandle.getReturnType().getDeclaredMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class)
			        };
			    } catch (Exception ex) {
			        return null;
			    }
			}).get();
			
			Location locGrab = new Location(getGiant().getWorld(), getGiant().getLocation().getX() - newXGrab, getGiant().getLocation().getY(),
					getGiant().getLocation().getZ() - newZGrab, getGiant().getLocation().getYaw() - 90, getGiant().getLocation().getPitch());
			
			try {
			    methods[1].invoke(methods[0].invoke(grabEntity), locGrab.getX(), locGrab.getY() + 7.25D, locGrab.getZ(), locGrab.getYaw(), locGrab.getPitch());
			} catch (Exception ex) {
				
			}
		}
	}
	
	public void grabPlayer(UUID uuid) {
		isGrabbing = true;
		grabbedPlayer = uuid;
		grabEntity = (Chicken) this.getGiant().getWorld().spawnEntity(this.getGiant().getLocation(), EntityType.CHICKEN);
		grabEntity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 0, false, false));
		grabEntity.setCanPickupItems(false);
		grabEntity.setAI(false);
		grabEntity.setCustomNameVisible(false);
		grabEntity.setGravity(false);
		grabEntity.setSilent(true);
		grabEntity.addPassenger(Bukkit.getPlayer(uuid));
		grabEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(0);
		data.getGrabbedPlayers().put(uuid, grabEntity.getEntityId());
		grabTaskID = new BukkitRunnable() {
			int count = 8;
			
			@Override
			public void run() {
				if (Bukkit.getPlayer(uuid) == null) {
					if (data.getGrabbedPlayers() != null && data.getGrabbedPlayers().containsKey(uuid)) {
						data.getGrabbedPlayers().remove(uuid);
					}
					isGrabbing = false;
					this.cancel();
					return;
				}
				if (count == 0) {
					Bukkit.broadcastMessage(Utils.color("&c" + Bukkit.getPlayer(uuid).getName() + " &7was eaten by a titan!"));
					Bukkit.getPlayer(uuid).setHealth(0);
					
					if (data.getGrabbedPlayers() != null && data.getGrabbedPlayers().containsKey(uuid)) {
						data.getGrabbedPlayers().remove(uuid);
					}
					isGrabbing = false;
					this.cancel();
					return;
				}
				
				Utils.message(Bukkit.getPlayer(uuid), "&7You will die in &c" + count + "&7 seconds!");
				count--;
			}
		}.runTaskTimer(AttackOnTitan.getInstance(), 0L, 20L).getTaskId();
	}
	
	public boolean isGrabbing() {
		return isGrabbing;
	}

	public void remove() {
		if (grabTaskID != -1) {
			Bukkit.getScheduler().cancelTask(grabTaskID);
		}
		this.slime.remove();
		this.giant.remove();
		this.zombie.remove();
		if (grabEntity != null) {
			this.grabEntity.remove();	
		}
		if (grabbedPlayer != null && data.getGrabbedPlayers() != null && data.getGrabbedPlayers().containsKey(grabbedPlayer)) {
			data.getGrabbedPlayers().remove(grabbedPlayer);
		}
	}
}
