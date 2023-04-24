#!/bin/bash

###########################################################

#                   after running script

###########################################################

#go to /opt/kafka/config/zookeeper.properties ->dataDir=/opt/kafka/data/zookeeper

#go to /opt/kafka/config/server.properties -> log.dirs=/opt/kafka/data/kafka

#in case you want multi brokers #listeners=PLAINTEXT://:9092 #advertised.listeners=PLAINTEXT://your.host.name:9092

#uncomment these lines in server.properties and put the ip of the current machine







KAFKA_VERSION="3.4.0"

KAFKA_SCALA_VERSION="2.13"

read -p "Enter username: " username



echo "Enter your username:"



if id "$username" >/dev/null 2>&1; then

    echo "User $username exists"

else

    echo "User $username does not exist"

    exit 1

fi

# Check if Kafka is already installed

if [ -d "/opt/kafka" ]; then

    echo "Kafka is already installed. Aborting."

    exit 1

fi



# Check if there is enough space in /opt

if [ "$(df -Pk /opt | tail -1 | awk '{print $4}')" -lt "500000" ]; then

    echo "There is not enough free space in /opt. Aborting."

    exit 1

fi

echo "/tmp/kafka_$KAFKA_SCALA_VERSION-$KAFKA_VERSION.tgz"

if [ -e "/tmp/kafka_$KAFKA_SCALA_VERSION-$KAFKA_VERSION.tgz" ]; then

   echo "tar file already installed"

else

  # Download Kafka

  echo "Downloading Kafka from https://downloads.apache.org/kafka/$KAFKA_VERSION/kafka_$KAFKA_SCALA_VERSION-$KAFKA_VERSION.tgz to /tmp/kafka_$KAFKA_SCALA_VERSION-$KAFKA_VERSION.tgz"

  wget "https://downloads.apache.org/kafka/$KAFKA_VERSION/kafka_$KAFKA_SCALA_VERSION-$KAFKA_VERSION.tgz" -P /tmp || {

      echo "Failed to download Kafka. Aborting."

      exit 1

  }

fi



ls -l /tmp/kafka_$KAFKA_SCALA_VERSION-$KAFKA_VERSION.tgz

# Extract Kafka

tar -xzf /tmp/kafka_$KAFKA_SCALA_VERSION-$KAFKA_VERSION.tgz -C /opt || {

    echo "Failed to extract Kafka. Aborting."

    exit 1

}



# Rename the Kafka directory

mv /opt/kafka_$KAFKA_SCALA_VERSION-$KAFKA_VERSION /opt/kafka || {

    echo "Failed to rename Kafka directory. Aborting."

    exit 1

}

chown -R $username:$username /opt/kafka

mkdir /opt/kafka/data

mkdir /opt/kafka/data/kafka



# Configure the Kafka environment variables

echo "export KAFKA_HOME=/opt/kafka" >> ~/.bashrc

echo "export PATH=\$PATH:\$KAFKA_HOME/bin" >> ~/.bashrc



# Reload the shell configuration

source ~/.bashrc



# Clean up

rm /tmp/kafka_$KAFKA_SCALA_VERSION-$KAFKA_VERSION.tgz || {

    echo "Failed to remove downloaded Kafka archive. Please delete it manually."

    exit 1

}



echo "Kafka installation complete. location: /opt/kafka"