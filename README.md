
## ChatAppSystem

This project is a fully functional real-time chat application designed to support Admin and User roles, each with specific capabilities for managing and participating in chat rooms. The application is built using Java Swing for the front-end user interface, Java RMI for chat communication, and Hibernate for interacting with a MySQL backend.

### Features:
- User Management:Users can create accounts, update their profiles, and subscribe/unsubscribe to chats.
- Admin Controls:The Admin can create chat rooms, manage user subscriptions, and remove users from the system.
- Chat Functionality:Real-time notifications are sent when a chat starts and users join or leave. Only subscribed users can participate in the chat.
- Chat History:All chat messages are saved in `.txt` files and linked to the chat in the database.
- Profile Display:During the chat, user profiles (nickname and profile picture) are displayed alongside their messages.
- Threaded Chat System:Each chat runs in its own thread, ensuring smooth interaction between multiple users.

### Technologies Used:

- Java Swing for GUI Design
- Java RMI for real-time communication
- Hibernate for ORM (Object-Relational Mapping) with MySQL
- MySQL for backend storage
- Observer Pattern for managing chat subscriptions and event notifications

