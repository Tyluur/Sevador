# Sevador-666

![License](https://img.shields.io/badge/license-MIT-blue.svg)

Sevador-666 is a custom RuneScape game server built for RuneScape build 666. It provides a robust and scalable solution for hosting and managing a RuneScape game world.

## Features

### Core Features

- **Multi-threaded Architecture**: Utilizes a fixed thread pool to efficiently manage concurrent tasks and updates.
- **Node Worker System**: Manages the game nodes and ensures smooth gameplay.
- **Event Management**: Handles various game events such as button actions, magic spells, special attacks, NPC actions, object actions, and item actions.
- **Networking**: Includes a custom channel handler for managing network connections.
- **Logging**: Utilizes a custom logger for detailed server logging.
- **Cross-Platform Compatibility**: Detects the operating system and adjusts debugging settings accordingly.

### World Class Features

- **Background Loader**: Executes background loading tasks.
- **MySQL Connection Pool**: Manages MySQL database connections.
- **Tick Management**: Manages and executes various game ticks.
- **Area Manager**: Manages different game areas.
- **Configuration Loading**: Loads server configuration from a file.
- **Player Management**: Manages player instances within the game world.
- **Drop Loader**: Handles item drop loading.

### Major Update Worker

The `MajorUpdateWorker` class is responsible for handling major updates within the game. It runs on a separate thread with maximum priority and keeps track of the number of cycle ticks.

### Entity Update Sequence

The `EntityUpdateSequence` class is responsible for updating entities within the game world. It includes:

- **Preparation Phase**: Updates players and NPCs, including their walking queues, actions, update masks, and skills.
- **Execution Phase**: Executes parallel updates for each player, including music, scene graph building, player rendering, and NPC rendering.
- **Finalization Phase**: Finalizes updates for players and NPCs, including update masks and damage lists.

## Getting Started

[Sevador 666 Client](https://mega.nz/file/kYRxBTYL#QGYzufwAUf_XPz_DkqFwJVlZRIsV8Mz7Irz4yK5KPF4)

### Prerequisites

- JDK 1.8 or higher
- Gradle for build management

### Building and Running

1. Clone the repository to your local machine.
2. Navigate to the project directory and run the Gradle build command:
   \`\`\`
   gradle build
   \`\`\`
3. Start the server by running the main class `com.sevador.Main`.

## Contributing

Contributions are welcome! Please read the [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Acknowledgments

- Special thanks to Tyluur and Emperor, the authors and significant contributors to the project.

## Support

For support, issues, or inquiries, please contact the project maintainers or open an issue on the GitHub repository.
