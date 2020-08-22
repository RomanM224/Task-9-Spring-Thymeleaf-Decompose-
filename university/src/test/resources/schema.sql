DROP TABLE IF EXISTS groups CASCADE;

CREATE TABLE groups(
	id IDENTITY  PRIMARY KEY,
	name VARCHAR(10));
	
INSERT INTO groups (id, name) VALUES (DEFAULT, 'AK-27');
INSERT INTO groups (id, name) VALUES (DEFAULT, 'FJ-52');
INSERT INTO groups (id, name) VALUES (DEFAULT, 'AE-49');

DROP TABLE IF EXISTS students CASCADE;

CREATE TABLE students(
	id IDENTITY PRIMARY KEY,
	first_name VARCHAR(30),
	last_name VARCHAR(30));
	
INSERT INTO students (id, first_name, last_name) VALUES (DEFAULT, 'Brianne', 'Wetterlund');
INSERT INTO students (id, first_name, last_name) VALUES (DEFAULT, 'Kate', 'Watson');
INSERT INTO students (id, first_name, last_name) VALUES (DEFAULT, 'Josh', 'Lawrence');
INSERT INTO students (id, first_name, last_name) VALUES (DEFAULT, 'Kate', 'Ross');
INSERT INTO students (id, first_name, last_name) VALUES (DEFAULT, 'Sophie', 'Middleditch');
INSERT INTO students (id, first_name, last_name) VALUES (DEFAULT, 'Matt', 'Brener');
INSERT INTO students (id, first_name, last_name) VALUES (DEFAULT, 'Martin', 'Shipp');
INSERT INTO students (id, first_name, last_name) VALUES (DEFAULT, 'Alice', 'Nanjiani');
INSERT INTO students (id, first_name, last_name) VALUES (DEFAULT, 'Emily', 'Connelly');
INSERT INTO students (id, first_name, last_name) VALUES (DEFAULT, 'Leonardo', 'Miller');

DROP TABLE IF EXISTS courses CASCADE;

CREATE TABLE courses(
	id IDENTITY PRIMARY KEY,
	name VARCHAR(50),
	description VARCHAR(300));
	
INSERT INTO courses (id, name, description) VALUES (DEFAULT, 'Database', 'Database description');
INSERT INTO courses (id, name, description) VALUES (DEFAULT, 'Computer science', 'Computer science description');
INSERT INTO courses (id, name, description) VALUES (DEFAULT, 'Geography', 'Geography description');

DROP TABLE IF EXISTS auditories CASCADE;

CREATE TABLE IF NOT EXISTS auditories(
	id IDENTITY PRIMARY KEY,
	number INT);
	
INSERT INTO auditories (id, number) VALUES (DEFAULT, 100);
INSERT INTO auditories (id, number) VALUES (DEFAULT, 101);
INSERT INTO auditories (id, number) VALUES (DEFAULT, 102);

DROP TABLE IF EXISTS time_tables CASCADE;

CREATE TABLE IF NOT EXISTS time_tables(
	id IDENTITY PRIMARY KEY,
	start_time TIME,
	finish_time TIME);
	
INSERT INTO time_tables(id, start_time, finish_time) VALUES (DEFAULT, PARSEDATETIME('09:00', 'HH:mm'), PARSEDATETIME('10:20', 'HH:mm'));
INSERT INTO time_tables(id, start_time, finish_time) VALUES (DEFAULT, PARSEDATETIME('10:40', 'HH:mm'), PARSEDATETIME('12:00', 'HH:mm'));
INSERT INTO time_tables(id, start_time, finish_time) VALUES (DEFAULT, PARSEDATETIME('12:20', 'HH:mm'), PARSEDATETIME('13:40', 'HH:mm'));
INSERT INTO time_tables(id, start_time, finish_time) VALUES (DEFAULT, PARSEDATETIME('14:00', 'HH:mm'), PARSEDATETIME('15:20', 'HH:mm'));

DROP TABLE IF EXISTS teachers CASCADE;

CREATE TABLE IF NOT EXISTS teachers(
	id IDENTITY PRIMARY KEY,
	first_name VARCHAR(30),
	last_name VARCHAR(30));
	
INSERT INTO teachers (id, first_name, last_name) VALUES (DEFAULT, 'Zoe', 'Barnes');
INSERT INTO teachers (id, first_name, last_name) VALUES (DEFAULT, 'Peter', 'Russo');
INSERT INTO teachers (id, first_name, last_name) VALUES (DEFAULT, 'Lucas', 'Goodwin');

