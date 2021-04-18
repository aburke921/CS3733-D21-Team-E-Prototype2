-- This is the sql file where the scripts were gathered.
-- It does not affect Edb.java or any code in any way.

-- ALL READY IMPLEMENTED CODE IN OUR CURRENT APPLICATION:-----------------------------------------------------


-- Create Statements:

Create Table node
(
	nodeID    varchar(31) Primary Key,
	xCoord    int        Not Null,
	yCoord    int        Not Null,
	floor     varchar(5) Not Null,
	building  varchar(20),
	nodeType  varchar(10),
	longName  varchar(50),
	shortName varchar(35),
	Unique (xCoord, yCoord, floor)
);

Create Table hasEdge
(
	edgeID    varchar(63) Primary Key,
	startNode varchar(31) Not Null References node (nodeID) On Delete Cascade,
	endNode   varchar(31) Not Null References node (nodeID) On Delete Cascade,
	length    float,
	Unique (startNode, endNode)
);

Create Table userAccount
(
	userID    int Primary Key,
	email     varchar(31) Unique,
	password  varchar(31),
	userType  varchar(31),
	firstName varchar(31),
	lastName  varchar(31),
	-- Constraint passwordLimit Check (
	-- password Like '%[a-z]%[a-z]%' And
	-- password Like '%[A-Z]%[A-Z]%' And
	-- password Like '%[0-9]%[0-9]%' And
	-- password Like '%[~!@#$%^&]%[~!@#$%^&]%' And
	-- Length(password) >= 8 ),
	-- TODO: checks don't allow normal password
	Constraint userTypeLimit Check (userType In ('visitor', 'patient', 'doctor', 'admin'))
-- Let's assume admins are just doctors but better, they have every power
);

-- addUserAccount()

Insert Into userAccount
Values (2333, '2333@wpi.edu', 'password', 'patient', 'firstName', 'lastName');

Create View visitorAccount As
Select *
From userAccount
Where userType = 'visitor';

Create View patientAccount As
Select *
From userAccount
Where userType = 'patient';

Create View doctorAccount As
Select *
From userAccount
Where userType = 'doctor';

Create View adminAccount As
Select *
From userAccount
Where userType = 'admin';

Create Table patient
(
	userID int Primary Key References userAccount On Delete Cascade,
	roomID varchar(31) References node On Delete Cascade-- nodeID for patient's room
);

Create Table requests
(
	requestID    int Primary Key,
	userID    int References userAccount On Delete Cascade,
	creationTime timestamp,
	requestType  varchar(31),
	requestState varchar(10),
	Constraint requestTypeLimit Check (requestType In
	                                   ('floral', 'medDelivery', 'sanitation', 'security', 'extTransport')),
	Constraint requestStateLimit Check (requestState In ('complete', 'canceled', 'inProgress'))
);

-- getAllRequestsFrom(userID)

Select *
From requests
Where creatorID = ?;

-- getRequestCount() -- not needed in Java yet(TM)

Select Count(*)
From requests;

Create Table floralRequests
(
	requestID     int Primary Key References requests On Delete Cascade,
	roomID        varchar(31) References node On Delete Cascade,
	recipientName varchar(31),
	flowerType    varchar(31),
	flowerAmount  int,
	vaseType      varchar(31),
	message       varchar(5000),
	Constraint flowerTypeLimit Check (flowerType In ('Roses', 'Tulips', 'Carnations', 'Assortment')),
	Constraint flowerAmountLimit Check (flowerAmount In (1, 6, 12)),
	Constraint vaseTypeLimit Check (vaseType In ('Round', 'Square', 'Tall', 'None'))
);

-- addFloralRequest() -- run both, in order

Insert Into requests
Values ((Select Count(*)
         From requests) + 1, 2333, current Timestamp, 'floral', 'inProgress');
Insert Into floralRequests
Values ((Select Count(*)
         From requests), 'test3', 'Ash', 'Tulips', 6, 'Round', '?');

Insert Into requests
Values ((Select Count(*)
         From requests) + 1, ?, current Timestamp, 'floral', 'inProgress');
Insert Into floralRequests
Values ((Select Count(*)
         From requests), 'test3', 'Aassh', 'Roses', 1, 'Tall', 'dasddw');

-- getFloralInfo() -- not needed in Java yet(TM)

