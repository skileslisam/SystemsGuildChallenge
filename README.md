# SystemsGuildChallenge
Systems Guild Inc programming challenge

Description of challenges:

I ended up deviating significantly from the challenge's instructions due to a few issues I encountered. This starts with setting up the adventureworks2017 database. My computer did not want to properly run Microsoft's SQL software, so I created a server with Microsoft Azure. I was unable to find a way to upload the adventureworks2017 database, so I went ahead on the project with the adventureworksLT database instead. I chose to use the NetBeans IDE due to familiarty and time constraints and got the IDE to communicate with my azure server, but I could not get my program to communicate with the database. I've included screenshots showing my azure server and the database displayed in my IDE.

From there, I decided to replicate the data tables from the database in some excel sheets to display some of my other coding abilities. Currently, the program does not take user input and runs on the console, but will calculate the average amount spent per transaction based on the zipcode. Because the adventureworksLT only has transactions from a single day listed, currently the program does not take into account a range of dates. Given more time, I would address this by adding or changing some of the data and coding this feature.

Finally, I could not get my IDE to communicate with gitHub, so I've manually added my code as a couple of text files and uploaded a zipped version of the project. 

Features I would/can code with extra time:
-user input
-A basic GUI using Java Swing for input/output
-extra data entries with varying times
-take into account range of dates
