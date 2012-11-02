mnf-storm
=========
This project is a very simple [Storm](http://storm-project.net) topology for a talk that I gave as
an introduction to Storm. The idea for the demo comes from a
[screencast](http://storm.twitsprout.com/) by Adrian Petrescu. Some of the code also comes from that
screencast.

The basic idea is that the topology will read tweet data that was gathered from a Monday Night
Football game from an
[event-server](http://github.com/dkincaid/event-server) in a JSON format and produce a scoreboard of
counts for each of the teams.

The tweets were gathered using [twitter-client](http://github.com/dkincaid/twitter-client) that I
created to slurp tweets off the Twitter streaming api.

Branches
--------------
The `complete` and `master` branches should have full working versions of the code. The `barebones`
branch has none of the topology code in it, so is what I used to start the live coding demo.

Building the code
---------------------------
The project uses Maven to build so once you've got Maven installed just run a
    mvn assembly:assembly
which will produce an uberjar in the `target` directory
    
Running the topology
---------------------------------
You can run the demo using
    java -jar mnf-storm-0.1-with-dependencies.jar
This will launch the topology in a local cluster.
