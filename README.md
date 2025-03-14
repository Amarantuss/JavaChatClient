# JavaChatClient
Java ChatClient using Socket Programming, pretty well made. Also my first project like that...

It uses Sockets so it should work just fine on a LAN network.

# Advantages

- MultiThreading
- JSON packet format
- Good enough base for future code expansion
- Easy to use
- Simple for learning

# How to use
You can use the default Client and Server classes for testing, adjust values like `ip` and `port` and start it.

You can also override the default CLI with you own implementation using the `ChatInterface` class.

If you stick to the default CLI look into the `resources\commands.md` file and see the commands list, now you can chat.

# Custom Interface

Your class need to implement the `ChatInterface` interface that contains one method `public void close()`. It's to tell the client that connection has been closed.

In order to register your new Interface run the `registerChat(ChatInterface)` method and then you can send and receive packets.

The default CLI will be also helpful

# TODO

Things you can expect to be added:
- GUI
- Some kind of basic encryption
- Updated packet codes and its description