Select *
From floralRequests,
     requests
Where floralRequests.requestID = requests.requestID
  And requests.requestID = ?;



Create Table sanitation(
    requestID int Primary Key References requests On Delete Cascade,
    roomID varchar(31) not null References node On Delete Cascade,
    signature varchar(31) NOT NULL,
    description varchar(5000),
    sanitationType varchar(31),
    urgency varchar(31) NOT NULL,
    Constraint sanitationTypeLimit Check (flowerType In ('Urine Cleanup', 'Feces Cleanup', 'Preparation Cleanup', 'Trash Removal'))
);

Create Table securityServ(
    requestID int Primary Key References requests On Delete Cascade,
    roomID varchar(31) not null References node On Delete Cascade,
    level varchar(31),
    urgency varchar(31) NOT NULL
);

Create Table medDelivery(
    requestID int Primary Key References requests On Delete Cascade,
    roomID varchar(31) not null References node On Delete Cascade,
    medacineName varchar(31) NOT NULL,
    quantity int NOT NULL,
    specialInstructions varchar(5000),
    signature varchar(31) NOT NULL
);



Create Table extTransport(
    requestID int Primary Key References requests On Delete Cascade,

    hospitalLocation varchar(100) not null,
    ETA varchar(100),

);


-- Code for the lengthFromEdges(int searchType, String nodeID) method when searchType == 1
Select startNode, endNode, Sqrt(((startX - endX) * (startX - endX)) + ((startY - endY) * (startY - endY))) As distance
From (Select startNode, endNode, node.xCoord As startX, node.yCoord As startY, endX, endY
      From node,
           (Select startNode, endNode, xCoord As endX, yCoord As endY
            From node,
                 (Select startNode, endNode
                  From hasEdge
                  Where startNode = '________') wantedEdges
            Where nodeID = startNode) wantedEdgesStartInfo
      Where node.nodeID = wantedEdgesStartInfo.endNode) desiredTable;


-- Code for the lengthFromEdges(int searchType, String nodeID) method when searchType == 2
Select startNode, endNode, Sqrt(((startX - endX) * (startX - endX)) + ((startY - endY) * (startY - endY))) As distance
From (Select startNode, endNode, node.xCoord As startX, node.yCoord As startY, endX, endY
      From node,
           (Select startNode, endNode, xCoord As endX, yCoord As endY
            From node,
                 (Select startNode, endNode
                  From hasEdge
                  Where endNode = '________') wantedEdges
            Where nodeID = startNode) wantedEdgesStartInfo
      Where node.nodeID = wantedEdgesStartInfo.endNode) desiredTable;


-- Code for the lengthFromEdges(int searchType, String nodeID) method when searchType == 3
Select startNode, endNode, Sqrt(((startX - endX) * (startX - endX)) + ((startY - endY) * (startY - endY))) As distance
From (Select startNode, endNode, node.xCoord As startX, node.yCoord As startY, endX, endY
      From node,
           (Select startNode, endNode, xCoord As endX, yCoord As endY
            From node,
                 (Select startNode, endNode
                  From hasEdge) wantedEdges
            Where nodeID = startNode) wantedEdgesStartInfo
      Where node.nodeID = wantedEdgesStartInfo.endNode) desiredTable;


-- addNode()

Insert Into node
Values (?, ?, ?, ?, ?, ?, ?, ?);

-- addEdge()

Insert Into hasEdge
Values (?, ?, ?, ?);

-- deleteNode()

Delete
From node
Where nodeID = ?;

-- deleteEdge()

Delete
From hasEdge
Where startNode = ? And endNode = ?
   Or endNode = ? And startNode = ?;

-- or

Delete
From hasEdge
Where startNode = ?
  And endNode = ?;
Delete
From hasEdge
Where endNode = ?
  And startNode = ?;

-- getNodeInfo()
Select *
From node
Where nodeID = ?;

-- countNodeTypeOnFloor(String Floor, String Type)

Select Count(*) As count
From node
Where nodeID Like ('SSSSS%')
  And floor = ?
  And nodeType = ?;

-- NOT YET IMPLEMENTED / PLANNING STAGES:-----------------------------------------------------


-- Needs a way to calculate edgeID, either in Java or by a sql trigger
-- probably in Java since it's a PK

-- Drop Statements:

Drop Table hasEdge;

