

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


drop table doctorExaminesAdmission;
drop table appointment;

Create Table appointment(
    appointmentID Int Primary Key,
    patientID Int References userAccount (userID) on delete,
    doctorID Int References userAccount (userID) on delete cascade,
    startTime timeStamp,
    endTime timestamp,
    Constraint appointmentUnique Unique(patientID, startTime, endTime)
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





