# Java Group Chat Application

## Project Description
This is a simple group chat application built in Java, allowing multiple users to communicate in real-time. The application supports basic chat functionalities and demonstrates the use of Java's networking and multithreading capabilities.

## Features
- Real-time group chat
- User authentication
- Message broadcasting
- Private messaging
- Simple and intuitive UI

## Technologies Used
- Java
- JavaSwing (for GUI)
- Socket Programming
- Multithreading

## Installation

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Git

### Steps
1. Clone the repository:
    ```sh
    git clone https://github.com/your-username/java-group-chat.git
    ```
2. Navigate to the project directory:
    ```sh
    cd java-group-chat
    ```
3. Compile the project:
    ```sh
    javac -d bin src/**/*.java
    ```
4. Run the server:
    ```sh
    java -cp bin com.yourpackage.Server
    ```
5. In a new terminal, run the client:
    ```sh
    java -cp bin com.yourpackage.Client
    ```

## Usage
1. Start the server by running the `Server` class.
2. Start one or more clients by running the `Client` class.
3. Enter your username and connect to the chat.
4. Begin chatting with other connected users.

## Contribution
1. Fork the repository.
2. Create a new branch:
    ```sh
    git checkout -b feature/your-feature-name
    ```
3. Make your changes.
4. Commit your changes:
    ```sh
    git commit -m 'Add some feature'
    ```
5. Push to the branch:
    ```sh
    git push origin feature/your-feature-name
    ```
6. Open a pull request.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgements
- Inspired by various online chat applications.
- Thanks to the Java community for continuous support and contributions.