Drop Table node;

-- Insert Statements:

Insert Into node
Values ('a', 2, 3, 'd', 'e', 'f', 'g', 'h');

Insert Into hasEdge
Values (?, ?, ?, ?);

-- 1 – Node Information

Select *
From node;

-- 2 – Update Node Coordinates

Update node
Set xCoord = 1,
    yCoord = 2
Where nodeID = ?;

-- 3 – Update Node Location Long Name

Update node
Set longName = 'LongName'
Where nodeID = ?;

-- 4 – Edge Information

Select *
From hasEdge;

-- 5 – Exit Program

-- getNodeLite(nodeID)
-- From Nodes
-- Given a NodeID
-- gives xCoord, yCoord, Floor, Type

Select xCoord, yCoord, floor, nodeType
From node
Where nodeID = ?;

-- getNeighbors(nodeID)
-- From hasEdge
-- Given a NodeID
-- gives nodeID of all neighbors
Select startNode As neighborID
From hasEdge
Where endNode = ?
Union
Select endNode As neighborID
From hasEdge
Where startNode = ?;

-- new table for hasEdge+distance
Create Table edgeLength
(
	startNode varchar(31) References node (nodeID) On Delete Cascade,
	endNode   varchar(31) References node (nodeID) On Delete Cascade,
	length    float,
	Primary Key (startNode, endNode)
);

/*CREATE TRIGGER calculateLength before INSERT ON hasEdge
    FOR EACH ROW
BEGIN

SELECT sqrt(((miniX - maxiX) * (miniX - maxiX)) + ((miniY - maxiY) * (miniY - maxiY))) into :new.length
FROM (SELECT min (xCoord) as miniX, max (xCoord) as maxiX, min (yCoord) as miniY, max (yCoord) as maxiY
    FROM (SELECT nodeID, xCoord, yCoord
    FROM node
    WHERE nodeID = : new.startNode OR nodeID = : new.endNode));
END;
/*/



-- L1Nodes.csv file:
INSERT INTO node VALUES('CCONF001L1', 	2255,	 849, 	'L1', 		'45 Francis', 		'CONF', 	'Anesthesia Conf Floor L1', 'Conf C001L1');
INSERT INTO node
VALUES ('CCONF002L1', 2665, 1043, 'L1', '45 Francis', 'CONF', 'Medical Records Conference Room Floor L1',
        'Conf C002L1');
INSERT INTO node
VALUES ('CCONF003L1', 2445, 1245, 'L1', '45 Francis', 'CONF', 'Abrams Conference Room', 'Conf C003L1');
INSERT INTO node
VALUES ('CDEPT002L1', 1980, 844, 'L1', 'Tower', 'DEPT', 'Day Surgery Family Waiting Floor L1', 'Department C002L1');
INSERT INTO node
VALUES ('CDEPT003L1', 1845, 844, 'L1', 'Tower', 'DEPT', 'Day Surgery Family Waiting Exit Floor L1',
        'Department C003L1');
INSERT INTO node
VALUES ('CDEPT004L1', 2310, 1043, 'L1', '45 Francis', 'DEPT', 'Medical Records Film Library Floor L1',
        'Department C004L1');
