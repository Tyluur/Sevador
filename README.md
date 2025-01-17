# Sevador #666

---

**Sevador** is a high-performance, modular, and extensible game engine designed for running a RuneScape private server.

Built with Java, this engine provides a robust foundation for creating and managing a custom RuneScape server with a wide range of features, including player management, combat, trading, quests, and more. W

hether you're a developer looking to create your own RuneScape server or a contributor interested in game engine development, Sevador offers a powerful and flexible platform to build upon.


## Features

### **Core Engine Features**
- **Multi-threaded Architecture**: Utilizes `ExecutorService` for efficient task management, ensuring smooth gameplay even with a large number of concurrent players.
- **Modular Design**: Organized into well-defined packages (`com.sevador`, `com.sevador.game`, `com.sevador.content`, etc.), making the codebase easy to navigate, maintain, and extend.
- **Database Integration**: Supports MySQL for persistent storage of player data, items, and game state. Includes a connection pooling system for optimized database performance.
- **Network Communication**: Handles client-server communication with a custom packet system, ensuring low-latency and reliable gameplay.

### **Gameplay Features**
- **Player Management**:
   - Player actions (e.g., movement, combat, trading) are handled through a robust `Action` system.
   - Skill management, including experience gain, leveling, and skill-related actions (e.g., crafting, combat).
- **Combat System**:
   - Supports melee, magic, and ranged combat.
   - Special attacks, damage calculation, and combat animations.
   - Player vs. Environment (PvE) and Player vs. Player (PvP) mechanics.
- **Trading and Economy**:
   - Player-to-player trading with a secure trade interface.
   - Grand Exchange system for buying and selling items.
- **Quests**:
   - Customizable quest system with progress tracking, rewards, and completion conditions.
   - Example quest: "Cook's Assistant" with fully functional quest logic.
- **Teleportation**:
   - Supports various teleportation methods, including spells, items (e.g., teletabs), and special abilities.
   - Animated teleportation sequences with customizable graphics and animations.

### **Content Management**
- **Item System**:
   - Comprehensive item definitions, including equipment, consumables, and tradeable items.
   - Item degradation and charges for special items.
- **World Management**:
   - Dynamic world regions with support for multi-area zones, PvP zones, and safe zones.
   - Ground item management for dropped items.
- **Custom Commands**:
   - Admin, moderator, and player commands for server management and debugging.
   - Commands for spawning items, teleporting, and managing player states.

---

## Game Engine

### **Modular and Extensible Design**
The engine is designed with modularity in mind, allowing developers to easily add new features or modify existing ones. Each component (e.g., combat, trading, quests) is encapsulated in its own package, making the codebase clean and maintainable.

### **High-Performance Networking**
The engine uses a custom packet system for client-server communication, ensuring low-latency and efficient data transfer. The `ChannelHandler` class manages network connections, while `OutgoingPacket` and related classes handle packet construction and sending.

### **Robust Database Integration**
Sevador supports MySQL for persistent storage, with a connection pooling system (`ConnectionPool`) to optimize database performance. The `DatabaseConnection` and `MySQLDatabaseConnection` classes provide a flexible interface for database operations.

### **Advanced Combat System**
The combat system is one of the engine's standout features, supporting multiple combat styles (melee, magic, ranged) and special attacks. The `CombatAction` class handles damage calculation, animations, and combat mechanics, providing a seamless and engaging combat experience.

### **Customizable Quests**
The quest system is highly customizable, allowing developers to create unique quests with progress tracking, rewards, and completion conditions. The `Quest` class and its subclasses (e.g., `CooksAssistant`) provide a framework for implementing quest logic.

### **Player-Friendly Features**
- **Grand Exchange**: A fully functional trading system for buying and selling items.
- **Teleportation**: Supports various teleportation methods with customizable animations and graphics.
- **Player Commands**: A wide range of commands for server management, debugging, and player interaction.

---

## Getting Started

### **Prerequisites**
- Java Development Kit (JDK) 8 or higher.
- MySQL database server.
- Basic knowledge of Java and MySQL.


### Building and Running

1. Clone the repository to your local machine.
2. Navigate to the project directory and run the Gradle build command:
    ```sh
    gradle build
    ```

3. Start the server by running the main class `com.sevador.Main`.

## Contributing

Contributions are welcome! Please read the [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.

## Acknowledgments

- Special thanks to Tyluur and Emperor, the authors and significant contributors to the project.

## Support

For support, issues, or inquiries, please contact the project maintainers or open an issue on the GitHub repository.