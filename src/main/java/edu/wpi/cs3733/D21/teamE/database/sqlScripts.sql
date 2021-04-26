

Drop Table securityServ;
Drop Table medDelivery;
Drop Table extTransport;
Drop Table sanitationRequest;
Drop Table floralRequests;
Drop Table requests;
Drop View visitorAccount;
Drop View patientAccount;
Drop View adminAccount;
Drop View doctorAccount;
Drop Table userAccount;
Drop Table hasEdge;
Drop Table node;




Create Table node(
                     nodeID    varchar(31) Primary Key,
                     xCoord    int Not Null,
                     yCoord    int Not Null,
                     floor     varchar(5) Not Null,
                     building  varchar(20),
                     nodeType  varchar(10),
                     longName  varchar(100),
                     shortName varchar(100),
                     Unique (xCoord, yCoord, floor),
                     Constraint floorLimit Check (floor In ('1', '2', '3', 'L1', 'L2')),
                     Constraint buildingLimit Check (building In ('BTM', '45 Francis', 'Tower', '15 Francis', 'Shapiro', 'Parking')),
                     Constraint nodeTypeLimit Check (nodeType In ('PARK', 'EXIT', 'WALK', 'HALL', 'CONF', 'DEPT', 'ELEV', 'INFO', 'LABS', 'REST', 'RETL', 'STAI', 'SERV', 'ELEV', 'BATH'))
);


Create Table hasEdge(
                        edgeID    varchar(63) Primary Key,
                        startNode varchar(31) Not Null References node (nodeid) On Delete Cascade,
                        endNode   varchar(31) Not Null References node (nodeid) On Delete Cascade,
                        length    float,
                        Unique (startNode, endNode)
);

Create Table userAccount (
                             userID    Int Primary Key,
                             email     Varchar(31) Unique Not Null,
                             password  Varchar(31)        Not Null,
                             userType  Varchar(31),
                             firstName Varchar(31),
                             lastName  Varchar(31),
                             Constraint userIDLimit Check ( userID != 0 ),
                             Constraint passwordLimit Check (Length(password) >= 8 ),
                             Constraint userTypeLimit Check (userType In ('visitor', 'patient', 'doctor', 'admin'))
);




Create View visitorAccount As
Select *
From useraccount
Where usertype = 'visitor';

Create View patientAccount As
Select *
From useraccount
Where usertype = 'patient';

Create View doctorAccount As
Select *
From useraccount
Where usertype = 'doctor';

Create View adminAccount As
Select *
From useraccount
Where usertype = 'admin';


Create Table requests(
                         requestID    int Primary Key,
                         creatorID    int References userAccount On Delete Cascade,
--                          creationTime timestamp,
                         requestType  varchar(31),
                         requestStatus varchar(10),
                         assignee varchar(31),
                         Constraint requestTypeLimit Check (requestType In ('floral', 'medDelivery', 'sanitation', 'security', 'extTransport')),
                         Constraint requestStatusLimit Check (requestStatus In ('complete', 'canceled', 'inProgress'))
);


