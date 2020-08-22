DROP TABLE IF EXISTS groups CASCADE;

CREATE TABLE IF NOT EXISTS groups(
	id SERIAL PRIMARY KEY,
	name VARCHAR(10));

DROP TABLE IF EXISTS students CASCADE;

CREATE TABLE IF NOT EXISTS students(
	id SERIAL PRIMARY KEY,
	first_name VARCHAR(30),
	last_name VARCHAR(30));

DROP TABLE IF EXISTS courses CASCADE;

CREATE TABLE IF NOT EXISTS courses(
	id SERIAL PRIMARY KEY,
	name VARCHAR(50),
	description VARCHAR(300));
	
DROP TABLE IF EXISTS auditories CASCADE;
	
CREATE TABLE IF NOT EXISTS auditories(
	id SERIAL PRIMARY KEY,
	number INTEGER);
	
DROP TABLE IF EXISTS time_tables CASCADE;

CREATE TABLE IF NOT EXISTS time_tables(
	id SERIAL PRIMARY KEY,
	start_time TIME,
	finish_time TIME);
	
DROP TABLE IF EXISTS teachers CASCADE;

CREATE TABLE IF NOT EXISTS teachers(
	id SERIAL PRIMARY KEY,
	first_name VARCHAR(30),
	last_name VARCHAR(30));
	
DROP TABLE IF EXISTS lessons CASCADE;

CREATE TABLE IF NOT EXISTS lessons(
	id SERIAL PRIMARY KEY,
	course_id INTEGER,
	group_id INTEGER,
	teacher_id INTEGER,
	auditory_id INTEGER,
	time_table_id INTEGER,
	date DATE,
	FOREIGN KEY (course_id) REFERENCES courses (id),
	FOREIGN KEY (group_id) REFERENCES groups (id),
	FOREIGN KEY (teacher_id) REFERENCES teachers (id),
	FOREIGN KEY (auditory_id) REFERENCES auditories (id),
	FOREIGN KEY (time_table_id) REFERENCES time_tables (id));

DROP TABLE IF EXISTS students_group;

CREATE TABLE IF NOT EXISTS students_group(
	student_id INTEGER,
	group_id INTEGER,
	FOREIGN KEY (student_id) REFERENCES students (id),
	FOREIGN KEY (group_id) REFERENCES groups (id));
	
	
DROP TABLE IF EXISTS courses_group;

CREATE TABLE IF NOT EXISTS courses_group(
	course_id INTEGER,
	group_id INTEGER,
	FOREIGN KEY (course_id) REFERENCES courses (id),
	FOREIGN KEY (group_id) REFERENCES groups (id));
	
DROP TABLE IF EXISTS courses_teacher;

CREATE TABLE IF NOT EXISTS courses_teacher(
	course_id INTEGER,
	teacher_id INTEGER,
	FOREIGN KEY (course_id) REFERENCES courses (id),
	FOREIGN KEY (teacher_id) REFERENCES teachers (id));

