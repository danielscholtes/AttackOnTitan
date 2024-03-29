package com.aotmc.attackontitan.titans;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityZombie;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.general.util.Utils;

public class Titan {

	private Giant giant;
	private Slime slime;
	private int size;
	private TitanType titanType;
	private int grabTaskID = -1;
	private boolean isGrabbing = false;
	private Chicken grabEntity;
	private TitanData data;
	private UUID grabbedPlayer;

	public Titan(Location spawnLocation, TitanType titanType, TitanData data) {
		if (titanType == TitanType.LARGE) {
			size = AttackOnTitan.getInstance().getRandom().nextInt((15 - 11) + 1) + 11;
		}
		if (titanType == TitanType.MEDIUM) {
			size = AttackOnTitan.getInstance().getRandom().nextInt((10 - 7) + 1) + 7;
		}
		if (titanType == TitanType.SMALL) {
			size = AttackOnTitan.getInstance().getRandom().nextInt((6 - 3) + 1) + 3;
		}
		this.titanType = titanType;
		this.data = data;
		spawnTitan(spawnLocation);
	}


	@SuppressWarnings("deprecation")
	public void spawnTitan(Location spawnLocation) {
		/*zombie = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
		zombie.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
		zombie.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1, false, false));
		zombie.setSilent(true);
		zombie.setCustomNameVisible(false);
		zombie.setCanPickupItems(false);
		zombie.setInvulnerable(true);
		zombie.setAI(true);
		zombie.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
		zombie.getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
		if (zombie.getVehicle() != null) {
			zombie.getVehicle().remove();
		}
		zombie.setPersistent(false);*/

		EntityTitan titan = new EntityTitan(spawnLocation.getWorld());
		titan.setPositionRotation(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), spawnLocation.getYaw(), spawnLocation.getPitch());
		((CraftWorld)spawnLocation.getWorld()).getHandle().addEntity(titan, CreatureSpawnEvent.SpawnReason.CUSTOM);
		giant = (Giant) Bukkit.getEntity(titan.getUniqueID());
		giant.setSilent(true);
		giant.setCustomNameVisible(true);
		giant.setCustomName(Utils.color("&6" + size + " Meter Titan"));
		giant.setCanPickupItems(false);
		giant.setInvulnerable(true);
		giant.setPersistent(false);
		
		slime = (Slime) spawnLocation.getWorld().spawnEntity(spawnLocation.add(0D, 7.5D, 0D), EntityType.SLIME);
		slime.setSize(6);
		slime.setCustomNameVisible(false);
		slime.setCanPickupItems(false);
		slime.setAI(false);
		slime.setGravity(false);
		slime.setMaxHealth(size * 100);
		slime.setHealth(slime.getMaxHealth());
		slime.setPersistent(false);
		Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), () -> {
			slime.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
		}, 3L);
		data.getTitans().put(slime.getEntityId(), this);
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

	public void syncEntities() {
		float nang = this.giant.getLocation().getYaw() + 90;

		if (nang < 0) {
			nang += 360;
		}

		double newX = Math.cos(Math.toRadians(nang));
		double newZ = Math.sin(Math.toRadians(nang));

		Location loc = new Location(this.giant.getWorld(), this.giant.getLocation().getX() - newX, this.giant.getLocation().getY(),
				this.giant.getLocation().getZ() - newZ, this.giant.getLocation().getYaw(), this.giant.getLocation().getPitch());
		
		this.slime.teleport(loc.add(0D, 7.8D, 0D));
		if (isGrabbing && grabEntity != null) {
			float nangGrab = getGiant().getLocation().getYaw() - 60;

			if (nangGrab < 0) {
				nangGrab += 360;
			}

			double newXGrab = Math.cos(Math.toRadians(nangGrab)) * 3.7;
			double newZGrab = Math.sin(Math.toRadians(nangGrab)) * 3.7;
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
		grabEntity.setPersistent(false);
		grabEntity.addPassenger(Bukkit.getPlayer(uuid));
		grabEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(0);
		data.getGrabbedPlayers().put(uuid, grabEntity.getEntityId());
		grabTaskID = new BukkitRunnable() {
			int count = 15;
			
			@Override
			public void run() {
				if (Bukkit.getPlayer(uuid) == null) {
					if (data.getGrabbedPlayers() != null && data.getGrabbedPlayers().containsKey(uuid)) {
						data.getGrabbedPlayers().remove(uuid);
					}
					isGrabbing = false;
					grabEntity.remove();
					this.cancel();
					return;
				}
				if (count == 0) {
					Bukkit.broadcastMessage(Utils.color("&2" + Bukkit.getPlayer(uuid).getName() + " &7has been devoured by a Titan!"));
					Bukkit.getPlayer(uuid).setHealth(0);
					
					if (data.getGrabbedPlayers() != null && data.getGrabbedPlayers().containsKey(uuid)) {
						data.getGrabbedPlayers().remove(uuid);
					}
					isGrabbing = false;
					grabEntity.remove();
					this.cancel();
					return;
				}
				
				Utils.message(Bukkit.getPlayer(uuid), "&7You will be devoured in &2" + count + "&7 seconds!");
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
		if (grabEntity != null) {
			if (grabbedPlayer != null){
				this.grabEntity.removePassenger(Bukkit.getPlayer(grabbedPlayer));
			}
			this.grabEntity.remove();
		}
		if (grabbedPlayer != null && data.getGrabbedPlayers() != null && data.getGrabbedPlayers().containsKey(grabbedPlayer)) {
			data.getGrabbedPlayers().remove(grabbedPlayer);
		}
	}
}
