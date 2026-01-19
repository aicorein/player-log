<div align="center">

![player-log Icon](src/main/resources/assets/player-log/icon.png)

# player-log

</div>

[中文](./README.md) | English

---

### Introduction 🎯

player-log is a Fabric mod that enables players to send messages directly to the server console in a private manner. Unlike default chat messages which are visible to all players, this mod offers a discreet alternative. This is particularly useful for external tools that rely on reading the server's standard input (stdin), such as [melobot-protocol-mcpm](https://github.com/aicorein/melobot-protocol-mcpm).

### Use Cases 🔒

- 🔒 **Private Communication**: Send information privately to the server in a multiplayer environment.
- 🧑‍💻 **Single Player**: Send messages to the local server in single-player mode.
- 🔌 **External Tool Integration**: Seamlessly integrate with external Minecraft server stdin reading tools implemented in other languages.

### Game Version 🎮

* **Minecraft Version**: 1.21.11
* **Loader**: Fabric Loader ≥ 0.18.4
* **Java Version**: Java 21 or higher

### Features & Configuration ⚙️

#### Basic Commands 🧭

**Main Command**: `/playerlog`

* Function: Displays mod version information.

**Log Command**: `/playerlog send <message>` or `/s <message>`

* Function: Sends a message to the server console. (This command maintains a history of sent messages for the user; defaults to 10 entries, configurable).
* Limitation: Message length must not exceed 128 characters (configurable).

**Config Reload Command**: `/playerlog reload` (Needs permission level 4 - server operator)

* Function: Hot reloads the configuration. (Note: Some configuration changes, such as message history size, may require re-joining the server to take full effect).
* Limitation: Only available to server operators, requires permission level 4.

#### Message Format 🗒️

The format of messages sent to the server console is as follows:

```text
[player-log] <player_name> <message>
```

#### Multi-Language Support 🌐

This mod fully supports multiple languages. Command outputs are automatically translated based on the player's game language settings. Currently supported:

* Chinese (Simplified)
* English

#### Command Aliases ✨

For quick usage, the mod provides a short command alias `/s`, which can be used as a direct replacement for `/playerlog send`.

#### Config File 🧩

The configuration file is automatically generated after the first run: `config/player-log/config.json`.

```json5
{
    // Maximum length for a single command message
    "maxLogMessageLength": 128,
    // Maximum number of message history entries to keep when using the command
    "maxLogMessageHistory": 10,
    // To prevent players from spamming commands and bloating server logs, 
    // a rate limit can be set.
    // The number of messages a single player is allowed to send per unit of time.
    "logMessageMaxPermits": 1,
    // The duration of the "unit of time" in milliseconds.
    // Combined with the setting above, this implements the limit: 1 message per 2.5s
    "logMessageMaxInterval": 2500
}

```

### Future Plans 🚀

This mod is currently developed based on the Fabric framework. In the future, we plan to support other platforms, including:

* **NeoForge**: Support for NeoForge loader users.
* **Other Minecraft Plugin Platforms**: Such as Spigot, Paper, etc., providing more choices for server administrators.
* **Mod Expansion**: Adding more features and configuration options.