DROP TABLE IF EXISTS lessons CASCADE;

CREATE TABLE IF NOT EXISTS lessons(
	id SERIAL PRIMARY KEY,
	course_id INT,
	group_id INT,
	teacher_id INT,
	auditory_id INT,
	time_table_id INT,
	date DATE,
	FOREIGN KEY (course_id) REFERENCES courses (id),
	FOREIGN KEY (group_id) REFERENCES groups (id),
	FOREIGN KEY (teacher_id) REFERENCES teachers (id),
	FOREIGN KEY (auditory_id) REFERENCES auditories (id),
	FOREIGN KEY (time_table_id) REFERENCES time_tables (id));

INSERT INTO lessons (id, course_id, group_id, teacher_id, auditory_id, time_table_id, date) 
VALUES (DEFAULT, 1, 1, 1, 1, 1, PARSEDATETIME('01-09-2020', 'dd-MM-yyyy'));
INSERT INTO lessons (id, course_id, group_id, teacher_id, auditory_id, time_table_id, date) 
VALUES (DEFAULT, 2, 1, 2, 2, 3, PARSEDATETIME('01-09-2020', 'dd-MM-yyyy'));
INSERT INTO lessons (id, course_id, group_id, teacher_id, auditory_id, time_table_id, date) 
VALUES (DEFAULT, 3, 2, 3, 3, 2, PARSEDATETIME('02-09-2020', 'dd-MM-yyyy'));
INSERT INTO lessons (id, course_id, group_id, teacher_id, auditory_id, time_table_id, date) 
VALUES (DEFAULT, 2, 2, 2, 1, 1, PARSEDATETIME('02-09-2020', 'dd-MM-yyyy'));
INSERT INTO lessons (id, course_id, group_id, teacher_id, auditory_id, time_table_id, date) 
VALUES (DEFAULT, 1, 3, 1, 2, 4, PARSEDATETIME('03-07-2020', 'dd-MM-yyyy'));
INSERT INTO lessons (id, course_id, group_id, teacher_id, auditory_id, time_table_id, date) 
VALUES (DEFAULT, 3, 3, 3, 2, 2, PARSEDATETIME('03-08-2020', 'dd-MM-yyyy'));

DROP TABLE IF EXISTS students_group;

CREATE TABLE IF NOT EXISTS students_group(
	student_id INT,
	group_id INT,
	FOREIGN KEY (student_id) REFERENCES students (id),
	FOREIGN KEY (group_id) REFERENCES groups (id));
	
INSERT INTO students_group(student_id, group_id) VALUES (1, 1);
INSERT INTO students_group(student_id, group_id) VALUES (2, 2);
INSERT INTO students_group(student_id, group_id) VALUES (3, 3);
INSERT INTO students_group(student_id, group_id) VALUES (4, 1);
INSERT INTO students_group(student_id, group_id) VALUES (5, 2);
INSERT INTO students_group(student_id, group_id) VALUES (6, 3);
INSERT INTO students_group(student_id, group_id) VALUES (7, 1);
INSERT INTO students_group(student_id, group_id) VALUES (8, 2);
INSERT INTO students_group(student_id, group_id) VALUES (9, 3);
INSERT INTO students_group(student_id, group_id) VALUES (10, 1);

DROP TABLE IF EXISTS courses_group;

CREATE TABLE IF NOT EXISTS courses_group(
	course_id INT,
	group_id INT,
	FOREIGN KEY (course_id) REFERENCES courses (id),
	FOREIGN KEY (group_id) REFERENCES groups (id));
	
INSERT INTO courses_group (course_id, group_id) VALUES (1, 1);
INSERT INTO courses_group (course_id, group_id) VALUES (2, 1);
INSERT INTO courses_group (course_id, group_id) VALUES (2, 2);
INSERT INTO courses_group (course_id, group_id) VALUES (3, 2);
INSERT INTO courses_group (course_id, group_id) VALUES (1, 3);
INSERT INTO courses_group (course_id, group_id) VALUES (3, 3);

DROP TABLE IF EXISTS courses_teacher;

CREATE TABLE IF NOT EXISTS courses_teacher(
	course_id INT,
	teacher_id INT,
	FOREIGN KEY (course_id) REFERENCES courses (id),
	FOREIGN KEY (teacher_id) REFERENCES teachers (id));

INSERT INTO courses_teacher (course_id, teacher_id) VALUES (1, 1);
INSERT INTO courses_teacher (course_id, teacher_id) VALUES (2, 2);
INSERT INTO courses_teacher (course_id, teacher_id) VALUES (3, 3);


	