INSERT INTO node
VALUES ('CHALL001L1', 1732, 924, 'L1', 'Tower', 'HALL', 'Hallway 1 Floor L1', 'Hallway C001L1');
INSERT INTO node
VALUES ('CHALL002L1', 2445, 1043, 'L1', '45 Francis', 'HALL', 'Hallway 2 Floor L1', 'Hallway C002L1');
INSERT INTO node
VALUES ('CHALL003L1', 2445, 1284, 'L1', '5 Francis', 'HALL', 'Hallway 3 Floor L1', 'Hallway C003L1');
INSERT INTO node
VALUES ('CHALL004L1', 2770, 1070, 'L1', '5 Francis', 'HALL', 'Hallway 4 Floor L1', 'Hallway C004L1');
INSERT INTO node
VALUES ('CHALL005L1', 1750, 1284, 'L1', 'Tower', 'HALL', 'Hallway 5 Floor L1', 'Hallway C005L1');
INSERT INTO node
VALUES ('CHALL006L1', 2130, 1284, 'L1', 'Tower', 'HALL', 'Hallway 6 Floor L1', 'Hallway C006L1');
INSERT INTO node
VALUES ('CHALL007L1', 2130, 1045, 'L1', 'Tower', 'HALL', 'Hallway 7 Floor L1', 'Hallway C007L1');
INSERT INTO node
VALUES ('CHALL008L1', 2215, 1045, 'L1', '45 Francis', 'HALL', 'Hallway 8 Floor L1', 'Hallway C008L1');
INSERT INTO node
VALUES ('CHALL009L1', 2220, 904, 'L1', '45 Francis', 'HALL', 'Hallway 9 Floor L1', 'Hallway C009L1');
INSERT INTO node
VALUES ('CHALL010L1', 2265, 904, 'L1', '45 Francis', 'HALL', 'Hallway 10 Floor L1', 'Hallway C010L1');
INSERT INTO node
VALUES ('CHALL011L1', 2360, 849, 'L1', '45 Francis', 'HALL', 'Hallway 11 Floor L1', 'Hallway C011L1');
INSERT INTO node
VALUES ('CHALL012L1', 2130, 904, 'L1', '45 Francis', 'HALL', 'Hallway 12 Floor L1', 'Hallway C012L1');
INSERT INTO node
VALUES ('CHALL013L1', 2130, 844, 'L1', '45 Francis', 'HALL', 'Hallway 13 Floor L1', 'Hallway C013L1');
INSERT INTO node
VALUES ('CHALL014L1', 1845, 924, 'L1', 'Tower', 'HALL', 'Hallway 14 Floor L1', 'Hallway C014L1');
INSERT INTO node
VALUES ('CHALL015L1', 2300, 849, 'L1', '45 Francis', 'HALL', 'Hallway 15 Floor L1', 'Hallway C015L1');
INSERT INTO node
VALUES ('CLABS001L1', 1965, 1284, 'L1', 'Tower', 'LABS', 'Outpatient Fluoroscopy Floor L1', 'Lab C001L1');
INSERT INTO node
VALUES ('CLABS002L1', 1750, 1090, 'L1', 'Tower', 'LABS', 'Pre-Op PACU Floor L1', 'Lab C002L1');
INSERT INTO node
VALUES ('CLABS003L1', 2290, 1284, 'L1', '45 Francis', 'LABS', 'Nuclear Medicine Floor L1', 'Lab C003L1');
INSERT INTO node
VALUES ('CLABS004L1', 2320, 1284, 'L1', '45 Francis', 'LABS', 'Ultrasound Floor L1', 'Lab C004L1');
INSERT INTO node
VALUES ('CLABS005L1', 2770, 1284, 'L1', '45 Francis', 'LABS', 'CSIR MRI Floor L1', 'Lab C005L1');
INSERT INTO node
VALUES ('CREST001L1', 1732, 1019, 'L1', 'Tower', 'REST', 'Restroom L Elevator Floor L1', 'Restroom C001L1');
INSERT INTO node
VALUES ('CREST002L1', 2065, 1284, 'L1', 'Tower', 'REST', 'Restroom M Elevator Floor L1', 'Restroom C002L1');
INSERT INTO node
VALUES ('CREST003L1', 2300, 879, 'L1', '45 Francis', 'REST', 'Restroom K Elevator Floor L1', 'Restroom C003L1');
INSERT INTO node
VALUES ('CREST004L1', 2770, 1160, 'L1', '45 Francis', 'REST', 'Restroom H Elevator Floor L1', 'Restroom C004L1');
INSERT INTO node
VALUES ('CRETL001L1', 2185, 904, 'L1', '45 Francis', 'RETL', 'Vending Machine 1 L1', 'Retail C001L1');
INSERT INTO node
VALUES ('CSERV001L1', 2490, 1043, 'L1', '45 Francis', 'SERV', 'Volunteers Floor L1', 'Service C001L1');
INSERT INTO node
VALUES ('CSERV001L2', 2015, 1280, 'L2', '45 Francis', 'SERV', 'Interpreter Services Floor L2', 'Service C001L2');
INSERT INTO node
VALUES ('GELEV00QL1', 1637, 2116, 'L1', 'Shapiro', 'ELEV', 'Elevator Q MapNode 7 Floor L1', 'Elevator Q L1');
INSERT INTO node
VALUES ('GEXIT001L1', 1702, 2260, 'L1', 'Shapiro', 'EXIT', 'Fenwood Road Exit MapNode 1 Floor L1',
        'Fenwood Road EntranceExit L1');
