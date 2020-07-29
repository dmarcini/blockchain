# Blockchain

A multithreaded console application that implements a
cryptocurrency mining simulator.
</br>
A main unit is block. Single block keep a lot of information like:
* block id
* block create timestamp
* nonce
* hash of previous block
* hash this block
* list of transactions
* miner who mined this block

Blocks are keeping in data structure called blockchain
where there are inseparably connected. To add a new block, you need to find
a magic number that will allow you to calculate the correct hash of this block.
This work is doing by miners. While miners mining block, other users
(miners and clients) of blockchain can do transactions which are added
to this block. When the magic number is found, a new block is added to
blockchain and all transactions are executed. Then, a new block is generated
and miners start to search a magic number from the beginning and new
transactions are added to this new block.
</br></br>
Single transaction keep information like:
</br>
<ul>
    <li>transaction id</li>
    <li>user which do send resources to recipient</li>
    <li>user which is recipient resources</li>
    <li>resources which are an amount of cryptocurrency</li>
    <li>signature of this transaction to verify its correctness</li>
    <li>public key to verify signature of this transaction</li>
</ul>
Transaction can be rejected when client hasn't enough resource in his wallet
but despite this, information about this transaction is keep in the block.
</br>
For every block mined, miner who found as first magic number
is getting a specific reward in cryptocurrency.
---

## Technology
* Java SE
* Apache Maven
---

## Requirements
* Java SE 14 JRE installed
* Apache Maven 3.6.x installed (at least)
---

## Building & Running
Example for Linux system
```
git clone https://github.com/dmarcini/blockchain
cd blockchain
mvn package

java -jar target/blockchain-1.0-SNAPSHOT.jar
```
---

## Sources
Project was based on Jetbrains Academy tutorial - Blockchain: </br>
https://hyperskill.org/projects/50
