import java.util.*;

class ScheduleRules {
	private Map<String, List<String[]>> restrictions;

	public ScheduleRules() {
		this.restrictions = new HashMap<>();
	}

	public void addRule(String day, String startTime, String endTime) {
		restrictions.computeIfAbsent(day, k -> new ArrayList<>()).add(new String[] { startTime, endTime });
	}

	public boolean isRestricted(String day, String time) {
		if (!restrictions.containsKey(day))
			return false;
		for (String[] range : restrictions.get(day)) {
			if (time.compareTo(range[0]) >= 0 && time.compareTo(range[1]) < 0) {
				return true;
			}
		}
		return false;
	}
}
