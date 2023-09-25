# Playground application

This application allows to create attractions, kids, playgrounds and playground attractions.


## Entities of application
Attraction has name, type, capacity.

Name has to be unique, it is mandatory and up to 255 symbols.

Attractions are of four types: carousel, double swings, ball pit and slide.

Capacity of attraction could be from 1 to 50.

------------------------------------------------------
Kid has name, age, ticket and customer code.

Name is mandatory and up to 255 symbols.

Age should be from 3 to 15.

Ticket number is generated by the application.

Customer code is generated by the application.

------------------------------------------------------
Playground has name, list of attractions, capacity, list of kids, queue, count of visitors and queue count.

Name has to be unique, it is mandatory and up to 255 symbols.

List of attractions is generated from provided attractions to be used to build a playground.

Capacity is calculated by the application.

List of kids shows what kids are playing at the playground at the moment.

Queue is build by application, when there are no free places on attractions, but kid wants to wait.

Count of visitors shows how many kids played and are playing at the playground.

Queue count increases each time a new kid is enqueued.

## Features
It is possible to buy a ticket for a kid, so he/she could be admitted into playground.

It is possible to add a kid to a playground.

It is possible to enqueue a kid, when playground is full and kid wants to wait.

it is possible to remove a kid from playground or playground queue.

It is possible to check visitors number of all playgrounds.

It is possible to check utilization of particular attraction on playground.

It is possible to check utilization of particular playground.

# Logic
Would kid want to wait, when playground is full, is determined by application.

It is possible to make that kid always waits.

When adding kid to a playground, it is determined accidentally which attraction kid will go.

When removing kid from playground, also, it is determined accidentally which attraction kid will go off.

There are 3 types of calculation of attraction utilization. 

## Initial setup
It is possible to insert initial data into database. It is necessary to set property 'insert.initial.data' to 'true'.

Also, if property 'add.tickets' set to 'true', for all kids tickets are added, except one.

Set property 'kid.always.waits' to 'true' and kid will always want to wait.
