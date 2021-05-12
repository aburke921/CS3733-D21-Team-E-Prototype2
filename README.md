# README.md

### QR Code:

There is a button on the main page of the app that opens the device’s default camera to allow the user to scan a QR code with the view finder.
The QR codes are going to be found inside the hospital so the user can get their phone out and quickly scan for information.
There are two kinds of QR codes, one for precise indoor locating and one for parking slot registration.
It allows the app to know where the user is currently is, so it can direct the user to somewhere without the need of locating satellites since it was indoors and has multiple floors.
The parking slot codes allow the user to keep track of their parked location with the app, they can click a link on their default page to be taken to their car.

Sample QR Code:
EEXIT00101 Ambulance Parking Exit Floor 1 Location:
![EEXIT00101 Ambulance Parking Exit Floor 1 Location](src/main/resources/edu/wpi/cs3733/D21/teamE/QRcode/EEXIT00101%20Ambulance%20Parking%20Exit%20Floor%201.png)
Parking Slot #1 Register:
![Parking Slot #1 Register](src/main/resources/edu/wpi/cs3733/D21/teamE/QRcode/qr-code.png)

### Usable User Accounts

> admin: `admin admin`

> doctor: `staff staff`

> patient: `patient patient`

> visitor: `visitor visitor`

Rest of accounts contained in `CSVs/out/USERACCOUNT.csv`

### User Management System

To use our user management system, please use a valid email account to log in to the application to test any service request, appointment request, and ToDo request functionalities. This will allow you to receive the confirmation and reminder emails we have set up.

> Username: wwong2@wpi.edu
> Password: admin

If you choose a reminder option for the appointment or ToDo request functionality, a pop up will launch asking you to log into the Gmail account. If you click “Ok”, please follow the steps below:

1. Console will display a link, please open it (the window will open automatically)
2. In the Google Sign-In page, click “Use another account”
3. Log into Google with the following username and password:
   
> Username: engineeringsoftware3733@gmail.com Password: SoftEngCS3733

4. A warning page will be displayed saying “Google has not verified this app”
5. Click the greyed-out link “Advanced” in the bottom left
6. Click “Go to QuickStart (unsafe)” link
7. A popup will be displayed asking to grant Quickstart permission
8. Click “Allow”
9. Click blue “Allow” button
10. Go back to the App being run and Login with email: `wwong@wpi.edu` Password: `admin`
