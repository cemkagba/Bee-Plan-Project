import java.io.*;
import java.util.*;

public class Main {
	public static void main(String[] args) throws IOException {

		List<Instructor> instructors = readInstructors(
				"C:\\Users\\cemka\\eclipse-workspace\\Bee Plan Project\\instructors.txt");

		List<Course> courses = readCourses("C:\\Users\\cemka\\eclipse-workspace\\Bee Plan Project\\courses.txt",
				instructors);

		ScheduleRules rules = readRules("C:\\Users\\cemka\\eclipse-workspace\\Bee Plan Project\\rules.txt");

		Scheduler scheduler = new Scheduler(courses, instructors, rules);

		scheduler.generateSchedule();

		scheduler.saveScheduleToFile("C:\\Users\\cemka\\eclipse-workspace\\Bee Plan Project\\schedule.txt");

		System.out.println("Schedule saved to schedule.txt");
	}

	public static List<Instructor> readInstructors(String fileName) throws IOException {
		List<Instructor> instructors = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = reader.readLine()) != null) {
				
				String[] parts = line.split(",");

				
				if (parts.length == 2) {
					String name = parts[0].trim(); 
					String department = parts[1].trim(); 

					
					instructors.add(new Instructor(name, department));
				} else {
					
					System.out.println("Skipping invalid line: " + line);
				}
			}
		}
		return instructors;
	}

	public static List<Course> readCourses(String fileName, List<Instructor> instructors) throws IOException {
		List<Course> courses = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				
				if (line.isEmpty()) {
					continue;
				}

				
				String[] parts = line.split(",");

			
				if (parts.length == 6) {
					try {
						String name = parts[0].trim(); 
						int theoreticalHours = Integer.parseInt(parts[1].trim()); 
						int practicalHours = Integer.parseInt(parts[2].trim());
						boolean isLab = Boolean.parseBoolean(parts[3].trim()); 
						String instructorName = parts[4].trim(); 
						int labCapacity = Integer.parseInt(parts[5].trim()); 

						
						Instructor instructor = instructors.stream().filter(i -> i.getName().equals(instructorName))
								.findFirst().orElseThrow(
										() -> new IllegalArgumentException("Instructor not found: " + instructorName));

						
						courses.add(new Course(name, theoreticalHours, practicalHours, isLab, instructor, labCapacity));
					} catch (NumberFormatException e) {
						
						System.out.println("Skipping invalid line in courses file (invalid number format): " + line);
					}
				} else {
					
					System.out.println("Skipping invalid line in courses file: " + line);
				}
			}
		}
		return courses;
	}

	public static ScheduleRules readRules(String fileName) throws IOException {
		ScheduleRules rules = new ScheduleRules();
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				String day = parts[0];
				String startTime = parts[1];
				String endTime = parts[2];
				rules.addRule(day, startTime, endTime);
			}
		}
		return rules;
	}
}