INSERT INTO node
VALUES ('GHALL002L1', 1702, 2167, 'L1', 'Shapiro', 'HALL', 'Hallway MapNode 2 Floor L1', 'Hall');
INSERT INTO node
VALUES ('GHALL003L1', 1688, 2167, 'L1', 'Shapiro', 'HALL', 'Hallway MapNode 3 Floor L1', 'Hall');
INSERT INTO node
VALUES ('GHALL004L1', 1666, 2167, 'L1', 'Shapiro', 'HALL', 'Hallway MapNode 4 Floor L1', 'Hall');
INSERT INTO node
VALUES ('GHALL005L1', 1688, 2131, 'L1', 'Shapiro', 'HALL', 'Hallway MapNode 5 Floor L1', 'Hall');
INSERT INTO node
VALUES ('GHALL006L1', 1665, 2116, 'L1', 'Shapiro', 'HALL', 'Hallway MapNode 6 Floor L1', 'Hall');
INSERT INTO node
VALUES ('GSTAI008L1', 1720, 2131, 'L1', 'Shapiro', 'STAI', 'Stairs MapNode 8 Floor L1', 'L1 Stairs');
INSERT INTO node
VALUES ('WELEV00HL1', 2715, 1070, 'L1', '45 Francis', 'ELEV', 'Elevator H Floor L1', 'Elevator HL1');
INSERT INTO node
VALUES ('WELEV00JL1', 2360, 799, 'L1', '45 Francis', 'ELEV', 'Elevator J Floor L1', 'Elevator JL1');
INSERT INTO node
VALUES ('WELEV00KL1', 2220, 974, 'L1', '45 Francis', 'ELEV', 'Elevator K Floor L1', 'Elevator KL1');
INSERT INTO node
VALUES ('WELEV00LL1', 1785, 924, 'L1', 'Tower', 'ELEV', 'Elevator L Floor L1', 'Elevator LL1');
INSERT INTO node
VALUES ('WELEV00ML1', 1820, 1284, 'L1', 'Tower', 'ELEV', 'Elevator M Floor L1', 'Elevator ML1');

