drop table if exists StudentCourses;
drop table if exists Courses;
drop table if exists Faculty;
drop table if exists Student;
create table Student(StudentSSN int not null, Name varchar(45) null, primary key(StudentSSN));
create table Courses(Number int not null, CourseName varchar(45) null, Faculty_FacSSN int not null, primary key(Number, Faculty_FacSSN));
create table Faculty(FacSSN int not null, Name varchar(45), primary key(FacSSN));