Create Table floralRequests(
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


Create Table sanitationRequest(
                                  requestID int Primary Key References requests On Delete Cascade,
                                  roomID varchar(31) Not Null References node On Delete Cascade,
                                  signature varchar(31) Not Null,
                                  description varchar(5000),
                                  sanitationType varchar(31),
                                  urgency varchar(31) Not Null,
                                  Constraint sanitationTypeLimit Check (sanitationType In ('Urine Cleanup', 'Feces Cleanup', 'Preparation Cleanup', 'Trash Removal')),
                                  Constraint urgencyTypeLimit Check (urgency In ('Low', 'Medium', 'High', 'Critical'))
);


Create Table extTransport(
                             requestID int Primary Key References requests On Delete Cascade,
                             roomID varchar(31) Not Null References node On Delete Cascade,
                             requestType varchar(100) Not Null,
                             severity varchar(30) Not Null,
                             patientID int Not Null,
                             ETA varchar(100),
                             description varchar(5000),
                             Constraint requestTypeLimitExtTrans Check (requestType In ('Ambulance', 'Helicopter', 'Plane'))
);


Create Table medDelivery (
                             requestID  int Primary Key References requests On Delete Cascade,
                             roomID     varchar(31) Not Null References node On Delete Cascade,
                             medicineName        varchar(31) Not Null,
                             quantity            int         Not Null,
                             dosage              varchar(31) Not Null,
                             specialInstructions varchar(5000),
                             signature           varchar(31) Not Null
);

Create Table securityServ (
                              requestID  int Primary Key References requests On Delete Cascade,
                              roomID     varchar(31) Not Null References node On Delete Cascade,
                              level     varchar(31),
                              urgency   varchar(31) Not Null,
                              Constraint urgencyTypeLimitServ Check (urgency In ('Low', 'Medium', 'High', 'Critical'))
);



Create Table appointment(
                            appointmentID Int Primary Key,
                            patientID Int References userAccount (userID) on delete cascade,
                            doctorID Int References userAccount (userID) on delete cascade,
                            startTime timeStamp,
                            endTime timestamp,
                            Constraint appointmentUnique Unique(patientID, startTime, endTime)
);


Drop Table beverageOrderedInRequest;
Drop Table beverage;
Drop Table foodOrderedInRequest;
Drop Table food;
Drop Table foodDelivery;

Create Table foodDelivery (
    requestID int Primary Key References requests (requestID) On Delete Cascade,
    roomID varchar(31) Not Null References node (nodeID) On Delete Cascade,
    allergy varchar(50),
    dietRestriction varchar(50),
    description varchar(3000)
);

Create Table food (
    foodID int Primary KEY,
    item varchar(50),
    price Double,
    calories int,
    description varchar(3000)
);

insert into food values (?,?,?,?,?);

Create Table foodOrderedInRequest (
    requestId int References foodDelivery (requestID)  on Delete Cascade,
    foodID int References food (foodID) On Delete Cascade,
    quantity int,
    Primary Key (requestId, foodID)
);

Create Table beverage (
    beverageID int Primary key,
    item varchar(50)
);

Create Table beverageOrderedInRequest(
    requestId int References foodDelivery (requestID)  on Delete Cascade,
    beverageID int References beverage (beverageID) On Delete Cascade,
    quantity int,
    Primary Key (requestId, beverageID)
);


insert into foodOrderedInRequest values (?,?,?);


create Table aubonPainMenu(
    foodImage varchar(600),
    foodItems varchar(100) Primary Key,
    foodPrice varchar(10),
    foodCalories varchar(10),
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






insert into userAccount values(24, 'rosas1@yahoo.com','doctor06','doctor','Rosa', 'Smith');
insert into userAccount values(25, 'rosas2@yahoo.com','doctor06','doctor','Rosa', 'Smith');
insert into userAccount values(26, 'rosas3@yahoo.com','doctor06','doctor','Rosa', 'Smith');
insert into userAccount values(27, 'rosas4@yahoo.com','doctor06','doctor','Rosa', 'Smith');
insert into userAccount values(28, 'rosas5@yahoo.com','doctor06','doctor','Rosa', 'Smith');
insert into userAccount values(29, 'rosas6@yahoo.com','doctor06','doctor','Rosa', 'Smith');
insert into userAccount values(30, 'rosas7@yahoo.com','doctor06','doctor','Rosa', 'Smith');



insert into node values('FDEPT00501',2128,1300,'1','Tower','DEPT','Emergency Department','Emergency');
insert into node values('EEXIT00101',2275,785,'1','45 Francis','EXIT','Ambulance Parking Exit Floor 1','AmbExit 1');


-- requestID    int Primary Key,
--                          creatorID    int References userAccount On Delete Cascade,
-- --                          creationTime timestamp,
--                          requestType  varchar(31),
--                          requestStatus varchar(10),
--                          assignee varchar(31),
insert into requests values (1, 24, 'extTransport', 'inProgress', 'asdflkdj');
insert into requests values (2, 24, 'extTransport', 'inProgress', 'asdflkdj');
insert into requests values (3, 24, 'extTransport', 'inProgress', 'asdflkdj');
insert into requests values (4, 24, 'extTransport', 'inProgress', 'asdflkdj');
insert into requests values (5, 24, 'extTransport', 'inProgress', 'asdflkdj');
insert into requests values (6, 24, 'extTransport', 'inProgress', 'asdflkdj');
insert into requests values (7, 24, 'extTransport', 'inProgress', 'asdflkdj');
insert into requests values (8, 24, 'extTransport', 'inProgress', 'asdflkdj');
insert into requests values (9, 24, 'extTransport', 'inProgress', 'asdflkdj');
insert into requests values (10, 24, 'extTransport', 'inProgress', 'asdflkdj');
insert into requests values (11, 24, 'extTransport', 'inProgress', 'asdflkdj');

--                              requestID int Primary Key References requests On Delete Cascade,
--                              roomID varchar(31) Not Null References node On Delete Cascade,
--                              requestType varchar(100) Not Null,
--                              severity varchar(30) Not Null,
--                              patientID int Not Null,
--                              ETA varchar(100),
--                              description varchar(5000),

insert into extTransport values(1,'EEXIT00101', 'Ambulance', 'High Severity', 12334567, '5 minutes', 'Patient dropped down into a state of unconsciousness randomly at the store. Patient is still unconscious and unresponsive but has a pulse. No friends or family around during the incident. ');
insert into extTransport values(2, 'EEXIT00101', 'Ambulance','Low Severity', 4093380, '20 minutes', 'Patient coming in with cut on right hand. Needs stitches. Bleeding is stable.');
insert into extTransport values(3,'FDEPT00501', 'Helicopter','High Severity', 92017693, '10 minutes', 'Car crash on the highway. 7 year old child in the backseat with no seatbelt on in critical condition. Blood pressure is low and has major trauma to the head.');
insert into extTransport values(4,'FDEPT00501', 'Helicopter','High Severity', 93754789, '20 minutes', 'Skier hit tree and lost consciousness. Has been unconscious for 30 minutes. Still has a pulse.');
insert into extTransport values(5, 'EEXIT00101', 'Ambulance','Medium Severity', 417592, '10 minutes', 'Smoke inhalation due to a fire. No burns but difficult time breathing.');
insert into extTransport values(6, 'FDEPT00501', 'Helicopter', 'High Severity', 44888936, '15 minutes', 'Major car crash on highway. Middle aged woman ejected from the passengers seat. Awake and unresponsive and in critical condition');
insert into extTransport values(7,'EEXIT00101', 'Ambulance','Medium Severity', 33337861, '7 minutes', 'Patient passed out for 30 seconds. Is responsive and aware of their surroundings. Has no history of passing out.');
insert into extTransport values(8, 'EEXIT00101', 'Ambulance','Low Severity', 40003829, '10 minutes', 'Relocating a patient with lung cancer from Mt.Auburn Hospital.');
insert into extTransport values(9,'FDEPT00501', 'Plane','High Severity', 38739983, '12 hours', 'Heart transplant organ in route');