-- L1Edges.csv file:
INSERT INTO hasEdge
VALUES ('CCONF002L1_WELEV00HL1', 'CCONF002L1', 'WELEV00HL1');
INSERT INTO hasEdge
VALUES ('CCONF003L1_CHALL002L1', 'CCONF003L1', 'CHALL002L1');
INSERT INTO hasEdge
VALUES ('CDEPT002L1_CDEPT003L1', 'CDEPT002L1', 'CDEPT003L1');
INSERT INTO hasEdge
VALUES ('CDEPT003L1_CHALL014L1', 'CDEPT003L1', 'CHALL014L1');
INSERT INTO hasEdge
VALUES ('CDEPT004L1_CHALL002L1', 'CDEPT004L1', 'CHALL002L1');
INSERT INTO hasEdge
VALUES ('CHALL001L1_CREST001L1', 'CHALL001L1', 'CREST001L1');
INSERT INTO hasEdge
VALUES ('CHALL002L1_CSERV001L1', 'CHALL002L1', 'CSERV001L1');
INSERT INTO hasEdge
VALUES ('CHALL003L1_CCONF003L1', 'CHALL003L1', 'CCONF003L1');
INSERT INTO hasEdge
VALUES ('CHALL003L1_CLABS004L1', 'CHALL003L1', 'CLABS004L1');
INSERT INTO hasEdge
VALUES ('CHALL004L1_CREST004L1', 'CHALL004L1', 'CREST004L1');
INSERT INTO hasEdge
VALUES ('CHALL005L1_WELEV00ML1', 'CHALL005L1', 'WELEV00ML1');
INSERT INTO hasEdge
VALUES ('CHALL006L1_CHALL007L1', 'CHALL006L1', 'CHALL007L1');
INSERT INTO hasEdge
VALUES ('CHALL007L1_CHALL008L1', 'CHALL007L1', 'CHALL008L1');
INSERT INTO hasEdge
VALUES ('CHALL008L1_CDEPT004L1', 'CHALL008L1', 'CDEPT004L1');
INSERT INTO hasEdge
VALUES ('CHALL008L1_WELEV00KL1', 'CHALL008L1', 'WELEV00KL1');
INSERT INTO hasEdge
VALUES ('CHALL009L1_CHALL010L1', 'CHALL009L1', 'CHALL010L1');
INSERT INTO hasEdge
VALUES ('CHALL009L1_CRETL001L1', 'CHALL009L1', 'CRETL001L1');
INSERT INTO hasEdge
VALUES ('CHALL010L1_CREST003L1', 'CHALL010L1', 'CREST003L1');
INSERT INTO hasEdge
VALUES ('CHALL012L1_CHALL013L1', 'CHALL012L1', 'CHALL013L1');
INSERT INTO hasEdge
VALUES ('CHALL013L1_CDEPT002L1', 'CHALL013L1', 'CDEPT002L1');
INSERT INTO hasEdge
VALUES ('CHALL014L1_WELEV00LL1', 'CHALL014L1', 'WELEV00LL1');
INSERT INTO hasEdge
VALUES ('CHALL015L1_CCONF001L1', 'CHALL015L1', 'CCONF001L1');
INSERT INTO hasEdge
VALUES ('CHALL015L1_CHALL011L1', 'CHALL015L1', 'CHALL011L1');
INSERT INTO hasEdge
VALUES ('CLABS001L1_CREST002L1', 'CLABS001L1', 'CREST002L1');
INSERT INTO hasEdge
VALUES ('CLABS002L1_CHALL005L1', 'CLABS002L1', 'CHALL005L1');
INSERT INTO hasEdge
VALUES ('CLABS002L1_CREST001L1', 'CLABS002L1', 'CREST001L1');
INSERT INTO hasEdge
VALUES ('CLABS003L1_CHALL006L1', 'CLABS003L1', 'CHALL006L1');
INSERT INTO hasEdge
VALUES ('CLABS004L1_CLABS003L1', 'CLABS004L1', 'CLABS003L1');
INSERT INTO hasEdge
VALUES ('CLABS005L1_CHALL003L1', 'CLABS005L1', 'CHALL003L1');
INSERT INTO hasEdge
VALUES ('CREST002L1_CHALL006L1', 'CREST002L1', 'CHALL006L1');
INSERT INTO hasEdge
VALUES ('CREST003L1_CHALL015L1', 'CREST003L1', 'CHALL015L1');
INSERT INTO hasEdge
VALUES ('CREST004L1_CLABS005L1', 'CREST004L1', 'CLABS005L1');
INSERT INTO hasEdge
VALUES ('CRETL001L1_CHALL012L1', 'CRETL001L1', 'CHALL012L1');
INSERT INTO hasEdge
VALUES ('CSERV001L1_CCONF002L1', 'CSERV001L1', 'CCONF002L1');
INSERT INTO hasEdge
VALUES ('WELEV00HL1_CHALL004L1', 'WELEV00HL1', 'CHALL004L1');
INSERT INTO hasEdge
VALUES ('WELEV00KL1_CHALL009L1', 'WELEV00KL1', 'CHALL009L1');
INSERT INTO hasEdge
VALUES ('WELEV00LL1_CHALL001L1', 'WELEV00LL1', 'CHALL001L1');
INSERT INTO hasEdge
VALUES ('WELEV00ML1_CLABS001L1', 'WELEV00ML1', 'CLABS001L1');
INSERT INTO hasEdge
VALUES ('GEXIT001L1_GHALL002L1', 'GEXIT001L1', 'GHALL002L1');
INSERT INTO hasEdge
VALUES ('GHALL002L1_GHALL003L1', 'GHALL002L1', 'GHALL003L1');
INSERT INTO hasEdge
VALUES ('GHALL003L1_GHALL004L1', 'GHALL003L1', 'GHALL004L1');
INSERT INTO hasEdge
VALUES ('GHALL003L1_GHALL005L1', 'GHALL003L1', 'GHALL005L1');
INSERT INTO hasEdge
VALUES ('GHALL005L1_GSTAI008L1', 'GHALL005L1', 'GSTAI008L1');
INSERT INTO hasEdge
VALUES ('GHALL005L1_GHALL006L1', 'GHALL005L1', 'GHALL006L1');
INSERT INTO hasEdge
VALUES ('GHALL006L1_GELEV007L1', 'GHALL006L1', 'GELEV00QL1');












