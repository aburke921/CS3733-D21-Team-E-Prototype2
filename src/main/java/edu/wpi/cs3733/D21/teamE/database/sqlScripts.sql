Drop Table securityServ;
Drop Table medDelivery;
Drop Table extTransport;
Drop Table sanitationRequest;
Drop Table floralRequests;
Drop Table requests;
-- Drop View visitorAccount;
-- Drop View patientAccount;
-- Drop View adminAccount;
-- Drop View doctorAccount;
Drop Table userAccount;
Drop Table hasEdge;
Drop Table node;



Create Table node
(
	nodeID    varchar(31) Primary Key,
	xCoord    int        Not Null,
	yCoord    int        Not Null,
	floor     varchar(5) Not Null,
	building  varchar(20),
	nodeType  varchar(10),
	longName  varchar(100),
	shortName varchar(100),
	Unique (xCoord, yCoord, floor),
	Constraint floorLimit Check (floor In ('1', '2', '3', 'L1', 'L2', 'G')),
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
	userID              Int Primary Key,
	email               Varchar(31) Unique Not Null,
	password            Varchar(31)        Not Null,
	userType            Varchar(31)        Not Null,
	firstName           Varchar(31)        Not Null,
	lastName            Varchar(31)        Not Null,
	creationTime        Timestamp          Not Null,
	lastCovidSurvey     Int,
	lastCovidSurveyDate Date,
	lastParkedNodeID    Varchar(31) References node,
	Constraint userIDLimit Check ( userID != 0 ),
	Constraint passwordLimit Check ( Length(password) >= 5 )
);

Create Table ToDo
(
	ToDoID           int Primary Key,
	userID           int References userAccount Not Null,
	title            varchar(63)                Not Null,
	status           int                        Not Null Default 1,    -- default 1 (normal), 10/0 (complete/deleted)
	priority         int                        Not Null Default 0,    -- default 0 (none), 1/2/3 (low/mid/high)
-- optional:
	scheduledDate    Varchar(31)                         Default Null, -- format:
	scheduledTime    Varchar(31)                         Default Null, -- format: 23:17:00
	nodeID           varchar(31) References node,
	detail           varchar(1023),
	expectedLength   Varchar(31),                                      -- how long it would take, format: 23:17:00
	notificationDate Varchar(31),                                      -- eg. remind me 2 days before this (send email)
	notificationTime Varchar(31)                                       -- format: 23:17:00 eg. remind me 30 mins before this (send email)
);

Create Table requests
(
	requestID     int Primary Key,
	creatorID     int References userAccount On Delete Cascade,
--                          creationTime timestamp,
	requestType   varchar(31),
	requestStatus varchar(10),
	assignee      int References userAccount On Delete Cascade,
--	Constraint requestTypeLimit Check (requestType In ('floral', 'medDelivery', 'sanitation', 'security', 'extTransport')),
	Constraint requestStatusLimit Check (requestStatus In ('complete', 'canceled', 'inProgress'))
);


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


Create Table sanitationRequest
(
	requestID      int Primary Key References requests On Delete Cascade,
	roomID         varchar(31) Not Null References node On Delete Cascade,
	signature      varchar(31) Not Null,
	description    varchar(5000),
	sanitationType varchar(31),
	urgency        varchar(31) Not Null,
	Constraint sanitationTypeLimit Check (sanitationType In
	                                      ('Urine Cleanup', 'Feces Cleanup', 'Preparation Cleanup', 'Trash Removal')),
	Constraint urgencyTypeLimit Check (urgency In ('Low', 'Medium', 'High', 'Critical'))
);


Create Table extTransport
(
	requestID   int Primary Key References requests On Delete Cascade,
	roomID      varchar(31)  Not Null References node On Delete Cascade,
	requestType varchar(100) Not Null,
	severity    varchar(30)  Not Null,
	patientID   int          Not Null,
	ETA         varchar(100),
	description varchar(5000),
	Constraint requestTypeLimitExtTrans Check (requestType In ('Ambulance', 'Helicopter', 'Plane'))
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

Create Table securityServ
(
	requestID int Primary Key References requests On Delete Cascade,
	roomID    varchar(31) Not Null References node On Delete Cascade,
	level     varchar(31),
	urgency   varchar(31) Not Null,
	Constraint urgencyTypeLimitServ Check (urgency In ('Low', 'Medium', 'High', 'Critical'))
);

Create Table entryRequest
(
	surveyResult int,
	decision     int, -- 1 for allow, 2 for ER, 3 for block
	Constraint decisionLimit Check (decision In (1, 2, 3))
);



Create Table appointment
(
	appointmentID Int Primary Key,
	patientID     Int References userAccount (userID) On Delete Cascade,
	doctorID      Int References userAccount (userID) On Delete Cascade,
	startTime     timeStamp,
	endTime       timestamp,
	Constraint appointmentUnique Unique (patientID, startTime, endTime)
);


Drop Table beverageOrderedInRequest;
Drop Table beverage;
Drop Table foodOrderedInRequest;
Drop Table food;
Drop Table foodDelivery;

Create Table foodDelivery
(
	requestID       int Primary Key References requests (requestID) On Delete Cascade,
	roomID          varchar(31) Not Null References node (nodeID) On Delete Cascade,
	allergy         varchar(50),
	dietRestriction varchar(50),
	description     varchar(3000)
);

Create Table food
(
	foodID      int Primary Key,
	item        varchar(50),
	price       Double,
	calories    int,
	description varchar(3000)
);

Insert Into food
Values (?, ?, ?, ?, ?);

Create Table foodOrderedInRequest
(
	requestId int References foodDelivery (requestID) On Delete Cascade,
	foodID    int References food (foodID) On Delete Cascade,
	quantity  int,
	Primary Key (requestId, foodID)
);

Create Table beverage
(
	beverageID int Primary Key,
	item       varchar(50)
);

Create Table beverageOrderedInRequest
(
	requestID  int References foodDelivery (requestID) On Delete Cascade,
	beverageID int References beverage (beverageID) On Delete Cascade,
	quantity   int,
	Primary Key (requestID, beverageID)
);


Insert Into foodOrderedInRequest
Values (?, ?, ?);

Create Table internalPatientRequest
(
	requestID       int Primary Key References requests On Delete Cascade,
	patientID       int References userAccount (userID) On Delete Cascade,
	pickUpLocation  varchar(31) Not Null References node On Delete Cascade,
	dropOffLocation varchar(31) Not Null References node On Delete Cascade,
	department      varchar(31),
	severity        varchar(31),
	description     varchar(5000)
);

Create Table aubonPainMenu
(
	foodImage       varchar(600),
	foodItems       varchar(100) Primary Key,
	foodPrice       varchar(10),
	foodCalories    varchar(10),
	foodDescription varchar(3000)
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



-- 5 – Exit Program

-- getNodeLite(nodeID)
-- From Nodes
-- Given a NodeID
-- gives xCoord, yCoord, Floor, Type


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
