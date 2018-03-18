# Kämp hotel task manager
Java EE Web application for managing tasks for hotel employees

Metropolia course
Application Development Methods TX00CK90-3003


## [The website (Hosted on Metropolia UAS server)](http://10.114.32.46:8080/management/login)

## Team VietFi

**Members:** 
+ Olli Vilmi (ollivilmi)
+ Pyry Kanerva (PyryKa / pyka / Pyrt)
+ Phuoc Nguyen (rangigo)
+ An Nguyen (AnNguyen1997)
+ Cuong Phan (phthcuong19)


## [Java back end](https://github.com/ollivilmi/hotel_app/tree/master/src/java)
### Application logic

**NotesBean**
- Creating, deleting, searching and editing notes
- Used in [NotesUpload (Servlet)](https://github.com/ollivilmi/hotel_app/blob/master/src/java/Servlets/NotesUpload.java) to upload notes
- Used in [NotesResources (REST)](https://github.com/ollivilmi/hotel_app/blob/master/src/java/service/NotesResources.java) to search, edit & delete

**UserBean**
- Creating users, updating users, searching for users
- Used in checking user for Login & Register [Servlets](https://github.com/ollivilmi/hotel_app/tree/master/src/java/Servlets)
- Used in checking for user when accessing the filtered urls
- Updating user [Update (Servlet)](https://github.com/ollivilmi/hotel_app/blob/master/src/java/Servlets/Update.java)

### Login system
- Uses [JBCrypt library](https://www.mindrot.org/projects/jBCrypt/) to one way hash passwords
- [Login (Servlet)](https://github.com/ollivilmi/hotel_app/blob/master/src/java/Servlets/Login.java) to handle login
- [Logout (Servlet)](https://github.com/ollivilmi/hotel_app/blob/master/src/java/Servlets/Logout.java) to handle logout

**User filters**
- Filters content for non authorized users [EmployeeFilter](https://github.com/ollivilmi/hotel_app/blob/master/src/java/Login/EmployeeFilter.java)
- Filters content for users who don't have manager permissions [ManagerFilter](https://github.com/ollivilmi/hotel_app/blob/master/src/java/Login/ManagerFilter.java)

### Database
- Uses JPA entities which have been automatically generated from the MariaDB database
- [Models](https://github.com/ollivilmi/hotel_app/tree/master/src/java/Models)

### REST API
- [Notes](https://github.com/ollivilmi/hotel_app/blob/master/src/java/service/NotesResources.java)
- [Users](https://github.com/ollivilmi/hotel_app/blob/master/src/java/service/UserResources.java)
- [Jobs](https://github.com/ollivilmi/hotel_app/blob/master/src/java/service/JobResources.java)

## MariaDB SQL scripts
- [Scripts](https://github.com/ollivilmi/hotel_app/tree/master/web/sql)

## Front-end
### HTML
- [Login](https://github.com/ollivilmi/hotel_app/blob/master/web/login.html) 
- [Main page](https://github.com/ollivilmi/hotel_app/blob/master/web/secure/main.html)
- [Profile page](https://github.com/ollivilmi/hotel_app/blob/master/web/secure/profilepage.html)
- [Note manager](https://github.com/ollivilmi/hotel_app/blob/master/web/secure/manager/notemanager.html)

### Scripts
- [Login scripts](https://github.com/ollivilmi/hotel_app/blob/master/web/assets/scripts/loginscript.js)
- [Main, profilepage, notemanager scripts](https://github.com/ollivilmi/hotel_app/tree/master/web/secure/scripts)
