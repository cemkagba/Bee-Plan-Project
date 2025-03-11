import java.io.*;
import java.util.*;

class Scheduler {
	private List<Course> courses;
	private List<Instructor> instructors;
	private ScheduleRules rules;
	private Map<String, Map<String, String>> schedule;

	public Scheduler(List<Course> courses, List<Instructor> instructors, ScheduleRules rules) {
		this.courses = courses;
		this.instructors = instructors;
		this.rules = rules;
		this.schedule = new HashMap<>();
	}

	public void generateSchedule() {
		List<String> days = List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
		int currentDayIndex = 0;

		for (Course course : courses) {
			boolean scheduled = false;

			for (int i = 0; i < days.size(); i++) {
				String day = days.get((currentDayIndex + i) % days.size());
				if (course.getPracticalHours() > 0 && course.isLargeLab()) {
					scheduled = scheduleCourseComponentForDay(course, day, "practical", course.getPracticalHours());
				} else {
					scheduled = scheduleCourseComponentForDay(course, day, "theoretical", course.getTheoreticalHours());
				}

				if (scheduled) {
					currentDayIndex = (currentDayIndex + 1) % days.size(); 
					break;
				}
			}

			if (!scheduled) {
				System.out.println("Cannot schedule course: " + course.getName());
			}
		}
	}

	private boolean scheduleLargeLab(Course course) {
		boolean scheduled = false;
		int hoursPerGroup = course.getPracticalHours() / 2;

		scheduled = scheduleCourseComponent(course, "practical", 1, hoursPerGroup);
		if (!scheduled)
			return false;

		return scheduleCourseComponent(course, "practical", 2, hoursPerGroup);
	}

	private boolean scheduleCourse(Course course) {

		return scheduleCourseComponent(course, "theoretical", 0, course.getTheoreticalHours());
	}

	private boolean scheduleCourseComponent(Course course, String type, int group, int hours) {
		boolean scheduled = false;

		for (String day : List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")) {
			for (int startTime = 800; startTime < 1700; startTime += 100) {
				String formattedTime = formatTime(startTime);
				if (rules.isRestricted(day, formattedTime))
					continue;

				Instructor instructor = course.getInstructor();
				if (instructor.canTeach(day, startTime, hours)) {

					if (isTimeSlotOccupied(day, formattedTime)) {
						continue;
					}

					String groupName = group == 1 ? "Group 1" : group == 2 ? "Group 2" : "N/A";
					assignCourse(course, instructor, day, formattedTime, groupName, type);
					scheduled = true;
					break;
				}
			}
			if (scheduled)
				break;
		}
		return scheduled;
	}

	private boolean scheduleCourseComponentForDay(Course course, String day, String type, int hours) {
		for (int startTime = 800; startTime < 1700; startTime += 100) {
			String formattedTime = formatTime(startTime);
			if (rules.isRestricted(day, formattedTime))
				continue;

			if (!isTimeSlotOccupied(day, formattedTime) && course.getInstructor().canTeach(day, startTime, hours)) {
				assignCourse(course, course.getInstructor(), day, formattedTime, "N/A", type);
				return true;
			}
		}
		return false;
	}

	private boolean isTimeSlotOccupied(String day, String time) {
		Map<String, String> daySchedule = schedule.getOrDefault(day, new HashMap<>());
		return daySchedule.containsKey(time);
	}

	private void assignCourse(Course course, Instructor instructor, String day, String time, String group,
			String type) {
		schedule.computeIfAbsent(day, k -> new HashMap<>());
		schedule.get(day).put(time,
				instructor.getDepartment() + " - " + course.getName() + " (" + type + " - " + group + ")");
		instructor.assignSchedule(day, Integer.parseInt(time.replace(":", "")), course.getPracticalHours());
		System.out.println("Scheduled " + course.getName() + " (" + type + " - " + group + ") with "
				+ instructor.getName() + " on " + day + " at " + time);
	}

	private String formatTime(int time) {
		int hours = time / 100;
		int minutes = time % 100;
		return String.format("%02d:%02d", hours, minutes);
	}

	public void saveScheduleToFile(String fileName) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			writer.write("Day\tTime\tCourse\n");
			for (String day : schedule.keySet()) {
				for (String time : schedule.get(day).keySet()) {
					writer.write(day + "\t" + time + "\t" + schedule.get(day).get(time) + "\n");
				}
			}
		}
	}
}
