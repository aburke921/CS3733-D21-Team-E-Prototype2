-- This is the sql file where the scripts were gathered.
-- It does not affect Edb.java or any code in any way.

-- Create Statements:

create table node
(
    nodeID    varchar(31) primary key,
    xCoord    int        not null,
    yCoord    int        not null,
    floor     varchar(5) not null,
    building  varchar(20),
    nodeType  varchar(10),
    longName  varchar(50),
    shortName varchar(35),
    unique (xCoord, yCoord, floor)
);

create table hasEdge
(
    edgeID    varchar(63) primary key,
    startNode varchar(31) not null references node (nodeID),
    endNode   varchar(31) not null references node (nodeID),
    unique (startNode, endNode)
);
-- Needs a way to calculate edgeID, either in Java or by a sql trigger
-- probably in Java since it's a PK

-- Drop Statements:

drop table hasEdge;

drop table node;

-- Insert Statements:

insert into node
values ('a', 2, 3, 'd', 'e', 'f', 'g', 'h');

insert into hasEdge
values ('a', 'b', 'c');

-- 1 – Node Information

select *
from node;

-- 2 – Update Node Coordinates

update node
set xCoord = 1,
    yCoord = 2
where nodeID = 'abc';

-- 3 – Update Node Location Long Name

update node
set longName = 'LongName'
where nodeID = 'abc';

-- 4 – Edge Information

select *
from hasEdge

-- 5 – Exit Program

-- getNodeLite(nodeID)
-- From Nodes
-- Given a NodeID
-- gives xCoord, yCoord, Floor, Type

select xCoord, yCoord, floor, nodeType
from node
where nodeID = nodeID;

-- getNeighbors(nodeID)
-- From hasEdge
-- Given a NodeID
-- gives nodeID of all neighbors
select startNode as neighborID
from hasEdge
where endNode = nodeID
union
select endNode as neighborID
from hasEdge
where startNode = nodeID;

-- new table for hasEdge+distance
create table edgeLength
(
    startNode varchar(31) references node (nodeID),
    endNode   varchar(31) references node (nodeID),
    length    float,
    primary key (startNode, endNode)
);