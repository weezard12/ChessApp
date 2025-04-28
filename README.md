# Chess App

## Overview
Chess App is a feature-rich Android application that allows users to play chess against a bot or other players. The app offers a customizable interface with multiple themes, user accounts, and settings persistence.

## Features

### Game Modes
- **Bot Mode**: Play against an AI opponent (Shtokfish AI)
- **Player vs Player**: Local multiplayer mode for two players on the same device

### User Interface
- **Customizable Themes**: Choose from 7 different board themes:
  - Default
  - Chess.com
  - Brown
  - Sky
  - Clear
  - Light
  - Light Brown
- **Responsive Design**: Works on various Android device sizes
- **Move Highlighting**: Visual indicators for possible moves and selected pieces

### User Management
- **Account System**: Create and log in to user accounts
- **Guest Mode**: Play without creating an account
- **Settings Persistence**: User preferences are saved to a local database

### Audio
- **Sound Effects**: Game sounds for piece movement and captures
- **Adjustable Volume**: Control the in-game audio volume

## Technical Details

### Architecture
- **Java-based Android Application**: Built using Android SDK
- **SQLite Database**: Stores user accounts and settings
- **Custom Views**: Specialized chess board and piece rendering

### Key Components
- **Chess Engine**: Handles game logic, move validation, and AI opponent
- **Board Renderer**: Draws the chess board and pieces with customizable themes
- **User Session Management**: Handles authentication and user state
- **Settings System**: Manages and persists user preferences

### Theme System
The app implements a flexible theme system that allows users to customize the appearance of the chess board:
- Themes are defined as `BoardColors` objects with color values for:
  - White squares
  - Black squares
  - Selected tiles
  - Move highlights
- Themes are stored by index in the database for efficiency

## Getting Started

### Prerequisites
- Android Studio
- Android SDK (minimum API level required)
- Java Development Kit (JDK)

### Installation
1. Clone the repository:
   ```
   git clone https://github.com/weezard12/ChessApp.git
   ```
2. Open the project in Android Studio
3. Build and run the application on an emulator or physical device

## Future Enhancements
- Online multiplayer
- Chess puzzles and challenges
- Game analysis tools
- Tournament mode
- ELO rating system

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments
- Chess piece designs inspired by standard chess notation
- Board themes influenced by popular chess platforms

---

Developed by weezard12
