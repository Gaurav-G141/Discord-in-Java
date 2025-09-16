DISCORD SCRAPER IN JAVA!!

-- Overview --

A series of Java classes, designed using Procedural Programming and the Discord API, to scrape the contents of a desired discord server.

If you're not used to discord scraping, I'd recommend watching https://www.youtube.com/watch?v=xh28F6f-Cds for a simple tutorial on how discord scraping works 
(Note: He uses python, not Java, but the general idea is still the same). 

I'd also recommend reading https://discord.com/developers/docs/reference, an extermely detailed guide on everything discord allows you to do with their API

-- Code Summary --

Main files

- Collector.java: Collects about 100 messages when called, given a Channel ID and the id of the last message scraped (so it knows where to start)

- ChannelScraper.java: Scrapes an entire channel and saves it to a file

- ServerScraper.java: Calls Channelscraper.java on every channel the user could access in said server

- LoadMessages.java: Reads the messages of a single channel (Note: The method to read the full server's messages still needs to be implemented)

- Messages.java: A small data sturcture holding a single discord message 
    - Used as significantly easier than holding everything as Strings/HashMaps
    - Does not store the full data of a message, only critical information like the author and content

-- What was used --

Fully coded in Java, help of Java.net and Java.IO for both online server reading and file saving

-- How to run --

Using the video above, find your token AND device and insert them into the fields in ServerScraper.java

From there, insert your server name and ID (Note: ID is a long, so use L)

Then, run ServerScraper.java

-- Future Updates -- 

- Class to load the entire server's messages
- Some data analysis code on said messages
- A way to filter out channels (Note: The user can manually decide what channels will be in their data by just deleting the channel folders they don't like, but putting it in code
would ensure that the scraper ignores said channels)
  - Would be useful if for example, a channel for using a bot would pollute the data a person may want

-- DATA STORAGE --

Due to the privacy and respect of the servers I am in, I unfortunately cannot provide an example of what the saved Message Data looks like. Here is, however, a typed up representation
- Server name (Directory)
  - Channel name (Directory)
    - A collection of .ser files. Each one contains at most 10,000 messages, which each message being roughly 100 bytes of data
  - Channel name (Directory)
    - A collection of .ser files. If the channel is small enough, then only .ser file is added
  - Channel name (Directory)
    - A collection of .ser files. 
  - etc

-- SAFETY NOTICE --

The following program requires your discord token. This token can be used to access your account, it is like your password

**DO NOT GIVE YOUR TOKEN TO ANY PERSON/PROGRAM UNLESS YOU HAVE FULLY READ AND UNDERSTAND THE FUNCTIONALITY OF SAID PROGRAM!**

**IF YOU DO MODIFY THE CODE FOR DIFFERENT PURPOSES, MAKE SURE YOU USE GET REQUESTS ONLY**

**DISCORD DOES NOT OFFICIALLY SUPPORT SCRAPING! THIS IS FOR EDUCATIONAL PURPOSES ONLY**

-- Created by --

Gaurav Gupta

UT Austin Computer Science C.O. 2029

Email: g.gupta31415@gmail.com

No lisense, anyone is free to use as they wish








