# 📩 Downloads
[Download here](https://modrinth.com/plugin/)
# 📖 What is this plugin?
This is a highly configurable Minecraft event manager plugin that allows you to dynamically manage events, spawn points, and player interactions. It provides a user-friendly interface for setting up and controlling various aspects of your Minecraft server events.
# ✅ Features & Commands
- Spawn
  - This new spawn feature has come with more configurable stuff like precise or rounded up coordinates and rotations. A dedicated spawn editor to set the spawn is also included.
  - /spawn <editor | delete>
- Deathban
  - This feature can be used to ban players with custom effects automatically when they die.
# 🚨 Permissions
- eventmanager.admin.spawn
- eventmanager.spawn
- eventmanager.deathban.bypass
# 🗓️ Coming Soon...
- [ ] Freeze players
- [ ] Announce a message
- [x] Set a spawn
- [ ] Scattered Spawnpoints
- [x] Custom death effects
- [ ] Merchant
- [ ] Teams
# 💾 Config
```
# ==========================================
# EventManager Configuration File v0.3.1
# ==========================================

# Messages
no-permission-message: "&cYou do not have permission to execute this command!"
spawn-set-message: "&aSpawn point saved successfully!"
commandsender-error: "&cOnly players can execute this command!"
invalid-arguments-error: "&cInvalid argument(s)!"
spawn-delete-message: "&aSpawn location deleted successfully!"
spawn-not-set-message: "&cSpawn has not been set yet!"
spawn-cooldown-message: "&cTry again in %seconds% second(s)."
spawn-moved-cancelled-message: "&cTeleportation cancelled because you moved!"
spawn-teleport-success-message: "&aTeleported to spawn!"

# Action Bar Text (%seconds% is replaced dynamically)
spawn-warmup-actionbar: "&fTeleporting in &b%seconds%&f seconds... Do not move!"

# Movement Tolerance (Distance in blocks a player can move before cancelling)
movement-threshold-blocks: 1.0

# Sounds (Bukkit Sounds)
teleport-tick-sound: "BLOCK.NOTE_BLOCK.HAT"
teleport-success-sound: "ENTITY.ENDERMAN.TELEPORT"

# Set Spawn Defaults
use-precise-coordinates: false
use-precise-yaw: false
use-precise-pitch: false
wait-time: 5
delay: 15

# Deathban settings
# Leave deathban-announcement empty to disable it.
custom-death-effects: true
deathban-announcement: "&c💀 %player% has been killed."
deathban-kick-message: "&cYou have died."
deathban-message: "You are dead."

# Other
# Leave messages empty to use vanilla messages.
ignore-bed-spawn: false
join-message: "&e%player% has joined the server."
quit-message: "&e%player% has left the server."
```

