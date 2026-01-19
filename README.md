<div align="center">

![Player Log Icon](src/main/resources/assets/player-log/icon.png)

# Player Log 📨

</div>

中文 | [English](./README_en.md)

---

### 介绍 🎯

Player Log 是一个 Fabric 模组，允许玩家通过私密的方式向服务端控制台发送消息。相比于使用默认聊天栏消息（会暴露给所有玩家），该模组提供了一种隐私的替代方案。这对于需要读取服务端标准输入的外部工具（如 [melobot-protocol-mcpm](https://github.com/aicorein/melobot-protocol-mcpm)）特别有用。

### 应用场景 🔒

- 🔒 **隐私通信**：在多人服务器中私密地向服务端发送信息
- 🧑‍💻 **单人游戏**：在单人游戏中向本地服务端发送消息
- 🔌 **外部工具集成**：与基于其他语言实现的 Minecraft 服务端 stdin 读取工具无缝集成

### 游戏版本 🎮

- **Minecraft 版本**：1.21.11
- **加载器**：Fabric Loader ≥ 0.18.4
- **Java 版本**：Java 21 或更高版本

### 功能和配置 ⚙️

#### 基本命令 🧭

**主命令**：`/playerlog`
- 功能：显示模组版本信息

**日志命令**：`/playerlog send <消息>` 或 `/s <消息>`
- 功能：将消息发送到服务端控制台（此命令在使用时可保留消息的历史记录，默认 10 条，可通过配置调整）
- 限制：消息长度不超过 128 个字符（可通过配置调整）

**配置重载命令**：`/playerlog reload`
- 功能：热重载配置（部分配置选项可能滞后，例如消息的历史记录，需要重新进入服务器）

#### 消息格式 🗒️

发送到服务端控制台的消息格式如下：
```text
[player-log] <玩家名>: <消息>
```

#### 多语言支持 🌐

该模组完全支持多语言，命令输出会自动根据玩家的游戏语言设置进行翻译。目前支持：
- 中文（简体）
- 英文

#### 命令别名 ✨

为了方便快速使用，模组提供了短命令别名 `/s`，可以直接替代 `/playerlog send`。

#### 配置文件 🧩

配置文件将在首次运行后自动生成：`config/player-log/config.json`。

```json
{
    // 单次命令发送消息的最大长度
    "maxLogMessageLength": 128,
    // 使用命令时，最多保留多少条消息的历史记录
    "maxLogMessageHistory": 10,
    // 为防止玩家频繁发送命令，导致服务器日志文件倍增，还可设置消息发送的速率限制
    // 单位时间内允许单个玩家发送消息多少条
    "logMessageMaxPermits": 1,
    // 单位时间对应多少毫秒，和上面的配置项实现限制：1条/2.5s
    "logMessageMaxInterval": 2500
}
```

### 未来计划 🚀

该模组目前基于 Fabric 框架开发。未来我们计划支持其他平台，包括：

- **NeoForge**：为 NeoForge 加载器用户提供支持
- **其他 Minecraft 插件平台**：如 Spigot、Paper 等，为服务器管理员提供更多选择
- **模组扩展**：增加更多功能和配置选项
