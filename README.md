# C195_Software2 Patient/Client Portal
- Michael Evans
- Student ID: 010539989
- Version 0.0.1

# Technical Details
- IDE: Idea-Community- 4:2022.3.2-1
- JDK: JAVA SE corretto-17.0.5
- JAVAFX: JavaFX-SDK-17.0.2
- mysql-connector-j-8.0.32

# Application Start:
- Connect the associated database. 
- Press shift-f10 within Idea (Linux) to run the program.
- Details on screens can be found below. 

# Application overview:
- Login screen
- Customer view
- Appointment view
- Reports

## Login Screen:
- Users will verify here and move to the Customer view upon successful login.
- This form is available in both English and French.
## Customer view:
- Users will be able to add, modify, and delete customers from this page.
- Users can navigate to available appointments from this page.
## Appointment view:
- Users will be able to add, modify, and delete appointments from this page.
- Users can sort the appointments by month, week, or see all available appointments.
- A combo box is located in the bottom corner where the user can choose which reports they would like to generate.
## Type/Month report view:
- Combo boxes populated with types and months can be selected to determine total appointment counts for those categories.
## Schedule By Contact view:
- A combo box is located in the top corner where you can choose the name of a contact. Once generated this report will show all appointments tied to this contact.
## Additional Reports:
- I've chosen to generate an additional schedule that will display all appointments that are scheduled before lunchtime. (or Tee time depending on whom the doctor is)
- This will automatically populate a tableview with appointment scheduled for the current day before 12:00.
