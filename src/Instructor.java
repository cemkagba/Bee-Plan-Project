import java.util.*;

class Instructor {
	private String name;
	private String department; 
	private Map<String, List<Integer>> schedule; 

	public Instructor(String name, String department) {
		this.name = name;
		this.department = department;
		this.schedule = new HashMap<>();
	}

	public String getName() {
		return name;
	}

	public String getDepartment() {
		return department;
	}

	public boolean canTeach(String day, int startHour, int hours) {
		List<Integer> assignedHours = schedule.getOrDefault(day, new ArrayList<>());
		return assignedHours.size() + hours <= 4; }

	public void assignSchedule(String day, int startHour, int hours) {
		schedule.computeIfAbsent(day, k -> new ArrayList<>());
		for (int i = 0; i < hours; i++) {
			schedule.get(day).add(startHour + i);
		}
	}
}
