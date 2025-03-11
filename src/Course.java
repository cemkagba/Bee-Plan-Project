	class Course {
		private String name;
		private int theoreticalHours;
		private int practicalHours;
		private boolean isLab;
		private Instructor instructor;
		private int labCapacity;
	
		public Course(String name, int theoreticalHours, int practicalHours, boolean isLab, Instructor instructor,
				int labCapacity) {
			this.name = name;
			this.theoreticalHours = theoreticalHours;
			this.practicalHours = practicalHours;
			this.isLab = isLab;
			this.instructor = instructor;
			this.labCapacity = labCapacity;
		}
	
		public String getName() {
			return name;
		}
	
		public int getTheoreticalHours() {
			return theoreticalHours;
		}
	
		public int getPracticalHours() {
			return practicalHours;
		}
	
		public boolean isLab() {
			return isLab;
		}
	
		public Instructor getInstructor() {
			return instructor;
		}
	
		public int getLabCapacity() {
			return labCapacity;
		}
	
		public boolean isLargeLab() {
			return labCapacity > 40;
		}
	}
