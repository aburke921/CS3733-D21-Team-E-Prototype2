-- This is the sql file where the scripts were gathered.
-- It does not affect Edb.java or any code in any way.

-- ALL READY IMPLEMENTED CODE IN OUR CURRENT APPLICATION:-----------------------------------------------------


-- Create Statements:

Create Table node
(
	nodeID    varchar(31) Primary Key,
	xCoord    int         Not Null,
	yCoord    int         Not Null,
	floor     varchar(5)  Not Null,
	building  varchar(20) Not Null,
	nodeType  varchar(10) Not Null,
	longName  varchar(50) Not Null,
	shortName varchar(35),
	Unique (xCoord, yCoord, floor),
	Constraint floorLimit Check (floor In ('1', '2', '3', 'L1', 'L2')),
	Constraint buildingLimit Check (building In ('BTM', '45 Francis', 'Tower', '15 Francis', 'Shapiro', 'Parking')),
	Constraint nodeTypeLimit Check (nodeType In
	                                ('PARK', 'EXIT', 'WALK', 'HALL', 'CONF', 'DEPT', 'ELEV', 'INFO', 'LABS', 'REST',
	                                 'RETL', 'STAI', 'SERV', 'ELEV', 'BATH'))
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
	userID    int Primary Key,             -- for security reasons
	email     varchar(31) Unique Not Null, -- use this for login
	password  varchar(31)        Not Null,
	userType  varchar(31),                 -- only allow visitor permission from java program for security reasons
	firstName varchar(31),                 -- get displayed in app
	lastName  varchar(31),                 -- get displayed in app
	Constraint userIDLimit Check ( userID != 0 ),
	Constraint passwordLimit Check (
		-- password Like '%[a-z]%[a-z]%' And
		-- password Like '%[A-Z]%[A-Z]%' And
		-- password Like '%[0-9]%[0-9]%' And
		-- password Like '%[~!@#$%^&]%[~!@#$%^&]%' And
		Length(password) >= 8 ),
	-- TODO: checks don't allow normal password, commented out
	Constraint userTypeLimit Check (userType In ('visitor', 'patient', 'doctor', 'admin'))
-- Let's assume admins are just doctors but better, they have every power
);

-- userLogin()

Select Count(*) As verification, userID
From userAccount
Where email = ?
  And password = ?;

-- addUserAccount()

Insert Into userAccount
Values ((Select Count(*) From userAccount) + 10000, '2333@wpi.edu', 'password', 'patient', 'firstName', 'lastName');

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
	requestID     int Primary Key,
	userID        int References userAccount On Delete Cascade,
	creationTime  timestamp,
	requestType   varchar(31),
	requestStatus varchar(10),
	Constraint requestTypeLimit Check (requestType In
	                                   ('floral', 'medDelivery', 'sanitation', 'security', 'extTransport')),
	Constraint requestStatusLimit Check (requestStatus In ('complete', 'canceled', 'inProgress'))
);

-- getAllRequestsFrom(userID)

Select *
From requests
Where userID = ?;

-- getRequestCount() -- not needed in Java yet(TM)

Select Count(*)
From requests;

-- changeRequestStatus()

Update requests
Set requestStatus = ?
Where requestID = ?;


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

Select longName From (Select roomID
                    From (Select *
                            From requests
                            where userID = 098908) thing, floralRequests
                            where thing.requestID = floralRequests.requestID) that, node;


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



Create Table sanitation
(
	requestID      int Primary Key References requests On Delete Cascade,
	roomID         varchar(31) Not Null References node On Delete Cascade,
	signature      varchar(31) Not Null,
	description    varchar(5000),
	sanitationType varchar(31),
	urgency        varchar(31) Not Null,
	Constraint sanitationTypeLimit Check (sanitationType In
	                                      ('Urine Cleanup', 'Feces Cleanup', 'Preparation Cleanup', 'Trash Removal')),
	Constraint urgencyTypeLimit Check (urgency In
	                                   ('Low', 'Medium', 'High', 'Critical'))

);

Create Table securityServ
(
	requestID int Primary Key References requests On Delete Cascade,
	roomID    varchar(31) Not Null References node On Delete Cascade,
	level     varchar(31),
	urgency   varchar(31) Not Null,
	Constraint urgencyTypeLimit Check (urgency In
	                                   ('Low', 'Medium', 'High', 'Critical'))
);

Create Table medDelivery
(
	requestID           int Primary Key References requests On Delete Cascade,
	roomID              varchar(31) Not Null References node On Delete Cascade,
	medicineName        varchar(31) Not Null,
	quantity            int         Not Null,
	dosage              varchar(31) Not Null,
	specialInstructions varchar(5000),
	signature           varchar(31) Not Null
);


Create Table extTransport
(
	requestID        int Primary Key References requests On Delete Cascade,
	hospitalLocation varchar(100) Not Null,
	severity         varchar(30)  Not Null,
	patientID        int          Not Null,
	ETA              varchar(100),
	description      varchar(5000)
);



Select requests.requestStatus
From requests,
     floralRequests
Where requests.requestID = floralRequests.requestID;


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
*/



